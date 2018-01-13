package com.digitusrevolution.rideshare.model.user.dto;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import com.digitusrevolution.rideshare.model.user.domain.MembershipForm;
import com.digitusrevolution.rideshare.model.user.domain.Photo;
import com.digitusrevolution.rideshare.model.user.domain.GroupFeedback;
import com.digitusrevolution.rideshare.model.user.domain.MembershipRequest;

public class FullGroup extends BasicGroup{

	private Collection<BasicUser> members = new HashSet<BasicUser>();
	private Collection<BasicUser> admins = new HashSet<BasicUser>();
	private Collection<GroupFeedback> feedbacks = new LinkedList<GroupFeedback>();
	private Collection<MembershipRequest> membershipRequests = new HashSet<MembershipRequest>();

	public Collection<BasicUser> getMembers() {
		return members;
	}

	public void setMembers(Collection<BasicUser> members) {
		this.members = members;
	}
	public Collection<BasicUser> getAdmins() {
		return admins;
	}

	public void setAdmins(Collection<BasicUser> admins) {
		this.admins = admins;
	}

	public Collection<GroupFeedback> getFeedbacks() {
		return feedbacks;
	}

	public void setFeedbacks(Collection<GroupFeedback> feedbacks) {
		this.feedbacks = feedbacks;
	}

	public Collection<MembershipRequest> getMembershipRequests() {
		return membershipRequests;
	}

	public void setMembershipRequests(Collection<MembershipRequest> membershipRequests) {
		this.membershipRequests = membershipRequests;
	}
}








































