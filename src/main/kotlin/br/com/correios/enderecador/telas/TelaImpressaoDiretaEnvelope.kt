package br.com.correios.enderecador.telas

import javax.swing.JFrame
import br.com.correios.enderecador.bean.DestinatarioBean
import javax.swing.JCheckBox
import javax.swing.JComboBox
import javax.swing.JLabel
import javax.swing.JRadioButton
import javax.swing.JTable
import br.com.correios.enderecador.util.EnderecadorObservable
import br.com.correios.enderecador.util.TextoCellRenderer
import javax.swing.DefaultCellEditor
import br.com.correios.enderecador.bean.RemetenteBean
import br.com.correios.enderecador.dao.RemetenteDao
import br.com.correios.enderecador.dao.DaoException
import javax.swing.JOptionPane
import br.com.correios.enderecador.util.Impressao
import br.com.correios.enderecador.excecao.EnderecadorExcecao
import javax.swing.ButtonGroup
import javax.swing.JToolBar
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.ImageIcon
import java.awt.event.ActionEvent
import javax.swing.BorderFactory
import javax.swing.DefaultComboBoxModel
import javax.swing.table.DefaultTableModel
import org.apache.log4j.Logger
import org.jdesktop.layout.GroupLayout
import java.awt.*
import java.util.*

class TelaImpressaoDiretaEnvelope private constructor(private val frmParent: Frame) : JFrame(), Observer {
    private val model = DestinatarioImpressaoTableModel("C")
    private var vecDestinatarioImpressao = Vector<DestinatarioBean?>()
    private var jchkImprimirRemetente: JCheckBox? = null
    private var jchkTelDestinatario: JCheckBox? = null
    private var jcmbRemetente: JComboBox<String?>? = null
    private var jcmbTamanhoFonte: JComboBox<String>? = null
    private var jlblEnvelope: JLabel? = null
    private var jrbEnvelopeC5: JRadioButton? = null
    private var jrbEnvelopeC6: JRadioButton? = null
    private var jtblDestinatarioImpressao: JTable? = null

    init {
        initComponents()
        configuracoesAdicionais()
        carregaRemetente()
        val observable = EnderecadorObservable.instance
        observable?.addObserver(this)
    }

    private fun configuracoesAdicionais() {
        var renderer = TextoCellRenderer(2)
        var coluna = jtblDestinatarioImpressao!!.columnModel.getColumn(0)
        coluna.cellRenderer = renderer
        coluna.preferredWidth = 300
        coluna = jtblDestinatarioImpressao!!.columnModel.getColumn(1)
        coluna.preferredWidth = 300
        coluna.cellRenderer = renderer
        coluna = jtblDestinatarioImpressao!!.columnModel.getColumn(2)
        coluna.preferredWidth = 120
        coluna.cellRenderer = renderer
        coluna = jtblDestinatarioImpressao!!.columnModel.getColumn(3)
        renderer = TextoCellRenderer(0)
        coluna.cellRenderer = renderer
        coluna.preferredWidth = 60
        coluna.width = 1
        val comboBox = JComboBox<String>()
        comboBox.addItem("Sim")
        comboBox.addItem("Não")
        coluna = jtblDestinatarioImpressao!!.columnModel.getColumn(4)
        coluna.cellEditor = DefaultCellEditor(comboBox)
        coluna.cellRenderer = renderer
    }

    private fun carregaRemetente() {
        try {
            jcmbRemetente!!.removeAllItems()
            val arrayRemetente = RemetenteDao.instance!!.recuperaRemetente("")
            for (remetenteBean in arrayRemetente) jcmbRemetente!!.addItem(remetenteBean.nome)
        } catch (e: DaoException) {
            logger.error(e.message, e)
            JOptionPane.showMessageDialog(
                frmParent,
                "Não foi possivel carregar relação de remetentes",
                "Endereçador ECT",
                JOptionPane.WARNING_MESSAGE
            )
        }
    }

