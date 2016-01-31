package com.digitusrevolution.rideshare.user.data;

import org.hibernate.Query;
import org.hibernate.Session;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.model.user.data.MembershipRequestEntity;
import com.digitusrevolution.rideshare.model.user.data.core.GroupEntity;

public class GroupDAO extends GenericDAOImpl<GroupEntity, Integer>{

	private static final Class<GroupEntity> entityClass = GroupEntity.class;

	public GroupDAO() {
		super(entityClass);
	}

	public MembershipRequestEntity getMembershipRequest(int groupId, int userId){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Query query = session.getNamedQuery("MembershipRequest.byUserIdAndGroupId").setParameter("groupId", groupId)
				.setParameter("userId", userId);
		MembershipRequestEntity membershipRequestEntity = (MembershipRequestEntity) query.uniqueResult();
		return membershipRequestEntity;
	}
}
