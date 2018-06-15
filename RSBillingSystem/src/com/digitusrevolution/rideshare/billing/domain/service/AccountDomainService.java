package com.digitusrevolution.rideshare.billing.domain.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.billing.domain.core.TransactionDO;
import com.digitusrevolution.rideshare.billing.domain.core.VirtualAccountDO;
import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.inf.DomainServiceInteger;
import com.digitusrevolution.rideshare.common.inf.DomainServiceLong;
import com.digitusrevolution.rideshare.model.billing.domain.core.Account;
import com.digitusrevolution.rideshare.model.billing.domain.core.AccountType;

public class AccountDomainService implements DomainServiceLong<Account>{

	private static final Logger logger = LogManager.getLogger(AccountDomainService.class.getName());

	public long createVirtualAccount() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		long number = 0;
		try {
			transaction = session.beginTransaction();

			VirtualAccountDO accountDO = new VirtualAccountDO();
			Account account = new Account();
			account.setBalance(0);
			account.setType(AccountType.Virtual);
			number = accountDO.create(account);
			
			transaction.commit();
		} catch (RuntimeException e) {
			if (transaction!=null){
				logger.error("Transaction Failed, Rolling Back");
				transaction.rollback();
				throw e;
			}
		}
		finally {
			if (session.isOpen()){
				logger.info("Closing Session");
				session.close();				
			}
		}
		return number;	
	}
	
	@Override
	public Account get(long number, boolean fetchChild) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		Account account = null;
		try {
			transaction = session.beginTransaction();

			VirtualAccountDO accountDO = new VirtualAccountDO();
			if (fetchChild){
				account = accountDO.getAllData(number);
			} else {
				account = accountDO.get(number);			
			}

			transaction.commit();
		} catch (RuntimeException e) {
			if (transaction!=null){
				logger.error("Transaction Failed, Rolling Back");
				transaction.rollback();
				throw e;
			}
		}
		finally {
			if (session.isOpen()){
				logger.info("Closing Session");
				session.close();				
			}
		}
		return account;	
	}

	@Override
	public List<Account> getAll() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		List<Account> accounts = null;
		try {
			transaction = session.beginTransaction();

			VirtualAccountDO accountDO = new VirtualAccountDO();	
			accounts = accountDO.getAll();
			
			transaction.commit();
		} catch (RuntimeException e) {
			if (transaction!=null){
				logger.error("Transaction Failed, Rolling Back");
				transaction.rollback();
				throw e;
			}
		}
		finally {
			if (session.isOpen()){
				logger.info("Closing Session");
				session.close();				
			}
		}
		return accounts;	
	}
	
	public com.digitusrevolution.rideshare.model.billing.domain.core.Transaction getTransaction(long id) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		com.digitusrevolution.rideshare.model.billing.domain.core.Transaction walletTransaction = null;
		try {
			transaction = session.beginTransaction();

			TransactionDO transactionDO = new TransactionDO();	
			walletTransaction = transactionDO.get(id);

			transaction.commit();
		} catch (RuntimeException e) {
			if (transaction!=null){
				logger.error("Transaction Failed, Rolling Back");
				transaction.rollback();
				throw e;
			}
		}
		finally {
			if (session.isOpen()){
				logger.info("Closing Session");
				session.close();				
			}
		}
		return walletTransaction;
	}
}
