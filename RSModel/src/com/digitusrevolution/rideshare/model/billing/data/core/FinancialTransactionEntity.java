package com.digitusrevolution.rideshare.model.billing.data.core;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.digitusrevolution.rideshare.model.billing.domain.core.PaymentGateway;
import com.digitusrevolution.rideshare.model.billing.domain.core.TransactionStatus;
import com.digitusrevolution.rideshare.model.billing.domain.core.TransactionType;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;

@Entity
@Table(name="financial_transaction")
public class FinancialTransactionEntity{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private ZonedDateTime dateTime;
	@Column
	@Enumerated(EnumType.STRING)
	private TransactionType type;
	private float amount;
	private String remark;
	@Column
	@Enumerated(EnumType.STRING)
	private TransactionStatus status;
	@Column
	@Enumerated(EnumType.STRING)
	private PaymentGateway paymentGateway;
	@OneToOne
	private TransactionEntity walletTransaction;
	@OneToOne
	private UserEntity user;
	private String pgTransactionStatus; 
	private String pgResponseCode;
	private String pgResponseMsg;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
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
	public TransactionEntity getWalletTransaction() {
		return walletTransaction;
	}
	public void setWalletTransaction(TransactionEntity walletTransaction) {
		this.walletTransaction = walletTransaction;
	}
	public TransactionStatus getStatus() {
		return status;
	}
	public void setStatus(TransactionStatus status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public PaymentGateway getPaymentGateway() {
		return paymentGateway;
	}
	public void setPaymentGateway(PaymentGateway paymentGateway) {
		this.paymentGateway = paymentGateway;
	}
	public UserEntity getUser() {
		return user;
	}
	public void setUser(UserEntity user) {
		this.user = user;
	}
	public String getPgTransactionStatus() {
		return pgTransactionStatus;
	}
	public void setPgTransactionStatus(String pgTransactionStatus) {
		this.pgTransactionStatus = pgTransactionStatus;
	}
	public String getPgResponseCode() {
		return pgResponseCode;
	}
	public void setPgResponseCode(String pgResponseCode) {
		this.pgResponseCode = pgResponseCode;
	}
	public String getPgResponseMsg() {
		return pgResponseMsg;
	}
	public void setPgResponseMsg(String pgResponseMsg) {
		this.pgResponseMsg = pgResponseMsg;
	}
	
	
}
