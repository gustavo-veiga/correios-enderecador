package br.com.correios.enderecador.dao

import br.com.correios.enderecador.conexao.ConexaoBD
import kotlin.Throws
import br.com.correios.enderecador.bean.GrupoDestinatarioBean
import java.sql.SQLException
import br.com.correios.enderecador.bean.DestinatarioBean
import br.com.correios.enderecador.exception.ConnectException
import br.com.correios.enderecador.exception.DaoException
import org.koin.core.annotation.Singleton
import java.sql.Connection

@Singleton
class GrupoDestinatarioDao(conexaoBD: ConexaoBD) {
    private val conexao: Connection

    init {
        try {
            conexao = conexaoBD.recuperaConexao()
        } catch (e: ConnectException) {
            throw DaoException("Não foi possível recuperar a conexão")
        }
    }

    @Throws(DaoException::class)
    fun incluirGrupoDestinatario(grupoDestinatarioBean: GrupoDestinatarioBean) {
        try {
            val stmt = conexao.prepareStatement("""
                INSERT INTO TEC_GRUPO_POSSUI_DEST (GRP_NU,DES_NU) VALUES (?,?)
            """.trimIndent())
            stmt.setString(1, grupoDestinatarioBean.numeroGrupo)
            stmt.setString(2, grupoDestinatarioBean.numeroDestinatario)
            val cont = stmt.executeUpdate()
            if (cont == 0) throw DaoException("Não foi possível adicionar dados!")
        } catch (e: SQLException) {
            throw DaoException(e.message)
        }
    }

    @Throws(DaoException::class)
    fun excluirGrupoDestinatario(filtro: String) {
        try {
            if (filtro.isBlank()) {
                throw DaoException("Não foi possível excluir dados!")
            }
            val stmt = conexao.prepareStatement("DELETE FROM TEC_GRUPO_POSSUI_DEST WHERE GRP_NU = $filtro")
            stmt.executeUpdate()
        } catch (e: SQLException) {
            throw DaoException(e.message)
        }
    }

    @Throws(DaoException::class)
    fun excluirDestinatarioDoGrupo(filtro: String) {
        try {
            if (filtro.isBlank()) {
                throw DaoException("Não foi possível excluir dados!")
            }
            val stmt = conexao.prepareStatement("DELETE FROM TEC_GRUPO_POSSUI_DEST WHERE DES_NU =$filtro")
            stmt.executeUpdate()
        } catch (e: SQLException) {
            throw DaoException(e.message)
        }
    }

    @Throws(DaoException::class)
    fun recuperaGrupoDestinatario(filtro: String = ""): List<DestinatarioBean> {
        val dados = mutableListOf<DestinatarioBean>()
        try {
            val stmt = conexao.prepareStatement("""
                SELECT  DES_NU, DES_TX_APELIDO, DES_TX_TITULO,
                    DES_TX_NOME, DES_ED_CEP, DES_ED_ENDERECO,
                    DES_ED_NUMERO, DES_ED_COMPLEMENTO, DES_ED_BAIRRO,
                    DES_ED_CIDADE, DES_ED_UF,DES_ED_EMAIL, DES_ED_TELEFONE,
                    DES_ED_FAX, DES_ED_CEP_CAIXA_POSTAL, DES_ED_CAIXA_POSTAL
                FROM TEC_DESTINATARIO
                INNER JOIN TEC_GRUPO_POSSUI_DEST
                ON TEC_GRUPO_POSSUI_DEST.DES_NU =TEC_DESTINATARIO.DES_NU
                AND GRP_NU = ?
                ORDER BY DES_TX_NOME ASC
            """.trimIndent())
            stmt.setString(1, filtro)
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
            throw DaoException(e.message)
        }
        return dados
    }
}
