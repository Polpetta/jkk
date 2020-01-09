package it.polpetta.cli

import com.nhaarman.mockitokotlin2.*
import it.polpetta.api.jenkins.Api
import it.polpetta.api.jenkins.Build
import it.polpetta.api.jenkins.Job
import it.polpetta.api.jenkins.Version
import it.polpetta.config.VersionInfo
import it.polpetta.utils.JenkinsSession
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.ByteArrayOutputStream
import java.io.PrintStream

internal class StatusTest {

    private val sessionMock = mock<JenkinsSession>()
    private val status = Status(sessionMock)
    private val apiMock = mock<Api>()
    private val jobMock = mock<Job>()
    private val buildMock = mock<Build>()
    private val outContent = ByteArrayOutputStream()
    private val errContent = ByteArrayOutputStream()
    private val originalOut = System.out
    private val originalErr = System.err

    @BeforeEach
    fun setUp()
    {
        sessionMock.stub {
            on { retrieveSession() } doReturn apiMock
        }
        apiMock.stub {
            on { getJob("cook") } doReturn jobMock
        }
        jobMock.stub {
            on { getLastBuild() } doReturn buildMock
            on { getName() } doReturn "cook"
        }
        System.setErr(PrintStream(errContent))
        System.setOut(PrintStream(outContent))
    }

    @AfterEach
    fun cleanup() {
        System.setErr(originalErr)
        System.setOut(originalOut)
    }

    @Test
    fun `building build`()
    {
        buildMock.stub {
            on { getStatus() } doReturn Build.Status.BUILDING
            on { getId() } doReturn 42
            on { getDuration() } doReturn 1234
            on { getUrl() } doReturn "http://www.savewalterwhite.com/"
            on { getTitle() } doReturn "Say my name"
            on { getDescription() } doReturn "You're goddamn right"
        }
        status.main(arrayOf("cook"))
        //TODO we need to find a better way to test text commands
        assertEquals("""
            Latest build on job cook
            Build id: 42
            Build url: http://www.savewalterwhite.com/
            Status: building
            Duration: 1234

            Title: Say my name
            Description:
            You're goddamn right
        """.trimIndent(), outContent.toString().trimIndent())
    }

    @Test
    fun `completed build`()
    {
        buildMock.stub {
            on { getStatus() } doReturn Build.Status.CANCELLED
            on { getId() } doReturn 42
            on { getDuration() } doReturn 1234
            on { getUrl() } doReturn "http://www.savewalterwhite.com/"
            on { getTitle() } doReturn "Say my name"
            on { getDescription() } doReturn "You're goddamn right"
        }
        status.main(arrayOf("cook"))
        assertEquals("""
            Latest build on job cook
            Build id: 42
            Build url: http://www.savewalterwhite.com/
            Status: completed
            Duration: 1234

            Title: Say my name
            Description:
            You're goddamn right
        """.trimIndent(), outContent.toString().trimIndent())
    }
}