package com.digitusrevolution.rideshare.billing.domain.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.billing.domain.core.VirtualAccountDO;
import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.inf.DomainService;
import com.digitusrevolution.rideshare.model.billing.domain.core.Account;

public class AccountDomainService implements DomainService<Account>{

	private static final Logger logger = LogManager.getLogger(AccountDomainService.class.getName());

	@Override
	public Account get(int number) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		Account account = null;
		try {
			transation = session.beginTransaction();

			VirtualAccountDO accountDO = new VirtualAccountDO();
			account = accountDO.get(number);			

			transation.commit();
		} catch (RuntimeException e) {
			if (transation!=null){
				logger.error("Transaction Failed, Rolling Back");
				transation.rollback();
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
		Transaction transation = null;	
		List<Account> accounts = null;
		try {
			transation = session.beginTransaction();

			VirtualAccountDO accountDO = new VirtualAccountDO();	
			accounts = accountDO.getAll();
			
			transation.commit();
		} catch (RuntimeException e) {
			if (transation!=null){
				logger.error("Transaction Failed, Rolling Back");
				transation.rollback();
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
}
