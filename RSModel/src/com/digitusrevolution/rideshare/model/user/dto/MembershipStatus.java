package com.digitusrevolution.rideshare.model.user.dto;

public class MembershipStatus {
    private boolean member;
    private boolean admin;
    private boolean invited;
    
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
	public boolean isInvited() {
		return invited;
	}
	public void setInvited(boolean invited) {
		this.invited = invited;
	}
}
