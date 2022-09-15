package br.com.correios.enderecador.util

import br.com.correios.enderecador.exception.EnderecadorExcecao
import java.awt.Desktop
import java.net.URI
import java.net.URISyntaxException

class Geral {
    companion object {
        fun validaEmail(email: String): Boolean {
            var contArroba = 0
            if (email.length < 5) return false
            for (element in email) {
                if (element == '@') contArroba++
            }
            return contArroba == 1
        }

        fun displayURL(url: String) {
            try {
                if (Desktop.isDesktopSupported()) {
                    val desktop = Desktop.getDesktop()
                    if (desktop.isSupported(Desktop.Action.BROWSE)) {
                        desktop.browse(URI.create(url))
                    }
                }
            } catch (ex: URISyntaxException) {
                throw EnderecadorExcecao("Não foi possível abrir o browser. " + ex.message)
            }
        }
    }
}
