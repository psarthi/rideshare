package com.digitusrevolution.rideshare.billing;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.billing.business.AccountBusinessService;
import com.digitusrevolution.rideshare.billing.domain.core.VirtualAccountDO;
import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.model.billing.domain.core.Account;
import com.digitusrevolution.rideshare.model.billing.domain.core.AccountType;

@Path("/domain/loaddata/account")
public class AccountDataLoader {
	
	private static final Logger logger = LogManager.getLogger(AccountDataLoader.class.getName());
	
	@GET
	public static void main(String[] args) {
		
		AccountBusinessService accountService = new AccountBusinessService();
		Account account = new Account();
		account.setBalance(0);
		account.setType(AccountType.Virtual);
		
		for(int i=0; i<6;i++){
			accountService.create(account);					
		}
		
		AccountDataLoader accountDataLoader = new AccountDataLoader();
		accountDataLoader.creditMoney();
		
	}
	
	public void creditMoney(){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		try {
			transaction = session.beginTransaction();

			VirtualAccountDO accountDO = new VirtualAccountDO();
			for(int i=1;i<7;i++){
				accountDO.credit(i, 1000, "Initial Credit");
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
	}

}
