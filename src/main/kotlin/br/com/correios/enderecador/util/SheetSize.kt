package br.com.correios.enderecador.util

enum class SheetSize(private val description: String) {
    A4("A4"),
    LETTER("Carta");

    override fun toString() = description
}