package com.digitusrevolution.rideshare.user.business;

import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.util.JsonObjectMapper;
import com.digitusrevolution.rideshare.model.user.domain.core.Group;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.dto.BasicGroup;
import com.digitusrevolution.rideshare.model.user.dto.BasicUser;
import com.digitusrevolution.rideshare.model.user.dto.FullGroup;
import com.digitusrevolution.rideshare.model.user.dto.GroupDetail;
import com.digitusrevolution.rideshare.user.domain.core.GroupDO;
import com.digitusrevolution.rideshare.user.domain.core.UserDO;

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
	
	public GroupDetail getGroupDetails(int groupId){

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		GroupDetail groupDetail = null;
		try {
			transaction = session.beginTransaction();
			
			GroupDO groupDO = new GroupDO();
			groupDetail = groupDO.getGroupDetails(groupId);
			
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
		return groupDetail;
	}
	
	public List<BasicUser> getMembers(int groupId, int page){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		List<BasicUser> users = new LinkedList<>();
		try {
			transaction = session.beginTransaction();
			
			GroupDO groupDO = new GroupDO();
			List<User> members = groupDO.getMembers(groupId, page);
			for (User user: members) {
				BasicUser basicUser = new BasicUser();
				basicUser = JsonObjectMapper.getMapper().convertValue(user, BasicUser.class);
				users.add(basicUser);
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
		return users;
	}
}
