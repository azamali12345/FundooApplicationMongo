package com.bridgelabz.fundooApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundooApp.dto.NoteDto;
import com.bridgelabz.fundooApp.model.Note;
import com.bridgelabz.fundooApp.response.Response;
import com.bridgelabz.fundooApp.service.NoteService;

@RestController
@RequestMapping("/noteservice")
public class NoteController 
{
	@Autowired
	private NoteService noteService;

	@PostMapping("/note")
	public ResponseEntity<Response> createNote(@RequestBody NoteDto 
			noteDto, @RequestParam String token)
	{
		String message = noteService.createNote(noteDto, token);
		Response response = new Response(HttpStatus.OK
				.value(), message, null);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@PutMapping("/note")
	public ResponseEntity<Response> updateNote(@RequestBody NoteDto 
			noteDto, @RequestParam String noteId,
			@RequestParam String token) 
	{
		String message = noteService.updateNote(noteDto, noteId, token);
		Response response = new Response(HttpStatus.OK
				.value(), message, null);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@GetMapping("/note")
	public Note getNote(@RequestParam String noteId, 
			@RequestParam String token) 
	{
		Note note = noteService.getNote(noteId, token);
		return note;
	}

	@GetMapping("/notes")
	public List<Note> getAllNote(@RequestParam String token) 
	{
		List<Note> noteList = noteService.getAllNote(token);
		return noteList;
	}
}
