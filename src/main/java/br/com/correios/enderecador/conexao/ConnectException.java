package br.com.correios.enderecador.conexao;

public class ConnectException extends Exception {
    public ConnectException() {
    }

    public ConnectException(String msg) {
        super(msg);
    }

    public ConnectException(String msg, Throwable t) {
        super(msg, t);
    }
}
