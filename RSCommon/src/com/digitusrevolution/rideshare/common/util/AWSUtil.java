package com.digitusrevolution.rideshare.common.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.UUID;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.WebApplicationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class AWSUtil {
	
	private static final Logger logger = LogManager.getLogger(AWSUtil.class.getName());
	
	public static String saveFileInS3(byte[] rawImage, String keyName) {
		String awsRootUrl = PropertyReader.getInstance().getProperty("AWS_S3_ROOT_URL");
		String bucketName = PropertyReader.getInstance().getProperty("GROUP_PHOTO_BUCKET_NAME");
		String AccessKeyID = PropertyReader.getInstance().getProperty("AWS_ACCESS_KEY");
		String SecretAccessKey = PropertyReader.getInstance().getProperty("AWS_SECRET_KEY");
		BasicAWSCredentials credentials = new BasicAWSCredentials(AccessKeyID, SecretAccessKey);
		//Region is very important else you will get exception
        AmazonS3 s3client = AmazonS3ClientBuilder.standard().withRegion(Regions.AP_SOUTH_1).withCredentials
        		(new AWSStaticCredentialsProvider(credentials)).build();
		//This is nothing but uploaded file name, we would use new name only if its not provided
        //So that we can override earlier file if exist
        String newKeyName = UUID.randomUUID().toString() + ".jpg"; 	
        String fullUrl = awsRootUrl + "/" + bucketName + "/" + newKeyName;
        try {
	    		InputStream bis = new ByteArrayInputStream(rawImage);
            logger.debug("Uploading a new object to S3 with name:"+keyName);
            AccessControlList acl = new AccessControlList();
            acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(rawImage.length);
            s3client.putObject(new PutObjectRequest(bucketName, newKeyName, bis, metadata).withAccessControlList(acl));
            if (keyName!=null) {
            		logger.debug("Deleting old file:"+keyName);
                s3client.deleteObject(new DeleteObjectRequest(bucketName, keyName));            	
            }
            logger.debug("Successfully uploaded file at url -"+fullUrl);
            return fullUrl;
        } catch (AmazonServiceException ase) {
        		throw new WebApplicationException("Unable to upload group photo", ase); 
        } catch (AmazonClientException ace) {
    			throw new WebApplicationException("Unable to upload group photo", ace); 
        }	
	}
	
	public static void sendEmail(String email, String subject, String body) throws MessagingException, UnsupportedEncodingException {
		
		// Replace sender@example.com with your "From" address.
		// This address must be verified.
		String FROM = PropertyReader.getInstance().getProperty("SMTP_FROM_ADDRESS");
		String FROMNAME = PropertyReader.getInstance().getProperty("SMTP_FROM_NAME");

		// Replace smtp_username with your Amazon SES SMTP user name.
		String SMTP_USERNAME = PropertyReader.getInstance().getProperty("SMTP_AWS_USERNAME");
		
		// Replace smtp_password with your Amazon SES SMTP password.
		String SMTP_PASSWORD = PropertyReader.getInstance().getProperty("SMTP_AWS_PASSWORD");

		// The name of the Configuration Set to use for this message.
		// If you comment out or remove this variable, you will also need to
		// comment out or remove the header below.
		//static final String CONFIGSET = "ConfigSet";

		// Amazon SES SMTP host name. This example uses the US West (Oregon) region.
		// See http://docs.aws.amazon.com/ses/latest/DeveloperGuide/regions.html#region-endpoints
		// for more information.
		String HOST = PropertyReader.getInstance().getProperty("SMTP_HOST");

		// The port you will connect to on the Amazon SES SMTP endpoint. 
		String PORT = PropertyReader.getInstance().getProperty("SMTP_PORT");
		
		// Replace recipient@example.com with a "To" address. If your account 
		// is still in the sandbox, this address must be verified.
		String TO = email;
		String SUBJECT = subject;
		String BODY = body;
		
		// Create a Properties object to contain connection configuration information.
		Properties props = System.getProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.port", PORT); 
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");

		// Create a Session object to represent a mail session with the specified properties. 
		Session session = Session.getDefaultInstance(props);

		// Create a message with the specified information. 
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(FROM,FROMNAME));
		msg.setRecipient(Message.RecipientType.TO, new InternetAddress(TO));
		msg.setSubject(SUBJECT);
		msg.setContent(BODY,"text/html");

		// Add a configuration set header. Comment or delete the 
		// next line if you are not using a configuration set
		//msg.setHeader("X-SES-CONFIGURATION-SET", CONFIGSET);

		// Create a transport.
		Transport transport = session.getTransport();

		// Send the message.
		try
		{
			logger.debug("Sending Email to "+email);

			// Connect to Amazon SES using the SMTP username and password you specified above.
			transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);

			// Send the email.
			transport.sendMessage(msg, msg.getAllRecipients());
			logger.debug("Email sent!");
		}
		catch (Exception ex) {
			logger.debug("The email was not sent.");
			logger.error("Error message: " + ex.getMessage());
		}
		finally
		{
			// Close and terminate the connection.
			transport.close();
		}
		
	}
}
