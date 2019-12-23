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
import it.polpetta.utils.Jenkins
import it.polpetta.utils.printErrln
import kotlin.system.exitProcess

class Status : CliktCommand(help = "Show the working tree status") {
    private val jobName: String by argument("job_name", "Name of the Job to display")
    override fun run() {
        val jenkins = Jenkins.session

        if (jenkins != null) {
            val jobInfo = jenkins.api().jobsApi().jobInfo(null, jobName)
            val lastBuild = jobInfo.lastBuild()
            val prettyIsBuilding = if (lastBuild.building()) {
                "building"
            } else {
                "completed"
            }
            val description = if (lastBuild.description() == null) {
                "no available description"
            } else {
                lastBuild.description()
            }
            println(
                """
                On job ${jobInfo.displayName()}
                Build number ${lastBuild.number()}
                Build url ${lastBuild.url()}
                Status: $prettyIsBuilding
                Duration: ${lastBuild.duration()}

                Title: ${lastBuild.displayName()}
                Description:
                $description
            """.trimIndent()
            )
        } else {
            printErrln("No Jenkins instance to connect to")
            exitProcess(1)
        }
    }
}