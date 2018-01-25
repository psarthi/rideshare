package com.digitusrevolution.rideshare.common.mapper.user.core;

import java.util.Collection;
import java.util.HashSet;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.common.mapper.billing.core.AccountMapper;
import com.digitusrevolution.rideshare.common.mapper.billing.core.BillMapper;
import com.digitusrevolution.rideshare.common.mapper.ride.core.RideMapper;
import com.digitusrevolution.rideshare.common.mapper.ride.core.RideRequestMapper;
import com.digitusrevolution.rideshare.common.mapper.user.CityMapper;
import com.digitusrevolution.rideshare.common.mapper.user.CountryMapper;
import com.digitusrevolution.rideshare.common.mapper.user.FriendRequestMapper;
import com.digitusrevolution.rideshare.common.mapper.user.MembershipRequestMapper;
import com.digitusrevolution.rideshare.common.mapper.user.PhotoMapper;
import com.digitusrevolution.rideshare.common.mapper.user.PreferenceMapper;
import com.digitusrevolution.rideshare.common.mapper.user.RoleMapper;
import com.digitusrevolution.rideshare.common.mapper.user.StateMapper;
import com.digitusrevolution.rideshare.common.mapper.user.UserFeedbackMapper;
import com.digitusrevolution.rideshare.model.ride.data.core.RideEntity;
import com.digitusrevolution.rideshare.model.ride.data.core.RidePassengerEntity;
import com.digitusrevolution.rideshare.model.user.data.CityEntity;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.domain.core.User;

public class UserMapper implements Mapper<User, UserEntity> {

	@Override
	public UserEntity getEntity(User user, boolean fetchChild){
		UserEntity userEntity = new UserEntity();
		userEntity.setId(user.getId());
		userEntity.setFirstName(user.getFirstName());
		userEntity.setLastName(user.getLastName());
		userEntity.setSex(user.getSex());
		userEntity.setMobileNumber(user.getMobileNumber());
		userEntity.setEmail(user.getEmail());
		userEntity.setPassword(user.getPassword());
		userEntity.setRegistrationType(user.getRegistrationType());
		
		CityMapper cityMapper = new CityMapper();
		if (user.getCity()!=null) {
			userEntity.setCity(cityMapper.getEntity(user.getCity(),fetchChild));			
		}

		
		StateMapper stateMapper = new StateMapper();
		if (user.getState()!=null) {
			userEntity.setState(stateMapper.getEntity(user.getState(), fetchChild));			
		}
		
		CountryMapper countryMapper = new CountryMapper();
		userEntity.setCountry(countryMapper.getEntity(user.getCountry(), fetchChild));
		
		AccountMapper accountMapper = new AccountMapper();
		userEntity.setAccounts(accountMapper.getEntities(userEntity.getAccounts(), user.getAccounts(), fetchChild));

		//Reason behind having this here and not in child function, as if just get(id) it will not get entityChild and 
		//since Vehicle is having cascade property in User, 
		//it will delete the vehicle if it doesn't found it while updating i.e. when you call update(user)
		//Otherwise, if you put this into child function, then ensure before updating you call getChild and not get function, 
		//so that all child entities are set as well
		VehicleMapper vehicleMapper = new VehicleMapper();
		userEntity.setVehicles(vehicleMapper.getEntities(userEntity.getVehicles(), user.getVehicles(), fetchChild));

		//Reason behind having this here and not in child function, as when you get(id) it gets just the entity and not child, 
		//But role is having Many to Many relationship, so when it doesn't get role and if you call update post get, then role would be deleted
		//Otherwise, if you put this into child function, then ensure before updating you call getChild and not get function, 
		//so that all child entities are set as well
		RoleMapper roleMapper = new RoleMapper();
		userEntity.setRoles(roleMapper.getEntities(userEntity.getRoles(), user.getRoles(), fetchChild));
		
		userEntity.setProfileRating(user.getProfileRating());
		
		PhotoMapper photoMapper = new PhotoMapper();
		if (user.getPhoto()!=null) userEntity.setPhoto(photoMapper.getEntity(user.getPhoto(), fetchChild));
		
		PreferenceMapper preferenceMapper = new PreferenceMapper();
		if (user.getPreference()!=null) userEntity.setPreference(preferenceMapper.getEntity(user.getPreference(), fetchChild));
				
		if (fetchChild){
			userEntity = getEntityChild(user, userEntity);			
		} 
		
		return userEntity;				
	}
	
