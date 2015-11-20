package com.digitusrevolution.rideshare.model.ride.data;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.digitusrevolution.rideshare.model.user.data.core.GroupEntity;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;

@Entity
@Table(name="trust_network")
public class TrustNetworkEntity {
	@Id
	@GeneratedValue
	private int id;
	@ManyToMany
	@JoinTable(name="trust_network_category",joinColumns=@JoinColumn(name="trust_network_id"))
	private List<TrustCategoryEntity> trustCategories;
	@ManyToMany
	@JoinTable(name="trust_network_friend",joinColumns=@JoinColumn(name="trust_network_id"))
	private List<UserEntity> friends;
	@ManyToMany
	@JoinTable(name="trust_network_group",joinColumns=@JoinColumn(name="trust_network_id"))
	private List<GroupEntity> groups;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<TrustCategoryEntity> getTrustCategories() {
		return trustCategories;
	}
	public void setTrustCategories(List<TrustCategoryEntity> trustCategories) {
		this.trustCategories = trustCategories;
	}
	public List<UserEntity> getFriends() {
		return friends;
	}
	public void setFriends(List<UserEntity> friends) {
		this.friends = friends;
	}
	public List<GroupEntity> getGroups() {
		return groups;
	}
	public void setGroups(List<GroupEntity> groups) {
		this.groups = groups;
	}

}
