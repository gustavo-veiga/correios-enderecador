package br.com.correios.enderecador.excecao;

public class ConsultaCepException extends EnderecadorExcecao {
    public ConsultaCepException() {
    }

    public ConsultaCepException(String msg) {
        super(msg);
    }

    public ConsultaCepException(String msg, Throwable t) {
        super(msg, t);
    }
}
