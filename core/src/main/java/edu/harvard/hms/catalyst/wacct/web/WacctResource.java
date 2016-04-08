package edu.harvard.hms.catalyst.wacct.web;

import com.sun.jersey.spi.resource.Singleton;
import edu.harvard.hms.catalyst.wacct.service.WacctService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * @author Bill Simons
 */
@Path("/")
@Singleton
@Component
public class WacctResource {
    private WacctService service;

    @Autowired
    public WacctResource(WacctService service) {
        this.service = service;
    }

    @POST
    @Path("/capture")
    public Response captureExecutionData() throws IOException {
        service.captureExecutionData();
        return Response.ok().build();
    }

    @POST
    @Path("/reset")
    public Response resetExecutionData() throws IOException {
        service.resetExecutionData();
        return Response.ok().build();
    }

    //TODO
    //show list available reports
    //delete report?
    //what's the best way to return report?  just redirect?
    //can we collect aggregate data over time? is that useful?
}
