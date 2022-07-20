package br.com.correios.enderecador.dao;

public class CepInvalidoException extends DaoException {
    public CepInvalidoException() {
    }

    public CepInvalidoException(String msg) {
        super(msg);
    }

    public CepInvalidoException(String msg, Throwable t) {
        super(msg, t);
    }
}
