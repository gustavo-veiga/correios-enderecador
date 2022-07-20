package br.com.correios.enderecador.excecao;

public class ConfiguracaoProxyException extends ConsultaCepException {
    public ConfiguracaoProxyException() {
    }

    public ConfiguracaoProxyException(String msg) {
        super(msg);
    }

    public ConfiguracaoProxyException(String msg, Throwable t) {
        super(msg, t);
    }
}
