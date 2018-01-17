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
import com.digitusrevolution.rideshare.model.user.dto.BasicMembershipRequest;
import com.digitusrevolution.rideshare.model.user.dto.BasicUser;
import com.digitusrevolution.rideshare.model.user.dto.FullGroup;
import com.digitusrevolution.rideshare.model.user.dto.GroupDetail;
import com.digitusrevolution.rideshare.model.user.dto.GroupInviteUserSearchResult;
import com.digitusrevolution.rideshare.model.user.dto.GroupMember;
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
	
	public GroupDetail getGroupDetails(int groupId, int userId){

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		GroupDetail groupDetail = null;
		try {
			transaction = session.beginTransaction();
			
			GroupDO groupDO = new GroupDO();
			groupDetail = groupDO.getGroupDetail(groupId, userId);
			
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
	
	public List<GroupMember> getMembers(int groupId, int page){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		List<GroupMember> members = null;
		try {
			transaction = session.beginTransaction();
			
			GroupDO groupDO = new GroupDO();
			members = groupDO.getMembers(groupId, page);
			
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
		return members;
	}
	
	public List<GroupInviteUserSearchResult> searchUserByNameForGroupInvite(int groupId, String name, int page){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		List<GroupInviteUserSearchResult> users = null;
		try {
			transaction = session.beginTransaction();
			
			GroupDO groupDO = new GroupDO();
			users = groupDO.searchUserByNameForGroupInvite(groupId, name, page);
			
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
	
	public void inviteUsers(int groupId, List<Integer> userIds){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		try {
			transaction = session.beginTransaction();
			
			GroupDO groupDO = new GroupDO();
			groupDO.inviteUsers(groupId, userIds);
			
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
	}
	
	public List<GroupDetail> searchGroupByName(int userId, String name, int page){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		List<GroupDetail> groupDetails = null;
		try {
			transaction = session.beginTransaction();
			
			GroupDO groupDO = new GroupDO();
			groupDetails = groupDO.searchGroupByName(userId, name, page);
			
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
		return groupDetails;
	}
	
	public List<BasicMembershipRequest> getGroupMembershipRequests(int groupId, int page){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		List<BasicMembershipRequest> requests = new LinkedList<>();
		try {
			transaction = session.beginTransaction();
			
			GroupDO groupDO = new GroupDO();
			requests = groupDO.getGroupMembershipRequests(groupId, page);			
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
		return requests;
	}
}

























