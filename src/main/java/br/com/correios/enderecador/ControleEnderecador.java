package br.com.correios.enderecador;

import br.com.correios.enderecador.telas.TelaPrincipal;
import com.formdev.flatlaf.extras.FlatInspector;
import com.formdev.flatlaf.extras.FlatUIDefaultsInspector;
import com.formdev.flatlaf.intellijthemes.FlatDarkFlatIJTheme;

import javax.swing.*;

public class ControleEnderecador {
    public static void main(String[] args) {
        ControleEnderecador controleEnderecador = new ControleEnderecador();
        controleEnderecador.ControleTela();
    }

    public void ControleTela() {
        FlatDarkFlatIJTheme.setup();
        FlatInspector.install( "ctrl shift alt X" );
        FlatUIDefaultsInspector.install( "ctrl shift alt Y" );
        TelaPrincipal frame = new TelaPrincipal();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setState(0);
    }
}
