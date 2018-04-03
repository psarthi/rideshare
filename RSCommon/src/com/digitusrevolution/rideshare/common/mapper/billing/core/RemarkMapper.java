package com.digitusrevolution.rideshare.common.mapper.billing.core;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.model.billing.data.core.RemarkEntity;
import com.digitusrevolution.rideshare.model.billing.domain.core.Remark;

public class RemarkMapper implements Mapper<Remark, RemarkEntity>{

	@Override
	public RemarkEntity getEntity(Remark remark, boolean fetchChild) {
		RemarkEntity remarkEntity = new RemarkEntity();
		remarkEntity.setMessage(remark.getMessage());
		remarkEntity.setPaidBy(remark.getPaidBy());
		remarkEntity.setPaidTo(remark.getPaidTo());
		remarkEntity.setPurpose(remark.getPurpose());
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
		remark.setMessage(remarkEntity.getMessage());
		remark.setPaidBy(remarkEntity.getPaidBy());
		remark.setPaidTo(remarkEntity.getPaidTo());
		remark.setPurpose(remarkEntity.getPurpose());
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
