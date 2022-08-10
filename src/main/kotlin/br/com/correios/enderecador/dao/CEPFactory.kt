package br.com.correios.enderecador.dao

import java.lang.IllegalArgumentException

abstract class CEPFactory {
    abstract val endereco: InterfaceEnderecoDao?

    companion object {
        private const val DNEC = 1
        private const val GPBE = 2
        @JvmStatic
        fun factory(type: Int): CEPFactory? {
            when (type) {
                DNEC -> return DnecCEPFactory.intance
                GPBE -> return GpbeCEPFactory.intance
            }
            throw IllegalArgumentException()
        }
    }
}
