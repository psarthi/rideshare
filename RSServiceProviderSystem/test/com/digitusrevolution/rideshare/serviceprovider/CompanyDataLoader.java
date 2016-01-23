package com.digitusrevolution.rideshare.serviceprovider;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

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
import com.digitusrevolution.rideshare.serviceprovider.dto.CompanyAccount;

@Path("/domain/loaddata/serviceprovider")
public class CompanyDataLoader {
	
	private static final Logger logger = LogManager.getLogger(CompanyDataLoader.class.getName());

	@GET
	public static void main(String args[]){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		try {
			transation = session.beginTransaction();
			CompanyDataLoader dataLoader = new CompanyDataLoader();
			dataLoader.loadCompany();
			dataLoader.addAccount();
			
			transation.commit();

			/*
			 * Reason for catching RuntimeException and not HibernateException as all exceptions thrown by Hibernate
			 * is not of type HibernateException such as NotFoundException
			 */
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
	
	public void loadCompany(){
		
		Company company = new Company();
		company.setName("Digitus Revolution");
		Currency currency = RESTClientUtil.getCurrency(1);
		company.setCurrency(currency);
		CompanyDO companyDO = new CompanyDO();
		companyDO.create(company);		
	}
	
	public void addAccount(){
		Account account = RESTClientUtil.getAccount(6);
		CompanyDO companyDO = new CompanyDO();
		companyDO.addAccount(1, account);
	}

}






























