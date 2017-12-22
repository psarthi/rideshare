package com.digitusrevolution.rideshare.common.mapper.user;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.common.mapper.ride.TrustCategoryMapper;
import com.digitusrevolution.rideshare.common.mapper.user.core.VehicleMapper;
import com.digitusrevolution.rideshare.model.user.data.PreferenceEntity;
import com.digitusrevolution.rideshare.model.user.domain.Preference;

public class PreferenceMapper implements Mapper<Preference, PreferenceEntity> {

	@Override
	public PreferenceEntity getEntity(Preference preference, boolean fetchChild) {
		PreferenceEntity preferenceEntity = new PreferenceEntity();
		preferenceEntity.setId(preference.getId());
		VehicleCategoryMapper vehicleCategoryMapper = new VehicleCategoryMapper();
		preferenceEntity.setVehicleCategory(vehicleCategoryMapper.getEntity(preference.getVehicleCategory(), fetchChild));
		VehicleSubCategoryMapper vehicleSubCategoryMapper = new VehicleSubCategoryMapper();
		preferenceEntity.setVehicleSubCategory(vehicleSubCategoryMapper.getEntity(preference.getVehicleSubCategory(), fetchChild));
		preferenceEntity.setPickupTimeVariation(preference.getPickupTimeVariation());
		preferenceEntity.setPickupPointVariation(preference.getPickupPointVariation());
		preferenceEntity.setDropPointVariation(preference.getDropPointVariation());
		preferenceEntity.setSeatRequired(preference.getSeatRequired());
		preferenceEntity.setLuggageCapacityRequired(preference.getLuggageCapacityRequired());
		TrustCategoryMapper trustCategoryMapper = new TrustCategoryMapper();
		preferenceEntity.setTrustCategory(trustCategoryMapper.getEntity(preference.getTrustCategory(), fetchChild));
		preferenceEntity.setSexPreference(preference.getSexPreference());
		preferenceEntity.setMinProfileRating(preference.getMinProfileRating());
		VehicleMapper vehicleMapper = new VehicleMapper();
		if (preference.getDefaultVehicle()!=null) preferenceEntity.setDefaultVehicle(vehicleMapper.getEntity(preference.getDefaultVehicle(), fetchChild));
		preferenceEntity.setRideMode(preference.getRideMode());
		return preferenceEntity;
	}

	@Override
	public PreferenceEntity getEntityChild(Preference preference, PreferenceEntity preferenceEntity) {
		return preferenceEntity;
	}

	@Override
	public Preference getDomainModel(PreferenceEntity preferenceEntity, boolean fetchChild) {
		Preference preference = new Preference();
		preference.setId(preferenceEntity.getId());
		VehicleCategoryMapper vehicleCategoryMapper = new VehicleCategoryMapper();
		preference.setVehicleCategory(vehicleCategoryMapper.getDomainModel(preferenceEntity.getVehicleCategory(), fetchChild));
		VehicleSubCategoryMapper vehicleSubCategoryMapper = new VehicleSubCategoryMapper();
		preference.setVehicleSubCategory(vehicleSubCategoryMapper.getDomainModel(preferenceEntity.getVehicleSubCategory(), fetchChild));
		preference.setPickupTimeVariation(preferenceEntity.getPickupTimeVariation());
		preference.setPickupPointVariation(preferenceEntity.getPickupPointVariation());
		preference.setDropPointVariation(preferenceEntity.getDropPointVariation());
		preference.setSeatRequired(preferenceEntity.getSeatRequired());
		preference.setLuggageCapacityRequired(preferenceEntity.getLuggageCapacityRequired());
		TrustCategoryMapper trustCategoryMapper = new TrustCategoryMapper();
		preference.setTrustCategory(trustCategoryMapper.getDomainModel(preferenceEntity.getTrustCategory(), fetchChild));
		preference.setSexPreference(preferenceEntity.getSexPreference());
		preference.setMinProfileRating(preferenceEntity.getMinProfileRating());
		VehicleMapper vehicleMapper = new VehicleMapper();
		if (preferenceEntity.getDefaultVehicle()!=null) preference.setDefaultVehicle(vehicleMapper.getDomainModel(preferenceEntity.getDefaultVehicle(), fetchChild));
		preference.setRideMode(preferenceEntity.getRideMode());
		return preference;	
	}

	@Override
	public Preference getDomainModelChild(Preference preference, PreferenceEntity preferenceEntity) {
		return preference;
	}

	@Override
	public Collection<Preference> getDomainModels(Collection<Preference> preferences, Collection<PreferenceEntity> preferenceEntities,
			boolean fetchChild) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<PreferenceEntity> getEntities(Collection<PreferenceEntity> preferenceEntities,
			Collection<Preference> preferences, boolean fetchChild) {
		// TODO Auto-generated method stub
		return null;
	}

}
