package br.com.correios.enderecador.view

import br.com.correios.enderecador.bean.RemetenteBean

class RemetenteTableModel : GenericTableModel<RemetenteBean>() {
    init {
        columns = arrayOf("Empresa/Nome", "Endereço", "Numero/Lote", "Cidade", "CEP", "UF")
    }

    override fun getValueAt(row: Int, column: Int): String {
        val remetenteBean = dataTable[row]
        return when (column) {
            0 -> remetenteBean.nome + " " + remetenteBean.apelido
            1 -> remetenteBean.endereco + " " + remetenteBean.complemento + " " + remetenteBean.bairro
            2 -> remetenteBean.numeroEndereco
            3 -> remetenteBean.cidade
            4 -> remetenteBean.cep
            5 -> remetenteBean.uf
            else -> ""
        }
    }
}
