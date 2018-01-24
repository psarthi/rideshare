package com.digitusrevolution.rideshare.billing.data;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.util.PropertyReader;
import com.digitusrevolution.rideshare.model.billing.data.core.AccountEntity;
import com.digitusrevolution.rideshare.model.billing.data.core.TransactionEntity;

public class AccountDAO extends GenericDAOImpl<AccountEntity, Long>{

	private static final Class<AccountEntity> entityClass = AccountEntity.class;

	public AccountDAO() {
		super(entityClass);
	}
	
	/*
	 * 
	 * Purpose - Get all transactions by start index which supports pagination 
	 *
	 * Reference - http://docs.jboss.org/hibernate/core/3.3/reference/en/html/querycriteria.html#querycriteria-associations
	 * 
	 * Explaination of what's happening below. In nutshell, we need to use createAlias/createCriteria with alias name mandatory
	 * We can use either of the one createAlias vs createCriteria. 
	 * 
	 * The second createCriteria() returns a new instance of Criteria that refers to the elements of the kittens collection.
	 * (createAlias() does not create a new instance of Criteria.)
	 * 
	 * VERY IMP -if you don't use alias name while creating second createCriteria, you will get result set of transaction as null as 
	 * there would not be eny entry in the map with just transactions, but when you give alias name as trns then you will see an entry added to 
	 * the map with key as trns.
	 * 
	 * IMP - Important thing here, is setResultTranformer with ALIAS_TO_ENTITY_MAP which basically helps you to get the child entity in the map
	 * 
	 * List cats = sess.createCriteria(Cat.class)
	 *	    .createCriteria("kittens", "kt")
	 *	        .add( Restrictions.eq("name", "F%") )
	 *	    .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
	 *	    .list();
	 *	Iterator iter = cats.iterator();
	 *		while ( iter.hasNext() ) {
	 *	    Map map = (Map) iter.next();
	 *	    Cat cat = (Cat) map.get(Criteria.ROOT_ALIAS);
	 *	    Cat kitten = (Cat) map.get("kt");
	 *	}
	 * 
	 * The kittens collections held by the Cat instances returned by the previous two queries are not pre-filtered by the criteria. 
	 * If you want to retrieve just the kittens that match the criteria, you must use a ResultTransformer.
	 * 
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<TransactionEntity> getTransactions(long accountNumber, int startIndex){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		int resultLimit = Integer.parseInt(PropertyReader.getInstance().getProperty("MAX_RESULT_LIMIT"));
		Criteria criteria = session.createCriteria(entityClass)
				.add(Restrictions.eq("number", accountNumber))
				.createCriteria("transactions", "trns",JoinType.RIGHT_OUTER_JOIN)
					.addOrder(Order.desc("dateTime"))
					.setFirstResult(startIndex)
					.setMaxResults(resultLimit)
					.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		
		//VERY IMP - Get the result in Set else you would get duplicate values
		Set list = new HashSet<>(criteria.list());
		List<TransactionEntity> transactionEntitiesList = new LinkedList<>();
		Iterator iter = list.iterator();
		while (iter.hasNext() ) {
		    Map map = (Map) iter.next();
		    //Commenting this as we don't need account entity
		    //AccountEntity accountEntity = (AccountEntity) map.get(Criteria.ROOT_ALIAS);
		    TransactionEntity transactionEntity = (TransactionEntity) map.get("trns");
		    transactionEntitiesList.add(transactionEntity);
		}

		return transactionEntitiesList;
	}
}
