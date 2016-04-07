package edu.harvard.hms.catalyst.qwacc;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jacoco.core.data.ExecutionDataWriter;
import org.jacoco.core.runtime.RemoteControlReader;
import org.jacoco.core.runtime.RemoteControlWriter;

/**
 * @author Bill Simons
 *         This example connects to a coverage agent that run in output mode
 *         <code>tcpserver</code> and requests execution data. The collected data is
 *         dumped to a local file.
 */
public final class Client {

    private static final String DESTFILE = "jacoco-client.exec";

    private static final String ADDRESS = "127.0.0.1";

    private static final int PORT = 6300;

    /**
     * Starts the execution data request.
     *
     * @param args
     * @throws IOException
     */
    public static void main(final String[] args) throws IOException {
        //TODO major cleanup... checking in to share
        String address;
        int port;
        String destFile;
        Options options = new Options()
                .addOption("a", true, "host address to connect to")
                .addOption("p", true, "port")
                .addOption("f", true, "destination file for dump");


        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine line = parser.parse(options, args);

            if(line.hasOption("a")) {
                address = line.getOptionValue("a");
            } else {
                address = ADDRESS;
            }
            if(line.hasOption("p")) {
                port = Integer.valueOf(line.getOptionValue("p"));
            } else {
                port = PORT;
            }
            if(line.hasOption("f")) {
                destFile = line.getOptionValue("f");
            } else {
                destFile = DESTFILE;
            }
        } catch(ParseException e) {
            e.printStackTrace();
            return;
        }


        final FileOutputStream localFile = new FileOutputStream(destFile);
        final ExecutionDataWriter localWriter = new ExecutionDataWriter(
                localFile);

        // Open a socket to the coverage agent:
        final Socket socket = new Socket(InetAddress.getByName(address), port);
        final RemoteControlWriter writer = new RemoteControlWriter(
                socket.getOutputStream());
        final RemoteControlReader reader = new RemoteControlReader(
                socket.getInputStream());
        reader.setSessionInfoVisitor(localWriter);
        reader.setExecutionDataVisitor(localWriter);

        // Send a dump command and read the response:
        writer.visitDumpCommand(true, false);
        reader.read();

        socket.close();
        localFile.close();
    }

    private Client() {
    }
}