	@Override
	public UserEntity getEntityChild(User user, UserEntity userEntity){
		
		RideMapper rideMapper = new RideMapper();
		//Don't get childs of rides as it will get into recursive loop as ride has driver and driver has rides
		userEntity.setRidesOffered(rideMapper.getEntities(userEntity.getRidesOffered(), user.getRidesOffered(), false));
				
		RideRequestMapper rideRequestMapper = new RideRequestMapper();
		//Don't get childs of ride request as it will get into recursive loop as ride request has passenger and passenger has rides
		userEntity.setRideRequests(rideRequestMapper.getEntities(userEntity.getRideRequests(), user.getRideRequests(), false));
		
		//Note - we are not setting rides taken as it require ride passenger in User, which we are not maintaining
		
		BillMapper billMapper = new BillMapper();
		//Don't fetch child as bill has user and user has bill, so it will get into recursive loop
		userEntity.setBills(billMapper.getEntities(userEntity.getBills(), user.getBills(), false));
				
		UserFeedbackMapper userFeedbackMapper = new UserFeedbackMapper();
		//Don't fetch child as User has feedback and feedback has user
		userEntity.setFeedbacks(userFeedbackMapper.getEntities(userEntity.getFeedbacks(), 
				user.getFeedbacks(), false));
		
		FriendRequestMapper friendRequestMapper = new FriendRequestMapper();
		//Don't fetch child as User has friend and friend has user
		userEntity.setFriendRequests(friendRequestMapper.getEntities(userEntity.getFriendRequests(), 
				user.getFriendRequests(), false));
		
		//Don't put this into entity/model function as entities call entity and this would call entities, so recursive loop 
		//Also don't fetch child, as User has friends and friends has user
		userEntity.setFriends(getEntities(userEntity.getFriends(), user.getFriends(), false));
		
		GroupMapper groupMapper = new GroupMapper();
		//Don't fetch child as user has group and group has user, so recursive loop
		userEntity.setGroups(groupMapper.getEntities(userEntity.getGroups(), user.getGroups(), false));
		userEntity.setGroupInvites(groupMapper.getEntities(userEntity.getGroupInvites(), user.getGroupInvites(), false));
		
		//Don't fetch child as user has request and request has user, so recursive loop
		MembershipRequestMapper membershipRequestMapper = new MembershipRequestMapper();
		userEntity.setMembershipRequests(membershipRequestMapper.getEntities(userEntity.getMembershipRequests(), user.getMembershipRequests(), false));
		
		return userEntity;
		
	}
	
	@Override
	public User getDomainModel(UserEntity userEntity, boolean fetchChild){
		User user = new User();
		user.setId(userEntity.getId());
		user.setFirstName(userEntity.getFirstName());
		user.setLastName(userEntity.getLastName());
		user.setSex(userEntity.getSex());
		user.setMobileNumber(userEntity.getMobileNumber());
		user.setEmail(userEntity.getEmail());
		user.setPassword(userEntity.getPassword());
		user.setRegistrationType(userEntity.getRegistrationType());

		CityMapper cityMapper = new CityMapper();
		if (userEntity.getCity()!=null) {
			user.setCity(cityMapper.getDomainModel(userEntity.getCity(), fetchChild));
		}
		
		StateMapper stateMapper = new StateMapper();
		if (userEntity.getState()!=null){
			user.setState(stateMapper.getDomainModel(userEntity.getState(), fetchChild));			
		}

		CountryMapper countryMapper = new CountryMapper();
		user.setCountry(countryMapper.getDomainModel(userEntity.getCountry(), fetchChild));
		
		AccountMapper accountMapper = new AccountMapper();
		user.setAccounts(accountMapper.getDomainModels(user.getAccounts(), userEntity.getAccounts(), fetchChild));
		
		//Reason behind having this here and not in child function, as if just get(id) it will not get entityChild and
		//since Vehicle is having cascade property in User, 
		//it will delete the vehicle if it doesn't found it while updating i.e. when you call update(user) 
		//Otherwise, if you put this into child function, then ensure before updating you call getChild and not get function, 
		//so that all child entities are set as well
		VehicleMapper vehicleMapper = new VehicleMapper();
		user.setVehicles(vehicleMapper.getDomainModels(user.getVehicles(), userEntity.getVehicles(), fetchChild));

		//Reason behind having this here and not in child function, as when you get(id) it gets just the entity and not child, 
		//But role is having Many to Many relationship, so when it doesn't get role and if you call update post get, then role would be deleted
		//Otherwise, if you put this into child function, then ensure before updating you call getChild and not get function, 
		//so that all child entities are set as well
		RoleMapper roleMapper = new RoleMapper();
		user.setRoles(roleMapper.getDomainModels(user.getRoles(), userEntity.getRoles(), fetchChild));

		user.setProfileRating(userEntity.getProfileRating());
		
		PhotoMapper photoMapper = new PhotoMapper();
		if (userEntity.getPhoto()!=null) user.setPhoto(photoMapper.getDomainModel(userEntity.getPhoto(), fetchChild));

		PreferenceMapper preferenceMapper = new PreferenceMapper();
		if (userEntity.getPreference()!=null)  user.setPreference(preferenceMapper.getDomainModel(userEntity.getPreference(), fetchChild));
		
		if (fetchChild){
			user = getDomainModelChild(user, userEntity);
		}
		
		return user;
	}
			
