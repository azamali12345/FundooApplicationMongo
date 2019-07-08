package com.bridgelabz.fundooApp.service;
import java.util.List;

import com.bridgelabz.fundooApp.dto.LabelDto;
import com.bridgelabz.fundooApp.model.Label;

public interface LabelService 
{
	String createLabel(String token, LabelDto labelDto);

	String updateLabel(String token, String labeId, LabelDto labelDto);

	String deleteLabel(String token, String labelId);

	Label getLabel(String token, String labelId);

	List<Label> getAllLabel(String token);
	
}
