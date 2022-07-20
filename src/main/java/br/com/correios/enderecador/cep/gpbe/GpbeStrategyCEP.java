package br.com.correios.enderecador.cep.gpbe;

import br.com.correios.enderecador.cep.CEPFactory;
import br.com.correios.enderecador.cep.StrategyCEP;

public class GpbeStrategyCEP implements StrategyCEP {
    public CEPFactory getFactory() {
        return CEPFactory.factory(2);
    }
}
