package com.digitusrevolution.rideshare.billing.business;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.billing.domain.core.BillDO;
import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.util.JsonObjectMapper;
import com.digitusrevolution.rideshare.model.billing.domain.core.Bill;
import com.digitusrevolution.rideshare.model.billing.dto.BillInfo;
import com.digitusrevolution.rideshare.model.billing.dto.TripInfo;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.dto.BasicUser;

public class BillingBusinessService {
	
	private static final Logger logger = LogManager.getLogger(BillingBusinessService.class.getName());
	
	public void approveBill(long billNumber){
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
	
	public void rejectBill(long billNumber){
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

	//We are using BillInfo from future perspective so that we can add more fields if required
	public Bill makePayment(BillInfo billInfo){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		Bill bill = null;
		try {
			transaction = session.beginTransaction();

			BillDO billDO = new BillDO();	
			bill = billDO.makePayment(billInfo.getBillNumber());
			
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
		return bill;
	}
	
	
	public List<Bill> getPendingBills(BasicUser passenger){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		List<Bill> bills = new ArrayList<>();
		try {
			transaction = session.beginTransaction();

			BillDO billDO = new BillDO();	
			bills = billDO.getPendingBills(JsonObjectMapper.getMapper().convertValue(passenger, User.class));
			
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
		return bills;
	}

}





































