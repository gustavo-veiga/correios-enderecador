package br.com.correios.enderecador.bean;

public class ImpressaoBean {
    private String des_campo1;

    private String des_campo2;

    private String des_campo3;

    private String des_campo4;

    private String des_campo5;

    private String des_campo6;

    private String des_campo7;

    private String des_campo8;

    private String des_cep;

    private String des_codBarras;

    private String desMaoPropria;

    private String desConteudo;

    private String des_entrega;

    private String rem_campo1;

    private String rem_campo2;

    private String rem_campo3;

    private String rem_campo4;

    private String rem_campo5;

    private String rem_campo6;

    private String rem_campo7;

    private String rem_campo8;

    private String rem_cep;

    private String rem_codBarras;

    private String codigoDoisD;

    public static final int ORGANIZA_CAMPOS_TODOS = 1;

    public static final int ORGANIZA_CAMPOS_ENCOMENDAS = 2;

    public String getDes_campo1() {
        return this.des_campo1;
    }

    public void setDes_campo1(String des_campo1) {
        this.des_campo1 = des_campo1.trim();
    }

    public String getDes_campo2() {
        return this.des_campo2;
    }

    public void setDes_campo2(String des_campo2) {
        this.des_campo2 = des_campo2.trim();
    }

    public String getDes_campo3() {
        return this.des_campo3;
    }

    public void setDes_campo3(String des_campo3) {
        this.des_campo3 = des_campo3.trim();
    }

    public String getDes_campo4() {
        return this.des_campo4;
    }

    public void setDes_campo4(String des_campo4) {
        this.des_campo4 = des_campo4.trim();
    }

    public String getDes_campo5() {
        return this.des_campo5;
    }

    public void setDes_campo5(String des_campo5) {
        this.des_campo5 = des_campo5.trim();
    }

    public String getDes_campo6() {
        return this.des_campo6;
    }

    public void setDes_campo6(String des_campo6) {
        this.des_campo6 = des_campo6.trim();
    }

    public String getDes_campo7() {
        return this.des_campo7;
    }

    public void setDes_campo7(String des_campo7) {
        this.des_campo7 = des_campo7.trim();
    }

    public String getDes_campo8() {
        return this.des_campo8;
    }

    public void setDes_campo8(String des_campo8) {
        this.des_campo8 = des_campo8.trim();
    }

    public String getDes_cep() {
        return this.des_cep;
    }

    public void setDes_cep(String des_cep) {
        this.des_cep = des_cep;
    }

    public String getDes_codBarras() {
        return this.des_codBarras;
    }

    public void setDes_codBarras(String des_codBarras) {
        this.des_codBarras = des_codBarras;
    }

    public String getRem_campo1() {
        return this.rem_campo1;
    }

    public void setRem_campo1(String rem_campo1) {
        this.rem_campo1 = rem_campo1.trim();
    }

    public String getRem_campo2() {
        return this.rem_campo2;
    }

    public void setRem_campo2(String rem_campo2) {
        this.rem_campo2 = rem_campo2.trim();
    }

    public String getRem_campo3() {
        return this.rem_campo3;
    }

    public void setRem_campo3(String rem_campo3) {
        this.rem_campo3 = rem_campo3.trim();
    }

    public String getRem_campo4() {
        return this.rem_campo4;
    }

    public void setRem_campo4(String rem_campo4) {
        this.rem_campo4 = rem_campo4.trim();
    }

    public String getRem_campo5() {
        return this.rem_campo5;
    }

    public void setRem_campo5(String rem_campo5) {
        this.rem_campo5 = rem_campo5.trim();
    }

    public String getRem_campo6() {
        return this.rem_campo6;
    }

    public void setRem_campo6(String rem_campo6) {
        this.rem_campo6 = rem_campo6.trim();
    }

    public String getRem_campo7() {
        return this.rem_campo7;
    }

    public void setRem_campo7(String rem_campo7) {
        this.rem_campo7 = rem_campo7.trim();
    }

    public String getRem_campo8() {
        return this.rem_campo8;
    }

    public void setRem_campo8(String rem_campo8) {
        this.rem_campo8 = rem_campo8.trim();
    }

    public String getRem_cep() {
        return this.rem_cep;
    }

    public void setRem_cep(String rem_cep) {
        this.rem_cep = rem_cep;
    }

