package it.polpetta.files

import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.nio.file.Path

class FileButler : FileHandler
{
    override fun read(path: Path): OutputStream
    {
        return File(path.toString()).outputStream()
    }

    override fun touch(path: Path): Boolean
    {
        return File(path.toString()).createNewFile()
    }

    override fun rm(path: Path): Boolean
    {
        return File(path.toString()).delete()
    }

    override fun write(path: Path, content: InputStream, append: Boolean)
    {
        if (exists(path))
        {
            File(path.toString()).writeBytes(content.readAllBytes());
        }
    }

    override fun mkdir(path: Path): Boolean
    {
        return File(path.toString()).mkdir()
    }

    override fun exists(path: Path): Boolean
    {
        return File(path.toString()).exists()
    }
}