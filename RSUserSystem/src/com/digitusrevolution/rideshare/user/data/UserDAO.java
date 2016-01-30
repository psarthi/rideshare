package com.digitusrevolution.rideshare.user.data;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;

public class UserDAO extends GenericDAOImpl<UserEntity,Integer>{

	private static final Class<UserEntity> entityClass = UserEntity.class;

	public UserDAO() {
		super(entityClass);
	}

	@SuppressWarnings("unchecked")
	public UserEntity getUserByEmail(String email){	
		Session session = HibernateUtil.getSessionFactory().openSession();
		//We can use either Query option or criteria, so below code is just for reference
		/*	Query query = session.getNamedQuery("UserEntity.byEmail")
								 .setParameter("email", email);
			userEntities = (List<UserEntity>) query.list();
		 */
		Criteria criteria = session.createCriteria(entityClass)
				.add(Restrictions.eq("email", email));	
		List<UserEntity> userEntities = criteria.list();			
		return userEntities.get(0);				
	}

	/*
	 * This will return all registered users matching either email id or mobile number excluding requested User
	 * i.e. if user1 has requested to get all the registered users, then it will exclude user 1 from the list
	 */
	public List<UserEntity> findAllRegisteredUserBasedOnEmailOrMobile(int userId, List<String> emailIds, List<String> mobileNumbers){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		@SuppressWarnings("unchecked")
		List<UserEntity> userEntities = criteria.add(Restrictions.or(Restrictions.in("email", emailIds),
				Restrictions.in("mobileNumber", mobileNumbers)))
		.add(Restrictions.ne("id", userId)).list();
		return userEntities;
	}

}




































