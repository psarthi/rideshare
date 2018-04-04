package com.digitusrevolution.rideshare.billing.business;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.billing.domain.core.FinancialTransactionDO;
import com.digitusrevolution.rideshare.common.db.HibernateUtil;

public class FinancialTransactionBusinessService {
	
	private static final Logger logger = LogManager.getLogger(FinancialTransactionBusinessService.class.getName());
	
	public Map<String, String> getPaytmOrderInfo(long userId, float amount) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		Map<String, String> paramMap = null;
		try {
			transaction = session.beginTransaction();

			FinancialTransactionDO transactionDO = new FinancialTransactionDO();
			paramMap = transactionDO.getPaytmOrderInfo(userId, amount);

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
		return paramMap;
	}
	
}
