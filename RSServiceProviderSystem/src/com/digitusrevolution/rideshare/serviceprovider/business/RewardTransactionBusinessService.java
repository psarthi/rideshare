package com.digitusrevolution.rideshare.serviceprovider.business;

import java.time.ZonedDateTime;
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
import com.digitusrevolution.rideshare.model.serviceprovider.dto.OfferEligibilityResult;
import com.digitusrevolution.rideshare.model.serviceprovider.dto.ReimbursementRequest;
import com.digitusrevolution.rideshare.serviceprovider.domain.core.OfferDO;
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
	
	public void createRewardReimbursementTransaction(long userId, int offerId, ReimbursementRequest reimbursementRequest) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		int id = 0;
		try {
			transaction = session.beginTransaction();

			RewardReimbursementTransactionDO reimbursementTransactionDO = new RewardReimbursementTransactionDO();
			reimbursementTransactionDO.createReimbursementTransactions(userId, offerId, reimbursementRequest);
			
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
	
	public OfferEligibilityResult getUserEligibilityForOffer(long userId, int rewardReimbursementTransactionId, ZonedDateTime dateTime) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		OfferEligibilityResult offerEligibilityResult = null;
		try {
			transaction = session.beginTransaction();

			RewardReimbursementTransactionDO transactionDO = new RewardReimbursementTransactionDO();
			RewardReimbursementTransaction rewardReimbursementTransaction = transactionDO.get(rewardReimbursementTransactionId);
			OfferDO offerDO = new OfferDO();
			offerEligibilityResult = offerDO.getUserEligibilityForOffer(userId, rewardReimbursementTransaction.getOffer().getId(), dateTime);
			
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
		return offerEligibilityResult;	
	}
	
	
	public RewardReimbursementTransaction approveRewardReimbursementTransaction(int id, int approvedAmount, String remarks) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		RewardReimbursementTransaction reimbursementTransaction = null;
		try {
			transaction = session.beginTransaction();

			RewardReimbursementTransactionDO transactionDO = new RewardReimbursementTransactionDO();
			reimbursementTransaction = transactionDO.approveRewardReimbursementTransaction(id, approvedAmount, remarks);
			
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
	
	public void processPaymentForRewardReimbursementTransaction(int id, com.digitusrevolution.rideshare.model.billing.domain.core.Transaction walletTransaction) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		try {
			transaction = session.beginTransaction();

			RewardReimbursementTransactionDO transactionDO = new RewardReimbursementTransactionDO();
			transactionDO.processPaymentForRewardReimbursementTransaction(id, walletTransaction);
			
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
	
	public RewardCouponTransaction generateCoupon(long userId, int offerId) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		RewardCouponTransaction couponTransaction = null;
		try {
			transaction = session.beginTransaction();

			RewardCouponTransactionDO rewardCouponTransactionDO = new RewardCouponTransactionDO();
			couponTransaction = rewardCouponTransactionDO.generateCoupon(userId, offerId);
			
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
	
	public void redeemCoupon(int id) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			RewardCouponTransactionDO rewardCouponTransactionDO = new RewardCouponTransactionDO();
			rewardCouponTransactionDO.redeemCoupon(id);
			
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
