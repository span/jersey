package net.danielkvist.async_rest;

import java.util.HashMap;

import javax.ws.rs.core.MediaType;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.filter.HttpMethodOverrideFilter;
import org.glassfish.jersey.server.filter.UriConnegFilter;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.fasterxml.jackson.jaxrs.xml.JacksonXMLProvider;
import com.sun.research.ws.wadl.Application;

public class BookApplication extends ResourceConfig {

	public BookApplication(final BookDao dao) {
		JacksonJsonProvider json = new JacksonJsonProvider().
				configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).
				configure(SerializationFeature.INDENT_OUTPUT, true);
		
		JacksonXMLProvider xml = new JacksonXMLProvider().
				configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).
				configure(SerializationFeature.INDENT_OUTPUT, true);
		
		packages("net.danielkvist.async_rest");
		property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
		
		HashMap<String, MediaType> mappings = new HashMap<String, MediaType>();
		mappings.put("xml", MediaType.APPLICATION_XML_TYPE);
		mappings.put("json", MediaType.APPLICATION_JSON_TYPE);
		UriConnegFilter ucf = new UriConnegFilter(mappings, null);		
		register(ucf);
		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(dao).to(BookDao.class);
			}
		});
		register(json);
		register(xml);
		register(HttpMethodOverrideFilter.class);
	}
}
