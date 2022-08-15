package br.com.correios.enderecador.bean

data class GrupoBean(
    var numeroGrupo: String,
    var descricaoGrupo: String,
) {
    override fun toString(): String {
        return descricaoGrupo
    }
}
