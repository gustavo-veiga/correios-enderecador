package br.com.correios.enderecador

import com.formdev.flatlaf.intellijthemes.FlatDarkFlatIJTheme
import com.formdev.flatlaf.extras.FlatInspector
import com.formdev.flatlaf.extras.FlatUIDefaultsInspector
import br.com.correios.enderecador.telas.TelaPrincipal
import javax.swing.WindowConstants
import kotlin.jvm.JvmStatic

class ControleEnderecador {
    fun ControleTela() {
        FlatDarkFlatIJTheme.setup()
        FlatInspector.install("ctrl shift alt X")
        FlatUIDefaultsInspector.install("ctrl shift alt Y")
        val frame = TelaPrincipal()
        frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        frame.isVisible = true
        frame.state = 0
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val controleEnderecador = ControleEnderecador()
            controleEnderecador.ControleTela()
        }
    }
}