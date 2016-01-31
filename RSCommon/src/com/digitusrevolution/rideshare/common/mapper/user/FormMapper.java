package com.digitusrevolution.rideshare.common.mapper.user;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.model.user.data.FormEntity;
import com.digitusrevolution.rideshare.model.user.domain.MembershipForm;

public class FormMapper implements Mapper<MembershipForm, FormEntity>{

	@Override
	public FormEntity getEntity(MembershipForm form, boolean fetchChild) {
		FormEntity formEntity = new FormEntity();
		formEntity.setId(form.getId());
		formEntity.setUserUniqueIdentifierName(form.getUserUniqueIdentifierName());
		formEntity.setQuestions(form.getQuestions());
		formEntity.setRemark(form.getRemark());
		return formEntity;
	}

	@Override
	public FormEntity getEntityChild(MembershipForm form, FormEntity formEntity) {
		return formEntity;
	}

	@Override
	public MembershipForm getDomainModel(FormEntity formEntity, boolean fetchChild) {
		MembershipForm form = new MembershipForm();
		form.setId(formEntity.getId());
		form.setUserUniqueIdentifierName(formEntity.getUserUniqueIdentifierName());
		form.setQuestions(formEntity.getQuestions());
		form.setRemark(formEntity.getRemark());
		return form;
	}

	@Override
	public MembershipForm getDomainModelChild(MembershipForm form, FormEntity formEntity) {
		return form;
	}

	@Override
	public Collection<MembershipForm> getDomainModels(Collection<MembershipForm> forms, Collection<FormEntity> formEntities,
			boolean fetchChild) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<FormEntity> getEntities(Collection<FormEntity> formEntities, Collection<MembershipForm> forms,
			boolean fetchChild) {
		// TODO Auto-generated method stub
		return null;
	}

}
