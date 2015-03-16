package net.danielkvist.async_rest;

import java.util.Collection;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.digest.DigestUtils;
import org.glassfish.jersey.server.ManagedAsync;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

@Path("/books")
public class BookResource {

	@Context BookDao dao;
	@Context Request request;
	
	@GET
	@Produces({"application/json;qs=1", "application/xml;qs=.5"})
	@ManagedAsync
	public void getBooks(@Suspended final AsyncResponse response) {
		//response.resume(dao.getBooks());
		ListenableFuture<Collection<Book>> bookFuture = dao.getBooksAsync();
		Futures.addCallback(bookFuture, new FutureCallback<Collection<Book>>() {
			public void onSuccess(Collection<Book> books) {
				response.resume(books);
			}
			
			public void onFailure(Throwable thrown) {
				response.resume(thrown);
			}
		});
	}
	
	@PoweredBy("customheader")
	@Path("/{id}")
	@GET
	@Produces({"application/json;qs=1", "application/xml;qs=.5"})
	@ManagedAsync
	public void getBook(@PathParam("id") String id, @Suspended final AsyncResponse response) {
		//response.resume(dao.getBook(id));
		ListenableFuture<Book> bookFuture = dao.getBookAsync(id);
		Futures.addCallback(bookFuture, new FutureCallback<Book>() {
			public void onSuccess(Book book) {
				//response.resume(book);
				EntityTag tag = generateEntityTag(book);
				Response.ResponseBuilder rb = request.evaluatePreconditions(tag);
				if(rb != null) {
					response.resume(rb.build());
				} else {
					response.resume(Response.ok().tag(tag).entity(book).build());
				}
			}
			
			public void onFailure(Throwable thrown) {
				response.resume(thrown);
			}
		});
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ManagedAsync
	public void addBook(@Valid @NotNull Book book, @Suspended final AsyncResponse response) {
		//response.resume(dao.addBook(book));
		ListenableFuture<Book> bookFuture = dao.addBookAsync(book);
		Futures.addCallback(bookFuture, new FutureCallback<Book>() {
			public void onSuccess(Book addedBook) {
				response.resume(addedBook);
			}
			
			public void onFailure(Throwable thrown) {
				response.resume(thrown);
			}
		});
	}
	
	@Path("/{id}")
	@PATCH
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ManagedAsync
	public void updateBook(@PathParam("id") final String id, final Book book, @Suspended final AsyncResponse response) {
		ListenableFuture<Book> getBookFuture = dao.getBookAsync(id);
		Futures.addCallback(getBookFuture, new FutureCallback<Book>() {
			public void onSuccess(Book originalBook) {
				Response.ResponseBuilder rb = request.evaluatePreconditions(generateEntityTag(originalBook));
				if(rb != null) {
					response.resume(rb.build());
				} else {
					ListenableFuture<Book> bookFuture = dao.updateBookAsync(id, book);
					Futures.addCallback(bookFuture, new FutureCallback<Book>() {
						public void onSuccess(Book updatedBook) { response.resume(updatedBook); }
						public void onFailure(Throwable thrown) { response.resume(thrown); }
					});
				}
			}
			
			public void onFailure(Throwable thrown) { response.resume(thrown); }
		});
	}
	
	EntityTag generateEntityTag(Book book) {
		return new EntityTag(DigestUtils.md5Hex(
				book.getAuthor() + book.getTitle() + 
				book.getPublished() + book.getExtras()));
	}
}
