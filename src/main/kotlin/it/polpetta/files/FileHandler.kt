package it.polpetta.files

import java.nio.file.Path

interface FileHandler {
    fun read(path : Path): String
    fun create(path : Path)
    fun delete(path : Path)
    fun write(path : Path, content : String, append : Boolean = false)
    fun mkdir(path : Path)
}