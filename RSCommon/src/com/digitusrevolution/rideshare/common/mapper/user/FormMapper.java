package com.digitusrevolution.rideshare.common.mapper.user;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.model.user.data.FormEntity;
import com.digitusrevolution.rideshare.model.user.domain.Form;

public class FormMapper implements Mapper<Form, FormEntity>{

	@Override
	public FormEntity getEntity(Form form, boolean fetchChild) {
		FormEntity formEntity = new FormEntity();
		formEntity.setId(form.getId());
		formEntity.setQuestions(form.getQuestions());
		formEntity.setRemark(form.getRemark());
		formEntity.setEmailVerification(form.isEmailVerification());
		return formEntity;
	}

	@Override
	public FormEntity getEntityChild(Form form, FormEntity formEntity) {
		return formEntity;
	}

	@Override
	public Form getDomainModel(FormEntity formEntity, boolean fetchChild) {
		Form form = new Form();
		form.setId(formEntity.getId());
		form.setQuestions(formEntity.getQuestions());
		form.setRemark(formEntity.getRemark());
		form.setEmailVerification(formEntity.isEmailVerification());
		return form;
	}

	@Override
	public Form getDomainModelChild(Form form, FormEntity formEntity) {
		return form;
	}

	@Override
	public Collection<Form> getDomainModels(Collection<Form> forms, Collection<FormEntity> formEntities,
			boolean fetchChild) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<FormEntity> getEntities(Collection<FormEntity> formEntities, Collection<Form> forms,
			boolean fetchChild) {
		// TODO Auto-generated method stub
		return null;
	}

}
