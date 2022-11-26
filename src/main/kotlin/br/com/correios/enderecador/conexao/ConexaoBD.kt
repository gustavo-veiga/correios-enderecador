package br.com.correios.enderecador.conexao

import kotlin.Throws
import br.com.correios.enderecador.bean.ConfiguracaoBean
import br.com.correios.enderecador.exception.ConnectException
import br.com.correios.enderecador.util.Logging
import java.sql.DriverManager
import java.lang.ClassNotFoundException
import org.koin.core.annotation.Singleton
import java.lang.Exception
import java.sql.Connection
import javax.swing.JOptionPane
import kotlin.system.exitProcess

@Singleton
class ConexaoBD {
    private val logger by Logging()

    private lateinit var connection: Connection

    @Throws(ConnectException::class)
    fun recuperaConexao(): Connection {
        try {
            Class.forName(ConfiguracaoBean.driverBanco)
            connection = DriverManager.getConnection(
                ConfiguracaoBean.urlBanco,
                ConfiguracaoBean.usuarioBanco,
                ConfiguracaoBean.senhaBanco
            ).apply {
                autoCommit = true
            }
            logger.info(ConfiguracaoBean.urlBanco)
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
        return connection
    }

    @Throws(ConnectException::class)
    fun liberarConexao() {
        connection.apply {
            createStatement().execute("SHUTDOWN")
            close()
        }
    }
}
