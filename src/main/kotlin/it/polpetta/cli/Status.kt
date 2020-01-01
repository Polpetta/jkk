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
import com.google.inject.Inject
import it.polpetta.utils.JenkinsSession
import it.polpetta.utils.printErrln
import kotlin.system.exitProcess

class Status @Inject constructor(private val jenkinsSession: JenkinsSession) :
    CliktCommand(help = "Show the working tree status") {

    private val jobName: String by argument("job_name", "Name of the Job to display")

    override fun run() {
        val session = jenkinsSession.retrieveSession {
            printErrln("ABORT: No Jenkins instance to connect to")
            exitProcess(1)
        }!!

        val jobInfo = session.api().jobsApi().jobInfo(null, jobName)
        val lastBuild = jobInfo.lastBuild()
        val prettyIsBuilding = if(lastBuild.building()) {
            "building"
        } else {
            "completed"
        }
        val description = lastBuild.description() ?: "no description available"
        val title = lastBuild.displayName() ?: "no title available"
        println(
            """
            Latest build on job ${jobInfo.displayName()}
            Build number: ${lastBuild.number()}
            Build url: ${lastBuild.url()}
            Status: $prettyIsBuilding
            Duration: ${lastBuild.duration()}

            Title: $title
            Description:
            $description
        """.trimIndent()
        )
    }
}