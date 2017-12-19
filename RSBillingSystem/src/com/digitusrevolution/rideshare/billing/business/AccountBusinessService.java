package com.digitusrevolution.rideshare.billing.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.billing.domain.core.VirtualAccountDO;
import com.digitusrevolution.rideshare.common.db.HibernateUtil;

public class AccountBusinessService {

	private static final Logger logger = LogManager.getLogger(AccountBusinessService.class.getName());

	public void addMoneyToWallet(int accountNumber, float amount) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			VirtualAccountDO virtualAccountDO = new VirtualAccountDO();	
			virtualAccountDO.addMoneyToWallet(accountNumber, amount);

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
	}
}
