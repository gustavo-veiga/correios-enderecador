package br.com.correios.enderecador.util

fun String.isNumeric() = this.all { it.isDigit() }

fun String.isNotNumeric() = this.isNumeric().not()

fun String.digits() = this.filter { it.isDigit() }