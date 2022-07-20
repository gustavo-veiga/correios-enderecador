package br.com.correios.enderecador.dao;

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
