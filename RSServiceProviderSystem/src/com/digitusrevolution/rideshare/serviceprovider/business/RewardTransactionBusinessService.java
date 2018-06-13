package com.digitusrevolution.rideshare.serviceprovider.business;

import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.RewardCouponTransaction;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.RewardReimbursementTransaction;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.RewardTransaction;
import com.digitusrevolution.rideshare.serviceprovider.domain.core.RewardCouponTransactionDO;
import com.digitusrevolution.rideshare.serviceprovider.domain.core.RewardReimbursementTransactionDO;

public class RewardTransactionBusinessService {
	
	private static final Logger logger = LogManager.getLogger(RewardTransactionBusinessService.class.getName());
	
	public List<RewardReimbursementTransaction> getRewardReimbursementTransactions(long userId, int page) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		List<RewardReimbursementTransaction> rewardReimbursementTransactions = new LinkedList<RewardReimbursementTransaction>();
		try {
			transaction = session.beginTransaction();

			RewardReimbursementTransactionDO reimbursementTransactionDO = new RewardReimbursementTransactionDO();
			rewardReimbursementTransactions.addAll(reimbursementTransactionDO.getReimbursementTransactions(userId, page));
			
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
		return rewardReimbursementTransactions;	
	}
	
	public List<RewardCouponTransaction> getRewardCouponTransactions(long userId, int page) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		List<RewardCouponTransaction> rewardCouponTransactions = new LinkedList<RewardCouponTransaction>();
		try {
			transaction = session.beginTransaction();

			RewardCouponTransactionDO couponTransactionDO = new RewardCouponTransactionDO();
			rewardCouponTransactions.addAll(couponTransactionDO.getCouponTransactions(userId, page));
			
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
		return rewardCouponTransactions;	
	}
	
	public RewardCouponTransaction getRewardCouponTransaction(int id) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		RewardCouponTransaction couponTransaction = null;
		try {
			transaction = session.beginTransaction();

			RewardCouponTransactionDO couponTransactionDO = new RewardCouponTransactionDO();
			couponTransaction = couponTransactionDO.get(id);
			
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
		return couponTransaction;	
	}
	
	public RewardReimbursementTransaction getRewardReimbursementTransaction(int id) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		RewardReimbursementTransaction reimbursementTransaction = null;
		try {
			transaction = session.beginTransaction();

			RewardReimbursementTransactionDO reimbursementTransactionDO = new RewardReimbursementTransactionDO();
			reimbursementTransaction = reimbursementTransactionDO.get(id);
			
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
		return reimbursementTransaction;	
	}

}
