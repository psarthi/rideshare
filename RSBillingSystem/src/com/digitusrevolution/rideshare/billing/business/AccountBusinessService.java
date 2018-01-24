package com.digitusrevolution.rideshare.billing.business;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.billing.domain.core.VirtualAccountDO;
import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.model.ride.dto.BasicRide;

public class AccountBusinessService {

	private static final Logger logger = LogManager.getLogger(AccountBusinessService.class.getName());

	public void addMoneyToWallet(long accountNumber, float amount) {
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
	
	public void redeemFromWallet(long accountNumber, float amount) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			VirtualAccountDO virtualAccountDO = new VirtualAccountDO();	
			virtualAccountDO.redeemFromWallet(accountNumber, amount);

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
	
	public List<com.digitusrevolution.rideshare.model.billing.domain.core.Transaction> getTransactions(long accountNumber, int page) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		List<com.digitusrevolution.rideshare.model.billing.domain.core.Transaction> transactions = new ArrayList<>();
		try {
			transaction = session.beginTransaction();

			VirtualAccountDO virtualAccountDO = new VirtualAccountDO();	
			transactions = virtualAccountDO.getTransactions(accountNumber, page);

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
		return transactions;
	}
}


