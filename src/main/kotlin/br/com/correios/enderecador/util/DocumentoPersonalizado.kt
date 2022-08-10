package br.com.correios.enderecador.util

import java.awt.Toolkit
import javax.swing.text.PlainDocument
import kotlin.Throws
import javax.swing.text.BadLocationException
import java.util.Locale
import java.lang.StringBuilder
import javax.swing.text.AttributeSet

class DocumentoPersonalizado(private val tamanhoMaximo: Int, private val tipoFormatacao: Int) : PlainDocument() {
    @Throws(BadLocationException::class)
    override fun insertString(offs: Int, novoTexto: String, a: AttributeSet) {
        var offs = offs
        var novoTexto: String? = novoTexto
        if (length == tamanhoMaximo) {
            Toolkit.getDefaultToolkit().beep()
        } else if (novoTexto != null) {
            if (length + novoTexto.length > tamanhoMaximo) novoTexto = novoTexto.substring(0, tamanhoMaximo - length)
            if (tipoFormatacao == 0) {
                novoTexto = novoTexto.uppercase(Locale.getDefault())
                super.insertString(offs, novoTexto, a)
            } else if (tipoFormatacao == 1) {
                val resultado = getDigitos(novoTexto)
                super.insertString(offs, resultado, a)
            } else if (tipoFormatacao == 5) {
                super.insertString(offs, novoTexto, a)
            } else if (tipoFormatacao == 2) {
                var resultado: String
                if (novoTexto.length == 10) {
                    resultado = novoTexto
                } else {
                    resultado = getDigitos(novoTexto)
                    if (length + resultado.length == 2) {
                        resultado = "$resultado/"
                    } else if (length + resultado.length == 5) {
                        resultado = "$resultado/"
                    }
                }
                super.insertString(offs, resultado, a)
            } else if (tipoFormatacao == 3) {
                var resultado: String
                if (novoTexto.length == 14 || novoTexto.length == 13) {
                    resultado = novoTexto
                } else {
                    resultado = getDigitos(novoTexto)
                    if (length + resultado.length == 1) {
                        resultado = "($resultado"
                    } else if (length + resultado.length == 3) {
                        resultado = "$resultado) "
                    } else if (length + resultado.length == 5) {
                        resultado = " $resultado"
                    } else if (length + resultado.length == 9) {
                        resultado = "-$resultado"
                    } else if (length + resultado.length == 14) {
                        val tel = getText(0, length)
                        val novoTel = StringBuilder()
                        var c: Char
                        for (i in 0 until tel.length) {
                            c = tel[i]
                            if (c != ' ' && c != '-') {
                                novoTel.append(c)
                                if (i == 9) novoTel.append("-")
                            }
                        }
                        resultado = novoTel.toString() + resultado
                        offs = 0
                        remove(0, length)
                    } else if (length + resultado.length > 14) {
                        remove(14, length)
                        resultado = ""
                    }
                }
                super.insertString(offs, resultado, a)
            } else if (tipoFormatacao == 4) {
                var resultado: String
                if (novoTexto.length == 10) {
                    resultado = novoTexto
                } else {
                    resultado = getDigitos(novoTexto)
                    if (length + resultado.length == 2) {
                        resultado = "$resultado."
                    } else if (length + resultado.length == 7) {
                        resultado = "-$resultado"
                    }
                }
                super.insertString(offs, resultado, a)
            }
        }
    }

    private fun getDigitos(texto: String): String {
        var c: Char
        val resultado = StringBuilder()
        for (element in texto) {
            c = element
            if (Character.isDigit(c)) resultado.append(c)
        }
        return resultado.toString()
    }
}
