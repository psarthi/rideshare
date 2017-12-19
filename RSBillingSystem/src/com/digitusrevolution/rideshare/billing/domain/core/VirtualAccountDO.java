package com.digitusrevolution.rideshare.billing.domain.core;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.exception.InSufficientBalanceException;
import com.digitusrevolution.rideshare.common.inf.DomainObjectPKInteger;
import com.digitusrevolution.rideshare.common.inf.GenericDAO;
import com.digitusrevolution.rideshare.common.mapper.billing.core.AccountMapper;
import com.digitusrevolution.rideshare.model.billing.data.core.AccountEntity;
import com.digitusrevolution.rideshare.model.billing.domain.core.Account;
import com.digitusrevolution.rideshare.model.billing.domain.core.Transaction;
import com.digitusrevolution.rideshare.model.billing.domain.core.TransactionType;

//Need to implement Account Interface which is standard for any AccountDO implementation so that all type of account has same behavior
public class VirtualAccountDO implements DomainObjectPKInteger<Account>, AccountDO{
	
	private Account account;
	private AccountEntity accountEntity;
	private AccountMapper accountMapper;
	private final GenericDAO<AccountEntity, Integer> genericDAO; 
	private static final Logger logger = LogManager.getLogger(VirtualAccountDO.class.getName());
	
	public VirtualAccountDO() {
		account = new Account();
		accountEntity = new AccountEntity();
		accountMapper = new AccountMapper();
		genericDAO = new GenericDAOImpl<>(AccountEntity.class);
	}

	public void setAccount(Account account) {
		this.account = account;
		accountEntity = accountMapper.getEntity(account, true);
	}

	public void setAccountEntity(AccountEntity accountEntity) {
		this.accountEntity = accountEntity;
		account = accountMapper.getDomainModel(accountEntity, false);
	}

	@Override
	public List<Account> getAll() {
		List<Account> accounts = new ArrayList<>();
		List<AccountEntity> accountEntities = genericDAO.getAll();
		for (AccountEntity accountEntity : accountEntities) {
			setAccountEntity(accountEntity);
			accounts.add(account);
		}
		return accounts;
	}

	@Override
	public void update(Account account) {
		if (account.getNumber()==0){
			throw new InvalidKeyException("Updated failed due to Invalid key: "+account.getNumber());
		}
		setAccount(account);
		genericDAO.update(accountEntity);
	}

	@Override
	public void fetchChild() {
		account = accountMapper.getDomainModelChild(account, accountEntity);
	}

	@Override
	public int create(Account account) {
		setAccount(account);
		int id = genericDAO.create(accountEntity);
		return id;
	}

	@Override
	public Account get(int number) {
		accountEntity = genericDAO.get(number);
		if (accountEntity == null){
			throw new NotFoundException("No Data found with number: "+number);
		}
		setAccountEntity(accountEntity);
		return account;
	}

	@Override
	public Account getAllData(int number) {
		get(number);
		fetchChild();
		return account;
	}

	@Override
	public void delete(int number) {
		account = get(number);
		setAccount(account);
		genericDAO.delete(accountEntity);
	}
	
	@Override
	public void debit(int accountNumber, float amount, String remark){
		//Its important to get child, else old transaction would get deleted as transactions is part of child
		//And if you just get account without old transactions, then it will consider only new transaction as part of this account
		//Since account owns the relationship of transaction, so you need to get all child before updating
		account = getAllData(accountNumber);
		float balance = account.getBalance();
		if (balance >= amount){
			account.setBalance(balance - amount);
			Transaction transaction = new Transaction();
			transaction.setAmount(amount);
			ZonedDateTime dateTime = ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC);
			transaction.setDateTime(dateTime);
			transaction.setType(TransactionType.Debit);
			transaction.setRemark(remark);
			account.getTransactions().add(transaction);
			update(account);
		} else {
			throw new InSufficientBalanceException("Not enough balance in the account. Current balance is:"+balance);			
		}
	}
	
	@Override
	public void credit(int accountNumber, float amount, String remark){
		//Its important to get child, else old transaction would get deleted as transactions is part of child
		//And if you just get account without old transactions, then it will consider only new transaction as part of this account
		//Since account owns the relationship of transaction, so you need to get all child before updating
		account = getAllData(accountNumber);
		float balance = account.getBalance();
		account.setBalance(balance + amount);
		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		ZonedDateTime dateTime = ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC);
		transaction.setDateTime(dateTime);
		transaction.setType(TransactionType.Credit);
		transaction.setRemark(remark);
		account.getTransactions().add(transaction);
		update(account);
	}
	
	public void addMoneyToWallet(int accountNumber, float amount) {
		//TODO Connect with Payment Gateway and on successful transaction, credit to its wallet which is virtual account
		boolean paymentSuccess=true;
		if (paymentSuccess) {
			credit(accountNumber, amount, "Top Up");	
		} else {
			throw new WebApplicationException("Recharge Failed");
		}
	}
	
	public float getBalance(int accountNumber) {
		account = get(accountNumber);
		return account.getBalance();
	}
	
	public void redeemFromWallet(int virtualAccountNumber, int redemptionAccountNumber, float amount) {
		//TODO Connect with payment gateway and on successful transaction, debit money from its wallet which is virtual account
		boolean transferSuccess=true;
		if (transferSuccess) {
			debit(virtualAccountNumber, amount, "Redemption");
		} else {
			throw new WebApplicationException("Redemption Failed");
		}
	}
}







































