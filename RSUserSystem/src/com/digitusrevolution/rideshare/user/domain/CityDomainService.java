package com.digitusrevolution.rideshare.user.domain;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.inf.DomainService;
import com.digitusrevolution.rideshare.model.user.data.CityEntity;
import com.digitusrevolution.rideshare.model.user.domain.City;
import com.digitusrevolution.rideshare.user.data.CityDAO;

public class CityDomainService implements DomainService<City>{
	
	private static final Logger logger = LogManager.getLogger(CityDomainService.class.getName());
	private final CityDAO cityDAO;
	
	public CityDomainService() {
		cityDAO = new CityDAO();
	}
	
	public int create(City city){
		logger.entry();
		CityDO cityDO = new CityDO();
		cityDO.setCity(city);
		int id = cityDAO.create(cityDO.getCityEntity());
		logger.exit();
		return id;
	}

	public City get(int id){
		CityDO cityDO = new CityDO();
		CityEntity cityEntity = new CityEntity();
		cityEntity = cityDAO.get(id);
		if (cityEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		cityDO.setCityEntity(cityEntity);
		return cityDO.getCity();
	}
	
	public City getChild(int id){
		
	 // Don't try to call getUser to avoid duplicate code, else you would loose persistent entity object which is required for lazy fetch 

		CityDO cityDO = new CityDO();
		CityEntity cityEntity = new CityEntity();
		cityEntity = cityDAO.get(id);
		if (cityEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		cityDO.setCityEntity(cityEntity);		
		cityDO.fetchChild();
		return cityDO.getCity();
	}
	
	public List<City> getAll(){
		List<CityEntity> cityEntities = new ArrayList<>();
		List<City> cities = new ArrayList<>();
		cityEntities = cityDAO.getAll();
		for (CityEntity cityEntity : cityEntities) {
			CityDO cityDO = new CityDO();
			cityDO.setCityEntity(cityEntity);
			cities.add(cityDO.getCity());
		}
		return cities;
	}
	
	public void update(City city){
		CityDO cityDO = new CityDO();
		cityDO.setCity(city);
		cityDAO.update(cityDO.getCityEntity());
	}

	public void delete(City city){
		CityDO cityDO = new CityDO();
		cityDO.setCity(city);
		cityDAO.delete(cityDO.getCityEntity());
	}


}
