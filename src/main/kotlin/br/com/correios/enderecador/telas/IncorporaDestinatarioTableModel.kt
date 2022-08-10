package br.com.correios.enderecador.telas

import javax.swing.table.DefaultTableModel
import br.com.correios.enderecador.bean.DestinatarioBean
import java.util.ArrayList

class IncorporaDestinatarioTableModel : DefaultTableModel() {
    private val colunas = arrayOf(
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
            0 -> destinatarioBean!!.titulo
            1 -> destinatarioBean!!.nome
            2 -> destinatarioBean!!.apelido
            3 -> destinatarioBean!!.caixaPostal
            4 -> destinatarioBean!!.endereco
            5 -> destinatarioBean!!.numeroEndereco
            6 -> destinatarioBean!!.complemento
            7 -> destinatarioBean!!.bairro
            8 -> destinatarioBean!!.cidade
            9 -> destinatarioBean!!.uf
            10 -> destinatarioBean!!.email
            11 -> destinatarioBean!!.telefone
            12 -> destinatarioBean!!.fax
            13 -> destinatarioBean!!.cepCaixaPostal
            14 -> destinatarioBean!!.cep.orEmpty()
            else -> ""
        }
    }

    override fun isCellEditable(linha: Int, coluna: Int): Boolean {
        return false
    }

    fun setDestinatario(destinatarioBean: ArrayList<DestinatarioBean?>?) {
        this.destinatarioBean = destinatarioBean
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
}
