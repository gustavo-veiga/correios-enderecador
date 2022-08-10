package br.com.correios.enderecador.telas

import javax.swing.table.DefaultTableModel
import br.com.correios.enderecador.bean.DestinatarioBean
import java.lang.StringBuilder
import java.util.*
import javax.swing.JOptionPane

class DestinatarioImpressaoTableModel(tipo: String) : DefaultTableModel() {
    private val TAMANHO_CAMPO_ENTREGA_VIZINHO = 40
    private val colunas: Array<String>
    private var destinatarioBean: Vector<DestinatarioBean?>? = Vector()

    init {
        colunas = if (tipo.equals("E", ignoreCase = true)) {
            arrayOf(
                "Empresa/Nome",
                "Endereço",
                "Cidade/UF",
                "Quantidade *",
                "Mão Própria *",
                "Observação *",
                "Entrega no Vizinho"
            )
        } else {
            arrayOf(
                "Empresa/Nome",
                "Endereço",
                "Cidade/UF",
                "Quantidade *",
                "Mão Própria *",
                "Observação *"
            )
        }
    }

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

    fun getElementAt(row: Int): DestinatarioBean? {
        return destinatarioBean!![row]
    }

    override fun setValueAt(value: Any, row: Int, col: Int) {
        val valor: String
        var acho: Boolean
        var i: Int
        val texto: String
        val sb: StringBuilder
        val destinatarioBean = destinatarioBean!![row]
        when (col) {
            3 -> {
                valor = value as String
                acho = true
                i = 0
                while (i < valor.length) {
                    if (!Character.isDigit(valor[i])) {
                        acho = false
                        break
                    }
                    i++
                }
                if (acho) {
                    destinatarioBean!!.quantidade = value
                }
            }
            4 -> {
                destinatarioBean!!.maoPropria = (value as String)
                if (value === "Sim") destinatarioBean.desEntregaVizinho = ""
            }
            5 -> destinatarioBean!!.desConteudo = (value as String)
            6 -> {
                texto = value as String
                if (texto.length <= TAMANHO_CAMPO_ENTREGA_VIZINHO) {
                    destinatarioBean!!.desEntregaVizinho = texto
                }
                sb = StringBuilder()
                sb.append("O texto não pode ter mais que ")
                sb.append(40)
                sb.append(" caracteres")
                JOptionPane.showMessageDialog(
                    null,
                    sb.toString(),
                    "Entrega autorizada no vizinho",
                    JOptionPane.WARNING_MESSAGE
                )
            }
        }
        fireTableCellUpdated(row, col)
    }

    override fun getValueAt(linha: Int, coluna: Int): String {
        val destinatarioBean = destinatarioBean!![linha]
        return when (coluna) {
            0 -> destinatarioBean!!.nome + " " + destinatarioBean.apelido
            1 -> destinatarioBean!!.endereco + " " + destinatarioBean.numeroEndereco + " " + destinatarioBean.complemento + " " + destinatarioBean.bairro
            2 -> destinatarioBean!!.cidade + " - " + destinatarioBean.uf
            3 -> destinatarioBean!!.quantidade
            4 -> destinatarioBean!!.maoPropria
            5 -> destinatarioBean!!.desConteudo
            6 -> destinatarioBean!!.desEntregaVizinho
            else -> ""
        }
    }

    override fun isCellEditable(linha: Int, coluna: Int): Boolean {
        var retorno = coluna == 3 || coluna == 4 || coluna == 5
        val s = getValueAt(linha, 4)
        if (coluna == 6 && s == "Não") retorno = true
        return retorno
    }

    fun getDestinatario(linha: Int): DestinatarioBean? {
        var destinatario: DestinatarioBean? = null
        if (destinatarioBean!![linha] != null) destinatario = destinatarioBean!![linha]
        return destinatario
    }

    var destinatario: Vector<DestinatarioBean?>?
        get() = destinatarioBean
        set(destinatarioBean) {
            this.destinatarioBean = destinatarioBean
            if (this.destinatarioBean == null) this.destinatarioBean = Vector()
            fireTableDataChanged()
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
