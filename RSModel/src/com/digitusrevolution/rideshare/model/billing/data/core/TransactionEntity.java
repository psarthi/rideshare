package com.digitusrevolution.rideshare.model.billing.data.core;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.digitusrevolution.rideshare.model.billing.domain.core.Remark;
import com.digitusrevolution.rideshare.model.billing.domain.core.TransactionType;

@Entity
@Table(name="transaction")
public class TransactionEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private ZonedDateTime dateTime;
	@Column
	@Enumerated(EnumType.STRING)
	private TransactionType type;
	private float amount;
	@Embedded
	private RemarkEntity remark;
	
	public ZonedDateTime getDateTime() {
		return dateTime;
	}
	public void setDateTime(ZonedDateTime dateTime) {
		this.dateTime = dateTime;
	}
	public TransactionType getType() {
		return type;
	}
	public void setType(TransactionType type) {
		this.type = type;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(amount);
		result = prime * result + ((dateTime == null) ? 0 : dateTime.hashCode());
		result = prime * result + id;
		result = prime * result + ((remark == null) ? 0 : remark.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof TransactionEntity)) {
			return false;
		}
		TransactionEntity other = (TransactionEntity) obj;
		if (Float.floatToIntBits(amount) != Float.floatToIntBits(other.amount)) {
			return false;
		}
		if (dateTime == null) {
			if (other.dateTime != null) {
				return false;
			}
		} else if (!dateTime.equals(other.dateTime)) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		if (remark == null) {
			if (other.remark != null) {
				return false;
			}
		} else if (!remark.equals(other.remark)) {
			return false;
		}
		if (type != other.type) {
			return false;
		}
		return true;
	}
	public RemarkEntity getRemark() {
		return remark;
	}
	public void setRemark(RemarkEntity remark) {
		this.remark = remark;
	}
	
}
