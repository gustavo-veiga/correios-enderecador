package br.com.correios.enderecador.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.reflect.full.companionObject

class Logging<in R : Any> : ReadOnlyProperty<R, Logger> {
    private fun <T : Any> getClassForLogging(javaClass: Class<T>): Class<*> {
        return javaClass.enclosingClass?.takeIf {
            it.kotlin.companionObject?.java == javaClass
        } ?: javaClass
    }

    override fun getValue(thisRef: R, property: KProperty<*>): Logger {
        return LoggerFactory.getLogger(getClassForLogging(thisRef.javaClass))
    }
}