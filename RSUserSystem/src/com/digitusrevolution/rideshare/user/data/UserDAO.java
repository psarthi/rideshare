package com.digitusrevolution.rideshare.user.data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideStatus;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;

public class UserDAO extends GenericDAOImpl<UserEntity,Integer>{

	private static final Class<UserEntity> entityClass = UserEntity.class;

	public UserDAO() {
		super(entityClass);
	}

	@SuppressWarnings("unchecked")
	public UserEntity getUserByEmail(String email){	
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		//We can use either Query option or criteria, so below code is just for reference
		/*	Query query = session.getNamedQuery("UserEntity.byEmail")
								 .setParameter("email", email);
			userEntities = (List<UserEntity>) query.list();
		 */
		Criteria criteria = session.createCriteria(entityClass)
				.add(Restrictions.eq("email", email));	
		List<UserEntity> userEntities = criteria.list();
		if (userEntities.isEmpty()){
			return null;
		} else {
			return userEntities.get(0);				
		}	
	}

	/*
	 * This will return all registered users matching either email id or mobile number excluding requested User
	 * i.e. if user1 has requested to get all the registered users, then it will exclude user 1 from the list
	 */
	public Set<UserEntity> findAllRegisteredUserBasedOnEmailOrMobile(int userId, List<String> emailIds, List<String> mobileNumbers){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		@SuppressWarnings("unchecked")
		Set<UserEntity> userEntities = new HashSet<>(criteria.add(Restrictions.or(Restrictions.in("email", emailIds),
				Restrictions.in("mobileNumber", mobileNumbers)))
		.add(Restrictions.ne("id", userId)).list());
		return userEntities;
	}
	
	public int getRidesOffered(int userId) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass)
				.add(Restrictions.eq("id", userId))
				.createCriteria("ridesOffered", JoinType.RIGHT_OUTER_JOIN)
					.add(Restrictions.eq("status", RideStatus.Finished))
				.setProjection(Projections.rowCount());
		int size = (int) Long.parseLong(criteria.uniqueResult().toString());
		return size;
	}
	
	public int getRidesTaken(int userId) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass)
				.add(Restrictions.eq("id", userId))
				.createCriteria("ridesTaken", JoinType.RIGHT_OUTER_JOIN)
				.setProjection(Projections.rowCount());
		int size = (int) Long.parseLong(criteria.uniqueResult().toString());
		return size;
	}

}




































