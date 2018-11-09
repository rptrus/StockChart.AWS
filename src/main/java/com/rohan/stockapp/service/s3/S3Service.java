package com.rohan.stockapp.service.s3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
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
	
	public boolean uploadToS3(String bucketname, String key, InputStream is, long size) {
		boolean status = true;
		ObjectMetadata objectMetaData = new ObjectMetadata();
		objectMetaData.setContentLength(size);
		PutObjectResult putObjectResult = amazonClient.getS3client().putObject(new PutObjectRequest(bucketname, key, is, objectMetaData)
		            .withCannedAcl(CannedAccessControlList.PublicRead));
		logger.info("Created file with eTag {}",putObjectResult.getETag());
		return status;
	}
	
	  public byte[] readFromS3(String bucketName, String key) throws IOException {
		  S3Object s3object = amazonClient.getS3client().getObject(new GetObjectRequest(bucketName, key));
	      byte[] byteArray = IOUtils.toByteArray(s3object.getObjectContent());
	      return byteArray;
	  }



}
