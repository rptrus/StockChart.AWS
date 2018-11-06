package com.rohan.stockapp.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

@Service
public class AmazonClient {
	
	private AmazonS3 s3client;

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;
    @Value("${amazonProperties.bucketName}")
    private String bucketName;
    @Value("${amazonProperties.accessKey}")
    private String accessKey;
    //@Value("${amazonProperties.secretKey}")
    //private String secretKey;
    
    @PostConstruct
    public void initializeAmazon() {
       AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, System.getenv("SECRET_KEY"));
       this.setS3client(new AmazonS3Client(credentials)); // fix later
    }

	public AmazonS3 getS3client() {
		return s3client;
	}

	public void setS3client(AmazonS3 s3client) {
		this.s3client = s3client;
	} 

}
