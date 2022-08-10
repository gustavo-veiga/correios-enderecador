package br.com.correios.enderecador.telas

import javax.swing.JFrame
import br.com.correios.enderecador.bean.DestinatarioBean
import javax.swing.ButtonGroup
import javax.swing.JPanel
import javax.swing.JCheckBox
import javax.swing.JComboBox
import br.com.correios.enderecador.bean.RemetenteBean
import javax.swing.JLabel
import javax.swing.JRadioButton
import javax.swing.JTable
import javax.swing.JTextField
import br.com.correios.enderecador.util.EnderecadorObservable
import br.com.correios.enderecador.util.TextoCellRenderer
import javax.swing.DefaultCellEditor
import br.com.correios.enderecador.dao.RemetenteDao
import br.com.correios.enderecador.dao.DaoException
import javax.swing.JOptionPane
import javax.swing.JToolBar
import javax.swing.JButton
import javax.swing.JScrollPane
import javax.swing.ImageIcon
import java.awt.event.ActionEvent
import javax.swing.BorderFactory
import br.com.correios.enderecador.util.DocumentoPersonalizado
import javax.swing.table.DefaultTableModel
import br.com.correios.enderecador.util.Impressao
import br.com.correios.enderecador.excecao.EnderecadorExcecao
import org.apache.log4j.Logger
import org.jdesktop.layout.GroupLayout
import java.awt.*
import java.util.*

