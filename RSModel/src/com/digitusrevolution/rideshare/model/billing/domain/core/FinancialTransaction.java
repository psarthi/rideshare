package com.digitusrevolution.rideshare.model.billing.domain.core;

import java.time.ZonedDateTime;

import com.digitusrevolution.rideshare.model.user.domain.core.User;

public class FinancialTransaction{
	
	private long id;
	private ZonedDateTime dateTime;
	private TransactionType type;
	private float amount;
	private String remark;
	private TransactionStatus status;
	private PaymentGateway paymentGateway;
	private Transaction walletTransaction;
	private User user;
	private String pgTransactionId;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Transaction getWalletTransaction() {
		return walletTransaction;
	}
	public void setWalletTransaction(Transaction walletTransaction) {
		this.walletTransaction = walletTransaction;
	}
	public TransactionStatus getStatus() {
		return status;
	}
	public void setStatus(TransactionStatus status) {
		this.status = status;
	}
	public PaymentGateway getPaymentGateway() {
		return paymentGateway;
	}
	public void setPaymentGateway(PaymentGateway paymentGateway) {
		this.paymentGateway = paymentGateway;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
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
	public String getPgTransactionId() {
		return pgTransactionId;
	}
	public void setPgTransactionId(String pgTransactionId) {
		this.pgTransactionId = pgTransactionId;
	}

	
}
