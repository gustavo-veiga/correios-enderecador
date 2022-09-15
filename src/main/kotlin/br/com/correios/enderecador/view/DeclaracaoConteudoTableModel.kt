package br.com.correios.enderecador.telas

import br.com.correios.enderecador.bean.ConteudoDeclaradoBean

class DeclaracaoConteudoTableModel : GenericTableModel<ConteudoDeclaradoBean>() {
    init {
        columns = arrayOf("Conteúdo", "Quantidade", "Valor")
        dataTable = mutableListOf(
            ConteudoDeclaradoBean(),
            ConteudoDeclaradoBean(),
            ConteudoDeclaradoBean(),
            ConteudoDeclaradoBean(),
            ConteudoDeclaradoBean(),
            ConteudoDeclaradoBean()
        )
    }

    override fun isCellEditable(row: Int, column: Int) = true

    override fun getValueAt(row: Int, column: Int): Any {
        val data = dataTable[row]
        return when (column) {
            0 -> data.conteudo
            1 -> data.quantidade
            2 -> data.valor
            else -> ""
        }
    }

    override fun setValueAt(value: Any, row: Int, column: Int) {
        value as String
        val data = dataTable[row]
        when (column) {
            0 -> data.conteudo = value
            1 -> data.quantidade = value
            2 -> data.valor = value
        }
        fireTableCellUpdated(row, column)
    }
}