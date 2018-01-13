package com.digitusrevolution.rideshare.user.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.util.JsonObjectMapper;
import com.digitusrevolution.rideshare.model.user.domain.core.Group;
import com.digitusrevolution.rideshare.model.user.dto.BasicGroup;
import com.digitusrevolution.rideshare.model.user.dto.FullGroup;
import com.digitusrevolution.rideshare.user.domain.core.GroupDO;

public class GroupBusinessService {
	
	private static final Logger logger = LogManager.getLogger(GroupBusinessService.class.getName());

	public int createGroup(BasicGroup group){

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		int id = 0;
		try {
			transaction = session.beginTransaction();
			
			GroupDO groupDO = new GroupDO();
			id = groupDO.createGroup(JsonObjectMapper.getMapper().convertValue(group, Group.class));
			
			transaction.commit();
		} catch (RuntimeException e) {
			if (transaction!=null){
				logger.error("Transaction Failed, Rolling Back");
				transaction.rollback();
				throw e;
			}
		}
		finally {
			if (session.isOpen()){
				logger.info("Closing Session");
				session.close();				
			}
		}
		return id;
	}
	
	public FullGroup getGroup(int groupId){

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		FullGroup fullGroup = null;
		try {
			transaction = session.beginTransaction();
			
			GroupDO groupDO = new GroupDO();
			Group group = groupDO.getAllData(groupId);
			fullGroup = JsonObjectMapper.getMapper().convertValue(group, FullGroup.class);
			
			transaction.commit();
		} catch (RuntimeException e) {
			if (transaction!=null){
				logger.error("Transaction Failed, Rolling Back");
				transaction.rollback();
				throw e;
			}
		}
		finally {
			if (session.isOpen()){
				logger.info("Closing Session");
				session.close();				
			}
		}
		return fullGroup;
	}
	
}
