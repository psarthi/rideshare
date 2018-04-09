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
import com.digitusrevolution.rideshare.model.billing.data.core.FinancialTransactionEntity;
import com.digitusrevolution.rideshare.model.billing.domain.core.BillStatus;
import com.digitusrevolution.rideshare.model.billing.domain.core.TransactionStatus;
import com.digitusrevolution.rideshare.model.billing.domain.core.TransactionType;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.domain.core.User;

public class FinancialTransactionDAO extends GenericDAOImpl<FinancialTransactionEntity, Long>{
	
	private static final Class<FinancialTransactionEntity> entityClass = FinancialTransactionEntity.class;

	public FinancialTransactionDAO() {
		super(entityClass);
	}
	
	public Set<FinancialTransactionEntity> getPendingTopUpTransactions(){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		@SuppressWarnings("unchecked")
		Set<FinancialTransactionEntity> transactionEntities = new HashSet<> (criteria.
				add(Restrictions.or(Restrictions.eq("status", TransactionStatus.Pending))
						.add(Restrictions.eq("status", TransactionStatus.Open))
						.add(Restrictions.eq("status", TransactionStatus.Initiated)))
				.add(Restrictions.eq("type", TransactionType.Credit))
				.list());
	
		return transactionEntities;
	}
	
	//Reason for keeping this function sperate than TopUp for future perspective 
	//so that we can accomodate special requirement separately
	public Set<FinancialTransactionEntity> getPendingRedemptionTransactions(){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		@SuppressWarnings("unchecked")
		Set<FinancialTransactionEntity> transactionEntities = new HashSet<> (criteria.
				add(Restrictions.or(Restrictions.eq("status", TransactionStatus.Pending))
						.add(Restrictions.eq("status", TransactionStatus.Open)))
				.add(Restrictions.eq("type", TransactionType.Debit))
				.list());
	
		return transactionEntities;
	}
	
	//Reason for keeping this function sperate than TopUp for future perspective 
	//so that we can accomodate special requirement separately
	public Set<FinancialTransactionEntity> getInitiatedRedemptionTransactions(){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		@SuppressWarnings("unchecked")
		Set<FinancialTransactionEntity> transactionEntities = new HashSet<> (criteria.
				add(Restrictions.or(Restrictions.eq("status", TransactionStatus.Initiated)))
				.add(Restrictions.eq("type", TransactionType.Debit))
				.list());
	
		return transactionEntities;
	}

	
}
