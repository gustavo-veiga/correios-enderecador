package br.com.correios.enderecador.tablemodel

import br.com.correios.enderecador.bean.DestinatarioBean

class RecipientImportTableModel : GenericTableModel<DestinatarioBean>() {
    init {
        columns = arrayOf(
            "Tratamento",
            "Empresa/Nome (linha 1)",
            "Empresa/Nome (linha 2)",
            "Caixa Postal", "Endereço",
            "Numero/Lote",
            "Complemento",
            "Bairro",
            "Cidade",
            "UF",
            "Email",
            "Telefone",
            "Fax",
            "CEP Caixa Postal",
            "Cep"
        )
    }

    override fun getValueAt(row: Int, column: Int): String {
        val destinatarioBean = dataTable[row]
        return when (column) {
            0 -> destinatarioBean.titulo
            1 -> destinatarioBean.nome
            2 -> destinatarioBean.apelido
            3 -> destinatarioBean.caixaPostal
            4 -> destinatarioBean.endereco
            5 -> destinatarioBean.numeroEndereco
            6 -> destinatarioBean.complemento
            7 -> destinatarioBean.bairro
            8 -> destinatarioBean.cidade
            9 -> destinatarioBean.uf
            10 -> destinatarioBean.email
            11 -> destinatarioBean.telefone
            12 -> destinatarioBean.fax
            13 -> destinatarioBean.cepCaixaPostal
            14 -> destinatarioBean.cep
            else -> ""
        }
    }
}
