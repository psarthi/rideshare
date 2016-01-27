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
		entity.setId(id);
	}

	public String getName() {
		return name;
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
		setDomainModelPrimitiveVariable();
	}

	public Collection<User> getMembers() {
		if (members.isEmpty()){
			Collection<UserEntity> memberEntities = entity.getMembers();
			for (UserEntity userEntity : memberEntities) {
				User member = new User();
				member.setEntity(userEntity);
				members.add(member);
			}
		}
		return members;
	}

	public void setMembers(Collection<User> members) {
		this.members = members;
		entity.getMembers().clear();
		for (User member : members) {
			entity.getMembers().add(member.getEntity());
		}
	}

	public void addMember(User member){
		members.add(member);
		entity.getMembers().add(member.getEntity());
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
		if (admins.isEmpty()){
			Collection<UserEntity> adminEntities = entity.getAdmins();
			for (UserEntity userEntity : adminEntities) {
				User admin = new User();
				admin.setEntity(userEntity);
				admins.add(admin);
			}
		}
		return admins;
	}

	public void setAdmins(Collection<User> admins) {
		this.admins = admins;
		entity.getAdmins().clear();
		for (User user : admins) {
			entity.getAdmins().add(user.getEntity());
		}
	}

	public void addAdmin(User admin){
		admins.add(admin);
		entity.getAdmins().add(admin.getEntity());
	}

	public Collection<GroupFeedback> getFeedbacks() {
		if (feedbacks.isEmpty()){
			Collection<GroupFeedbackEntity> feedbackEntities = entity.getFeedbacks();
			for (GroupFeedbackEntity groupFeedbackEntity : feedbackEntities) {
				GroupFeedback feedback = new GroupFeedback();
				feedback.setEntity(groupFeedbackEntity);
				feedbacks.add(feedback);
			}
		}
		return feedbacks;
	}

	public void setFeedbacks(Collection<GroupFeedback> feedbacks) {
		this.feedbacks = feedbacks;
		entity.getFeedbacks().clear();
		for (GroupFeedback groupFeedback : feedbacks) {
			entity.getFeedbacks().add(groupFeedback.getEntity());
		}
	}

	public void addFeedback(GroupFeedback feedback){
		feedbacks.add(feedback);
		entity.getFeedbacks().add(feedback.getEntity());
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
		return createdDateTime;
	}

	public void setCreatedDateTime(ZonedDateTime createdDateTime) {
		this.createdDateTime = createdDateTime;
		entity.setCreatedDateTime(createdDateTime);
	}

	public String getUrl() {
		return url; 
	}

	public void setUrl(String url) {
		this.url = url;
		entity.setUrl(url);
	}

	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
		entity.setInformation(information);
	}

	public Collection<MembershipRequest> getMembershipRequests() {
		if (membershipRequests.isEmpty()){
			Collection<MembershipRequestEntity> membershipRequestEntities = entity.getMembershipRequests();
			for (MembershipRequestEntity membershipRequestEntity : membershipRequestEntities) {
				MembershipRequest membershipRequest = new MembershipRequest();
				membershipRequest.setEntity(membershipRequestEntity);
				membershipRequests.add(membershipRequest);
			}
		}
		return membershipRequests;
	}

	public void setMembershipRequests(Collection<MembershipRequest> membershipRequests) {
		this.membershipRequests = membershipRequests;
		entity.getMembershipRequests().clear();
		for (MembershipRequest membershipRequest : membershipRequests) {
			entity.getMembershipRequests().add(membershipRequest.getEntity());
		}
	}
	
	public void addMembershipRequest(MembershipRequest membershipRequest){
		membershipRequests.add(membershipRequest);
		entity.getMembershipRequests().add(membershipRequest.getEntity());
	}

	public int getGenuineVotes() {
		return genuineVotes;
	}

	public void setGenuineVotes(int genuineVotes) {
		this.genuineVotes = genuineVotes;
		entity.setGenuineVotes(genuineVotes);
	}

	public int getFakeVotes() {
		return fakeVotes;
	}

	public void setFakeVotes(int fakeVotes) {
		this.fakeVotes = fakeVotes;
		entity.setFakeVotes(fakeVotes);
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

	@Override
	public void setDomainModelPrimitiveVariable() {
		id = entity.getId();
		name = entity.getName();
		createdDateTime = entity.getCreatedDateTime();
		url = entity.getUrl();
		information = entity.getInformation();
		genuineVotes = entity.getGenuineVotes();
		fakeVotes = entity.getFakeVotes();	
	}

	@Override
	public void fetchReferenceVariable() {
		// TODO Auto-generated method stub
		
	}
}
