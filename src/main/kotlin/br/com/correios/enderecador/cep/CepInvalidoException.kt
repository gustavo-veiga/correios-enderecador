package br.com.correios.enderecador.cep

import br.com.correios.enderecador.dao.DaoException

class CepInvalidoException : DaoException {
    constructor(msg: String?) : super(msg)
    constructor(msg: String?, t: Throwable?) : super(msg, t)
}
