package br.com.correios.enderecador.bean

import java.awt.Image

data class ImpressaoBean(
    var des_campo1: String? = null,
    var des_campo2: String? = null,
    var des_campo3: String? = null,
    var des_campo4: String? = null,
    var des_campo5: String? = null,
    var des_campo6: String? = null,
    var des_campo7: String? = null,
    var des_campo8: String? = null,
    var des_cep: String? = null,
    var des_codBarras: String? = null,
    var desMaoPropria: String? = null,
    var desConteudo: String? = null,
    var des_entrega: String? = null,
    var rem_campo1: String? = null,
    var rem_campo2: String? = null,
    var rem_campo3: String? = null,
    var rem_campo4: String? = null,
    var rem_campo5: String? = null,
    var rem_campo6: String? = null,
    var rem_campo7: String? = null,
    var rem_campo8: String? = null,
    var rem_CodBarras: String? = null,
    var codigoDoisD: String? = null,
    var barcodeZip: Image? = null,
    var datamatrix: Image? = null
) {
    @JvmOverloads
    fun organizaCampos(tipo: Int = ORGANIZA_CAMPOS_TODOS) {
        if (tipo == ORGANIZA_CAMPOS_ENCOMENDAS) {
            organizaCamposEncomendas()
        } else {
            organizaCamposTodos()
        }
    }

    fun organizaCamposTodos() {
        if (rem_campo7.isNullOrBlank()) {
            rem_campo7 = rem_campo8
            rem_campo8 = null
        }
        if (rem_campo6.isNullOrBlank()) {
            rem_campo6 = rem_campo7
            rem_campo7 = rem_campo8
            rem_campo8 = null
        }
        if (rem_campo5.isNullOrBlank()) {
            rem_campo5 = rem_campo6
            rem_campo6 = rem_campo7
            rem_campo7 = rem_campo8
            rem_campo8 = null
        }
        if (rem_campo4.isNullOrBlank()) {
            rem_campo4 = rem_campo5
            rem_campo5 = rem_campo6
            rem_campo6 = rem_campo7
            rem_campo7 = rem_campo8
            rem_campo8 = null
        }
        if (rem_campo3.isNullOrBlank()) {
            rem_campo3 = rem_campo4
            rem_campo4 = rem_campo5
            rem_campo5 = rem_campo6
            rem_campo6 = rem_campo7
            rem_campo7 = rem_campo8
            rem_campo8 = null
        }
        if (rem_campo2.isNullOrBlank()) {
            rem_campo2 = rem_campo3
            rem_campo3 = rem_campo4
            rem_campo4 = rem_campo5
            rem_campo5 = rem_campo6
            rem_campo6 = rem_campo7
            rem_campo7 = rem_campo8
            rem_campo8 = null
        }
        if (rem_campo1.isNullOrBlank()) {
            rem_campo1 = rem_campo2
            rem_campo2 = rem_campo3
            rem_campo3 = rem_campo4
            rem_campo4 = rem_campo5
            rem_campo5 = rem_campo6
            rem_campo6 = rem_campo7
            rem_campo7 = rem_campo8
            rem_campo8 = null
        }
        if (des_campo7.isNullOrBlank()) {
            des_campo7 = des_campo8
            des_campo8 = null
        }
        if (des_campo6.isNullOrBlank()) {
            des_campo6 = des_campo7
            des_campo7 = des_campo8
            des_campo8 = null
        }
        if (des_campo5.isNullOrBlank()) {
            des_campo5 = des_campo6
            des_campo6 = des_campo7
            des_campo7 = des_campo8
            des_campo8 = null
        }
        if (des_campo4.isNullOrBlank()) {
            des_campo4 = des_campo5
            des_campo5 = des_campo6
            des_campo6 = des_campo7
            des_campo7 = des_campo8
            des_campo8 = null
        }
        if (des_campo3.isNullOrBlank()) {
            des_campo3 = des_campo4
            des_campo4 = des_campo5
            des_campo5 = des_campo6
            des_campo6 = des_campo7
            des_campo7 = des_campo8
            des_campo8 = null
        }
        if (des_campo2.isNullOrBlank()) {
            des_campo2 = des_campo3
            des_campo3 = des_campo4
            des_campo4 = des_campo5
            des_campo5 = des_campo6
            des_campo6 = des_campo7
            des_campo7 = des_campo8
            des_campo8 = null
        }
        if (des_campo1.isNullOrBlank()) {
            des_campo1 = des_campo2
            des_campo2 = des_campo3
            des_campo3 = des_campo4
            des_campo4 = des_campo5
            des_campo5 = des_campo6
            des_campo6 = des_campo7
            des_campo7 = des_campo8
            des_campo8 = null
        }
    }

    fun organizaCamposEncomendas() {
        if (rem_campo5.isNullOrBlank()) {
            rem_campo5 = rem_campo8
            rem_campo8 = null
        }
        if (rem_campo4.isNullOrBlank()) {
            rem_campo4 = rem_campo5
            rem_campo5 = rem_campo8
            rem_campo8 = null
        }
        if (rem_campo3.isNullOrBlank()) {
            rem_campo3 = rem_campo4
            rem_campo4 = rem_campo5
            rem_campo5 = rem_campo8
            rem_campo8 = null
        }
        if (rem_campo2.isNullOrBlank()) {
            rem_campo2 = rem_campo3
            rem_campo3 = rem_campo4
            rem_campo4 = rem_campo5
            rem_campo5 = rem_campo8
            rem_campo8 = null
        }
        if (rem_campo1.isNullOrBlank()) {
            rem_campo1 = rem_campo2
            rem_campo2 = rem_campo3
            rem_campo3 = rem_campo4
            rem_campo4 = rem_campo5
            rem_campo5 = rem_campo8
            rem_campo8 = null
        }
        if (des_campo5.isNullOrBlank()) {
            des_campo5 = des_campo8
            des_campo8 = null
        }
        if (des_campo4.isNullOrBlank()) {
            des_campo4 = des_campo5
            des_campo5 = des_campo8
            des_campo8 = null
        }
        if (des_campo3.isNullOrBlank()) {
            des_campo3 = des_campo4
            des_campo4 = des_campo5
            des_campo5 = des_campo8
            des_campo8 = null
        }
        if (des_campo2.isNullOrBlank()) {
            des_campo2 = des_campo3
            des_campo3 = des_campo4
            des_campo4 = des_campo5
            des_campo5 = des_campo8
            des_campo8 = null
        }
        if (des_campo1.isNullOrBlank()) {
            des_campo1 = des_campo2
            des_campo2 = des_campo3
            des_campo3 = des_campo4
            des_campo4 = des_campo5
            des_campo5 = des_campo8
            des_campo8 = null
        }
    }

    companion object {
        const val ORGANIZA_CAMPOS_TODOS = 1
        const val ORGANIZA_CAMPOS_ENCOMENDAS = 2
    }
}
