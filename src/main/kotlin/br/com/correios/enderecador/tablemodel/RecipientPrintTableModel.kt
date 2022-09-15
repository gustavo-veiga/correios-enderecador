package br.com.correios.enderecador.tablemodel

import br.com.correios.enderecador.bean.DestinatarioBean
import br.com.correios.enderecador.util.isNumeric
import javax.swing.JOptionPane

class RecipientPrintTableModel(withDeliveryToNeighbor: Boolean = false) : GenericTableModel<DestinatarioBean>() {
    private val TAMANHO_CAMPO_ENTREGA_VIZINHO = 40

    init {
        val columns = arrayListOf("Empresa/Nome",
            "Endereço",
            "Cidade/UF",
            "Quantidade *",
            "Mão Própria *",
            "Observação *")

        if (withDeliveryToNeighbor) {
            columns.add("Entrega no Vizinho")
        }

        this.columns = columns.toTypedArray()
    }

    override fun setValueAt(value: Any, row: Int, column: Int) {
        value as String
        val destinatarioBean = dataTable[row]
        when (column) {
            3 -> {
                if (value.isNumeric()) {
                    destinatarioBean.quantidade = value
                }
            }
            4 -> {
                destinatarioBean.maoPropria = value
                if (value === "Sim") destinatarioBean.desEntregaVizinho = ""
            }
            5 -> destinatarioBean.desConteudo = value
            6 -> {
                if (value.length <= TAMANHO_CAMPO_ENTREGA_VIZINHO) {
                    destinatarioBean.desEntregaVizinho = value
                }
                JOptionPane.showMessageDialog(
                    null,
                    "O texto não pode ter mais que " +
                            TAMANHO_CAMPO_ENTREGA_VIZINHO +
                            " caracteres",
                    "Entrega autorizada no vizinho",
                    JOptionPane.WARNING_MESSAGE
                )
            }
        }
        fireTableCellUpdated(row, column)
    }

    override fun getValueAt(row: Int, column: Int): String {
        val destinatarioBean = dataTable[row]
        return when (column) {
            0 -> destinatarioBean.nome + " " + destinatarioBean.apelido
            1 -> destinatarioBean.endereco + " " + destinatarioBean.numeroEndereco + " " + destinatarioBean.complemento + " " + destinatarioBean.bairro
            2 -> destinatarioBean.cidade + " - " + destinatarioBean.uf
            3 -> destinatarioBean.quantidade
            4 -> destinatarioBean.maoPropria
            5 -> destinatarioBean.desConteudo
            6 -> destinatarioBean.desEntregaVizinho
            else -> ""
        }
    }

    override fun isCellEditable(row: Int, column: Int): Boolean {
        var retorno = column == 3 || column == 4 || column == 5
        val s = getValueAt(row, 4)
        if (column == 6 && s == "Não") retorno = true
        return retorno
    }
}
