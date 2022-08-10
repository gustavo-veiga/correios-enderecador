package br.com.correios.enderecador.dao

import kotlin.jvm.Synchronized

class DnecCEPFactory : CEPFactory() {
    override val endereco: InterfaceEnderecoDao
        get() = EnderecoDnecDao.instance!!

    companion object {
        private var instance: DnecCEPFactory? = null

        @get:Synchronized
        val intance: DnecCEPFactory?
            get() {
                if (instance == null) {
                    instance = DnecCEPFactory()
                }
                return instance
            }
    }
}
