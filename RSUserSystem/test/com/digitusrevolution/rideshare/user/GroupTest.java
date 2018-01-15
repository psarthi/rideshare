package com.digitusrevolution.rideshare.user;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.model.user.domain.GroupFeedback;
import com.digitusrevolution.rideshare.model.user.domain.MembershipForm;
import com.digitusrevolution.rideshare.model.user.domain.MembershipRequest;
import com.digitusrevolution.rideshare.model.user.domain.Vote;
import com.digitusrevolution.rideshare.model.user.domain.core.Group;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.user.data.GroupDAO;
import com.digitusrevolution.rideshare.user.domain.core.GroupDO;
import com.digitusrevolution.rideshare.user.domain.core.UserDO;

public class GroupTest {

	private static final Logger logger = LogManager.getLogger(UserFriendTest.class.getName());

	public static void main(String args[]){


		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		try {
			transaction = session.beginTransaction();

			GroupTest groupTest = new GroupTest();
			groupTest.test();

			
			transaction.commit();

			/*
			 * Reason for catching RuntimeException and not HibernateException as all exceptions thrown by Hibernate
			 * is not of type HibernateException such as NotFoundException
			 */
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
	
	private void test(){
		
		GroupDAO groupDAO = new GroupDAO();
		int size = groupDAO.getMemberCount(3);
		System.out.println("Size is:"+size);
		
		size = groupDAO.getAdmins(2).size();
		System.out.println("Group Admins Counts is:"+size);
		System.out.println("User is Admin:"+groupDAO.isAdmin(2, 2));
		System.out.println("User is Member:"+groupDAO.isAdmin(2, 1));
		
		/*
		GroupDAO groupDAO = new GroupDAO();
	//	groupDAO.getMember(1, 4);
		
		GroupDO groupDO = new GroupDO();
		Group group = new Group();
		group.setName("Group-1");
		group.setUrl("url-1");
		
		UserDO userDO = new UserDO();
		User user = userDO.get(1);
		group.setOwner(user);
		
		MembershipForm form = new MembershipForm();
		form.getQuestions().add("Question-1");
		form.getQuestions().add("Question-2");
		form.getQuestions().add("Question-3");
		form.getQuestions().add("Question-4");
		form.setUserUniqueIdentifierName("Employee Id");
		group.setMembershipForm(form);
		groupDO.createGroup(group);
			
		/*
		MembershipRequest membershipRequest = new MembershipRequest();
		membershipRequest.getQuestionAnswers().put("Question-1", "Answer-1");
		membershipRequest.getQuestionAnswers().put("Question-2", "Answer-2");
		membershipRequest.getQuestionAnswers().put("Question-3", "Answer-3");
		membershipRequest.setUserUniqueIdentifier("Employee Id-1");
		UserDO userDO = new UserDO();
		User user = userDO.get(4);
		membershipRequest.setUser(user);
//		groupDO.sendMembershipRequest(1, membershipRequest);
//		groupDO.approveMembershipRequest(1, 4);
//		groupDO.rejectMembershipRequest(1, 2, "remark");
//		groupDO.removeMember(1, 4);
//		groupDO.addAdmin(1, 3);
		List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(2);
		list.add(3);
		//groupDO.inviteUsers(1, list);
		GroupFeedback feedback = new GroupFeedback();
		feedback.setVote(Vote.Fake);
		groupDO.giveFeedback(1, 3, feedback);
		*/
	}
}







































