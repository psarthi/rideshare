package com.digitusrevolution.rideshare.user;


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.HibernateUtil;
import com.digitusrevolution.rideshare.model.user.domain.City;
import com.digitusrevolution.rideshare.model.user.domain.Role;
import com.digitusrevolution.rideshare.model.user.domain.Sex;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.user.domain.CityDomainService;
import com.digitusrevolution.rideshare.user.domain.RoleDomainService;
import com.digitusrevolution.rideshare.user.domain.core.UserDO;
import com.digitusrevolution.rideshare.user.domain.core.UserDomainService;
import com.digitusrevolution.rideshare.user.domain.core.VehicleDomainService;

@Path("/domain/loadsample")
@Consumes(MediaType.APPLICATION_JSON)
public class DataLoader {
	
	
	private static final Logger logger = LogManager.getLogger(DataLoader.class.getName());
	
	@GET
	public Response load(){
		
		String[] args ={};
		DataLoader.main(args);
		return Response.ok().build();
	}
	
	
	public static void main(String args[]){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		try {
			transation = session.beginTransaction();
			
			DataLoader dataLoader = new DataLoader();
			dataLoader.loadCity();
			dataLoader.loadRole();
			dataLoader.loadUser();
			dataLoader.loadVehicle();
			
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
	
	public void loadCity(){
		CityDomainService cityDomainService = new CityDomainService();
		City city = new City();
		city.setName("Bangalore");
		int id;
		id = cityDomainService.create(city);
		System.out.println("City ID: "+id);
		city.setName("Chennai");
		id = cityDomainService.create(city);
		System.out.println("City ID: "+id);
		city.setName("Mumbai");
		id = cityDomainService.create(city);
		System.out.println("City ID: "+id);
		city.setName("New Delhi");
		id = cityDomainService.create(city);
		System.out.println("City ID: "+id);
		city.setName("Kolkata");
		id = cityDomainService.create(city);
		System.out.println("City ID: "+id);
		
	}

	public void loadRole(){
		
		RoleDomainService roleDomainService = new RoleDomainService();
		Role role = new Role();
		role.setName("Passenger");
		roleDomainService.create(role);
		role.setName("Driver");
		roleDomainService.create(role);		
	
	}
	
	public void loadUser(){
		
		UserDomainService userDomainService = new UserDomainService();
		CityDomainService cityDomainService = new CityDomainService();
		User user = new User();
		City city = new City();
	
		for (int i=1; i<6; i++){
	
			user.setFirstName("firstName-"+i);
			user.setLastName("lastName-"+i);
			user.setEmail("email-"+i);
			user.setMobileNumber("mobileNumber-"+i);
			if ((i & 1)==0){
				user.setSex(Sex.Male);				
			}else {
				user.setSex(Sex.Female);
			}			
			city = cityDomainService.get(i);
			user.setCity(city);
			userDomainService.update(user);

		}
		
	}
	
	public void loadVehicle(){
		
		UserDomainService userDomainService = new UserDomainService();
		VehicleDomainService vehicleDomainService = new VehicleDomainService();
		UserDO userDO = new UserDO();
		User user = new User();
		Vehicle vehicle = new Vehicle();
		
		for (int i=0;i<2;i++){
			int id = vehicleDomainService.create(vehicle);
			vehicle = vehicleDomainService.get(id);
			user = userDomainService.getChild(1);
			userDO.setUser(user);
			userDO.addVehicle(vehicle);
		}
		
		for (int i=0;i<2;i++){
			user = userDomainService.getChild(2);
			userDO.setUser(user);
			Vehicle vehicle2 = new Vehicle();			
			userDO.addVehicle(vehicle2);
		}
		
	}

	
}
