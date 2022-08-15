package br.com.correios.enderecador.conexao

import kotlin.Throws
import br.com.correios.enderecador.bean.ConfiguracaoBean
import java.sql.SQLException
import java.sql.DriverManager
import java.lang.ClassNotFoundException
import org.apache.log4j.Logger
import org.koin.core.annotation.Singleton
import java.lang.Exception
import java.sql.Connection
import java.sql.Statement
import javax.swing.JOptionPane
import kotlin.system.exitProcess

@Singleton
class ConexaoBD(
    private val configuracaoBean: ConfiguracaoBean
) {
    private var conexao: Connection? = null

    @Throws(ConnectException::class)
    fun recuperaConexao(): Connection? {
        try {
            Class.forName(configuracaoBean.driverBanco)
            conexao = DriverManager.getConnection(
                configuracaoBean.urlBanco,
                configuracaoBean.usuarioBanco,
                configuracaoBean.senhaBanco
            )
            if (conexao == null) throw ConnectException("A conexão com o banco de dados não foi criada!")
        } catch (e: ClassNotFoundException) {
            throw ConnectException("Não foi possível localizar o driver")
        } catch (e: Exception) {
            logger.error(e.message, e)
            JOptionPane.showMessageDialog(null, "Já existe uma cópia deste programa rodando!", "Endereçador", 2)
            exitProcess(0)
        }
        return conexao
    }

    @Throws(ConnectException::class)
    fun liberarConexao() {
        var st: Statement? = null
        try {
            st = conexao!!.createStatement()
            st.execute("SHUTDOWN")
        } catch (ex: Throwable) {
            logger.info(ex.message, ex)
        } finally {
            if (st != null) try {
                st.close()
            } catch (ignored: SQLException) {
            }
            try {
                conexao!!.close()
                conexao = null
            } catch (e: SQLException) {
                throw ConnectException("Erro ao fechar o recuso")
            }
        }
    }

    companion object {
        private val logger = Logger.getLogger(ConexaoBD::class.java)
    }
}
