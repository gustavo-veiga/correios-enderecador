package br.com.correios.enderecador.dao

import br.com.correios.enderecador.conexao.ConexaoBD
import kotlin.Throws
import br.com.correios.enderecador.bean.RemetenteBean
import br.com.correios.enderecador.conexao.ConnectException
import org.koin.core.annotation.Singleton
import java.sql.PreparedStatement
import java.lang.StringBuilder
import java.sql.SQLException
import java.sql.ResultSet
import java.sql.Connection
import java.util.ArrayList

@Singleton
class RemetenteDao(
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
    fun incluirRemetente(remetenteBean: RemetenteBean) {
        val stmt: PreparedStatement?
        val sql = StringBuilder()
        try {
            conexao!!.autoCommit = true
            sql.append("INSERT INTO TEC_REMETENTE ")
            sql.append("(REM_TX_APELIDO , REM_TX_TITULO,REM_TX_NOME, ")
            sql.append("REM_ED_CEP, REM_ED_ENDERECO,REM_ED_NUMERO, ")
            sql.append("REM_ED_COMPLEMENTO, REM_ED_BAIRRO, REM_ED_CIDADE, ")
            sql.append("REM_ED_UF, REM_ED_EMAIL, REM_ED_TELEFONE, ")
            sql.append("REM_ED_FAX, REM_ED_CEP_CAIXA_POSTAL, REM_ED_CAIXA_POSTAL) ")
            sql.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?) ")
            stmt = conexao!!.prepareStatement(sql.toString())
            stmt.setString(1, remetenteBean.apelido)
            stmt.setString(2, remetenteBean.titulo)
            stmt.setString(3, remetenteBean.nome)
            stmt.setString(4, remetenteBean.cep)
            stmt.setString(5, remetenteBean.endereco)
            stmt.setString(6, remetenteBean.numeroEndereco)
            stmt.setString(7, remetenteBean.complemento)
            stmt.setString(8, remetenteBean.bairro)
            stmt.setString(9, remetenteBean.cidade)
            stmt.setString(10, remetenteBean.uf)
            stmt.setString(11, remetenteBean.email)
            stmt.setString(12, remetenteBean.telefone)
            stmt.setString(13, remetenteBean.fax)
            stmt.setString(14, remetenteBean.cepCaixaPostal)
            stmt.setString(15, remetenteBean.caixaPostal)
            val cont = stmt.executeUpdate()
            if (cont == 0) throw DaoException("Não foi possível adicionar dados!")
            remetenteBean.numeroRemetente = chave
        } catch (e: SQLException) {
            throw DaoException(e.message)
        }
    }

    @get:Throws(DaoException::class)
    private val chave: String?
        get() {
            val chave: String?
            val rs: ResultSet?
            val sql = StringBuilder()
            val stmt: PreparedStatement?
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
    fun alterarRemetente(remetenteBean: RemetenteBean) {
        val stmt: PreparedStatement
        val sql = StringBuilder()
        try {
            conexao!!.autoCommit = true
            sql.append("UPDATE TEC_REMETENTE SET ")
            sql.append(" REM_TX_APELIDO=? , REM_TX_TITULO=?,REM_TX_NOME=? ,")
            sql.append(" REM_ED_CEP=?, REM_ED_ENDERECO=?,REM_ED_NUMERO=? ,")
            sql.append(" REM_ED_COMPLEMENTO=?, REM_ED_BAIRRO=?, REM_ED_CIDADE=? ,")
            sql.append(" REM_ED_UF=?, REM_ED_EMAIL=?, REM_ED_TELEFONE=?, ")
            sql.append(" REM_ED_FAX=?, REM_ED_CEP_CAIXA_POSTAL=?, REM_ED_CAIXA_POSTAL=? ")
            sql.append(" WHERE REM_NU= ?  ")
            stmt = conexao!!.prepareStatement(sql.toString())
            stmt.setString(1, remetenteBean.apelido)
            stmt.setString(2, remetenteBean.titulo)
            stmt.setString(3, remetenteBean.nome)
            stmt.setString(4, remetenteBean.cep)
            stmt.setString(5, remetenteBean.endereco)
            stmt.setString(6, remetenteBean.numeroEndereco)
            stmt.setString(7, remetenteBean.complemento)
            stmt.setString(8, remetenteBean.bairro)
            stmt.setString(9, remetenteBean.cidade)
            stmt.setString(10, remetenteBean.uf)
            stmt.setString(11, remetenteBean.email)
            stmt.setString(12, remetenteBean.telefone)
            stmt.setString(13, remetenteBean.fax)
            stmt.setString(14, remetenteBean.cepCaixaPostal)
            stmt.setString(15, remetenteBean.caixaPostal)
            stmt.setString(16, remetenteBean.numeroRemetente)
            val cont = stmt.executeUpdate()
            if (cont == 0) throw DaoException("Não foi possível alterar dados!")
        } catch (e: SQLException) {
            throw DaoException(e.message)
        }
    }

    @Throws(DaoException::class)
    fun excluirRemetente(remetente: String?) {
        val stmt: PreparedStatement?
        val sql = StringBuilder()
        try {
            conexao!!.autoCommit = true
            sql.append("DELETE FROM TEC_REMETENTE WHERE ")
            sql.append("REM_NU = ? ")
            stmt = conexao!!.prepareStatement(sql.toString())
            stmt.setString(1, remetente)
            val cont = stmt.executeUpdate()
            if (cont == 0) throw DaoException("Não foi possível excluir dados!")
        } catch (e: SQLException) {
            throw DaoException(e.message)
        }
    }

    @Throws(DaoException::class)
    fun recuperaRemetente(filtro: String): ArrayList<RemetenteBean> {
        val rs: ResultSet?
        val stmt: PreparedStatement?
        val dados = ArrayList<RemetenteBean>()
        val sql = StringBuilder()
        var remetenteBean: RemetenteBean?
        try {
            sql.append(" SELECT  REM_NU, REM_TX_APELIDO,REM_TX_TITULO,")
            sql.append("         REM_TX_NOME,REM_ED_CEP,REM_ED_ENDERECO,")
            sql.append("         REM_ED_NUMERO,REM_ED_COMPLEMENTO,REM_ED_BAIRRO,")
            sql.append("         REM_ED_CIDADE,REM_ED_UF,REM_ED_EMAIL,REM_ED_TELEFONE,")
            sql.append("         REM_ED_FAX,REM_ED_CEP_CAIXA_POSTAL,REM_ED_CAIXA_POSTAL ")
            sql.append(" FROM  TEC_REMETENTE")
            if (filtro != "") sql.append(filtro)
            sql.append(" ORDER BY UPPER(REM_TX_NOME) ASC ")
            stmt = conexao!!.prepareStatement(sql.toString())
            rs = stmt.executeQuery()
            while (rs.next()) {
                remetenteBean = RemetenteBean()
                remetenteBean.numeroRemetente = rs.getString(1)
                remetenteBean.apelido = rs.getString(2)
                remetenteBean.titulo = rs.getString(3)
                remetenteBean.nome = rs.getString(4)
                remetenteBean.cep = rs.getString(5)
                remetenteBean.endereco = rs.getString(6)
                remetenteBean.numeroEndereco = rs.getString(7)
                remetenteBean.complemento = rs.getString(8)
                remetenteBean.bairro = rs.getString(9)
                remetenteBean.cidade = rs.getString(10)
                remetenteBean.uf = rs.getString(11)
                remetenteBean.email = rs.getString(12)
                remetenteBean.telefone = rs.getString(13)
                remetenteBean.fax = rs.getString(14)
                remetenteBean.cepCaixaPostal = rs.getString(15)
                remetenteBean.caixaPostal = rs.getString(16)
                dados.add(remetenteBean)
            }
        } catch (e: SQLException) {
            throw DaoException(e.message)
        }
        return dados
    }
}
