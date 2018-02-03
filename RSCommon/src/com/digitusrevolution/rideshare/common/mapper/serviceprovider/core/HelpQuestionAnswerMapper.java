package com.digitusrevolution.rideshare.common.mapper.serviceprovider.core;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.model.serviceprovider.data.core.HelpQuestionAnswerEntity;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.HelpQuestionAnswer;

public class HelpQuestionAnswerMapper implements Mapper<HelpQuestionAnswer, HelpQuestionAnswerEntity>{

	@Override
	public HelpQuestionAnswerEntity getEntity(HelpQuestionAnswer helpQuestionAnswer, boolean fetchChild) {
		HelpQuestionAnswerEntity questionAnswerEntity = new HelpQuestionAnswerEntity();
		questionAnswerEntity.setId(helpQuestionAnswer.getId());
		questionAnswerEntity.setQuestion(helpQuestionAnswer.getQuestion());
		questionAnswerEntity.setAnswer(helpQuestionAnswer.getAnswer());
		return questionAnswerEntity;
	}

	@Override
	public HelpQuestionAnswerEntity getEntityChild(HelpQuestionAnswer helpQuestionAnswer, HelpQuestionAnswerEntity helpQuestionAnswerEntity) {
		return helpQuestionAnswerEntity;
	}

	@Override
	public HelpQuestionAnswer getDomainModel(HelpQuestionAnswerEntity helpQuestionAnswerEntity, boolean fetchChild) {
		HelpQuestionAnswer questionAnswer = new HelpQuestionAnswer();
		questionAnswer.setId(helpQuestionAnswerEntity.getId());
		questionAnswer.setQuestion(helpQuestionAnswerEntity.getQuestion());
		questionAnswer.setAnswer(helpQuestionAnswerEntity.getAnswer());
		return questionAnswer;
	}

	@Override
	public HelpQuestionAnswer getDomainModelChild(HelpQuestionAnswer helpQuestionAnswer, HelpQuestionAnswerEntity helpQuestionAnswerEntity) {
		return helpQuestionAnswer;
	}

	@Override
	public Collection<HelpQuestionAnswer> getDomainModels(Collection<HelpQuestionAnswer> helpQuestionAnswers,
			Collection<HelpQuestionAnswerEntity> helpQuestionAnswerEntities, boolean fetchChild) {
		for (HelpQuestionAnswerEntity questionAnswerEntity: helpQuestionAnswerEntities) {
			HelpQuestionAnswer questionAnswer = new HelpQuestionAnswer();
			questionAnswer = getDomainModel(questionAnswerEntity, fetchChild);
			helpQuestionAnswers.add(questionAnswer);
		}
		return helpQuestionAnswers;
	}

	@Override
	public Collection<HelpQuestionAnswerEntity> getEntities(Collection<HelpQuestionAnswerEntity> helpQuestionAnswerEntities,
			Collection<HelpQuestionAnswer> helpQuestionAnswers, boolean fetchChild) {
		for (HelpQuestionAnswer questionAnswer: helpQuestionAnswers) {
			HelpQuestionAnswerEntity questionAnswerEntity = new HelpQuestionAnswerEntity();
			questionAnswerEntity = getEntity(questionAnswer, fetchChild);
			helpQuestionAnswerEntities.add(questionAnswerEntity);
		}
		return helpQuestionAnswerEntities;
	}

}
