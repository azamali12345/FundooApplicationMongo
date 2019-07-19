package com.bridgelabz.fundooApp.service;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.bridgelabz.fundooApp.exception.UserException;
import com.bridgelabz.fundooApp.model.User;
import com.bridgelabz.fundooApp.repository.UserRepository;
import com.bridgelabz.fundooApp.utility.JWTTokenGenerator;

@Service
public class AmazonServiceImpl implements AmazonService
{
	private AmazonS3 s3client;
	
	@Autowired
	private UserRepository userRepository;

	@Value("https://988750701731.signin.aws.amazon.com/console")
	private String endpointUrl;
	
	@Value("azam-bucket")
	private String bucketName;
	
	@Value("AKIA6MNRCMSRU3CVI2GO")
	private String accessKey;
	
	@Value("9K3Ic16CPb2iPraA8urAR1i2qekoZw+0AwCza5xk")
	private String secretKey;

	@Autowired
	private JWTTokenGenerator tokenGenerator;
	   
	@SuppressWarnings("deprecation")
	@PostConstruct
	private void initializeAmazon() 
	{
		AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
		this.s3client = new AmazonS3Client(credentials);
	}

	private File convertMultiPartToFile(MultipartFile file) throws IOException 
	{
		File convFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

	private String generateFileName(MultipartFile multiPart) 
	{
		return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
	}

	private void uploadFileTos3bucket(String fileName, File file) 
	{
		s3client.putObject(new PutObjectRequest (bucketName, fileName, file)
				.withCannedAcl(CannedAccessControlList.PublicRead));
	}

	@Override
	public String uploadFile(MultipartFile multipartFile,String token) throws IOException 
	{
		String userId=tokenGenerator.verifyToken(token);
		Optional<User> optUser=userRepository.findById(userId);
		if(optUser.isPresent())
		{
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
			String fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
			uploadFileTos3bucket(fileName,  file);
			file.delete();
			return fileUrl;
		}
		else
		{
			throw new UserException("User not present");
		}
	}

	@Override
	public String deleteFileFromS3Bucket(String fileName, String token) 
	{
		String userId=tokenGenerator.verifyToken(token);
		Optional<User> optionaluser = userRepository.findById(userId);
		if (optionaluser.isPresent()) 
		{
			User user = optionaluser.get();
			s3client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
			user.setImageUrl(null);
			userRepository.save(user);
			return "file deleted";
		}
		else
		{
			throw new UserException("User not present");
		}
	}
}