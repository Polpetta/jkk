package it.polpetta.cli

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import it.polpetta.api.jenkins.Api
import it.polpetta.api.jenkins.Version
import it.polpetta.utils.JenkinsSession
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class LoginTest{

    private val sessionMock : JenkinsSession = mockk()
    private val apiMock : Api = mockk()
    private val versionMock : Version = mockk()
    private val login = Login(sessionMock);

    private val urlSlot = slot<String>()
    private val userSlot = slot<String>()
    private val passwordSlot = slot<String>()

    @BeforeEach
    fun setUp()
    {
        clearAllMocks()
        every {
            sessionMock.with(capture(urlSlot), capture(userSlot), capture(passwordSlot))
        } answers { apiMock }

        every {
            apiMock.getVersion()
        } answers { versionMock }

        every {
            versionMock.isValid()
        } answers { true }
    }

    @Test
    fun `with url as an argument`()
    {
        login.main(listOf("https://totallyAJenkinsInstance", "-u username", "-p password"))
        assertEquals("https://totallyAJenkinsInstance", urlSlot.captured)
        assertEquals("username", userSlot.captured.trim())
        assertEquals("password", passwordSlot.captured.trim())
    }

    @Test
    fun `without url as an argument`()
    {
        login.main(listOf("-u username", "-p password"))
        assertEquals("http://localhost/", urlSlot.captured)
        assertEquals("username", userSlot.captured.trim())
        assertEquals("password", passwordSlot.captured.trim())
    }
}