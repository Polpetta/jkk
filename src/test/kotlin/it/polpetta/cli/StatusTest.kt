package it.polpetta.cli

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
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

    private val sessionMock : JenkinsSession = mockk()
    private val status = Status(sessionMock)
    private val apiMock : Api = mockk()
    private val jobMock : Job = mockk()
    private val buildMock : Build = mockk()
    private val outContent = ByteArrayOutputStream()
    private val errContent = ByteArrayOutputStream()
    private val originalOut = System.out
    private val originalErr = System.err

    @BeforeEach
    fun setUp()
    {
        clearAllMocks()
        every { sessionMock.retrieveSession() } answers { apiMock }
        every { apiMock.getJob("cook") } answers { jobMock }
        every { jobMock.getLastBuild() } answers { buildMock }
        every { jobMock.getName() } answers { "cook" }

        System.setErr(PrintStream(errContent))
        System.setOut(PrintStream(outContent))
    }

    @AfterEach
    fun cleanup()
    {
        System.setErr(originalErr)
        System.setOut(originalOut)
    }

    @Test
    fun `building build`()
    {
        every { buildMock.getStatus() } answers { Build.Status.BUILDING }
        every { buildMock.getId() } answers { 42 }
        every { buildMock.getDuration() } answers { 1234 }
        every { buildMock.getUrl() } answers { "http://www.savewalterwhite.com/" }
        every { buildMock.getTitle() } answers { "Say my name" }
        every { buildMock.getDescription() } answers { "You're goddamn right" }

        status.main(arrayOf("cook"))
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
        every { buildMock.getStatus() } answers { Build.Status.CANCELLED }
        every { buildMock.getId() } answers { 42 }
        every { buildMock.getDuration() } answers { 1234 }
        every { buildMock.getUrl() } answers { "http://www.savewalterwhite.com/" }
        every { buildMock.getTitle() } answers { "Say my name" }
        every { buildMock.getDescription() } answers { "You're goddamn right" }

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