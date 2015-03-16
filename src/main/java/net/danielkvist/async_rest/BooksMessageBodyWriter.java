package net.danielkvist.async_rest;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@Provider
@Produces(MediaType.APPLICATION_XML)
public class BooksMessageBodyWriter implements
		MessageBodyWriter<Collection<Book>> {
	
	@JacksonXmlRootElement(localName = "books")
	public class BooksWrapper {
		
		@JacksonXmlElementWrapper(useWrapping = false)
		@JacksonXmlProperty(localName = "book")
		public Collection<Book> books;
		
		public BooksWrapper(Collection<Book> books) {
			this.books = books;
		}
	}
	
	@Context Providers providers;

	@Override
	public long getSize(Collection<Book> books, Class<?> type,
			Type genericType, Annotation[] annotations, MediaType mediaType) {

		return -1;
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return Collection.class.isAssignableFrom(type);
	}

	@Override
	public void writeTo(Collection<Book> books, Class<?> type,
			Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException,
			WebApplicationException {
		// Simple pass on the writing to an existing writer now that we have wrapped the xml correctly
		providers.getMessageBodyWriter(BooksWrapper.class, genericType, annotations, mediaType).
			writeTo(new BooksWrapper(books), type, genericType, annotations, mediaType, httpHeaders, entityStream);
	}
}
