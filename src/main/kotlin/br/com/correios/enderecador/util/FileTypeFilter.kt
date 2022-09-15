package br.com.correios.enderecador.util

import java.io.File
import javax.swing.filechooser.FileFilter

class FileTypeFilter(val fileExtension: FileExtension) : FileFilter()  {
    override fun accept(file: File): Boolean {
        if (file.isDirectory) {
            return true
        }
        return file.name.endsWith("." + fileExtension.extension)
    }

    override fun getDescription(): String {
        return "${fileExtension.description} *.${fileExtension}"
    }
}
