package com.digitusrevolution.rideshare.model.serviceprovider.domain.core;

import java.util.Collection;
import java.util.HashSet;

import com.digitusrevolution.rideshare.model.billing.domain.core.Transaction;
import com.digitusrevolution.rideshare.model.user.domain.Photo;

public class RewardReimbursementTransaction extends RewardTransaction {

	private Collection<Photo> photos = new HashSet<Photo>();
	private ReimbursementStatus status;
	private int approvedAmount;
	private String remarks;
	private Transaction transaction;
	
	public Collection<Photo> getPhotos() {
		return photos;
	}
	public void setPhotos(Collection<Photo> photos) {
		this.photos = photos;
	}
	public ReimbursementStatus getStatus() {
		return status;
	}
	public void setStatus(ReimbursementStatus status) {
		this.status = status;
	}
	public int getApprovedAmount() {
		return approvedAmount;
	}
	public void setApprovedAmount(int approvedAmount) {
		this.approvedAmount = approvedAmount;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Transaction getTransaction() {
		return transaction;
	}
	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}
}
