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
import com.digitusrevolution.rideshare.model.serviceprovider.data.core.RewardReimbursementTransactionEntity;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;

public class RewardReimbursementTransactionDAO extends GenericDAOImpl<RewardReimbursementTransactionEntity, Integer>{
	
	private static final Class<RewardReimbursementTransactionEntity> entityClass = RewardReimbursementTransactionEntity.class;

	public RewardReimbursementTransactionDAO() {
		super(entityClass);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<RewardReimbursementTransactionEntity> getReimbursementTransactions(UserEntity user, int startIndex){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		int resultLimit = Integer.parseInt(PropertyReader.getInstance().getProperty("MAX_RESULT_LIMIT"));

		Set transactionEntities = new HashSet<>(criteria.add(Restrictions.eq("user", user))
				.addOrder(Order.desc("id"))
				.setFirstResult(startIndex)
				.setMaxResults(resultLimit)
				.list());
		List<RewardReimbursementTransactionEntity> transactionEntitiesList = new LinkedList<>(transactionEntities);
		return transactionEntitiesList;
	}

}
