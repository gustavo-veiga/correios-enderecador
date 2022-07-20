package br.com.correios.enderecador.cep.gpbe;

import br.com.correios.enderecador.cep.CEPFactory;
import br.com.correios.enderecador.cep.InterfaceEnderecoDao;

public class GpbeCEPFactory extends CEPFactory {
    private static GpbeCEPFactory instance;

    public static synchronized GpbeCEPFactory getIntance() {
        if (instance == null)
            instance = new GpbeCEPFactory();
        return instance;
    }

    public InterfaceEnderecoDao getEndereco() {
        return EnderecoGpbeDao.getInstance();
    }
}
