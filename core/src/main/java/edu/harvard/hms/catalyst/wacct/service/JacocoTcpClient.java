package edu.harvard.hms.catalyst.wacct.service;

import org.jacoco.core.data.ExecutionDataWriter;
import org.jacoco.core.runtime.RemoteControlReader;
import org.jacoco.core.runtime.RemoteControlWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author Bill Simons
 *         Connects to a coverage agent that is running in output mode
 *         <code>tcpserver</code> and requests execution data.
 */

@Component
public final class JacocoTcpClient {

    private String address;
    private int port;

    @Autowired
    public JacocoTcpClient(@Value("${wacct.address}") String address, @Value("${wacct.port}") String port) {
        this.address = address;
        this.port = Integer.parseInt(port);
    }

    public byte[] captureExecutionData() throws IOException {

        try (final ByteArrayOutputStream localStream = new ByteArrayOutputStream();
             final Socket socket = new Socket(InetAddress.getByName(address), port)) {

            final ExecutionDataWriter localWriter = new ExecutionDataWriter(localStream);

            final RemoteControlWriter writer = new RemoteControlWriter(socket.getOutputStream());
            final RemoteControlReader reader = new RemoteControlReader(socket.getInputStream());
            reader.setSessionInfoVisitor(localWriter);
            reader.setExecutionDataVisitor(localWriter);

            writer.visitDumpCommand(true, false);
            reader.read();

            return localStream.toByteArray();
        }
    }

    public void resetExecutionData() throws IOException {
        try (final Socket socket = new Socket(InetAddress.getByName(address), port)) {

            final RemoteControlWriter writer = new RemoteControlWriter(socket.getOutputStream());
            writer.visitDumpCommand(false, true);
        }
    }
}