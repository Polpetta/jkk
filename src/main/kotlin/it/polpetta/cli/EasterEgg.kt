package it.polpetta.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.google.common.io.Resources
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.list
import kotlinx.serialization.serializer
import java.nio.charset.Charset

class EasterEgg : CliktCommand(name = "thekillerisalwaysthebutler", help = "An easter egg ;)") {
    override fun run() {
        val json = Json(JsonConfiguration.Stable)
        val logos = json.parse(
            String.serializer().list,
            Resources.getResource("logos.json").readText(Charset.defaultCharset())
        )

        println("${logos.random()}\n")
        println("A git-like CLI for Jenkins written in Kotlin")
        println("https://github.com/Polpetta/jkk")
    }
}