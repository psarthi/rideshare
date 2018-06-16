package com.digitusrevolution.rideshare.serviceprovider.business;

import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.Offer;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.Partner;
import com.digitusrevolution.rideshare.model.serviceprovider.dto.OfferEligibilityResult;
import com.digitusrevolution.rideshare.model.serviceprovider.dto.UserOffer;
import com.digitusrevolution.rideshare.serviceprovider.domain.core.OfferDO;
import com.digitusrevolution.rideshare.serviceprovider.domain.core.PartnerDO;

public class OfferBusinessService {

	private static final Logger logger = LogManager.getLogger(CompanyBusinessService.class.getName());
	
	public Offer get(int id, boolean fetchChild) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		Offer offer = null;
		try {
			transaction = session.beginTransaction();

			OfferDO offerDO = new OfferDO();
			if (fetchChild){
				offer = offerDO.getAllData(id);
			} else {
				offer = offerDO.get(id);			
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
		return offer;	
	}
	
	public List<UserOffer> getAll(long userId, int page) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		List<UserOffer> userOffers = new LinkedList<>();
		try {
			transaction = session.beginTransaction();

			OfferDO offerDO = new OfferDO();
			userOffers = offerDO.getOffers(userId, page);
			
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
		return userOffers;	
	}
	
	public OfferEligibilityResult getUserEligibilityForOffer(long userId, int offerId, ZonedDateTime dateTime) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		OfferEligibilityResult offerEligibilityResult = null;
		try {
			transaction = session.beginTransaction();

			OfferDO offerDO = new OfferDO();
			offerEligibilityResult = offerDO.getUserEligibilityForOffer(userId, offerId, dateTime);
			
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
		return offerEligibilityResult;	
	}
	
	public int createOffer(Offer offer) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		int id = 0;
		try {
			transaction = session.beginTransaction();

			OfferDO offerDO = new OfferDO();
			id = offerDO.createOffer(offer);
			
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
	
	public void addPartnerOffer(int partnerId, Offer offer) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		try {
			transaction = session.beginTransaction();

			OfferDO offerDO = new OfferDO();
			offerDO.addPartnerOffer(partnerId, offer);
			
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
