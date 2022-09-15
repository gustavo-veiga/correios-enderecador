package br.com.correios.enderecador.dao

import br.com.correios.enderecador.conexao.ConexaoBD
import br.com.correios.enderecador.bean.RemetenteBean
import br.com.correios.enderecador.exception.ConnectException
import br.com.correios.enderecador.exception.DaoException
import org.koin.core.annotation.Singleton
import java.sql.PreparedStatement
import java.lang.StringBuilder
import java.sql.SQLException
import java.sql.Connection

@Singleton
class RemetenteDao(conexaoBD: ConexaoBD) {
    private val conexao: Connection

    init {
        try {
            conexao = conexaoBD.recuperaConexao()
        } catch (e: ConnectException) {
            throw DaoException("Não foi possível recuperar a conexão")
        }
    }

    private fun PreparedStatement.remetenteBean(remetenteBean: RemetenteBean) {
        this.setString(1, remetenteBean.apelido)
        this.setString(2, remetenteBean.titulo)
        this.setString(3, remetenteBean.nome)
        this.setString(4, remetenteBean.cep)
        this.setString(5, remetenteBean.endereco)
        this.setString(6, remetenteBean.numeroEndereco)
        this.setString(7, remetenteBean.complemento)
        this.setString(8, remetenteBean.bairro)
        this.setString(9, remetenteBean.cidade)
        this.setString(10, remetenteBean.uf)
        this.setString(11, remetenteBean.email)
        this.setString(12, remetenteBean.telefone)
        this.setString(13, remetenteBean.fax)
        this.setString(14, remetenteBean.cepCaixaPostal)
        this.setString(15, remetenteBean.caixaPostal)
    }

    fun incluirRemetente(remetenteBean: RemetenteBean) {
        try {
            val stmt = conexao.prepareStatement("""
                INSERT INTO TEC_REMETENTE (
                    REM_TX_APELIDO, REM_TX_TITULO, REM_TX_NOME,
                    REM_ED_CEP, REM_ED_ENDERECO, REM_ED_NUMERO,
                    REM_ED_COMPLEMENTO, REM_ED_BAIRRO, REM_ED_CIDADE,
                    REM_ED_UF, REM_ED_EMAIL, REM_ED_TELEFONE,
                    REM_ED_FAX, REM_ED_CEP_CAIXA_POSTAL, REM_ED_CAIXA_POSTAL) 
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?)
            """.trimIndent())
            stmt.remetenteBean(remetenteBean)
            stmt.executeUpdate()
            remetenteBean.numeroRemetente = chave
        } catch (e: SQLException) {
            throw DaoException(e.message)
        }
    }

    private val chave: String
        get() {
            val chave: String?
            try {
                val stmt = conexao.prepareStatement(" CALL IDENTITY() ")
                val rs = stmt.executeQuery()
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

    fun alterarRemetente(remetenteBean: RemetenteBean) {
        try {
            val stmt = conexao.prepareStatement("""
                UPDATE TEC_REMETENTE SET 
                    REM_TX_APELIDO = ?, REM_TX_TITULO = ?, REM_TX_NOME = ?,
                    REM_ED_CEP = ?, REM_ED_ENDERECO = ?, REM_ED_NUMERO = ?,
                    REM_ED_COMPLEMENTO = ?, REM_ED_BAIRRO = ?, REM_ED_CIDADE = ?,
                    REM_ED_UF = ?, REM_ED_EMAIL = ?, REM_ED_TELEFONE = ?,
                    REM_ED_FAX = ?, REM_ED_CEP_CAIXA_POSTAL = ?, REM_ED_CAIXA_POSTAL = ?
                 WHERE REM_NU = ?
            """.trimIndent())
            stmt.remetenteBean(remetenteBean)
            stmt.executeUpdate()
        } catch (e: SQLException) {
            throw DaoException(e.message)
        }
    }

    fun excluirRemetente(remetente: String) {
        try {
            val stmt = conexao.prepareStatement("""
                DELETE FROM TEC_REMETENTE WHERE REM_NU = ?
            """.trimIndent())
            stmt.setString(1, remetente)
            stmt.executeUpdate()
        } catch (e: SQLException) {
            throw DaoException(e.message)
        }
    }

    fun recuperaRemetente(filtro: String = ""): List<RemetenteBean> {
        val dados = mutableListOf<RemetenteBean>()
        try {
            val stmt = conexao.prepareStatement("""
                SELECT  REM_NU, REM_TX_APELIDO, REM_TX_TITULO,
                    REM_TX_NOME, REM_ED_CEP, REM_ED_ENDERECO,
                    REM_ED_NUMERO,REM_ED_COMPLEMENTO,REM_ED_BAIRRO,
                    REM_ED_CIDADE,REM_ED_UF,REM_ED_EMAIL,REM_ED_TELEFONE,
                    REM_ED_FAX,REM_ED_CEP_CAIXA_POSTAL,REM_ED_CAIXA_POSTAL
                    FROM  TEC_REMETENTE
                    $filtro
                    ORDER BY UPPER(REM_TX_NOME) ASC
            """.trimIndent())
            val rs = stmt.executeQuery()
            while (rs.next()) {
                dados.add(RemetenteBean(
                    numeroRemetente = rs.getString(1),
                    apelido = rs.getString(2),
                    titulo = rs.getString(3),
                    nome = rs.getString(4),
                    cep = rs.getString(5),
                    endereco = rs.getString(6),
                    numeroEndereco = rs.getString(7),
                    complemento = rs.getString(8),
                    bairro = rs.getString(9),
                    cidade = rs.getString(10),
                    uf = rs.getString(11),
                    email = rs.getString(12),
                    telefone = rs.getString(13),
                    fax = rs.getString(14),
                    cepCaixaPostal = rs.getString(15),
                    caixaPostal = rs.getString(16),
                ))
            }
        } catch (e: SQLException) {
            throw DaoException(e.message)
        }
        return dados
    }
}
