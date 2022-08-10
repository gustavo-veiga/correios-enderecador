package br.com.correios.enderecador.telas

import br.com.correios.enderecador.util.EnderecadorObservable
import br.com.correios.enderecador.bean.DestinatarioBean
import br.com.correios.enderecador.dao.DestinatarioDao
import br.com.correios.enderecador.util.TextoCellRenderer
import javax.swing.table.TableColumn
import br.com.correios.enderecador.dao.DaoException
import java.awt.BorderLayout
import org.netbeans.lib.awtextra.AbsoluteLayout
import org.netbeans.lib.awtextra.AbsoluteConstraints
import javax.swing.table.DefaultTableModel
import java.awt.event.ActionEvent
import br.com.correios.enderecador.dao.GrupoDestinatarioDao
import org.apache.log4j.Logger
import java.awt.Dimension
import java.awt.Font
import java.awt.Frame
import java.util.*
import javax.swing.*

class TelaDestinatario private constructor(parent: Frame) : JFrame(), Observer {
    private val destinatarioTableModel: DestinatarioTableModel = DestinatarioTableModel()
    private val observable: EnderecadorObservable? = EnderecadorObservable.instance
    private val frmParent: Frame
    private var ultimaConsulta: String
    private var jScrollPane1: JScrollPane? = null
    private var jtxtNomeDestinatario: JTextField? = null
    private var tabDestinatario: JTable? = null

    init {
        ultimaConsulta = ""
        frmParent = parent
        initComponents()
        recuperarDadosTabelaDestinatario()
        observable?.addObserver(this)
    }

    private fun recuperarDadosTabelaDestinatario() {
        try {
            val arrayDestinatario = DestinatarioDao.instance!!.recuperaDestinatario("")
            destinatarioTableModel.setDestinatario(arrayDestinatario)
            tabDestinatario!!.setSelectionMode(2)
            val renderer = TextoCellRenderer(2)
            var coluna: TableColumn? = null
            coluna = tabDestinatario!!.columnModel.getColumn(0)
            coluna.cellRenderer = renderer
            coluna = tabDestinatario!!.columnModel.getColumn(1)
            coluna.preferredWidth = 70
            coluna.cellRenderer = renderer
            coluna = tabDestinatario!!.columnModel.getColumn(2)
            coluna.cellRenderer = renderer
            coluna.preferredWidth = 70
            coluna = tabDestinatario!!.columnModel.getColumn(3)
            coluna.cellRenderer = renderer
            coluna = tabDestinatario!!.columnModel.getColumn(4)
            coluna.cellRenderer = renderer
            coluna.preferredWidth = 5
            coluna = tabDestinatario!!.columnModel.getColumn(5)
            coluna.cellRenderer = renderer
            coluna.preferredWidth = 1
            coluna.width = 1
        } catch (e: DaoException) {
            logger.error(e.message, e)
            JOptionPane.showMessageDialog(
                this,
                e.message,
                "Não foi possivel carregar relação de destinatários",
                JOptionPane.WARNING_MESSAGE
            )
        }
    }

