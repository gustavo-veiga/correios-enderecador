package br.com.correios.enderecador.cep;

import br.com.correios.enderecador.cep.dnec.DnecCEPFactory;
import br.com.correios.enderecador.cep.gpbe.GpbeCEPFactory;

public abstract class CEPFactory {
    public static final int DNEC = 1;
    public static final int GPBE = 2;

    public static CEPFactory factory(int type) {
        switch (type) {
            case DNEC:
                return DnecCEPFactory.getIntance();
            case GPBE:
                return GpbeCEPFactory.getIntance();
        }
        throw new IllegalArgumentException();
    }

    public abstract InterfaceEnderecoDao getEndereco();
}
