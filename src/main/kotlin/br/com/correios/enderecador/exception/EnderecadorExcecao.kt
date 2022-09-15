package br.com.correios.enderecador.exception

import java.lang.Exception

open class EnderecadorExcecao : Exception {
    constructor()
    constructor(mensagem: String?) : super(mensagem)
    constructor(msg: String?, t: Throwable?) : super(msg, t)
}
