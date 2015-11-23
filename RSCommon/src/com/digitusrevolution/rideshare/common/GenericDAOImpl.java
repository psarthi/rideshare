package com.digitusrevolution.rideshare.common;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
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
		Session session = null;
		Transaction transation = null;
		int id = 0;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transation = session.beginTransaction();
			id = (int) session.save(entity);
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
		return id;
	}

	@Override
	public T get(int id) {
		Session session = null;
		Transaction transation = null;
		T entity = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transation = session.beginTransaction();
			logger.debug("\n entityClass -"+ entityClass + "\n entityClass.getName() -" + entityClass.getName() + 
					"\n entityClass.getClass() -"+entityClass.getClass() + "\n entityClass.getClass().getName() -"+entityClass.getClass().getName() +
					"\n entityClass.getClass().getClass() -"+entityClass.getClass().getClass() + 
					"\n entityClass.getClass().getClass().getName() -"+entityClass.getClass().getClass().getName());
			entity = session.get(entityClass,id);
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
		Session session = null;
		Transaction transation = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transation = session.beginTransaction();
			session.update(entity);
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
	public void delete(T entity) {
		Session session = null;
		Transaction transation = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transation = session.beginTransaction();
			session.delete(entity);
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
	public List<T> getAll() {	
		Session session = null;
		Transaction transation = null;
		List<T> entityList = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transation = session.beginTransaction();
			
/*			Query query = session.createQuery("from "+entityClass.getName());
			entityList = query.list();
*/			
			Criteria criteria = session.createCriteria(entityClass);
			entityList = criteria.list();			
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
		return entityList;
	}
}
