package com.digitusrevolution.rideshare.model.ride.data;

import java.util.ArrayList;
import java.util.Collection;

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
	private Collection<TrustCategoryEntity> trustCategories = new ArrayList<TrustCategoryEntity>();
	@ManyToMany
	@JoinTable(name="trust_network_friend",joinColumns=@JoinColumn(name="trust_network_id"))
	private Collection<UserEntity> friends = new ArrayList<UserEntity>();
	@ManyToMany
	@JoinTable(name="trust_network_group",joinColumns=@JoinColumn(name="trust_network_id"))
	private Collection<GroupEntity> groups = new ArrayList<GroupEntity>();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Collection<TrustCategoryEntity> getTrustCategories() {
		return trustCategories;
	}
	public void setTrustCategories(Collection<TrustCategoryEntity> trustCategories) {
		this.trustCategories = trustCategories;
	}
	public Collection<UserEntity> getFriends() {
		return friends;
	}
	public void setFriends(Collection<UserEntity> friends) {
		this.friends = friends;
	}
	public Collection<GroupEntity> getGroups() {
		return groups;
	}
	public void setGroups(Collection<GroupEntity> groups) {
		this.groups = groups;
	}


}
