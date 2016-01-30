package com.digitusrevolution.rideshare.user;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.user.data.UserDAO;
import com.digitusrevolution.rideshare.user.domain.core.UserDO;

public class UserSystemTest {

	private static final Logger logger = LogManager.getLogger(UserSystemTest.class.getName());

	public static void main(String args[]){


		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		try {
			transation = session.beginTransaction();

			UserSystemTest userSystemTest = new UserSystemTest();
			userSystemTest.test();

			
			transation.commit();

			/*
			 * Reason for catching RuntimeException and not HibernateException as all exceptions thrown by Hibernate
			 * is not of type HibernateException such as NotFoundException
			 */
		} catch (RuntimeException e) {
			if (transation!=null){
				logger.error("Transaction Failed, Rolling Back");
				transation.rollback();
				throw e;
			}
		}
		finally {
			if (session.isOpen()){
				logger.info("Closing Session");
				session.close();				
			}
		}	
	}	
	
	public void test(){
		UserDO userDO = new UserDO();
		List<String> emailIds = new LinkedList<>();
		emailIds.add("email-1");
		emailIds.add("email-2");
		emailIds.add("email-7");
		emailIds.add("email-8");		

		List<String> mobileNumbers = new LinkedList<>();
		mobileNumbers.add("mobileNumber-1");
		mobileNumbers.add("mobileNumber-3");
		mobileNumbers.add("mobileNumber-5");
		mobileNumbers.add("mobileNumber-9");
		List<User> users = userDO.findAllPotentialFriendsBasedOnEmailOrMobile(1, emailIds, mobileNumbers);
		System.out.println(users.size());		
		userDO.sendFriendRequest(1, users);
		
		userDO.acceptFriendRequest(1, 4);
		userDO.rejectFriendRequest(1, 4);
		User user = userDO.getChild(1);
		System.out.println(user.getFriends().size());
		
	}

}


























