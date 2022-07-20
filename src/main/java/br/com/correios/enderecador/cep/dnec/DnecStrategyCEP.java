package br.com.correios.enderecador.cep.dnec;

import br.com.correios.enderecador.cep.CEPFactory;
import br.com.correios.enderecador.cep.StrategyCEP;

public class DnecStrategyCEP implements StrategyCEP {
    public CEPFactory getFactory() {
        return CEPFactory.factory(1);
    }
}
