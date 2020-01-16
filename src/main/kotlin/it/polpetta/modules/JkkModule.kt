package it.polpetta.modules

import dev.misfitlabs.kotlinguice4.KotlinModule
import it.polpetta.files.FileButler
import it.polpetta.files.FileHandler

class JkkModule : KotlinModule() {
    override fun configure() {
        bind<FileHandler>().to<FileButler>()
    }
}