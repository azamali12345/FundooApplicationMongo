package com.bridgelabz.fundooApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundooApp.dto.LabelDto;
import com.bridgelabz.fundooApp.model.Label;
import com.bridgelabz.fundooApp.response.Response;
import com.bridgelabz.fundooApp.service.LabelService;

@RestController
@RequestMapping("/labelservice")
public class LabelController 
{
	@Autowired
	private LabelService labelService;

	@PostMapping("/label")
	public ResponseEntity<Response> createLabel(@RequestParam String token,
			@RequestBody LabelDto labelDto) 
	{
		String message = labelService.createLabel(token, labelDto);
		Response response = new Response(HttpStatus.OK
				.value(), message, null);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@PutMapping("/label")
	public ResponseEntity<Response> updateLabel(@RequestParam String token,
			@RequestParam String labelId,
			@RequestBody LabelDto labelDto) 
	{
		String message = labelService.updateLabel(token, 
				labelId, labelDto);
		Response response = new Response(HttpStatus.OK
				.value(), message, null);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@DeleteMapping("/label")
	public ResponseEntity<Response> deleteLable(@RequestParam String token,
			@RequestParam String labelId) 
	{
		String message = labelService.deleteLabel(token, labelId);
		Response response = new Response(HttpStatus.OK
				.value(), message, null);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@GetMapping("/labels")
	public List<Label> getAllLabel(@RequestParam String token) 
	{
		List<Label> labelList = labelService.getAllLabel(token);
		return labelList;
	}

	@GetMapping("/label")
	public Label getLabel(@RequestParam String token, @RequestParam 
			String labelId)
	{
		Label label = labelService.getLabel(token, labelId);
		return label;
	}
}