    public String getRem_CodBarras() {
        return this.rem_codBarras;
    }

    public void setRem_CodBarras(String rem_codBarras) {
        this.rem_codBarras = rem_codBarras;
    }

    public void organizaCampos() {
        organizaCampos(1);
    }

    public void organizaCampos(int tipo) {
        if (tipo == 2) {
            organizaCamposEncomendas();
        } else {
            organizaCamposTodos();
        }
    }

    public void organizaCamposTodos() {
        if (this.rem_campo7 == null || this.rem_campo7.trim().equals("")) {
            this.rem_campo7 = this.rem_campo8;
            this.rem_campo8 = null;
        }
        if (this.rem_campo6 == null || this.rem_campo6.trim().equals("")) {
            this.rem_campo6 = this.rem_campo7;
            this.rem_campo7 = this.rem_campo8;
            this.rem_campo8 = null;
        }
        if (this.rem_campo5 == null || this.rem_campo5.trim().equals("")) {
            this.rem_campo5 = this.rem_campo6;
            this.rem_campo6 = this.rem_campo7;
            this.rem_campo7 = this.rem_campo8;
            this.rem_campo8 = null;
        }
        if (this.rem_campo4 == null || this.rem_campo4.trim().equals("")) {
            this.rem_campo4 = this.rem_campo5;
            this.rem_campo5 = this.rem_campo6;
            this.rem_campo6 = this.rem_campo7;
            this.rem_campo7 = this.rem_campo8;
            this.rem_campo8 = null;
        }
        if (this.rem_campo3 == null || this.rem_campo3.trim().equals("")) {
            this.rem_campo3 = this.rem_campo4;
            this.rem_campo4 = this.rem_campo5;
            this.rem_campo5 = this.rem_campo6;
            this.rem_campo6 = this.rem_campo7;
            this.rem_campo7 = this.rem_campo8;
            this.rem_campo8 = null;
        }
        if (this.rem_campo2 == null || this.rem_campo2.trim().equals("")) {
            this.rem_campo2 = this.rem_campo3;
            this.rem_campo3 = this.rem_campo4;
            this.rem_campo4 = this.rem_campo5;
            this.rem_campo5 = this.rem_campo6;
            this.rem_campo6 = this.rem_campo7;
            this.rem_campo7 = this.rem_campo8;
            this.rem_campo8 = null;
        }
        if (this.rem_campo1 == null || this.rem_campo1.trim().equals("")) {
            this.rem_campo1 = this.rem_campo2;
            this.rem_campo2 = this.rem_campo3;
            this.rem_campo3 = this.rem_campo4;
            this.rem_campo4 = this.rem_campo5;
            this.rem_campo5 = this.rem_campo6;
            this.rem_campo6 = this.rem_campo7;
            this.rem_campo7 = this.rem_campo8;
            this.rem_campo8 = null;
        }
        if (this.des_campo7 == null || this.des_campo7.trim().equals("")) {
            this.des_campo7 = this.des_campo8;
            this.des_campo8 = null;
        }
        if (this.des_campo6 == null || this.des_campo6.trim().equals("")) {
            this.des_campo6 = this.des_campo7;
            this.des_campo7 = this.des_campo8;
            this.des_campo8 = null;
        }
        if (this.des_campo5 == null || this.des_campo5.trim().equals("")) {
            this.des_campo5 = this.des_campo6;
            this.des_campo6 = this.des_campo7;
            this.des_campo7 = this.des_campo8;
            this.des_campo8 = null;
        }
        if (this.des_campo4 == null || this.des_campo4.trim().equals("")) {
            this.des_campo4 = this.des_campo5;
            this.des_campo5 = this.des_campo6;
            this.des_campo6 = this.des_campo7;
            this.des_campo7 = this.des_campo8;
            this.des_campo8 = null;
        }
        if (this.des_campo3 == null || this.des_campo3.trim().equals("")) {
            this.des_campo3 = this.des_campo4;
            this.des_campo4 = this.des_campo5;
            this.des_campo5 = this.des_campo6;
            this.des_campo6 = this.des_campo7;
            this.des_campo7 = this.des_campo8;
            this.des_campo8 = null;
        }
        if (this.des_campo2 == null || this.des_campo2.trim().equals("")) {
            this.des_campo2 = this.des_campo3;
            this.des_campo3 = this.des_campo4;
            this.des_campo4 = this.des_campo5;
            this.des_campo5 = this.des_campo6;
            this.des_campo6 = this.des_campo7;
            this.des_campo7 = this.des_campo8;
            this.des_campo8 = null;
        }
        if (this.des_campo1 == null || this.des_campo1.trim().equals("")) {
            this.des_campo1 = this.des_campo2;
            this.des_campo2 = this.des_campo3;
            this.des_campo3 = this.des_campo4;
            this.des_campo4 = this.des_campo5;
            this.des_campo5 = this.des_campo6;
            this.des_campo6 = this.des_campo7;
            this.des_campo7 = this.des_campo8;
            this.des_campo8 = null;
        }
    }

