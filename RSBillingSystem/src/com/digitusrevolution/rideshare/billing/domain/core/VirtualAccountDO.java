package com.digitusrevolution.rideshare.billing.domain.core;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.billing.data.AccountDAO;
import com.digitusrevolution.rideshare.common.exception.InSufficientBalanceException;
import com.digitusrevolution.rideshare.common.inf.DomainObjectPKLong;
import com.digitusrevolution.rideshare.common.mapper.billing.core.AccountMapper;
import com.digitusrevolution.rideshare.common.mapper.billing.core.TransactionMapper;
import com.digitusrevolution.rideshare.model.billing.data.core.AccountEntity;
import com.digitusrevolution.rideshare.model.billing.data.core.TransactionEntity;
import com.digitusrevolution.rideshare.model.billing.domain.core.Account;
import com.digitusrevolution.rideshare.model.billing.domain.core.Purpose;
import com.digitusrevolution.rideshare.model.billing.domain.core.Remark;
import com.digitusrevolution.rideshare.model.billing.domain.core.Transaction;
import com.digitusrevolution.rideshare.model.billing.domain.core.TransactionType;

//Need to implement Account Interface which is standard for any AccountDO implementation so that all type of account has same behavior
public class VirtualAccountDO implements DomainObjectPKLong<Account>, AccountDO{
	
	private Account account;
	private AccountEntity accountEntity;
	private AccountMapper accountMapper;
	private AccountDAO accountDAO;
	private static final Logger logger = LogManager.getLogger(VirtualAccountDO.class.getName());
	
	public VirtualAccountDO() {
		account = new Account();
		accountEntity = new AccountEntity();
		accountMapper = new AccountMapper();
		accountDAO = new AccountDAO();
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
		List<AccountEntity> accountEntities = accountDAO.getAll();
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
		accountDAO.update(accountEntity);
	}

	@Override
	public void fetchChild() {
		account = accountMapper.getDomainModelChild(account, accountEntity);
	}

	@Override
	public long create(Account account) {
		setAccount(account);
		long id = accountDAO.create(accountEntity);
		return id;
	}

	@Override
	public Account get(long number) {
		accountEntity = accountDAO.get(number);
		if (accountEntity == null){
			throw new NotFoundException("No Data found with number: "+number);
		}
		setAccountEntity(accountEntity);
		return account;
	}

	@Override
	public Account getAllData(long number) {
		get(number);
		fetchChild();
		return account;
	}

	@Override
	public void delete(long number) {
		account = get(number);
		setAccount(account);
		accountDAO.delete(accountEntity);
	}
	
	@Override
	public void debit(long accountNumber, float amount, Remark remark){
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
	public void credit(long accountNumber, float amount, Remark remark){
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
	
	public void addMoneyToWallet(long accountNumber, float amount) {
		//TODO Connect with Payment Gateway and on successful transaction, credit to its wallet which is virtual account
		boolean paymentSuccess=true;
		if (paymentSuccess) {
			Remark remark = new Remark();
			remark.setPurpose(Purpose.TopUp);
			remark.setPaidBy("Self");
			remark.setPaidTo("Self");
			remark.setMessage(Purpose.TopUp.toString());
			credit(accountNumber, amount, remark);	
		} else {
			throw new WebApplicationException("Recharge Failed");
		}
	}
	
	public float getBalance(long accountNumber) {
		account = get(accountNumber);
		return account.getBalance();
	}
	
	public void redeemFromWallet(long virtualAccountNumber, float amount) {
		//TODO Connect with payment gateway and on successful transaction, debit money from its wallet which is virtual account
		boolean transferSuccess=true;
		if (transferSuccess) {
			Remark remark = new Remark();
			remark.setPurpose(Purpose.Redeem);
			remark.setPaidBy("Self");
			remark.setPaidTo("Self");
			remark.setMessage(Purpose.Redeem.toString());
			debit(virtualAccountNumber, amount, remark);
		} else {
			throw new WebApplicationException("Redemption Failed");
		}
	}
	
	//This will get transaction sublist in ascending order
	public List<Transaction> getTransactions(long accountNumber, int page){
		
		//This will help in calculating the index for the result - 0 to 9, 10 to 19, 20 to 29 etc.
		int itemsCount = 10;
		int startIndex = page*itemsCount; 
		List<TransactionEntity> transactionEntities = accountDAO.getTransactions(accountNumber, startIndex);
		TransactionMapper transactionMapper = new TransactionMapper();
		LinkedList<Transaction> transactions = new LinkedList<>();
		transactionMapper.getDomainModels(transactions, transactionEntities, true);
		//this will sort the list further
		Collections.sort(transactions);
		return transactions;
	}
}







































