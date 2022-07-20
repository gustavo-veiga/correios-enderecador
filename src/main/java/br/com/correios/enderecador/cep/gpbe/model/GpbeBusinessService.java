package br.com.correios.enderecador.cep.gpbe.model;

import br.com.correios.enderecador.cep.gpbe.bean.CepBean;
import br.com.correios.enderecador.cep.gpbe.excecao.CepNotFoundException;
import br.com.correios.enderecador.cep.gpbe.excecao.GpbeException;
import br.com.correios.enderecador.cep.gpbe.security.GpbeFox;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

public class GpbeBusinessService {
    private Connection conn;

    private static GpbeBusinessService business;

    private final GpbeFox fox;

    private final String urlConexao = "jdbc:ucanaccess:DRIVER={Microsoft Access Driver (*.mdb)};PWD=«¹½¿¥Ð²þÞ¢§³¼»;DBQ=" + System.getProperty("user.dir") + "\\..\\access\\dbgpbe.mdb";

    private GpbeBusinessService() throws GpbeException {
        getConnection();
        this.fox = new GpbeFox();
    }

    private void getConnection() throws GpbeException {
        try {
            if (this.conn == null) {
                this.conn = DriverManager.getConnection(this.urlConexao);
            }
        } catch (Exception e) {
            throw new GpbeException("Não foi possível estabelecer uma conexão com o banco.");
        }
    }

    public static GpbeBusinessService getInstance() throws GpbeException {
        try {
            if (business == null)
                business = new GpbeBusinessService();
        } catch (GpbeException e) {
            throw new GpbeException("Não foi possível instanciar objeto");
        }
        return business;
    }

    public CepBean recuperaCep(String cep) throws GpbeException, CepNotFoundException {
        CepBean cepBean = new CepBean();
        String cepFox = null;
        try {
            cepFox = this.fox.encrypt(cep);
        } catch (Exception e) {
            throw new GpbeException(e.getMessage());
        }
        CepBean localidade = recuperaCepLocalidade(cepFox);
        CepBean logradouro = recuperaCepLogradouro(cepFox);
        CepBean cepEspecial = recuperaCepEspecial(cepFox);
        CepBean unidadeOperacional = recuperaUnidadeOperacional(cepFox);
        CepBean caixaComunitaria = recuperaCaixaComunitaria(cepFox);
        if (localidade != null) {
            cepBean = localidade;
        } else if (logradouro != null) {
            cepBean = logradouro;
        } else if (cepEspecial != null) {
            cepBean = cepEspecial;
        } else if (unidadeOperacional != null) {
            cepBean = unidadeOperacional;
        } else if (caixaComunitaria != null) {
            cepBean = caixaComunitaria;
        } else {
            throw new CepNotFoundException("CEP Inválido!");
        }
        return cepBean;
    }

