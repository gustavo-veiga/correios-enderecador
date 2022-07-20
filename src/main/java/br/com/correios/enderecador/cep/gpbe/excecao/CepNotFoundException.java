package br.com.correios.enderecador.cep.gpbe.excecao;

public class CepNotFoundException extends Exception {
    public CepNotFoundException() {
    }

    public CepNotFoundException(String msg) {
        super(msg);
    }
}
