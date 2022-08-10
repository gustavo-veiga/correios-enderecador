package br.com.correios.enderecador.bean

class GrupoDestinatarioBean {
    var numeroGrupo: String? = null
    var numeroDestinatario: String? = null

    companion object {
        private var grupoDestinatarioBean: GrupoDestinatarioBean? = null
        @JvmStatic
        val instance: GrupoDestinatarioBean?
            get() {
                if (grupoDestinatarioBean == null) {
                    grupoDestinatarioBean = GrupoDestinatarioBean()
                }
                return grupoDestinatarioBean
            }
    }
}
