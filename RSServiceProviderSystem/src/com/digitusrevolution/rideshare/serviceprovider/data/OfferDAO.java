package com.digitusrevolution.rideshare.serviceprovider.data;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.util.PropertyReader;
import com.digitusrevolution.rideshare.model.serviceprovider.data.core.OfferEntity;

public class OfferDAO extends GenericDAOImpl<OfferEntity, Integer>{
	
	private static final Logger logger = LogManager.getLogger(OfferDAO.class.getName());
	private static final Class<OfferEntity> entityClass = OfferEntity.class;

	public OfferDAO() {
		super(entityClass);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<OfferEntity> getOffers(int startIndex){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		int resultLimit = Integer.parseInt(PropertyReader.getInstance().getProperty("MAX_RESULT_LIMIT"));
		//VERY IMP - Get the result in Set else you would get duplicate values
		Set offerEntities = new HashSet<>(criteria.addOrder(Order.desc("id"))
				.setFirstResult(startIndex)
				.setMaxResults(resultLimit)
				.list());		
		
		logger.debug("Offer List Size:"+offerEntities.size());
		List<OfferEntity> offerEntitiesList = new LinkedList<>(offerEntities);
		return offerEntitiesList;	
	}

}
