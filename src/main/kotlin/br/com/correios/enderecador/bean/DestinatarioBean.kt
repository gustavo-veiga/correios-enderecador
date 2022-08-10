package br.com.correios.enderecador.bean

import java.util.Comparator

class DestinatarioBean : Comparator<Any?> {
    var numeroDestinatario = ""
    var apelido = ""
    var titulo = ""
    var nome = ""
    var cep: String? = null
    var endereco = ""
    var numeroEndereco = ""
    var complemento = ""
    var bairro = ""
    var cidade = ""
    var uf = ""
    var email = ""
    var telefone = ""
    var fax = ""
    var cepCaixaPostal = ""
    var caixaPostal = ""
    var quantidade = "1"
    var maoPropria = "Não"
    var desConteudo = ""
    var desEntregaVizinho = ""

    override fun toString(): String {
        return nome
    }

    override fun compare(obj1: Any?, obj2: Any?): Int {
        val destinatarioBean1 = obj1 as DestinatarioBean?
        val destinatarioBean2 = obj2 as DestinatarioBean?
        val vNome1 = destinatarioBean1!!.nome
            .replace("[ÁáÀaÂâÃã]".toRegex(), "A")
            .replace("[ÉéÈèÊê]".toRegex(), "E")
            .replace("[ÍíÌìÎî]".toRegex(), "I")
            .replace("[ÓóÒòÔôÕõ]".toRegex(), "O")
            .replace("[ÚúÙù]".toRegex(), "U")
        val vNome2 = destinatarioBean2!!.nome
            .replace("[ÁáÀaÂâÃã]".toRegex(), "A")
            .replace("[ÉéÈèÊê]".toRegex(), "E")
            .replace("[ÍíÌìÎî]".toRegex(), "I")
            .replace("[ÓóÒòÔôÕõ]".toRegex(), "O")
            .replace("[ÚúÙù]".toRegex(), "U")
        return vNome1.compareTo(vNome2, ignoreCase = true)
    }

    override fun equals(other: Any?): Boolean {
        if (other is DestinatarioBean) {
            return numeroDestinatario == other.numeroDestinatario
        }
        return false
    }

    override fun hashCode(): Int {
        return numeroDestinatario.hashCode() * 17
    }

    fun validaEmail(): Boolean {
        var result = false
        var contArroba = 0
        if (email != "" && email.length >= 5) {
            for (i in 0 until email.length) {
                if (email[i] == '@') contArroba++
            }
            if (contArroba == 1) {
                result = true
            }
        }
        return result
    }

    companion object {
        private var destinatarioBean: DestinatarioBean? = null
        @JvmStatic
        val instance: DestinatarioBean?
            get() {
                if (destinatarioBean == null) {
                    destinatarioBean = DestinatarioBean()
                }
                return destinatarioBean
            }
    }
}
