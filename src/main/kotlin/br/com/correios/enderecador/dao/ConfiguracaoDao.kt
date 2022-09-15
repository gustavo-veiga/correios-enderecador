package br.com.correios.enderecador.dao

import br.com.correios.enderecador.conexao.ConexaoBD
import br.com.correios.enderecador.bean.ConfiguracaoBean
import br.com.correios.enderecador.exception.ConnectException
import br.com.correios.enderecador.exception.DaoException
import br.com.correios.enderecador.exception.EnderecadorExcecao
import org.koin.core.annotation.Singleton
import java.sql.SQLException
import java.lang.Exception
import java.sql.Connection

@Singleton
class ConfiguracaoDao(conexaoBD: ConexaoBD) {
    private val conexao: Connection

    init {
        try {
            conexao = conexaoBD.recuperaConexao()
        } catch (e: ConnectException) {
            throw DaoException("Não foi possível recuperar a conexão")
        }
    }

    fun incluirConfiguracao(configuracaoBean: ConfiguracaoBean) {
        try {
            val stmt = conexao.prepareStatement("""
                INSERT INTO TEC_CONFIGURACAO (
                    CFG_CAMPO1, CFG_CAMPO2, CFG_CAMPO3,
                    CFG_CAMPO4, CFG_CAMPO5, CFG_CAMPO6, 
                    CFG_CAMPO7, CFG_CAMPO8, CFG_CAMPO9, 
                    CFG_CAMPO10, CFG_CAMPO11,CFG_CAMPO12,
                    CFG_CAMPO13, CFG_CAMPO14, CFG_CAMPO15, 
                    CFG_CAMPO16, CFG_CAMPO17)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?, ?, ?)
            """.trimIndent())
            stmt.setString(1, configuracaoBean.proxy)
            stmt.setString(2, configuracaoBean.porta)
            stmt.setString(3, configuracaoBean.chave)
            stmt.setString(4, configuracaoBean.dominio)
            stmt.setString(5, null)
            stmt.setString(6, null)
            stmt.setString(7, null)
            stmt.setString(8, null)
            stmt.setString(9, null)
            stmt.setString(10, null)
            stmt.setString(11, null)
            stmt.setString(12, null)
            stmt.setString(13, null)
            stmt.setString(14, null)
            stmt.setString(15, null)
            stmt.setString(16, null)
            stmt.setString(17, null)
            val cont = stmt.executeUpdate()
            if (cont == 0) {
                throw EnderecadorExcecao("Não foi possível adicionar dados!")
            }
        } catch (e: Exception) {
            throw DaoException(e.message)
        }
    }

    fun alterarConfiguracao(configuracaoBean: ConfiguracaoBean) {
        try {
            val stmt = conexao.prepareStatement("""
                UPDATE TEC_CONFIGURACAO SET
                    CFG_CAMPO1=?, CFG_CAMPO2=?,CFG_CAMPO3=?,
                    CFG_CAMPO4=?, CFG_CAMPO5=?,CFG_CAMPO6=?,
                    CFG_CAMPO7=?, CFG_CAMPO8=?, CFG_CAMPO9=?,
                    CFG_CAMPO10=?, CFG_CAMPO11=?, CFG_CAMPO12=?,
                    CFG_CAMPO13=?, CFG_CAMPO14=?, CFG_CAMPO15=? ,
                    CFG_CAMPO16=?, CFG_CAMPO17=?
            """.trimIndent())
            stmt.setString(1, configuracaoBean.proxy)
            stmt.setString(2, configuracaoBean.porta)
            stmt.setString(3, configuracaoBean.chave)
            stmt.setString(4, configuracaoBean.dominio)
            stmt.setString(5, null)
            stmt.setString(6, null)
            stmt.setString(7, null)
            stmt.setString(8, null)
            stmt.setString(9, null)
            stmt.setString(10, null)
            stmt.setString(11, null)
            stmt.setString(12, null)
            stmt.setString(13, null)
            stmt.setString(14, null)
            stmt.setString(15, null)
            stmt.setString(16, null)
            stmt.setString(17, null)
            val cont = stmt.executeUpdate()
            if (cont == 0) {
                throw DaoException("Não foi possível alterar dados!")
            }
        } catch (ex: SQLException) {
            throw DaoException(ex.message)
        }
    }

    fun excluirConfiguracao(configurcao: String) {
        try {
            val stmt = conexao.prepareStatement("""
                DELETE FROM TEC_CONFIGURACAO WHERE ?
            """.trimIndent())
            stmt.setString(1, configurcao)
            val cont = stmt.executeUpdate()
            if (cont == 0) throw DaoException("Não foi possível excluir dados!")
        } catch (e: SQLException) {
            throw DaoException(e.message)
        }
    }
}