    private fun initComponents() {
        val jPanel1 = JPanel()
        val jPanel2 = JPanel()
        val jLabel1 = JLabel()
        jtxtNomeDestinatario = JTextField()
        val jLabel2 = JLabel()
        jScrollPane1 = JScrollPane()
        tabDestinatario = JTable()
        val jToolBar1 = JToolBar()
        val jbtNovo = JButton()
        val jbtEditar = JButton()
        val jbtPesquisar = JButton()
        val jbtExcluir = JButton()
        isResizable = true
        title = "Cadastro de destinatário"
        preferredSize = Dimension(744, 434)
        jPanel1.layout = BorderLayout()
        jPanel1.border = BorderFactory.createEtchedBorder()
        jPanel2.layout = AbsoluteLayout()
        jLabel1.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jLabel1.text = "Procurar por:"
        jPanel2.add(jLabel1, AbsoluteConstraints(10, 10, -1, 20))
        jPanel2.add(jtxtNomeDestinatario, AbsoluteConstraints(90, 10, 300, -1))
        jLabel2.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jLabel2.text = "Destinatários:"
        jPanel2.add(jLabel2, AbsoluteConstraints(10, 30, -1, 30))
        jPanel1.add(jPanel2, "North")
        tabDestinatario!!.model = DefaultTableModel(
            arrayOf(
                arrayOf(null, null, null, null),
                arrayOf(null, null, null, null),
                arrayOf(null, null, null, null),
                arrayOf(null, null, null, null)
            ), arrayOf("Title 1", "Title 2", "Title 3", "Title 4")
        )
        tabDestinatario!!.model = destinatarioTableModel
        jScrollPane1!!.setViewportView(tabDestinatario)
        jPanel1.add(jScrollPane1, "Center")
        contentPane.add(jPanel1, "Center")
        jbtNovo.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jbtNovo.icon = ImageIcon(javaClass.getResource("/imagens/usuario.gif"))
        jbtNovo.text = "Novo destinatário"
        jbtNovo.horizontalTextPosition = 0
        jbtNovo.maximumSize = Dimension(90, 60)
        jbtNovo.minimumSize = Dimension(87, 47)
        jbtNovo.preferredSize = Dimension(53, 51)
        jbtNovo.verticalTextPosition = 3
        jbtNovo.addActionListener { evt: ActionEvent -> jbtNovoActionPerformed(evt) }
        jToolBar1.add(jbtNovo)
        jbtEditar.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jbtEditar.icon = ImageIcon(javaClass.getResource("/imagens/editar.gif"))
        jbtEditar.text = "Editar"
        jbtEditar.horizontalTextPosition = 0
        jbtEditar.maximumSize = Dimension(90, 60)
        jbtEditar.verticalTextPosition = 3
        jbtEditar.addActionListener { evt: ActionEvent -> jbtEditarActionPerformed(evt) }
        jToolBar1.add(jbtEditar)
        jbtPesquisar.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jbtPesquisar.icon = ImageIcon(javaClass.getResource("/imagens/binoculo.gif"))
        jbtPesquisar.text = "Pesquisar"
        jbtPesquisar.horizontalTextPosition = 0
        jbtPesquisar.maximumSize = Dimension(90, 60)
        jbtPesquisar.minimumSize = Dimension(47, 55)
        jbtPesquisar.verticalTextPosition = 3
        jbtPesquisar.addActionListener { evt: ActionEvent -> jbtPesquisarActionPerformed(evt) }
        jToolBar1.add(jbtPesquisar)
        jbtExcluir.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jbtExcluir.icon = ImageIcon(javaClass.getResource("/imagens/TRASH.gif"))
        jbtExcluir.text = "Excluir"
        jbtExcluir.horizontalTextPosition = 0
        jbtExcluir.maximumSize = Dimension(90, 60)
        jbtExcluir.minimumSize = Dimension(47, 55)
        jbtExcluir.verticalTextPosition = 3
        jbtExcluir.addActionListener { evt: ActionEvent -> jbtExcluirActionPerformed(evt) }
        jToolBar1.add(jbtExcluir)
        contentPane.add(jToolBar1, "North")
        pack()
    }

    private fun jbtNovoActionPerformed(evt: ActionEvent) {
        val telaEditarDestinatrio = TelaEditarDestinatario(frmParent, true)
        telaEditarDestinatrio.isVisible = true
    }

    private fun jbtEditarActionPerformed(evt: ActionEvent) {
        if (tabDestinatario!!.selectedRow < 0) {
            JOptionPane.showMessageDialog(
                this,
                "Não existe nenhum remetente selecionado!",
                "Endereçador ECT",
                JOptionPane.INFORMATION_MESSAGE
            )
            return
        }
        val telaEditarDestinatrio = destinatarioTableModel.getDestinatario(tabDestinatario!!.selectedRow)?.let {
            TelaEditarDestinatario(
                frmParent, true, it
            )
        }
        telaEditarDestinatrio!!.isVisible = true
    }

