package br.com.correios.enderecador.cep.dnec;

import br.com.correios.enderecador.cep.CEPFactory;
import br.com.correios.enderecador.cep.InterfaceEnderecoDao;

public class DnecCEPFactory extends CEPFactory {
    private static DnecCEPFactory instance;

    public static synchronized DnecCEPFactory getIntance() {
        if (instance == null)
            instance = new DnecCEPFactory();
        return instance;
    }

    public InterfaceEnderecoDao getEndereco() {
        return EnderecoDnecDao.getInstance();
    }
}
