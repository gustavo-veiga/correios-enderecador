package br.com.correios.enderecador.dao;

public class DnecStrategyCEP implements StrategyCEP {
    public CEPFactory getFactory() {
        return CEPFactory.factory(1);
    }
}
