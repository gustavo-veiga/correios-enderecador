package br.com.correios.enderecador.telas

import javax.swing.JFrame
import br.com.correios.enderecador.bean.DestinatarioBean
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JComboBox
import javax.swing.JLabel
import javax.swing.JRadioButton
import javax.swing.JTable
import javax.swing.JTextField
import br.com.correios.enderecador.util.EnderecadorObservable
import br.com.correios.enderecador.util.TextoCellRenderer
import javax.swing.DefaultCellEditor
import br.com.correios.enderecador.bean.RemetenteBean
import br.com.correios.enderecador.dao.RemetenteDao
import br.com.correios.enderecador.dao.DaoException
import javax.swing.JOptionPane
import javax.swing.ButtonGroup
import javax.swing.JToolBar
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.ImageIcon
import java.awt.event.ActionEvent
import javax.swing.BorderFactory
import javax.swing.DefaultComboBoxModel
import br.com.correios.enderecador.util.DocumentoPersonalizado
import javax.swing.table.DefaultTableModel
import br.com.correios.enderecador.util.Impressao
import br.com.correios.enderecador.excecao.EnderecadorExcecao
import org.apache.log4j.Logger
import org.jdesktop.layout.GroupLayout
import java.awt.*
import java.util.*

class TelaImpressaoEnvelope private constructor(private val frmParent: Frame) : JFrame(), Observer {
    private val model = DestinatarioImpressaoTableModel("C")
    private var vecDestinatarioImpressao = Vector<DestinatarioBean?>()
    private var relatorio = "A4Envelope14.jasper"
    private var imprimirTratamento = false
    private var jbtRemoverDestinatario: JButton? = null
    private var jbtRemoverTodos: JButton? = null
    private var jbtSelecionarDestinatario: JButton? = null
    private var jbtSelecionarGrupo: JButton? = null
    private var jbtnVisializarAR: JButton? = null
    private var jchkTelDestinatario: JCheckBox? = null
    private var jchkTelRemetente: JCheckBox? = null
    private var jcmbFolha: JComboBox<String>? = null
    private var jcmbRemetente: JComboBox<String?>? = null
    private var jcmbTamanhoFonte: JComboBox<String>? = null
    private var jlNumeroVezes: JLabel? = null
    private var jlSelecioneRemetente: JLabel? = null
    private var jlblImpressao: JLabel? = null
    private var jrbt10Rotulos: JRadioButton? = null
    private var jrbt14Rotulos: JRadioButton? = null
    private var jrbtDestinatario: JRadioButton? = null
    private var jrbtRemetente: JRadioButton? = null
    private var jrbtRemetenteDestinatario: JRadioButton? = null
    private var jtblDestinatarioImpressao: JTable? = null
    private var jtxtNumeroEtiqueta: JTextField? = null
    private var jtxtNumeroVezes: JTextField? = null

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
                2
            )
        }
    }

    private fun initComponents() {
        val jbtgTipoEtiqueta = ButtonGroup()
        val jbtgTipoImpressao = ButtonGroup()
        val jToolBar1 = JToolBar()
        jbtSelecionarDestinatario = JButton()
        jbtSelecionarGrupo = JButton()
        jbtRemoverDestinatario = JButton()
        jbtRemoverTodos = JButton()
        jbtnVisializarAR = JButton()
        val jbtVisualizar = JButton()
        val jPanel1 = JPanel()
        val jPanel2 = JPanel()
        jlblImpressao = JLabel()
        jrbt14Rotulos = JRadioButton()
        jrbt10Rotulos = JRadioButton()
        val jLabel2 = JLabel()
        val jLabel1 = JLabel()
        jcmbFolha = JComboBox()
        val jLabelTamanhoFonte = JLabel()
        jcmbTamanhoFonte = JComboBox()
        val jPanel3 = JPanel()
        jlSelecioneRemetente = JLabel()
        jlNumeroVezes = JLabel()
        val jLabel5 = JLabel()
        jchkTelRemetente = JCheckBox()
        jchkTelDestinatario = JCheckBox()
        jcmbRemetente = JComboBox()
        jtxtNumeroVezes = JTextField()
        val jLabel6 = JLabel()
        jtxtNumeroEtiqueta = JTextField()
        val jScrollPane1 = JScrollPane()
        jtblDestinatarioImpressao = JTable()
        val jPanel4 = JPanel()
        val jLabel7 = JLabel()
        jrbtRemetente = JRadioButton()
        jrbtDestinatario = JRadioButton()
        jrbtRemetenteDestinatario = JRadioButton()
        isResizable = true
        title = "Etiquetas para cartas"
        jbtSelecionarDestinatario!!.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jbtSelecionarDestinatario!!.icon = ImageIcon(javaClass.getResource("/imagens/addusuario.gif"))
        jbtSelecionarDestinatario!!.text = "Selecionar destinatário"
        jbtSelecionarDestinatario!!.isEnabled = false
        jbtSelecionarDestinatario!!.horizontalTextPosition = 0
        jbtSelecionarDestinatario!!.maximumSize = Dimension(110, 60)
        jbtSelecionarDestinatario!!.verticalTextPosition = 3
        jbtSelecionarDestinatario!!.addActionListener { evt: ActionEvent -> jbtSelecionarDestinatarioActionPerformed(evt) }
        jToolBar1.add(jbtSelecionarDestinatario)
        jbtSelecionarGrupo!!.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jbtSelecionarGrupo!!.icon = ImageIcon(javaClass.getResource("/imagens/addusuarios.gif"))
        jbtSelecionarGrupo!!.text = "Selecionar grupo"
        jbtSelecionarGrupo!!.isEnabled = false
        jbtSelecionarGrupo!!.horizontalTextPosition = 0
        jbtSelecionarGrupo!!.maximumSize = Dimension(90, 60)
        jbtSelecionarGrupo!!.verticalTextPosition = 3
        jbtSelecionarGrupo!!.addActionListener { evt: ActionEvent -> jbtSelecionarGrupoActionPerformed(evt) }
        jToolBar1.add(jbtSelecionarGrupo)
        jbtRemoverDestinatario!!.font = Font("MS Sans Serif", 0, 9)
        jbtRemoverDestinatario!!.icon = ImageIcon(javaClass.getResource("/imagens/remover.gif"))
        jbtRemoverDestinatario!!.text = "Remover destinatário"
        jbtRemoverDestinatario!!.isEnabled = false
        jbtRemoverDestinatario!!.horizontalTextPosition = 0
        jbtRemoverDestinatario!!.maximumSize = Dimension(110, 60)
        jbtRemoverDestinatario!!.verticalTextPosition = 3
        jbtRemoverDestinatario!!.addActionListener { evt: ActionEvent -> jbtRemoverDestinatarioActionPerformed(evt) }
        jToolBar1.add(jbtRemoverDestinatario)
        jbtRemoverTodos!!.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jbtRemoverTodos!!.icon = ImageIcon(javaClass.getResource("/imagens/removerTodos.gif"))
        jbtRemoverTodos!!.text = "Remover todos"
        jbtRemoverTodos!!.isEnabled = false
        jbtRemoverTodos!!.horizontalTextPosition = 0
        jbtRemoverTodos!!.maximumSize = Dimension(90, 60)
        jbtRemoverTodos!!.verticalTextPosition = 3
        jbtRemoverTodos!!.addActionListener { evt: ActionEvent -> jbtRemoverTodosActionPerformed(evt) }
        jToolBar1.add(jbtRemoverTodos)
        jbtnVisializarAR!!.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jbtnVisializarAR!!.icon = ImageIcon(javaClass.getResource("/imagens/print.gif"))
        jbtnVisializarAR!!.text = "Visualizar AR"
        jbtnVisializarAR!!.isEnabled = false
        jbtnVisializarAR!!.horizontalTextPosition = 0
        jbtnVisializarAR!!.maximumSize = Dimension(90, 60)
        jbtnVisializarAR!!.verticalTextPosition = 3
        jbtnVisializarAR!!.addActionListener { evt: ActionEvent -> jbtnVisializarARActionPerformed(evt) }
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
        jPanel2.border = BorderFactory.createTitledBorder(
            null,
            "Escolha o tamanho do rótulo que deseja gerar",
            0,
            0,
            Font("Tahoma", 0, 10)
        )
        jlblImpressao!!.icon = ImageIcon(javaClass.getResource("/imagens/tipo_14etq.gif"))
        jbtgTipoEtiqueta.add(jrbt14Rotulos)
        jrbt14Rotulos!!.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jrbt14Rotulos!!.isSelected = true
        jrbt14Rotulos!!.text = "14 rótulos por vez, distribuidos numa folha"
        jrbt14Rotulos!!.border = BorderFactory.createEmptyBorder(0, 0, 0, 0)
        jrbt14Rotulos!!.margin = Insets(0, 0, 0, 0)
        jrbt14Rotulos!!.addActionListener { evt: ActionEvent -> jrbt14RotulosActionPerformed(evt) }
        jbtgTipoEtiqueta.add(jrbt10Rotulos)
        jrbt10Rotulos!!.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jrbt10Rotulos!!.text = "10 rótulos por vez, distribuídos numa folha "
        jrbt10Rotulos!!.border = BorderFactory.createEmptyBorder(0, 0, 0, 0)
        jrbt10Rotulos!!.margin = Insets(0, 0, 0, 0)
        jrbt10Rotulos!!.addActionListener { evt: ActionEvent -> jrbt10RotulosActionPerformed(evt) }
        jLabel2.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jLabel2.text = "(com impressão do tratamento)"
        jLabel1.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jLabel1.text = "Folhas do tamanho:"
        jcmbFolha!!.model = DefaultComboBoxModel(arrayOf("A4", "Carta"))
        jcmbFolha!!.addActionListener { evt: ActionEvent -> jcmbFolhaActionPerformed(evt) }
        jLabelTamanhoFonte.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jLabelTamanhoFonte.text = "Tamanho da Fonte:"
        jcmbTamanhoFonte!!.model = DefaultComboBoxModel(arrayOf("Pequeno", "Médio", "Grande"))
        jcmbTamanhoFonte!!.addActionListener { evt: ActionEvent -> jcmbTamanhoFonteActionPerformed(evt) }
        val jPanel2Layout = GroupLayout(jPanel2)
        jPanel2.layout = jPanel2Layout
        jPanel2Layout.horizontalGroup = jPanel2Layout.createParallelGroup(1)
            .add(
                2, jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .add(jlblImpressao, -2, 87, -2)
                    .add(
                        jPanel2Layout.createParallelGroup(1)
                            .add(
                                jPanel2Layout.createSequentialGroup()
                                    .add(21, 21, 21)
                                    .add(
                                        jPanel2Layout.createParallelGroup(1)
                                            .add(jrbt10Rotulos)
                                            .add(
                                                jPanel2Layout.createSequentialGroup()
                                                    .add(17, 17, 17)
                                                    .add(jLabel2)
                                            )
                                    )
                            )
                            .add(
                                jPanel2Layout.createSequentialGroup()
                                    .add(20, 20, 20)
                                    .add(jrbt14Rotulos)
                            )
                            .add(
                                jPanel2Layout.createSequentialGroup()
                                    .add(18, 18, 18)
                                    .add(
                                        jPanel2Layout.createParallelGroup(1)
                                            .add(
                                                jPanel2Layout.createSequentialGroup()
                                                    .add(jLabel1)
                                                    .addPreferredGap(0)
                                                    .add(jcmbFolha, -2, -1, -2)
                                            )
                                            .add(
                                                jPanel2Layout.createSequentialGroup()
                                                    .add(jLabelTamanhoFonte)
                                                    .addPreferredGap(0)
                                                    .add(jcmbTamanhoFonte, -2, 89, -2)
                                            )
                                    )
                            )
                    )
                    .add(102, 102, 102)
            )
        jPanel2Layout.verticalGroup = jPanel2Layout.createParallelGroup(1)
            .add(
                jPanel2Layout.createSequentialGroup()
                    .add(
                        jPanel2Layout.createParallelGroup(1).add(jlblImpressao)
                            .add(
                                jPanel2Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .add(jrbt14Rotulos)
                                    .add(17, 17, 17)
                                    .add(jrbt10Rotulos)
                                    .addPreferredGap(0)
                                    .add(jLabel2)
                                    .addPreferredGap(0)
                                    .add(
                                        jPanel2Layout.createParallelGroup(3)
                                            .add(jLabel1)
                                            .add(jcmbFolha, -2, -1, -2)
                                    )
                                    .addPreferredGap(0)
                                    .add(
                                        jPanel2Layout.createParallelGroup(3)
                                            .add(jLabelTamanhoFonte)
                                            .add(jcmbTamanhoFonte, -2, -1, -2)
                                    )
                            )
                    )
                    .addContainerGap(-1, 32767)
            )
        jPanel3.border = BorderFactory.createTitledBorder(" ")
        jlSelecioneRemetente!!.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jlSelecioneRemetente!!.text = "Selecione o remetente:"
        jlNumeroVezes!!.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jlNumeroVezes!!.text = "Quantas vezes deseja imprimir o remetente:"
        jLabel5.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jLabel5.text = "Incluir telefone na etiqueta:"
        jchkTelRemetente!!.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jchkTelRemetente!!.text = "do remetente"
        jchkTelRemetente!!.border = BorderFactory.createEmptyBorder(0, 0, 0, 0)
        jchkTelRemetente!!.margin = Insets(0, 0, 0, 0)
        jchkTelDestinatario!!.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jchkTelDestinatario!!.text = "do destinatário"
        jchkTelDestinatario!!.border = BorderFactory.createEmptyBorder(0, 0, 0, 0)
        jchkTelDestinatario!!.isEnabled = false
        jchkTelDestinatario!!.margin = Insets(0, 0, 0, 0)
        carregaRemetente()
        jtxtNumeroVezes!!.document = DocumentoPersonalizado(4, 1)
        jLabel6.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jLabel6.text = "Quero imprimir a partir da etiqueta nº:"
        jtxtNumeroEtiqueta!!.document = DocumentoPersonalizado(3, 1)
        val jPanel3Layout = GroupLayout(jPanel3)
        jPanel3.layout = jPanel3Layout
        jPanel3Layout.horizontalGroup = jPanel3Layout.createParallelGroup(1)
            .add(
                jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .add(
                        jPanel3Layout.createParallelGroup(1)
                            .add(
                                jPanel3Layout.createSequentialGroup()
                                    .add(jlSelecioneRemetente)
                                    .addPreferredGap(0)
                                    .add(jcmbRemetente, -2, -1, -2)
                            )
                            .add(
                                jPanel3Layout.createSequentialGroup()
                                    .add(jlNumeroVezes)
                                    .addPreferredGap(0)
                                    .add(jtxtNumeroVezes, -2, 66, -2)
                            )
                            .add(
                                jPanel3Layout.createSequentialGroup()
                                    .add(jLabel6)
                                    .addPreferredGap(0)
                                    .add(jtxtNumeroEtiqueta, -2, 66, -2)
                            )
                            .add(
                                jPanel3Layout.createSequentialGroup()
                                    .add(jLabel5)
                                    .addPreferredGap(0)
                                    .add(jchkTelDestinatario)
                                    .addPreferredGap(0)
                                    .add(jchkTelRemetente)
                            )
                    )
                    .addContainerGap(105, 32767)
            )
        jPanel3Layout.verticalGroup = jPanel3Layout.createParallelGroup(1)
            .add(
                jPanel3Layout.createSequentialGroup()
                    .add(
                        jPanel3Layout.createParallelGroup(3)
                            .add(jlSelecioneRemetente)
                            .add(jcmbRemetente, -2, -1, -2)
                    )
                    .addPreferredGap(0, -1, 32767)
                    .add(
                        jPanel3Layout.createParallelGroup(3)
                            .add(jLabel6)
                            .add(jtxtNumeroEtiqueta, -2, -1, -2)
                    )
                    .addPreferredGap(0)
                    .add(
                        jPanel3Layout.createParallelGroup(3)
                            .add(jlNumeroVezes)
                            .add(jtxtNumeroVezes, -2, -1, -2)
                    )
                    .add(17, 17, 17)
                    .add(
                        jPanel3Layout.createParallelGroup(3)
                            .add(jLabel5)
                            .add(jchkTelDestinatario)
                            .add(jchkTelRemetente)
                    )
                    .add(23, 23, 23)
            )
        val jPanel1Layout = GroupLayout(jPanel1)
        jPanel1.layout = jPanel1Layout
        jPanel1Layout.horizontalGroup = jPanel1Layout.createParallelGroup(1)
            .add(
                jPanel1Layout.createSequentialGroup()
                    .add(jPanel2, -2, 353, -2)
                    .addPreferredGap(0).add(jPanel3, -1, -1, 32767)
            )
        jPanel1Layout.verticalGroup = jPanel1Layout.createParallelGroup(1)
            .add(jPanel2, -1, -1, 32767)
            .add(jPanel3, -1, -1, 32767)
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
        jPanel4.border = BorderFactory.createEtchedBorder()
        jLabel7.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jLabel7.text = "Quero imprimir etiquetas de:"
        jbtgTipoImpressao.add(jrbtRemetente)
        jrbtRemetente!!.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jrbtRemetente!!.isSelected = true
        jrbtRemetente!!.text = "Remetente"
        jrbtRemetente!!.border = BorderFactory.createEmptyBorder(0, 0, 0, 0)
        jrbtRemetente!!.margin = Insets(0, 0, 0, 0)
        jrbtRemetente!!.addActionListener { evt: ActionEvent -> jrbtRemetenteActionPerformed(evt) }
        jbtgTipoImpressao.add(jrbtDestinatario)
        jrbtDestinatario!!.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jrbtDestinatario!!.text = "Destinatário"
        jrbtDestinatario!!.border = BorderFactory.createEmptyBorder(0, 0, 0, 0)
        jrbtDestinatario!!.margin = Insets(0, 0, 0, 0)
        jrbtDestinatario!!.addActionListener { evt: ActionEvent -> jrbtDestinatarioActionPerformed(evt) }
        jbtgTipoImpressao.add(jrbtRemetenteDestinatario)
        jrbtRemetenteDestinatario!!.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jrbtRemetenteDestinatario!!.text = "Remetente e destinatário"
        jrbtRemetenteDestinatario!!.border = BorderFactory.createEmptyBorder(0, 0, 0, 0)
        jrbtRemetenteDestinatario!!.margin = Insets(0, 0, 0, 0)
        jrbtRemetenteDestinatario!!.addActionListener { evt: ActionEvent -> jrbtRemetenteDestinatarioActionPerformed(evt) }
        val jPanel4Layout = GroupLayout(jPanel4)
        jPanel4.layout = jPanel4Layout
        jPanel4Layout.horizontalGroup = jPanel4Layout.createParallelGroup(1)
            .add(
                jPanel4Layout.createSequentialGroup()
                    .addContainerGap()
                    .add(jLabel7).add(17, 17, 17)
                    .add(jrbtRemetente)
                    .add(95, 95, 95)
                    .add(jrbtDestinatario)
                    .add(88, 88, 88)
                    .add(jrbtRemetenteDestinatario)
                    .addContainerGap(172, 32767)
            )
        jPanel4Layout.verticalGroup = jPanel4Layout.createParallelGroup(1)
            .add(
                jPanel4Layout.createSequentialGroup()
                    .addContainerGap()
                    .add(
                        jPanel4Layout.createParallelGroup(3)
                            .add(jrbtRemetenteDestinatario)
                            .add(jLabel7)
                            .add(jrbtRemetente)
                            .add(jrbtDestinatario)
                    )
                    .addContainerGap(-1, 32767)
            )
        val layout = GroupLayout(contentPane)
        contentPane.layout = layout
        layout.horizontalGroup = layout.createParallelGroup(1)
            .add(jToolBar1, -1, 800, 32767)
            .add(jPanel4, -1, -1, 32767)
            .add(jPanel1, -1, -1, 32767)
            .add(jScrollPane1, -1, 800, 32767)
        layout.verticalGroup = layout.createParallelGroup(1)
            .add(
                layout.createSequentialGroup()
                    .add(jToolBar1, -2, 59, -2)
                    .addPreferredGap(0)
                    .add(jPanel4, -2, -1, -2)
                    .addPreferredGap(0)
                    .add(jPanel1, -2, -1, -2)
                    .addPreferredGap(0)
                    .add(jScrollPane1, -1, 183, 32767)
            )
        pack()
    }

    private fun jbtnVisializarARActionPerformed(evt: ActionEvent) {
        val impressao = Impressao()
        if (vecDestinatarioImpressao.size < 1) {
            JOptionPane.showMessageDialog(
                this,
                "Não existe nenhum destinatário selecionado!",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE
            )
            return
        }
        if (jcmbRemetente!!.itemCount == 0) {
            JOptionPane.showMessageDialog(
                this,
                "Por favor cadastre um remetente antes de fazer a impressão.",
                "Endereçador ECT",
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
            logger.error(ex.message, ex as Throwable)
            JOptionPane.showMessageDialog(
                this,
                "Não foi possível imprimir o AR",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE
            )
        }
    }

    private fun getSiglaTamanhoFonte(tamanhoFonte: String): String {
        return tamanhoFonte.substring(0, 1)
    }

    private fun jbtVisualizarActionPerformed(evt: ActionEvent) {
        val impressao = Impressao()
        if ((jrbtDestinatario!!.isSelected || jrbtRemetenteDestinatario!!.isSelected) && vecDestinatarioImpressao.size < 1) {
            JOptionPane.showMessageDialog(
                this,
                "Não existe nenhum destinatário selecionado!",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE
            )
            jtxtNumeroVezes!!.requestFocus()
            return
        }
        try {
            if (jtxtNumeroEtiqueta!!.text.trim { it <= ' ' } == "") jtxtNumeroEtiqueta!!.text = "1"
            if (jtxtNumeroVezes!!.text.trim { it <= ' ' } == "") jtxtNumeroVezes!!.text = "1"
            if (jrbt14Rotulos!!.isSelected && jtxtNumeroEtiqueta!!.text.toInt() > 14) {
                JOptionPane.showMessageDialog(
                    this,
                    "Posição inicial para impressão de etiquetas deve ser um número de 1 a 14.",
                    "Endereçador",
                    2
                )
                return
            }
            if (jrbt10Rotulos!!.isSelected && jtxtNumeroEtiqueta!!.text.toInt() > 10) {
                JOptionPane.showMessageDialog(
                    this,
                    "Posição inicial para impressão de etiquetas deve ser um número de 1 a 10.",
                    "Endereçador",
                    2
                )
                return
            }
            if (jrbtDestinatario!!.isSelected) {
                impressao.impressaoCarta(
                    relatorio,
                    null,
                    vecDestinatarioImpressao,
                    0,
                    jtxtNumeroEtiqueta!!.text.toInt(),
                    false,
                    jchkTelDestinatario!!.isSelected,
                    imprimirTratamento,
                    (Objects.requireNonNull(
                        jcmbTamanhoFonte!!.selectedItem
                    ) as String).substring(0, 1)
                )
            } else if (jrbtRemetente!!.isSelected) {
                if (jcmbRemetente!!.itemCount == 0) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Por favor cadastre um remetente antes de fazer a impressão.",
                        "Endereçador ECT",
                        2
                    )
                    return
                }
                impressao.impressaoCarta(
                    relatorio,
                    jcmbRemetente!!.selectedItem as RemetenteBean,
                    null,
                    jtxtNumeroVezes!!.text.toInt(),
                    jtxtNumeroEtiqueta!!.text.toInt(),
                    jchkTelRemetente!!.isSelected,
                    false,
                    imprimirTratamento,
                    (Objects.requireNonNull(
                        jcmbTamanhoFonte!!.selectedItem
                    ) as String).substring(0, 1)
                )
            } else {
                if (jcmbRemetente!!.itemCount == 0) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Por favor cadastre um remetente antes de fazer a impressão.",
                        "Endereçador ECT",
                        2
                    )
                    return
                }
                impressao.impressaoCarta(
                    relatorio,
                    jcmbRemetente!!.selectedItem as RemetenteBean,
                    vecDestinatarioImpressao,
                    jtxtNumeroVezes!!.text.toInt(),
                    jtxtNumeroEtiqueta!!.text.toInt(),
                    jchkTelRemetente!!.isSelected,
                    jchkTelDestinatario!!.isSelected,
                    imprimirTratamento,
                    (Objects.requireNonNull(
                        jcmbTamanhoFonte!!.selectedItem
                    ) as String).substring(0, 1)
                )
            }
        } catch (ex: EnderecadorExcecao) {
            logger.error(ex.message, ex as Throwable)
        }
    }

    private fun jcmbFolhaActionPerformed(evt: ActionEvent) {
        if (jcmbFolha!!.selectedItem == "A4") {
            if (jrbt14Rotulos!!.isSelected) {
                relatorio = "A4Envelope14.jasper"
            } else if (jrbt10Rotulos!!.isSelected) {
                relatorio = "A4Envelope10.jasper"
            }
        } else if (jrbt14Rotulos!!.isSelected) {
            relatorio = "CartaEnvelope14.jasper"
        } else if (jrbt10Rotulos!!.isSelected) {
            relatorio = "CartaEnvelope10.jasper"
        }
    }

    private fun jrbtRemetenteDestinatarioActionPerformed(evt: ActionEvent) {
        jbtSelecionarDestinatario!!.isEnabled = true
        jbtRemoverDestinatario!!.isEnabled = true
        jbtSelecionarGrupo!!.isEnabled = true
        jbtRemoverTodos!!.isEnabled = true
        jtxtNumeroVezes!!.isEnabled = true
        jlNumeroVezes!!.isEnabled = true
        jcmbRemetente!!.isEnabled = true
        jcmbRemetente!!.isEnabled = true
        jlSelecioneRemetente!!.isEnabled = true
        jtxtNumeroVezes!!.background = Color.white
        jchkTelDestinatario!!.isEnabled = true
        jchkTelRemetente!!.isEnabled = true
        jbtnVisializarAR!!.isEnabled = true
    }

    private fun jrbtDestinatarioActionPerformed(evt: ActionEvent) {
        jbtSelecionarDestinatario!!.isEnabled = true
        jbtRemoverDestinatario!!.isEnabled = true
        jbtSelecionarGrupo!!.isEnabled = true
        jbtRemoverTodos!!.isEnabled = true
        jtxtNumeroVezes!!.isEnabled = false
        jtxtNumeroVezes!!.text = ""
        jtxtNumeroVezes!!.background = Color.lightGray
        jlNumeroVezes!!.isEnabled = false
        jcmbRemetente!!.isEnabled = false
        jlSelecioneRemetente!!.isEnabled = false
        jchkTelDestinatario!!.isEnabled = true
        jchkTelRemetente!!.isEnabled = false
        jbtnVisializarAR!!.isEnabled = false
    }

    private fun jrbtRemetenteActionPerformed(evt: ActionEvent) {
        jbtSelecionarDestinatario!!.isEnabled = false
        jbtRemoverDestinatario!!.isEnabled = false
        jbtSelecionarGrupo!!.isEnabled = false
        jbtRemoverTodos!!.isEnabled = false
        jtxtNumeroVezes!!.isEnabled = true
        jlNumeroVezes!!.isEnabled = true
        jcmbRemetente!!.isEnabled = true
        jtxtNumeroVezes!!.background = Color.white
        jlSelecioneRemetente!!.isEnabled = true
        vecDestinatarioImpressao.removeAllElements()
        model.destinatario = vecDestinatarioImpressao
        jchkTelDestinatario!!.isEnabled = false
        jchkTelRemetente!!.isEnabled = true
        jbtnVisializarAR!!.isEnabled = false
    }

    private fun jrbt10RotulosActionPerformed(evt: ActionEvent) {
        jlblImpressao!!.icon = ImageIcon(javaClass.getResource("/imagens/tipo_10etq.gif"))
        if (jcmbFolha!!.selectedItem == "A4") {
            relatorio = "A4Envelope10.jasper"
        } else {
            relatorio = "CartaEnvelope10.jasper"
        }
        imprimirTratamento = true
    }

    private fun jrbt14RotulosActionPerformed(evt: ActionEvent) {
        jlblImpressao!!.icon = ImageIcon(javaClass.getResource("/imagens/tipo_14etq.gif"))
        if (jcmbFolha!!.selectedItem == "A4") {
            relatorio = "A4Envelope14.jasper"
        } else {
            relatorio = "CartaEnvelope14.jasper"
        }
        imprimirTratamento = false
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
        private val logger = Logger.getLogger(TelaImpressaoEnvelope::class.java)
        private var instance: TelaImpressaoEnvelope? = null
        fun getInstance(parent: Frame): TelaImpressaoEnvelope? {
            if (instance == null) instance = TelaImpressaoEnvelope(parent)
            return instance
        }
    }
}
