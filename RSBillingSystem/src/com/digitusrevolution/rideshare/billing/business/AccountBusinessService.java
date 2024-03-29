package com.digitusrevolution.rideshare.billing.business;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.billing.domain.core.TransactionDO;
import com.digitusrevolution.rideshare.billing.domain.core.VirtualAccountDO;
import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.model.billing.domain.core.FinancialTransaction;
import com.digitusrevolution.rideshare.model.billing.dto.WalletInfo;
import com.digitusrevolution.rideshare.model.ride.dto.BasicRide;

public class AccountBusinessService {

	private static final Logger logger = LogManager.getLogger(AccountBusinessService.class.getName());

	public void addMoneyToWallet(long userId, long accountNumber, float amount) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			VirtualAccountDO virtualAccountDO = new VirtualAccountDO();	
			virtualAccountDO.addMoneyToWallet(userId, accountNumber, amount);

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
	
	public long redeemFromWallet(long userId, float amount) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		long id = 0;
		try {
			transaction = session.beginTransaction();

			VirtualAccountDO virtualAccountDO = new VirtualAccountDO();	
			id = virtualAccountDO.redeemFromWallet(userId, amount); 

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
		return id;
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
	
	
	public com.digitusrevolution.rideshare.model.billing.domain.core.Transaction addRewardToWallet(long userId, long accountNumber, float amount, int rewardReimbursementTransactionId) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		com.digitusrevolution.rideshare.model.billing.domain.core.Transaction walletTransaction = null;
		try {
			transaction = session.beginTransaction();

			VirtualAccountDO virtualAccountDO = new VirtualAccountDO();	
			walletTransaction = virtualAccountDO.addRewardToWallet(userId, accountNumber, amount, rewardReimbursementTransactionId);

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
	
	public WalletInfo getWalletInfo(long userId) {
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		WalletInfo walletInfo = null;
		try {
			transaction = session.beginTransaction();

			VirtualAccountDO virtualAccountDO = new VirtualAccountDO();	
			walletInfo = virtualAccountDO.getWalletInfo(userId);

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
		return walletInfo;
		
	}
}


