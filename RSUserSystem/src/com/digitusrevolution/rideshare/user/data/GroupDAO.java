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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.util.PropertyReader;
import com.digitusrevolution.rideshare.model.user.data.GroupFeedbackEntity;
import com.digitusrevolution.rideshare.model.user.data.MembershipRequestEntity;
import com.digitusrevolution.rideshare.model.user.data.core.GroupEntity;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.domain.ApprovalStatus;
import com.digitusrevolution.rideshare.model.user.domain.Vote;

public class GroupDAO extends GenericDAOImpl<GroupEntity, Long>{

	private static final Class<GroupEntity> entityClass = GroupEntity.class;

	public GroupDAO() {
		super(entityClass);
	}

	public MembershipRequestEntity getMembershipRequest(long groupId, long userId){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Query query = session.getNamedQuery("MembershipRequest.byGroupIdAndUserId").setParameter("groupId", groupId)
				.setParameter("userId", userId);
		MembershipRequestEntity membershipRequestEntity = (MembershipRequestEntity) query.uniqueResult();
		return membershipRequestEntity;
	}
	
	public UserEntity getMember(long groupId, long memberUserId){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Query query = session.getNamedQuery("Member.byGroupIdAndUserId").setParameter("groupId", groupId)
				.setParameter("memberUserId", memberUserId);
		UserEntity userEntity = (UserEntity) query.uniqueResult();
		return userEntity;
	}
	
	// This function will tell if a user is an admin of a group or not
	public boolean isAdmin(long groupId, long memberUserId){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Query query = session.getNamedQuery("Admin.byGroupIdAndUserId").setParameter("groupId", groupId)
				.setParameter("memberUserId", memberUserId);
		UserEntity userEntity = (UserEntity) query.uniqueResult();
		if (userEntity!=null) {
			return true;
		} else {
			return false;
		}
	}
	
