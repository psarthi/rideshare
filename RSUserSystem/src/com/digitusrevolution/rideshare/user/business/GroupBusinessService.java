package com.digitusrevolution.rideshare.user.business;

import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.util.JsonObjectMapper;
import com.digitusrevolution.rideshare.model.user.domain.GroupFeedback;
import com.digitusrevolution.rideshare.model.user.domain.MembershipForm;
import com.digitusrevolution.rideshare.model.user.domain.MembershipRequest;
import com.digitusrevolution.rideshare.model.user.domain.core.Group;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.dto.BasicGroupInfo;
import com.digitusrevolution.rideshare.model.user.dto.BasicMembershipRequest;
import com.digitusrevolution.rideshare.model.user.dto.GroupDetail;
import com.digitusrevolution.rideshare.model.user.dto.GroupFeedbackInfo;
import com.digitusrevolution.rideshare.model.user.dto.GroupInviteUserSearchResult;
import com.digitusrevolution.rideshare.model.user.dto.GroupMember;
import com.digitusrevolution.rideshare.user.domain.core.GroupDO;

public class GroupBusinessService {
	
	private static final Logger logger = LogManager.getLogger(GroupBusinessService.class.getName());

	public long createGroup(BasicGroupInfo group){

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		long id = 0;
		try {
			transaction = session.beginTransaction();
			
			GroupDO groupDO = new GroupDO();
			id = groupDO.createGroup(JsonObjectMapper.getMapper().convertValue(group, Group.class), group.getRawImage());
			
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
	
	public void updateGroup(BasicGroupInfo group) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		try {
			transaction = session.beginTransaction();
			
			GroupDO groupDO = new GroupDO();
			groupDO.updateGroup(JsonObjectMapper.getMapper().convertValue(group, Group.class), group.getRawImage());
			
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
	
	public GroupDetail getGroupDetail(long groupId, long userId){

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
	
	public List<GroupMember> getMembers(long groupId, int page){
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
	
	public List<GroupInviteUserSearchResult> searchUserByNameForGroupInvite(long groupId, String name, int page){
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
	
	public void inviteUsers(long groupId, List<Long> userIds){
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
	
	public List<GroupDetail> searchGroupByName(long userId, String name, int page){
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
	
	public List<BasicMembershipRequest> getGroupMembershipRequests(long groupId, int page){
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
	
	public BasicMembershipRequest getMembershipRequest(long groupId, long userId){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		BasicMembershipRequest request = null;
		try {
			transaction = session.beginTransaction();
			
			GroupDO groupDO = new GroupDO();
			request = groupDO.getBasicMembershipRequest(groupId, userId);			
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
		return request;
	}
	
	public void sendMembershipRequest(long groupId, BasicMembershipRequest membershipRequest){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			GroupDO groupDO = new GroupDO();
			groupDO.sendMembershipRequest(groupId, JsonObjectMapper.getMapper().convertValue(membershipRequest, MembershipRequest.class));	
			
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
	
	public void approveMembershipRequest(long groupId, long requesterUserId, String remark){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			GroupDO groupDO = new GroupDO();
			groupDO.approveMembershipRequest(groupId, requesterUserId, remark);	
			
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
	
	public void rejectMembershipRequest(long groupId, long requesterUserId, String remark){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			GroupDO groupDO = new GroupDO();
			groupDO.rejectMembershipRequest(groupId, requesterUserId, remark);	
			
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
	
	public void giveFeedback(long groupId, long memberUserId, GroupFeedbackInfo feedback){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			GroupDO groupDO = new GroupDO();
			groupDO.giveFeedback(groupId, memberUserId, JsonObjectMapper.getMapper().convertValue(feedback, GroupFeedback.class));	
			
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
	
	public void leaveGroup(long groupId, long userId){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			GroupDO groupDO = new GroupDO();
			groupDO.leaveGroup(groupId, userId);	
			
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
	
	public void updateMembershipForm(long groupId, MembershipForm form) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			GroupDO groupDO = new GroupDO();
			groupDO.updateMembershipForm(groupId, form);	
			
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
	
	public void addAdmin(long signedInUserId, long groupId, long memberUserId){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			GroupDO groupDO = new GroupDO();
			groupDO.addAdmin(signedInUserId, groupId, memberUserId);	
			
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
	
	
	public void removeMember(long signedInUserId, long groupId, long memberUserId){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			GroupDO groupDO = new GroupDO();
			groupDO.removeMember(signedInUserId, groupId, memberUserId);	
			
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
	
	public GroupMember getMember(long groupId, long memberUserId){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		GroupMember groupMember = null;
		try {
			transaction = session.beginTransaction();
			
			GroupDO groupDO = new GroupDO();
			User member = groupDO.getMember(groupId, memberUserId);	
			groupMember = groupDO.getGroupMemberFromUser(groupId, member);
			
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
		return groupMember;
	}
	
	public boolean isGroupNameExist(String name) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		boolean status = false;
		try {
			transaction = session.beginTransaction();
			
			GroupDO groupDO = new GroupDO();
			status = groupDO.isGroupNameExist(name);
			
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
		return status;
	}
}

























