package edu.harvard.hms.catalyst.wacct.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jersey.spi.resource.Singleton;
import edu.harvard.hms.catalyst.wacct.service.ReportTuple;
import edu.harvard.hms.catalyst.wacct.service.WacctService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Bill Simons
 */
@Path("/")
@Singleton
@Component
public class WacctResource {
    private final Gson gson;
    private WacctService service;

    @Autowired
    public WacctResource(WacctService service) {
        this.service = service;

        final GsonBuilder builder = new GsonBuilder();
        gson = builder.create();
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

    @POST
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public String listReports() {
        List<ReportTuple> reportTuples = service.listReports();

        Collections.sort(reportTuples, (o1, o2) -> (int) (Long.valueOf(o2.getName()) - Long.valueOf(o1.getName())));

        return gson.toJson(reportTuples);
    }

    //TODO
    //delete report?
    //what's the best way to return report?  just redirect?
    //can we collect aggregate data over time? is that useful?
}
