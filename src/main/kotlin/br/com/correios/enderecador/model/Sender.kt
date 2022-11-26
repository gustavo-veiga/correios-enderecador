package br.com.correios.enderecador.model

data class Sender(
    val id: String?,
    var title: String,
    var name: String,
    var lastName: String,
    var email: String,
    var phone: String,
    var address: Address
)
