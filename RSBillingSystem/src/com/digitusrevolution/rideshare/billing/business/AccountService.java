package com.digitusrevolution.rideshare.billing.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.billing.domain.core.AccountDO;
import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.model.billing.domain.core.Account;

public class AccountService {
	
	private static final Logger logger = LogManager.getLogger(AccountService.class.getName());

	public int create(Account account) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		int id = 0;
		try {
			transation = session.beginTransaction();

			AccountDO accountDO = new AccountDO();	
			id = accountDO.create(account);
			
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
		return id;	
	}

}
