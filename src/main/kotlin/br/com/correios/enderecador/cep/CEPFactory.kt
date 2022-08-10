package br.com.correios.enderecador.cep

import br.com.correios.enderecador.cep.dnec.DnecCEPFactory
import br.com.correios.enderecador.cep.gpbe.GpbeCEPFactory
import java.lang.IllegalArgumentException

abstract class CEPFactory {
    abstract val endereco: InterfaceEnderecoDao?

    companion object {
        private const val DNEC = 1
        private const val GPBE = 2
        fun factory(type: Int): CEPFactory? {
            when (type) {
                DNEC -> return DnecCEPFactory.intance
                GPBE -> return GpbeCEPFactory.intance
            }
            throw IllegalArgumentException()
        }
    }
}
