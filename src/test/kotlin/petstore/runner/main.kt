package petstore.runner

import mu.KotlinLogging
import org.junit.platform.engine.discovery.DiscoverySelectors
import org.junit.platform.launcher.LauncherDiscoveryRequest
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder
import org.junit.platform.launcher.core.LauncherFactory
import org.junit.platform.launcher.listeners.SummaryGeneratingListener
import org.junit.platform.launcher.listeners.TestExecutionSummary
import org.junit.platform.reporting.legacy.xml.LegacyXmlReportGeneratingListener
import java.io.PrintWriter
import java.nio.file.Path
import kotlin.system.exitProcess

private val logger = KotlinLogging.logger {}

fun main() {
    try {
        logger.info { "Launching tests..." }
        val summaryListener = SummaryGeneratingListener()
        val xmlReportListener = LegacyXmlReportGeneratingListener(Path.of(""), PrintWriter(System.out))
        val request: LauncherDiscoveryRequest = LauncherDiscoveryRequestBuilder
            .request()
            .selectors(DiscoverySelectors.selectPackage("petstore"))
            .build()
        val launcher = LauncherFactory.create()
        launcher.discover(request)
        launcher.registerTestExecutionListeners(xmlReportListener, summaryListener)
        launcher.execute(request)
        val summary: TestExecutionSummary = summaryListener.summary
        val exitCode = if (summary.totalFailureCount == 0L) 0 else -1
        logger.info { "Tests have been done. Exit code is: $exitCode" }
        exitProcess(exitCode)
    } catch (e: Throwable) {
        logger.error(e) { "Unexpected error was happened." }
        exitProcess(-1)
    }
}