package it.polpetta.cli

import com.nhaarman.mockitokotlin2.*
import it.polpetta.api.jenkins.Api
import it.polpetta.api.jenkins.Version
import it.polpetta.utils.JenkinsSession
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

internal class LoginTest{

    private val sessionMock = mock<JenkinsSession>()
    private val apiMock = mock<Api>()
    private val versionMock = mock<Version>()
    private val login = Login(sessionMock);

    private val urlCaptor = argumentCaptor<String>()
    private val userCaptor = argumentCaptor<String>()
    private val passwordCaptor = argumentCaptor<String>()

    @BeforeEach
    fun setUp()
    {
        sessionMock.stub {
            on { with(urlCaptor.capture(), userCaptor.capture(), passwordCaptor.capture()) } doReturn apiMock
        }
        apiMock.stub {
            on { getVersion() } doReturn versionMock
        }
        versionMock.stub {
            on { isValid() } doReturn true
        }
    }
    //TODO: we need to find a way to manage file system writes and reads
    //  because now it's just writing to the real file system even when testing
    @Test
    fun `with url as an argument`()
    {
        login.main(listOf("https://totallyAJenkinsInstance", "-u username", "-p password"))
        assertEquals("https://totallyAJenkinsInstance", urlCaptor.firstValue)
        assertEquals("username", userCaptor.firstValue.trim())
        assertEquals("password", passwordCaptor.firstValue.trim())
    }

    @Test
    fun `without url as an argument`()
    {
        login.main(listOf("-u username", "-p password"))
        assertEquals("http://localhost/", urlCaptor.firstValue)
        assertEquals("username", userCaptor.firstValue.trim())
        assertEquals("password", passwordCaptor.firstValue.trim())
    }
}