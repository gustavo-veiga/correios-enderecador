package br.com.correios.enderecador.util

fun String.isNumeric() = this.all { it.isDigit() }

fun String.digits() = this.filter { it.isDigit() }

fun String.maskZipCode(): String {
    if (this.length == 8) {
        return this.substring(0, 5) + "-" + this.substring(5, 8)
    }
    return this
}
