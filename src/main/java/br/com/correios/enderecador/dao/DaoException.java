package br.com.correios.enderecador.dao;

public class DaoException extends Exception {
    public DaoException() {
    }

    public DaoException(String msg) {
        super(msg);
    }

    public DaoException(String msg, Throwable t) {
        super(msg, t);
    }
}
