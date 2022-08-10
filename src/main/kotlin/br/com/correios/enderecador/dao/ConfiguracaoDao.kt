package br.com.correios.enderecador.dao

import br.com.correios.enderecador.conexao.ConexaoBD
import kotlin.Throws
import br.com.correios.enderecador.bean.ConfiguracaoBean
import br.com.correios.enderecador.conexao.ConnectException
import java.sql.PreparedStatement
import java.lang.StringBuilder
import br.com.correios.enderecador.excecao.EnderecadorExcecao
import java.sql.SQLException
import java.sql.ResultSet
import java.lang.Exception
import java.sql.Connection
import java.util.*

class ConfiguracaoDao {
    private var conexao: Connection? = null

    init {
        try {
            conexao = ConexaoBD.instance!!.recuperaConexao()
        } catch (e: ConnectException) {
            throw DaoException("Não foi possível recuperar a conexão")
        }
    }

    @Throws(DaoException::class)
    fun incluirConfiguracao(configuracaoBean: ConfiguracaoBean) {
        val stmt: PreparedStatement?
        val sql = StringBuilder()
        try {
            conexao!!.autoCommit = true
            sql.append(" INSERT INTO TEC_CONFIGURACAO (CFG_CAMPO1, CFG_CAMPO2, CFG_CAMPO3,")
            sql.append("         CFG_CAMPO4, CFG_CAMPO5, CFG_CAMPO6,")
            sql.append("         CFG_CAMPO6, CFG_CAMPO7, CFG_CAMPO8,")
            sql.append("         CFG_CAMPO9, CFG_CAMPO10, CFG_CAMPO11, CFG_CAMPO12,")
            sql.append("         CFG_CAMPO13, CFG_CAMPO14, CFG_CAMPO15, CFG_CAMPO16, CFG_CAMPO17)")
            sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?) ")
            stmt = conexao!!.prepareStatement(sql.toString())
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
            if (cont == 0) throw EnderecadorExcecao("Não foi possível adicionar dados!")
        } catch (e: Exception) {
            throw DaoException(e.message)
        }
    }

    @Throws(DaoException::class)
    fun alterarConfiguracao(configuracaoBean: ConfiguracaoBean) {
        val stmt: PreparedStatement?
        val sql = StringBuilder()
        try {
            conexao!!.autoCommit = true
            sql.append("UPDATE TEC_CONFIGURACAO SET ")
            sql.append(" CFG_CAMPO1=? , CFG_CAMPO2=?,CFG_CAMPO3=? ,")
            sql.append(" CFG_CAMPO4=?, CFG_CAMPO5=?,CFG_CAMPO6=? ,")
            sql.append(" CFG_CAMPO7=?, CFG_CAMPO8=?, CFG_CAMPO9=? ,")
            sql.append(" CFG_CAMPO10=?, CFG_CAMPO11=?, CFG_CAMPO12=?, ")
            sql.append(" CFG_CAMPO13=?, CFG_CAMPO14=?, CFG_CAMPO15=? ,")
            sql.append(" CFG_CAMPO16=?, CFG_CAMPO17=? ")
            stmt = conexao!!.prepareStatement(sql.toString())
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
            if (cont == 0) throw DaoException("Não foi possível alterar dados!")
        } catch (ex: SQLException) {
            throw DaoException(ex.message)
        }
    }

    @Throws(DaoException::class)
    fun excluirConfiguracao(configurcao: String?) {
        val stmt: PreparedStatement?
        val sql = StringBuilder()
        try {
            conexao!!.autoCommit = true
            sql.append("DELETE FROM TEC_CONFIGURACAO WHERE ")
            sql.append("?")
            stmt = conexao!!.prepareStatement(sql.toString())
            stmt.setString(1, configurcao)
            val cont = stmt.executeUpdate()
            if (cont == 0) throw DaoException("Não foi possível excluir dados!")
        } catch (e: SQLException) {
            throw DaoException(e.message)
        }
    }

    @Throws(DaoException::class)
    fun recuperaConfiguracao(filtro: String): Vector<String> {
        val stmt: PreparedStatement?
        val rs: ResultSet?
        val dados = Vector<String>()
        val sql = StringBuilder()
        try {
            sql.append(" SELECT  CFG_CAMPO1, CFG_CAMPO2, CFG_CAMPO3,")
            sql.append("         CFG_CAMPO4, CFG_CAMPO5, CFG_CAMPO6,")
            sql.append("         CFG_CAMPO6, CFG_CAMPO7, CFG_CAMPO8,")
            sql.append("         CFG_CAMPO9,CFG_CAMPO10,CFG_CAMPO11,CFG_CAMPO12,")
            sql.append("         CFG_CAMPO13,CFG_CAMPO14,CFG_CAMPO15,CFG_CAMPO16,CFG_CAMPO17")
            sql.append(" FROM  TEC_CONFIGURACAO")
            if (filtro != "") sql.append(filtro)
            stmt = conexao!!.prepareStatement(sql.toString())
            rs = stmt.executeQuery()
            while (rs.next()) {
                dados.add(rs.getString(1))
                dados.add(rs.getString(2))
                dados.add(rs.getString(3))
                dados.add(rs.getString(4))
            }
        } catch (e: SQLException) {
            throw DaoException(e.message)
        }
        return dados
    }

    companion object {
        private var configuracaoDao: ConfiguracaoDao? = null

        @JvmStatic
        @get:Throws(DaoException::class)
        val instance: ConfiguracaoDao?
            get() {
                if (configuracaoDao == null) {
                    configuracaoDao = ConfiguracaoDao()
                }
                return configuracaoDao
            }
    }
}
