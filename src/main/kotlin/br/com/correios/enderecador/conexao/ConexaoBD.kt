package br.com.correios.enderecador.conexao

import kotlin.Throws
import br.com.correios.enderecador.bean.ConfiguracaoBean
import java.sql.SQLException
import java.sql.DriverManager
import java.lang.ClassNotFoundException
import org.apache.log4j.Logger
import java.lang.Exception
import java.sql.Connection
import java.sql.Statement
import javax.swing.JOptionPane
import kotlin.jvm.JvmStatic

class ConexaoBD {
    private var conexao: Connection? = null

    @Throws(ConnectException::class)
    fun recuperaConexao(): Connection? {
        try {
            Class.forName(ConfiguracaoBean.instance!!.driverBanco)
            conexao = DriverManager.getConnection(
                ConfiguracaoBean.instance!!.urlBanco,
                ConfiguracaoBean.instance!!.usuarioBanco,
                ConfiguracaoBean.instance!!.senhaBanco
            )
            if (conexao == null) throw ConnectException("A conexão com o banco de dados não foi criada!")
        } catch (e: ClassNotFoundException) {
            throw ConnectException("Não foi possível localizar o driver")
        } catch (e: Exception) {
            logger.error(e.message, e)
            JOptionPane.showMessageDialog(null, "Já existe uma cópia deste programa rodando!", "Endereçador", 2)
            System.exit(0)
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
        @JvmStatic
        var instance: ConexaoBD? = null
            get() {
                if (field == null) {
                    field = ConexaoBD()
                }
                return field
            }
            private set
        var logger = Logger.getLogger(ConexaoBD::class.java)
    }
}
