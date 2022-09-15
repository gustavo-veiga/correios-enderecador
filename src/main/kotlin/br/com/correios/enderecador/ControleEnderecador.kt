package br.com.correios.enderecador

import br.com.correios.enderecador.view.TelaPrincipal
import com.formdev.flatlaf.intellijthemes.FlatDarkFlatIJTheme
import com.formdev.flatlaf.extras.FlatInspector
import com.formdev.flatlaf.extras.FlatUIDefaultsInspector
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module
import javax.swing.SwingUtilities

fun main() {
    startKoin {
        // use Koin logger
        printLogger()
        // declare modules
        modules(AppModule().module)
    }

    FlatDarkFlatIJTheme.setup()
    FlatInspector.install("ctrl shift alt X")
    FlatUIDefaultsInspector.install("ctrl shift alt Y")

    SwingUtilities.invokeLater {
        TelaPrincipal()
    }
}
