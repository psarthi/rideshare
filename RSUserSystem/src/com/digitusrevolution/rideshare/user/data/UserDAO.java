package com.digitusrevolution.rideshare.user.data;

import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.mapper.ride.core.RideMapper;
import com.digitusrevolution.rideshare.common.mapper.user.core.UserMapper;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.domain.core.User;

public class UserDAO extends GenericDAOImpl<UserEntity,Integer>{

	private static final Logger logger = LogManager.getLogger(GenericDAOImpl.class.getName());
	private static final Class<UserEntity> entityClass = UserEntity.class;

	public UserDAO() {
		super(entityClass);
	}

	@SuppressWarnings("unchecked")
	public UserEntity getUserByEmail(String email){	
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transation = null;
		List<UserEntity> userEntities = null;
		try {
			transation = session.beginTransaction();

			/*			Query query = session.getNamedQuery("UserEntity.byEmail")
								 .setParameter("email", email);
			userEntities = (List<UserEntity>) query.list();
			 */
			Criteria criteria = session.createCriteria(entityClass)
					.add(Restrictions.eq("email", email));	
			userEntities = criteria.list();			
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
		if (userEntities.isEmpty()){
			return null;
		} else {
			return userEntities.get(0);				
		}	
	}
	
	public Collection<Ride> getRidesOffered(int userId){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		UserEntity userEntity = (UserEntity) criteria.add(Restrictions.eq("id", userId)).uniqueResult();
		RideMapper rideMapper = new RideMapper();
		Collection<Ride> rides = new User().getRidesOffered(); 
		rides = rideMapper.getDomainModels(rides, userEntity.getRidesOffered(), true);
		return rides;
	}

}




































