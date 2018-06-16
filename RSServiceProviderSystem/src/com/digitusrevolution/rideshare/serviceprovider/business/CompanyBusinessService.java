package com.digitusrevolution.rideshare.serviceprovider.business;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.Company;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.HelpQuestionAnswer;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.Partner;
import com.digitusrevolution.rideshare.model.serviceprovider.dto.CompanyAccount;
import com.digitusrevolution.rideshare.serviceprovider.domain.core.CompanyDO;
import com.digitusrevolution.rideshare.serviceprovider.domain.core.HelpQuestionAnswerDO;
import com.digitusrevolution.rideshare.serviceprovider.domain.core.PartnerDO;


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
	
	public List<HelpQuestionAnswer> getAllHelpQuestionAnswer() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		List<HelpQuestionAnswer> helpQuestionAnswers = new ArrayList<>();
		try {
			transaction = session.beginTransaction();

			HelpQuestionAnswerDO helpQuestionAnswerDO = new HelpQuestionAnswerDO();
			helpQuestionAnswers = helpQuestionAnswerDO.getAll();

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
		return helpQuestionAnswers;	
	}
	
	public int createPartner(Partner partner) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		int id = 0;
		try {
			transaction = session.beginTransaction();

			PartnerDO partnerDO = new PartnerDO();
			id = partnerDO.create(partner);
			
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
		return id;	
	}
	
}
