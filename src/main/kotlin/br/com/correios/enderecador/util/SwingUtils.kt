package br.com.correios.enderecador.util

import javax.swing.JFormattedTextField
import javax.swing.JList
import javax.swing.text.MaskFormatter

fun List<String>.toComboBox(): Array<String> {
    return this.toMutableList().apply {
        add(0, "")
    }.toTypedArray()
}

fun JFormattedTextField.maskZipCode() {
    MaskFormatter("#####-###").apply {
        validCharacters = "0123456789"
        placeholderCharacter = '_'
    }.install(this)
}

fun <T> JList<T>.getAllItems(): List<T> {
    val bucket = mutableListOf<T>()
    for (i in 0 until this.model.size) {
        bucket.add(this.model.getElementAt(i))
    }
    return bucket
}

fun <T> JList<T>.setSelectedItem(value: T) {
    for (i in 0 until this.model.size) {
        if (this.model.getElementAt(i) == value) {
            this.selectedIndex = i
            break
        }
    }
}