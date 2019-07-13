package com.bridgelabz.fundooApp.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
		if(optUser.isPresent()) 
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
	public String updateNote(NoteDto noteDto, String noteId, String token) 
	{
		String userId = tokenGenerator.verifyToken(token);
		Optional<User> optUser = userRepository.findByUserId(userId);
		if(optUser.isPresent()) 
		{
			Optional<Note> optNote = noteRepository.findById(noteId);
			if(optNote.isPresent()) 
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
				throw new NoteException("Note doesnt exist");
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
		if(optUser.isPresent()) 
		{
			Optional<Note> optNote = noteRepository.findById(noteId);
			if(optNote.isPresent()) 
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
		List<Note> notesList = new ArrayList<Note>();

		for(Note userNotes : notes)
		{
			notesList.add(userNotes);
		}
		return notesList;
	}

	@Override
	public String deleteNote(String noteId, String token) 
	{
		String userId = tokenGenerator.verifyToken(token);
		Optional<User> optUser = userRepository.findByUserId(userId);
		if(optUser.isPresent()) 
		{
			Optional<Note> optNote = noteRepository.findById(noteId);
			if (optNote.isPresent()) 
			{
				Note note = optNote.get();
				if(note.isTrash()) 
				{
					noteRepository.delete(note);
					return "note deleted";
				} 
				else 
				{
					throw new NoteException("note not peresent in trash");
				}
			} 
			else 
			{
				throw new NoteException("Note is not present");
			}
		} 
		else 
		{
			throw new UserException("User doesnt exist");
		}
	}

	@Override
	public String trashAndUntrash(String token, String noteId) 
	{
		String userId = tokenGenerator.verifyToken(token);
		Optional<User> optUser = userRepository.findByUserId(userId);
		if(optUser.isPresent()) 
		{
			Optional<Note> optNote = noteRepository.findById(noteId);
			if(optNote.isPresent()) 
			{
				Note note = optNote.get();
				if(note.isTrash()) 
				{
					note.setTrash(false);
					noteRepository.save(note);
					return "Note is untrash";
				} 
				else 
				{
					note.setTrash(true);
					noteRepository.save(note);
					return "Note is trash";
				}
			} 
			else 
			{
				throw new NoteException("Note is not present");
			}
		} 
		else 
		{
			throw new UserException("User doesnt exist");
		}
	}

	@Override
	public List<Note> getTrash(String token) 
	{
		String userId = tokenGenerator.verifyToken(token);
		List<Note> notes = noteRepository.findByUserId(userId);
		List<Note> notesList = new ArrayList<Note>();
		for(Note note : notes) 
		{
			if(note.isTrash()) 
			{
				notesList.add(note);
			}
		}
		return notesList;
	}

	@Override
	public String archiveAndUnarchive(String token, String noteId) 
	{
		String userId = tokenGenerator.verifyToken(token);
		Optional<User> optUser = userRepository.findById(userId);
		if(optUser.isPresent()) 
		{
			Optional<Note> optNote = noteRepository.findById(noteId);
			if(optNote.isPresent()) 
			{
				Note note = optNote.get();
				if(note.isArchive()) 
				{
					note.setArchive(false);
					noteRepository.save(note);
					return "Note is Unarchive";
				} 
				else 
				{
					note.setArchive(true);
					noteRepository.save(note);
					return "Note is archive";
				}
			} 
			else 
			{
				throw new NoteException("Note is not present");
			}
		} 
		else 
		{
			throw new UserException("User doesnt exist");
		}
	}

	@Override
	public List<Note> getArchive(String token) 
	{
		String userId = tokenGenerator.verifyToken(token);
		return noteRepository.findByUserIdAndIsArchive(userId, true);
	}

	@Override
	public String pinAndUnpin(String token, String noteId) 
	{
		String userId = tokenGenerator.verifyToken(token);
		Optional<User> optUser = userRepository.findById(userId);
		if(optUser.isPresent()) 
		{
			Optional<Note> optNote = noteRepository.findById(noteId);
			if(optNote.isPresent()) 
			{
				Note note = optNote.get();
				if(note.isPin()) 
				{
					note.setPin(false);
					noteRepository.save(note);
					return "Note is Unpin";
				} 
				else 
				{
					note.setPin(true);
					noteRepository.save(note);
					return "Note is pin";
				}
			} 
			else 
			{
				throw new NoteException("Note is not presnet");
			}
		} 
		else 
		{
			throw new UserException("User doesnt exist");
		}
	}

	@Override
	public List<Note> sortByName(String token) 
	{
		String userId=tokenGenerator.verifyToken(token);
		Optional<User> optUser =userRepository.findById(userId);
		if(optUser.isPresent())
		{
			List<Note> noteList=noteRepository.findAll();
			noteList.sort(Comparator.comparing(Note::getTitle));
			
			/*for(int i=0;i<noteList.size();i++)
			{
				for(int j=0;j<noteList.size()-1;j++)
				{
					if(noteList.get(i).getTitle().compareTo(noteList.get(j).getTitle())>0)
					{
						Note note=noteList.get(i);
						noteList.set(i,noteList.get(j));
						noteList.set(j,note);
					}
				}
			}*/
			
			return noteList;
		}
		else
		{
			throw new UserException("User doesnt exist");
		}
	}
	
	@Override
	public List<Note> sortByDate(String token) 
	{
		String userId =tokenGenerator.verifyToken(token);
		Optional<User> optUser=userRepository.findById(userId);
		if (optUser.isPresent()) 
		{
			List<Note> noteList = noteRepository.findAll();
			noteList.sort(Comparator.comparing(Note::getCreationtTime).reversed());
			 
			/*for (int i=0;i<noteList.size();i++) 
			  { 
				  for (int j=0;j<noteList.size();j++) 
				  { 
					  if(noteList.get(i).getCreationtTime().compareTo(noteList.get(i)
							  .getCreationtTime())<0) 
					  { 
						  Note note = noteList.get(i); 
						  noteList.set(i,noteList.get(j)); 
						  noteList.set(j, note); 
					  } 
				  } 
			  }*/
			
			return noteList;
		} 
		else 
		{
			throw new UserException("User not present ");
		}
	}

	@Override
	public List<Note> sortById(String token) 
	{
		String userId =tokenGenerator.verifyToken(token);
		Optional<User> optionalNote =userRepository.findById(userId);
		if(optionalNote.isPresent()) 
		{
			List<Note> noteList = noteRepository.findAll();
			noteList.sort(Comparator.comparing(Note::getNoteId));
			
			/*for(int i=0;i<noteList.size();i++) 
			{ 
				for (int j=0;j<noteList.size();j++) 
				{ 
					if(noteList.get(i).getNoteId().compareTo(noteList.get(j)
							.getNoteId()) > 0) 
					{
						Note note =noteList.get(i); 
						noteList.set(i, noteList.get(j));
						noteList.set(j, note); 
					} 
				} 
			}*/
			
			return noteList;
		} 
		else 
		{
			throw new UserException("User not found");
		}
	}
}
