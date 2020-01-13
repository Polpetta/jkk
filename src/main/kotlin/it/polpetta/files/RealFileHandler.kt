package it.polpetta.files

import java.nio.file.Path

class RealFileHandler : FileHandler
{
    override fun read(path: Path): String
    {
        return ""
    }

    override fun create(path: Path)
    {

    }

    override fun delete(path: Path)
    {

    }

    override fun write(path: Path, content: String, append: Boolean)
    {

    }

    override fun mkdir(path: Path)
    {

    }
}