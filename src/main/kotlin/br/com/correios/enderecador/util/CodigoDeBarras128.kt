package br.com.correios.enderecador.util

import br.com.correios.enderecador.exception.EnderecadorExcecao
import java.lang.StringBuilder
import kotlin.jvm.JvmStatic

class CodigoDeBarras128 {
    private val tabelaCode128AB = arrayOf(
        "®", "!", '"'.toString(), "#", "$", "%", "&", "", "(", ")",
        "*", "+", ",", "-", ".", "/", "0", "1", "2", "3",
        "4", "5", "6", "7", "8", "9", ":", ";", "<", "=",
        ">", "?", "@", "A", "B", "C", "D", "E", "F", "G",
        "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
        "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "[",
        "\\", "]", "^", "_", "`", "a", "b", "c", "d", "e",
        "f", "g", "h", "i", "j", "k", "l", "m", "n", "o",
        "p", "q", "r", "s", "t", "u", "v", "x", "w", "y",
        "z", "¡", "¢", "£", "¤", "¥", "¦", "§", "¨", "©",
        "ª", "«", "¬", "{", "|", "}"
    )
    private val tabelaCode128C = arrayOf(
        "!", '"'.toString(), "#", "$", "%", "&", "'", "(", ")", "*",
        "+", ",", "-", ".", "/", "0", "1", "2", "3", "4",
        "5", "6", "7", "8", "9", ":", ";", "<", "=", ">",
        "?", "@", "A", "B", "C", "D", "E", "F", "G", "H",
        "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
        "S", "T", "U", "V", "W", "X", "Y", "Z", "[", "\\",
        "]", "^", "_", "`", "a", "b", "c", "d", "e", "f",
        "g", "h", "i", "j", "k", "l", "m", "n", "o", "p",
        "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
        "¡", "¢", "£", "¤", "¥", "¦", "§", "¨", "©", "ª",
        "°", "±", "²", "{", "|", "}"
    )
    private val tabelaCode128 = arrayOf(
        "2,1,2,2,2,2,",
        "2,2,2,1,2,2,",
        "2,2,2,2,2,1,",
        "1,2,1,2,2,3,",
        "1,2,1,3,2,2,",
        "1,3,1,2,2,2,",
        "1,2,2,2,1,3,",
        "1,2,2,3,1,2,",
        "1,3,2,2,1,2,",
        "2,2,1,2,1,3,",
        "2,2,1,3,1,2,",
        "2,3,1,2,1,2,",
        "1,1,2,2,3,2,",
        "1,2,2,1,3,2,",
        "1,2,2,2,3,1,",
        "1,1,3,2,2,2,",
        "1,2,3,1,2,2,",
        "1,2,3,2,2,1,",
        "2,2,3,2,1,1,",
        "2,2,1,1,3,2,",
        "2,2,1,2,3,1,",
        "2,1,3,2,1,2,",
        "2,2,3,1,1,2,",
        "3,1,2,1,3,1,",
        "3,1,1,2,2,2,",
        "3,2,1,1,2,2,",
        "3,2,1,2,2,1,",
        "3,1,2,2,1,2,",
        "3,2,2,1,1,2,",
        "3,2,2,2,1,1,",
        "2,1,2,1,2,3,",
        "2,1,2,3,2,1,",
        "2,3,2,1,2,1,",
        "1,1,1,3,2,3,",
        "1,3,1,1,2,3,",
        "1,3,1,3,2,1,",
        "1,1,2,3,1,3,",
        "1,3,2,1,1,3,",
        "1,3,2,3,1,1,",
        "2,1,1,3,1,3,",
        "2,3,1,1,1,3,",
        "2,3,1,3,1,1,",
        "1,1,2,1,3,3,",
        "1,1,2,3,3,1,",
        "1,3,2,1,3,1,",
        "1,1,3,1,2,3,",
        "1,1,3,3,2,1,",
        "1,3,3,1,2,1,",
        "3,1,3,1,2,1,",
        "2,1,1,3,3,1,",
        "2,3,1,1,3,1,",
        "2,1,3,1,1,3,",
        "2,1,3,3,1,1,",
        "2,1,3,1,3,1,",
        "3,1,1,1,2,3,",
        "3,1,1,3,2,1,",
        "3,3,1,1,2,1,",
        "3,1,2,1,1,3,",
        "3,1,2,3,1,1,",
        "3,3,2,1,1,1,",
        "3,1,4,1,1,1,",
        "2,2,1,4,1,1,",
        "4,3,1,1,1,1,",
        "1,1,1,2,2,4,",
        "1,1,1,4,2,2,",
        "1,2,1,1,2,4,",
        "1,2,1,4,2,1,",
        "1,4,1,1,2,2,",
        "1,4,1,2,2,1,",
        "1,1,2,2,1,4,",
        "1,1,2,4,1,2,",
        "1,2,2,1,1,4,",
        "1,2,2,4,1,1,",
        "1,4,2,1,1,2,",
        "1,4,2,2,1,1,",
        "2,4,1,2,1,1,",
        "2,2,1,1,1,4,",
        "4,1,3,1,1,1,",
        "2,4,1,1,1,2,",
        "1,3,4,1,1,1,",
        "1,1,1,2,4,2,",
        "1,2,1,1,4,2,",
        "1,2,1,2,4,1,",
        "1,1,4,2,1,2,",
        "1,2,4,1,1,2,",
        "1,2,4,2,1,1,",
        "4,1,1,2,1,2,",
        "4,2,1,1,1,2,",
        "4,2,1,2,1,1,",
        "2,1,2,1,4,1,",
        "2,1,4,1,2,1,",
        "4,1,2,1,2,1,",
        "1,1,1,1,4,3,",
        "1,1,1,3,4,1,",
        "1,3,1,1,4,1,",
        "1,1,4,1,1,3,",
        "1,1,4,3,1,1,",
        "4,1,1,1,1,3,",
        "4,1,1,3,1,1,",
        "1,1,3,1,4,1,",
        "1,1,4,1,3,1,",
        "3,1,1,1,4,1,",
        "4,1,1,1,3,1,",
        "2,1,1,4,1,2,",
        "2,1,1,2,1,4,",
        "2,1,1,2,3,2,",
        "2,3,3,1,1,1,2"
    )

    @Throws(EnderecadorExcecao::class)
    fun gerarCodigoDeBarra(tipoCodigoDeBarras: Int, informacao: String?): String {
        var informacao = informacao
        val codigoDeBarras = StringBuilder()
        val strTmp = StringBuilder()
        var indiceTmp: String
        var largura = 1
        var indice: Int
        var numCaracteres: Int
        if (informacao == null || informacao == "") throw EnderecadorExcecao("A informação deve ser fornecida!")
        if (informacao.length % 2 != 0) informacao = "0$informacao"
        if (tipoCodigoDeBarras == TIPO_128_AB) {
            indice = 103
        } else if (tipoCodigoDeBarras == TIPO_128_C) {
            indice = 105
            if (informacao.isNotNumeric()) {
                throw EnderecadorExcecao("O código de barras CODE_128C deve ser numérico!")
            }
        } else {
            throw EnderecadorExcecao("Tipo de código de barras inválido!")
        }
        codigoDeBarras.append(getCaractereTabela(tipoCodigoDeBarras, indice))
        if (tipoCodigoDeBarras == 2) {
            var j = 0
            while (j < informacao.length) {
                numCaracteres = j + 2
                if (numCaracteres > informacao.length) numCaracteres = informacao.length
                indiceTmp = informacao.substring(j, numCaracteres)
                strTmp.append(getCaractereTabela(tipoCodigoDeBarras, indiceTmp))
                j += 2
            }
            informacao = strTmp.toString()
        }
        for (i in informacao.indices) {
            indice += largura * getIndiceTabela(tipoCodigoDeBarras, informacao.substring(i, i + 1))
            largura++
        }
        indice %= 103
        codigoDeBarras.append(informacao)
        codigoDeBarras.append(getCaractereTabela(tipoCodigoDeBarras, indice))
        codigoDeBarras.append("~")
        return codigoDeBarras.toString()
    }

    fun gerarCodigoDeBarra128(tipoCodigoDeBarra: Int, cep: String): String {
        val codCep = StringBuilder()
        for (i in 0 until cep.length - 1) {
            codCep.append(tabelaCode128[getIndiceTabela(
                    tipoCodigoDeBarra,
                    cep.substring(i, i + 1)
                )]
            )
        }
        codCep.append(tabelaCode128[tabelaCode128.size - 1])
        return codCep.toString()
    }

    private fun getCaractereTabela(tipoCodigoDeBarra: Int, indice: String): String {
        return getCaractereTabela(tipoCodigoDeBarra, indice.toInt())
    }

    private fun getCaractereTabela(tipoCodigoDeBarra: Int, indice: Int): String {
        return if (tipoCodigoDeBarra == 1) {
            tabelaCode128AB[indice]
        } else {
            tabelaCode128C[indice]
        }
    }

    private fun getIndiceTabela(tipoCodigoDeBarra: Int, caractere: String): Int {
        var indice = -1
        val tabela = if (tipoCodigoDeBarra == 1) {
            tabelaCode128AB
        } else {
            tabelaCode128C
        }

        for (i in tabela.indices) {
            if (tabela[i] == caractere) {
                indice = i
                break
            }
        }
        return indice
    }

    companion object {
        const val TIPO_128_AB = 1
        const val TIPO_128_C = 2
        @Throws(EnderecadorExcecao::class)
        @JvmStatic
        fun main(arg: Array<String>) {
            val codigoDeBarras128 = CodigoDeBarras128()
            codigoDeBarras128.gerarCodigoDeBarra(2, "72110025")
        }
    }
}
