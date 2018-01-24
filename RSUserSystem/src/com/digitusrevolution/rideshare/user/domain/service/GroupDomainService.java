package com.digitusrevolution.rideshare.user.domain.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.inf.DomainServiceInteger;
import com.digitusrevolution.rideshare.common.inf.DomainServiceLong;
import com.digitusrevolution.rideshare.model.user.domain.core.Group;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.user.domain.core.GroupDO;
import com.digitusrevolution.rideshare.user.domain.core.UserDO;

public class GroupDomainService implements DomainServiceLong<Group>{

	private static final Logger logger = LogManager.getLogger(GroupDomainService.class.getName());
	
	@Override
	public Group get(long id, boolean fetchChild) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		Group group = null;
		try {
			transaction = session.beginTransaction();
	
			GroupDO groupDO = new GroupDO();
			if (fetchChild){
				group = groupDO.getAllData(id);
			} else {
				group = groupDO.get(id);			
			}
			
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
		return group;
	}

	@Override
	public List<Group> getAll() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		List<Group> groups = null;
		try {
			transaction = session.beginTransaction();

			GroupDO groupDO = new GroupDO();
			groups = groupDO.getAll();

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
		return groups;	
	}

}
