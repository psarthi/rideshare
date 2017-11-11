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
import com.digitusrevolution.rideshare.user.domain.OTPDO;
import com.digitusrevolution.rideshare.user.domain.core.UserDO;

public class UserSystemTest {

	private static final Logger logger = LogManager.getLogger(UserSystemTest.class.getName());

	public static void main(String args[]){


		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		try {
			transaction = session.beginTransaction();

			UserSystemTest userSystemTest = new UserSystemTest();
			userSystemTest.test();
			
			transaction.commit();

			/*
			 * Reason for catching RuntimeException and not HibernateException as all exceptions thrown by Hibernate
			 * is not of type HibernateException such as NotFoundException
			 */
		} catch (RuntimeException e) {
			if (transaction!=null){
				logger.error("Transaction Failed, Rolling Back");
				transaction.rollback();
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
		OTPDO otpdo = new OTPDO();
		String otpNumber = otpdo.getOTP("+91 8123110015");
		System.out.println("OTP Validation status:" + otpdo.validateOTP("+91 8123110015", otpNumber));
		
	}

}


























