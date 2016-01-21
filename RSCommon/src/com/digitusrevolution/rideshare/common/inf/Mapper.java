package com.digitusrevolution.rideshare.common.inf;

import java.util.Collection;

public interface Mapper<M,E> {
	
    /**
     * Set entity by setting all of the basic properties at root level, which doesn't have any recursive dependencies
     * Note - Recursive dependency is key here otherwise it will get into recursive loop
     * 
     * Set Child properties depending on the caller, if fetchChild is true, then set child else no
     * 
     *<P>Sample code -
     * 
     *<P>UserEntity userEntity = new UserEntity();
     *<p>userEntity.setId(user.getId());
	 *<P>userEntity.setFirstName(user.getFirstName());
	 *
	 *<P>if (fetchChild){
	 *<P>	userEntity = getEntityChild(user, userEntity);
	 *<P>}
	 *	
	 *<P>return userEntity;				
     * 
     */
	E getEntity(M model, boolean fetchChild);

    /**
     * Set entity child including those which has recursive dependencies. Note - Don't set fetchChild as true for recursive dependencies
     * 
     *<P>Sample code -
     * 
 	 *<P>   VehicleMapper vehicleMapper = new VehicleMapper();
	 *<P>	userEntity.setVehicles(vehicleMapper.getEntities(userEntity.getVehicles(), user.getVehicles(), true));
	 *
	 *<P>	RideMapper rideMapper = new RideMapper();
	 *<P>	//Don't get childs of rides as it will get into recursive loop as ride has driver and driver has rides
	 *<P>	userEntity.setRidesOffered(rideMapper.getEntities(userEntity.getRidesOffered(), user.getRidesOffered(), false));
	 *
     *<P> 	return userEntity;
     * 
     */
	E getEntityChild(M model, E entity);
	
    /**
     * Set domain model by setting all of the basic properties at root level, which doesn't have any recursive dependencies
     * Note - Recursive dependency is key here otherwise it will get into recursive loop
     * 
     * Set Child properties depending on the caller, if fetchChild is true, then set child else no
     * 
     *<P> Sample code -
     * 
     *<P> 	User user = new User();
	 *<P>	user.setId(userEntity.getId());
	 *<P>	user.setFirstName(userEntity.getFirstName());
	 *
	 *<P> 	if (fetchChild){
	 *<p>		user = getDomainModelChild(user, userEntity);
	 *<p> 	}
     *<P> 	return user;
     *  
     */
	M getDomainModel(E entity, boolean fetchChild);
	
    /**
     * Set domain model child including those which has recursive dependencies. Note - Don't set fetchChild as true for recursive dependencies
     * Note - This method needs to be called by fetchChild of DO's instead of calling getDomainModel with fetchChild false. 
     *        as it would unnecessarily set the domain root level property once again 
     * 
     *<P>Sample code -
     * 
 	 *<P>   VehicleMapper vehicleMapper = new VehicleMapper();
	 *<P>	user.setVehicles(vehicleMapper.getDomainModels(user.getVehicles(), userEntity.getVehicles(), true));
	 *
	 *<P>	RideMapper rideMapper = new RideMapper();
	 *<P>	//Don't get childs of rides as it will get into recursive loop as ride has driver and driver has rides
	 *<P>	user.setRidesOffered(rideMapper.getDomainModels(user.getRidesOffered(), userEntity.getRidesOffered(), false));
	 *
     *<P> 	return user;
     * 
     */
	M getDomainModelChild(M model, E entity);
	

	/**
     * Set all domain models and its child depending on the caller fetchChild value
     * i.e if fetchChild is true, child would be set else no
     * 
     *<P> Sample code -
     * 
     * 
	 *<P>	for (UserEntity userEntity : userEntities) {
	 *<P>		User user = new User();
	 *<P>		user = getDomainModel(userEntity, fetchChild);
	 *<P>		users.add(user);
	 *<P>	}
	 *
	 *<P>	return users;		
	 *
     * 
     */
	Collection<M> getDomainModels(Collection<M> models, Collection<E> entities, boolean fetchChild);


	/**
     * Set all entities and its child depending on the caller fetchChild value
     * i.e if fetchChild is true, child would be set else no
     * 
     *<P> Sample code -
     * 
	 *<P>	for (User user : users) {
	 *<P>		userEntities.add(getEntity(user), fetchChild);
	 *<P>	}
	 *
	 *<P>	return userEntities;		
	 *
	 *	
     */
	Collection<E> getEntities(Collection<E> entities, Collection<M> models, boolean fetchChild);

}