    private fun imprimirEnvelope(nomeArquivo: String) {
        try {
            cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
            val impressao = Impressao()
            if (jchkImprimirRemetente!!.isSelected) {
                if (jcmbRemetente!!.itemCount == 0) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Por favor cadastre um remetente antes de fazer a impressão.",
                        "Endereçador ECT",
                        JOptionPane.WARNING_MESSAGE
                    )
                    return
                }
                impressao.imprimirEnvelope(
                    nomeArquivo,
                    jcmbRemetente!!.selectedItem as RemetenteBean,
                    vecDestinatarioImpressao,
                    jchkTelDestinatario!!.isSelected,
                    (Objects.requireNonNull(
                        jcmbTamanhoFonte!!.selectedItem
                    ) as String).substring(0, 1)
                )
            } else {
                impressao.imprimirEnvelope(
                    nomeArquivo,
                    null,
                    vecDestinatarioImpressao,
                    jchkTelDestinatario!!.isSelected,
                    (Objects.requireNonNull(
                        jcmbTamanhoFonte!!.selectedItem
                    ) as String).substring(0, 1)
                )
            }
        } catch (e: EnderecadorExcecao) {
            logger.error(e.message, e)
            JOptionPane.showMessageDialog(this, "Não foi possivel imprimir.", "Endereçador ECT", 2)
        } finally {
            cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
        }
    }

    private fun initComponents() {
        val buttonGroup1 = ButtonGroup()
        val jToolBar1 = JToolBar()
        val jbtSelecionarDestinatario = JButton()
        val jbtSelecionarGrupo = JButton()
        val jbtRemoverDestinatario = JButton()
        val jbtRemoverTodos = JButton()
        val jbtnVisializarAR = JButton()
        val jbtVisualizar = JButton()
        val jPanel1 = JPanel()
        val jPanel2 = JPanel()
        jchkTelDestinatario = JCheckBox()
        val jrbEnvelopeC6C5 = JRadioButton()
        jrbEnvelopeC5 = JRadioButton()
        jrbEnvelopeC6 = JRadioButton()
        jlblEnvelope = JLabel()
        val jPanel3 = JPanel()
        val jlblImprimirAR = JLabel()
        jcmbRemetente = JComboBox()
        jchkImprimirRemetente = JCheckBox()
        val jLabelTamanhoFonte = JLabel()
        jcmbTamanhoFonte = JComboBox()
        val jScrollPane1 = JScrollPane()
        jtblDestinatarioImpressao = JTable()
        isResizable = true
        title = "Impressão direta no envelope"
        jbtSelecionarDestinatario.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jbtSelecionarDestinatario.icon = ImageIcon(javaClass.getResource("/imagens/addusuario.gif"))
        jbtSelecionarDestinatario.text = "Selecionar destinatário"
        jbtSelecionarDestinatario.horizontalTextPosition = 0
        jbtSelecionarDestinatario.maximumSize = Dimension(110, 60)
        jbtSelecionarDestinatario.verticalTextPosition = 3
        jbtSelecionarDestinatario.addActionListener { evt: ActionEvent -> jbtSelecionarDestinatarioActionPerformed(evt) }
        jToolBar1.add(jbtSelecionarDestinatario)
        jbtSelecionarGrupo.font = Font("MS Sans Serif", 0, 9)
        jbtSelecionarGrupo.icon = ImageIcon(javaClass.getResource("/imagens/addusuarios.gif"))
        jbtSelecionarGrupo.text = "Selecionar grupo"
        jbtSelecionarGrupo.horizontalTextPosition = 0
        jbtSelecionarGrupo.maximumSize = Dimension(90, 60)
        jbtSelecionarGrupo.verticalTextPosition = 3
        jbtSelecionarGrupo.addActionListener { evt: ActionEvent -> jbtSelecionarGrupoActionPerformed(evt) }
        jToolBar1.add(jbtSelecionarGrupo)
        jbtRemoverDestinatario.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jbtRemoverDestinatario.icon = ImageIcon(javaClass.getResource("/imagens/remover.gif"))
        jbtRemoverDestinatario.text = "Remover destinatário"
        jbtRemoverDestinatario.horizontalTextPosition = 0
        jbtRemoverDestinatario.maximumSize = Dimension(110, 60)
        jbtRemoverDestinatario.verticalTextPosition = 3
        jbtRemoverDestinatario.addActionListener { evt: ActionEvent -> jbtRemoverDestinatarioActionPerformed(evt) }
        jToolBar1.add(jbtRemoverDestinatario)
        jbtRemoverTodos.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jbtRemoverTodos.icon = ImageIcon(javaClass.getResource("/imagens/removerTodos.gif"))
        jbtRemoverTodos.text = "Remover todos"
        jbtRemoverTodos.horizontalTextPosition = 0
        jbtRemoverTodos.maximumSize = Dimension(90, 60)
        jbtRemoverTodos.verticalTextPosition = 3
        jbtRemoverTodos.addActionListener { evt: ActionEvent -> jbtRemoverTodosActionPerformed(evt) }
        jToolBar1.add(jbtRemoverTodos)
        jbtnVisializarAR.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jbtnVisializarAR.icon = ImageIcon(javaClass.getResource("/imagens/print.gif"))
        jbtnVisializarAR.text = "Visualizar AR"
        jbtnVisializarAR.horizontalTextPosition = 0
        jbtnVisializarAR.maximumSize = Dimension(90, 60)
        jbtnVisializarAR.verticalTextPosition = 3
        jbtnVisializarAR.addActionListener { evt: ActionEvent -> jbtnVisializarARActionPerformed(evt) }
        jToolBar1.add(jbtnVisializarAR)
        jbtVisualizar.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jbtVisualizar.icon = ImageIcon(javaClass.getResource("/imagens/IMPRIMIR.gif"))
        jbtVisualizar.text = "Visualizar"
        jbtVisualizar.horizontalTextPosition = 0
        jbtVisualizar.maximumSize = Dimension(90, 60)
        jbtVisualizar.verticalTextPosition = 3
        jbtVisualizar.addActionListener { evt: ActionEvent -> jbtVisualizarActionPerformed(evt) }
        jToolBar1.add(jbtVisualizar)
        jPanel1.border = BorderFactory.createEtchedBorder()
        jPanel2.border =
            BorderFactory.createTitledBorder(null, "Escolha o tipo do envelope:", 0, 0, Font("Tahoma", 0, 10))
        jchkTelDestinatario!!.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jchkTelDestinatario!!.text = "Incluir telefone"
        jchkTelDestinatario!!.border = BorderFactory.createEmptyBorder(0, 0, 0, 0)
        jchkTelDestinatario!!.margin = Insets(0, 0, 0, 0)
        buttonGroup1.add(jrbEnvelopeC6C5)
        jrbEnvelopeC6C5.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jrbEnvelopeC6C5.text = "Formato C6/C5 (114x229mm)"
        jrbEnvelopeC6C5.border = BorderFactory.createEmptyBorder(0, 0, 0, 0)
        jrbEnvelopeC6C5.margin = Insets(0, 0, 0, 0)
        jrbEnvelopeC6C5.addActionListener { evt: ActionEvent -> jrbEnvelopeC6C5ActionPerformed(evt) }
        buttonGroup1.add(jrbEnvelopeC5)
        jrbEnvelopeC5!!.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jrbEnvelopeC5!!.isSelected = true
        jrbEnvelopeC5!!.text = "Formato C5 (162x229mm)"
        jrbEnvelopeC5!!.border = BorderFactory.createEmptyBorder(0, 0, 0, 0)
        jrbEnvelopeC5!!.margin = Insets(0, 0, 0, 0)
        jrbEnvelopeC5!!.addActionListener { evt: ActionEvent -> jrbEnvelopeC5ActionPerformed(evt) }
        buttonGroup1.add(jrbEnvelopeC6)
        jrbEnvelopeC6!!.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jrbEnvelopeC6!!.text = "Formato C6 (114x162mm)"
        jrbEnvelopeC6!!.border = BorderFactory.createEmptyBorder(0, 0, 0, 0)
        jrbEnvelopeC6!!.margin = Insets(0, 0, 0, 0)
        jrbEnvelopeC6!!.addActionListener { evt: ActionEvent -> jrbEnvelopeC6ActionPerformed(evt) }
        jlblEnvelope!!.horizontalAlignment = 0
        jlblEnvelope!!.icon = ImageIcon(javaClass.getResource("/imagens/envelopeC5.gif"))
        val jPanel2Layout = GroupLayout(jPanel2)
        jPanel2.layout = jPanel2Layout
        jPanel2Layout.horizontalGroup = jPanel2Layout.createParallelGroup(1)
            .add(
                2, jPanel2Layout.createSequentialGroup()
                    .add(jlblEnvelope, -2, 75, -2)
                    .addPreferredGap(0, -1, 32767)
                    .add(
                        jPanel2Layout.createParallelGroup(1)
                            .add(jrbEnvelopeC6, -2, 150, -2)
                            .add(jrbEnvelopeC5)
                    )
                    .addPreferredGap(0)
                    .add(
                        jPanel2Layout.createParallelGroup(1)
                            .add(jchkTelDestinatario)
                            .add(jrbEnvelopeC6C5)
                    )
                    .add(171, 171, 171)
            )
        jPanel2Layout.verticalGroup = jPanel2Layout.createParallelGroup(1)
            .add(
                jPanel2Layout.createSequentialGroup()
                    .add(
                        jPanel2Layout.createParallelGroup(1)
                            .add(
                                jPanel2Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .add(
                                        jPanel2Layout.createParallelGroup(3)
                                            .add(jrbEnvelopeC5)
                                            .add(jrbEnvelopeC6C5)
                                    )
                                    .addPreferredGap(0, -1, 32767)
                                    .add(
                                        jPanel2Layout.createParallelGroup(3)
                                            .add(jchkTelDestinatario)
                                            .add(jrbEnvelopeC6)
                                    )
                                    .add(8, 8, 8)
                            )
                            .add(jlblEnvelope, -1, -1, 32767)
                    )
                    .addContainerGap()
            )
        jPanel3.border = BorderFactory.createTitledBorder(" ")
        jlblImprimirAR.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jlblImprimirAR.text = "Selecione o remetente:"
        carregaRemetente()
        jchkImprimirRemetente!!.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jchkImprimirRemetente!!.text = "Imprimir remetente"
        jchkImprimirRemetente!!.border = BorderFactory.createEmptyBorder(0, 0, 0, 0)
        jchkImprimirRemetente!!.margin = Insets(0, 0, 0, 0)
        jLabelTamanhoFonte.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jLabelTamanhoFonte.text = "Tamanho da Fonte:"
        jcmbTamanhoFonte!!.model = DefaultComboBoxModel(arrayOf("Pequeno", "Médio", "Grande"))
        jcmbTamanhoFonte!!.addActionListener { evt: ActionEvent -> jcmbTamanhoFonteActionPerformed(evt) }
        val jPanel3Layout = GroupLayout(jPanel3)
        jPanel3.layout = jPanel3Layout
        jPanel3Layout.horizontalGroup = jPanel3Layout.createParallelGroup(1)
            .add(
                jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .add(
                        jPanel3Layout.createParallelGroup(1)
                            .add(jlblImprimirAR)
                            .add(jLabelTamanhoFonte)
                            .add(jchkImprimirRemetente)
                    )
                    .addPreferredGap(0)
                    .add(
                        jPanel3Layout.createParallelGroup(1)
                            .add(
                                jPanel3Layout.createSequentialGroup()
                                    .add(jcmbTamanhoFonte, -2, 89, -2)
                                    .add(0, 0, 32767)
                            )
                            .add(jcmbRemetente, 0, 182, 32767)
                    )
                    .addContainerGap()
            )
        jPanel3Layout.verticalGroup = jPanel3Layout.createParallelGroup(1)
            .add(
                2, jPanel3Layout.createSequentialGroup()
                    .add(jchkImprimirRemetente)
                    .addPreferredGap(0, -1, 32767)
                    .add(
                        jPanel3Layout.createParallelGroup(3)
                            .add(jLabelTamanhoFonte)
                            .add(jcmbTamanhoFonte, -2, -1, -2)
                    )
                    .addPreferredGap(0)
                    .add(
                        jPanel3Layout.createParallelGroup(3)
                            .add(jlblImprimirAR)
                            .add(jcmbRemetente, -2, -1, -2)
                    )
                    .add(5, 5, 5)
            )
        val jPanel1Layout = GroupLayout(jPanel1)
        jPanel1.layout = jPanel1Layout
        jPanel1Layout.horizontalGroup = jPanel1Layout.createParallelGroup(1)
            .add(
                jPanel1Layout.createSequentialGroup()
                    .add(jPanel2, -2, 409, -2)
                    .addPreferredGap(0).add(jPanel3, -1, -1, 32767)
            )
        jPanel1Layout.verticalGroup = jPanel1Layout.createParallelGroup(1)
            .add(
                2, jPanel1Layout.createSequentialGroup()
                    .add(
                        jPanel1Layout.createParallelGroup(2)
                            .add(1, jPanel2, -1, -1, 32767)
                            .add(jPanel3, -1, -1, 32767)
                    )
                    .addContainerGap()
            )
        jtblDestinatarioImpressao!!.model = DefaultTableModel(
            arrayOf(
                arrayOf(null, null, null, null),
                arrayOf(null, null, null, null),
                arrayOf(null, null, null, null),
                arrayOf(null, null, null, null)
            ), arrayOf("Title 1", "Title 2", "Title 3", "Title 4")
        )
        jtblDestinatarioImpressao!!.model = model
        jScrollPane1.setViewportView(jtblDestinatarioImpressao)
        val layout = GroupLayout(contentPane)
        contentPane.layout = layout
        layout.horizontalGroup = layout.createParallelGroup(1)
            .add(jToolBar1, -1, -1, 32767)
            .add(jPanel1, -1, -1, 32767)
            .add(jScrollPane1)
        layout.verticalGroup = layout.createParallelGroup(1)
            .add(
                layout.createSequentialGroup()
                    .add(jToolBar1, -2, 59, -2)
                    .addPreferredGap(0).add(jPanel1, -2, 103, -2)
                    .addPreferredGap(0).add(jScrollPane1, -1, 329, 32767)
                    .addContainerGap()
            )
        pack()
    }

    private fun jbtnVisializarARActionPerformed(evt: ActionEvent) {
        val impressao = Impressao()
        if (jcmbRemetente!!.itemCount == 0) {
            JOptionPane.showMessageDialog(
                this,
                "Por favor cadastre um remetente antes de fazer a impressão.",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE
            )
            return
        }
        if (vecDestinatarioImpressao.size < 1) {
            JOptionPane.showMessageDialog(
                this,
                "Não existe nenhum destinatário selecionado!",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE
            )
            return
        }
        try {
            impressao.imprimirAR(
                "avisoRecebimento.jasper",
                jcmbRemetente!!.selectedItem as RemetenteBean,
                vecDestinatarioImpressao
            )
        } catch (ex: EnderecadorExcecao) {
            logger.error(ex.message, ex)
            JOptionPane.showMessageDialog(
                this,
                "Não foi possível imprimir o AR",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE
            )
        }
    }

    private fun jbtVisualizarActionPerformed(evt: ActionEvent) {
        if (vecDestinatarioImpressao.size <= 0) {
            JOptionPane.showMessageDialog(
                this,
                "Não existe nenhum destinatário selecionado!",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE
            )
            return
        }
        if (jrbEnvelopeC5!!.isSelected) {
            imprimirEnvelope("EnvelopeC5.jasper")
        } else if (jrbEnvelopeC6!!.isSelected) {
            imprimirEnvelope("EnvelopeC6.jasper")
        } else {
            imprimirEnvelope("EnvelopeC6C5.jasper")
        }
    }

    private fun jrbEnvelopeC6C5ActionPerformed(evt: ActionEvent) {
        jlblEnvelope!!.icon = ImageIcon(javaClass.getResource("/imagens/envelopeC5C6.gif"))
    }

    private fun jrbEnvelopeC5ActionPerformed(evt: ActionEvent) {
        jlblEnvelope!!.icon = ImageIcon(javaClass.getResource("/imagens/envelopeC5.gif"))
    }

    private fun jrbEnvelopeC6ActionPerformed(evt: ActionEvent) {
        jlblEnvelope!!.icon = ImageIcon(javaClass.getResource("/imagens/envelopeC6.gif"))
    }

    private fun jbtRemoverTodosActionPerformed(evt: ActionEvent) {
        vecDestinatarioImpressao.removeAllElements()
        model.destinatario = vecDestinatarioImpressao
        jtblDestinatarioImpressao!!.model = model
    }

    private fun jbtRemoverDestinatarioActionPerformed(evt: ActionEvent) {
        if (jtblDestinatarioImpressao!!.selectedRow == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Não existe nenhum destinatário selecionado.",
                "Endereçador ECT",
                JOptionPane.INFORMATION_MESSAGE
            )
            return
        }
        val selectedRows = jtblDestinatarioImpressao!!.selectedRows
        vecDestinatarioImpressao = Vector(model.destinatario)
        for (selectedRow in selectedRows) vecDestinatarioImpressao.remove(model.getElementAt(selectedRow))
        model.destinatario = vecDestinatarioImpressao
    }

    private fun jbtSelecionarGrupoActionPerformed(evt: ActionEvent) {
        val telaPesquisarGrupo = TelaPesquisarGrupo(frmParent, true, vecDestinatarioImpressao)
        telaPesquisarGrupo.isVisible = true
        model.destinatario = vecDestinatarioImpressao
        jtblDestinatarioImpressao!!.model = model
    }

    private fun jbtSelecionarDestinatarioActionPerformed(evt: ActionEvent) {
        val telaPesquisaDestinatario = TelaPesquisarDestinatario(frmParent, true, vecDestinatarioImpressao)
        telaPesquisaDestinatario.isVisible = true
        model.destinatario = vecDestinatarioImpressao
        jtblDestinatarioImpressao!!.model = model
    }

    private fun jcmbTamanhoFonteActionPerformed(evt: ActionEvent) {}
    override fun update(o: Observable, arg: Any) {
        if (arg is RemetenteBean) {
            val remetente = arg
            jcmbRemetente!!.removeItem(remetente)
            jcmbRemetente!!.addItem(remetente.nome)
        } else if (arg is DestinatarioBean) {
            val destinatario = arg
            val index = model.indexOf(destinatario)
            if (index != -1) model.setDestinatario(index, destinatario)
        } else if (arg is List<*>) {
            for (listaUsuario in arg) {
                if (listaUsuario is DestinatarioBean) {
                    model.removeDestinatario(listaUsuario as DestinatarioBean?)
                } else if (listaUsuario is RemetenteBean) {
                    jcmbRemetente!!.removeItem(listaUsuario)
                }
            }
        }
    }

    companion object {
        var logger = Logger.getLogger(TelaEditarRemetente::class.java)
        private var instance: TelaImpressaoDiretaEnvelope? = null
        fun getInstance(parent: Frame): TelaImpressaoDiretaEnvelope? {
            if (instance == null) instance = TelaImpressaoDiretaEnvelope(parent)
            return instance
        }
    }
}