package br.com.correios.enderecador.bean

class GlobalBean {
    var mostraMensagem = "SIM"

    companion object {
        private var globalBean: GlobalBean? = null
        @JvmStatic
        val instance: GlobalBean?
            get() {
                if (globalBean == null) {
                    globalBean = GlobalBean()
                }
                return globalBean
            }
    }
}
