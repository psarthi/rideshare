package com.digitusrevolution.rideshare.billing.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.billing.domain.core.VirtualAccountDO;
import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.model.billing.domain.core.Account;

public class AccountBusinessService {
	
	private static final Logger logger = LogManager.getLogger(AccountBusinessService.class.getName());

	public int create(Account account) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		int number = 0;
		try {
			transaction = session.beginTransaction();

			VirtualAccountDO accountDO = new VirtualAccountDO();	
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

}
