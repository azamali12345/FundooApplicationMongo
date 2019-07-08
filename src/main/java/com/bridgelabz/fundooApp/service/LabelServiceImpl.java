package com.bridgelabz.fundooApp.service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundooApp.dto.LabelDto;
import com.bridgelabz.fundooApp.exception.LabelException;
import com.bridgelabz.fundooApp.exception.UserException;
import com.bridgelabz.fundooApp.model.Label;
import com.bridgelabz.fundooApp.model.User;
import com.bridgelabz.fundooApp.repository.LabelRespository;
import com.bridgelabz.fundooApp.repository.NoteRepository;
import com.bridgelabz.fundooApp.repository.UserRepository;
import com.bridgelabz.fundooApp.utility.JWTTokenGenerator;

@Service
public class LabelServiceImpl implements LabelService 
{
	@Autowired
	private JWTTokenGenerator tokenGenerator;
	
	@Autowired
	private LabelRespository labelRespository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private NoteRepository noteRepository;

	@Override
	public String createLabel(String token, LabelDto labelDto) 
	{
		String userId = tokenGenerator.verifyToken(token);
		Optional<User> optUser = userRepository.findById(userId);
		if (optUser.isPresent()) 
		{
			User user = optUser.get();
			Label label = modelMapper.map(labelDto, Label.class);
			label.setCreationTime(LocalDateTime.now());
			label.setUpdateTime(LocalDateTime.now());
			label.setUserId(user.getUserid());
			labelRespository.save(label);
			return "label created";
		} 
		else 
		{
			throw new UserException("User not present");
		}
	}

	@Override
	public String updateLabel(String token, String labeId, 
			LabelDto labelDto) 
	{
		String userId = tokenGenerator.verifyToken(token);
		Optional<User> optUser = userRepository.findById(userId);
		if (optUser.isPresent()) 
		{
			Optional<Label> optLabel = labelRespository.findById(labeId);
			if (optLabel.isPresent()) 
			{
				Label label = optLabel.get();
				label.setLabelName(labelDto.getLabelName());
				label.setUpdateTime(LocalDateTime.now());
				labelRespository.save(label);
				// return new Response(200, "success", null);
				return "label updated";
			} 
			else 
			{
				// return new Response(202, "label id doesnt match", null);
				throw new LabelException("label id doesnt match");
			}
		} 
		else 
		{
			throw new UserException("User doesnt match");
		}
	}

	@Override
	public String deleteLabel(String token, String labelId) 
	{
		String userId = tokenGenerator.verifyToken(token);
		Optional<User> optUser = userRepository.findById(userId);
		System.out.println(userId);
		if (optUser.isPresent()) 
		{
			Optional<Label> optLabel = labelRespository.findById(labelId);
			System.out.println("LabelServiceImpl.deleteLabel()");
			if (optLabel.isPresent()) 
			{
				System.out.println("LabelServiceImpl.deleteLabel()");
				Label label = optLabel.get();
				labelRespository.delete(label);
				return "label deleted";

			} 
			else 
			{

				throw new LabelException("label doesnt exist");
			}
		} 
		else 
		{
			throw new UserException("User not found");
		}
	}

	@Override
	public Label getLabel(String token, String labelId) 
	{
		String userId = tokenGenerator.verifyToken(token);
		Optional<User> optUser = userRepository.findById(userId);
		if (optUser.isPresent()) 
		{
			Optional<Label> optLabel = labelRespository
					.findById(labelId);
			if (optLabel.isPresent()) 
			{
				Label label = optLabel.get();
				return label;
			} 
			else 
			{
				throw new LabelException("label not present");
			}
		} 
		else 
		{
			throw new UserException("User not present");
		}
	}
	
	@Override
	public List<Label> getAllLabel(String token) 
	{
		String userId = tokenGenerator.verifyToken(token);
		List<Label> labels = labelRespository.findByUserId(userId);
		return labels;
	}
}
