package com.digitusrevolution.rideshare.user.data;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.util.PropertyReader;
import com.digitusrevolution.rideshare.model.billing.data.core.TransactionEntity;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideStatus;
import com.digitusrevolution.rideshare.model.user.data.MembershipRequestEntity;
import com.digitusrevolution.rideshare.model.user.data.core.GroupEntity;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.domain.ApprovalStatus;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.dto.GroupListType;

public class UserDAO extends GenericDAOImpl<UserEntity,Integer>{

	private static final Class<UserEntity> entityClass = UserEntity.class;

	public UserDAO() {
		super(entityClass);
	}

	@SuppressWarnings("unchecked")
	public UserEntity getUserByEmail(String email){	
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		//We can use either Query option or criteria, so below code is just for reference
		/*	Query query = session.getNamedQuery("UserEntity.byEmail")
								 .setParameter("email", email);
			userEntities = (List<UserEntity>) query.list();
		 */
		Criteria criteria = session.createCriteria(entityClass)
				.add(Restrictions.eq("email", email));	
		List<UserEntity> userEntities = criteria.list();
		if (userEntities.isEmpty()){
			return null;
		} else {
			return userEntities.get(0);				
		}	
	}

	/*
	 * This will return all registered users matching either email id or mobile number excluding requested User
	 * i.e. if user1 has requested to get all the registered users, then it will exclude user 1 from the list
	 */
	public Set<UserEntity> findAllRegisteredUserBasedOnEmailOrMobile(int userId, List<String> emailIds, List<String> mobileNumbers){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		@SuppressWarnings("unchecked")
		Set<UserEntity> userEntities = new HashSet<>(criteria.add(Restrictions.or(Restrictions.in("email", emailIds),
				Restrictions.in("mobileNumber", mobileNumbers)))
		.add(Restrictions.ne("id", userId)).list());
		return userEntities;
	}
	
	/*
	 * Keeping this for reference purpose only, use the below one which will take care of searching in fullName
	 *  
	 * This will return all user starting with search string either in firstName/lastName
	 * 
	 */
	/*public Set<UserEntity> searchUserByName(int userId, String name){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		@SuppressWarnings("unchecked")
		Set<UserEntity> userEntities = new HashSet<>(criteria.add(Restrictions.or(Restrictions.ilike("firstName", name, MatchMode.START)))
			.add(Restrictions.ne("id", userId)).list());
		return userEntities;
	}*/
	
	public Set<UserEntity> searchUserByName(String name, int startIndex){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		int resultLimit = Integer.parseInt(PropertyReader.getInstance().getProperty("MAX_RESULT_LIMIT"));
		//VERY IMP - Don't order by name, else you will miss some items due to edge condition of matching names
		//e.g if you have 20 groups of the same name, then order of that can be anything and since we are just getting 
		//sublist from the whole list, you may miss items
		//So, ensure that your order by should be unique when you are using sublist
		Query query = session.getNamedQuery("User.SearchByName").setParameter("name", name+"%")
				.setFirstResult(startIndex)
				.setMaxResults(resultLimit);
		Set<UserEntity> userEntities = new HashSet<>(query.list());
		return userEntities;
	} 

	
	public int getRidesOffered(int userId) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass)
				.add(Restrictions.eq("id", userId))
				.createCriteria("ridesOffered", JoinType.RIGHT_OUTER_JOIN)
					.add(Restrictions.eq("status", RideStatus.Finished))
				.setProjection(Projections.rowCount());
		int size = (int) Long.parseLong(criteria.uniqueResult().toString());
		return size;
	}
	
	public int getRidesTaken(int userId) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass)
				.add(Restrictions.eq("id", userId))
				.createCriteria("ridesTaken", JoinType.RIGHT_OUTER_JOIN)
				.setProjection(Projections.rowCount());
		int size = (int) Long.parseLong(criteria.uniqueResult().toString());
		return size;
	}
	
	/*
	 * Purpose - This will get list of groups based on startIndex to support pagination
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<GroupEntity> getGroups(GroupListType listType, int userId, int startIndex){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		int resultLimit = Integer.parseInt(PropertyReader.getInstance().getProperty("MAX_RESULT_LIMIT"));
		String subCriteriaAssociationPath = null;
		if (listType.equals(GroupListType.Member)) {
			subCriteriaAssociationPath = "groups";
		}
		if (listType.equals(GroupListType.Invite)) {
			subCriteriaAssociationPath = "groupInvites";
		}
		//VERY IMP - Don't order by name, else you will miss some items due to edge condition of matching names
		//e.g if you have 20 groups of the same name, then order of that can be anything and since we are just getting 
		//sublist from the whole list, you may miss items
		//So, ensure that your order by should be unique when you are using sublist
		Criteria criteria = session.createCriteria(entityClass)
				.add(Restrictions.eq("id", userId))
				.createCriteria(subCriteriaAssociationPath, "grp",JoinType.RIGHT_OUTER_JOIN)
					.addOrder(Order.asc("id"))
					.setFirstResult(startIndex)
					.setMaxResults(resultLimit)
					.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		
		//VERY IMP - Get the result in Set else you would get duplicate values
		Set list = new HashSet<>(criteria.list());
		List<GroupEntity> groupEntities = new LinkedList<>();
		Iterator iter = list.iterator();
		while (iter.hasNext() ) {
		    Map map = (Map) iter.next();
		    GroupEntity groupEntity = (GroupEntity) map.get("grp");
		    groupEntities.add(groupEntity);
		}
		
		return groupEntities;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<MembershipRequestEntity> getUserMembershipRequests(int userId, int startIndex){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		int resultLimit = Integer.parseInt(PropertyReader.getInstance().getProperty("MAX_RESULT_LIMIT"));
		String subCriteriaAssociationPath = "membershipRequests";
		Criteria criteria = session.createCriteria(entityClass)
				.add(Restrictions.eq("id", userId))
				.createCriteria("membershipRequests", "request",JoinType.RIGHT_OUTER_JOIN)
					.addOrder(Order.asc("id"))
					.add(Restrictions.ne("status", ApprovalStatus.Approved))
					.setFirstResult(startIndex)
					.setMaxResults(resultLimit)
					.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		
		//VERY IMP - Get the result in Set else you would get duplicate values
		Set list = new HashSet<>(criteria.list());
		List<MembershipRequestEntity> requestEntities = new LinkedList<>();
		Iterator iter = list.iterator();
		while (iter.hasNext() ) {
		    Map map = (Map) iter.next();
		    MembershipRequestEntity requestEntity = (MembershipRequestEntity) map.get("request");
		    requestEntities.add(requestEntity);
		}
		return requestEntities;
	}
	
	//This will tell if user is invite to a group or not
	public boolean isInvited(int groupId, int userId){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Query query = session.getNamedQuery("Invite.ByUserIdAndGroupId").setParameter("groupId", groupId)
				.setParameter("userId", userId);
		GroupEntity groupEntity = (GroupEntity) query.uniqueResult();
		if (groupEntity!=null) {
			return true;
		} else {
			return false;
		}
	}

}




































