package com.digitusrevolution.rideshare.user.data;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.digitusrevolution.rideshare.common.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.HibernateUtil;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;

public class UserDAO extends GenericDAOImpl<UserEntity>{

	private static final Logger logger = LogManager.getLogger(GenericDAOImpl.class.getName());

	public UserDAO() {
		super(UserEntity.class);
	}

	@SuppressWarnings("unchecked")
	public UserEntity getUserByEmail(String email){	
		Session session = null;
		Transaction transation = null;
		List<UserEntity> userEntities = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transation = session.beginTransaction();

/*			Query query = session.getNamedQuery("UserEntity.byEmail")
								 .setParameter("email", email);
			userEntities = (List<UserEntity>) query.list();
*/
			Criteria criteria = session.createCriteria(UserEntity.class)
										.add(Restrictions.eq("email", email));	
			userEntities = criteria.list();			
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
		if (userEntities.isEmpty()){
			return null;
		} else {
			return userEntities.get(0);				
		}	
}
	
}
