package com.bridgelabz.fundooApp.service;
import java.util.List;

import com.bridgelabz.fundooApp.dto.NoteDto;
import com.bridgelabz.fundooApp.model.Note;

public interface NoteService 
{
	String createNote(NoteDto noteDto, String token);

	String updateNote(NoteDto noteDto, String noteId, String token);

	Note getNote(String noteId, String token);

	List<Note> getAllNote(String token);
	
	String trashAndUntrash(String token, String noteId);
	
	List<Note> getTrash(String token);
	
	String deleteNote(String noteId, String token);
	
	String archiveAndUnarchive(String token, String noteId);
	
	List<Note> getArchive(String token);
	
	String pinAndUnpin(String token, String noteId);

}
