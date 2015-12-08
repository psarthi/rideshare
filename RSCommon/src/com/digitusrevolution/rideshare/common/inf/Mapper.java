package com.digitusrevolution.rideshare.common.inf;

import java.util.Collection;


public interface Mapper<M,E> {
	
    /**
     * Set entity PK only
     * 
     *<P>Sample code -
     * 
     *<P>UserEntity userEntity = new UserEntity();
     *<P>userEntity.setId(user.getId());
	 *	
	 *<P>return userEntity;				
     * 
     */
	E getEntityWithOnlyPK(M model);

    /**
     * Set entity by first invoking getEntityWithOnlyPK() and then setting rest of the properties at root level,   
     * and then invoke getEntityChild() to set entity child
     * 
     *<P>Sample code -
     * 
     *<P>UserEntity userEntity = new UserEntity();
     *<P>userEntity = getEntityWithOnlyPK(user);
	 *<P>userEntity.setFirstName(user.getFirstName());
	 *		
	 *<P>userEntity = getEntityChild(user, userEntity);	
	 *<P>return userEntity;				
     * 
     */
	E getEntity(M model);

    /**
     * Set entity child 
     * 
     *<P>Sample code -
     * 
     *<P>	VehicleMapper vehicleMapper = new VehicleMapper();
	 *<P>	Collection<Vehicle> vehicles = user.getVehicles();
	 *<P>	userEntity.setVehicles(vehicleMapper.getEntities(vehicles));
	 *
     *<P> 	return userEntity;
     * 
     */
	E getEntityChild(M model, E entity);

	/**
     * Set domain model PK only
     * 
     *<P> Sample code -
     * 
     *<P> 	User user = new User();
	 *<P>	user.setId(userEntity.getId());
	 *
     *<P> 	return user;
     * 
     */
	M getDomainModelWithOnlyPK(E entity);
	
    /**
     * Set domain model by first invoking getEntityWithOnlyPK() and then setting rest of the properties at root level.
     * 
     *<P> Sample code -
     * 
     *<P> 	User user = new User();
	 *<P>	user.setId(userEntity.getId());
	 *<P>	user.setFirstName(userEntity.getFirstName());
	 *
     *<P> 	return user;
     *  
     */
	M getDomainModel(E entity);
	
	/**
     * Set domain model child and this method needs to be invoked from DO.fetchChild() method
     * or parent mapper getDomainModels() method
     * 
     *<P> Sample code -
     * 
     *<P> 	VehicleMapper vehicleMapper = new VehicleMapper();
	 *<P>	Collection<VehicleEntity> vehicleEntities = userEntity.getVehicles();
	 *<P>	user.setVehicles(vehicleMapper.getDomainModels(vehicleEntities));
	 *
	 *<P> 	return user;
     * 
     */
	M getDomainModelChild(M model, E entity);
	
	/**
     * Set all domain models PK only
     * 
     *<P> Sample code -
     * 
     * 
     *<P>  Collection<User> users = new LinkedList<>();
	 *<P>	User user = new User();
	 *<P>	for (UserEntity userEntity : userEntities) {
	 *<P>		user = getDomainModelWithOnlyPK(userEntity);
	 *<P>		users.add(user);
	 *<P>	}
	 *
	 *<P>	return users;		
	 *
     * 
     */
	Collection<M> getDomainModelsWithOnlyPK(Collection<E> entities);

	/**
     * Set all domain models and invoke getDomainChild to set domain model child elements
     * 
     *<P> Sample code -
     * 
     * 
     *<P>  Collection<User> users = new LinkedList<>();
	 *<P>	User user = new User();
	 *<P>	for (UserEntity userEntity : userEntities) {
	 *<P>		user = getDomainModel(userEntity);
	 *<P>		user = getDomainModelChild(user, userEntity);
	 *<P>		users.add(user);
	 *<P>	}
	 *
	 *<P>	return users;		
	 *
     * 
     */
	Collection<M> getDomainModels(Collection<E> entities);
	
	/**
     * Set all entities PK only
     * 
     *<P> Sample code -
     * 
     *<P> 	Collection<UserEntity> userEntities = new LinkedList<>();
	 *<P>	for (User user : users) {
	 *<P>		userEntities.add(getEntityWithOnlyPK(user));
	 *<P>	}
	 *
	 *<P>	return userEntities;		
	 *
	 *	
     */
	Collection<E> getEntitiesWithOnlyPK(Collection<M> model);


	/**
     * Set all entities
     * 
     *<P> Sample code -
     * 
     *<P> 	Collection<UserEntity> userEntities = new LinkedList<>();
	 *<P>	for (User user : users) {
	 *<P>		userEntities.add(getEntity(user));
	 *<P>	}
	 *
	 *<P>	return userEntities;		
	 *
	 *	
     */
	Collection<E> getEntities(Collection<M> model);

}