package br.com.correios.enderecador.bean;

public class GrupoBean {
    private String grp_nu;

    private String grp_tx_descricao;

    private static GrupoBean grupoBean;

    public static GrupoBean getInstance() {
        if (grupoBean == null)
            grupoBean = new GrupoBean();
        return grupoBean;
    }

    public String getNumeroGrupo() {
        return this.grp_nu;
    }

    public void setNumeroGrupo(String numero) {
        this.grp_nu = numero;
    }

    public String getDescricaoGrupo() {
        return this.grp_tx_descricao;
    }

    public void setDescricaoGrupo(String descricao) {
        this.grp_tx_descricao = descricao;
    }

    public String toString() {
        return this.grp_tx_descricao;
    }
}
