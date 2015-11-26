package com.digitusrevolution.rideshare.user.domain;

import com.digitusrevolution.rideshare.model.user.data.CityEntity;
import com.digitusrevolution.rideshare.model.user.data.core.VehicleEntity;
import com.digitusrevolution.rideshare.model.user.domain.City;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.user.domain.core.VehicleDO;

public class UserUtil {
	
	public CityEntity getCityEntity(City city ){
		
		CityDO cityDO = new CityDO();
		cityDO.setCity(city);
		return cityDO.getCityEntity();
	}

	public City getCity(CityEntity cityEntity ){
		
		CityDO cityDO = new CityDO();
		cityDO.setCityEntity(cityEntity);
		return cityDO.getCity();
	}
	
	public VehicleEntity getVehicleEntity(Vehicle vehicle ){
		
		VehicleDO vehicleDO = new VehicleDO();
		vehicleDO.setVehicle(vehicle);
		return vehicleDO.getVehicleEntity();
	}

	public Vehicle getVehicle(VehicleEntity vehicleEntity){

		VehicleDO vehicleDO = new VehicleDO();
		vehicleDO.setVehicleEntity(vehicleEntity);
		return vehicleDO.getVehicle();
		
	}


}