    private fun jbtPesquisarActionPerformed(evt: ActionEvent) {
        val lsSelectionModelDestinatario = tabDestinatario!!.selectionModel
        val nomeDestinatario = jtxtNomeDestinatario!!.text.trim { it <= ' ' }
        val numeroRows = tabDestinatario!!.rowCount
        var index = -1
        if (ultimaConsulta.equals(nomeDestinatario, ignoreCase = true)) {
            index = tabDestinatario!!.selectedRow
        } else {
            ultimaConsulta = nomeDestinatario
        }
        for (i in index + 1 until numeroRows) {
            val nomeDestinatarioTabela = tabDestinatario!!.getValueAt(i, 0) as String
            if (nomeDestinatarioTabela.uppercase(Locale.getDefault())
                    .contains(nomeDestinatario.uppercase(Locale.getDefault()))
            ) {
                index = i
                break
            }
        }
        if (index == -1) {
            JOptionPane.showMessageDialog(
                frmParent,
                "Destinatário não encontrado!",
                "Endereçador",
                JOptionPane.INFORMATION_MESSAGE
            )
        } else {
            lsSelectionModelDestinatario.setSelectionInterval(index, index)
            val tamanhoJscrool = jScrollPane1!!.verticalScrollBar.maximum
            var value = 0
            value = index * tamanhoJscrool / numeroRows
            jScrollPane1!!.verticalScrollBar.value = value
        }
    }

    private fun jbtExcluirActionPerformed(evt: ActionEvent) {
        var destinatarioBean: DestinatarioBean? = null
        val listaDestinatarios: MutableList<DestinatarioBean?> = ArrayList()
        if (tabDestinatario!!.selectedRow == -1) {
            JOptionPane.showMessageDialog(
                frmParent,
                "Não existe nenhum destinatário selecionado!",
                "Enderecador",
                JOptionPane.WARNING_MESSAGE
            )
        } else {
            val options = arrayOf("Sim", "Não")
            val resp = JOptionPane.showOptionDialog(
                frmParent,
                "Tem certeza que deseja excluir o(s) destinatário(s)?",
                "Endereçador",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                null
            )
            if (resp == 0) {
                val linhasSelecionadas = tabDestinatario!!.selectedRows
                try {
                    for (linhasSelecionada in linhasSelecionadas) {
                        destinatarioBean = destinatarioTableModel.getDestinatario(linhasSelecionada)
                        GrupoDestinatarioDao.instance!!.excluirDestinatarioDoGrupo(destinatarioBean!!.numeroDestinatario)
                        DestinatarioDao.instance!!.excluirDestinatario(destinatarioBean.numeroDestinatario)
                        listaDestinatarios.add(destinatarioBean)
                    }
                    observable?.notifyObservers(listaDestinatarios)
                    jtxtNomeDestinatario!!.text = ""
                    JOptionPane.showMessageDialog(
                        frmParent,
                        "Destinatário(s) excluído(s) com sucesso!",
                        "Endereçador",
                        JOptionPane.INFORMATION_MESSAGE
                    )
                } catch (ex: DaoException) {
                    logger.error(ex.message, ex as Throwable)
                    JOptionPane.showMessageDialog(
                        frmParent,
                        "Não foi possivel carregar relação de destinatários!",
                        "Endereçador ECT",
                        JOptionPane.WARNING_MESSAGE
                    )
                }
            }
        }
    }

    override fun update(o: Observable, arg: Any) {
        if (arg is DestinatarioBean) {
            val destinatario = arg
            val index = destinatarioTableModel.indexOf(destinatario)
            if (index != -1) {
                destinatarioTableModel.setDestinatario(index, destinatario)
            } else {
                destinatarioTableModel.addDestinatario(destinatario)
            }
        } else if (arg is List<*>) {
            val listaDestinatarios = arg as List<DestinatarioBean>
            for (listaDestinatario in listaDestinatarios) {
                if (listaDestinatario != null) destinatarioTableModel.removeDestinatario(listaDestinatario)
            }
        }
    }

    companion object {
        private val logger = Logger.getLogger(TelaDestinatario::class.java)
        private var instance: TelaDestinatario? = null
        fun getInstance(parent: Frame): TelaDestinatario? {
            if (instance == null) instance = TelaDestinatario(parent)
            return instance
        }
    }
}
