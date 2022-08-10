package br.com.correios.enderecador.telas

import br.com.correios.enderecador.bean.DestinatarioBean
import br.com.correios.enderecador.bean.RemetenteBean
import javax.swing.table.DefaultTableModel
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.awt.event.ActionEvent
import java.awt.Color
import br.com.correios.enderecador.util.Impressao
import br.com.correios.enderecador.excecao.EnderecadorExcecao
import org.apache.log4j.Logger
import java.awt.Dimension
import java.awt.Font
import java.awt.Frame
import java.util.*
import javax.swing.*

class TelaDeclararConteudo(
    parent: Frame?,
    modal: Boolean,
    vecDestinatario: Vector<DestinatarioBean?>,
    remetente: RemetenteBean
) : JDialog(parent, modal) {
    private val remetente: RemetenteBean
    private val model: DefaultTableModel
    private var vecDestinatario: Vector<DestinatarioBean?> = Vector()
    private var jTable1: JTable? = null
    private var txtPesoTotal: JTextField? = null

    init {
        val data = arrayOf(
            arrayOf<String?>(null, null, null),
            arrayOf<String?>(null, null, null),
            arrayOf<String?>(null, null, null),
            arrayOf<String?>(null, null, null),
            arrayOf<String?>(null, null, null),
            arrayOf<String?>(null, null, null)
        )
        val columnNames = arrayOf("Conteúdo", "Quantidade", "Valor")
        model = DefaultTableModel(data, columnNames)
        initComponents()
        this.remetente = remetente
        this.vecDestinatario = vecDestinatario
    }

    private fun initComponents() {
        val jToolBar1 = JToolBar()
        val jButton1 = JButton()
        val jButton2 = JButton()
        val jScrollPane1 = JScrollPane()
        jTable1 = JTable()
        txtPesoTotal = JTextField()
        val jLabel1 = JLabel()
        val jLabel3 = JLabel()
        defaultCloseOperation = 0
        title = "Declaração de Conteúdo"
        isResizable = false
        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(evt: WindowEvent) {
                fecharJanela(evt)
            }
        })
        jToolBar1.isRollover = true
        jButton1.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jButton1.icon = ImageIcon(javaClass.getResource("/imagens/print.gif"))
        jButton1.text = "Imprimir"
        jButton1.horizontalTextPosition = 0
        jButton1.maximumSize = Dimension(90, 60)
        jButton1.minimumSize = Dimension(47, 55)
        jButton1.verticalTextPosition = 3
        jButton1.addActionListener { evt -> jButton1ActionPerformed(evt) }
        jToolBar1.add(jButton1)
        jButton2.font = Font(Font.SANS_SERIF, 0, 9)
        jButton2.icon = ImageIcon(javaClass.getResource("/imagens/remover.gif"))
        jButton2.text = "Excluir item"
        jButton2.horizontalTextPosition = 0
        jButton2.maximumSize = Dimension(90, 60)
        jButton2.minimumSize = Dimension(47, 55)
        jButton2.verticalTextPosition = 3
        jButton2.addActionListener { evt: ActionEvent -> jButton2ActionPerformed(evt) }
        jToolBar1.add(jButton2)
        jScrollPane1.name = ""
        jTable1!!.model = model
        jTable1!!.setSelectionMode(0)
        jScrollPane1.setViewportView(jTable1)
        txtPesoTotal!!.horizontalAlignment = 4
        txtPesoTotal!!.text = "0"
        jLabel1.text = "Peso total (kg):"
        jLabel3.foreground = Color(51, 51, 255)
        jLabel3.text = "* Após o preenchimento de cada campo, pressionar a tecla <tab>"
        val layout = GroupLayout(contentPane)
        contentPane.layout = layout
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(
                            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(jToolBar1, GroupLayout.Alignment.TRAILING, -1, -1, 32767)
                                .addGroup(
                                    layout.createSequentialGroup()
                                        .addComponent(jLabel1).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtPesoTotal, -2, 78, -2)
                                )
                                .addComponent(jScrollPane1, -2, 672, -2)
                        )
                )
                .addGroup(
                    GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(-1, 32767)
                        .addComponent(jLabel3).addContainerGap()
                )
        )
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    layout.createSequentialGroup()
                        .addComponent(jToolBar1, -2, 57, -2)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel3, -2, 14, -2)
                        .addGap(3, 3, 3)
                        .addComponent(jScrollPane1, -2, 159, -2)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(
                            layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(txtPesoTotal, -2, -1, -2)
                                .addComponent(jLabel1)
                        ).addGap(30, 30, 30)
                )
        )
        jScrollPane1.accessibleContext.accessibleName = ""
        getAccessibleContext().accessibleDescription = ""
        size = Dimension(689, 328)
        setLocationRelativeTo(null)
    }

    private fun jButton1ActionPerformed(evt: ActionEvent) {
        val impressao = Impressao()
        val itens = model.dataVector
        try {
            impressao.imprimirDeclaracao("declaracao", itens, remetente, vecDestinatario, txtPesoTotal!!.text)
        } catch (ex: EnderecadorExcecao) {
            LOGGER.error(ex.message, ex as Throwable)
            JOptionPane.showMessageDialog(
                this,
                "Não foi possível imprimir a declaração de conteúdo",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE
            )
        }
        isVisible = false
    }

    private fun jButton2ActionPerformed(evt: ActionEvent) {
        if (jTable1!!.selectedRow != -1) {
            model.removeRow(jTable1!!.selectedRow)
            val novaLinha = arrayOf<Any?>(null, null, null)
            model.addRow(novaLinha)
        }
    }

    private fun fecharJanela(evt: WindowEvent) {
        val resposta = JOptionPane.showConfirmDialog(
            null,
            "A declaração de conteúdo não é armazenada. Deseja sair?",
            "Fechar Declaração de Conteúdo",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        )
        if (resposta == 0) defaultCloseOperation = 2
    }

    companion object {
        private val LOGGER = Logger.getLogger(TelaDeclararConteudo::class.java)
    }
}