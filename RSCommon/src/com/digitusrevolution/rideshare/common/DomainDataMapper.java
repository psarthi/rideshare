package com.digitusrevolution.rideshare.common;

public interface DomainDataMapper {

	void mapDomainModelToDataModel();

	void mapDataModelToDomainModel();
	
	void mapChildDataModelToDomainModel();
	
	void mapChildDomainModelToDataModel();

}