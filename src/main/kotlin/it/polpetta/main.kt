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

@file:JvmName("Cli")
package it.polpetta

import com.github.ajalt.clikt.core.subcommands
import com.google.inject.Guice
import it.polpetta.cli.Login
import it.polpetta.cli.Root
import it.polpetta.cli.Status
import it.polpetta.cli.Version
import it.polpetta.modules.JkkModule
import it.polpetta.utils.debugThrow
import it.polpetta.utils.printErrln
import kotlin.system.exitProcess
import dev.misfitlabs.kotlinguice4.getInstance

fun main(args: Array<String>) {
    val injector = Guice.createInjector(JkkModule())

    try {
        Root().subcommands(
            injector.getInstance<Version>(),
            injector.getInstance<Login>(),
            injector.getInstance<Status>()
        ).main(args)
    }
    catch (e: Exception) {
        e.message?.let { printErrln("Error: $it") }
        debugThrow(e)
        exitProcess(1)
    }
}