package edu.harvard.hms.catalyst.wacct.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Bill Simons
 */
@Service
public class WacctService {

    private JacocoTcpClient client;
    private ReportGenerator generator;
    private String outputDirectoryName;
    private String reportContext;

    @Autowired
    public WacctService(JacocoTcpClient client, ReportGenerator generator,
            @Value("${wacct.outputDirectory}") String outputDirectoryName,
            @Value("${wacct.reportContext}") String reportContext) {

        this.client = client;
        this.generator = generator;
        this.outputDirectoryName = outputDirectoryName;
        this.reportContext = reportContext;
    }

    public void captureExecutionData() throws IOException {
        byte[] executionData = client.captureExecutionData();
        generator.generateReport(executionData);
    }

    public void resetExecutionData() throws IOException {
        client.resetExecutionData();
    }

    public List<ReportTuple> listReports() {
        String[] list = new File(outputDirectoryName).list();
        return Arrays.asList(list).stream().map(n -> new ReportTuple(reportContext, n)).collect(Collectors.toList());
    }
}
