package com.digitusrevolution.rideshare.serviceprovider.domain.core;

import java.util.ArrayList;
import java.util.List;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotFoundException;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.inf.DomainObjectPKInteger;
import com.digitusrevolution.rideshare.common.inf.GenericDAO;
import com.digitusrevolution.rideshare.common.mapper.serviceprovider.core.HelpQuestionAnswerMapper;
import com.digitusrevolution.rideshare.model.serviceprovider.data.core.HelpQuestionAnswerEntity;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.HelpQuestionAnswer;

public class HelpQuestionAnswerDO implements DomainObjectPKInteger<HelpQuestionAnswer>{

	private HelpQuestionAnswer questionAnswer;
	private HelpQuestionAnswerEntity questionAnswerEntity;
	private HelpQuestionAnswerMapper questionAnswerMapper;
	private final GenericDAO<HelpQuestionAnswerEntity, Integer> genericDAO;
	
	public HelpQuestionAnswerDO() {
		questionAnswer = new HelpQuestionAnswer();
		questionAnswerEntity = new HelpQuestionAnswerEntity();
		questionAnswerMapper = new HelpQuestionAnswerMapper();
		genericDAO = new GenericDAOImpl<>(HelpQuestionAnswerEntity.class);
	}

	public void setHelpQuestionAnswer(HelpQuestionAnswer questionAnswer) {
		this.questionAnswer = questionAnswer;
		questionAnswerEntity = questionAnswerMapper.getEntity(questionAnswer, true);
	}

	public void setHelpQuestionAnswerEntity(HelpQuestionAnswerEntity questionAnswerEntity) {
		this.questionAnswerEntity = questionAnswerEntity;
		questionAnswer = questionAnswerMapper.getDomainModel(questionAnswerEntity, false);
	}

	@Override
	public List<HelpQuestionAnswer> getAll() {
		List<HelpQuestionAnswer> questionAnswers = new ArrayList<>();
		List<HelpQuestionAnswerEntity> questionAnswerEntities = genericDAO.getAll();
		for (HelpQuestionAnswerEntity questionAnswerEntity : questionAnswerEntities) {
			setHelpQuestionAnswerEntity(questionAnswerEntity);
			questionAnswers.add(questionAnswer);
		}
		return questionAnswers;
	}

	@Override
	public void update(HelpQuestionAnswer questionAnswer) {
		if (questionAnswer.getId()==0){
			throw new InvalidKeyException("Updated failed due to Invalid key: "+questionAnswer.getId());
		}
		setHelpQuestionAnswer(questionAnswer);
		genericDAO.update(questionAnswerEntity);				
	}

	@Override
	public void fetchChild() {
		questionAnswer = questionAnswerMapper.getDomainModelChild(questionAnswer, questionAnswerEntity);
	}

	@Override
	public int create(HelpQuestionAnswer questionAnswer) {
		setHelpQuestionAnswer(questionAnswer);
		int id = genericDAO.create(questionAnswerEntity);
		return id;
	}

	@Override
	public HelpQuestionAnswer get(int id) {
		questionAnswerEntity = genericDAO.get(id);
		if (questionAnswerEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		setHelpQuestionAnswerEntity(questionAnswerEntity);
		return questionAnswer;
	}

	@Override
	public HelpQuestionAnswer getAllData(int id) {
		get(id);
		fetchChild();
		return questionAnswer;
	}

	@Override
	public void delete(int id) {
		questionAnswer = get(id);
		setHelpQuestionAnswer(questionAnswer);
		genericDAO.delete(questionAnswerEntity);			
	}

}
