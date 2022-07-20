package br.com.correios.enderecador.bean;

public class GrupoDestinatarioBean {
    private String grp_nu;

    private String des_nu;

    private static GrupoDestinatarioBean grupoDestinatarioBean;

    public static GrupoDestinatarioBean getInstance() {
        if (grupoDestinatarioBean == null)
            grupoDestinatarioBean = new GrupoDestinatarioBean();
        return grupoDestinatarioBean;
    }

    public String getNumeroGrupo() {
        return this.grp_nu;
    }

    public void setNumeroGrupo(String numero) {
        this.grp_nu = numero;
    }

    public String getNumeroDestinatario() {
        return this.des_nu;
    }

    public void setNumeroDestinatario(String numero) {
        this.des_nu = numero;
    }
}
