package br.com.correios.enderecador.bean

import kotlin.jvm.JvmOverloads

class ImpressaoBean {
    var des_campo1: String? = null
        private set
    var des_campo2: String? = null
        private set
    var des_campo3: String? = null
        private set
    var des_campo4: String? = null
        private set
    var des_campo5: String? = null
        private set
    var des_campo6: String? = null
        private set
    var des_campo7: String? = null
        private set
    var des_campo8: String? = null
        private set
    var des_cep: String? = null
    var des_codBarras: String? = null
    var desMaoPropria: String? = null
    var desConteudo: String? = null
    var des_entrega: String? = null
    var rem_campo1: String? = null
        private set
    var rem_campo2: String? = null
        private set
    var rem_campo3: String? = null
        private set
    var rem_campo4: String? = null
        private set
    var rem_campo5: String? = null
        private set
    var rem_campo6: String? = null
        private set
    var rem_campo7: String? = null
        private set
    var rem_campo8: String? = null
        private set
    var rem_cep: String? = null
    var rem_CodBarras: String? = null
    var codigoDoisD: String? = null
    fun setDes_campo1(des_campo1: String) {
        this.des_campo1 = des_campo1.trim()
    }

    fun setDes_campo2(des_campo2: String) {
        this.des_campo2 = des_campo2.trim()
    }

    fun setDes_campo3(des_campo3: String) {
        this.des_campo3 = des_campo3.trim()
    }

    fun setDes_campo4(des_campo4: String) {
        this.des_campo4 = des_campo4.trim()
    }

    fun setDes_campo5(des_campo5: String) {
        this.des_campo5 = des_campo5.trim()
    }

    fun setDes_campo6(des_campo6: String) {
        this.des_campo6 = des_campo6.trim()
    }

    fun setDes_campo7(des_campo7: String) {
        this.des_campo7 = des_campo7.trim()
    }

    fun setDes_campo8(des_campo8: String) {
        this.des_campo8 = des_campo8.trim()
    }

    fun setRem_campo1(rem_campo1: String) {
        this.rem_campo1 = rem_campo1.trim()
    }

    fun setRem_campo2(rem_campo2: String) {
        this.rem_campo2 = rem_campo2.trim()
    }

    fun setRem_campo3(rem_campo3: String) {
        this.rem_campo3 = rem_campo3.trim()
    }

    fun setRem_campo4(rem_campo4: String) {
        this.rem_campo4 = rem_campo4.trim()
    }

    fun setRem_campo5(rem_campo5: String) {
        this.rem_campo5 = rem_campo5.trim()
    }

    fun setRem_campo6(rem_campo6: String) {
        this.rem_campo6 = rem_campo6.trim()
    }

    fun setRem_campo7(rem_campo7: String) {
        this.rem_campo7 = rem_campo7.trim()
    }

    fun setRem_campo8(rem_campo8: String) {
        this.rem_campo8 = rem_campo8.trim()
    }

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
