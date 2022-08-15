package br.com.correios.enderecador.bean

import org.koin.core.annotation.Singleton
import java.io.Serializable

@Singleton
class RemetenteBean : Serializable {
    var numeroRemetente: String? = null
    var apelido: String? = null
    var titulo: String? = null
    var nome: String? = null
    var cep: String? = null
    var endereco: String? = null
    var numeroEndereco: String? = null
    var complemento: String? = null
    var bairro: String? = null
    var cidade: String? = null
    var uf: String? = null
    var email: String? = null
    var telefone: String? = null
    var fax: String? = null
    var cepCaixaPostal: String? = null
    var caixaPostal: String? = null

    override fun equals(other: Any?): Boolean {
        if (other is RemetenteBean) {
            return numeroRemetente == other.numeroRemetente
        }
        return false
    }

    override fun hashCode(): Int {
        return numeroRemetente.hashCode() * 17
    }

    override fun toString(): String {
        return nome!!
    }

    fun validaEmail(): Boolean {
        var contArroba = 0
        if (email == "" || email!!.length < 5) return false
        for (i in 0 until email!!.length) {
            if (email!![i] == '@') contArroba++
        }
        return contArroba == 1
    }

    companion object {
        private var remetenteBean: RemetenteBean? = null
        val instance: RemetenteBean?
            get() {
                if (remetenteBean == null) {
                    remetenteBean = RemetenteBean()
                }
                return remetenteBean
            }
    }
}
