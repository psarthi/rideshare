package com.digitusrevolution.rideshare.model.serviceprovider.dto;

import java.time.ZonedDateTime;
import java.util.LinkedList;

public class ReimbursementRequest {
	
	private ZonedDateTime rewardTransactionDateTime;
	private LinkedList<byte[]> images;

	public LinkedList<byte[]> getImages() {
		return images;
	}

	public void setImages(LinkedList<byte[]> images) {
		this.images = images;
	}

	public ZonedDateTime getRewardTransactionDateTime() {
		return rewardTransactionDateTime;
	}

	public void setRewardTransactionDateTime(ZonedDateTime rewardTransactionDateTime) {
		this.rewardTransactionDateTime = rewardTransactionDateTime;
	}
	
}
