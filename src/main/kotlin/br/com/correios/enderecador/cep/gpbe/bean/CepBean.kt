package br.com.correios.enderecador.cep.gpbe.bean

class CepBean {
    var bairro = ""
    var localidade = ""
    var logradouro = ""
    var uf = ""

    override fun toString(): String {
        return "<?xml version='1.0' encoding='ISO-8859-1' ?><CEPS><CEP><VUF>$uf</VUF><VLOC_NO>$localidade</VLOC_NO><VLOG_NO_DNEC>$logradouro</VLOG_NO_DNEC><VBAIRRO>$bairro</VBAIRRO></CEP></CEPS>"
    }
}
