package br.com.correios.enderecador.dao;

import br.com.correios.enderecador.bean.DestinatarioBean;
import br.com.correios.enderecador.bean.GrupoDestinatarioBean;
import br.com.correios.enderecador.conexao.ConexaoBD;
import br.com.correios.enderecador.conexao.ConnectException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GrupoDestinatarioDao {
    private static GrupoDestinatarioDao grupoDestinatarioDao;

    private final Connection conexao;

    public GrupoDestinatarioDao() throws DaoException {
        try {
            this.conexao = ConexaoBD.getInstance().recuperaConexao();
        } catch (ConnectException e) {
            throw new DaoException("Não foi possível recuperar a conexão");
        }
    }

    public static GrupoDestinatarioDao getInstance() throws DaoException {
        if (grupoDestinatarioDao == null)
            grupoDestinatarioDao = new GrupoDestinatarioDao();
        return grupoDestinatarioDao;
    }

    public void incluirGrupoDestinatario(GrupoDestinatarioBean grupoDestinatarioBean) throws DaoException {
        PreparedStatement stmt = null;
        StringBuilder sql = new StringBuilder();
        try {
            this.conexao.setAutoCommit(true);
            sql.append("INSERT INTO TEC_GRUPO_POSSUI_DEST");
            sql.append("(GRP_NU,DES_NU) ");
            sql.append("VALUES (?,?) ");
            stmt = this.conexao.prepareStatement(sql.toString());
            stmt.setString(1, grupoDestinatarioBean.getNumeroGrupo());
            stmt.setString(2, grupoDestinatarioBean.getNumeroDestinatario());
            int cont = stmt.executeUpdate();
            if (cont == 0)
                throw new DaoException("Não foi possível adicionar dados!");
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    public void excluirGrupoDestinatario(String filtro) throws DaoException {
        PreparedStatement stmt = null;
        try {
            this.conexao.setAutoCommit(true);
            if (filtro.equals(""))
                throw new DaoException("Não foi possível excluir dados!");
            stmt = this.conexao.prepareStatement("DELETE FROM TEC_GRUPO_POSSUI_DEST WHERE GRP_NU =" + filtro);
            int cont = stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    public void excluirDestinatarioDoGrupo(String filtro) throws DaoException {
        PreparedStatement stmt = null;
        try {
            this.conexao.setAutoCommit(true);
            if (filtro.equals(""))
                throw new DaoException("Não foi possível excluir dados!");
            stmt = this.conexao.prepareStatement("DELETE FROM TEC_GRUPO_POSSUI_DEST WHERE DES_NU =" + filtro);
            int cont = stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    public ArrayList<DestinatarioBean> recuperaGrupoDestinatario(String filtro) throws DaoException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<DestinatarioBean> dados = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        try {
            this.conexao.setAutoCommit(true);
            sql.append(" SELECT  DES_NU, DES_TX_APELIDO,DES_TX_TITULO,");
            sql.append("         DES_TX_NOME,DES_ED_CEP,DES_ED_ENDERECO,");
            sql.append("         DES_ED_NUMERO,DES_ED_COMPLEMENTO,DES_ED_BAIRRO,");
            sql.append("         DES_ED_CIDADE,DES_ED_UF,DES_ED_EMAIL,DES_ED_TELEFONE,");
            sql.append("         DES_ED_FAX,DES_ED_CEP_CAIXA_POSTAL,DES_ED_CAIXA_POSTAL");
            sql.append(" FROM  TEC_DESTINATARIO");
            sql.append(" INNER JOIN  TEC_GRUPO_POSSUI_DEST");
            sql.append(" ON TEC_GRUPO_POSSUI_DEST.DES_NU =TEC_DESTINATARIO.DES_NU");
            sql.append(" AND GRP_NU = ?");
            sql.append(" ORDER BY DES_TX_NOME ASC");
            stmt = this.conexao.prepareStatement(sql.toString());
            stmt.setString(1, filtro);
            rs = stmt.executeQuery();
            while (rs.next()) {
                DestinatarioBean destinatarioBean = new DestinatarioBean();
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
            throw new DaoException(e.getMessage());
        }
        return dados;
    }
}
