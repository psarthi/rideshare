package com.digitusrevolution.rideshare.user.data.inf;

import com.digitusrevolution.rideshare.user.data.entity.core.VehicleEntity;

public interface VehicleDAO {

	void createVehicle(VehicleEntity vehicleEntity);

	VehicleEntity getVehicle(int vehicleId);

	void updateVehicle(VehicleEntity vehicleEntity);

	void deleteVehicle(int vehicleId);

}
