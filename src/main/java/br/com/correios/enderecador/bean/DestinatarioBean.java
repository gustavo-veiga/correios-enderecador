package br.com.correios.enderecador.bean;

import java.util.Comparator;

public class DestinatarioBean implements Comparator {
    private String des_nu = "";

    private String des_tx_apelido = "";

    private String des_tx_titulo = "";

    private String des_tx_nome = "";

    private String des_ed_cep;

    private String des_ed_endereco = "";

    private String des_ed_numero = "";

    private String des_ed_complemento = "";

    private String des_ed_bairro = "";

    private String des_ed_cidade = "";

    private String des_ed_uf = "";

    private String des_ed_email = "";

    private String des_ed_telefone = "";

    private String des_ed_fax = "";

    private String des_ed_cep_caixa_postal = "";

    private String des_ed_caixa_postal = "";

    private String des_quantidade_impressao = "1";

    private String des_mao_propria = "Não";

    private String desConteudo = "";

    private String desEntregaVizinho = "";

    private static DestinatarioBean destinatarioBean;

    public static DestinatarioBean getInstance() {
        if (destinatarioBean == null)
            destinatarioBean = new DestinatarioBean();
        return destinatarioBean;
    }

    public String getNumeroDestinatario() {
        return this.des_nu;
    }

    public void setNumeroDestinatario(String numero) {
        this.des_nu = numero;
    }

    public String getQuantidade() {
        return this.des_quantidade_impressao;
    }

    public void setQuantidade(String quantidade) {
        this.des_quantidade_impressao = quantidade;
    }

    public String getApelido() {
        return this.des_tx_apelido;
    }

    public void setApelido(String apelido) {
        this.des_tx_apelido = apelido;
    }

    public String getTitulo() {
        return this.des_tx_titulo;
    }

    public void setTitulo(String titulo) {
        this.des_tx_titulo = titulo;
    }

    public String getNome() {
        return this.des_tx_nome;
    }

    public void setNome(String nome) {
        this.des_tx_nome = nome;
    }

    public String getCep() {
        return this.des_ed_cep;
    }

    public void setCep(String cep) {
        this.des_ed_cep = cep;
    }

    public String getEndereco() {
        return this.des_ed_endereco;
    }

    public void setEndereco(String endereco) {
        this.des_ed_endereco = endereco;
    }

    public String getNumeroEndereco() {
        return this.des_ed_numero;
    }

    public void setNumeroEndereco(String numero) {
        this.des_ed_numero = numero;
    }

    public String getComplemento() {
        return this.des_ed_complemento;
    }

    public void setComplemento(String complemento) {
        this.des_ed_complemento = complemento;
    }

    public String getBairro() {
        return this.des_ed_bairro;
    }

    public void setBairro(String bairro) {
        this.des_ed_bairro = bairro;
    }

    public String getCidade() {
        return this.des_ed_cidade;
    }

    public void setCidade(String cidade) {
        this.des_ed_cidade = cidade;
    }

    public String getUf() {
        return this.des_ed_uf;
    }

    public void setUf(String uf) {
        this.des_ed_uf = uf;
    }

    public String getEmail() {
        return this.des_ed_email;
    }

    public void setEmail(String email) {
        this.des_ed_email = email;
    }

    public String getTelefone() {
        return this.des_ed_telefone;
    }

    public void setTelefone(String telefone) {
        this.des_ed_telefone = telefone;
    }

    public String getFax() {
        return this.des_ed_fax;
    }

    public void setFax(String fax) {
        this.des_ed_fax = fax;
    }

    public String getCepCaixaPostal() {
        return this.des_ed_cep_caixa_postal;
    }

    public void setCepCaixaPostal(String cepCaixaPostal) {
        this.des_ed_cep_caixa_postal = cepCaixaPostal;
    }

    public String getCaixaPostal() {
        return this.des_ed_caixa_postal;
    }

    public void setCaixaPostal(String caixaPostal) {
        this.des_ed_caixa_postal = caixaPostal;
    }

    public String getMaoPropria() {
        return this.des_mao_propria;
    }

    public void setMaoPropria(String maoPropria) {
        this.des_mao_propria = maoPropria;
    }

    public String getDesConteudo() {
        return this.desConteudo;
    }

    public void setDesConteudo(String conteudo) {
        this.desConteudo = conteudo;
    }

    public String getDesEntregaVizinho() {
        return this.desEntregaVizinho;
    }

    public void setDesEntregaVizinho(String desEntregaVizinho) {
        this.desEntregaVizinho = desEntregaVizinho;
    }

    public String toString() {
        return this.des_tx_nome;
    }

    public int compare(Object obj1, Object obj2) {
        DestinatarioBean destinatarioBean1 = (DestinatarioBean) obj1;
        DestinatarioBean destinatarioBean2 = (DestinatarioBean) obj2;
        String vNome1 = destinatarioBean1.getNome().replaceAll("[ÁáÀaÂâÃã]", "A").replaceAll("[ÉéÈèÊê]", "E").replaceAll("[ÍíÌìÎî]", "I").replaceAll("[ÓóÒòÔôÕõ]", "O").replaceAll("[ÚúÙù]", "U");
        String vNome2 = destinatarioBean2.getNome().replaceAll("[ÁáÀaÂâÃã]", "A").replaceAll("[ÉéÈèÊê]", "E").replaceAll("[ÍíÌìÎî]", "I").replaceAll("[ÓóÒòÔôÕõ]", "O").replaceAll("[ÚúÙù]", "U");
        return vNome1.compareToIgnoreCase(vNome2);
    }

    public boolean equals(Object other) {
        if (other instanceof DestinatarioBean) {
            DestinatarioBean u = (DestinatarioBean) other;
            return getNumeroDestinatario().equals(u.getNumeroDestinatario());
        }
        return false;
    }

    public int hashCode() {
        return getNumeroDestinatario().hashCode() * 17;
    }

    public boolean validaEmail() {
        boolean result = false;
        int contArroba = 0;
        if (this.des_ed_email.equals("") || this.des_ed_email.length() < 5) {
        } else {
            for (int i = 0; i < this.des_ed_email.length(); i++) {
                if (this.des_ed_email.charAt(i) == '@')
                    contArroba++;
            }
            if (contArroba == 1) {
                result = true;
            }
        }
        return result;
    }
}
