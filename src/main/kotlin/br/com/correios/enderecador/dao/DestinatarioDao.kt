package br.com.correios.enderecador.dao

import br.com.correios.enderecador.conexao.ConexaoBD
import kotlin.Throws
import br.com.correios.enderecador.bean.DestinatarioBean
import br.com.correios.enderecador.conexao.ConnectException
import org.koin.core.annotation.Singleton
import java.sql.PreparedStatement
import java.lang.StringBuilder
import java.sql.SQLException
import java.sql.ResultSet
import java.lang.StringBuffer
import java.sql.Connection
import java.util.ArrayList

@Singleton
class DestinatarioDao(
    private val conexaoBD: ConexaoBD
) {
    private var conexao: Connection? = null

    init {
        try {
            conexao = conexaoBD.recuperaConexao()
        } catch (e: ConnectException) {
            throw DaoException("Não foi possível recuperar a conexão")
        }
    }

    @Throws(DaoException::class)
    fun incluirDestinatario(destinatarioBean: DestinatarioBean) {
        val stmt: PreparedStatement?
        val sql = StringBuilder()
        try {
            conexao!!.autoCommit = true
            sql.append("INSERT INTO TEC_DESTINATARIO ")
            sql.append("(DES_TX_APELIDO , DES_TX_TITULO,DES_TX_NOME, ")
            sql.append("DES_ED_CEP, DES_ED_ENDERECO,DES_ED_NUMERO ,")
            sql.append("DES_ED_COMPLEMENTO, DES_ED_BAIRRO, DES_ED_CIDADE, ")
            sql.append("DES_ED_UF, DES_ED_EMAIL, DES_ED_TELEFONE, ")
            sql.append("DES_ED_FAX, DES_ED_CEP_CAIXA_POSTAL, DES_ED_CAIXA_POSTAL) ")
            sql.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?) ")
            stmt = conexao!!.prepareStatement(sql.toString())
            stmt.setString(1, destinatarioBean.apelido)
            stmt.setString(2, destinatarioBean.titulo)
            stmt.setString(3, destinatarioBean.nome)
            stmt.setString(4, destinatarioBean.cep)
            stmt.setString(5, destinatarioBean.endereco)
            stmt.setString(6, destinatarioBean.numeroEndereco)
            stmt.setString(7, destinatarioBean.complemento)
            stmt.setString(8, destinatarioBean.bairro)
            stmt.setString(9, destinatarioBean.cidade)
            stmt.setString(10, destinatarioBean.uf)
            stmt.setString(11, destinatarioBean.email)
            stmt.setString(12, destinatarioBean.telefone)
            stmt.setString(13, destinatarioBean.fax)
            stmt.setString(14, destinatarioBean.cepCaixaPostal)
            stmt.setString(15, destinatarioBean.caixaPostal)
            val cont = stmt.executeUpdate()
            destinatarioBean.numeroDestinatario = chave
            if (cont == 0) throw DaoException("Não foi possível adicionar dados!")
        } catch (e: SQLException) {
            throw DaoException(e.message, e)
        }
    }

    @get:Throws(DaoException::class)
    private val chave: String
        get() {
            val chave: String
            val stmt: PreparedStatement?
            val sql = StringBuilder()
            val rs: ResultSet?
            sql.append(" CALL IDENTITY() ")
            try {
                stmt = conexao!!.prepareStatement(sql.toString())
                rs = stmt.executeQuery()
                chave = if (rs.next()) {
                    rs.getString(1)
                } else {
                    throw DaoException("Não foi possível recuperar a chave do remetente.")
                }
            } catch (ex: SQLException) {
                throw DaoException(ex.message)
            }
            return chave
        }

    @Throws(DaoException::class)
    fun alterarDestinatario(destinatarioBean: DestinatarioBean) {
        val stmt: PreparedStatement?
        val sql = StringBuilder()
        try {
            conexao!!.autoCommit = true
            sql.append("UPDATE TEC_DESTINATARIO SET ")
            sql.append("DES_TX_APELIDO=? , DES_TX_TITULO=?,DES_TX_NOME=? ,")
            sql.append("DES_ED_CEP=?, DES_ED_ENDERECO=?,DES_ED_NUMERO=? ,")
            sql.append("DES_ED_COMPLEMENTO=?, DES_ED_BAIRRO=?, DES_ED_CIDADE=? ,")
            sql.append("DES_ED_UF=?, DES_ED_EMAIL=?, DES_ED_TELEFONE=?, ")
            sql.append("DES_ED_FAX=?, DES_ED_CEP_CAIXA_POSTAL=?, DES_ED_CAIXA_POSTAL=? ")
            sql.append(" WHERE DES_NU= ? ")
            stmt = conexao!!.prepareStatement(sql.toString())
            stmt.setString(1, destinatarioBean.apelido)
            stmt.setString(2, destinatarioBean.titulo)
            stmt.setString(3, destinatarioBean.nome)
            stmt.setString(4, destinatarioBean.cep)
            stmt.setString(5, destinatarioBean.endereco)
            stmt.setString(6, destinatarioBean.numeroEndereco)
            stmt.setString(7, destinatarioBean.complemento)
            stmt.setString(8, destinatarioBean.bairro)
            stmt.setString(9, destinatarioBean.cidade)
            stmt.setString(10, destinatarioBean.uf)
            stmt.setString(11, destinatarioBean.email)
            stmt.setString(12, destinatarioBean.telefone)
            stmt.setString(13, destinatarioBean.fax)
            stmt.setString(14, destinatarioBean.cepCaixaPostal)
            stmt.setString(15, destinatarioBean.caixaPostal)
            stmt.setString(16, destinatarioBean.numeroDestinatario)
            val cont = stmt.executeUpdate()
            if (cont == 0) throw DaoException("Não foi possível alterar dados!")
        } catch (ex: SQLException) {
            throw DaoException(ex.message, ex)
        }
    }

    @Throws(DaoException::class)
    fun excluirDestinatario(destinatario: String?) {
        val stmt: PreparedStatement?
        val sql = StringBuilder()
        try {
            conexao!!.autoCommit = true
            sql.append("DELETE FROM TEC_DESTINATARIO WHERE ")
            sql.append("DES_NU = ? ")
            stmt = conexao!!.prepareStatement(sql.toString())
            stmt.setString(1, destinatario)
            val cont = stmt.executeUpdate()
            if (cont == 0) throw DaoException("Não foi possível excluir dados!")
        } catch (e: SQLException) {
            throw DaoException(e.message, e)
        }
    }

    @Throws(DaoException::class)
    fun recuperaUltimo(): String {
        val stmt: PreparedStatement?
        val rs: ResultSet?
        val sql = StringBuffer()
        try {
            conexao!!.autoCommit = true
            sql.append("SELECT MAX(DES_NU) AS ULTIMO FROM TEC_DESTINATARIO")
            stmt = conexao!!.prepareStatement(sql.toString())
            rs = stmt.executeQuery()
            if (rs.next()) return rs.getString(1)
        } catch (e: SQLException) {
            throw DaoException(e.message, e)
        }
        return ""
    }

    @Throws(DaoException::class)
    fun recuperaDestinatarioPorGrupo(grupo: String): ArrayList<DestinatarioBean> {
        return recuperaDestinatario("""
            INNER JOIN  TEC_GRUPO_POSSUI_DEST
            ON TEC_GRUPO_POSSUI_DEST.DES_NU =TEC_DESTINATARIO.DES_NU
            AND GRP_NU = $grupo
        """.trimIndent())
    }

    @Throws(DaoException::class)
    fun recuperarDestinatarioForaDoGrupo(grupo: String): ArrayList<DestinatarioBean> {
        return recuperaDestinatario("""
            WHERE DES_NU
            NOT IN
            (SELECT DES_NU FROM TEC_GRUPO_POSSUI_DEST WHERE GRP_NU=$grupo)
        """.trimIndent())
    }

    @Throws(DaoException::class)
    fun recuperaDestinatario(filtro: String): ArrayList<DestinatarioBean> {
        val stmt: PreparedStatement?
        val rs: ResultSet?
        val dados = ArrayList<DestinatarioBean>()
        val sql = StringBuilder()
        var destinatarioBean: DestinatarioBean?
        try {
            conexao!!.autoCommit = true
            sql.append(" SELECT  DES_NU, DES_TX_APELIDO,DES_TX_TITULO,")
            sql.append("         DES_TX_NOME,DES_ED_CEP,DES_ED_ENDERECO,")
            sql.append("         DES_ED_NUMERO,DES_ED_COMPLEMENTO,DES_ED_BAIRRO,")
            sql.append("         DES_ED_CIDADE,DES_ED_UF,DES_ED_EMAIL,DES_ED_TELEFONE,")
            sql.append("         DES_ED_FAX,DES_ED_CEP_CAIXA_POSTAL,DES_ED_CAIXA_POSTAL")
            sql.append(" FROM  TEC_DESTINATARIO ")
            if (filtro != "") sql.append(filtro)
            sql.append(" ORDER BY UPPER(DES_TX_NOME) ASC ")
            stmt = conexao!!.prepareStatement(sql.toString())
            rs = stmt.executeQuery()
            while (rs.next()) {
                destinatarioBean = DestinatarioBean()
                destinatarioBean.numeroDestinatario = rs.getString(1)
                destinatarioBean.apelido = rs.getString(2)
                destinatarioBean.titulo = rs.getString(3)
                destinatarioBean.nome = rs.getString(4)
                destinatarioBean.cep = rs.getString(5)
                destinatarioBean.endereco = rs.getString(6)
                destinatarioBean.numeroEndereco = rs.getString(7)
                destinatarioBean.complemento = rs.getString(8)
                destinatarioBean.bairro = rs.getString(9)
                destinatarioBean.cidade = rs.getString(10)
                destinatarioBean.uf = rs.getString(11)
                destinatarioBean.email = rs.getString(12)
                destinatarioBean.telefone = rs.getString(13)
                destinatarioBean.fax = rs.getString(14)
                destinatarioBean.cepCaixaPostal = rs.getString(15)
                destinatarioBean.caixaPostal = rs.getString(16)
                dados.add(destinatarioBean)
            }
        } catch (e: SQLException) {
            throw DaoException(e.message, e)
        }
        return dados
    }
}
