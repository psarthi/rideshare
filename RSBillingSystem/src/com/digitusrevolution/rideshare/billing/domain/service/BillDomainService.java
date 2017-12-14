package com.digitusrevolution.rideshare.billing.domain.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.billing.domain.core.BillDO;
import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.inf.DomainService;
import com.digitusrevolution.rideshare.model.billing.domain.core.Bill;
import com.digitusrevolution.rideshare.model.billing.dto.TripInfo;

public class BillDomainService implements DomainService<Bill>{

	private static final Logger logger = LogManager.getLogger(BillDomainService.class.getName());

	@Override
	public Bill get(int number, boolean fetchChild) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		Bill bill = null;
		try {
			transaction = session.beginTransaction();

			BillDO billDO = new BillDO();
			if (fetchChild){
				bill = billDO.getAllData(number);
			} else {
				bill = billDO.get(number);			
			}

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

	@Override
	public List<Bill> getAll() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		List<Bill> bills = null;
		try {
			transaction = session.beginTransaction();

			BillDO billDO = new BillDO();
			bills = billDO.getAll();

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

	public Bill generateBill(TripInfo tripInfo){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		Bill bill = null;
		try {
			transaction = session.beginTransaction();

			BillDO billDO = new BillDO();	
			int number = billDO.generateBill(tripInfo.getRide(), tripInfo.getRideRequest(), tripInfo.getDiscountPercentage());
			bill = billDO.get(number);
			
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
}
