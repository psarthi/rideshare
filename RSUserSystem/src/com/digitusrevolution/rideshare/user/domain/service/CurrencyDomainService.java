package com.digitusrevolution.rideshare.user.domain.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.inf.DomainServiceInteger;
import com.digitusrevolution.rideshare.model.user.domain.Currency;
import com.digitusrevolution.rideshare.user.domain.CurrencyDO;

public class CurrencyDomainService implements DomainServiceInteger<Currency>{

	private static final Logger logger = LogManager.getLogger(CurrencyDomainService.class.getName());

	@Override
	public Currency get(int id, boolean fetchChild) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		Currency currency = null;
		try {
			transaction = session.beginTransaction();

			CurrencyDO currencyDO = new CurrencyDO();
			if (fetchChild){
				currency = currencyDO.getAllData(id);
			} else {
				currency = currencyDO.get(id);			
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
		return currency;	
	}

	@Override
	public List<Currency> getAll() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		List<Currency> currencies = null;
		try {
			transaction = session.beginTransaction();

			CurrencyDO currencyDO = new CurrencyDO();
			currencies = currencyDO.getAll();

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
		return currencies;	
	}

}
