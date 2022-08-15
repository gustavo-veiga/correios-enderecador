package br.com.correios.enderecador.dao

import br.com.correios.enderecador.conexao.ConexaoBD
import kotlin.Throws
import br.com.correios.enderecador.bean.GrupoBean
import br.com.correios.enderecador.conexao.ConnectException
import java.sql.PreparedStatement
import java.lang.StringBuilder
import java.sql.SQLException
import java.sql.ResultSet
import java.sql.Connection
import java.util.ArrayList

class GrupoDao {
    private var conexao: Connection? = null

    init {
        try {
            conexao = ConexaoBD.instance!!.recuperaConexao()
        } catch (e: ConnectException) {
            throw DaoException("Não foi possível recuperar a conexão")
        }
    }

    @Throws(DaoException::class)
    fun incluirGrupo(grupoBean: GrupoBean) {
        val stmt: PreparedStatement?
        val sql = StringBuilder()
        try {
            conexao!!.autoCommit = true
            sql.append("INSERT INTO TEC_GRUPO")
            sql.append("(GRP_TX_DESCRICAO) ")
            sql.append("VALUES (?) ")
            stmt = conexao!!.prepareStatement(sql.toString())
            stmt.setString(1, grupoBean.descricaoGrupo)
            val cont = stmt.executeUpdate()
            if (cont == 0) throw DaoException("Não foi possível adicionar dados!")
        } catch (e: SQLException) {
            throw DaoException(e.message, e)
        }
    }

    @Throws(DaoException::class)
    fun alterarGrupo(grupoBean: GrupoBean) {
        val stmt: PreparedStatement?
        val sql = StringBuilder()
        try {
            conexao!!.autoCommit = true
            sql.append("UPDATE TEC_GRUPO SET ")
            sql.append(" GRP_TX_DESCRICAO=? ")
            sql.append(" WHERE GRP_NU= ?  ")
            stmt = conexao!!.prepareStatement(sql.toString())
            stmt.setString(1, grupoBean.descricaoGrupo)
            stmt.setString(2, grupoBean.numeroGrupo)
            val cont = stmt.executeUpdate()
            if (cont == 0) throw DaoException("Não foi possível alterar dados!")
        } catch (e: SQLException) {
            throw DaoException(e.message, e)
        }
    }

    @Throws(DaoException::class)
    fun excluirGrupo(grupo: String?) {
        val stmt: PreparedStatement?
        val sql = StringBuilder()
        try {
            conexao!!.autoCommit = true
            sql.append("DELETE FROM TEC_GRUPO WHERE ")
            sql.append("GRP_NU = ? ")
            stmt = conexao!!.prepareStatement(sql.toString())
            stmt.setString(1, grupo)
            val cont = stmt.executeUpdate()
            if (cont == 0) throw DaoException("Não foi possível excluir dados!")
        } catch (e: SQLException) {
            throw DaoException(e.message, e)
        }
    }

    @Throws(DaoException::class)
    fun recuperaGrupo(filtro: String): ArrayList<GrupoBean> {
        val rs: ResultSet?
        val stmt: PreparedStatement?
        val dados = ArrayList<GrupoBean>()
        val sql = StringBuilder()
        try {
            conexao!!.autoCommit = true
            sql.append("SELECT * FROM TEC_GRUPO ")
            if ("" != filtro) sql.append(filtro)
            sql.append("ORDER BY UPPER(GRP_TX_DESCRICAO) ASC ")
            stmt = conexao!!.prepareStatement(sql.toString())
            rs = stmt.executeQuery()
            while (rs.next()) {
                val grupoBean = GrupoBean()
                grupoBean.numeroGrupo = rs.getString(1)
                grupoBean.descricaoGrupo = rs.getString(2)
                dados.add(grupoBean)
            }
        } catch (e: SQLException) {
            throw DaoException(e.message, e)
        }
        return dados
    }

    @Throws(DaoException::class)
    fun recuperaUltimoGrupo(): String {
        val rs: ResultSet?
        val stmt: PreparedStatement?
        val sql = StringBuilder()
        var ultimoNumero = ""
        try {
            conexao!!.autoCommit = true
            sql.append("SELECT MAX(GRP_NU) AS ULTIMO FROM TEC_GRUPO ")
            stmt = conexao!!.prepareStatement(sql.toString())
            rs = stmt.executeQuery()
            while (rs.next()) ultimoNumero = rs.getString(1)
        } catch (e: SQLException) {
            throw DaoException(e.message)
        }
        return ultimoNumero
    }

    companion object {
        private var grupoDao: GrupoDao? = null

        @JvmStatic
        @get:Throws(DaoException::class)
        val instance: GrupoDao?
            get() {
                if (grupoDao == null) grupoDao = GrupoDao()
                return grupoDao
            }
    }
}
