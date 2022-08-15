package br.com.correios.enderecador.dao

import kotlin.Throws
import br.com.correios.enderecador.excecao.ConfiguracaoProxyException
import br.com.correios.enderecador.bean.EnderecoBean
import java.io.BufferedReader
import java.lang.StringBuilder
import br.com.correios.enderecador.bean.ConfiguracaoBean
import br.com.correios.enderecador.excecao.EnderecadorExcecao
import java.io.IOException
import br.com.correios.enderecador.bean.UsuarioBean
import org.apache.commons.lang.StringEscapeUtils
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import kotlin.jvm.Synchronized

class EnderecoDnecDao : InterfaceEnderecoDao {
    @Throws(DaoException::class, ConfiguracaoProxyException::class)
    override fun consultar(cep: String, proxy: Boolean): EnderecoBean {
        return if (proxy) buscaCepComProxy(cep) else buscaCepSemProxy(cep)
    }

    @Throws(DaoException::class)
    private fun buscaCepSemProxy(cepPesquisa: String?): EnderecoBean {
        val url: URL?
        val con: HttpURLConnection?
        val reader: BufferedReader?
        val linha = StringBuilder()
        val enderecoBean = EnderecoBean()
        val configuracaoBean = ConfiguracaoBean.instance
        try {
            configuracaoBean!!.carregaVariaveis()
        } catch (ex: EnderecadorExcecao) {
            throw DaoException(ex.message, ex)
        }
        System.setProperty("http.proxyHost", "")
        try {
            url = URL(configuracaoBean.urlBuscaCep + "?cep=" + cepPesquisa + "&chave=" + configuracaoBean.chave)
            con = url.openConnection() as HttpURLConnection
            reader = BufferedReader(InputStreamReader(con.inputStream))
            var inputLine: String
            while (reader.readLine().also { inputLine = it } != null) {
                inputLine = StringEscapeUtils.unescapeHtml(inputLine.trim { it <= ' ' })
                linha.append(inputLine)
            }
            reader.close()
            val codigoErro: Int
            var startTag = "<Versao>"
            var endTag = "</Versao>"
            var start = linha.indexOf(startTag) + startTag.length
            var end = linha.indexOf(endTag)
            configuracaoBean.novaVersao = linha.substring(start, end)
            startTag = "<CodMensagem>"
            endTag = "</CodMensagem>"
            start = linha.indexOf(startTag) + startTag.length
            end = linha.indexOf(endTag)
            codigoErro = linha.substring(start, end).toInt()
            if (codigoErro == 1) {
                startTag = "<Logradouro>"
                endTag = "</Logradouro>"
                start = linha.indexOf(startTag) + startTag.length
                end = linha.indexOf(endTag)
                enderecoBean.logradouro = linha.substring(start, end)
                startTag = "<Bairro>"
                endTag = "</Bairro>"
                start = linha.indexOf(startTag) + startTag.length
                end = linha.indexOf(endTag)
                enderecoBean.bairro = linha.substring(start, end)
                startTag = "<Cidade>"
                endTag = "</Cidade>"
                start = linha.indexOf(startTag) + startTag.length
                end = linha.indexOf(endTag)
                enderecoBean.cidade = linha.substring(start, end)
                startTag = "<UF>"
                endTag = "</UF>"
                start = linha.indexOf(startTag) + startTag.length
                end = linha.indexOf(endTag)
                enderecoBean.uf = linha.substring(start, end)
                enderecoBean.cep = cepPesquisa
                if (enderecoBean.cidade.isNullOrBlank() && enderecoBean.logradouro.isNullOrBlank()) {
                    throw CepInvalidoException("CEP inválido!")
                }
            } else {
                startTag = "<Mensagem>"
                endTag = "</Mensagem>"
                start = linha.indexOf(startTag) + startTag.length
                end = linha.indexOf(endTag)
                throw DaoException(linha.substring(start, end))
            }
        } catch (ex1: IOException) {
            throw DaoException("Erro ao consultar o CEP.", ex1)
        }
        return enderecoBean
    }

