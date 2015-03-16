package net.danielkvist.async_rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BookNotFoundMapper implements ExceptionMapper<BookNotFoundException> {
	
	public Response toResponse(BookNotFoundException e) {
		return Response.status(404).entity(e.getMessage()).type("text/plain").build();
	}
}
