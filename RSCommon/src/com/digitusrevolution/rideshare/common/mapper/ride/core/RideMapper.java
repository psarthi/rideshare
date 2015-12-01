package com.digitusrevolution.rideshare.common.mapper.ride.core;

import java.util.Collection;
import java.util.LinkedList;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.common.mapper.ride.PointMapper;
import com.digitusrevolution.rideshare.common.mapper.ride.RouteMapper;
import com.digitusrevolution.rideshare.common.mapper.ride.TrustNetworkMapper;
import com.digitusrevolution.rideshare.common.mapper.user.core.UserMapper;
import com.digitusrevolution.rideshare.common.mapper.user.core.VehicleMapper;
import com.digitusrevolution.rideshare.model.ride.data.PointEntity;
import com.digitusrevolution.rideshare.model.ride.data.RouteEntity;
import com.digitusrevolution.rideshare.model.ride.data.TrustNetworkEntity;
import com.digitusrevolution.rideshare.model.ride.data.core.RideEntity;
import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.model.ride.domain.Route;
import com.digitusrevolution.rideshare.model.ride.domain.TrustNetwork;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.data.core.VehicleEntity;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;

public class RideMapper implements Mapper<Ride, RideEntity>{

	@Override
	public RideEntity getEntity(Ride ride) {
		RideEntity rideEntity = new RideEntity();
		rideEntity.setId(ride.getId());
		rideEntity.setDateTime(ride.getDateTime());
		rideEntity.setSeatOffered(ride.getSeatOffered());
		rideEntity.setLuggageCapacityOffered(ride.getLuggageCapacityOffered());
		rideEntity.setRecur(ride.getRecur());
		rideEntity.setStatus(ride.getStatus());
		rideEntity.setSexPreference(ride.getSexPreference());
		
		PointMapper pointMapper = new PointMapper();
		Point point = ride.getStartPoint();
		rideEntity.setStartPoint(pointMapper.getEntity(point));
		point = ride.getEndPoint();
		rideEntity.setEndPoint(pointMapper.getEntity(point));
		
		TrustNetworkMapper trustNetworkMapper = new TrustNetworkMapper();
		TrustNetwork trustNetwork = ride.getTrustNetwork();
		rideEntity.setTrustNetwork(trustNetworkMapper.getEntity(trustNetwork));
		
		RouteMapper routeMapper = new RouteMapper();
		Route route = ride.getRoute();
		rideEntity.setRoute(routeMapper.getEntity(route));
		
		VehicleMapper vehicleMapper = new VehicleMapper();
		Vehicle vehicle = ride.getVehicle();
		rideEntity.setVehicle(vehicleMapper.getEntity(vehicle));
		
		UserMapper userMapper = new UserMapper();
		User user = ride.getDriver();
		rideEntity.setDriver(userMapper.getEntity(user));

		rideEntity = getEntityChild(ride, rideEntity);
		
		return rideEntity;
	}

	@Override
	public RideEntity getEntityChild(Ride ride, RideEntity rideEntity) {
		
		UserMapper userMapper = new UserMapper();
		Collection<User> users = ride.getPassengers();
		rideEntity.setPassengers(userMapper.getEntities(users));
		
		return rideEntity;
	}

	@Override
	public Ride getDomainModel(RideEntity rideEntity) {
		Ride ride = new Ride();
		ride.setId(rideEntity.getId());
		ride.setDateTime(rideEntity.getDateTime());
		ride.setSeatOffered(rideEntity.getSeatOffered());
		ride.setLuggageCapacityOffered(rideEntity.getLuggageCapacityOffered());
		ride.setRecur(rideEntity.getRecur());
		ride.setStatus(rideEntity.getStatus());
		ride.setSexPreference(rideEntity.getSexPreference());
		
		PointMapper pointMapper = new PointMapper();
		PointEntity pointEntity = rideEntity.getStartPoint();
		ride.setStartPoint(pointMapper.getDomainModel(pointEntity));
		pointEntity = rideEntity.getEndPoint();
		ride.setEndPoint(pointMapper.getDomainModel(pointEntity));
		
		TrustNetworkMapper trustNetworkMapper = new TrustNetworkMapper();
		TrustNetworkEntity trustNetworkEntity = rideEntity.getTrustNetwork();
		ride.setTrustNetwork(trustNetworkMapper.getDomainModel(trustNetworkEntity));
		
		RouteMapper routeMapper = new RouteMapper();
		RouteEntity routeEntity = rideEntity.getRoute();
		ride.setRoute(routeMapper.getDomainModel(routeEntity));
		
		VehicleMapper vehicleMapper = new VehicleMapper();
		VehicleEntity vehicleEntity = rideEntity.getVehicle();
		ride.setVehicle(vehicleMapper.getDomainModel(vehicleEntity));
		
		UserMapper userMapper = new UserMapper();
		UserEntity userEntity = rideEntity.getDriver();
		ride.setDriver(userMapper.getDomainModel(userEntity));
		
		return ride;
	}


	@Override
	public Ride getDomainModelChild(Ride ride, RideEntity rideEntity) {
		
		UserMapper userMapper = new UserMapper();
		Collection<UserEntity> userEntities = rideEntity.getPassengers();
		ride.setPassengers(userMapper.getDomainModels(userEntities));
		
		return ride;
	}

	@Override
	public Collection<Ride> getDomainModels(Collection<RideEntity> rideEntities) {
		Collection<Ride> rides = new LinkedList<>();
		Ride ride = new Ride();
		for (RideEntity rideEntity : rideEntities) {
			ride = getDomainModel(rideEntity);
			ride = getDomainModelChild(ride, rideEntity);
			rides.add(ride);
		}
		return rides;		
	}

	@Override
	public Collection<RideEntity> getEntities(Collection<Ride> rides) {
		Collection<RideEntity> rideEntities = new LinkedList<>();
		for (Ride ride : rides) {
			rideEntities.add(getEntity(ride));
		}
		return rideEntities;		
	}

}
