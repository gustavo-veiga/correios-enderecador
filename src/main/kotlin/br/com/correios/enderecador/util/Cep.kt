package br.com.correios.enderecador.util

import java.lang.StringBuilder

object Cep {
    fun validaCep(cep: String): Boolean {
        val novoCep = StringBuilder()
        var retorno = true
        if (cep.length < 8) {
            retorno = false
        } else {
            for (element in cep) {
                if (Character.isDigit(element)) {
                    novoCep.append(element)
                }
            }
            if (novoCep.length != 8) {
                retorno = false
            }
        }
        return retorno
    }

    fun tiraMascaraCep(cep: String): String {
        val novoCep = StringBuilder()
        for (element in cep) {
            if (Character.isDigit(element)) {
                novoCep.append(element)
            }
        }
        return if (novoCep.length == 8) {
            novoCep.toString()
        } else ""
    }
}
