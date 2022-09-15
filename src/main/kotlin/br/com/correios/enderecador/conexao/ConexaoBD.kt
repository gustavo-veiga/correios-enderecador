package br.com.correios.enderecador.conexao

import kotlin.Throws
import br.com.correios.enderecador.bean.ConfiguracaoBean
import br.com.correios.enderecador.exception.ConnectException
import java.sql.DriverManager
import java.lang.ClassNotFoundException
import org.apache.log4j.Logger
import org.koin.core.annotation.Singleton
import java.lang.Exception
import java.sql.Connection
import javax.swing.JOptionPane
import kotlin.system.exitProcess

@Singleton
class ConexaoBD {
    private lateinit var conexao: Connection

    @Throws(ConnectException::class)
    fun recuperaConexao(): Connection {
        try {
            Class.forName(ConfiguracaoBean.driverBanco)
            conexao = DriverManager.getConnection(
                ConfiguracaoBean.urlBanco,
                ConfiguracaoBean.usuarioBanco,
                ConfiguracaoBean.senhaBanco
            ).apply {
                autoCommit = true
            }
        } catch (e: ClassNotFoundException) {
            throw ConnectException("Não foi possível localizar o driver")
        } catch (e: Exception) {
            logger.error(e.message, e)
            JOptionPane.showMessageDialog(
                null,
                "Já existe uma cópia deste programa rodando!", "Endereçador",
                JOptionPane.WARNING_MESSAGE
            )
            exitProcess(0)
        }
        return conexao
    }

    @Throws(ConnectException::class)
    fun liberarConexao() {
        conexao.apply {
            createStatement().execute("SHUTDOWN")
            close()
        }
    }

    companion object {
        private val logger = Logger.getLogger(ConexaoBD::class.java)
    }
}
