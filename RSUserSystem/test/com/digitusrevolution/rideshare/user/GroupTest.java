package com.digitusrevolution.rideshare.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.model.user.domain.MembershipForm;
import com.digitusrevolution.rideshare.model.user.domain.MembershipRequest;
import com.digitusrevolution.rideshare.model.user.domain.core.Group;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.user.data.GroupDAO;
import com.digitusrevolution.rideshare.user.domain.core.GroupDO;
import com.digitusrevolution.rideshare.user.domain.core.UserDO;

public class GroupTest {

	private static final Logger logger = LogManager.getLogger(UserFriendTest.class.getName());

	public static void main(String args[]){


		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		try {
			transation = session.beginTransaction();

			GroupTest groupTest = new GroupTest();
			groupTest.test();

			
			transation.commit();

			/*
			 * Reason for catching RuntimeException and not HibernateException as all exceptions thrown by Hibernate
			 * is not of type HibernateException such as NotFoundException
			 */
		} catch (RuntimeException e) {
			if (transation!=null){
				logger.error("Transaction Failed, Rolling Back");
				transation.rollback();
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
		
		
		GroupDO groupDO = new GroupDO();
		Group group = new Group();
		group.setName("Group-1");
		group.setUrl("url-1");
		
		MembershipForm form = new MembershipForm();
		form.getQuestions().add("Question-1");
		form.getQuestions().add("Question-2");
		form.getQuestions().add("Question-3");
		form.getQuestions().add("Question-4");
		form.setUserUniqueIdentifierName("Employee Id");
		group.setMembershipForm(form);
//		groupDO.createGroup(group, 1);
			
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
		groupDO.removeMember(1, 4);
		
	}
}







































