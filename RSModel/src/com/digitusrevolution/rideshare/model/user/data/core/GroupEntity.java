package com.digitusrevolution.rideshare.model.user.data.core;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.digitusrevolution.rideshare.model.user.data.FormEntity;
import com.digitusrevolution.rideshare.model.user.data.GroupFeedbackEntity;
import com.digitusrevolution.rideshare.model.user.data.MembershipRequestEntity;
import com.digitusrevolution.rideshare.model.user.data.PhotoEntity;

@Entity
@Table(name="group_detail")
//Its important to select request else without that, it will fetch two object, one if group entity and 2nd one is membership request
//Don't use group, member keyword as its reserved in db i.e. don't use any reserved keyword in sql 
//Note - Join works only with entity and not element collection
//VERY IMP - Don't order by name, else you will miss some items due to edge condition of matching names
//e.g if you have 20 groups of the same name, then order of that can be anything and since we are just getting 
//sublist from the whole list, you may miss items
//So, ensure that your order by should be unique when you are using sublist
@NamedQueries({
	@NamedQuery(name="MembershipRequest.byGroupIdAndUserId", 
			query="select request from GroupEntity as grp join grp.membershipRequests as request "
					+ "where grp.id=:groupId and request.user.id=:userId"),
	@NamedQuery(name="Member.byGroupIdAndUserId", 
	query="select mbr from GroupEntity as grp join grp.members as mbr "
			+ "where grp.id=:groupId and mbr.id=:memberUserId"),
	@NamedQuery(name="Admin.byGroupIdAndUserId", 
	query="select adm from GroupEntity as grp join grp.admins as adm "
			+ "where grp.id=:groupId and adm.id=:memberUserId"),
	@NamedQuery(name="Group.SearchByName", 
	query="from GroupEntity where name like :name order by id asc"),
	@NamedQuery(name="MembershipRequests.byGroupId", 
	query="select request from GroupEntity as grp join grp.membershipRequests as request "
			+ "where grp.id=:groupId and request.status!=:approvedStatus and request.status!=:rejectedStatus "
			+ "order by request.id asc"),
	@NamedQuery(name="Members.byGroupId", 
	query="select mbr from GroupEntity as grp join grp.members as mbr "
			+ "where grp.id=:groupId order by mbr.id asc"),
	@NamedQuery(name="Admins.byGroupId", 
	query="select adm from GroupEntity as grp join grp.admins as adm "
			+ "where grp.id=:groupId order by adm.id asc")
})
public class GroupEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String name;
	@OneToOne(cascade=CascadeType.ALL)
	private PhotoEntity photo;

	@ManyToMany
	@JoinTable(name="group_member", joinColumns=@JoinColumn(name="group_id"))
	private Collection<UserEntity> members = new HashSet<UserEntity>();
	@OneToOne
	private UserEntity owner;
	@ManyToMany
	@JoinTable(name="group_admin", joinColumns=@JoinColumn(name="group_id"))
	private Collection<UserEntity> admins = new HashSet<UserEntity>();
	@ElementCollection
	@JoinTable(name="group_feedback", joinColumns=@JoinColumn(name="group_id"))
	private Collection<GroupFeedbackEntity> feedbacks = new LinkedList<GroupFeedbackEntity>();
	@OneToOne(cascade=CascadeType.ALL)
	private FormEntity membershipForm;
	private ZonedDateTime createdDateTime;
	private String url;
	private String information;
	@OneToMany(cascade=CascadeType.ALL)
	@JoinTable(name="group_membership_request" , joinColumns=@JoinColumn(name="group_id"))
	private Collection<MembershipRequestEntity> membershipRequests = new HashSet<MembershipRequestEntity>();
	private int genuineVotes;
	private int fakeVotes;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public PhotoEntity getPhoto() {
		return photo;
	}
	public void setPhoto(PhotoEntity photo) {
		this.photo = photo;
	}
	public Collection<UserEntity> getMembers() {
		return members;
	}
	public void setMembers(Collection<UserEntity> members) {
		this.members = members;
	}
	public UserEntity getOwner() {
		return owner;
	}
	public void setOwner(UserEntity owner) {
		this.owner = owner;
	}
	public Collection<UserEntity> getAdmins() {
		return admins;
	}
	public void setAdmins(Collection<UserEntity> admins) {
		this.admins = admins;
	}
	public Collection<GroupFeedbackEntity> getFeedbacks() {
		return feedbacks;
	}
	public void setFeedbacks(Collection<GroupFeedbackEntity> feedbacks) {
		this.feedbacks = feedbacks;
	}
	public FormEntity getMembershipForm() {
		return membershipForm;
	}
	public void setMembershipForm(FormEntity membershipForm) {
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
	public Collection<MembershipRequestEntity> getMembershipRequests() {
		return membershipRequests;
	}
	public void setMembershipRequests(Collection<MembershipRequestEntity> membershipRequests) {
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		if (!(obj instanceof GroupEntity)) {
			return false;
		}
		GroupEntity other = (GroupEntity) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}

}
