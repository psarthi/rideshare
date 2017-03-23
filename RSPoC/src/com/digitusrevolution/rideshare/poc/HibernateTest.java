package com.digitusrevolution.rideshare.poc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.db.HibernateUtil;

public class HibernateTest {
	
	private static final Logger logger = LogManager.getLogger(HibernateTest.class.getName());
	
	public static void main(String[] args) {
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		try {
			transaction = session.beginTransaction();

			GenericDAOImpl<A, Integer> genericDAOImplA = new GenericDAOImpl<>(A.class);
			GenericDAOImpl<B, Integer> genericDAOImplB = new GenericDAOImpl<>(B.class);
//			for (int i=0;i<5;i++){
//				int id = genericDAOImplA.create(new A());
//				System.out.println("Created Id:"+id);
//				id = genericDAOImplB.create(new B());
//				System.out.println("Created Id:"+id);				
//			}
//			B b1 = genericDAOImplB.get(5);
			B b2 = genericDAOImplB.get(6);
			A a = genericDAOImplA.get(5);
//			a.getbCollection().add(b1);
			a.getbCollection().remove(b2);
			genericDAOImplA.update(a);

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

}
