package com.digitusrevolution.rideshare.billing;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

import com.digitusrevolution.rideshare.common.util.AWSUtil;

public class SendEmailTest {
	
	public static void main(String[] args){
		
		
		String TO = "partha.sarthi@parift.com";

		String SUBJECT = "Amazon SES test (SMTP interface accessed using Java)";

		String BODY = String.join(
				System.getProperty("line.separator"),
				"<h1>Amazon SES SMTP Email Test</h1>",
				"<p>This email was sent with Amazon SES using the ", 
				"<a href='https://github.com/javaee/javamail'>Javamail Package</a>",
				" for <a href='https://www.java.com'>Java</a>."
				);

		
		try {
			AWSUtil.sendEmail(TO, SUBJECT, BODY);
		} catch (UnsupportedEncodingException | MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
