package br.com.correios.enderecador.model

data class Address(
    val street: String,
    val number: Int,
    val complement: String,
    val district: String,
    val city: String,
    val state: String,
    val zipCode: String
)
