package br.com.correios.enderecador.conexao;

import br.com.correios.enderecador.bean.ConfiguracaoBean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

public class ConexaoBD {
    private Connection conexao = null;

    private static ConexaoBD instance;

    static Logger logger = Logger.getLogger(ConexaoBD.class);

    public static ConexaoBD getInstance() {
        if (instance == null)
            instance = new ConexaoBD();
        return instance;
    }

    public Connection recuperaConexao() throws ConnectException {
        try {
            if (ConfiguracaoBean.getInstance().getUrlBanco() == null)
                throw new SQLException("Não foi possível recuperar as configurações da conexão com o banco de dados");
            Class.forName(ConfiguracaoBean.getInstance().getDriverBanco());
            this.conexao = DriverManager.getConnection(ConfiguracaoBean.getInstance().getUrlBanco(), ConfiguracaoBean.getInstance().getUsuarioBanco(), ConfiguracaoBean.getInstance().getSenhaBanco());
            if (this.conexao == null)
                throw new ConnectException("A conexão com o banco de dados não foi criada!");
        } catch (ClassNotFoundException e) {
            throw new ConnectException("Não foi possível localizar o driver");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            JOptionPane.showMessageDialog(null, "Já existe uma cópia deste programa rodando!", "Endereçador", 2);
            System.exit(0);
        }
        return this.conexao;
    }

    public void liberarConexao() throws ConnectException {
        Statement st = null;
        try {
            st = this.conexao.createStatement();
            st.execute("SHUTDOWN");
        } catch (Throwable ex) {
            logger.info(ex.getMessage(), ex);
        } finally {
            if (st != null)
                try {
                    st.close();
                } catch (SQLException ignored) {
                }
            try {
                this.conexao.close();
                this.conexao = null;
            } catch (SQLException e) {
                throw new ConnectException("Erro ao fechar o recuso");
            }
        }
    }

    public static void main(String[] args) throws ConnectException {
        Connection con = null;
        ConexaoBD conexaoBD = getInstance();
        try {
            con = conexaoBD.recuperaConexao();
        } catch (ConnectException ex) {
            ex.printStackTrace();
        }
        if (con != null)
            conexaoBD.liberarConexao();
    }
}
