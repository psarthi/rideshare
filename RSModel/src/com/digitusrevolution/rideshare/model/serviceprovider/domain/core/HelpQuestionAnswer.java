package com.digitusrevolution.rideshare.model.serviceprovider.domain.core;

public class HelpQuestionAnswer {
	private int id;
	private HelpCategory category;
	private String question;
	private String answer;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public HelpCategory getCategory() {
		return category;
	}
	public void setCategory(HelpCategory category) {
		this.category = category;
	}	
}
