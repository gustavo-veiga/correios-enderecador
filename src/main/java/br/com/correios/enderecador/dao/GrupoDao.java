package br.com.correios.enderecador.dao;

import br.com.correios.enderecador.bean.GrupoBean;
import br.com.correios.enderecador.conexao.ConexaoBD;
import br.com.correios.enderecador.conexao.ConnectException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GrupoDao {
    private static GrupoDao grupoDao;

    private final Connection conexao;

    public GrupoDao() throws DaoException {
        try {
            this.conexao = ConexaoBD.getInstance().recuperaConexao();
        } catch (ConnectException e) {
            throw new DaoException("Não foi possível recuperar a conexão");
        }
    }

    public static GrupoDao getInstance() throws DaoException {
        if (grupoDao == null)
            grupoDao = new GrupoDao();
        return grupoDao;
    }

    public void incluirGrupo(GrupoBean grupoBean) throws DaoException {
        PreparedStatement stmt = null;
        StringBuilder sql = new StringBuilder();
        try {
            this.conexao.setAutoCommit(true);
            sql.append("INSERT INTO TEC_GRUPO");
            sql.append("(GRP_TX_DESCRICAO) ");
            sql.append("VALUES (?) ");
            stmt = this.conexao.prepareStatement(sql.toString());
            stmt.setString(1, grupoBean.getDescricaoGrupo());
            int cont = stmt.executeUpdate();
            if (cont == 0)
                throw new DaoException("Não foi possível adicionar dados!");
        } catch (SQLException e) {
            throw new DaoException(e.getMessage(), e);
        }
    }

    public void alterarGrupo(GrupoBean grupoBean) throws DaoException {
        PreparedStatement stmt = null;
        StringBuilder sql = new StringBuilder();
        try {
            this.conexao.setAutoCommit(true);
            sql.append("UPDATE TEC_GRUPO SET ");
            sql.append(" GRP_TX_DESCRICAO=? ");
            sql.append(" WHERE GRP_NU= ?  ");
            stmt = this.conexao.prepareStatement(sql.toString());
            stmt.setString(1, grupoBean.getDescricaoGrupo());
            stmt.setString(2, grupoBean.getNumeroGrupo());
            int cont = stmt.executeUpdate();
            if (cont == 0)
                throw new DaoException("Não foi possível alterar dados!");
        } catch (SQLException e) {
            throw new DaoException(e.getMessage(), e);
        }
    }

    public void excluirGrupo(String grupo) throws DaoException {
        PreparedStatement stmt = null;
        StringBuilder sql = new StringBuilder();
        try {
            this.conexao.setAutoCommit(true);
            sql.append("DELETE FROM TEC_GRUPO WHERE ");
            sql.append("GRP_NU = ? ");
            stmt = this.conexao.prepareStatement(sql.toString());
            stmt.setString(1, grupo);
            int cont = stmt.executeUpdate();
            if (cont == 0)
                throw new DaoException("Não foi possível excluir dados!");
        } catch (SQLException e) {
            throw new DaoException(e.getMessage(), e);
        }
    }

    public ArrayList<GrupoBean> recuperaGrupo(String filtro) throws DaoException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<GrupoBean> dados = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        try {
            this.conexao.setAutoCommit(true);
            sql.append("SELECT * FROM TEC_GRUPO ");
            if (!"".equals(filtro))
                sql.append(filtro);
            sql.append("ORDER BY UPPER(GRP_TX_DESCRICAO) ASC ");
            stmt = this.conexao.prepareStatement(sql.toString());
            rs = stmt.executeQuery();
            while (rs.next()) {
                GrupoBean grupoBean = new GrupoBean();
                grupoBean.setNumeroGrupo(rs.getString(1));
                grupoBean.setDescricaoGrupo(rs.getString(2));
                dados.add(grupoBean);
            }
        } catch (SQLException e) {
            throw new DaoException(e.getMessage(), e);
        }
        return dados;
    }

    public String recuperaUltimoGrupo() throws DaoException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        StringBuilder sql = new StringBuilder();
        String ultimoNumero = "";
        try {
            this.conexao.setAutoCommit(true);
            sql.append("SELECT MAX(GRP_NU) AS ULTIMO FROM TEC_GRUPO ");
            stmt = this.conexao.prepareStatement(sql.toString());
            rs = stmt.executeQuery();
            while (rs.next())
                ultimoNumero = rs.getString(1);
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
        return ultimoNumero;
    }
}
