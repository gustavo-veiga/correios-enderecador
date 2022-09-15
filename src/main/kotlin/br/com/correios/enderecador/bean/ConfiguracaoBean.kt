package br.com.correios.enderecador.bean

import java.io.File

object ConfiguracaoBean {
    var caminhoImagem = "/imagens/"
    var caminhoImagem2d = File("").absolutePath
    var novaVersao = ""
    var versao = "2.4.4"
    var caminhoRelatorio = "relatorios/"
    var chave = ""
    var proxy = ""
    var porta = ""
    var dominio = ""
    var senhaBanco = ""
    var driverBanco = "org.hsqldb.jdbcDriver"
    var usuarioBanco = "SA"
    var paginaPesquisaCep = "http://www.buscacep.correios.com.br/"
    var paginaFaleConosco = "http://www2.correios.com.br/sistemas/falecomoscorreios/"
    val banco = "DNEC"

    val userDir = System.getProperty("user.dir")
    val fileSeparator = System.getProperty("file.separator")

    val urlBanco = "jdbc:hsqldb:${userDir}${fileSeparator}lib${fileSeparator}db${fileSeparator}dbenderecadorect"

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
}
