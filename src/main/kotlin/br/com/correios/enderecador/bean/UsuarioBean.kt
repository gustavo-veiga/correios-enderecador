package br.com.correios.enderecador.bean

import org.koin.core.annotation.Singleton

@Singleton
class UsuarioBean {
    var pwd = ""
    var usuario = ""

    companion object {
        private var usuarioBean: UsuarioBean? = null
        @JvmStatic
        val instance: UsuarioBean?
            get() {
                if (usuarioBean == null) {
                    usuarioBean = UsuarioBean()
                }
                return usuarioBean
            }
    }
}
