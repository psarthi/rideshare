package com.digitusrevolution.rideshare.serviceprovider;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.util.RESTClientUtil;
import com.digitusrevolution.rideshare.model.billing.domain.core.Account;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.Company;
import com.digitusrevolution.rideshare.model.user.domain.Currency;
import com.digitusrevolution.rideshare.serviceprovider.domain.core.CompanyDO;

@Path("/domain/loaddata/serviceprovider")
public class CompanyDataLoader {
	
	private static final Logger logger = LogManager.getLogger(CompanyDataLoader.class.getName());

	@GET
	public static void main(String args[]){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		try {
			transaction = session.beginTransaction();
			CompanyDataLoader dataLoader = new CompanyDataLoader();
			dataLoader.loadCompany();
			dataLoader.addAccount();
			
			transaction.commit();

			/*
			 * Reason for catching RuntimeException and not HibernateException as all exceptions thrown by Hibernate
			 * is not of type HibernateException such as NotFoundException
			 */
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
	
	public void loadCompany(){
		
		Company company = new Company();
		company.setName("Digitus Revolution");
		Currency currency = RESTClientUtil.getCurrency(1);
		company.setCurrency(currency);
		company.setServiceChargePercentage(10);
		CompanyDO companyDO = new CompanyDO();
		companyDO.create(company);		
	}
	
	public void addAccount(){
		//This will create virtual account
		Account account = RESTClientUtil.createVirtualAccount();
		//This will take care of exception thrown by the Billing system if any
		if (account==null) {
			throw new WebApplicationException("Unable to create Virtual account for the company");
		}
		CompanyDO companyDO = new CompanyDO();
		companyDO.addAccount(1, account);
	}

}






























