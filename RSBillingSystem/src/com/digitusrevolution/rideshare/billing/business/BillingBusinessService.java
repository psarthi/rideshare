package com.digitusrevolution.rideshare.billing.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.billing.domain.core.BillDO;
import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.model.billing.dto.BillDTO;
import com.digitusrevolution.rideshare.model.billing.dto.RideDTO;

public class BillingBusinessService {
	
	private static final Logger logger = LogManager.getLogger(BillingBusinessService.class.getName());
	
	public int generateBill(RideDTO rideDTO){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		int number = 0;
		try {
			transaction = session.beginTransaction();

			BillDO billDO = new BillDO();	
			number = billDO.generateBill(rideDTO.getRide(), rideDTO.getRideRequest());
			
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
	
	public void approveBill(int billNumber){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		try {
			transaction = session.beginTransaction();

			BillDO billDO = new BillDO();	
			billDO.approveBill(billNumber);
			
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
	
	public void rejectBill(int billNumber){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		try {
			transaction = session.beginTransaction();

			BillDO billDO = new BillDO();	
			billDO.rejectBill(billNumber);
			
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

	public void makePayment(BillDTO billDTO){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		try {
			transaction = session.beginTransaction();

			BillDO billDO = new BillDO();	
			billDO.makePayment(billDTO.getBillNumber(), billDTO.getAccountType());
			
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





































