package com.digitusrevolution.rideshare.model.billing.dto;

public class WalletInfo {
	
	private float pendingBillsAmount;
	private float rewardMoneyBalance;
	
	public float getPendingBillsAmount() {
		return pendingBillsAmount;
	}
	public void setPendingBillsAmount(float pendingBillsAmount) {
		this.pendingBillsAmount = pendingBillsAmount;
	}
	public float getRewardMoneyBalance() {
		return rewardMoneyBalance;
	}
	public void setRewardMoneyBalance(float rewardMoneyBalance) {
		this.rewardMoneyBalance = rewardMoneyBalance;
	}
	
	
}
