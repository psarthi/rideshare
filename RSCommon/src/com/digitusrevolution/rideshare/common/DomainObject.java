package com.digitusrevolution.rideshare.common;

public interface DomainObject {
	
	void mapDomainModelToDataModel();

	void mapDataModelToDomainModel();
	
	void mapChildDataModelToDomainModel();
	
	void mapChildDomainModelToDataModel();

}