package com.digitusrevolution.rideshare.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.util.RESTClientUtil;
import com.digitusrevolution.rideshare.model.billing.domain.core.Account;
import com.digitusrevolution.rideshare.user.business.UserRegistrationService;
import com.digitusrevolution.rideshare.user.dto.UserAccount;

public class UserSystemTest {

	private static final Logger logger = LogManager.getLogger(UserSystemTest.class.getName());
	
	public static void main(String args[]){
		
		logger.info("Logger Testing");
		
		UserRegistrationService userRegistrationService = new UserRegistrationService();
		UserAccount userAccount = new UserAccount();
		userAccount.setUserId(3);
		Account account = RESTClientUtil.getAccount(2);
		userAccount.setAccount(account);
		userRegistrationService.addAccount(userAccount);
		
		
	}
}
