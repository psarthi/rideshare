package com.digitusrevolution.rideshare.common.mapper.user;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.common.mapper.ride.TrustNetworkMapper;
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
		preferenceEntity.setSeatOffered(preference.getSeatOffered());
		preferenceEntity.setLuggageCapacityRequired(preference.getLuggageCapacityRequired());
		TrustNetworkMapper trustNetworkMapper = new TrustNetworkMapper();
		preferenceEntity.setTrustNetwork(trustNetworkMapper.getEntity(preference.getTrustNetwork(), fetchChild));
		preferenceEntity.setSexPreference(preference.getSexPreference());
		preferenceEntity.setMinProfileRating(preference.getMinProfileRating());
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
		preference.setSeatOffered(preferenceEntity.getSeatOffered());
		preference.setLuggageCapacityRequired(preferenceEntity.getLuggageCapacityRequired());
		TrustNetworkMapper trustNetworkMapper = new TrustNetworkMapper();
		preference.setTrustNetwork(trustNetworkMapper.getDomainModel(preferenceEntity.getTrustNetwork(), fetchChild));
		preference.setSexPreference(preferenceEntity.getSexPreference());
		preference.setMinProfileRating(preferenceEntity.getMinProfileRating());
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
