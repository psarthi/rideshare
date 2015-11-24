package com.digitusrevolution.rideshare.common;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
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
	public int create(T entity) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transation = null;	
		int id = 0;
		try {
			transation = session.beginTransaction();
			id = (int) session.save(entity);
			transation.commit();
		} catch (HibernateException e) {
			if (transation!=null){
				logger.error("Transaction Failed, Rolling Back");
				transation.rollback();
				throw e;
			}
		} finally {
			if (session!=null){
				session.close();
			}
		}
		return id;
	}

	@Override
	public T get(int id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transation = null;
		T entity = null;
		try {
			transation = session.beginTransaction();
			logger.debug("\n entityClass -"+ entityClass + "\n entityClass.getName() -" + entityClass.getName() + 
					"\n entityClass.getClass() -"+entityClass.getClass() + "\n entityClass.getClass().getName() -"+entityClass.getClass().getName() +
					"\n entityClass.getClass().getClass() -"+entityClass.getClass().getClass() + 
					"\n entityClass.getClass().getClass().getName() -"+entityClass.getClass().getClass().getName());
			entity = session.get(entityClass,id);
			transation.commit();
		} catch (HibernateException e) {
			if (transation!=null){
				logger.error("Transaction Failed, Rolling Back");
				transation.rollback();
				throw e;
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
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transation = null;
		try {
			transation = session.beginTransaction();
			session.update(entity);
			transation.commit();
		} catch (HibernateException e) {
			if (transation!=null){
				logger.error("Transaction Failed, Rolling Back");
				transation.rollback();
				throw e;
			}
		} finally {
			if (session!=null){
				session.close();
			}
		}
	}

	@Override
	public void delete(T entity) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transation = null;
		try {
			transation = session.beginTransaction();
			session.delete(entity);
			transation.commit();
		} catch (HibernateException e) {
			if (transation!=null){
				logger.error("Transaction Failed, Rolling Back");
				transation.rollback();
				throw e;
			}
		} finally {
			if (session!=null){
				session.close();
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getAll() {	
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transation = null;
		List<T> entityList = null;
		try {
			transation = session.beginTransaction();
			
/*			Query query = session.createQuery("from "+entityClass.getName());
			entityList = query.list();
*/			
			Criteria criteria = session.createCriteria(entityClass);
			entityList = criteria.list();			
			transation.commit();
		} catch (HibernateException e) {
			if (transation!=null){
				logger.error("Transaction Failed, Rolling Back");
				transation.rollback();
				throw e;
			}
		} finally {
			if (session!=null){
				session.close();
			}
		}
		return entityList;
	}
}
