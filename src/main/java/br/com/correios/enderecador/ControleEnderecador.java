package br.com.correios.enderecador;

import br.com.correios.enderecador.telas.TelaPrincipal;

import javax.swing.*;

public class ControleEnderecador {
    public static void main(String[] args) {
        ControleEnderecador controleEnderecador = new ControleEnderecador();
        controleEnderecador.ControleTela();
    }

    public void ControleTela() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        TelaPrincipal frame = new TelaPrincipal();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setState(0);
    }
}
