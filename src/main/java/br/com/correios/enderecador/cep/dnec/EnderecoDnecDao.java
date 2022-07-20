package br.com.correios.enderecador.cep.dnec;

import br.com.correios.enderecador.bean.ConfiguracaoBean;
import br.com.correios.enderecador.bean.EnderecoBean;
import br.com.correios.enderecador.bean.UsuarioBean;
import br.com.correios.enderecador.cep.CepInvalidoException;
import br.com.correios.enderecador.cep.InterfaceEnderecoDao;
import br.com.correios.enderecador.dao.DaoException;
import br.com.correios.enderecador.excecao.ConfiguracaoProxyException;
import br.com.correios.enderecador.excecao.EnderecadorExcecao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;

public class EnderecoDnecDao implements InterfaceEnderecoDao {
    private static EnderecoDnecDao instance;

    public static synchronized EnderecoDnecDao getInstance() {
        if (instance == null)
            instance = new EnderecoDnecDao();
        return instance;
    }

    public EnderecoBean consultar(String cep, boolean proxy) throws DaoException, ConfiguracaoProxyException {
        if (proxy)
            return buscaCepComProxy(cep);
        return buscaCepSemProxy(cep);
    }

    private EnderecoBean buscaCepSemProxy(String cepPesquisa) throws DaoException {
        URL url = null;
        HttpURLConnection con = null;
        BufferedReader in = null;
        StringBuilder linha = new StringBuilder();
        EnderecoBean enderecoBean = new EnderecoBean();
        ConfiguracaoBean configuracaoBean = ConfiguracaoBean.getInstance();
        try {
            configuracaoBean.carregaVariaveis();
        } catch (EnderecadorExcecao ex) {
            throw new DaoException(ex.getMessage(), ex);
        }
        System.setProperty("http.proxyHost", "");
        try {
            url = new URL(configuracaoBean.getUrlBuscaCep() + "?cep=" + cepPesquisa + "&chave=" + configuracaoBean.getChave());
            con = (HttpURLConnection) url.openConnection();
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                inputLine = inputLine.trim();
                linha.append(inputLine);
            }
            in.close();
            int codigoErro = -1;
            String startTag = "<Versao>";
            String endTag = "</Versao>";
            int start = linha.indexOf(startTag) + startTag.length();
            int end = linha.indexOf(endTag);
            configuracaoBean.setNovaVersao(linha.substring(start, end));
            startTag = "<CodMensagem>";
            endTag = "</CodMensagem>";
            start = linha.indexOf(startTag) + startTag.length();
            end = linha.indexOf(endTag);
            codigoErro = Integer.parseInt(linha.substring(start, end));
            if (codigoErro == 1) {
                startTag = "<Logradouro>";
                endTag = "</Logradouro>";
                start = linha.indexOf(startTag) + startTag.length();
                end = linha.indexOf(endTag);
                enderecoBean.setLogradouro(linha.substring(start, end));
                startTag = "<Bairro>";
                endTag = "</Bairro>";
                start = linha.indexOf(startTag) + startTag.length();
                end = linha.indexOf(endTag);
                enderecoBean.setBairro(linha.substring(start, end));
                startTag = "<Cidade>";
                endTag = "</Cidade>";
                start = linha.indexOf(startTag) + startTag.length();
                end = linha.indexOf(endTag);
                enderecoBean.setCidade(linha.substring(start, end));
                startTag = "<UF>";
                endTag = "</UF>";
                start = linha.indexOf(startTag) + startTag.length();
                end = linha.indexOf(endTag);
                enderecoBean.setUf(linha.substring(start, end));
                enderecoBean.setCep(cepPesquisa);
                if ("".equals(enderecoBean.getCidade().trim()) && "".equals(enderecoBean.getLogradouro().trim()))
                    throw new CepInvalidoException("CEP inválido!");
            } else {
                startTag = "<Mensagem>";
                endTag = "</Mensagem>";
                start = linha.indexOf(startTag) + startTag.length();
                end = linha.indexOf(endTag);
                throw new DaoException(linha.substring(start, end));
            }
        } catch (IOException ex1) {
            throw new DaoException("Erro ao consultar o CEP.", ex1);
        }
        return enderecoBean;
    }

    private EnderecoBean buscaCepComProxy(String cepPesquisa) throws DaoException, ConfiguracaoProxyException {
        ConfiguracaoBean configuracaoBean = ConfiguracaoBean.getInstance();
        EnderecoBean enderecoBean = new EnderecoBean();
        try {
            configuracaoBean.carregaVariaveis();
        } catch (EnderecadorExcecao ex) {
            throw new DaoException(ex.getMessage(), ex);
        }
        UsuarioBean usuarioBean = UsuarioBean.getInstance();
        System.setProperty("proxySet", "true");
        System.setProperty("http.proxyHost", configuracaoBean.getProxy());
        System.setProperty("http.proxyPort", configuracaoBean.getPorta());
        System.setProperty("http.proxyUser", usuarioBean.getUsuario());
        System.setProperty("http.proxyPassword", usuarioBean.getPwd());
        URL url = null;
        try {
            url = new URL(configuracaoBean.getUrlBuscaCep() + "?cep=" + cepPesquisa + "&chave=" + configuracaoBean.getChave());
        } catch (MalformedURLException ex) {
            throw new ConfiguracaoProxyException("Erro ao consultar o CEP.", ex);
        }
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) url.openConnection();
        } catch (IOException ex1) {
            throw new ConfiguracaoProxyException("Erro ao consultar o CEP.", ex1);
        }
        String encodedUserPwd = Base64.getEncoder().encodeToString((configuracaoBean.getDominio() + "\\" + usuarioBean.getUsuario() + ":" + usuarioBean.getPwd()).getBytes());
        con.setRequestProperty("Proxy-Authorization", "Basic " + encodedUserPwd);
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } catch (IOException ex2) {
            throw new ConfiguracaoProxyException("Erro ao consultar o CEP.", ex2);
        }
        StringBuffer linha = new StringBuffer();
        try {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                inputLine = inputLine.trim();
                linha.append(inputLine);
            }
            in.close();
            int codigoErro = -1;
            String startTag = "<Versao>";
            String endTag = "</Versao>";
            int start = linha.indexOf(startTag) + startTag.length();
            int end = linha.indexOf(endTag);
            configuracaoBean.setNovaVersao(linha.substring(start, end));
            startTag = "<CodMensagem>";
            endTag = "</CodMensagem>";
            start = linha.indexOf(startTag) + startTag.length();
            end = linha.indexOf(endTag);
            codigoErro = Integer.parseInt(linha.substring(start, end));
            if (codigoErro == 1) {
                startTag = "<Logradouro>";
                endTag = "</Logradouro>";
                start = linha.indexOf(startTag) + startTag.length();
                end = linha.indexOf(endTag);
                enderecoBean.setLogradouro(linha.substring(start, end));
                startTag = "<Bairro>";
                endTag = "</Bairro>";
                start = linha.indexOf(startTag) + startTag.length();
                end = linha.indexOf(endTag);
                enderecoBean.setBairro(linha.substring(start, end));
                startTag = "<Cidade>";
                endTag = "</Cidade>";
                start = linha.indexOf(startTag) + startTag.length();
                end = linha.indexOf(endTag);
                enderecoBean.setCidade(linha.substring(start, end));
                startTag = "<UF>";
                endTag = "</UF>";
                start = linha.indexOf(startTag) + startTag.length();
                end = linha.indexOf(endTag);
                enderecoBean.setUf(linha.substring(start, end));
                enderecoBean.setCep(cepPesquisa);
                if ("".equals(enderecoBean.getCidade().trim()) && "".equals(enderecoBean.getLogradouro().trim()))
                    throw new CepInvalidoException("CEP inválido!");
            } else {
                startTag = "<Mensagem>";
                endTag = "</Mensagem>";
                start = linha.indexOf(startTag) + startTag.length();
                end = linha.indexOf(endTag);
                throw new DaoException(linha.substring(start, end));
            }
        } catch (IOException ex3) {
            throw new ConfiguracaoProxyException("Erro ao consultar o CEP.", ex3);
        }
        return enderecoBean;
    }
}
