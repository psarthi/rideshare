package com.digitusrevolution.rideshare.common.inf;

import javax.ws.rs.core.Response;

public interface DomainResourceLong<M> {

	/**
	 * 
	 * @param id Id of domain model
	 * @param fetchChild value should be either true or false, this value should be passed as query parameter e.g. url?fetchchild=true. True value would return all data and false would return basic data of the model
	 * @return return domain model
	 */
	Response get(long id, String fetchChild);

	/**
	 * 
	 * @return list of domain models
	 */
	Response getAll();
}