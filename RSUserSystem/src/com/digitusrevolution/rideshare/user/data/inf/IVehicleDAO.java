package com.digitusrevolution.rideshare.user.data.inf;

import com.digitusrevolution.rideshare.model.user.data.core.VehicleEntity;

public interface IVehicleDAO {

	void createVehicle(VehicleEntity vehicleEntity);

	VehicleEntity getVehicle(int vehicleId);

	void updateVehicle(VehicleEntity vehicleEntity);

	void deleteVehicle(int vehicleId);

}
