package br.com.correios.enderecador.util;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class FiltroArquivo extends FileFilter {
    private final String extension;

    public String getExtension() {
        return this.extension;
    }

    public FiltroArquivo(String extension) {
        this.extension = extension;
    }

    public boolean accept(File f) {
        if (f.isDirectory())
            return true;
        String extension = Geral.getExtension(f);
        if (extension != null) {
            return extension.equals(this.extension);
        }
        return false;
    }

    public String getDescription() {
        if (this.extension.equals("csv"))
            return "CSV (separado por ponto-e-vírgula)(*.csv)";
        return "Texto (*.txt)";
    }
}
