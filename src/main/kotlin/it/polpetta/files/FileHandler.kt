package it.polpetta.files

import java.io.InputStream
import java.io.OutputStream
import java.nio.file.Path

interface FileHandler {
    fun read(path : Path): OutputStream
    fun touch(path : Path): Boolean
    fun rm(path : Path): Boolean
    fun write(path : Path, content : InputStream, append : Boolean = false)
    fun mkdir(path : Path): Boolean
    fun exists(path : Path): Boolean
}