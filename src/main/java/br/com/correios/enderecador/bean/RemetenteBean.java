package br.com.correios.enderecador.bean;

import java.io.Serializable;

public class RemetenteBean implements Serializable {
    private String rem_nu;

    private String rem_tx_apelido;

    private String rem_tx_titulo;

    private String rem_tx_nome;

    private String rem_ed_cep;

    private String rem_ed_endereco;

    private String rem_ed_numero;

    private String rem_ed_complemento;

    private String rem_ed_bairro;

    private String rem_ed_cidade;

    private String rem_ed_uf;

    private String rem_ed_email;

    private String rem_ed_telefone;

    private String rem_ed_fax;

    private String rem_ed_cep_caixa_postal;

    private String rem_ed_caixa_postal;

    private static RemetenteBean remetenteBean;

    public static RemetenteBean getInstance() {
        if (remetenteBean == null)
            remetenteBean = new RemetenteBean();
        return remetenteBean;
    }

    public boolean equals(Object other) {
        if (other instanceof RemetenteBean) {
            RemetenteBean u = (RemetenteBean) other;
            return getNumeroRemetente().equals(u.getNumeroRemetente());
        }
        return false;
    }

    public int hashCode() {
        return getNumeroRemetente().hashCode() * 17;
    }

    public String getNumeroRemetente() {
        return this.rem_nu;
    }

    public void setNumeroRemetente(String numero) {
        this.rem_nu = numero;
    }

    public String getApelido() {
        return this.rem_tx_apelido;
    }

    public void setApelido(String apelido) {
        this.rem_tx_apelido = apelido;
    }

    public String getTitulo() {
        return this.rem_tx_titulo;
    }

    public void setTitulo(String titulo) {
        this.rem_tx_titulo = titulo;
    }

    public String getNome() {
        return this.rem_tx_nome;
    }

    public void setNome(String nome) {
        this.rem_tx_nome = nome;
    }

    public String getCep() {
        return this.rem_ed_cep;
    }

    public void setCep(String cep) {
        this.rem_ed_cep = cep;
    }

    public String getEndereco() {
        return this.rem_ed_endereco;
    }

    public void setEndereco(String endereco) {
        this.rem_ed_endereco = endereco;
    }

    public String getNumeroEndereco() {
        return this.rem_ed_numero;
    }

    public void setNumeroEndereco(String numero) {
        this.rem_ed_numero = numero;
    }

    public String getComplemento() {
        return this.rem_ed_complemento;
    }

    public void setComplemento(String complemento) {
        this.rem_ed_complemento = complemento;
    }

    public String getBairro() {
        return this.rem_ed_bairro;
    }

    public void setBairro(String bairro) {
        this.rem_ed_bairro = bairro;
    }

    public String getCidade() {
        return this.rem_ed_cidade;
    }

    public void setCidade(String cidade) {
        this.rem_ed_cidade = cidade;
    }

    public String getUf() {
        return this.rem_ed_uf;
    }

    public void setUf(String uf) {
        this.rem_ed_uf = uf;
    }

    public String getEmail() {
        return this.rem_ed_email;
    }

    public void setEmail(String email) {
        this.rem_ed_email = email;
    }

    public String getTelefone() {
        return this.rem_ed_telefone;
    }

    public void setTelefone(String telefone) {
        this.rem_ed_telefone = telefone;
    }

    public String getFax() {
        return this.rem_ed_fax;
    }

    public void setFax(String fax) {
        this.rem_ed_fax = fax;
    }

    public String getCepCaixaPostal() {
        return this.rem_ed_cep_caixa_postal;
    }

    public void setCepCaixaPostal(String cepCaixaPostal) {
        this.rem_ed_cep_caixa_postal = cepCaixaPostal;
    }

    public String getCaixaPostal() {
        return this.rem_ed_caixa_postal;
    }

    public void setCaixaPostal(String caixaPostal) {
        this.rem_ed_caixa_postal = caixaPostal;
    }

    public String toString() {
        return this.rem_tx_nome;
    }

    public boolean validaEmail() {
        int contArroba = 0;
        if (this.rem_ed_email.equals("") || this.rem_ed_email.length() < 5)
            return false;
        for (int i = 0; i < this.rem_ed_email.length(); i++) {
            if (this.rem_ed_email.charAt(i) == '@')
                contArroba++;
        }
        return contArroba == 1;
    }
}
