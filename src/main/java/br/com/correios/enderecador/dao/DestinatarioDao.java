package br.com.correios.enderecador.dao;

import br.com.correios.enderecador.bean.DestinatarioBean;
import br.com.correios.enderecador.conexao.ConexaoBD;
import br.com.correios.enderecador.conexao.ConnectException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DestinatarioDao {
    private final Connection conexao;

    private static DestinatarioDao destinatarioDao;

    public DestinatarioDao() throws DaoException {
        try {
            this.conexao = ConexaoBD.getInstance().recuperaConexao();
        } catch (ConnectException e) {
            throw new DaoException("Não foi possível recuperar a conexão");
        }
    }

    public static DestinatarioDao getInstance() throws DaoException {
        if (destinatarioDao == null)
            destinatarioDao = new DestinatarioDao();
        return destinatarioDao;
    }

    public void incluirDestinatario(DestinatarioBean destinatarioBean) throws DaoException {
        PreparedStatement stmt = null;
        StringBuilder sql = new StringBuilder();
        try {
            this.conexao.setAutoCommit(true);
            sql.append("INSERT INTO TEC_DESTINATARIO ");
            sql.append("(DES_TX_APELIDO , DES_TX_TITULO,DES_TX_NOME, ");
            sql.append("DES_ED_CEP, DES_ED_ENDERECO,DES_ED_NUMERO ,");
            sql.append("DES_ED_COMPLEMENTO, DES_ED_BAIRRO, DES_ED_CIDADE, ");
            sql.append("DES_ED_UF, DES_ED_EMAIL, DES_ED_TELEFONE, ");
            sql.append("DES_ED_FAX, DES_ED_CEP_CAIXA_POSTAL, DES_ED_CAIXA_POSTAL) ");
            sql.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?) ");
            stmt = this.conexao.prepareStatement(sql.toString());
            stmt.setString(1, destinatarioBean.getApelido());
            stmt.setString(2, destinatarioBean.getTitulo());
            stmt.setString(3, destinatarioBean.getNome());
            stmt.setString(4, destinatarioBean.getCep());
            stmt.setString(5, destinatarioBean.getEndereco());
            stmt.setString(6, destinatarioBean.getNumeroEndereco());
            stmt.setString(7, destinatarioBean.getComplemento());
            stmt.setString(8, destinatarioBean.getBairro());
            stmt.setString(9, destinatarioBean.getCidade());
            stmt.setString(10, destinatarioBean.getUf());
            stmt.setString(11, destinatarioBean.getEmail());
            stmt.setString(12, destinatarioBean.getTelefone());
            stmt.setString(13, destinatarioBean.getFax());
            stmt.setString(14, destinatarioBean.getCepCaixaPostal());
            stmt.setString(15, destinatarioBean.getCaixaPostal());
            int cont = stmt.executeUpdate();
            destinatarioBean.setNumeroDestinatario(getChave());
            if (cont == 0)
                throw new DaoException("Não foi possível adicionar dados!");
        } catch (SQLException e) {
            throw new DaoException(e.getMessage(), e);
        }
    }

    private String getChave() throws DaoException {
        String chave;
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

    public void alterarDestinatario(DestinatarioBean destinatarioBean) throws DaoException {
        PreparedStatement stmt = null;
        StringBuilder sql = new StringBuilder();
        try {
            this.conexao.setAutoCommit(true);
            sql.append("UPDATE TEC_DESTINATARIO SET ");
            sql.append("DES_TX_APELIDO=? , DES_TX_TITULO=?,DES_TX_NOME=? ,");
            sql.append("DES_ED_CEP=?, DES_ED_ENDERECO=?,DES_ED_NUMERO=? ,");
            sql.append("DES_ED_COMPLEMENTO=?, DES_ED_BAIRRO=?, DES_ED_CIDADE=? ,");
            sql.append("DES_ED_UF=?, DES_ED_EMAIL=?, DES_ED_TELEFONE=?, ");
            sql.append("DES_ED_FAX=?, DES_ED_CEP_CAIXA_POSTAL=?, DES_ED_CAIXA_POSTAL=? ");
            sql.append(" WHERE DES_NU= ? ");
            stmt = this.conexao.prepareStatement(sql.toString());
            stmt.setString(1, destinatarioBean.getApelido());
            stmt.setString(2, destinatarioBean.getTitulo());
            stmt.setString(3, destinatarioBean.getNome());
            stmt.setString(4, destinatarioBean.getCep());
            stmt.setString(5, destinatarioBean.getEndereco());
            stmt.setString(6, destinatarioBean.getNumeroEndereco());
            stmt.setString(7, destinatarioBean.getComplemento());
            stmt.setString(8, destinatarioBean.getBairro());
            stmt.setString(9, destinatarioBean.getCidade());
            stmt.setString(10, destinatarioBean.getUf());
            stmt.setString(11, destinatarioBean.getEmail());
            stmt.setString(12, destinatarioBean.getTelefone());
            stmt.setString(13, destinatarioBean.getFax());
            stmt.setString(14, destinatarioBean.getCepCaixaPostal());
            stmt.setString(15, destinatarioBean.getCaixaPostal());
            stmt.setString(16, destinatarioBean.getNumeroDestinatario());
            int cont = stmt.executeUpdate();
            if (cont == 0)
                throw new DaoException("Não foi possível alterar dados!");
        } catch (SQLException ex) {
            throw new DaoException(ex.getMessage(), ex);
        }
    }

    public void excluirDestinatario(String destinatario) throws DaoException {
        PreparedStatement stmt = null;
        StringBuilder sql = new StringBuilder();
        try {
            this.conexao.setAutoCommit(true);
            sql.append("DELETE FROM TEC_DESTINATARIO WHERE ");
            sql.append("DES_NU = ? ");
            stmt = this.conexao.prepareStatement(sql.toString());
            stmt.setString(1, destinatario);
            int cont = stmt.executeUpdate();
            if (cont == 0)
                throw new DaoException("Não foi possível excluir dados!");
        } catch (SQLException e) {
            throw new DaoException(e.getMessage(), e);
        }
    }

    public String recuperaUltimo() throws DaoException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        StringBuffer sql = new StringBuffer();
        try {
            this.conexao.setAutoCommit(true);
            sql.append("SELECT MAX(DES_NU) AS ULTIMO FROM TEC_DESTINATARIO");
            stmt = this.conexao.prepareStatement(sql.toString());
            rs = stmt.executeQuery();
            if (rs.next())
                return rs.getString(1);
        } catch (SQLException e) {
            throw new DaoException(e.getMessage(), e);
        }
        return "";
    }

    public ArrayList<DestinatarioBean> recuperaDestinatarioPorGrupo(String grupo) throws DaoException {
        String sql = "";
        sql = sql + " INNER JOIN  TEC_GRUPO_POSSUI_DEST";
        sql = sql + " ON TEC_GRUPO_POSSUI_DEST.DES_NU =TEC_DESTINATARIO.DES_NU";
        sql = sql + " AND GRP_NU =" + grupo;
        return recuperaDestinatario(sql);
    }

    public ArrayList<DestinatarioBean> recuperarDestinatarioForaDoGrupo(String grupo) throws DaoException {
        String sql = "";
        sql = sql + " WHERE DES_NU";
        sql = sql + " NOT IN";
        sql = sql + " (SELECT DES_NU FROM TEC_GRUPO_POSSUI_DEST WHERE GRP_NU=" + grupo + ")";
        return recuperaDestinatario(sql);
    }

    public ArrayList<DestinatarioBean> recuperaDestinatario(String filtro) throws DaoException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<DestinatarioBean> dados = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        DestinatarioBean destinatarioBean = null;
        try {
            this.conexao.setAutoCommit(true);
            sql.append(" SELECT  DES_NU, DES_TX_APELIDO,DES_TX_TITULO,");
            sql.append("         DES_TX_NOME,DES_ED_CEP,DES_ED_ENDERECO,");
            sql.append("         DES_ED_NUMERO,DES_ED_COMPLEMENTO,DES_ED_BAIRRO,");
            sql.append("         DES_ED_CIDADE,DES_ED_UF,DES_ED_EMAIL,DES_ED_TELEFONE,");
            sql.append("         DES_ED_FAX,DES_ED_CEP_CAIXA_POSTAL,DES_ED_CAIXA_POSTAL");
            sql.append(" FROM  TEC_DESTINATARIO ");
            if (!filtro.equals(""))
                sql.append(filtro);
            sql.append(" ORDER BY UPPER(DES_TX_NOME) ASC ");
            stmt = this.conexao.prepareStatement(sql.toString());
            rs = stmt.executeQuery();
            while (rs.next()) {
                destinatarioBean = new DestinatarioBean();
                destinatarioBean.setNumeroDestinatario(rs.getString(1));
                destinatarioBean.setApelido(rs.getString(2));
                destinatarioBean.setTitulo(rs.getString(3));
                destinatarioBean.setNome(rs.getString(4));
                destinatarioBean.setCep(rs.getString(5));
                destinatarioBean.setEndereco(rs.getString(6));
                destinatarioBean.setNumeroEndereco(rs.getString(7));
                destinatarioBean.setComplemento(rs.getString(8));
                destinatarioBean.setBairro(rs.getString(9));
                destinatarioBean.setCidade(rs.getString(10));
                destinatarioBean.setUf(rs.getString(11));
                destinatarioBean.setEmail(rs.getString(12));
                destinatarioBean.setTelefone(rs.getString(13));
                destinatarioBean.setFax(rs.getString(14));
                destinatarioBean.setCepCaixaPostal(rs.getString(15));
                destinatarioBean.setCaixaPostal(rs.getString(16));
                dados.add(destinatarioBean);
            }
        } catch (SQLException e) {
            throw new DaoException(e.getMessage(), e);
        }
        return dados;
    }
}
