package br.com.correios.enderecador.util

import com.couchbase.lite.MutableDictionary
import kotlin.reflect.KProperty

fun mutableDictionary(init: MutableDictionary.() -> Unit): MutableDictionary {
    val dictionary = MutableDictionary()
    dictionary.init()
    return dictionary
}

fun MutableDictionary.string(key: KProperty<String>, value: String) {
    this.setString(key.name, value)
}