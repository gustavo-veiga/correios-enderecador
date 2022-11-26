package br.com.correios.enderecador.util

import javax.swing.table.DefaultTableCellRenderer

class TextCellRenderer(alinhamentoHorizontal: Int) : DefaultTableCellRenderer() {
    init {
        horizontalAlignment = alinhamentoHorizontal
    }
}
