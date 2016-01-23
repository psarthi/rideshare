package com.digitusrevolution.rideshare.billing.data;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.model.billing.data.core.BillEntity;
import com.digitusrevolution.rideshare.model.billing.domain.core.BillStatus;

public class BillDAO extends GenericDAOImpl<BillEntity, Integer>{
	
	private static final Class<BillEntity> entityClass = BillEntity.class;

	public BillDAO() {
		super(entityClass);
	}

	/*
	 * Purpose - Get the status of bill
	 */
	public BillStatus getStatus(int billNumber){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		BillStatus status = (BillStatus) criteria.add(Restrictions.eq("number",billNumber))
				.setProjection(Projections.property("status")).uniqueResult();
		return status;
	}

}
