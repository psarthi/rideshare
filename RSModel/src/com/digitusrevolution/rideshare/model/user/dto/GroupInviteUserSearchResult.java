package com.digitusrevolution.rideshare.model.user.dto;

public class GroupInviteUserSearchResult {
	
	private BasicUser user;
	private boolean member;
	private boolean invited;
	private boolean requestSubmitted;
	
	public BasicUser getUser() {
		return user;
	}
	public void setUser(BasicUser user) {
		this.user = user;
	}
	public boolean isMember() {
		return member;
	}
	public void setMember(boolean member) {
		this.member = member;
	}
	public boolean isInvited() {
		return invited;
	}
	public void setInvited(boolean invited) {
		this.invited = invited;
	}
	public boolean isRequestSubmitted() {
		return requestSubmitted;
	}
	public void setRequestSubmitted(boolean requestSubmitted) {
		this.requestSubmitted = requestSubmitted;
	}
	
	
}
