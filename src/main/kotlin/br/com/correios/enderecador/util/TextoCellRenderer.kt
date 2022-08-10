package br.com.correios.enderecador.util

import javax.swing.table.DefaultTableCellRenderer

class TextoCellRenderer(alinhamentoHorizontal: Int) : DefaultTableCellRenderer() {
    init {
        horizontalAlignment = alinhamentoHorizontal
    }
}
