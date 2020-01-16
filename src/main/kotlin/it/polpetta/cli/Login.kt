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
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt
import com.google.inject.Inject
import com.uchuhimo.konf.Config
import com.uchuhimo.konf.source.toml.toToml
import it.polpetta.config.Auth
import it.polpetta.config.Resources
import it.polpetta.files.FileHandler
import it.polpetta.utils.JenkinsSession
import it.polpetta.utils.printErrln
import it.polpetta.utils.pwd
import java.nio.file.Path
import kotlin.system.exitProcess

class Login @Inject constructor(private val jenkinsSession: JenkinsSession, private val fileHandler: FileHandler) :
    CliktCommand(help = "Login to the remote Jenkins instance") {
    private val urlArgName: String = "server_url"
    private val url: String? by argument(urlArgName, "Jenkins server URL").optional()
    private val username: String? by option("-u", "--username", help = "Jenkins username").prompt("Username")
    private val password: String? by option("-p", "--password", help = "Jenkins password/auth token").prompt("Password")

    override fun run()
    {
        var validUrl: String = url.orEmpty()
        if (url == null || url.isNullOrBlank())
        {
            validUrl = "http://localhost/"
            println("No $urlArgName provided, using default url $validUrl")
        }
        val jenkins = jenkinsSession.with(validUrl, username, password)
        val authConfig = Config { addSpec(Auth) }
        val jenkinsVersion = jenkins.getVersion()

        if (jenkinsVersion.isValid())
        {
            println("Login succeed, found Jenkins $jenkinsVersion")

            authConfig[Auth.username] = username.orEmpty()
            authConfig[Auth.password] = password.orEmpty()
            authConfig[Auth.url] = validUrl

            val saveFile = Path.of(pwd(), Resources.JENKINS_AUTH_FILENAME)
            if (fileHandler.exists(saveFile))
            {
                fileHandler.rm(saveFile)
            }
            fileHandler.touch(saveFile)
            fileHandler.write(saveFile, authConfig.toToml.toText().byteInputStream())

            // TODO we should detect the current VCS used by the user and add the current credential files in the
            //  ignored files, in order to avoid accidentals commits
        }
        else
        {
            printErrln("Login failed")
            exitProcess(1)
        }
    }
}