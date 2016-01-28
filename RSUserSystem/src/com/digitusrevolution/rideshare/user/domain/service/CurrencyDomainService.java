package com.digitusrevolution.rideshare.user.domain.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.inf.DomainService;
import com.digitusrevolution.rideshare.model.user.domain.Currency;
import com.digitusrevolution.rideshare.user.domain.CurrencyDO;

public class CurrencyDomainService implements DomainService<Currency>{

	private static final Logger logger = LogManager.getLogger(CurrencyDomainService.class.getName());

	@Override
	public Currency get(int id, boolean fetchChild) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		Currency currency = null;
		try {
			transation = session.beginTransaction();

			CurrencyDO currencyDO = new CurrencyDO();
			if (fetchChild){
				currency = currencyDO.getChild(id);
			} else {
				currency = currencyDO.get(id);			
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
		return currency;	
	}

	@Override
	public List<Currency> getAll() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		List<Currency> currencies = null;
		try {
			transation = session.beginTransaction();

			CurrencyDO currencyDO = new CurrencyDO();
			currencies = currencyDO.getAll();

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
		return currencies;	
	}

}
