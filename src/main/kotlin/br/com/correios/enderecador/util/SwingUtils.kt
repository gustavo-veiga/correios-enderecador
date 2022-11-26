package br.com.correios.enderecador.util

import br.com.correios.enderecador.exception.EnderecadorExcecao
import java.awt.Desktop
import java.net.URI
import java.net.URISyntaxException
import java.util.*
import javax.swing.JFormattedTextField
import javax.swing.JList
import javax.swing.text.MaskFormatter

fun openBrowseLink(url: String) {
    try {
        if (Desktop.isDesktopSupported()) {
            val desktop = Desktop.getDesktop()
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                desktop.browse(URI.create(url))
            }
        }
    } catch (ex: URISyntaxException) {
        throw EnderecadorExcecao("Não foi possível abrir o browser. " + ex.message)
    }
}

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

fun <T> JList<T>.insertNotRepeated(values: List<T>, comparator: Comparator<T>) {
    val items = mutableListOf<T>()
    for (i in 0 until this.model.size) {
        items.add(this.model.getElementAt(i))
    }
    values.forEach { value ->
        if (items.contains(value).not()) {
            items.add(value)
        }
    }
    items.sortWith(comparator)
    this.setListData(Vector(items))
}

