package com.bridgelabz.fundooApp.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface AmazonService 
{
	String uploadFile(MultipartFile multipartFile,String token) throws IOException;

	String deleteFileFromS3Bucket(String fileName, String token);
}
