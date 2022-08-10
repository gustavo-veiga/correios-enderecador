package br.com.correios.enderecador.util

import java.io.File
import javax.swing.filechooser.FileFilter

class FiltroArquivo(val extension: String) : FileFilter() {
    override fun accept(f: File): Boolean {
        if (f.isDirectory) return true
        val extension = Geral.getExtension(f)
        return if (extension != null) {
            extension == this.extension
        } else false
    }

    override fun getDescription(): String {
        return if (this.extension == "csv") "CSV (separado por ponto-e-vírgula)(*.csv)" else "Texto (*.txt)"
    }
}
