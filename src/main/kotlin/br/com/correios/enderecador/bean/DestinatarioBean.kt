package br.com.correios.enderecador.bean

data class DestinatarioBean(
    var numeroDestinatario: String?,
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
    var quantidade: String = "1",
    var maoPropria: String = "Não",
    var desConteudo: String = "",
    var desEntregaVizinho: String = "",
) {
    override fun toString(): String {
        return nome
    }

    override fun equals(other: Any?): Boolean {
        if (other is DestinatarioBean) {
            return numeroDestinatario == other.numeroDestinatario
        }
        return false
    }

    override fun hashCode(): Int {
        return numeroDestinatario.hashCode() * 17
    }
}