    public void organizaCamposEncomendas() {
        if (this.rem_campo5 == null || this.rem_campo5.trim().equals("")) {
            this.rem_campo5 = this.rem_campo8;
            this.rem_campo8 = null;
        }
        if (this.rem_campo4 == null || this.rem_campo4.trim().equals("")) {
            this.rem_campo4 = this.rem_campo5;
            this.rem_campo5 = this.rem_campo8;
            this.rem_campo8 = null;
        }
        if (this.rem_campo3 == null || this.rem_campo3.trim().equals("")) {
            this.rem_campo3 = this.rem_campo4;
            this.rem_campo4 = this.rem_campo5;
            this.rem_campo5 = this.rem_campo8;
            this.rem_campo8 = null;
        }
        if (this.rem_campo2 == null || this.rem_campo2.trim().equals("")) {
            this.rem_campo2 = this.rem_campo3;
            this.rem_campo3 = this.rem_campo4;
            this.rem_campo4 = this.rem_campo5;
            this.rem_campo5 = this.rem_campo8;
            this.rem_campo8 = null;
        }
        if (this.rem_campo1 == null || this.rem_campo1.trim().equals("")) {
            this.rem_campo1 = this.rem_campo2;
            this.rem_campo2 = this.rem_campo3;
            this.rem_campo3 = this.rem_campo4;
            this.rem_campo4 = this.rem_campo5;
            this.rem_campo5 = this.rem_campo8;
            this.rem_campo8 = null;
        }
        if (this.des_campo5 == null || this.des_campo5.trim().equals("")) {
            this.des_campo5 = this.des_campo8;
            this.des_campo8 = null;
        }
        if (this.des_campo4 == null || this.des_campo4.trim().equals("")) {
            this.des_campo4 = this.des_campo5;
            this.des_campo5 = this.des_campo8;
            this.des_campo8 = null;
        }
        if (this.des_campo3 == null || this.des_campo3.trim().equals("")) {
            this.des_campo3 = this.des_campo4;
            this.des_campo4 = this.des_campo5;
            this.des_campo5 = this.des_campo8;
            this.des_campo8 = null;
        }
        if (this.des_campo2 == null || this.des_campo2.trim().equals("")) {
            this.des_campo2 = this.des_campo3;
            this.des_campo3 = this.des_campo4;
            this.des_campo4 = this.des_campo5;
            this.des_campo5 = this.des_campo8;
            this.des_campo8 = null;
        }
        if (this.des_campo1 == null || this.des_campo1.trim().equals("")) {
            this.des_campo1 = this.des_campo2;
            this.des_campo2 = this.des_campo3;
            this.des_campo3 = this.des_campo4;
            this.des_campo4 = this.des_campo5;
            this.des_campo5 = this.des_campo8;
            this.des_campo8 = null;
        }
    }

    public String getDesMaoPropria() {
        return this.desMaoPropria;
    }

    public void setDesMaoPropria(String desMaoPropria) {
        this.desMaoPropria = desMaoPropria;
    }

    public String getDesConteudo() {
        return this.desConteudo;
    }

    public void setDesConteudo(String desConteudo) {
        this.desConteudo = desConteudo;
    }

    public String getCodigoDoisD() {
        return this.codigoDoisD;
    }

    public void setCodigoDoisD(String codigoDoisD) {
        this.codigoDoisD = codigoDoisD;
    }

    public String getDes_entrega() {
        return this.des_entrega;
    }

    public void setDes_entrega(String des_entrega) {
        this.des_entrega = des_entrega;
    }
}
