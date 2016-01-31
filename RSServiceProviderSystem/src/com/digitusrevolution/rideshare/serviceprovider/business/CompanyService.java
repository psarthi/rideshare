package com.digitusrevolution.rideshare.serviceprovider.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.Company;
import com.digitusrevolution.rideshare.serviceprovider.domain.core.CompanyDO;
import com.digitusrevolution.rideshare.serviceprovider.dto.CompanyAccount;


public class CompanyService {

	private static final Logger logger = LogManager.getLogger(CompanyService.class.getName());
	
	public void addAccount(CompanyAccount companyAccount){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		try {
			transation = session.beginTransaction();
			
			CompanyDO companyDO = new CompanyDO();
			companyDO.addAccount(companyAccount.getCompanyId(), companyAccount.getAccount());
			
			transation.commit();
		} catch (RuntimeException e) {
			if (transation!=null){
				logger.error("Transaction Failed, Rolling Back");
				transation.rollback();
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
		Transaction transation = null;	
		Company company = null;
		try {
			transation = session.beginTransaction();

			CompanyDO companyDO = new CompanyDO();
			if (fetchChild){
				company = companyDO.getAllData(id);
			} else {
				company = companyDO.get(id);			
			}

			transation.commit();
		} catch (RuntimeException e) {
			if (transation!=null){
				logger.error("Transaction Failed, Rolling Back");
				transation.rollback();
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
