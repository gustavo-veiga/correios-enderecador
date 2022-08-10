package br.com.correios.enderecador.dao

import kotlin.jvm.Synchronized

class GpbeCEPFactory : CEPFactory() {
    override val endereco: InterfaceEnderecoDao?
        get() = EnderecoGpbeDao.instance

    companion object {
        private var instance: GpbeCEPFactory? = null

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
