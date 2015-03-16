package net.danielkvist.async_rest;

import java.util.Date;
import java.util.HashMap;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JsonPropertyOrder({"id"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "book")
public class Book {

	@NotNull(message = "title is a required field")
	private String title;
	
	@NotNull(message = "author is a required field")
	private String author;
	
	private String isbn;
	private String id;
	private Date published;
	
	private HashMap<String, Object> extras = new HashMap<String, Object>();

	public String getAuthor() {
		return author;
	}

	@JacksonXmlProperty(isAttribute = true)
	public String getId() {
		return id; 
	}

	public String getIsbn() {
		return isbn;
	}

	public Date getPublished() {
		return published;
	}

	public String getTitle() {
		return title;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public void setPublished(Date published) {
		this.published = published;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@JsonAnyGetter
	public HashMap<String, Object> getExtras() {
		return extras;
	}
	
	@JsonAnySetter
	public void set(String key, Object value) {
		this.extras.put(key, value);
	}
	
}
