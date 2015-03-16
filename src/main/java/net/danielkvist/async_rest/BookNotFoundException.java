package net.danielkvist.async_rest;

public class BookNotFoundException extends Exception {
	
	public BookNotFoundException(String s) {
		super(s);
	}
}
