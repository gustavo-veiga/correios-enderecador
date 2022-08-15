package br.com.correios.enderecador.dao

import br.com.correios.enderecador.conexao.ConexaoBD
import kotlin.Throws
import br.com.correios.enderecador.bean.GrupoDestinatarioBean
import java.sql.PreparedStatement
import java.lang.StringBuilder
import java.sql.SQLException
import br.com.correios.enderecador.bean.DestinatarioBean
import br.com.correios.enderecador.conexao.ConnectException
import org.koin.core.annotation.Singleton
import java.sql.ResultSet
import java.sql.Connection
import java.util.ArrayList

@Singleton
class GrupoDestinatarioDao(
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
    fun incluirGrupoDestinatario(grupoDestinatarioBean: GrupoDestinatarioBean) {
        val stmt: PreparedStatement?
        val sql = StringBuilder()
        try {
            conexao!!.autoCommit = true
            sql.append("INSERT INTO TEC_GRUPO_POSSUI_DEST")
            sql.append("(GRP_NU,DES_NU) ")
            sql.append("VALUES (?,?) ")
            stmt = conexao!!.prepareStatement(sql.toString())
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
        val stmt: PreparedStatement?
        try {
            conexao!!.autoCommit = true
            if (filtro == "") throw DaoException("Não foi possível excluir dados!")
            stmt = conexao!!.prepareStatement("DELETE FROM TEC_GRUPO_POSSUI_DEST WHERE GRP_NU =$filtro")
            stmt.executeUpdate()
        } catch (e: SQLException) {
            throw DaoException(e.message)
        }
    }

    @Throws(DaoException::class)
    fun excluirDestinatarioDoGrupo(filtro: String) {
        val stmt: PreparedStatement?
        try {
            conexao!!.autoCommit = true
            if (filtro == "") throw DaoException("Não foi possível excluir dados!")
            stmt = conexao!!.prepareStatement("DELETE FROM TEC_GRUPO_POSSUI_DEST WHERE DES_NU =$filtro")
            stmt.executeUpdate()
        } catch (e: SQLException) {
            throw DaoException(e.message)
        }
    }

    @Throws(DaoException::class)
    fun recuperaGrupoDestinatario(filtro: String?): ArrayList<DestinatarioBean> {
        val rs: ResultSet?
        val stmt: PreparedStatement?
        val dados = ArrayList<DestinatarioBean>()
        val sql = StringBuilder()
        try {
            conexao!!.autoCommit = true
            sql.append(" SELECT  DES_NU, DES_TX_APELIDO,DES_TX_TITULO,")
            sql.append("         DES_TX_NOME,DES_ED_CEP,DES_ED_ENDERECO,")
            sql.append("         DES_ED_NUMERO,DES_ED_COMPLEMENTO,DES_ED_BAIRRO,")
            sql.append("         DES_ED_CIDADE,DES_ED_UF,DES_ED_EMAIL,DES_ED_TELEFONE,")
            sql.append("         DES_ED_FAX,DES_ED_CEP_CAIXA_POSTAL,DES_ED_CAIXA_POSTAL")
            sql.append(" FROM  TEC_DESTINATARIO")
            sql.append(" INNER JOIN  TEC_GRUPO_POSSUI_DEST")
            sql.append(" ON TEC_GRUPO_POSSUI_DEST.DES_NU =TEC_DESTINATARIO.DES_NU")
            sql.append(" AND GRP_NU = ?")
            sql.append(" ORDER BY DES_TX_NOME ASC")
            stmt = conexao!!.prepareStatement(sql.toString())
            stmt.setString(1, filtro)
            rs = stmt.executeQuery()
            while (rs.next()) {
                val destinatarioBean = DestinatarioBean()
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
            throw DaoException(e.message)
        }
        return dados
    }
}
