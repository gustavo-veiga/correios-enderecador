package br.com.correios.enderecador.bean

data class RemetenteBean(
    var numeroRemetente: String,
    var apelido: String,
    var titulo: String,
    var nome: String,
    var cep: String,
    var endereco: String,
    var numeroEndereco: String,
    var complemento: String,
    var bairro: String,
    var cidade: String,
    var uf: String,
    var email: String,
    var telefone: String,
    var fax: String,
    var cepCaixaPostal: String,
    var caixaPostal: String,
) {
    override fun equals(other: Any?): Boolean {
        if (other is RemetenteBean) {
            return numeroRemetente == other.numeroRemetente
        }
        return false
    }

    override fun hashCode(): Int {
        return numeroRemetente.hashCode() * 17
    }

    override fun toString(): String {
        return nome
    }
}
