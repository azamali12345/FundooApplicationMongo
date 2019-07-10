package com.bridgelabz.fundooApp.service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundooApp.dto.NoteDto;
import com.bridgelabz.fundooApp.exception.NoteException;
import com.bridgelabz.fundooApp.exception.UserException;
import com.bridgelabz.fundooApp.model.Note;
import com.bridgelabz.fundooApp.model.User;
import com.bridgelabz.fundooApp.repository.NoteRepository;
import com.bridgelabz.fundooApp.repository.UserRepository;
import com.bridgelabz.fundooApp.utility.JWTTokenGenerator;

@Service
public class NoteServiceImpl implements NoteService 
{
	@Autowired
	private JWTTokenGenerator tokenGenerator;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private NoteRepository noteRepository;

	@Override
	public String createNote(NoteDto noteDto, String token) 
	{
		String userId = tokenGenerator.verifyToken(token);
		Optional<User> optUser = userRepository.findByUserId(userId);
		if (optUser.isPresent()) 
		{
			Note note = modelMapper.map(noteDto, Note.class);
			note.setCreationtTime(LocalDateTime.now());
			note.setUpdateTime(LocalDateTime.now());
			note.setUserId(userId);
			noteRepository.save(note);
			return "note created";
		} 
		else 
		{
			throw new NoteException("note not created");
		}
	}

	@Override
	public String updateNote(NoteDto noteDto, String noteId, 
			String token) 
	{
		String userId = tokenGenerator.verifyToken(token);
		Optional<User> optUser = userRepository.findByUserId(userId);
		if (optUser.isPresent()) 
		{
			Optional<Note> optNote = noteRepository.findById(noteId);
			if (optNote.isPresent()) 
			{
				Note note = optNote.get();
				note.setUpdateTime(LocalDateTime.now());
				note.setTitle(noteDto.getTitle());
				note.setDescription(noteDto.getDescription());
				noteRepository.save(note);
				return "note updated";
			} 
			else 
			{
				throw new NoteException("note doesnt exist");
			}
		} 
		else 
		{
			throw new UserException("User doesnt exist");
		}
	}

	@Override
	public Note getNote(String noteId, String token) 
	{
		String userId = tokenGenerator.verifyToken(token);
		Optional<User> optUser = userRepository.findByUserId(userId);
		if (optUser.isPresent()) 
		{
			Optional<Note> optNote = noteRepository.findById(noteId);
			if (optNote.isPresent()) 
			{
				Note note = optNote.get();
				return note;
			} 
			else 
			{
				throw new NoteException("noteId doesnt match");
			}
		} 
		else 
		{
			throw new UserException("User is not present");
		}
	}

	@Override
	public List<Note> getAllNote(String token) 
	{
		String userId = tokenGenerator.verifyToken(token);
		List<Note> notes = noteRepository.findAll();
		List<Note> filteredNotes = notes.stream().filter(note -> {
			return note.getUserId().equals(userId);
		}).collect(Collectors.toList());
		return filteredNotes;
		/*
		 * List<NoteDto> noteslist = new ArrayList<NoteDto>(); 
		 * for (Note userNotes :note) 
		 * { 
		 * NoteDto noteDto = modelMapper.map(userNotes, NoteDto.class);
		 * noteslist.add(noteDto);
		 * } return noteslist;
		 */
	}
}
