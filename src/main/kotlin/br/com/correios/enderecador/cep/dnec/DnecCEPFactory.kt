package br.com.correios.enderecador.cep.dnec

import br.com.correios.enderecador.cep.CEPFactory
import br.com.correios.enderecador.cep.InterfaceEnderecoDao
import kotlin.jvm.Synchronized

class DnecCEPFactory : CEPFactory() {
    override val endereco: InterfaceEnderecoDao?
        get() = EnderecoDnecDao.instance!!

    companion object {
        private var instance: DnecCEPFactory? = null

        @JvmStatic
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