	/*
	 * Associations explaination -  
	 * 
	 * By navigating associations using createCriteria() you can specify constraints upon related entities:
	 *
	 *	List cats = sess.createCriteria(Cat.class)
	 *	    .add( Restrictions.like("name", "F%") )
	 *	    .createCriteria("kittens")
	 *	        .add( Restrictions.like("name", "F%") )
	 *	    .list();
	 *
	 *	The second createCriteria() returns a new instance of Criteria that refers to the elements of the kittens collection.
	 *
	 *  What it means that once you have got your collection of kittens or your child elements, then you can apply all your standard restriction on that as well
	 *  Apart from that, you can see in below example that we have applied id Restrictions on the parent object, so it will get members of group with id as groupId
	 *  
	 *  We have also used Projects to get result count	
	 * 
	 */
	public int getMemberCount(long groupId) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass)
				.add(Restrictions.eq("id", groupId))
				.createCriteria("members", JoinType.RIGHT_OUTER_JOIN)
				.setProjection(Projections.rowCount());
		int size = (int) Long.parseLong(criteria.uniqueResult().toString());
		return size;
	}
	
	/* 
	 * 	 Kept here for reference purpose only
	 * 
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<UserEntity> getMembers(long groupId, long startIndex){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		int resultLimit = Integer.parseInt(PropertyReader.getInstance().getProperty("MAX_RESULT_LIMIT"));
		Criteria criteria = session.createCriteria(entityClass)
				.add(Restrictions.eq("id", groupId))
				.createCriteria("members", "mbr",JoinType.RIGHT_OUTER_JOIN)
					.addOrder(Order.asc("firstName"))
					.setFirstResult(startIndex)
					.setMaxResults(resultLimit)
					.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

		//VERY IMP - Get the result in Set else you would get duplicate values
		Set list = new HashSet<>(criteria.list());
		List<UserEntity> userEntities = new LinkedList<>();
		Iterator iter = list.iterator();
		while (iter.hasNext() ) {
		    Map map = (Map) iter.next();
		    UserEntity userEntity = (UserEntity) map.get("mbr");
		    userEntities.add(userEntity);
		}
		
		return userEntities;
	}
	*/
	
	/*
	 * VERY IMP - Don't use Criteria way and fetching Results as its not working and its having some weired outcome
	 * so don't implement function similar to getUserMembershipRequests of UserDAO instead use the Query way
	 * 
	 */
	public List<UserEntity> getMembers(long groupId, int startIndex){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		int resultLimit = Integer.parseInt(PropertyReader.getInstance().getProperty("MAX_RESULT_LIMIT"));
		Query query = session.getNamedQuery("Members.byGroupId").setParameter("groupId", groupId)
				.setFirstResult(startIndex)
				.setMaxResults(resultLimit);
		Set<UserEntity> userEntities = new HashSet<>(query.list());
		List<UserEntity> userEntitiesList = new LinkedList<>();
		for (UserEntity userEntity: userEntities) {
			userEntitiesList.add(userEntity);
		}
		return userEntitiesList;		
	}
	

	//This will get all admins of the group
	public List<UserEntity> getAdmins(long groupId){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass)
				.add(Restrictions.eq("id", groupId))
				.createCriteria("admins", "adm",JoinType.RIGHT_OUTER_JOIN)
				.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	
		//VERY IMP - Get the result in Set else you would get duplicate values
		Set list = new HashSet<>(criteria.list());
		List<UserEntity> userEntities = new LinkedList<>();
		Iterator iter = list.iterator();
		while (iter.hasNext() ) {
		    Map map = (Map) iter.next();
		    UserEntity userEntity = (UserEntity) map.get("adm");
		    userEntities.add(userEntity);
		}
		
		return userEntities;
	}
	
	public Set<GroupEntity> searchGroupByName(String name, int startIndex){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		int resultLimit = Integer.parseInt(PropertyReader.getInstance().getProperty("MAX_RESULT_LIMIT"));
		Query query = session.getNamedQuery("Group.SearchByName").setParameter("name", name+"%")
				.setFirstResult(startIndex)
				.setMaxResults(resultLimit);
		Set<GroupEntity> groupEntities = new HashSet<>(query.list());
		return groupEntities;
	}
	
	public boolean isGroupNameExist(String name) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass)
				.add(Restrictions.eq("name", name));
		GroupEntity groupEntity = (GroupEntity) criteria.uniqueResult();
		if (groupEntity == null) {
			return false;
		}
		return true;
	}
	
	/*
	 * VERY IMP - Don't use Criteria way and fetching Results as its not working and its having some weired outcome
	 * so don't implement function similar to getUserMembershipRequests of UserDAO instead use the Query way
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Set<MembershipRequestEntity> getGroupMembershipRequests(long groupId, int startIndex){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		int resultLimit = Integer.parseInt(PropertyReader.getInstance().getProperty("MAX_RESULT_LIMIT"));
		Query query = session.getNamedQuery("MembershipRequests.byGroupId").setParameter("groupId", groupId)
				.setParameter("approvedStatus", ApprovalStatus.Approved)
				.setParameter("rejectedStatus", ApprovalStatus.Rejected)
				.setFirstResult(startIndex)
				.setMaxResults(resultLimit);
		Set<MembershipRequestEntity> requestEntities = new HashSet<>(query.list());
		return requestEntities;		
	}
	
	public int getGroupMembershipRequestCount(long groupId){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		int resultLimit = Integer.parseInt(PropertyReader.getInstance().getProperty("MAX_RESULT_LIMIT"));
		Query query = session.getNamedQuery("MembershipRequests.byGroupId").setParameter("groupId", groupId)
				.setParameter("approvedStatus", ApprovalStatus.Approved)
				.setParameter("rejectedStatus", ApprovalStatus.Rejected);
		
		Set<MembershipRequestEntity> requestEntities = new HashSet<>(query.list());
		return requestEntities.size();		
	}

	

}
