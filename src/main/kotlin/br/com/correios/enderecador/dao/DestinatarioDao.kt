package br.com.correios.enderecador.dao

import br.com.correios.enderecador.conexao.ConexaoBD
import kotlin.Throws
import br.com.correios.enderecador.bean.DestinatarioBean
import br.com.correios.enderecador.exception.ConnectException
import br.com.correios.enderecador.exception.DaoException
import org.koin.core.annotation.Singleton
import java.sql.SQLException
import java.sql.Connection

@Singleton
class DestinatarioDao(conexaoBD: ConexaoBD) {
    private val conexao: Connection

    init {
        try {
            conexao = conexaoBD.recuperaConexao()
        } catch (e: ConnectException) {
            throw DaoException("Não foi possível recuperar a conexão")
        }
    }

    fun incluirDestinatario(destinatarioBean: DestinatarioBean) {
        try {
            val stmt = conexao.prepareStatement("""
                INSERT INTO TEC_DESTINATARIO (
                    DES_TX_APELIDO , DES_TX_TITULO,DES_TX_NOME,
                    DES_ED_CEP, DES_ED_ENDERECO,DES_ED_NUMERO ,
                    DES_ED_COMPLEMENTO, DES_ED_BAIRRO, DES_ED_CIDADE,
                    DES_ED_UF, DES_ED_EMAIL, DES_ED_TELEFONE,
                    DES_ED_FAX, DES_ED_CEP_CAIXA_POSTAL, DES_ED_CAIXA_POSTAL)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?)
            """.trimIndent())
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

    private val chave: String
        get() {
            try {
                val stmt = conexao.prepareStatement(" CALL IDENTITY() ")
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    return rs.getString(1)
                } else {
                    throw DaoException("Não foi possível recuperar a chave do remetente.")
                }
            } catch (ex: SQLException) {
                throw DaoException(ex.message)
            }
        }

    fun alterarDestinatario(destinatarioBean: DestinatarioBean) {
        try {
            val stmt = conexao.prepareStatement("""
                UPDATE TEC_DESTINATARIO SET
                    DES_TX_APELIDO = ?, DES_TX_TITULO = ?, DES_TX_NOME = ?,
                    DES_ED_CEP = ?, DES_ED_ENDERECO = ?,DES_ED_NUMERO = ?,
                    DES_ED_COMPLEMENTO = ?, DES_ED_BAIRRO = ?, DES_ED_CIDADE = ?,
                    DES_ED_UF = ?, DES_ED_EMAIL = ?, DES_ED_TELEFONE = ?, 
                    DES_ED_FAX = ?, DES_ED_CEP_CAIXA_POSTAL = ?, DES_ED_CAIXA_POSTAL = ? 
                WHERE DES_NU = ? 
            """.trimIndent())
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
    fun excluirDestinatario(destinatario: String) {
        try {
            val stmt = conexao.prepareStatement("""
                DELETE FROM TEC_DESTINATARIO WHERE DES_NU = ?
            """.trimIndent())
            stmt.setString(1, destinatario)
            val cont = stmt.executeUpdate()
            if (cont == 0) {
                throw DaoException("Não foi possível excluir dados!")
            }
        } catch (e: SQLException) {
            throw DaoException(e.message, e)
        }
    }

    @Throws(DaoException::class)
    fun recuperaUltimo(): String {
        try {
            val stmt = conexao.prepareStatement("""
                SELECT MAX(DES_NU) AS ULTIMO FROM TEC_DESTINATARIO
            """.trimIndent())
            val rs = stmt.executeQuery()
            if (rs.next()) {
                return rs.getString(1)
            }
        } catch (e: SQLException) {
            throw DaoException(e.message, e)
        }
        return ""
    }

    fun recuperaDestinatarioPorGrupo(grupo: String): List<DestinatarioBean> {
        return recuperaDestinatario("""
            INNER JOIN  TEC_GRUPO_POSSUI_DEST
            ON TEC_GRUPO_POSSUI_DEST.DES_NU =TEC_DESTINATARIO.DES_NU
            AND GRP_NU = $grupo
        """.trimIndent())
    }

    fun recuperarDestinatarioForaDoGrupo(grupo: String): List<DestinatarioBean> {
        return recuperaDestinatario("""
            WHERE DES_NU
            NOT IN
            (SELECT DES_NU FROM TEC_GRUPO_POSSUI_DEST WHERE GRP_NU=$grupo)
        """.trimIndent())
    }

    @Throws(DaoException::class)
    fun recuperaDestinatario(filtro: String = ""): List<DestinatarioBean> {
        val dados = mutableListOf<DestinatarioBean>()
        try {
            val stmt = conexao.prepareStatement("""
                SELECT DES_NU, DES_TX_APELIDO,DES_TX_TITULO,
                    DES_TX_NOME,DES_ED_CEP,DES_ED_ENDERECO,
                    DES_ED_NUMERO,DES_ED_COMPLEMENTO,DES_ED_BAIRRO,
                    DES_ED_CIDADE,DES_ED_UF,DES_ED_EMAIL,DES_ED_TELEFONE,
                    DES_ED_FAX,DES_ED_CEP_CAIXA_POSTAL,DES_ED_CAIXA_POSTAL
                FROM TEC_DESTINATARIO
                $filtro
                 ORDER BY UPPER(DES_TX_NOME) ASC
            """.trimIndent())
            val rs = stmt.executeQuery()
            while (rs.next()) {
                dados.add(DestinatarioBean(
                    numeroDestinatario = rs.getString(1),
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
                    caixaPostal = rs.getString(16)
                ))
            }
        } catch (e: SQLException) {
            throw DaoException(e.message, e)
        }
        return dados
    }
}
