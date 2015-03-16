package net.danielkvist.async_rest;

import java.io.IOException;
import java.lang.annotation.Annotation;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
@PoweredBy
public class PoweredByFilter implements ContainerResponseFilter {

	@Override
	public void filter(ContainerRequestContext request,
			ContainerResponseContext response) throws IOException {
		for(Annotation a : response.getEntityAnnotations()) {
			if(a.annotationType() == PoweredBy.class) {
				String value = ((PoweredBy) a).value();
				response.getHeaders().add("X-Powered-By", value);
			}
		}
	}

}