    private CepBean recuperaCepLocalidade(String cep) throws GpbeException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        StringBuilder sql = new StringBuilder();
        CepBean cepBean = null;
        try {
            sql.append(" SELECT LOC_NO AS LOCALIDADE, UFE_SG AS UF ");
            sql.append("   FROM LOG_LOCALIDADE ");
            sql.append("  WHERE LOC_KEY_DNE = ");
            sql.append("'").append(cep).append("'");
            stmt = this.conn.prepareStatement(sql.toString());
            rs = stmt.executeQuery();
            while (rs.next()) {
                cepBean = new CepBean();
                cepBean.setLocalidade(rs.getString("LOCALIDADE"));
                cepBean.setUf(rs.getString("UF"));
            }
        } catch (Exception e) {
            throw new GpbeException(e.getMessage());
        }
        return cepBean;
    }

    private CepBean recuperaCepLogradouro(String cep) throws GpbeException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        StringBuilder sql = new StringBuilder();
        CepBean cepBean = null;
        try {
            sql.append(" SELECT LOG.LOG_NOME AS LOGRADOURO, LOG.UFE_SG AS UF, ");
            sql.append("        BAI.BAI_NO AS BAIRRO, LOC.LOC_NO AS LOCALIDADE ");
            sql.append("   FROM LOG_LOGRADOURO LOG,   LOG_BAIRRO BAI, LOG_LOCALIDADE LOC ");
            sql.append("  WHERE (LOG.UFE_SG                = BAI.UFE_SG) ");
            sql.append("    AND (LOG.LOC_NU_SEQUENCIAL     = BAI.LOC_NU_SEQUENCIAL) ");
            sql.append("    AND (LOG.BAI_NU_SEQUENCIAL_INI = BAI.BAI_NU_SEQUENCIAL) ");
            sql.append("    AND (LOG.UFE_SG                = LOC.UFE_SG) ");
            sql.append("    AND (LOG.LOC_NU_SEQUENCIAL     = LOC.LOC_NU_SEQUENCIAL) ");
            sql.append("    AND (LOG.LOG_KEY_DNE = ");
            sql.append("'").append(cep).append("')");
            stmt = this.conn.prepareStatement(sql.toString());
            rs = stmt.executeQuery();
            while (rs.next()) {
                cepBean = new CepBean();
                cepBean.setBairro(rs.getString("BAIRRO"));
                cepBean.setLogradouro(rs.getString("LOGRADOURO"));
                cepBean.setLocalidade(rs.getString("LOCALIDADE"));
                cepBean.setUf(rs.getString("UF"));
            }
        } catch (Exception e) {
            throw new GpbeException(e.getMessage());
        }
        return cepBean;
    }

    private CepBean recuperaCepEspecial(String cep) throws GpbeException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        StringBuilder sql = new StringBuilder();
        CepBean cepBean = null;
        try {
            sql.append(" SELECT LGU.GRU_ENDERECO AS LOGRADOURO, LL.LOC_NO AS LOCALIDADE, ");
            sql.append("        LB.BAI_NO AS BAIRRO, LGU.UFE_SG AS UF ");
            sql.append("   FROM LOG_GRANDE_USUARIO LGU, LOG_BAIRRO LB, LOG_LOCALIDADE LL ");
            sql.append("  WHERE (LGU.UFE_SG                = LB.UFE_SG) ");
            sql.append("    AND (LGU.LOC_NU_SEQUENCIAL     = LB.LOC_NU_SEQUENCIAL) ");
            sql.append("    AND (LGU.BAI_NU_SEQUENCIAL     = LB.BAI_NU_SEQUENCIAL) ");
            sql.append("    AND (LGU.UFE_SG                = LL.UFE_SG) ");
            sql.append("    AND (LGU.LOC_NU_SEQUENCIAL     = LL.LOC_NU_SEQUENCIAL) ");
            sql.append("    AND (LGU.GRU_KEY_DNE = ");
            sql.append("'").append(cep).append("')");
            stmt = this.conn.prepareStatement(sql.toString());
            rs = stmt.executeQuery();
            while (rs.next()) {
                cepBean = new CepBean();
                cepBean.setBairro(rs.getString("BAIRRO"));
                cepBean.setLogradouro(rs.getString("LOGRADOURO"));
                cepBean.setLocalidade(rs.getString("LOCALIDADE"));
                cepBean.setUf(rs.getString("UF"));
            }
        } catch (Exception e) {
            throw new GpbeException(e.getMessage());
        }
        return cepBean;
    }

    private CepBean recuperaUnidadeOperacional(String cep) throws GpbeException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        StringBuilder sql = new StringBuilder();
        CepBean cepBean = null;
        try {
            sql.append(" SELECT LUOP.UOP_ENDERECO AS LOGRADOURO, LL.LOC_NO AS LOCALIDADE, ");
            sql.append("        LB.BAI_NO AS BAIRRO, LUOP.UFE_SG AS UF ");
            sql.append("   FROM Log_Unid_Oper LUOP, LOG_BAIRRO LB, LOG_LOCALIDADE LL ");
            sql.append("  WHERE  (LUOP.UFE_SG                = LB.UFE_SG) ");
            sql.append("    AND (LUOP.LOC_NU_SEQUENCIAL     = LB.LOC_NU_SEQUENCIAL) ");
            sql.append("    AND (LUOP.BAI_NU_SEQUENCIAL     = LB.BAI_NU_SEQUENCIAL) ");
            sql.append("    AND (LUOP.UFE_SG                = LL.UFE_SG) ");
            sql.append("    AND (LUOP.LOC_NU_SEQUENCIAL     = LL.LOC_NU_SEQUENCIAL) ");
            sql.append("    AND (LUOP.UOP_KEY_DNE = ");
            sql.append("'").append(cep).append("')");
            stmt = this.conn.prepareStatement(sql.toString());
            rs = stmt.executeQuery();
            while (rs.next()) {
                cepBean = new CepBean();
                cepBean.setBairro(rs.getString("BAIRRO"));
                cepBean.setLogradouro(rs.getString("LOGRADOURO"));
                cepBean.setLocalidade(rs.getString("LOCALIDADE"));
                cepBean.setUf(rs.getString("UF"));
            }
        } catch (Exception e) {
            throw new GpbeException(e.getMessage());
        }
        return cepBean;
    }

    private CepBean recuperaCaixaComunitaria(String cep) throws GpbeException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        StringBuilder sql = new StringBuilder();
        CepBean cepBean = null;
        try {
            sql.append(" SELECT CPC.CPC_ENDERECO AS LOGRADOURO, LOC.LOC_NO AS LOCALIDADE, ");
            sql.append("        CPC.UFE_SG AS UF ");
            sql.append("   FROM LOG_CPC CPC, LOG_LOCALIDADE LOC ");
            sql.append("  WHERE (CPC.UFE_SG = LOC.UFE_SG) ");
            sql.append("    AND (CPC.LOC_NU_SEQUENCIAL = LOC.LOC_NU_SEQUENCIAL) ");
            sql.append("     AND (CPC.CPC_KEY_DNE = ");
            sql.append("'").append(cep).append("')");
            stmt = this.conn.prepareStatement(sql.toString());
            rs = stmt.executeQuery();
            while (rs.next()) {
                cepBean = new CepBean();
                cepBean.setLogradouro(rs.getString("LOGRADOURO"));
                cepBean.setLocalidade(rs.getString("LOCALIDADE"));
                cepBean.setUf(rs.getString("UF"));
            }
        } catch (Exception e) {
            throw new GpbeException(e.getMessage());
        }
        return cepBean;
    }
}
