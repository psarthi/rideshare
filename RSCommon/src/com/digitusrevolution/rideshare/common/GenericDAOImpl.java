package com.digitusrevolution.rideshare.common;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;

public class GenericDAOImpl<T> implements GenericDAO<T> {

	private final Class<T> entityClass;

	public GenericDAOImpl(Class<T> entityClass){
		this.entityClass = entityClass;
	}

	@Override
	public int create(T entity) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		int id = (int) session.save(entity);
		return id;
	}

	@Override
	public T get(int id) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		T entity = null;
		entity = session.get(entityClass,id);
		return entity;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T update(T entity) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		entity = (T) session.merge(entity);
		return entity;
	}

	@Override
	public void delete(T entity) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.delete(entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getAll() {	
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		List<T> entityList = null;

		/*			
		Query query = session.createQuery("from "+entityClass.getName());
		entityList = query.list();
		 */			
		
		Criteria criteria = session.createCriteria(entityClass);
		entityList = criteria.list();			
		return entityList;
	}
}
