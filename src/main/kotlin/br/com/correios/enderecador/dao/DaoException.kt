package br.com.correios.enderecador.dao

import java.lang.Exception

open class DaoException : Exception {
    constructor()
    constructor(msg: String?) : super(msg)
    constructor(msg: String?, t: Throwable?) : super(msg, t)
}
