package com.digitusrevolution.rideshare.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class GenericDAOImpl<T> implements GenericDAO<T> {
	
	private final Class<T> entityClass;
	private static final Logger logger = LogManager.getLogger(GenericDAOImpl.class.getName());
	
	public GenericDAOImpl(Class<T> entityClass){
		this.entityClass = entityClass;
	}
	
	@Override
	public void create(T entity) {
		Session session = null;
		Transaction transation = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transation = session.beginTransaction();
			session.save(entity);
			transation.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			if (transation!=null){
				logger.error("Transaction Failed, Rolling Back");
				transation.rollback();
			}
		} finally {
			if (session!=null){
				session.close();
			}
		}
	}

	@Override
	public T get(int id) {
		Session session = null;
		Transaction transation = null;
		T entity = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transation = session.beginTransaction();
			logger.info("\n entityClass -"+ entityClass + "\n entityClass.getName() -" + entityClass.getName() + "\n entityClass.getClass() -"+entityClass.getClass() + "\n entityClass.getClass().getName() -"+entityClass.getClass().getName());
			entity = (T) session.get(entityClass,id);
			transation.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			if (transation!=null){
				logger.error("Transaction Failed, Rolling Back");
				transation.rollback();
			}
		} finally {
			if (session!=null){
				session.close();
			}
		}
		return entity;
	}

	@Override
	public void update(T entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(T entity) {
		// TODO Auto-generated method stub

	}
}
