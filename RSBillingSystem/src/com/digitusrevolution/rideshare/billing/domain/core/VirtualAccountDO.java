package com.digitusrevolution.rideshare.billing.domain.core;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.exception.InSufficientBalanceException;
import com.digitusrevolution.rideshare.common.inf.DomainObjectPKInteger;
import com.digitusrevolution.rideshare.common.inf.GenericDAO;
import com.digitusrevolution.rideshare.model.billing.data.core.AccountEntity;
import com.digitusrevolution.rideshare.model.billing.domain.core.Account;
import com.digitusrevolution.rideshare.model.billing.domain.core.Transaction;
import com.digitusrevolution.rideshare.model.billing.domain.core.TransactionType;

//Need to implement Account Interface which is standard for any AccountDO implementation so that all type of account has same behavior
public class VirtualAccountDO implements DomainObjectPKInteger<Account>, com.digitusrevolution.rideshare.billing.domain.core.Account{
	
	private Account account;
	private final GenericDAO<AccountEntity, Integer> genericDAO; 
	private static final Logger logger = LogManager.getLogger(VirtualAccountDO.class.getName());
	
	public VirtualAccountDO() {
		account = new Account();
		genericDAO = new GenericDAOImpl<>(AccountEntity.class);
	}

	public void setAccount(Account account) {
		this.account = account;
	}
	
	public Account getAccount() {
		return account;
	}

	@Override
	public List<Account> getAll() {
		List<Account> accounts = new ArrayList<>();
		List<AccountEntity> accountEntities = genericDAO.getAll();
		for (AccountEntity accountEntity : accountEntities) {
			account.setEntity(accountEntity);
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
		genericDAO.update(account.getEntity());
	}

	@Override
	public int create(Account account) {
		setAccount(account);
		int id = genericDAO.create(account.getEntity());
		return id;
	}

	@Override
	public Account get(int number) {
		AccountEntity accountEntity = genericDAO.get(number);
		if (accountEntity == null){
			throw new NotFoundException("No Data found with number: "+number);
		}
		account.setEntity(accountEntity);
		return account;
	}

	@Override
	public void delete(int number) {
		account = get(number);
		setAccount(account);
		genericDAO.delete(account.getEntity());
	}
	
	@Override
	public void debit(int accountNumber, float amount, String remark){
		//And if you just get account without old transactions, then it will consider only new transaction as part of this account
		//Since account owns the relationship of transaction, so you need to get all old transactions before updating
		account = get(accountNumber);
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
		//And if you just get account without old transactions, then it will consider only new transaction as part of this account
		//Since account owns the relationship of transaction, so you need to get all old transactions before updating
		account = get(accountNumber);
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
}







































