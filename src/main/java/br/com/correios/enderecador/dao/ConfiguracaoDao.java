package br.com.correios.enderecador.dao;

import br.com.correios.enderecador.bean.ConfiguracaoBean;
import br.com.correios.enderecador.conexao.ConexaoBD;
import br.com.correios.enderecador.conexao.ConnectException;
import br.com.correios.enderecador.excecao.EnderecadorExcecao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class ConfiguracaoDao {
    private static ConfiguracaoDao configuracaoDao;

    private final Connection conexao;

    public ConfiguracaoDao() throws DaoException {
        try {
            this.conexao = ConexaoBD.getInstance().recuperaConexao();
        } catch (ConnectException e) {
            throw new DaoException("Não foi possível recuperar a conexão");
        }
    }

    public static ConfiguracaoDao getInstance() throws DaoException {
        if (configuracaoDao == null)
            configuracaoDao = new ConfiguracaoDao();
        return configuracaoDao;
    }

    public void incluirConfiguracao(ConfiguracaoBean configuracaoBean) throws DaoException {
        PreparedStatement stmt = null;
        StringBuilder sql = new StringBuilder();
        try {
            this.conexao.setAutoCommit(true);
            sql.append(" INSERT INTO TEC_CONFIGURACAO (CFG_CAMPO1, CFG_CAMPO2, CFG_CAMPO3,");
            sql.append("         CFG_CAMPO4, CFG_CAMPO5, CFG_CAMPO6,");
            sql.append("         CFG_CAMPO6, CFG_CAMPO7, CFG_CAMPO8,");
            sql.append("         CFG_CAMPO9, CFG_CAMPO10, CFG_CAMPO11, CFG_CAMPO12,");
            sql.append("         CFG_CAMPO13, CFG_CAMPO14, CFG_CAMPO15, CFG_CAMPO16, CFG_CAMPO17)");
            sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?) ");
            stmt = this.conexao.prepareStatement(sql.toString());
            stmt.setString(1, configuracaoBean.getProxy());
            stmt.setString(2, configuracaoBean.getPorta());
            stmt.setString(3, configuracaoBean.getChave());
            stmt.setString(4, configuracaoBean.getDominio());
            stmt.setString(5, null);
            stmt.setString(6, null);
            stmt.setString(7, null);
            stmt.setString(8, null);
            stmt.setString(9, null);
            stmt.setString(10, null);
            stmt.setString(11, null);
            stmt.setString(12, null);
            stmt.setString(13, null);
            stmt.setString(14, null);
            stmt.setString(15, null);
            stmt.setString(16, null);
            stmt.setString(17, null);
            int cont = stmt.executeUpdate();
            if (cont == 0)
                throw new EnderecadorExcecao("Não foi possível adicionar dados!");
        } catch (Exception e) {
            throw new DaoException(e.getMessage());
        }
    }

    public void alterarConfiguracao(ConfiguracaoBean configuracaoBean) throws DaoException {
        PreparedStatement stmt = null;
        StringBuilder sql = new StringBuilder();
        try {
            this.conexao.setAutoCommit(true);
            sql.append("UPDATE TEC_CONFIGURACAO SET ");
            sql.append(" CFG_CAMPO1=? , CFG_CAMPO2=?,CFG_CAMPO3=? ,");
            sql.append(" CFG_CAMPO4=?, CFG_CAMPO5=?,CFG_CAMPO6=? ,");
            sql.append(" CFG_CAMPO7=?, CFG_CAMPO8=?, CFG_CAMPO9=? ,");
            sql.append(" CFG_CAMPO10=?, CFG_CAMPO11=?, CFG_CAMPO12=?, ");
            sql.append(" CFG_CAMPO13=?, CFG_CAMPO14=?, CFG_CAMPO15=? ,");
            sql.append(" CFG_CAMPO16=?, CFG_CAMPO17=? ");
            stmt = this.conexao.prepareStatement(sql.toString());
            stmt.setString(1, configuracaoBean.getProxy());
            stmt.setString(2, configuracaoBean.getPorta());
            stmt.setString(3, configuracaoBean.getChave());
            stmt.setString(4, configuracaoBean.getDominio());
            stmt.setString(5, null);
            stmt.setString(6, null);
            stmt.setString(7, null);
            stmt.setString(8, null);
            stmt.setString(9, null);
            stmt.setString(10, null);
            stmt.setString(11, null);
            stmt.setString(12, null);
            stmt.setString(13, null);
            stmt.setString(14, null);
            stmt.setString(15, null);
            stmt.setString(16, null);
            stmt.setString(17, null);
            int cont = stmt.executeUpdate();
            if (cont == 0)
                throw new DaoException("Não foi possível alterar dados!");
        } catch (SQLException ex) {
            throw new DaoException(ex.getMessage());
        }
    }

    public void excluirConfiguracao(String configurcao) throws DaoException {
        PreparedStatement stmt = null;
        StringBuilder sql = new StringBuilder();
        try {
            this.conexao.setAutoCommit(true);
            sql.append("DELETE FROM TEC_CONFIGURACAO WHERE ");
            sql.append("?");
            stmt = this.conexao.prepareStatement(sql.toString());
            stmt.setString(1, configurcao);
            int cont = stmt.executeUpdate();
            if (cont == 0)
                throw new DaoException("Não foi possível excluir dados!");
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    public Vector<String> recuperaConfiguracao(String filtro) throws DaoException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Vector<String> dados = new Vector<>();
        StringBuilder sql = new StringBuilder();
        try {
            sql.append(" SELECT  CFG_CAMPO1, CFG_CAMPO2, CFG_CAMPO3,");
            sql.append("         CFG_CAMPO4, CFG_CAMPO5, CFG_CAMPO6,");
            sql.append("         CFG_CAMPO6, CFG_CAMPO7, CFG_CAMPO8,");
            sql.append("         CFG_CAMPO9,CFG_CAMPO10,CFG_CAMPO11,CFG_CAMPO12,");
            sql.append("         CFG_CAMPO13,CFG_CAMPO14,CFG_CAMPO15,CFG_CAMPO16,CFG_CAMPO17");
            sql.append(" FROM  TEC_CONFIGURACAO");
            if (!filtro.equals(""))
                sql.append(filtro);
            stmt = this.conexao.prepareStatement(sql.toString());
            rs = stmt.executeQuery();
            while (rs.next()) {
                dados.add(rs.getString(1));
                dados.add(rs.getString(2));
                dados.add(rs.getString(3));
                dados.add(rs.getString(4));
            }
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
        return dados;
    }
}
