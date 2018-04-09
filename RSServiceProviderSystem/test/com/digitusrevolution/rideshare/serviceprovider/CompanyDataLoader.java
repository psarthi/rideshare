package com.digitusrevolution.rideshare.serviceprovider;

import java.util.Collection;
import java.util.List;

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
import com.digitusrevolution.rideshare.model.user.domain.Country;
import com.digitusrevolution.rideshare.model.user.domain.Currency;
import com.digitusrevolution.rideshare.model.user.domain.State;
import com.digitusrevolution.rideshare.serviceprovider.domain.core.CompanyDO;
import com.digitusrevolution.rideshare.serviceprovider.domain.core.HelpQuestionAnswerDO;

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
			//dataLoader.addAccount();
			
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
		
		CompanyDO companyDO = new CompanyDO();
		//Ensure you get all Data before updating otherwise account details would be deleted
		Company company = companyDO.getAllData(1);
		company.setName("Parift Technologies Private Limited");
		List<Country> countries = (List<Country>) RESTClientUtil.getCountries();
		
		for (Country country: countries) {
			if (country.getName().equals("India")) {
				company.setCountry(country);
				for (State state: country.getStates()) {
					if (state.getId() == 1 ) {
						company.setState(state);
						break;
					}
				}
				break;
			}
		}
		
		Currency currency = RESTClientUtil.getCurrency(1);
		company.setCurrency(currency);
		company.setServiceChargePercentage(10);
		company.setAddress("Villa No. 38, MS Shelters,Kammasandra(A), Kasaba(H),Anekal Taluk, Bangalore, Karnataka, India, 562106");
		company.setGstNumber("29AAJCP6630K1ZX");
		company.setGstCode("999799");
		company.setPan("AAJCP6630K");
		company.setCgstPercentage(9);
		company.setSgstPercentage(9);
		company.setIgstPercentage(18);
		company.setTcsPercentage(0);
		companyDO.update(company);		
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






























