/*
 * jkk
 * Copyright (C) 2019 - 2019 Davide Polonio <poloniodavide@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package it.polpetta.cli

import com.cdancy.jenkins.rest.JenkinsClient
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.stub
import it.polpetta.config.VersionInfo
import it.polpetta.utils.JenkinsSession
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream


internal class VersionTest {
    private val jenkinsSessionMock = mock<JenkinsSession>()
    private val version = Version(jenkinsSessionMock)
    private val outContent = ByteArrayOutputStream()
    private val errContent = ByteArrayOutputStream()
    private val originalOut = System.out
    private val originalErr = System.err

    @BeforeEach
    fun setup_all() {
        System.setErr(PrintStream(errContent))
        System.setOut(PrintStream(outContent))
    }

    @AfterEach
    fun cleanup_all() {
        System.setErr(originalErr)
        System.setOut(originalOut)
    }

    @Tag("it")
    @Test
    fun `Version formatting before login`()
    {
        //Example test, this is not really a command worth testing
        jenkinsSessionMock.stub {
            on { retrieveSession() } doReturn null
        }
        version.run()
        assertEquals("""
            Version: ${VersionInfo.NUMBER}
            Version name: ${VersionInfo.NAME}
            """.trimIndent(), outContent.toString().trimIndent())
    }
}