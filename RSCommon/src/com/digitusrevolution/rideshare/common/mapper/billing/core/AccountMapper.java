package com.digitusrevolution.rideshare.common.mapper.billing.core;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.model.billing.data.core.AccountEntity;
import com.digitusrevolution.rideshare.model.billing.domain.core.Account;

public class AccountMapper implements Mapper<Account, AccountEntity>{

	@Override
	public AccountEntity getEntity(Account account, boolean fetchChild) {
		AccountEntity accountEntity = new AccountEntity();
		accountEntity.setNumber(account.getNumber());
		accountEntity.setBalance(account.getBalance());
		
		if (fetchChild){
			accountEntity = getEntityChild(account, accountEntity);
		}
		
		return accountEntity;
	}

	@Override
	public AccountEntity getEntityChild(Account account, AccountEntity accountEntity) {
		TransactionMapper transactionMapper = new TransactionMapper();
		accountEntity.setTransactions(transactionMapper.getEntities(accountEntity.getTransactions(), account.getTransactions(), true));		
		return accountEntity;
	}

	@Override
	public Account getDomainModel(AccountEntity accountEntity, boolean fetchChild) {
		Account account = new Account();
		account.setNumber(accountEntity.getNumber());
		account.setBalance(accountEntity.getBalance());
		
		if (fetchChild){
			account = getDomainModelChild(account, accountEntity);
		}
		return account;
	}

	@Override
	public Account getDomainModelChild(Account account, AccountEntity accountEntity) {
		TransactionMapper transactionMapper = new TransactionMapper();
		account.setTransactions(transactionMapper.getDomainModels(account.getTransactions(), accountEntity.getTransactions(), true));
		return account;
	}

	@Override
	public Collection<Account> getDomainModels(Collection<Account> accounts, Collection<AccountEntity> accountEntities,
			boolean fetchChild) {
		for (AccountEntity accountEntity : accountEntities) {
			Account account = new Account();
			account = getDomainModel(accountEntity, fetchChild);
			accounts.add(account);
		}
		return accounts;
	}

	@Override
	public Collection<AccountEntity> getEntities(Collection<AccountEntity> accountEntities, Collection<Account> accounts,
			boolean fetchChild) {
		for (Account account : accounts) {
			AccountEntity accountEntity = new AccountEntity();
			accountEntity = getEntity(account, fetchChild);
			accountEntities.add(accountEntity);
		}
		return accountEntities;
	}

}
