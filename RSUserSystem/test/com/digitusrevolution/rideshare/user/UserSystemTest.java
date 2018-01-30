package com.digitusrevolution.rideshare.user;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.auth.AuthService;
import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.util.RESTClientUtil;
import com.digitusrevolution.rideshare.model.common.NotificationMessage;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.user.data.MembershipRequestEntity;
import com.digitusrevolution.rideshare.model.user.data.core.GroupEntity;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.domain.FuelType;
import com.digitusrevolution.rideshare.model.user.domain.VehicleCategory;
import com.digitusrevolution.rideshare.model.user.domain.VehicleSubCategory;
import com.digitusrevolution.rideshare.model.user.domain.core.Group;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.user.data.UserDAO;
import com.digitusrevolution.rideshare.user.domain.OTPDO;
import com.digitusrevolution.rideshare.user.domain.VehicleCategoryDO;
import com.digitusrevolution.rideshare.user.domain.VehicleSubCategoryDO;
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
				
		UserDO userDO = new UserDO();
		
		/*User user = userDO.get(1);
		Message message = new Message();
		message.setTo(user.getPushNotificationToken());
		message.getNotification().setTitle("Offered Ride");
		message.getNotification().setBody("Found matching ride partner");
		RESTClientUtil.sendNotification(message);
		
		
		userDO.getCommonGroups(1, 2);

		List<User> users = userDO.searchUserByName("Par",1);
		for (User user:users) {
			System.out.println("User is:"+user.getFirstName()+" "+user.getLastName());
		}
		
		UserDAO userDAO = new UserDAO();
		boolean status = userDAO.isInvited(2, 2);
		System.out.println("Invited status:"+status);
		
		List<MembershipRequestEntity> requestEntities = userDAO.getUserMembershipRequests(4, 0);
		System.out.println("Request Size:"+requestEntities.size());
		

		int count = userDAO.getRidesOffered(2);
		System.out.println("Rides Offered is:"+count);
		count = userDAO.getRidesTaken(2);
		System.out.println("Rides Taken is:"+count);
		

		VehicleCategory vehicleCategory = new VehicleCategory();
		vehicleCategory.setName("All");
		
		VehicleCategoryDO vehicleCategoryDO = new VehicleCategoryDO();
		int catId = vehicleCategoryDO.create(vehicleCategory);

		
		VehicleSubCategory vehicleSubCategory = new VehicleSubCategory();
		vehicleSubCategory.setName("All");
		vehicleSubCategory.setAirConditioner(true);
		vehicleSubCategory.setFuelType(FuelType.Petrol);
		vehicleSubCategory.setAverageMileage(12);
		
		VehicleSubCategoryDO vehicleSubCategoryDO = new VehicleSubCategoryDO();
		int id = vehicleSubCategoryDO.create(vehicleSubCategory);
		vehicleSubCategory = vehicleSubCategoryDO.get(id);
		
		vehicleCategory = vehicleCategoryDO.getAllData(catId);
		
		vehicleCategory.getSubCategories().add(vehicleSubCategory);
		vehicleCategoryDO.update(vehicleCategory);
		*/
		
	}

}


























