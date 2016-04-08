package edu.harvard.hms.catalyst.wacct.web;

import com.sun.jersey.spi.resource.Singleton;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * @author Bill Simons
 */
@Path("/")
@Singleton
@Component
public class WacctResource {
    @GET
    @Path("/hello")
    public String getVersionInfo() {
        return "hello world";
    }
}
