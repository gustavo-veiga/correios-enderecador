package br.com.correios.enderecador.util

import java.awt.Toolkit
import javax.swing.text.PlainDocument
import javax.swing.text.BadLocationException
import javax.swing.text.AttributeSet

class DocumentoPersonalizado(private val maxLength: Int, private val tipoFormatacao: Int) : PlainDocument() {
    @Throws(BadLocationException::class)
    override fun insertString(offs: Int, str: String?, a: AttributeSet?) {
        if (length == maxLength) {
            Toolkit.getDefaultToolkit().beep()
        } else if (str != null) {
            val text = if (length + str.length > maxLength) {
                str.substring(0, maxLength - length)
            } else {
                str
            }

            when (tipoFormatacao) {
                0 -> {
                    super.insertString(offs, text.uppercase(), a)
                }
                1 -> {
                    super.insertString(offs, text.digits(), a)
                }
                2 -> {
                    val result = if (text.length == 10) {
                        text
                    } else {
                        val digits = text.digits()
                        if (length + digits.length == 2) {
                            "$digits/"
                        } else if (length + digits.length == 5) {
                            "$digits/"
                        } else {
                            digits
                        }
                    }
                    super.insertString(offs, result, a)
                }
                3 -> {
                    var offset = offs
                    val result = if (text.length == 14 || text.length == 13) {
                        text
                    } else {
                        val digits = text.digits()
                        if (length + digits.length == 1) {
                           "($digits"
                        } else if (length + digits.length == 3) {
                            "$digits) "
                        } else if (length + digits.length == 5) {
                            digits
                        } else if (length + digits.length == 9) {
                           "-$digits"
                        } else if (length + digits.length == 14) {
                            offset = 0
                            getText(0, length)
                                .mapIndexed { index, digit ->
                                if (digit != ' ' && digit != '-') {
                                    digit
                                    if (index == 9) {
                                        "-"
                                    }
                                }
                            }.joinToString() + digits.also { remove(0, length) }
                        } else if (length + digits.length > 14) {
                            remove(14, length)
                            ""
                        } else {
                            digits
                        }
                    }
                    super.insertString(offset, result, a)
                }
                4 -> {
                    val result = if (text.length == 10) {
                        text
                    } else {
                        val digits = text.digits()
                        if (length + digits.length == 2) {
                            "$digits."
                        } else if (length + digits.length == 7) {
                            "-$digits"
                        } else {
                            digits
                        }
                    }
                    super.insertString(offs, result, a)
                }
                5 -> {
                    super.insertString(offs, str, a)
                }
            }
        }
    }
}
