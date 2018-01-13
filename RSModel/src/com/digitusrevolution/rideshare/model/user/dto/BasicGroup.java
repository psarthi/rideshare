package com.digitusrevolution.rideshare.model.user.dto;

import java.time.ZonedDateTime;

import com.digitusrevolution.rideshare.model.user.domain.MembershipForm;
import com.digitusrevolution.rideshare.model.user.domain.Photo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//Reason behind this jsonignore so that it doesn't throw error while converting from Domain Model to DTO which has less fields
@JsonIgnoreProperties (ignoreUnknown=true)
public class BasicGroup {

	private int id;
	private String name;
	private Photo photo;
	
	private BasicUser owner;
	private MembershipForm membershipForm;
	private ZonedDateTime createdDateTime;
	private String url;
	private String information;
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

	public BasicUser getOwner() {
		return owner;
	}

	public void setOwner(BasicUser owner) {
		this.owner = owner;
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
		if (!(obj instanceof BasicGroup)) {
			return false;
		}
		BasicGroup other = (BasicGroup) obj;
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
}








































