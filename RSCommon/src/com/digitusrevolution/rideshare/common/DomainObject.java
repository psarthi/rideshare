package com.digitusrevolution.rideshare.common;

public interface DomainObject {
	
	void mapDomainModelToDataModel();

	void mapDataModelToDomainModel();
	
	void mapChildDomainModelToDataModel();

	void mapChildDataModelToDomainModel();
}