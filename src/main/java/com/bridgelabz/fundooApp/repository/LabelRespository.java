package com.bridgelabz.fundooApp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bridgelabz.fundooApp.model.Label;

public interface LabelRespository extends MongoRepository<Label, String> 
{
	List<Label> findByUserId(String userid);
	
	Optional<Label> findByLabelIdAndUserId(String labelId, String userId);
}
