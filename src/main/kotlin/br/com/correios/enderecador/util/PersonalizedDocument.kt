package br.com.correios.enderecador.util

import br.com.correios.enderecador.util.TypeDocumentFormat.*
import java.awt.Toolkit
import javax.swing.text.PlainDocument
import javax.swing.text.BadLocationException
import javax.swing.text.AttributeSet

class PersonalizedDocument(private val maxLength: Int, private val format: TypeDocumentFormat) : PlainDocument() {
    @Throws(BadLocationException::class)
    override fun insertString(offs: Int, str: String?, a: AttributeSet?) {
        if (length >= maxLength) {
            Toolkit.getDefaultToolkit().beep()
        } else if (str != null) {
            when (format) {
                NONE -> super.insertString(offs, str, a)
                ONLY_DIGITS -> super.insertString(offs, str.digits(), a)
                UPPERCASE -> super.insertString(offs, str.uppercase(), a)
            }
        }
    }
}
