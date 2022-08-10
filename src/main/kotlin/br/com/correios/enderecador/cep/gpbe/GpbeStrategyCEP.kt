package br.com.correios.enderecador.cep.gpbe

import br.com.correios.enderecador.cep.CEPFactory
import br.com.correios.enderecador.cep.StrategyCEP

class GpbeStrategyCEP : StrategyCEP {
    override val factory: CEPFactory?
        get() = CEPFactory.factory(2)
}
