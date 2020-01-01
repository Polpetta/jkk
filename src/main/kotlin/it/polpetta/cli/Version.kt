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

import com.github.ajalt.clikt.core.CliktCommand
import com.google.inject.Inject
import it.polpetta.config.VersionInfo
import it.polpetta.utils.JenkinsSession

class Version @Inject constructor(private val jenkinsSession: JenkinsSession) :
    CliktCommand(help = "Prints information about the cli and the server, if logged in") {
    override fun run() {
        val jenkinsSession = jenkinsSession.retrieveSession()

        if (jenkinsSession != null && jenkinsSession.api().systemApi().systemInfo().jenkinsVersion() != "-1")
        {
            println("""
            Local:
            Version: ${VersionInfo.NUMBER}
            Version name: ${VersionInfo.NAME}
            
            Remote:
            Version: ${jenkinsSession.api().systemApi().systemInfo().jenkinsVersion()}
            Endpoint: ${jenkinsSession.endPoint()}
            """.trimIndent())
        } else {
            println("""
            Version: ${VersionInfo.NUMBER}
            Version name: ${VersionInfo.NAME}
            """.trimIndent())
        }
    }
}