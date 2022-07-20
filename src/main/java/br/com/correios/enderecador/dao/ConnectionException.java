package br.com.correios.enderecador.dao;

public class ConnectionException extends DaoException {
    public ConnectionException() {
    }

    public ConnectionException(String msg) {
        super(msg);
    }

    public ConnectionException(String msg, Throwable t) {
        super(msg, t);
    }
}
