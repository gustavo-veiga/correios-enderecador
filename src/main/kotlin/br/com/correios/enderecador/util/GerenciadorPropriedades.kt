package br.com.correios.enderecador.util

import java.util.Properties
import java.io.IOException

class GerenciadorPropriedades(private val arquivoPropriedade: String) {
    private val p = Properties()

    init {
        loadProperties()
    }

    fun getProperties(v: String?): String {
        return p.getProperty(v)
    }

    fun setProperties(p: String?, v: String?) {
        this.p.setProperty(p, v)
    }

    private fun loadProperties() {
        val `in` = javaClass.getResourceAsStream(arquivoPropriedade)
        if (`in` != null) try {
            p.load(`in`)
        } catch (ex: IOException) {
            ex.printStackTrace()
        } finally {
            try {
                `in`.close()
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
        }
    }
}
