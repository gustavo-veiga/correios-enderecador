package br.com.correios.enderecador.dao

import br.com.correios.enderecador.dao.CEPFactory.Companion.factory

class DnecStrategyCEP : StrategyCEP {
    override val factory: CEPFactory?
        get() = factory(1)
}
