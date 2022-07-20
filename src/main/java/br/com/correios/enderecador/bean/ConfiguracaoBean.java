package br.com.correios.enderecador.bean;

import br.com.correios.enderecador.dao.ConfiguracaoDao;
import br.com.correios.enderecador.dao.DaoException;
import br.com.correios.enderecador.dao.DnecStrategyCEP;
import br.com.correios.enderecador.dao.GpbeStrategyCEP;
import br.com.correios.enderecador.dao.StrategyCEP;
import br.com.correios.enderecador.excecao.EnderecadorExcecao;
import br.com.correios.enderecador.util.GerenciadorPropriedades;

import java.io.File;
import java.util.Vector;

public class ConfiguracaoBean {
    private String caminhoImagem = "/imagens/";

    private String caminhoImagem2d = (new File("")).getAbsolutePath();

    private String mostraMensagem = "Sim";

    private String versao = "2.4.4";

    private String novaVersao = "";

    private String caminhoRelatorio = "relatorios/";

    private String chave = "";

    private String proxy = "";

    private String porta = "";

    private String dominio = "";

    private String urlBanco = "";

    private String senhaBanco = "";

    private String arquivoHelp = "";

    private String driverBanco = "org.hsqldb.jdbcDriver";

    private String usuarioBanco = "SA";

    private String paginaEnderecador = "http://www2.correios.com.br/enderecador/escritorio/default.cfm";

    private String paginaPesquisaCep = "http://www.buscacep.correios.com.br/";

    private String urlBuscaCep = "http://www2.correios.com.br/enderecador/requisicao/xmlPesquisa.cfm";

    private String paginaFaleConosco = "http://www2.correios.com.br/sistemas/falecomoscorreios/";

    private boolean utilizaProxy;

    private static ConfiguracaoBean configuracaoBean;

    private String banco = null;

    private final StrategyCEP cepStrategy;
    public static final String GPBE = "GPBE";
    public static final String DNEC = "DNEC";

    private ConfiguracaoBean() {
        GerenciadorPropriedades propriedades = new GerenciadorPropriedades("/enderecador.properties");
        this.banco = propriedades.getProperties("banco.cep").trim().toUpperCase();
        if (this.banco.equalsIgnoreCase(GPBE)) {
            this.cepStrategy = new GpbeStrategyCEP();
        } else {
            this.cepStrategy = new DnecStrategyCEP();
        }
        setUrlBanco("jdbc:hsqldb:" + System.getProperty("user.dir") + System.getProperty("file.separator") + "lib" + System.getProperty("file.separator") + "db" + System.getProperty("file.separator") + "dbenderecadorect");
    }

    public static ConfiguracaoBean getInstance() {
        if (configuracaoBean == null)
            configuracaoBean = new ConfiguracaoBean();
        return configuracaoBean;
    }

    public void carregaVariaveis() throws EnderecadorExcecao {
        try {
            ConfiguracaoDao configuracaoDao = ConfiguracaoDao.getInstance();
            Vector<String> dadosConfig = configuracaoDao.recuperaConfiguracao("");
            if (dadosConfig.size() != 0) {
                setProxy((dadosConfig.get(0) == null) ? "" : dadosConfig.get(0));
                setPorta((dadosConfig.get(1) == null) ? "" : dadosConfig.get(1));
                setChave((dadosConfig.get(2) == null) ? "" : dadosConfig.get(2));
                setDominio((dadosConfig.get(3) == null) ? "" : dadosConfig.get(3));
            }
        } catch (DaoException ex) {
            throw new EnderecadorExcecao(ex.getMessage(), ex);
        }
    }

    public String getCaminhoImagem() {
        return this.caminhoImagem;
    }

    public void setCaminhoImagem(String caminhoImagem) {
        this.caminhoImagem = caminhoImagem;
    }

    public String getCaminhoRelatorio() {
        return this.caminhoRelatorio;
    }

    public void setCaminhoRelatorio(String caminhoRelatorio) {
        this.caminhoRelatorio = caminhoRelatorio;
    }

    public boolean getUtilizaProxy() {
        return this.utilizaProxy;
    }

    public void setUtilizaProxy(boolean utilizaProxy) {
        this.utilizaProxy = utilizaProxy;
    }

    public String getProxy() {
        return this.proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public String getPorta() {
        return this.porta;
    }

    public void setPorta(String porta) {
        this.porta = porta;
    }

    public String getDominio() {
        return this.dominio;
    }

    public void setDominio(String dominio) {
        this.dominio = dominio;
    }

    public String getPaginaPesquisaCep() {
        return this.paginaPesquisaCep;
    }

    public void setPaginaPesquisaCep(String paginaPesquisaCep) {
        this.paginaPesquisaCep = paginaPesquisaCep;
    }

    public String getPaginaEnderecador() {
        return this.paginaEnderecador;
    }

    public void setPaginaEnderecador(String paginaEnderecador) {
        this.paginaEnderecador = paginaEnderecador;
    }

    public String getUrlBuscaCep() {
        return this.urlBuscaCep;
    }

    public String getUrlBanco() {
        return this.urlBanco;
    }

    public void setUrlBanco(String urlBanco) {
        this.urlBanco = urlBanco;
    }

    public String getDriverBanco() {
        return this.driverBanco;
    }

    public void setDriverBanco(String driverBanco) {
        this.driverBanco = driverBanco;
    }

    public String getSenhaBanco() {
        return this.senhaBanco;
    }

    public void setSenhaBanco(String senhaBanco) {
        this.senhaBanco = senhaBanco;
    }

    public String getUsuarioBanco() {
        return this.usuarioBanco;
    }

    public void setUsuarioBanco(String usuarioBanco) {
        this.usuarioBanco = usuarioBanco;
    }

    public String getChave() {
        return this.chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getVersao() {
        return this.versao;
    }

    public void setVersao(String versao) {
        this.versao = versao;
    }

    public String getArquivoHelp() {
        return this.arquivoHelp;
    }

    public void setArquivoHelp(String arquivoHelp) {
        this.arquivoHelp = arquivoHelp;
    }

    public String getMostraMensagem() {
        return this.mostraMensagem;
    }

    public void setMostraMensagem(String mostraMensagem) {
        this.mostraMensagem = mostraMensagem;
    }

    public String getPaginaFaleConosco() {
        return this.paginaFaleConosco;
    }

    public void setPaginaFaleConosco(String paginaFaleConosco) {
        this.paginaFaleConosco = paginaFaleConosco;
    }

    public String getNovaVersao() {
        return this.novaVersao;
    }

    public void setNovaVersao(String novaVersao) {
        this.novaVersao = novaVersao;
    }

    public String getBanco() {
        return this.banco;
    }

    public StrategyCEP getCepStrategy() {
        return this.cepStrategy;
    }

    public String getCaminhoImagem2d() {
        return this.caminhoImagem2d;
    }

    public void setCaminhoImagem2d(String caminhoImagem2d) {
        this.caminhoImagem2d = caminhoImagem2d;
    }
}
