package com.digitusrevolution.rideshare.model.billing.domain.core;

import java.time.ZonedDateTime;

import com.digitusrevolution.rideshare.model.billing.data.core.TransactionEntity;

public class Transaction {
	
	private TransactionEntity entity = new TransactionEntity();
	private int id;
	private ZonedDateTime dateTime;
	private TransactionType type;
	private float amount;
	private String remark;
	
	public ZonedDateTime getDateTime() {
		dateTime = entity.getDateTime();
		return dateTime;
	}
	public void setDateTime(ZonedDateTime dateTime) {
		this.dateTime = dateTime;
		entity.setDateTime(dateTime);
	}
	public TransactionType getType() {
		type = entity.getType();
		return type;
	}
	public void setType(TransactionType type) {
		this.type = type;
		entity.setType(type);
	}
	public float getAmount() {
		amount = entity.getAmount();
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
		entity.setAmount(amount);
	}
	public String getRemark() {
		remark = entity.getRemark();
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
		entity.setRemark(remark);
	}
	public int getId() {
		id = entity.getId();
		return id;
	}
	public void setId(int id) {
		this.id = id;
		entity.setId(id);
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
		if (!(obj instanceof Transaction)) {
			return false;
		}
		Transaction other = (Transaction) obj;
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
	public TransactionEntity getEntity() {
		return entity;
	}
	public void setEntity(TransactionEntity entity) {
		this.entity = entity;
	}

}
