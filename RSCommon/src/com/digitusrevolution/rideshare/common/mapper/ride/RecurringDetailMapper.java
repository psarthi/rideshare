package com.digitusrevolution.rideshare.common.mapper.ride;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.model.ride.data.RecurringDetailEntity;
import com.digitusrevolution.rideshare.model.ride.domain.RecurringDetail;
import com.digitusrevolution.rideshare.model.ride.domain.WeekDay;

public class RecurringDetailMapper implements Mapper<RecurringDetail, RecurringDetailEntity>{

	@Override
	public RecurringDetailEntity getEntity(RecurringDetail recurringDetail, boolean fetchChild) {
		RecurringDetailEntity recurringDetailEntity = new RecurringDetailEntity();
		recurringDetailEntity.setRecurringStatus(recurringDetail.getRecurringStatus());
		
		//VERY IMP - Don't just set weekdays as recurringDetail.setWeekDays(recurringDetailEntity.getWeekDays) as we are setting proxy object reference here 
		//and once session is closed its child element is not accessible, so we will iterate through all of them and then set the weekdays so that we have now the data read through instead of just in proxy
		//if we don't do this, then it will throw lazy initialization failure exception and jsonparser exception
		for (WeekDay weekDay: recurringDetail.getWeekDays()) {
			recurringDetailEntity.getWeekDays().add(weekDay);
		}
	
		if (fetchChild) {
			recurringDetailEntity = getEntityChild(recurringDetail, recurringDetailEntity);
		}
		
		return recurringDetailEntity;
	}

	@Override
	public RecurringDetailEntity getEntityChild(RecurringDetail recurringDetail, RecurringDetailEntity recurringDetailEntity) {
		
		return recurringDetailEntity;
	}

	@Override
	public RecurringDetail getDomainModel(RecurringDetailEntity recurringDetailEntity, boolean fetchChild) {
		RecurringDetail recurringDetail = new RecurringDetail();
		recurringDetail.setRecurringStatus(recurringDetailEntity.getRecurringStatus());

		for (WeekDay weekDay: recurringDetailEntity.getWeekDays()) {
			recurringDetail.getWeekDays().add(weekDay);
		}
			
		if (fetchChild) {
			recurringDetail = getDomainModelChild(recurringDetail, recurringDetailEntity);
		}
		
		return recurringDetail;
	}

	@Override
	public RecurringDetail getDomainModelChild(RecurringDetail recurringDetail, RecurringDetailEntity recurringDetailEntity) {

		return recurringDetail;
	}

	@Override
	public Collection<RecurringDetail> getDomainModels(Collection<RecurringDetail> models,
			Collection<RecurringDetailEntity> entities, boolean fetchChild) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<RecurringDetailEntity> getEntities(Collection<RecurringDetailEntity> entities,
			Collection<RecurringDetail> models, boolean fetchChild) {
		// TODO Auto-generated method stub
		return null;
	}

}
