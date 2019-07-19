package com.bridgelabz.fundooApp.controller;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.fundooApp.response.Response;
import com.bridgelabz.fundooApp.service.AmazonService;

@RestController
@RequestMapping("/storage")
public class BucketController 
{
	@Autowired
	private AmazonService amazonService;
	
	@PostMapping("/uploadFile")
	public ResponseEntity<Response> uploadFile(@RequestPart MultipartFile multipartFile, 
			@RequestHeader String token) throws IOException 
	{
		String url=amazonService.uploadFile(multipartFile, token);
		Response response=new Response(HttpStatus.OK
				.value(),"uploaded file successfully..", url);
		return new ResponseEntity<Response>(response,HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteFile")
	public ResponseEntity<Response> deleteFile(@RequestHeader String fileName, 
			@RequestHeader String token) 
	{
		String message = amazonService.deleteFileFromS3Bucket(fileName, token);
		Response response=new Response(HttpStatus.OK.value(), message, null);
		return new ResponseEntity<Response>(response,HttpStatus.OK);
	}
}
