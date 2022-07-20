package br.com.correios.enderecador.cep.gpbe.bean;

public class CepBean {
    private String bairro = "";

    private String localidade = "";

    private String logradouro = "";

    private String uf = "";

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getBairro() {
        return this.bairro;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public String getLocalidade() {
        return this.localidade;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getLogradouro() {
        return this.logradouro;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getUf() {
        return this.uf;
    }

    public String toString() {
        return "<?xml version='1.0' encoding='ISO-8859-1' ?><CEPS><CEP><VUF>" + this.uf + "</VUF>" + "<VLOC_NO>" + this.localidade + "</VLOC_NO>" + "<VLOG_NO_DNEC>" + this.logradouro + "</VLOG_NO_DNEC>" + "<VBAIRRO>" + this.bairro + "</VBAIRRO>" + "</CEP>" + "</CEPS>";
    }
}
