package com.rohan.stockapp.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Service
public class AmazonClient {
	
	private AmazonS3 s3Client;

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;
    @Value("${amazonProperties.bucketName}")
    private String bucketName;
    @Value("${amazonProperties.accessKey}")
    private String accessKey;
    //@Value("${amazonProperties.secretKey}")
    //private String secretKey;
	@Value("${amazonProperties.region:us-east-1:us-east-1}")
	private String region;    
    
    @PostConstruct
    public void initializeAmazon() {
    	// Must set the environment variables within AWS ELB
		s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.fromName(region))
                .build();
	
    }

	public AmazonS3 getS3client() {
		return s3Client;
	}

	public void setS3client(AmazonS3 s3client) {
		this.s3Client = s3client;
	} 

}
