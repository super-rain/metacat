package com.netflix.metacat.server.jersey;

import com.netflix.metacat.common.MetacatContext;
import com.netflix.metacat.common.util.MetacatContextManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Created by amajumdar on 8/3/15.
 */
@Provider
public class MetacatRestFilter implements ContainerRequestFilter, ContainerResponseFilter{
    private static final Logger log = LoggerFactory.getLogger(MetacatRestFilter.class);
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String userName = requestContext.getHeaderString(MetacatContext.HEADER_KEY_USER_NAME);
        if( userName == null){
            userName = "metacat";
        }
        String clientAppName = requestContext.getHeaderString(MetacatContext.HEADER_KEY_CLIENT_APP_NAME);
        String clientHost = requestContext.getHeaderString("X-Forwarded-For");
        String jobId = requestContext.getHeaderString(MetacatContext.HEADER_KEY_JOB_ID);
        String dataTypeContext = requestContext.getHeaderString(MetacatContext.HEADER_KEY_DATA_TYPE_CONTEXT);
        MetacatContext context = new MetacatContext( userName, clientAppName, clientHost, jobId, dataTypeContext);
        MetacatContextManager.setContext(context);
        log.info(context.toString());
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException {
        MetacatContextManager.removeContext();
    }
}