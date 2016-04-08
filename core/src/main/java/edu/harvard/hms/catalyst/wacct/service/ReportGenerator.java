package edu.harvard.hms.catalyst.wacct.service;

/**
 * @author Bill Simons
 */

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IBundleCoverage;
import org.jacoco.core.tools.ExecFileLoader;
import org.jacoco.report.DirectorySourceFileLocator;
import org.jacoco.report.FileMultiReportOutput;
import org.jacoco.report.IReportVisitor;
import org.jacoco.report.html.HTMLFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

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

    private final String title = "foo";

    private final File classesDirectory;
    private final File sourceDirectory;
    private final File outputDirectory;

    private ExecFileLoader execFileLoader;

    /**
     * Create a new generator based for the given project.
     * @param outputDirectoryName
     */
    @Autowired
    public ReportGenerator(@Qualifier("outputDirectory") String outputDirectoryName) {
        this.classesDirectory = new File("/Users/bsimons/.m2/repository/edu/harvard/catalyst/scheduler/scheduler-core/2.17.0/scheduler-core-2.17.0.jar");
        this.sourceDirectory = new File("/Users/bsimons/scheduler/scheduler/target/");
        this.outputDirectory = new File(outputDirectoryName);
    }

    /**
     * Create the report.
     *
     * @throws IOException
     * @param executionData
     */
    public void generateReport(byte[] executionData) throws IOException {

        // Read the jacoco.exec file. Multiple data files could be merged
        // at this point
        loadExecutionData(executionData);

        // Run the structure analyzer on a single class folder to build up
        // the coverage model. The process would be similar if your classes
        // were in a jar file. Typically you would create a bundle for each
        // class folder and each jar you want in your report. If you have
        // more than one bundle you will need to add a grouping node to your
        // report
        final IBundleCoverage bundleCoverage = analyzeStructure();

        createReport(bundleCoverage);

    }

    private void createReport(final IBundleCoverage bundleCoverage)
            throws IOException {

        // Create a concrete report visitor based on some supplied
        // configuration. In this case we use the defaults
        final HTMLFormatter htmlFormatter = new HTMLFormatter();
        final IReportVisitor visitor = htmlFormatter
                .createVisitor(new FileMultiReportOutput(currentReportDir()));

        // Initialize the report with all of the execution and session
        // information. At this point the report doesn't know about the
        // structure of the report being created
        visitor.visitInfo(execFileLoader.getSessionInfoStore().getInfos(),
                execFileLoader.getExecutionDataStore().getContents());

        // Populate the report structure with the bundle coverage information.
        // Call visitGroup if you need groups in your report.
        visitor.visitBundle(bundleCoverage, new DirectorySourceFileLocator(
                sourceDirectory, "utf-8", 4));

        // Signal end of structure information to allow report to write all
        // information out
        visitor.visitEnd();

    }

    private File currentReportDir() {
        //TODO ugly?
        return new File(outputDirectory, String.valueOf(OffsetDateTime.now().toEpochSecond()));
    }

    private void loadExecutionData(byte[] executionData) throws IOException {
        execFileLoader = new ExecFileLoader();
        execFileLoader.load(new ByteArrayInputStream(executionData));
    }

    private IBundleCoverage analyzeStructure() throws IOException {
        final CoverageBuilder coverageBuilder = new CoverageBuilder();
        final Analyzer analyzer = new Analyzer(
                execFileLoader.getExecutionDataStore(), coverageBuilder);

        analyzer.analyzeAll(classesDirectory);

        return coverageBuilder.getBundle(title);
    }
}
