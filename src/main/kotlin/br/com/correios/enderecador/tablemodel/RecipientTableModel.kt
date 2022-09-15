package br.com.correios.enderecador.tablemodel

import br.com.correios.enderecador.bean.DestinatarioBean

class RecipientTableModel : GenericTableModel<DestinatarioBean>() {
    init {
        columns = arrayOf("Empresa/Nome", "Endereço", "Numero/Lote", "Cidade", "CEP", "UF")
    }

    override fun getValueAt(row: Int, column: Int): String {
        val destinatarioBean = dataTable[row]
        return when (column) {
            0 -> destinatarioBean.nome + " " + destinatarioBean.apelido
            1 -> destinatarioBean.endereco + " " + destinatarioBean.complemento + " " + destinatarioBean.bairro
            2 -> destinatarioBean.numeroEndereco
            3 -> destinatarioBean.cidade
            4 -> destinatarioBean.cep
            5 -> destinatarioBean.uf
            else -> ""
        }
    }

    fun orderBy() {
        dataTable.sortBy { it.numeroDestinatario }
    }
}
