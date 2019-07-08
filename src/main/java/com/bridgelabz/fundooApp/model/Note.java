package com.bridgelabz.fundooApp.model;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

public class Note 
{
	@Id
	private String noteId;
	private String title;
	private String description;
	private LocalDateTime creationtTime;
	private LocalDateTime updateTime;
	private String userId;
	private boolean isTrash;
	private boolean isArchive;
	private boolean isPin;
	
	@DBRef
	private List<Label> labels;
	
	public Note() 
	{}

	public Note(String noteId, String title, String description, LocalDateTime creationtTime, 
			LocalDateTime updateTime,
			String userId, boolean isTrash, boolean isArchive, boolean isPin) 
	{
		super();
		this.noteId = noteId;
		this.title = title;
		this.description = description;
		this.creationtTime = creationtTime;
		this.updateTime = updateTime;
		this.userId = userId;
		this.isTrash = isTrash;
		this.isArchive = isArchive;
		this.isPin = isPin;
	}

	public String getNoteId() 
	{
		return noteId;
	}

	public void setNoteId(String noteId) 
	{
		this.noteId = noteId;
	}

	public String getTitle() 
	{
		return title;
	}

	public void setTitle(String title) 
	{
		this.title = title;
	}

	public String getDescription() 
	{
		return description;
	}

	public void setDescription(String description) 
	{
		this.description = description;
	}

	public LocalDateTime getCreationtTime() 
	{
		return creationtTime;
	}

	public void setCreationtTime(LocalDateTime creationtTime) 
	{
		this.creationtTime = creationtTime;
	}

	public LocalDateTime getUpdateTime() 
	{
		return updateTime;
	}

	public void setUpdateTime(LocalDateTime updateTime) 
	{
		this.updateTime = updateTime;
	}

	public String getUserId() 
	{
		return userId;
	}

	public void setUserId(String userId) 
	{
		this.userId = userId;
	}

	public boolean isTrash() 
	{
		return isTrash;
	}

	public void setTrash(boolean isTrash) 
	{
		this.isTrash = isTrash;
	}

	public boolean isArchive() 
	{
		return isArchive;
	}

	public void setArchive(boolean isArchive) 
	{
		this.isArchive = isArchive;
	}

	public boolean isPin() 
	{
		return isPin;
	}

	public void setPin(boolean isPin) 
	{
		this.isPin = isPin;
	}
}
	