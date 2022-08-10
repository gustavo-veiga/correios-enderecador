package br.com.correios.enderecador.cep.dnec

import br.com.correios.enderecador.cep.CEPFactory
import br.com.correios.enderecador.cep.StrategyCEP

class DnecStrategyCEP : StrategyCEP {
    override val factory: CEPFactory?
        get() = CEPFactory.factory(1)
}
