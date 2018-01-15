package com.digitusrevolution.rideshare.model.user.dto;

public class MembershipStatus {
    private boolean member;
    private boolean admin;
    
	public boolean isMember() {
		return member;
	}
	public void setMember(boolean member) {
		this.member = member;
	}
	public boolean isAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
}
