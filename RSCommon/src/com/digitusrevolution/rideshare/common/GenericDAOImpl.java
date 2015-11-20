package com.digitusrevolution.rideshare.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class GenericDAOImpl<T> implements GenericDAO<T> {
	
	private SessionFactory sessionFactory;
	private Session session;
	private Transaction transation;
	private static final Logger logger = LogManager.getLogger(GenericDAOImpl.class.getName());

	@Override
	public void create(T entity) {

		try {
			sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
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
	public T get(Integer id) {
		// TODO Auto-generated method stub
		return null;
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
