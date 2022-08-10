package br.com.correios.enderecador.dao

class CepInvalidoException : DaoException {
    constructor(msg: String?) : super(msg)
    constructor(msg: String?, t: Throwable?) : super(msg, t)
}