class TelaImpressaoEncomenda private constructor(private val frmParent: Frame) : JFrame(), Observer {
    private val model = DestinatarioImpressaoTableModel("E")
    private var vecDestinatarioImpressao = Vector<DestinatarioBean?>()
    private var relatorio = "Encomenda2_vizinho.jasper"
    private var buttonGroup1: ButtonGroup? = null
    private var jPanel2: JPanel? = null
    private var jchkTelDestinatario: JCheckBox? = null
    private var jchkTelRemetente: JCheckBox? = null
    private var jcmbRemetente: JComboBox<RemetenteBean>? = null
    private var jlblImpressao: JLabel? = null
    private var jrbtDoisRotulos: JRadioButton? = null
    private var jrbtQuatroRotulos: JRadioButton? = null
    private var jtblDestinatarioImpressao: JTable? = null
    private var jtxtNumeroEtiqueta: JTextField? = null

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
        coluna.preferredWidth = 100
        coluna = jtblDestinatarioImpressao!!.columnModel.getColumn(1)
        coluna.preferredWidth = 250
        coluna.cellRenderer = renderer
        coluna = jtblDestinatarioImpressao!!.columnModel.getColumn(2)
        coluna.preferredWidth = 40
        coluna.cellRenderer = renderer
        coluna = jtblDestinatarioImpressao!!.columnModel.getColumn(3)
        renderer = TextoCellRenderer(0)
        coluna.cellRenderer = renderer
        coluna.preferredWidth = 20
        val comboBox = JComboBox<String>()
        comboBox.addItem("Sim")
        comboBox.addItem("Não")
        coluna = jtblDestinatarioImpressao!!.columnModel.getColumn(4)
        coluna.cellEditor = DefaultCellEditor(comboBox)
        coluna.cellRenderer = renderer
        coluna.preferredWidth = 10
        coluna = jtblDestinatarioImpressao!!.columnModel.getColumn(5)
        coluna.cellRenderer = renderer
        coluna.preferredWidth = 20
        coluna = jtblDestinatarioImpressao!!.columnModel.getColumn(6)
        coluna.cellRenderer = renderer
        coluna.preferredWidth = 10
        jrbtQuatroRotulos!!.doClick()
        buttonGroup1!!.remove(jrbtDoisRotulos)
        val jPanel2Layout = GroupLayout(jPanel2)
        jPanel2!!.layout = jPanel2Layout
        jPanel2Layout.horizontalGroup = jPanel2Layout.createParallelGroup(1)
            .add(
                jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .add(jlblImpressao)
                    .addPreferredGap(0)
                    .add(
                        jPanel2Layout.createParallelGroup(1)
                            .add(jrbtQuatroRotulos)
                    )
                    .addContainerGap(18, 32767)
            )
        jPanel2Layout.verticalGroup = jPanel2Layout.createParallelGroup(1)
            .add(
                jPanel2Layout.createSequentialGroup()
                    .add(26, 26, 26)
                    .add(jrbtQuatroRotulos)
                    .addPreferredGap(0, -1, 32767)
                    .add(30, 30, 30)
            )
            .add(2, jlblImpressao, -2, 123, -2)
    }

    private fun carregaRemetente() {
        try {
            jcmbRemetente!!.removeAllItems()
            val arrayRemetente = RemetenteDao.instance!!.recuperaRemetente("")
            for (remetenteBean in arrayRemetente) jcmbRemetente!!.addItem(remetenteBean)
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

    private fun initComponents() {
        buttonGroup1 = ButtonGroup()
        val jToolBar1 = JToolBar()
        val jbtnSelecionarDestinatario = JButton()
        val jbtnSelecionarGrupo = JButton()
        val jbtnRemoverDestinatario = JButton()
        val jbtnRemoverTodos = JButton()
        val jbtnVisializarAR = JButton()
        val jbtnVisializar = JButton()
        val jButton2 = JButton()
        val jPanel1 = JPanel()
        jPanel2 = JPanel()
        jrbtDoisRotulos = JRadioButton()
        jrbtQuatroRotulos = JRadioButton()
        jlblImpressao = JLabel()
        val jPanel4 = JPanel()
        val jLabel4 = JLabel()
        jcmbRemetente = JComboBox()
        val jLabel2 = JLabel()
        jtxtNumeroEtiqueta = JTextField()
        jchkTelRemetente = JCheckBox()
        val jLabel1 = JLabel()
        jchkTelDestinatario = JCheckBox()
        val jScrollPane1 = JScrollPane()
        jtblDestinatarioImpressao = JTable()
        val jLabel3 = JLabel()
        isResizable = true
        title = "Etiquetas para encomendas"
        jbtnSelecionarDestinatario.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jbtnSelecionarDestinatario.icon = ImageIcon(javaClass.getResource("/imagens/addusuario.gif"))
        jbtnSelecionarDestinatario.text = "Selecionar destinatário"
        jbtnSelecionarDestinatario.horizontalTextPosition = 0
        jbtnSelecionarDestinatario.maximumSize = Dimension(110, 60)
        jbtnSelecionarDestinatario.verticalTextPosition = 3
        jbtnSelecionarDestinatario.addActionListener { evt: ActionEvent -> jbtnSelecionarDestinatarioActionPerformed(evt) }
        jToolBar1.add(jbtnSelecionarDestinatario)
        jbtnSelecionarGrupo.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jbtnSelecionarGrupo.icon = ImageIcon(javaClass.getResource("/imagens/addusuarios.gif"))
        jbtnSelecionarGrupo.text = "Selecionar grupo"
        jbtnSelecionarGrupo.horizontalTextPosition = 0
        jbtnSelecionarGrupo.maximumSize = Dimension(100, 60)
        jbtnSelecionarGrupo.verticalTextPosition = 3
        jbtnSelecionarGrupo.addActionListener { evt: ActionEvent -> jbtnSelecionarGrupoActionPerformed(evt) }
        jToolBar1.add(jbtnSelecionarGrupo)
        jbtnRemoverDestinatario.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jbtnRemoverDestinatario.icon = ImageIcon(javaClass.getResource("/imagens/remover.gif"))
        jbtnRemoverDestinatario.text = "Remover destinatário"
        jbtnRemoverDestinatario.horizontalTextPosition = 0
        jbtnRemoverDestinatario.maximumSize = Dimension(110, 60)
        jbtnRemoverDestinatario.verticalTextPosition = 3
        jbtnRemoverDestinatario.addActionListener { evt: ActionEvent -> jbtnRemoverDestinatarioActionPerformed(evt) }
        jToolBar1.add(jbtnRemoverDestinatario)
        jbtnRemoverTodos.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jbtnRemoverTodos.icon = ImageIcon(javaClass.getResource("/imagens/removerTodos.gif"))
        jbtnRemoverTodos.text = "Remover todos"
        jbtnRemoverTodos.horizontalTextPosition = 0
        jbtnRemoverTodos.maximumSize = Dimension(90, 60)
        jbtnRemoverTodos.verticalTextPosition = 3
        jbtnRemoverTodos.addActionListener { evt: ActionEvent -> jbtnRemoverTodosActionPerformed(evt) }
        jToolBar1.add(jbtnRemoverTodos)
        jbtnVisializarAR.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jbtnVisializarAR.icon = ImageIcon(javaClass.getResource("/imagens/print.gif"))
        jbtnVisializarAR.text = "Visualizar AR"
        jbtnVisializarAR.horizontalTextPosition = 0
        jbtnVisializarAR.maximumSize = Dimension(90, 60)
        jbtnVisializarAR.verticalTextPosition = 3
        jbtnVisializarAR.addActionListener { evt: ActionEvent -> jbtnVisializarARActionPerformed(evt) }
        jToolBar1.add(jbtnVisializarAR)
        jbtnVisializar.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jbtnVisializar.icon = ImageIcon(javaClass.getResource("/imagens/IMPRIMIR.gif"))
        jbtnVisializar.text = "Visualizar Etiqueta"
        jbtnVisializar.horizontalTextPosition = 0
        jbtnVisializar.maximumSize = Dimension(90, 60)
        jbtnVisializar.verticalTextPosition = 3
        jbtnVisializar.addActionListener { evt: ActionEvent -> jbtnVisializarActionPerformed(evt) }
        jToolBar1.add(jbtnVisializar)
        jButton2.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jButton2.icon = ImageIcon(javaClass.getResource("/imagens/print.gif"))
        jButton2.text = "Declaração de Conteúdo"
        jButton2.isFocusable = false
        jButton2.horizontalTextPosition = 0
        jButton2.verticalTextPosition = 3
        jButton2.addActionListener { evt: ActionEvent -> jButton2ActionPerformed(evt) }
        jToolBar1.add(jButton2)
        jPanel1.border = BorderFactory.createEtchedBorder()
        jPanel2!!.border = BorderFactory.createTitledBorder(
            null,
            "Escolha o tamanho do rótulo que deseja gerar",
            0,
            0,
            Font("Tahoma", 0, 10)
        )
        jPanel2!!.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        buttonGroup1!!.add(jrbtDoisRotulos)
        jrbtDoisRotulos!!.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jrbtDoisRotulos!!.text = "2 rótulos por vez, distribuídos numa folha"
        jrbtDoisRotulos!!.border = BorderFactory.createEmptyBorder(0, 0, 0, 0)
        jrbtDoisRotulos!!.isEnabled = false
        jrbtDoisRotulos!!.isFocusable = false
        jrbtDoisRotulos!!.margin = Insets(0, 0, 0, 0)
        jrbtDoisRotulos!!.addActionListener { evt: ActionEvent -> jrbtDoisRotulosActionPerformed(evt) }
        buttonGroup1!!.add(jrbtQuatroRotulos)
        jrbtQuatroRotulos!!.font = Font("MS Sans Serif", 0, 10)
        jrbtQuatroRotulos!!.isSelected = true
        jrbtQuatroRotulos!!.text = "4 rótulos por vez, distribuídos numa folha"
        jrbtQuatroRotulos!!.border = BorderFactory.createEmptyBorder(0, 0, 0, 0)
        jrbtQuatroRotulos!!.margin = Insets(0, 0, 0, 0)
        jrbtQuatroRotulos!!.addActionListener { evt: ActionEvent -> jrbtQuatroRotulosActionPerformed(evt) }
        jlblImpressao!!.icon = ImageIcon(javaClass.getResource("/imagens/tipo_2etq.gif"))
        jlblImpressao!!.border = BorderFactory.createEmptyBorder(1, 1, 1, 1)
        val jPanel2Layout = GroupLayout(jPanel2)
        jPanel2!!.layout = jPanel2Layout
        jPanel2Layout.horizontalGroup = jPanel2Layout.createParallelGroup(1)
            .add(
                jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .add(jlblImpressao)
                    .addPreferredGap(0)
                    .add(
                        jPanel2Layout.createParallelGroup(1)
                            .add(jrbtDoisRotulos)
                            .add(jrbtQuatroRotulos)
                    )
                    .addContainerGap(18, 32767)
            )
        jPanel2Layout.verticalGroup = jPanel2Layout.createParallelGroup(1)
            .add(
                jPanel2Layout.createSequentialGroup()
                    .add(26, 26, 26)
                    .add(jrbtDoisRotulos)
                    .addPreferredGap(0, -1, 32767)
                    .add(jrbtQuatroRotulos)
                    .add(30, 30, 30)
            )
            .add(2, jlblImpressao, -2, 123, -2)
        jPanel4.border = BorderFactory.createTitledBorder(null, " ", 0, 0, Font("Tahoma", Font.PLAIN, 10))
        jPanel4.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jLabel4.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jLabel4.text = "Selecione o remetente:"
        jLabel2.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jLabel2.text = "Quero imprimir a partir da etiqueta nº:"
        jtxtNumeroEtiqueta!!.document = DocumentoPersonalizado(3, 1)
        jchkTelRemetente!!.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jchkTelRemetente!!.text = "do remetente"
        jchkTelRemetente!!.border = BorderFactory.createEmptyBorder(0, 0, 0, 0)
        jchkTelRemetente!!.margin = Insets(0, 0, 0, 0)
        jLabel1.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jLabel1.text = "Incluir telefone na etiqueta:"
        jchkTelDestinatario!!.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jchkTelDestinatario!!.text = "do destinatário"
        jchkTelDestinatario!!.border = BorderFactory.createEmptyBorder(0, 0, 0, 0)
        jchkTelDestinatario!!.margin = Insets(0, 0, 0, 0)
        val jPanel4Layout = GroupLayout(jPanel4)
        jPanel4.layout = jPanel4Layout
        jPanel4Layout.horizontalGroup = jPanel4Layout.createParallelGroup(1)
            .add(
                jPanel4Layout.createSequentialGroup()
                    .addContainerGap()
                    .add(
                        jPanel4Layout.createParallelGroup(1)
                            .add(
                                jPanel4Layout.createSequentialGroup()
                                    .add(jLabel1)
                                    .add(17, 17, 17)
                                    .add(jchkTelRemetente)
                                    .add(18, 18, 18)
                                    .add(jchkTelDestinatario)
                            )
                            .add(
                                jPanel4Layout.createSequentialGroup()
                                    .add(jLabel4)
                                    .addPreferredGap(0)
                                    .add(jcmbRemetente, -2, -1, -2)
                            )
                            .add(
                                jPanel4Layout.createSequentialGroup()
                                    .add(jLabel2)
                                    .addPreferredGap(0)
                                    .add(jtxtNumeroEtiqueta, -2, 66, -2)
                            )
                    )
                    .addContainerGap(-1, 32767)
            )
        jPanel4Layout.verticalGroup = jPanel4Layout.createParallelGroup(1)
            .add(
                jPanel4Layout.createSequentialGroup()
                    .add(
                        jPanel4Layout.createParallelGroup(3)
                            .add(jLabel4).add(jcmbRemetente, -2, -1, -2)
                    )
                    .add(20, 20, 20)
                    .add(
                        jPanel4Layout.createParallelGroup(3)
                            .add(jLabel2)
                            .add(jtxtNumeroEtiqueta, -2, -1, -2)
                    )
                    .addPreferredGap(0, 29, 32767)
                    .add(
                        jPanel4Layout.createParallelGroup(3)
                            .add(jLabel1)
                            .add(jchkTelRemetente)
                            .add(jchkTelDestinatario)
                    )
                    .add(21, 21, 21)
            )
        val jPanel1Layout = GroupLayout(jPanel1)
        jPanel1.layout = jPanel1Layout
        jPanel1Layout.horizontalGroup = jPanel1Layout.createParallelGroup(1)
            .add(
                jPanel1Layout.createSequentialGroup()
                    .add(jPanel2, -2, -1, -2)
                    .addPreferredGap(0)
                    .add(jPanel4, -1, -1, 32767)
            )
        jPanel1Layout.verticalGroup = jPanel1Layout.createParallelGroup(1)
            .add(
                2, jPanel1Layout.createSequentialGroup()
                    .add(
                        jPanel1Layout.createParallelGroup(2)
                            .add(1, jPanel4, -1, -1, 32767)
                            .add(jPanel2, -1, -1, 32767)
                    )
                    .add(12, 12, 12)
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
        jLabel3.foreground = Color(51, 51, 255)
        jLabel3.text = "* Os campos de Quantidades, Mão Própria, Observação são editáveis com um duplo clique."
        val layout = GroupLayout(contentPane)
        contentPane.layout = layout
        layout.horizontalGroup = layout.createParallelGroup(1).add(jToolBar1, -1, 846, 32767)
            .add(jPanel1, -1, -1, 32767)
            .add(
                layout.createSequentialGroup()
                    .addContainerGap()
                    .add(
                        layout.createParallelGroup(1)
                            .add(jScrollPane1)
                            .add(
                                2, layout.createSequentialGroup()
                                    .add(0, 0, 32767)
                                    .add(jLabel3)
                            )
                    )
                    .addContainerGap()
            )
        layout.verticalGroup = layout.createParallelGroup(1)
            .add(
                layout.createSequentialGroup()
                    .add(jToolBar1, -2, 59, -2)
                    .addPreferredGap(0)
                    .add(jPanel1, -2, -1, -2)
                    .addPreferredGap(1)
                    .add(jLabel3, -2, 14, -2)
                    .addPreferredGap(0)
                    .add(jScrollPane1, -1, 259, 32767)
                    .addContainerGap()
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

    private fun jrbtQuatroRotulosActionPerformed(evt: ActionEvent) {
        jlblImpressao!!.icon = ImageIcon(javaClass.getResource("/imagens/tipo_4etq.gif"))
        relatorio = "Encomenda4_vizinho.jasper"
    }

    private fun jrbtDoisRotulosActionPerformed(evt: ActionEvent) {
        jlblImpressao!!.icon = ImageIcon(javaClass.getResource("/imagens/tipo_2etq.gif"))
        relatorio = "Encomenda2_vizinho.jasper"
    }

    private fun jbtnVisializarActionPerformed(evt: ActionEvent) {
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
        val impressao = Impressao()
        try {
            if (jtxtNumeroEtiqueta!!.text.trim { it <= ' ' } == "") jtxtNumeroEtiqueta!!.text = "1"
            if (relatorio == "Encomenda2_vizinho.jasper" && jtxtNumeroEtiqueta!!.text.toInt() > 2) {
                JOptionPane.showMessageDialog(
                    this,
                    "Posição inicial para impressão de etiquetas deve ser 1 ou 2.",
                    "Endereçador",
                    JOptionPane.WARNING_MESSAGE
                )
                return
            }
            if (relatorio == "Encomenda4_vizinho.jasper" && jtxtNumeroEtiqueta!!.text.toInt() > 4) {
                JOptionPane.showMessageDialog(
                    this,
                    "Posição inicial para impressão de etiquetas deve ser um número de 1 a 4.",
                    "Endereçador",
                    JOptionPane.WARNING_MESSAGE
                )
                return
            }
            impressao.impressaoEncomenda(
                relatorio,
                jcmbRemetente!!.selectedItem as RemetenteBean,
                vecDestinatarioImpressao,
                jtxtNumeroEtiqueta!!.text.toInt(),
                jchkTelRemetente!!.isSelected,
                jchkTelDestinatario!!.isSelected
            )
        } catch (ex: EnderecadorExcecao) {
            logger.error(ex.message, ex)
            JOptionPane.showMessageDialog(this, "Não foi possível imprimir o relatório.", "Endereçador", 2)
        }
    }

    private fun jbtnRemoverTodosActionPerformed(evt: ActionEvent) {
        vecDestinatarioImpressao.removeAllElements()
        model.destinatario = vecDestinatarioImpressao
        jtblDestinatarioImpressao!!.model = model
    }

    private fun jbtnRemoverDestinatarioActionPerformed(evt: ActionEvent) {
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

    private fun jbtnSelecionarGrupoActionPerformed(evt: ActionEvent) {
        val telaPesquisarGrupo = TelaPesquisarGrupo(frmParent, true, vecDestinatarioImpressao)
        telaPesquisarGrupo.isVisible = true
        model.destinatario = vecDestinatarioImpressao
        jtblDestinatarioImpressao!!.model = model
    }

    private fun jbtnSelecionarDestinatarioActionPerformed(evt: ActionEvent) {
        val telaPesquisaDestinatario = TelaPesquisarDestinatario(frmParent, true, vecDestinatarioImpressao)
        telaPesquisaDestinatario.isVisible = true
        model.destinatario = vecDestinatarioImpressao
        jtblDestinatarioImpressao!!.model = model
    }

    private fun jButton2ActionPerformed(evt: ActionEvent) {
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
        val telaDeclararConteudo = TelaDeclararConteudo(
            frmParent,
            true,
            vecDestinatarioImpressao,
            (jcmbRemetente!!.selectedItem as RemetenteBean)
        )
        telaDeclararConteudo.isVisible = true
    }

    override fun update(o: Observable, arg: Any) {
        if (arg is RemetenteBean) {
            val remetente = arg
            jcmbRemetente!!.removeItem(remetente)
            jcmbRemetente!!.addItem(remetente)
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
        private val logger = Logger.getLogger(TelaImpressaoEncomenda::class.java)
        private var instance: TelaImpressaoEncomenda? = null
        fun getInstance(parent: Frame): TelaImpressaoEncomenda? {
            if (instance == null) instance = TelaImpressaoEncomenda(parent)
            return instance
        }
    }
}