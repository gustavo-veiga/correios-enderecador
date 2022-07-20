package br.com.correios.enderecador.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GerenciadorPropriedades {
    private final Properties p = new Properties();

    private final String arquivoPropriedade;

    public GerenciadorPropriedades(String arquivo) {
        this.arquivoPropriedade = arquivo;
        loadProperties();
    }

    public String getProperties(String v) {
        return this.p.getProperty(v);
    }

    public void setProperties(String p, String v) {
        this.p.setProperty(p, v);
    }

    private void loadProperties() {
        InputStream in = getClass().getResourceAsStream(this.arquivoPropriedade);
        if (in != null)
            try {
                this.p.load(in);
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    in.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
    }
}
