package com.digitusrevolution.rideshare.billing.business;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.billing.domain.core.FinancialTransactionDO;
import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.model.billing.dto.paytm.PaytmTransactionResponse;
import com.digitusrevolution.rideshare.model.common.ResponseMessage;

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
	
	public String validatePaytmResponseAndProcessPayment(Map<String, String> paytmResponseHashMap) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		String responseMessage = null;
		try {
			transaction = session.beginTransaction();

			FinancialTransactionDO transactionDO = new FinancialTransactionDO();
			responseMessage = transactionDO.validatePaytmResponseAndProcessPayment(paytmResponseHashMap);			

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
		return responseMessage;
	}
	
	public void cancelFinancialTransaction(long orderId) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			FinancialTransactionDO transactionDO = new FinancialTransactionDO();
			transactionDO.cancelFinancialTransaction(orderId);			

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
	
	public void handlePendingTransactionNotificationFromPaytm(PaytmTransactionResponse paytmTransactionResponse) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			FinancialTransactionDO transactionDO = new FinancialTransactionDO();
			transactionDO.handlePendingTransactionNotificationFromPaytm(paytmTransactionResponse);			

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
		
	public void processAllPendingTopUpOrders() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			FinancialTransactionDO transactionDO = new FinancialTransactionDO();
			transactionDO.processAllPendingTopUpOrders();			

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
	
	public void sendMoneyToUserPayTMWallet(long financialTransactionId) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			FinancialTransactionDO transactionDO = new FinancialTransactionDO();
			transactionDO.sendMoneyToUserPayTMWallet(financialTransactionId);			

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
	
	public void processAllPendingRedemptionOrders() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			FinancialTransactionDO transactionDO = new FinancialTransactionDO();
			transactionDO.processAllPendingRedemptionOrders();			

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
	
	public void processAllInitiatedRedemptionOrders() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			FinancialTransactionDO transactionDO = new FinancialTransactionDO();
			transactionDO.processAllInitiatedRedemptionOrders();			

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









