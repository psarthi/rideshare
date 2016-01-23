package com.digitusrevolution.rideshare.billing.domain.core;

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
import com.digitusrevolution.rideshare.common.mapper.billing.core.AccountMapper;
import com.digitusrevolution.rideshare.model.billing.data.core.AccountEntity;
import com.digitusrevolution.rideshare.model.billing.domain.core.Account;

public class AccountDO implements DomainObjectPKInteger<Account>{
	
	private Account account;
	private AccountEntity accountEntity;
	private AccountMapper accountMapper;
	private final GenericDAO<AccountEntity, Integer> genericDAO; 
	private static final Logger logger = LogManager.getLogger(AccountDO.class.getName());
	
	public AccountDO() {
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
	public Account getChild(int number) {
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
	
	public void debit(Account account, float amount){
		float balance = account.getBalance();
		if (balance >= amount){
			account.setBalance(balance - amount);
			update(account);
		}
		throw new InSufficientBalanceException("Not enough balance in the account. Current balance is:"+balance);
	}
	
	public void credit(Account account, float amount){
		float balance = account.getBalance();
		account.setBalance(balance + amount);
		update(account);
	}
}







































