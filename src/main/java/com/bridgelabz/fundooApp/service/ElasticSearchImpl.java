package com.bridgelabz.fundooApp.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundooApp.exception.NoteException;
import com.bridgelabz.fundooApp.model.Note;
import com.bridgelabz.fundooApp.repository.NoteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.bulk.IndexRequest;

@Service
public class ElasticSearchImpl implements ElasticSearch 
{
	private final String INDEX = "elasticsearch";
	private String TYPE = "note";

	@Autowired
	private RestHighLevelClient restHighLevelClient;
	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private NoteRepository noteRepository;

	@Override
	public String createNote(Note note) 
	{
		@SuppressWarnings("unchecked")
		Map<String, Object> mapper = objectMapper.convertValue(note, Map.class);
		IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, note
				.getNoteId()).source(mapper);
		IndexResponse indexResponse = null;
		try 
		{
			indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}

		return indexResponse.getResult().name();
	}

	@Override
	public String updateNote(String noteId) 
	{
		Optional<Note> optNote = noteRepository.findByNoteId(noteId);
		if (optNote.isPresent()) 
		{
			Note note = optNote.get();
			@SuppressWarnings("unchecked")
			Map<String, Object> mapper = objectMapper.convertValue(note, Map.class);
			UpdateRequest updateRequest = new UpdateRequest(INDEX, TYPE, note
					.getNoteId()).doc(mapper);
			UpdateResponse updateResponse = null;
			try 
			{
				updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			return updateResponse.getResult().name();
		} 
		else 
		{
			throw new NoteException("note not present");
		}
	}

	@Override
	public String deleteNote(String noteId) 
	{
		DeleteRequest deleteRequest = new DeleteRequest(INDEX, TYPE, noteId);
		DeleteResponse deleteResponse = null;
		try 
		{
			deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return deleteResponse.getResult().name();
	}

	@Override
	public List<Note> searchByText(String title, String userId) 
	{
		SearchRequest searchRequest = new SearchRequest();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
				.must(QueryBuilders.queryStringQuery("*" + title + "*")
				.analyzeWildcard(true).field("title").field("description"))
				.filter(QueryBuilders.termQuery("userId", userId));
		searchSourceBuilder.query(queryBuilder);
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = null;
		try 
		{
			searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return getSearchResult(searchResponse);
	}

	private List<Note> getSearchResult(SearchResponse searchResponse) 
	{
		SearchHit[] searchHits = searchResponse.getHits().getHits();
		List<Note> noteList = new ArrayList<Note>();
		if (searchHits.length > 0) 
		{
			Arrays.stream(searchHits)
					.forEach(hit -> noteList.add(objectMapper
					.convertValue(hit.getSourceAsMap(), Note.class)));
		}
		return noteList;
	}
}
