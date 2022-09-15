package br.com.correios.enderecador.bean

import java.io.Serializable

data class DeclaracaoBean(
    var nomeRemetente: String,
    var enderecoRemetente: String,
    var endereco2Remetente: String,
    var cidadeRemetente: String,
    var ufRemetente: String,
    var cepRemetente: String,
    var cpfRemetente: String,
    var nomeDestinatario: String,
    var enderecoDestinatario: String,
    var endereco2Destinatario: String,
    var cidadeDestinatario: String,
    var ufDestinatario: String,
    var cepDestinatario: String,
    var cpfDestinatario: String
) : Serializable {
    var numeroItem1: String? = null
    var numeroItem2: String? = null
    var numeroItem3: String? = null
    var numeroItem4: String? = null
    var numeroItem5: String? = null
    var numeroItem6: String? = null
    var conteudoItem1: String? = null
    var conteudoItem2: String? = null
    var conteudoItem3: String? = null
    var conteudoItem4: String? = null
    var conteudoItem5: String? = null
    var conteudoItem6: String? = null
    var quantidadeItem1: String? = null
    var quantidadeItem2: String? = null
    var quantidadeItem3: String? = null
    var quantidadeItem4: String? = null
    var quantidadeItem5: String? = null
    var quantidadeItem6: String? = null
    var valorItem1: String? = null
    var valorItem2: String? = null
    var valorItem3: String? = null
    var valorItem4: String? = null
    var valorItem5: String? = null
    var valorItem6: String? = null
    var quantidadeTotal: String? = null
    var valorTotal: String? = null
    var pesoTotal: String? = null
}
