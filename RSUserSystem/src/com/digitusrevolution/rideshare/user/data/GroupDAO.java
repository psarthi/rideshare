package com.digitusrevolution.rideshare.user.data;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.model.user.data.MembershipRequestEntity;
import com.digitusrevolution.rideshare.model.user.data.core.GroupEntity;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;

public class GroupDAO extends GenericDAOImpl<GroupEntity, Integer>{

	private static final Class<GroupEntity> entityClass = GroupEntity.class;

	public GroupDAO() {
		super(entityClass);
	}

	public MembershipRequestEntity getMembershipRequest(int groupId, int userId){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Query query = session.getNamedQuery("MembershipRequest.byGroupIdAndUserId").setParameter("groupId", groupId)
				.setParameter("userId", userId);
		MembershipRequestEntity membershipRequestEntity = (MembershipRequestEntity) query.uniqueResult();
		return membershipRequestEntity;
	}
	
	public UserEntity getMember(int groupId, int memberUserId){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Query query = session.getNamedQuery("Member.byGroupIdAndUserId").setParameter("groupId", groupId)
				.setParameter("memberUserId", memberUserId);
		UserEntity userEntity = (UserEntity) query.uniqueResult();
		return userEntity;
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
	public int getMemberCount(int groupId) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass)
				.add(Restrictions.eq("id", groupId))
				.createCriteria("members", JoinType.RIGHT_OUTER_JOIN)
				.setProjection(Projections.rowCount());
		int size = (int) Long.parseLong(criteria.uniqueResult().toString());
		return size;
	}

}