    @Throws(DaoException::class, ConfiguracaoProxyException::class)
    private fun buscaCepComProxy(cepPesquisa: String?): EnderecoBean {
        val configuracaoBean = ConfiguracaoBean.instance
        val enderecoBean = EnderecoBean()
        try {
            configuracaoBean!!.carregaVariaveis()
        } catch (ex: EnderecadorExcecao) {
            throw DaoException(ex.message, ex)
        }
        val usuarioBean = UsuarioBean.instance
        System.setProperty("proxySet", "true")
        System.setProperty("http.proxyHost", configuracaoBean.proxy)
        System.setProperty("http.proxyPort", configuracaoBean.porta)
        System.setProperty("http.proxyUser", usuarioBean!!.usuario)
        System.setProperty("http.proxyPassword", usuarioBean.pwd)
        val url: URL?
        url = try {
            URL(configuracaoBean.urlBuscaCep + "?cep=" + cepPesquisa + "&chave=" + configuracaoBean.chave)
        } catch (ex: MalformedURLException) {
            throw ConfiguracaoProxyException("Erro ao consultar o CEP.", ex)
        }
        val con: HttpURLConnection?
        con = try {
            url!!.openConnection() as HttpURLConnection
        } catch (ex1: IOException) {
            throw ConfiguracaoProxyException("Erro ao consultar o CEP.", ex1)
        }
        val encodedUserPwd = Base64.getEncoder()
            .encodeToString((configuracaoBean.dominio + "\\" + usuarioBean.usuario + ":" + usuarioBean.pwd).toByteArray())
        con!!.setRequestProperty("Proxy-Authorization", "Basic $encodedUserPwd")
        val reader: BufferedReader? = try {
            BufferedReader(InputStreamReader(con.inputStream))
        } catch (ex2: IOException) {
            throw ConfiguracaoProxyException("Erro ao consultar o CEP.", ex2)
        }
        val linha = StringBuilder()
        try {
            var inputLine: String
            while (reader!!.readLine().also { inputLine = it } != null) {
                inputLine = StringEscapeUtils.unescapeHtml(inputLine.trim { it <= ' ' })
                linha.append(inputLine)
            }
            reader.close()
            val codigoErro: Int
            var startTag = "<Versao>"
            var endTag = "</Versao>"
            var start = linha.indexOf(startTag) + startTag.length
            var end = linha.indexOf(endTag)
            configuracaoBean.novaVersao = linha.substring(start, end)
            startTag = "<CodMensagem>"
            endTag = "</CodMensagem>"
            start = linha.indexOf(startTag) + startTag.length
            end = linha.indexOf(endTag)
            codigoErro = linha.substring(start, end).toInt()
            if (codigoErro == 1) {
                startTag = "<Logradouro>"
                endTag = "</Logradouro>"
                start = linha.indexOf(startTag) + startTag.length
                end = linha.indexOf(endTag)
                enderecoBean.logradouro = linha.substring(start, end)
                startTag = "<Bairro>"
                endTag = "</Bairro>"
                start = linha.indexOf(startTag) + startTag.length
                end = linha.indexOf(endTag)
                enderecoBean.bairro = linha.substring(start, end)
                startTag = "<Cidade>"
                endTag = "</Cidade>"
                start = linha.indexOf(startTag) + startTag.length
                end = linha.indexOf(endTag)
                enderecoBean.cidade = linha.substring(start, end)
                startTag = "<UF>"
                endTag = "</UF>"
                start = linha.indexOf(startTag) + startTag.length
                end = linha.indexOf(endTag)
                enderecoBean.uf = linha.substring(start, end)
                enderecoBean.cep = cepPesquisa
                if (enderecoBean.cidade.isNullOrBlank() && enderecoBean.logradouro.isNullOrBlank()) {
                    throw CepInvalidoException("CEP inválido!")
                }
            } else {
                startTag = "<Mensagem>"
                endTag = "</Mensagem>"
                start = linha.indexOf(startTag) + startTag.length
                end = linha.indexOf(endTag)
                throw DaoException(linha.substring(start, end))
            }
        } catch (ex3: IOException) {
            throw ConfiguracaoProxyException("Erro ao consultar o CEP.", ex3)
        }
        return enderecoBean
    }

    companion object {
        @get:Synchronized
        var instance: EnderecoDnecDao? = null
            get() {
                if (field == null) field = EnderecoDnecDao()
                return field
            }
            private set
    }
}
