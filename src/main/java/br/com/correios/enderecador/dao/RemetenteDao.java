package br.com.correios.enderecador.dao;

import br.com.correios.enderecador.bean.RemetenteBean;
import br.com.correios.enderecador.conexao.ConexaoBD;
import br.com.correios.enderecador.conexao.ConnectException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RemetenteDao {
    private final Connection conexao;

    private static RemetenteDao remetenteDao;

    public RemetenteDao() throws DaoException {
        try {
            this.conexao = ConexaoBD.getInstance().recuperaConexao();
        } catch (ConnectException e) {
            throw new DaoException("Não foi possível recuperar a conexão");
        }
    }

    public static RemetenteDao getInstance() throws DaoException {
        if (remetenteDao == null)
            remetenteDao = new RemetenteDao();
        return remetenteDao;
    }

    public void incluirRemetente(RemetenteBean remetenteBean) throws DaoException {
        PreparedStatement stmt = null;
        StringBuilder sql = new StringBuilder();
        try {
            this.conexao.setAutoCommit(true);
            sql.append("INSERT INTO TEC_REMETENTE ");
            sql.append("(REM_TX_APELIDO , REM_TX_TITULO,REM_TX_NOME, ");
            sql.append("REM_ED_CEP, REM_ED_ENDERECO,REM_ED_NUMERO, ");
            sql.append("REM_ED_COMPLEMENTO, REM_ED_BAIRRO, REM_ED_CIDADE, ");
            sql.append("REM_ED_UF, REM_ED_EMAIL, REM_ED_TELEFONE, ");
            sql.append("REM_ED_FAX, REM_ED_CEP_CAIXA_POSTAL, REM_ED_CAIXA_POSTAL) ");
            sql.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?) ");
            stmt = this.conexao.prepareStatement(sql.toString());
            stmt.setString(1, remetenteBean.getApelido());
            stmt.setString(2, remetenteBean.getTitulo());
            stmt.setString(3, remetenteBean.getNome());
            stmt.setString(4, remetenteBean.getCep());
            stmt.setString(5, remetenteBean.getEndereco());
            stmt.setString(6, remetenteBean.getNumeroEndereco());
            stmt.setString(7, remetenteBean.getComplemento());
            stmt.setString(8, remetenteBean.getBairro());
            stmt.setString(9, remetenteBean.getCidade());
            stmt.setString(10, remetenteBean.getUf());
            stmt.setString(11, remetenteBean.getEmail());
            stmt.setString(12, remetenteBean.getTelefone());
            stmt.setString(13, remetenteBean.getFax());
            stmt.setString(14, remetenteBean.getCepCaixaPostal());
            stmt.setString(15, remetenteBean.getCaixaPostal());
            int cont = stmt.executeUpdate();
            if (cont == 0)
                throw new DaoException("Não foi possível adicionar dados!");
            remetenteBean.setNumeroRemetente(getChave());
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    private String getChave() throws DaoException {
        String chave = null;
        PreparedStatement stmt = null;
        StringBuilder sql = new StringBuilder();
        ResultSet rs = null;
        sql.append(" CALL IDENTITY() ");
        try {
            stmt = this.conexao.prepareStatement(sql.toString());
            rs = stmt.executeQuery();
            if (rs.next()) {
                chave = rs.getString(1);
            } else {
                throw new DaoException("Não foi possível recuperar a chave do remetente.");
            }
        } catch (SQLException ex) {
            throw new DaoException(ex.getMessage());
        }
        return chave;
    }

    public void alterarRemetente(RemetenteBean remetenteBean) throws DaoException {
        PreparedStatement stmt;
        StringBuilder sql = new StringBuilder();
        try {
            this.conexao.setAutoCommit(true);
            sql.append("UPDATE TEC_REMETENTE SET ");
            sql.append(" REM_TX_APELIDO=? , REM_TX_TITULO=?,REM_TX_NOME=? ,");
            sql.append(" REM_ED_CEP=?, REM_ED_ENDERECO=?,REM_ED_NUMERO=? ,");
            sql.append(" REM_ED_COMPLEMENTO=?, REM_ED_BAIRRO=?, REM_ED_CIDADE=? ,");
            sql.append(" REM_ED_UF=?, REM_ED_EMAIL=?, REM_ED_TELEFONE=?, ");
            sql.append(" REM_ED_FAX=?, REM_ED_CEP_CAIXA_POSTAL=?, REM_ED_CAIXA_POSTAL=? ");
            sql.append(" WHERE REM_NU= ?  ");
            stmt = this.conexao.prepareStatement(sql.toString());
            stmt.setString(1, remetenteBean.getApelido());
            stmt.setString(2, remetenteBean.getTitulo());
            stmt.setString(3, remetenteBean.getNome());
            stmt.setString(4, remetenteBean.getCep());
            stmt.setString(5, remetenteBean.getEndereco());
            stmt.setString(6, remetenteBean.getNumeroEndereco());
            stmt.setString(7, remetenteBean.getComplemento());
            stmt.setString(8, remetenteBean.getBairro());
            stmt.setString(9, remetenteBean.getCidade());
            stmt.setString(10, remetenteBean.getUf());
            stmt.setString(11, remetenteBean.getEmail());
            stmt.setString(12, remetenteBean.getTelefone());
            stmt.setString(13, remetenteBean.getFax());
            stmt.setString(14, remetenteBean.getCepCaixaPostal());
            stmt.setString(15, remetenteBean.getCaixaPostal());
            stmt.setString(16, remetenteBean.getNumeroRemetente());
            int cont = stmt.executeUpdate();
            if (cont == 0)
                throw new DaoException("Não foi possível alterar dados!");
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    public void excluirRemetente(String remetente) throws DaoException {
        PreparedStatement stmt = null;
        StringBuilder sql = new StringBuilder();
        try {
            this.conexao.setAutoCommit(true);
            sql.append("DELETE FROM TEC_REMETENTE WHERE ");
            sql.append("REM_NU = ? ");
            stmt = this.conexao.prepareStatement(sql.toString());
            stmt.setString(1, remetente);
            int cont = stmt.executeUpdate();
            if (cont == 0)
                throw new DaoException("Não foi possível excluir dados!");
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    public ArrayList<RemetenteBean> recuperaRemetente(String filtro) throws DaoException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<RemetenteBean> dados = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        RemetenteBean remetenteBean = null;
        try {
            sql.append(" SELECT  REM_NU, REM_TX_APELIDO,REM_TX_TITULO,");
            sql.append("         REM_TX_NOME,REM_ED_CEP,REM_ED_ENDERECO,");
            sql.append("         REM_ED_NUMERO,REM_ED_COMPLEMENTO,REM_ED_BAIRRO,");
            sql.append("         REM_ED_CIDADE,REM_ED_UF,REM_ED_EMAIL,REM_ED_TELEFONE,");
            sql.append("         REM_ED_FAX,REM_ED_CEP_CAIXA_POSTAL,REM_ED_CAIXA_POSTAL ");
            sql.append(" FROM  TEC_REMETENTE");
            if (!filtro.equals(""))
                sql.append(filtro);
            sql.append(" ORDER BY UPPER(REM_TX_NOME) ASC ");
            stmt = this.conexao.prepareStatement(sql.toString());
            rs = stmt.executeQuery();
            while (rs.next()) {
                remetenteBean = new RemetenteBean();
                remetenteBean.setNumeroRemetente(rs.getString(1));
                remetenteBean.setApelido(rs.getString(2));
                remetenteBean.setTitulo(rs.getString(3));
                remetenteBean.setNome(rs.getString(4));
                remetenteBean.setCep(rs.getString(5));
                remetenteBean.setEndereco(rs.getString(6));
                remetenteBean.setNumeroEndereco(rs.getString(7));
                remetenteBean.setComplemento(rs.getString(8));
                remetenteBean.setBairro(rs.getString(9));
                remetenteBean.setCidade(rs.getString(10));
                remetenteBean.setUf(rs.getString(11));
                remetenteBean.setEmail(rs.getString(12));
                remetenteBean.setTelefone(rs.getString(13));
                remetenteBean.setFax(rs.getString(14));
                remetenteBean.setCepCaixaPostal(rs.getString(15));
                remetenteBean.setCaixaPostal(rs.getString(16));
                dados.add(remetenteBean);
            }
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
        return dados;
    }
}
