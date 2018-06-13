package com.digitusrevolution.rideshare.serviceprovider.data;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.util.PropertyReader;
import com.digitusrevolution.rideshare.model.serviceprovider.data.core.RewardCouponTransactionEntity;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;

public class RewardCouponTransactionDAO extends GenericDAOImpl<RewardCouponTransactionEntity, Integer>{
	
	private static final Class<RewardCouponTransactionEntity> entityClass = RewardCouponTransactionEntity.class;

	public RewardCouponTransactionDAO() {
		super(entityClass);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<RewardCouponTransactionEntity> getCouponTransactions(UserEntity user, int startIndex){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		int resultLimit = Integer.parseInt(PropertyReader.getInstance().getProperty("MAX_RESULT_LIMIT"));
		Set couponTransactionEntities = new HashSet<>(criteria.add(Restrictions.eq("user", user))
				.addOrder(Order.desc("id"))
				.setFirstResult(startIndex)
				.setMaxResults(resultLimit)
				.list());
		List<RewardCouponTransactionEntity> couponTransactionEntitiesList = new LinkedList<>(couponTransactionEntities);
		return couponTransactionEntitiesList;
	}
	
}
