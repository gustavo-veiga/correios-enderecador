package br.com.correios.enderecador.cep.gpbe

import br.com.correios.enderecador.cep.CEPFactory
import br.com.correios.enderecador.cep.InterfaceEnderecoDao
import kotlin.jvm.Synchronized

class GpbeCEPFactory : CEPFactory() {
    override val endereco: InterfaceEnderecoDao?
        get() = EnderecoGpbeDao.instance!!

    companion object {
        private var instance: GpbeCEPFactory? = null

        @JvmStatic
        @get:Synchronized
        val intance: GpbeCEPFactory?
            get() {
                if (instance == null) {
                    instance = GpbeCEPFactory()
                }
                return instance
            }
    }
}
