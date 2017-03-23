package com.digitusrevolution.rideshare.serviceprovider.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.Company;
import com.digitusrevolution.rideshare.model.serviceprovider.dto.CompanyAccount;
import com.digitusrevolution.rideshare.serviceprovider.domain.core.CompanyDO;


public class CompanyBusinessService {

	private static final Logger logger = LogManager.getLogger(CompanyBusinessService.class.getName());
	
	public void addAccount(CompanyAccount companyAccount){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		try {
			transaction = session.beginTransaction();
			
			CompanyDO companyDO = new CompanyDO();
			companyDO.addAccount(companyAccount.getCompanyId(), companyAccount.getAccount());
			
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
	
	public Company get(int id, boolean fetchChild) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		Company company = null;
		try {
			transaction = session.beginTransaction();

			CompanyDO companyDO = new CompanyDO();
			if (fetchChild){
				company = companyDO.getAllData(id);
			} else {
				company = companyDO.get(id);			
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
		return company;	
	}
}
