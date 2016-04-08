package edu.harvard.hms.catalyst.wacct.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author Bill Simons
 */
@Service
public class WacctService {

    private JacocoTcpClient client;
    private ReportGenerator generator;

    @Autowired
    public WacctService(JacocoTcpClient client, ReportGenerator generator) {
        this.client = client;
        this.generator = generator;
    }

    public void captureExecutionData() throws IOException {
        byte[] executionData = client.captureExecutionData();
        generator.generateReport(executionData);
    }

    public void resetExecutionData() throws IOException {
        client.resetExecutionData();
    }
}
