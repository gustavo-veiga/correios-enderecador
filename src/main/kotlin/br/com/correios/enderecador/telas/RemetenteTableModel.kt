package br.com.correios.enderecador.telas

import javax.swing.table.DefaultTableModel
import br.com.correios.enderecador.bean.RemetenteBean
import java.util.ArrayList

class RemetenteTableModel : DefaultTableModel() {
    private val colunas = arrayOf("Empresa/Nome", "Endereço", "Numero/Lote", "Cidade", "CEP", "UF")
    private var remetenteBean: ArrayList<RemetenteBean>? = ArrayList()

    override fun getColumnCount(): Int {
        return colunas.size
    }

    override fun getColumnName(i: Int): String {
        return colunas[i]
    }

    override fun getRowCount(): Int {
        return remetenteBean?.size ?: 0
    }

    override fun getValueAt(linha: Int, coluna: Int): String {
        val remetenteBean = remetenteBean?.get(linha)
        return when (coluna) {
            0 -> remetenteBean!!.nome + " " + remetenteBean.apelido
            1 -> remetenteBean!!.endereco + " " + remetenteBean.complemento + " " + remetenteBean.bairro
            2 -> remetenteBean!!.numeroEndereco.orEmpty()
            3 -> remetenteBean!!.cidade.orEmpty()
            4 -> remetenteBean!!.cep.orEmpty()
            5 -> remetenteBean!!.uf.orEmpty()
            else -> ""
        }
    }

    override fun isCellEditable(linha: Int, coluna: Int): Boolean {
        return false
    }

    fun setRemetente(remetenteBean: ArrayList<RemetenteBean>) {
        this.remetenteBean = remetenteBean
        fireTableDataChanged()
    }

    fun getRemetente(linha: Int): RemetenteBean? {
        return remetenteBean?.get(linha)
    }

    fun addRemetente(remetente: RemetenteBean) {
        remetenteBean?.add(remetente)
        fireTableDataChanged()
    }

    fun setRemetente(linha: Int, remetenteBean: RemetenteBean) {
        this.remetenteBean?.set(linha, remetenteBean)
        fireTableDataChanged()
    }

    fun removeRemetente(remetente: RemetenteBean) {
        remetenteBean?.remove(remetente)
        fireTableDataChanged()
    }

    fun indexOf(remetente: RemetenteBean): Int {
        return remetenteBean?.indexOf(remetente) ?: -1
    }
}
