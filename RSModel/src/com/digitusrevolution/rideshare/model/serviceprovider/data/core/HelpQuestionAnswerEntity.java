package com.digitusrevolution.rideshare.model.serviceprovider.data.core;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="help")
public class HelpQuestionAnswerEntity {
	@Id
	@GeneratedValue
	private int id;
	private String question;
	@Column(columnDefinition="TEXT")
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

	
}
