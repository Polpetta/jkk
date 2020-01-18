package it.polpetta.cli

import io.mockk.*
import it.polpetta.api.jenkins.Api
import it.polpetta.api.jenkins.Version
import it.polpetta.config.Resources
import it.polpetta.files.FileHandler
import it.polpetta.utils.JenkinsSession
import it.polpetta.utils.pwd
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.io.InputStream
import java.nio.file.Path
import org.assertj.core.api.Assertions.*

internal class LoginTest{

    private val sessionMock : JenkinsSession = mockk()
    private val apiMock : Api = mockk()
    private val versionMock : Version = mockk()
    private val fileHandler : FileHandler = mockk(relaxed = true)
    private val login = Login(sessionMock, fileHandler)
    private val configPath = Path.of(pwd(), Resources.JENKINS_AUTH_FILENAME)

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

    @Nested
    inner class `Argument URL`()
    {
        @BeforeEach
        fun classSetup()
        {
            fileHandler.apply {
                every {
                    exists(any())
                } answers { true }
            }
        }

        @Test
        fun `it should take that argument as URL`()
        {
            login.main(listOf("https://totallyAJenkinsInstance", "-u username", "-p password"))
            assertEquals(
                AuthParams("https://totallyAJenkinsInstance", "username", "password"),
                AuthParams(urlSlot.captured, userSlot.captured.trim(), passwordSlot.captured.trim())
            )
        }

        @Test
        fun `without url as an argument it should take localhost as default host`()
        {
            login.main(listOf("-u username", "-p password"))

            assertEquals(
                AuthParams("http://localhost/", "username", "password"),
                AuthParams(urlSlot.captured, userSlot.captured.trim(), passwordSlot.captured.trim())
            )
        }
    }

    @Nested
    inner class `Config file test`
    {
        @Test
        fun `it already exists`()
        {
            every {
                fileHandler.exists(configPath)
            } answers { true }
            login.main(listOf("https://totallyAJenkinsInstance", "-u username", "-p password"))

            verifyOrder {
                fileHandler.rm(configPath)
                fileHandler.touch(configPath)
                fileHandler.write(configPath, any())
            }
        }

        @Test
        fun `it doesn't exists`()
        {
            every {
                fileHandler.exists(configPath)
            } answers { false }
            login.main(listOf("https://totallyAJenkinsInstance", "-u username", "-p password"))
            
            verifyOrder {
                fileHandler.touch(configPath)
                fileHandler.write(configPath, any())
            }

            verify(exactly = 0) { fileHandler.rm(configPath) }
        }
    }

    data class AuthParams(
        val url: String,
        val username: String,
        val password: String
    )
}