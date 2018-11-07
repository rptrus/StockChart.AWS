package com.rohan.stockapp.service.s3;

import java.io.File;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.rohan.stockapp.config.AmazonClient;

@Service
public class S3Service {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AmazonClient amazonClient;
		
	public boolean uploadToS3(String bucketname, String key, File file) {
		boolean status = true;		
		PutObjectResult putObjectResult = amazonClient.getS3client().putObject(new PutObjectRequest(bucketname, key, file)
		            .withCannedAcl(CannedAccessControlList.PublicRead));
		logger.info("Created file with eTag {}",putObjectResult.getETag());
		return status;
	}

}
