package com.digitusrevolution.rideshare.model.user.domain.core;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import com.digitusrevolution.rideshare.model.user.domain.MembershipForm;
import com.digitusrevolution.rideshare.model.user.domain.Photo;
import com.digitusrevolution.rideshare.model.user.domain.GroupFeedback;
import com.digitusrevolution.rideshare.model.user.domain.MembershipRequest;

public class Group {

	private int id;
	private String name;
	private Photo photo;
	
	private Collection<User> members = new HashSet<User>();
	private User owner;
	private Collection<User> admins = new HashSet<User>();
	private Collection<GroupFeedback> feedbacks = new LinkedList<GroupFeedback>();
	private MembershipForm membershipForm;
	private ZonedDateTime createdDateTime;
	private String url;
	private String information;
	private Collection<MembershipRequest> membershipRequests = new HashSet<MembershipRequest>();
	private int genuineVotes;
	private int fakeVotes;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Photo getPhoto() {
		return photo;
	}

	public void setPhoto(Photo photo) {
		this.photo = photo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
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
		if (!(obj instanceof Group)) {
			return false;
		}
		Group other = (Group) obj;
		if (id != other.id) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (owner == null) {
			if (other.owner != null) {
				return false;
			}
		} else if (!owner.equals(other.owner)) {
			return false;
		}
		return true;
	}

	public Collection<User> getMembers() {
		return members;
	}

	public void setMembers(Collection<User> members) {
		this.members = members;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Collection<User> getAdmins() {
		return admins;
	}

	public void setAdmins(Collection<User> admins) {
		this.admins = admins;
	}

	public Collection<GroupFeedback> getFeedbacks() {
		return feedbacks;
	}

	public void setFeedbacks(Collection<GroupFeedback> feedbacks) {
		this.feedbacks = feedbacks;
	}

	public MembershipForm getMembershipForm() {
		return membershipForm;
	}

	public void setMembershipForm(MembershipForm membershipForm) {
		this.membershipForm = membershipForm;
	}

	public ZonedDateTime getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(ZonedDateTime createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
	}

	public Collection<MembershipRequest> getMembershipRequests() {
		return membershipRequests;
	}

	public void setMembershipRequests(Collection<MembershipRequest> membershipRequests) {
		this.membershipRequests = membershipRequests;
	}

	public int getGenuineVotes() {
		return genuineVotes;
	}

	public void setGenuineVotes(int genuineVotes) {
		this.genuineVotes = genuineVotes;
	}

	public int getFakeVotes() {
		return fakeVotes;
	}

	public void setFakeVotes(int fakeVotes) {
		this.fakeVotes = fakeVotes;
	}
	
	public boolean isMemberAdmin(int memberUserId){
		Collection<User> admins = getAdmins();
		for (User user : admins) {
			if (user.getId()==memberUserId){
				return true;
			}
		}
		return false;
	}
}








































