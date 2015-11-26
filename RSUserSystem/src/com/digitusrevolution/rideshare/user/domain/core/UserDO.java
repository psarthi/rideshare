package com.digitusrevolution.rideshare.user.domain.core;

import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import com.digitusrevolution.rideshare.common.DomainDataMapper;
import com.digitusrevolution.rideshare.common.HibernateUtil;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.data.core.VehicleEntity;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.user.domain.UserUtil;

public class UserDO implements DomainDataMapper {
	
	private User user;
	private UserEntity userEntity;
	private static final Logger logger = LogManager.getLogger(UserDO.class.getName());

	public UserDO(){
		user = new User();
		userEntity = new UserEntity();
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
		mapDomainModelToDataModel();
	}

	public UserEntity getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
		mapDataModelToDomainModel();
	}

	@Override
	public void mapDomainModelToDataModel(){
		UserUtil userUtil = new UserUtil();
		userEntity.setId(user.getId());
		userEntity.setFirstName(user.getFirstName());
		userEntity.setLastName(user.getLastName());
		userEntity.setSex(user.getSex());
		userEntity.setMobileNumber(user.getMobileNumber());
		userEntity.setEmail(user.getEmail());
		userEntity.setPassword(user.getPassword());
//		userEntity.setCity(userUtil.getCityEntity(user.getCity()));
	}
	
	@Override
	public void mapDataModelToDomainModel(){	
		user.setId(userEntity.getId());
		user.setFirstName(userEntity.getFirstName());
		user.setLastName(userEntity.getLastName());
		user.setEmail(userEntity.getEmail());		
		
	}
	
	@Override
	public void mapChildDomainModelToDataModel(){
		
		mapVehicleDomainModelToDataModel();
	}
	
	@Override
	public void mapChildDataModelToDomainModel(){
	
		mapVehicleDataModelToDomainModel();
		
	}
	
	private void mapVehicleDomainModelToDataModel(){

		UserUtil userUtil = new UserUtil();
		Collection<Vehicle> vehicles = user.getVehicles();
		Collection<VehicleEntity> vehicleEntities = userEntity.getVehicles();
		for (Vehicle vehicle : vehicles) {
			vehicleEntities.add(userUtil.getVehicleEntity(vehicle));	
		}

		userEntity.setVehicles(vehicleEntities);		

	}
	

	private void mapVehicleDataModelToDomainModel(){
		UserUtil userUtil = new UserUtil();
		Collection<Vehicle> vehicles = user.getVehicles();
		Collection<VehicleEntity> vehicleEntities = userEntity.getVehicles();
		for (VehicleEntity vehicleEntity : vehicleEntities) {
			vehicles.add(userUtil.getVehicle(vehicleEntity));			
		}
		
		user.setVehicles(vehicles);

	}
	
	public User addVehicle(Vehicle vehicle){
		logger.debug("Getting Session");
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		user.getVehicles().add(vehicle);
		UserService userService = new UserService();
		userService.updateUser(user);
		logger.debug("Session Status: " + session.isOpen());		
		logger.debug("Transaction Status: "+session.getTransaction().getStatus());
		return userService.getUserFullDetail(user.getId());
	}
}
