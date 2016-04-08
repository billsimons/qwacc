package edu.harvard.hms.catalyst.wacct.service;

/**
 * @author Bill Simons
 */

import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IBundleCoverage;
import org.jacoco.core.tools.ExecFileLoader;
import org.jacoco.report.DirectorySourceFileLocator;
import org.jacoco.report.FileMultiReportOutput;
import org.jacoco.report.IReportVisitor;
import org.jacoco.report.html.HTMLFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * This example creates a HTML report for eclipse like projects based on a
 * single execution data store called jacoco.exec. The report contains no
 * grouping information.
 * <p>
 * The class files under test must be compiled with debug information, otherwise
 * source highlighting will not work.
 */
@Component
public class ReportGenerator {

    private final File classesJar;
    private final File sourcesDirectory;
    private final File outputDirectory;

    private ExecFileLoader execFileLoader;

    /**
     * Create a new generator based for the given project.
     * @param outputDirectoryName
     * @param sourcesDirectory
     * @param classesJar
     */
    @Autowired
    public ReportGenerator(@Value("${wacct.outputDirectory}") String outputDirectoryName,
            @Value("${wacct.sourcesDirectory}")File sourcesDirectory, @Value("${wacct.classesJar}")File classesJar) {

        this.classesJar = classesJar;
        this.sourcesDirectory = sourcesDirectory;
        this.outputDirectory = new File(outputDirectoryName);
    }

    /**
     * Create the report.
     *
     * @throws IOException
     * @param executionData
     */
    public void generateReport(byte[] executionData) throws IOException {

        loadExecutionData(executionData);

        // Run the structure analyzer on a single class folder to build up
        // the coverage model. The process would be similar if your classes
        // were in a jar file. Typically you would create a bundle for each
        // class folder and each jar you want in your report. If you have
        // more than one bundle you will need to add a grouping node to your
        // report
        long time = new Date().getTime();

        final IBundleCoverage bundleCoverage = analyzeStructure(String.valueOf(time));
        createReport(bundleCoverage, time);

    }

    private void createReport(final IBundleCoverage bundleCoverage, final long time)
            throws IOException {

        // Create a concrete report visitor based on some supplied
        // configuration. In this case we use the defaults
        final HTMLFormatter htmlFormatter = new HTMLFormatter();
        final IReportVisitor visitor = htmlFormatter
                .createVisitor(new FileMultiReportOutput(currentReportDir(time)));

        // Initialize the report with all of the execution and session
        // information. At this point the report doesn't know about the
        // structure of the report being created
        visitor.visitInfo(execFileLoader.getSessionInfoStore().getInfos(),
                execFileLoader.getExecutionDataStore().getContents());

        // Populate the report structure with the bundle coverage information.
        // Call visitGroup if you need groups in your report.
        visitor.visitBundle(bundleCoverage, new DirectorySourceFileLocator(
                sourcesDirectory, "utf-8", 4));

        // Signal end of structure information to allow report to write all
        // information out
        visitor.visitEnd();

    }

    private File currentReportDir(final long time) {
        return new File(outputDirectory, String.valueOf(time));
    }

    private void loadExecutionData(byte[] executionData) throws IOException {
        execFileLoader = new ExecFileLoader();
        execFileLoader.load(new ByteArrayInputStream(executionData));
    }

    private IBundleCoverage analyzeStructure(final String name) throws IOException {
        final CoverageBuilder coverageBuilder = new CoverageBuilder();
        final Analyzer analyzer = new Analyzer(
                execFileLoader.getExecutionDataStore(), coverageBuilder);

        analyzer.analyzeAll(classesJar);

        return coverageBuilder.getBundle(name);
    }
}
