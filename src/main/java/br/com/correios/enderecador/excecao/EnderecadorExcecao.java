package br.com.correios.enderecador.excecao;

public class EnderecadorExcecao extends Exception {
    public EnderecadorExcecao() {
    }

    public EnderecadorExcecao(String mensagem) {
        super(mensagem);
    }

    public EnderecadorExcecao(String msg, Throwable t) {
        super(msg, t);
    }
}
