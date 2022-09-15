package br.com.correios.enderecador.util

enum class FontSize(val description: String) {
    SMALL("Pequeno"),
    MEDIUM("Médio"),
    LARGER("Grande");

    override fun toString() = description
}