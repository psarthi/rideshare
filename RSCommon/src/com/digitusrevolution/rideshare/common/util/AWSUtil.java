package com.digitusrevolution.rideshare.common.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

import javax.ws.rs.WebApplicationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class AWSUtil {
	
	private static final Logger logger = LogManager.getLogger(AWSUtil.class.getName());
	
	public static String saveFileInS3(byte[] rawImage) {
		String awsRootUrl = PropertyReader.getInstance().getProperty("AWS_S3_ROOT_URL");
		String bucketName = PropertyReader.getInstance().getProperty("GROUP_PHOTO_BUCKET_NAME");
		String AccessKeyID = PropertyReader.getInstance().getProperty("AWS_ACCESS_KEY");
		String SecretAccessKey = PropertyReader.getInstance().getProperty("AWS_SECRET_KEY");
		BasicAWSCredentials credentials = new BasicAWSCredentials(AccessKeyID, SecretAccessKey);
        AmazonS3 s3client = AmazonS3ClientBuilder.standard().withCredentials
        		(new AWSStaticCredentialsProvider(credentials)).build();
		//This is nothing but uploaded file name
        String keyName = UUID.randomUUID().toString() + ".jpg";
        String fullUrl = awsRootUrl + "/" + bucketName + "/" + keyName;
        try {
	    		InputStream bis = new ByteArrayInputStream(rawImage);
            logger.debug("Uploading a new object to S3 with name:"+keyName);
            AccessControlList acl = new AccessControlList();
            acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(rawImage.length);
            s3client.putObject(new PutObjectRequest(bucketName, keyName, bis, metadata).withAccessControlList(acl));
            
        } catch (AmazonServiceException ase) {
        		throw new WebApplicationException("Unable to upload group photo", ase); 
        } catch (AmazonClientException ace) {
    			throw new WebApplicationException("Unable to upload group photo", ace); 
        }	
        logger.debug("Successfully uploaded file at url -"+fullUrl);
        return fullUrl;
	}
}
