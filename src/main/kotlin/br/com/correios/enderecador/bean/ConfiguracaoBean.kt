package br.com.correios.enderecador.bean

import br.com.correios.enderecador.util.GerenciadorPropriedades
import java.util.Locale
import br.com.correios.enderecador.dao.*
import kotlin.Throws
import br.com.correios.enderecador.excecao.EnderecadorExcecao
import org.koin.core.annotation.Singleton
import java.io.File

@Singleton
class ConfiguracaoBean {
    var caminhoImagem = "/imagens/"
    var caminhoImagem2d = File("").absolutePath
    var versao = "2.4.4"
    var novaVersao = ""
    var caminhoRelatorio = "relatorios/"
    var chave = ""
    var proxy = ""
    var porta = ""
    var dominio = ""
    var urlBanco = ""
    var senhaBanco = ""
    var driverBanco = "org.hsqldb.jdbcDriver"
    var usuarioBanco = "SA"
    var paginaPesquisaCep = "http://www.buscacep.correios.com.br/"
    var paginaFaleConosco = "http://www2.correios.com.br/sistemas/falecomoscorreios/"
    var banco: String? = null

    init {
        val propriedades = GerenciadorPropriedades("/enderecador.properties")
        banco = propriedades.getProperties("banco.cep")
            .trim { it <= ' ' }
            .uppercase(Locale.getDefault())

        urlBanco = "jdbc:hsqldb:${System.getProperty("user.dir")}${System.getProperty("file.separator")}lib${
            System.getProperty("file.separator")
        }db${System.getProperty("file.separator")}dbenderecadorect"
    }

    @Throws(EnderecadorExcecao::class)
    fun carregaVariaveis() {
        /*
        try {
            val configuracaoDao = ConfiguracaoDao.instance
            val dadosConfig = configuracaoDao!!.recuperaConfiguracao("")
            if (dadosConfig.size != 0) {
                proxy = if (dadosConfig[0] == null) "" else dadosConfig[0]!!
                porta = if (dadosConfig[1] == null) "" else dadosConfig[1]!!
                chave = if (dadosConfig[2] == null) "" else dadosConfig[2]!!
                dominio = if (dadosConfig[3] == null) "" else dadosConfig[3]!!
            }
        } catch (ex: DaoException) {
            throw EnderecadorExcecao(ex.message, ex)
        }
         */
    }

    companion object {
        private var configuracaoBean: ConfiguracaoBean? = null
        @JvmStatic
        val instance: ConfiguracaoBean?
            get() {
                if (configuracaoBean == null) {
                    configuracaoBean = ConfiguracaoBean()
                }
                return configuracaoBean
            }
    }
}