	@Override
	public User getDomainModelChild(User user, UserEntity userEntity){
		
		RideMapper rideMapper = new RideMapper();
		//Don't get childs of rides as it will get into recursive loop as ride has driver and driver has rides
		user.setRidesOffered(rideMapper.getDomainModels(user.getRidesOffered(), userEntity.getRidesOffered(), false));
				
		RideRequestMapper rideRequestMapper = new RideRequestMapper();
		//Don't get childs of ride request as it will get into recursive loop as ride request has passenger and passenger has rides
		user.setRideRequests(rideRequestMapper.getDomainModels(user.getRideRequests(), userEntity.getRideRequests(), false));
		
		BillMapper billMapper = new BillMapper();
		//Don't fetch child as bill has user and user has bill, so it will get into recursive loop
		user.setBills(billMapper.getDomainModels(user.getBills(), userEntity.getBills(), false));
				
		UserFeedbackMapper userFeedbackMapper = new UserFeedbackMapper();
		//Don't fetch child as User has feedback and feedback has user
		user.setFeedbacks(userFeedbackMapper.getDomainModels(user.getFeedbacks(), 
				userEntity.getFeedbacks(), false));
		
		FriendRequestMapper friendRequestMapper = new FriendRequestMapper();
		//Don't fetch child as User has friend and friend has user
		user.setFriendRequests(friendRequestMapper.getDomainModels(user.getFriendRequests(), 
				userEntity.getFriendRequests(), false));
		
		//Don't put this into entity/model function as entities call entity and this would call entities, so recursive loop 
		//Also don't fetch child, as User has friends and friends has user
		user.setFriends(getDomainModels(user.getFriends(), userEntity.getFriends(), false));
		
		GroupMapper groupMapper = new GroupMapper();
		//Don't fetch child as user has group and group has user, so recursive loop
		user.setGroups(groupMapper.getDomainModels(user.getGroups(), userEntity.getGroups(), false));
		user.setGroupInvites(groupMapper.getDomainModels(user.getGroupInvites(), userEntity.getGroupInvites(), false));
		
		//Don't fetch child as user has request and request has user, so recursive loop
		MembershipRequestMapper membershipRequestMapper = new MembershipRequestMapper();
		user.setMembershipRequests(membershipRequestMapper.getDomainModels(user.getMembershipRequests(), userEntity.getMembershipRequests(), false));

		return user;
	}
	
	@Override
	public Collection<User> getDomainModels(Collection<User> users, Collection<UserEntity> userEntities, boolean fetchChild){
		for (UserEntity userEntity : userEntities) {
			User user = new User();
			user = getDomainModel(userEntity, fetchChild);
			users.add(user);
		}
		return users;		
	}
	
	@Override
	public Collection<UserEntity> getEntities(Collection<UserEntity> userEntities, Collection<User> users, boolean fetchChild){
		for (User user : users) {
			userEntities.add(getEntity(user, fetchChild));
		}
		return userEntities;		
	}		
}
