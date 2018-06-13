package com.digitusrevolution.rideshare.model.serviceprovider.data.core;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.ReimbursementStatus;
import com.digitusrevolution.rideshare.model.user.data.PhotoEntity;

@Entity
@Table(name="reward_reimbursement_transaction")
public class RewardReimbursementTransactionEntity extends RewardTransactionEntity {

	@OneToMany(cascade=CascadeType.ALL)
	@JoinTable(name="reward_reimbursement_transaction_photo",
	joinColumns=@JoinColumn(name="reward_reimbursement_transaction_id"),
	inverseJoinColumns=@JoinColumn(name="photo_id"))
	private Collection<PhotoEntity> photos = new HashSet<PhotoEntity>();
	@Column
	@Enumerated(EnumType.STRING)
	private ReimbursementStatus status;

	
	public Collection<PhotoEntity> getPhotos() {
		return photos;
	}
	public void setPhotos(Collection<PhotoEntity> photos) {
		this.photos = photos;
	}
	public ReimbursementStatus getStatus() {
		return status;
	}
	public void setStatus(ReimbursementStatus status) {
		this.status = status;
	}
}
