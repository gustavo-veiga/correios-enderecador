package br.com.correios.enderecador.telas

import javax.swing.table.DefaultTableModel
import br.com.correios.enderecador.bean.DestinatarioBean
import java.util.ArrayList

class DestinatarioTableModel : DefaultTableModel() {
    private val colunas = arrayOf("Empresa/Nome", "Endereço", "Numero/Lote", "Cidade", "CEP", "UF")
    private var destinatarioBean: ArrayList<DestinatarioBean?>? = ArrayList()

    override fun getColumnCount(): Int {
        return colunas.size
    }

    override fun getColumnName(i: Int): String {
        return colunas[i]
    }

    override fun getRowCount(): Int {
        var retorno = 0
        if (destinatarioBean != null) retorno = destinatarioBean!!.size
        return retorno
    }

    override fun getValueAt(linha: Int, coluna: Int): String {
        val destinatarioBean = destinatarioBean!![linha]
        return when (coluna) {
            0 -> destinatarioBean!!.nome + " " + destinatarioBean.apelido
            1 -> destinatarioBean!!.endereco + " " + destinatarioBean.complemento + " " + destinatarioBean.bairro
            2 -> destinatarioBean!!.numeroEndereco
            3 -> destinatarioBean!!.cidade
            4 -> destinatarioBean!!.cep.orEmpty()
            5 -> destinatarioBean!!.uf
            else -> ""
        }
    }

    override fun isCellEditable(linha: Int, coluna: Int): Boolean {
        return false
    }

    fun setDestinatario(destinatarioBean: ArrayList<*>?) {
        this.destinatarioBean = destinatarioBean as ArrayList<DestinatarioBean?>?
        if (this.destinatarioBean == null) this.destinatarioBean = ArrayList()
        fireTableDataChanged()
    }

    fun getDestinatario(linha: Int): DestinatarioBean? {
        var destinatario: DestinatarioBean? = null
        if (destinatarioBean!![linha] != null) destinatario = destinatarioBean!![linha]
        return destinatario
    }

    fun setDestinatario(linha: Int, destinatarioBean: DestinatarioBean?) {
        this.destinatarioBean!![linha] = destinatarioBean
        fireTableDataChanged()
    }

    fun addDestinatario(destinatario: DestinatarioBean?) {
        destinatarioBean!!.add(destinatario)
        fireTableDataChanged()
    }

    fun removeDestinatario(destinatario: DestinatarioBean?) {
        destinatarioBean!!.remove(destinatario)
        fireTableDataChanged()
    }

    fun indexOf(destinatario: DestinatarioBean?): Int {
        return destinatarioBean!!.indexOf(destinatario)
    }
}