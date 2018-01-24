package com.digitusrevolution.rideshare.billing.data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.model.billing.data.core.BillEntity;
import com.digitusrevolution.rideshare.model.billing.domain.core.BillStatus;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.domain.core.User;

public class BillDAO extends GenericDAOImpl<BillEntity, Long>{
	
	private static final Class<BillEntity> entityClass = BillEntity.class;

	public BillDAO() {
		super(entityClass);
	}

	/*
	 * Purpose - Get the status of bill
	 */
	public BillStatus getStatus(long billNumber){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		BillStatus status = (BillStatus) criteria.add(Restrictions.eq("number",billNumber))
				.setProjection(Projections.property("status")).uniqueResult();
		return status;
	}
	
	public Set<BillEntity> getPendingBills(UserEntity passenger){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		@SuppressWarnings("unchecked")
		Set<BillEntity> billEntities = new HashSet<> (criteria.add(Restrictions.eq("passenger",passenger))
		.add(Restrictions.or(Restrictions.eq("status", BillStatus.Pending)))
		.list());
	
		return billEntities;
	}
	
}
