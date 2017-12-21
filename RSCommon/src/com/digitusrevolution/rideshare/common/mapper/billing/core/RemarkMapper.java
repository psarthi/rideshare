package com.digitusrevolution.rideshare.common.mapper.billing.core;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.model.billing.data.core.RemarkEntity;
import com.digitusrevolution.rideshare.model.billing.domain.core.Remark;

public class RemarkMapper implements Mapper<Remark, RemarkEntity>{

	@Override
	public RemarkEntity getEntity(Remark remark, boolean fetchChild) {
		RemarkEntity remarkEntity = new RemarkEntity();
		remarkEntity.setBillNumber(remark.getBillNumber());
		remarkEntity.setMessage(remark.getMessage());
		remarkEntity.setPaidBy(remark.getPaidBy());
		remarkEntity.setPaidTo(remark.getPaidTo());
		remarkEntity.setPurpose(remark.getPurpose());
		remarkEntity.setRideId(remark.getRideId());
		remarkEntity.setRideRequestId(remark.getRideRequestId());
		return remarkEntity;
	}

	@Override
	public RemarkEntity getEntityChild(Remark model, RemarkEntity entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Remark getDomainModel(RemarkEntity remarkEntity, boolean fetchChild) {
		Remark remark = new Remark();
		remark.setBillNumber(remarkEntity.getBillNumber());
		remark.setMessage(remarkEntity.getMessage());
		remark.setPaidBy(remarkEntity.getPaidBy());
		remark.setPaidTo(remarkEntity.getPaidTo());
		remark.setPurpose(remarkEntity.getPurpose());
		remark.setRideId(remarkEntity.getRideId());
		remark.setRideRequestId(remarkEntity.getRideRequestId());
		// TODO Auto-generated method stub
		return remark;
	}

	@Override
	public Remark getDomainModelChild(Remark model, RemarkEntity entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Remark> getDomainModels(Collection<Remark> models, Collection<RemarkEntity> entities,
			boolean fetchChild) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<RemarkEntity> getEntities(Collection<RemarkEntity> entities, Collection<Remark> models,
			boolean fetchChild) {
		// TODO Auto-generated method stub
		return null;
	}

}
