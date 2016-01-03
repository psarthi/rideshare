package com.digitusrevolution.rideshare.common.db;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;

import com.digitusrevolution.rideshare.common.inf.GenericDAO;

public class GenericDAOImpl<T,ID extends Serializable> implements GenericDAO<T,ID> {

	private final Class<T> entityClass;

	public GenericDAOImpl(Class<T> entityClass){
		this.entityClass = entityClass;
	}

	@Override
	public ID create(T entity) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		@SuppressWarnings("unchecked")
		ID id = (ID) session.save(entity);
		return id;
	}

	@Override
	public T get(ID id) {
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
		/*
		 * This is done to avoid NonUnique exception thrown by Hibernate, when it finds two objects with the same Primary key,
		 * So in case, you first fetch the data based on primary key and then send the data for deletion, you get two objects 
		 * with same primary key in the session, so by merging the object you get one object and then delete function would work
		 * 
		 */
		Object mergedEntity = session.merge(entity);
		session.delete(mergedEntity);
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
