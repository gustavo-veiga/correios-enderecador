package br.com.correios.enderecador;

import br.com.correios.enderecador.telas.TelaPrincipal;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

public class ControleEnderecador {
    public static void main(String[] args) {
        ControleEnderecador controleEnderecador = new ControleEnderecador();
        controleEnderecador.ControleTela();
    }

    public void ControleTela() {
        FlatLightLaf.setup();
        TelaPrincipal frame = new TelaPrincipal();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setState(0);
    }
}
