package com.digitusrevolution.rideshare.model.serviceprovider.data.core;

import java.time.ZonedDateTime;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;

@MappedSuperclass
public class RewardTransactionEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@OneToOne
	private OfferEntity offer;
	private ZonedDateTime rewardTransactionDateTime;
	@OneToOne
	private UserEntity user;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public ZonedDateTime getRewardTransactionDateTime() {
		return rewardTransactionDateTime;
	}
	public void setRewardTransactionDateTime(ZonedDateTime rewardTransactionDateTime) {
		this.rewardTransactionDateTime = rewardTransactionDateTime;
	}
	public OfferEntity getOffer() {
		return offer;
	}
	public void setOffer(OfferEntity offer) {
		this.offer = offer;
	}
	public UserEntity getUser() {
		return user;
	}
	public void setUser(UserEntity user) {
		this.user = user;
	}
	
	
}
