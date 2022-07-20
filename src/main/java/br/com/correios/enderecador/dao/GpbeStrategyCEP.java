package br.com.correios.enderecador.dao;

public class GpbeStrategyCEP implements StrategyCEP {
    public CEPFactory getFactory() {
        return CEPFactory.factory(2);
    }
}
