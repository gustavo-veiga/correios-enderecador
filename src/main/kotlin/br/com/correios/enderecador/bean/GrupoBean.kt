package br.com.correios.enderecador.bean

class GrupoBean {
    var numeroGrupo: String? = null
    var descricaoGrupo: String? = null
    override fun toString(): String {
        return descricaoGrupo!!
    }

    companion object {
        private var grupoBean: GrupoBean? = null
        @JvmStatic
        val instance: GrupoBean?
            get() {
                if (grupoBean == null) {
                    grupoBean = GrupoBean()
                }
                return grupoBean
            }
    }
}
