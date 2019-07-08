package com.bridgelabz.fundooApp.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bridgelabz.fundooApp.model.Note;

public interface NoteRepository extends MongoRepository<Note, String> 
{
	Optional<Note> findByNoteId(String noteid);
	
	List<Note> findByUserId(String token);

	Optional<Note> findByNoteIdAndUserId(String noteId, String userId);
	
	List<Note> findByUserIdAndIsArchive(String userId, boolean isArchive);
}
