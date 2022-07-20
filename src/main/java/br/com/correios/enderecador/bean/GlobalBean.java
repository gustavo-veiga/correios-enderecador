package br.com.correios.enderecador.bean;

public class GlobalBean {
    private static GlobalBean globalBean;

    private String mostraMensagem = "SIM";

    public static GlobalBean getInstance() {
        if (globalBean == null)
            globalBean = new GlobalBean();
        return globalBean;
    }

    public String getMostraMensagem() {
        return this.mostraMensagem;
    }

    public void setMostraMensagem(String mostraMensagem) {
        this.mostraMensagem = mostraMensagem;
    }
}
