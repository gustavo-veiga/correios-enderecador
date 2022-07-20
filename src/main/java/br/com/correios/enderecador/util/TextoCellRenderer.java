package br.com.correios.enderecador.util;

import javax.swing.table.DefaultTableCellRenderer;

public class TextoCellRenderer extends DefaultTableCellRenderer {
    public TextoCellRenderer(int alinhamentoHorizontal) {
        setHorizontalAlignment(alinhamentoHorizontal);
    }
}
