package br.com.correios.enderecador.dao

import br.com.correios.enderecador.conexao.ConexaoBD
import kotlin.Throws
import br.com.correios.enderecador.bean.GrupoBean
import br.com.correios.enderecador.exception.ConnectException
import br.com.correios.enderecador.exception.DaoException
import org.koin.core.annotation.Singleton
import java.sql.SQLException
import java.sql.Connection

@Singleton
class GrupoDao(conexaoBD: ConexaoBD) {
    private val conexao: Connection

    init {
        try {
            conexao = conexaoBD.recuperaConexao()
        } catch (e: ConnectException) {
            throw DaoException("Não foi possível recuperar a conexão")
        }
    }

    @Throws(DaoException::class)
    fun incluirGrupo(grupoBean: GrupoBean) {
        try {
            val stmt = conexao.prepareStatement("""
                INSERT INTO TEC_GRUPO (GRP_TX_DESCRICAO) VALUES (?)
            """.trimIndent())
            stmt.setString(1, grupoBean.descricaoGrupo)
            val cont = stmt.executeUpdate()
            if (cont == 0) {
                throw DaoException("Não foi possível adicionar dados!")
            }
        } catch (e: SQLException) {
            throw DaoException(e.message, e)
        }
    }

    @Throws(DaoException::class)
    fun alterarGrupo(grupoBean: GrupoBean) {
        try {
            val stmt = conexao.prepareStatement("""
                UPDATE TEC_GRUPO SET
                    GRP_TX_DESCRICAO = ?
                WHERE GRP_NU = ?
            """.trimIndent())
            stmt.setString(1, grupoBean.descricaoGrupo)
            stmt.setString(2, grupoBean.numeroGrupo)
            val cont = stmt.executeUpdate()
            if (cont == 0) throw DaoException("Não foi possível alterar dados!")
        } catch (e: SQLException) {
            throw DaoException(e.message, e)
        }
    }

    @Throws(DaoException::class)
    fun excluirGrupo(grupo: String) {
        try {
            val stmt = conexao.prepareStatement("""
                DELETE FROM TEC_GRUPO WHERE GRP_NU = ?
            """.trimIndent())
            stmt.setString(1, grupo)
            val cont = stmt.executeUpdate()
            if (cont == 0) throw DaoException("Não foi possível excluir dados!")
        } catch (e: SQLException) {
            throw DaoException(e.message, e)
        }
    }

    @Throws(DaoException::class)
    fun recuperaGrupo(filtro: String = ""): List<GrupoBean> {
        val dados = mutableListOf<GrupoBean>()
        try {
            val stmt = conexao.prepareStatement("""
                SELECT * FROM TEC_GRUPO
                $filtro
                ORDER BY UPPER(GRP_TX_DESCRICAO) ASC
            """.trimIndent())
            val rs = stmt.executeQuery()
            while (rs.next()) {
                dados.add(GrupoBean(
                    numeroGrupo = rs.getString(1),
                    descricaoGrupo = rs.getString(2)
                ))
            }
        } catch (e: SQLException) {
            throw DaoException(e.message, e)
        }
        return dados
    }

    @Throws(DaoException::class)
    fun recuperaUltimoGrupo(): String {
        var ultimoNumero = ""
        try {
            val stmt = conexao.prepareStatement("""
                SELECT MAX(GRP_NU) AS ULTIMO FROM TEC_GRUPO
            """.trimIndent())
            val rs = stmt.executeQuery()
            while (rs.next()) {
                ultimoNumero = rs.getString(1)
            }
        } catch (e: SQLException) {
            throw DaoException(e.message)
        }
        return ultimoNumero
    }
}
