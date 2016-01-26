package com.digitusrevolution.rideshare.model.user.domain.core;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import com.digitusrevolution.rideshare.model.inf.DomainModel;
import com.digitusrevolution.rideshare.model.user.data.GroupFeedbackEntity;
import com.digitusrevolution.rideshare.model.user.data.MembershipRequestEntity;
import com.digitusrevolution.rideshare.model.user.data.core.GroupEntity;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.domain.Form;
import com.digitusrevolution.rideshare.model.user.domain.Photo;
import com.digitusrevolution.rideshare.model.user.domain.GroupFeedback;
import com.digitusrevolution.rideshare.model.user.domain.MembershipRequest;

public class Group implements DomainModel{

	private GroupEntity entity = new GroupEntity();
	private int id;
	private String name;
	private Photo photo = new Photo();
	
	private Collection<User> members = new HashSet<User>();
	private User owner = new User();
	private Collection<User> admins = new HashSet<User>();
	private Collection<GroupFeedback> feedbacks = new LinkedList<GroupFeedback>();
	private Form membershipForm = new Form();
	@SuppressWarnings("unused")
	private ZonedDateTime createdDateTime;
	@SuppressWarnings("unused")
	private String url;
	@SuppressWarnings("unused")
	private String information;
	private Collection<MembershipRequest> membershipRequests = new HashSet<MembershipRequest>();
	@SuppressWarnings("unused")
	private int genuineVotes;
	@SuppressWarnings("unused")
	private int fakeVotes;

	public int getId() {
		return entity.getId();
	}

	public void setId(int id) {
		this.id = id;
		entity.setId(id);
	}

	public String getName() {
		return entity.getName();
	}

	public void setName(String name) {
		this.name = name;
		entity.setName(name);
	}

	public Photo getPhoto() {
		photo.setEntity(entity.getPhoto());
		return photo;
	}

	public void setPhoto(Photo photo) {
		this.photo = photo;
		entity.setPhoto(photo.getEntity());
	}

	public GroupEntity getEntity() {
		return entity;
	}

	public void setEntity(GroupEntity entity) {
		this.entity = entity;
	}

	public Collection<User> getMembers() {
		Collection<UserEntity> memberEntities = entity.getMembers();
		for (UserEntity userEntity : memberEntities) {
			User member = new User();
			member.setEntity(userEntity);
			members.add(member);
		}
		return members;
	}

	public void setMembers(Collection<User> members) {
		this.members = members;
		for (User member : members) {
			entity.getMembers().add(member.getEntity());
		}
	}

	public User getOwner() {
		owner.setEntity(entity.getOwner());
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
		entity.setOwner(owner.getEntity());
	}

	public Collection<User> getAdmins() {
		Collection<UserEntity> adminEntities = entity.getAdmins();
		for (UserEntity userEntity : adminEntities) {
			User admin = new User();
			admin.setEntity(userEntity);
			admins.add(admin);
		}
		return admins;
	}

	public void setAdmins(Collection<User> admins) {
		this.admins = admins;
		for (User user : admins) {
			entity.getAdmins().add(user.getEntity());
		}
	}

	public Collection<GroupFeedback> getFeedbacks() {
		Collection<GroupFeedbackEntity> feedbackEntities = entity.getFeedbacks();
		for (GroupFeedbackEntity groupFeedbackEntity : feedbackEntities) {
			GroupFeedback feedback = new GroupFeedback();
			feedback.setEntity(groupFeedbackEntity);
			feedbacks.add(feedback);
		}
		return feedbacks;
	}

	public void setFeedbacks(Collection<GroupFeedback> feedbacks) {
		this.feedbacks = feedbacks;
		for (GroupFeedback groupFeedback : feedbacks) {
			entity.getFeedbacks().add(groupFeedback.getEntity());
		}
	}

	public Form getMembershipForm() {
		membershipForm.setEntity(entity.getMembershipForm());
		return membershipForm;
	}

	public void setMembershipForm(Form membershipForm) {
		this.membershipForm = membershipForm;
		entity.setMembershipForm(membershipForm.getEntity());
	}

	public ZonedDateTime getCreatedDateTime() {
		return entity.getCreatedDateTime();
	}

	public void setCreatedDateTime(ZonedDateTime createdDateTime) {
		this.createdDateTime = createdDateTime;
		entity.setCreatedDateTime(createdDateTime);
	}

	public String getUrl() {
		return entity.getUrl();
	}

	public void setUrl(String url) {
		this.url = url;
		entity.setUrl(url);
	}

	public String getInformation() {
		return entity.getInformation();
	}

	public void setInformation(String information) {
		this.information = information;
		entity.setInformation(information);
	}

	public Collection<MembershipRequest> getMembershipRequests() {
		Collection<MembershipRequestEntity> membershipRequestEntities = entity.getMembershipRequests();
		for (MembershipRequestEntity membershipRequestEntity : membershipRequestEntities) {
			MembershipRequest membershipRequest = new MembershipRequest();
			membershipRequest.setEntity(membershipRequestEntity);
			membershipRequests.add(membershipRequest);
		}
		return membershipRequests;
	}

	public void setMembershipRequests(Collection<MembershipRequest> membershipRequests) {
		this.membershipRequests = membershipRequests;
		for (MembershipRequest membershipRequest : membershipRequests) {
			entity.getMembershipRequests().add(membershipRequest.getEntity());
		}
	}

	public int getGenuineVotes() {
		return entity.getGenuineVotes();
	}

	public void setGenuineVotes(int genuineVotes) {
		this.genuineVotes = genuineVotes;
		entity.setGenuineVotes(genuineVotes);
	}

	public int getFakeVotes() {
		return entity.getFakeVotes();
	}

	public void setFakeVotes(int fakeVotes) {
		this.fakeVotes = fakeVotes;
		entity.setFakeVotes(fakeVotes);
	}

	@Override
	public void setUniqueInstanceVariable() {
		id = getId();
		name = getName();
		owner = getOwner();
		
	}
	@Override
	public int hashCode() {
		setUniqueInstanceVariable();
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		setUniqueInstanceVariable();
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
}
