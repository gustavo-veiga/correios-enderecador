package br.com.correios.enderecador.telas

import javax.swing.JFrame
import br.com.correios.enderecador.util.EnderecadorObservable
import javax.swing.JScrollPane
import javax.swing.JTextField
import javax.swing.JTable
import br.com.correios.enderecador.bean.RemetenteBean
import br.com.correios.enderecador.dao.RemetenteDao
import br.com.correios.enderecador.util.TextoCellRenderer
import javax.swing.table.TableColumn
import br.com.correios.enderecador.dao.DaoException
import javax.swing.JOptionPane
import javax.swing.JToolBar
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JLabel
import javax.swing.ImageIcon
import java.awt.event.ActionEvent
import com.formdev.flatlaf.extras.FlatSVGIcon
import java.awt.BorderLayout
import javax.swing.BorderFactory
import org.netbeans.lib.awtextra.AbsoluteLayout
import org.netbeans.lib.awtextra.AbsoluteConstraints
import javax.swing.table.DefaultTableModel
import org.apache.log4j.Logger
import java.awt.Dimension
import java.awt.Font
import java.awt.Frame
import java.util.*

class TelaRemetente private constructor(private val frmParent: Frame) : JFrame(), Observer {
    private val remetenteTableModel = RemetenteTableModel()
    private val observable = EnderecadorObservable.instance
    private var ultimaConsulta = ""
    private var jScrollPane1: JScrollPane? = null
    private var jtxtNomeRemetente: JTextField? = null
    private var tabRemetente: JTable? = null

    init {
        initComponents()
        observable?.addObserver(this)
    }

    private fun recuperarDadosTabelaRemetente() {
        try {
            val arrayRemetente = RemetenteDao.instance!!.recuperaRemetente("")
            remetenteTableModel.setRemetente(arrayRemetente)
            tabRemetente!!.setSelectionMode(2)
            val renderer = TextoCellRenderer(2)
            var coluna: TableColumn
            coluna = tabRemetente!!.columnModel.getColumn(0)
            coluna.cellRenderer = renderer
            coluna = tabRemetente!!.columnModel.getColumn(1)
            coluna.preferredWidth = 70
            coluna.cellRenderer = renderer
            coluna = tabRemetente!!.columnModel.getColumn(2)
            coluna.cellRenderer = renderer
            coluna.preferredWidth = 70
            coluna = tabRemetente!!.columnModel.getColumn(3)
            coluna.cellRenderer = renderer
            coluna = tabRemetente!!.columnModel.getColumn(4)
            coluna.cellRenderer = renderer
            coluna.preferredWidth = 5
            coluna = tabRemetente!!.columnModel.getColumn(5)
            coluna.cellRenderer = renderer
            coluna.preferredWidth = 1
            coluna.width = 1
        } catch (e: DaoException) {
            logger.error(e.message, e)
            JOptionPane.showMessageDialog(
                this,
                "Não foi possivel carregar relação de remetentes",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE
            )
        }
    }

    private fun initComponents() {
        val jToolBar1 = JToolBar()
        val jbtNovo = JButton()
        val jbtEditar = JButton()
        val jbtPesquisar = JButton()
        val jbtExcluir = JButton()
        val jPanel1 = JPanel()
        val jPanel2 = JPanel()
        val jLabel1 = JLabel()
        jtxtNomeRemetente = JTextField()
        val jLabel2 = JLabel()
        jScrollPane1 = JScrollPane()
        tabRemetente = JTable()
        isResizable = true
        title = "Cadastro de remetentes"
        preferredSize = Dimension(744, 434)
        jbtNovo.font = Font(Font.DIALOG, Font.PLAIN, 9)
        jbtNovo.icon = ImageIcon(javaClass.getResource("/imagens/usuario.gif"))
        jbtNovo.text = "Novo remetente"
        jbtNovo.horizontalTextPosition = 0
        jbtNovo.maximumSize = Dimension(90, 60)
        jbtNovo.minimumSize = Dimension(81, 47)
        jbtNovo.preferredSize = Dimension(60, 50)
        jbtNovo.verticalTextPosition = 3
        jbtNovo.addActionListener { evt: ActionEvent -> jbtNovoActionPerformed(evt) }
        jToolBar1.add(jbtNovo)
        jbtEditar.font = Font(Font.DIALOG, Font.PLAIN, 9)
        jbtEditar.icon = ImageIcon(javaClass.getResource("/imagens/editar.gif"))
        jbtEditar.text = "Editar"
        jbtEditar.horizontalTextPosition = 0
        jbtEditar.maximumSize = Dimension(90, 60)
        jbtEditar.verticalTextPosition = 3
        jbtEditar.addActionListener { evt: ActionEvent -> jbtEditarActionPerformed(evt) }
        jToolBar1.add(jbtEditar)
        jbtPesquisar.font = Font(Font.DIALOG, Font.PLAIN, 9)
        jbtPesquisar.icon = FlatSVGIcon("images/icon/search.svg", 24, 24)
        jbtPesquisar.text = "Pesquisar"
        jbtPesquisar.horizontalTextPosition = 0
        jbtPesquisar.maximumSize = Dimension(90, 60)
        jbtPesquisar.verticalTextPosition = 3
        jbtPesquisar.addActionListener { evt: ActionEvent -> jbtPesquisarActionPerformed(evt) }
        jToolBar1.add(jbtPesquisar)
        jbtExcluir.font = Font(Font.DIALOG, Font.PLAIN, 9)
        jbtExcluir.icon = FlatSVGIcon("images/icon/trash.svg", 24, 24)
        jbtExcluir.text = "Excluir"
        jbtExcluir.horizontalTextPosition = 0
        jbtExcluir.maximumSize = Dimension(90, 60)
        jbtExcluir.verticalTextPosition = 3
        jbtExcluir.addActionListener { evt: ActionEvent -> jbtExcluirActionPerformed(evt) }
        jToolBar1.add(jbtExcluir)
        contentPane.add(jToolBar1, "North")
        jPanel1.layout = BorderLayout()
        jPanel1.border = BorderFactory.createEtchedBorder()
        jPanel2.layout = AbsoluteLayout()
        jLabel1.font = Font("MS Sans Serif", Font.PLAIN, 10)
        jLabel1.text = "Procurar por:"
        jPanel2.add(jLabel1, AbsoluteConstraints(10, 11, 70, 20))
        jPanel2.add(jtxtNomeRemetente, AbsoluteConstraints(80, 10, 310, -1))
        jLabel2.font = Font("MS Sans Serif", Font.PLAIN, 10)
        jLabel2.text = "Destinatários:"
        jPanel2.add(jLabel2, AbsoluteConstraints(10, 30, 70, 30))
        jPanel1.add(jPanel2, "North")
        tabRemetente!!.model = DefaultTableModel(
            arrayOf(
                arrayOf(null, null, null, null),
                arrayOf(null, null, null, null),
                arrayOf(null, null, null, null),
                arrayOf(null, null, null, null)
            ), arrayOf("Title 1", "Title 2", "Title 3", "Title 4")
        )
        tabRemetente!!.model = remetenteTableModel
        jScrollPane1!!.setViewportView(tabRemetente)
        jPanel1.add(jScrollPane1, "Center")
        contentPane.add(jPanel1, "Center")
        pack()
    }

    private fun jbtEditarActionPerformed(evt: ActionEvent) {
        if (tabRemetente!!.selectedRow < 0) {
            JOptionPane.showMessageDialog(
                this,
                "Não existe nenhum remetente selecionado!",
                "Endereçador ECT",
                JOptionPane.INFORMATION_MESSAGE
            )
            return
        }
        val telaEditarRemetente = remetenteTableModel.getRemetente(
            tabRemetente!!.selectedRow
        )?.let {
            TelaEditarRemetente(
                frmParent, true, it
            )
        }
        if (telaEditarRemetente != null) {
            telaEditarRemetente.isVisible = true
        }
    }

    private fun jbtNovoActionPerformed(evt: ActionEvent) {
        val telaEditarRemetente = TelaEditarRemetente(frmParent, true)
        telaEditarRemetente.isVisible = true
    }

    private fun jbtExcluirActionPerformed(evt: ActionEvent) {
        var remetenteBean: RemetenteBean
        val listaRemetentes: MutableList<RemetenteBean> = ArrayList()
        if (tabRemetente!!.selectedRow == -1) {
            JOptionPane.showMessageDialog(
                frmParent,
                "Não existe nenhum remetente selecionado!",
                "Enderecador",
                JOptionPane.WARNING_MESSAGE
            )
            return
        }
        val options = arrayOf("Sim", "Não")
        val resp = JOptionPane.showOptionDialog(
            frmParent,
            "Tem certeza que deseja excluir o(s) remetente(s)?",
            "Endereçador",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            null
        )
        if (resp == 0) {
            val linhasSelecionadas = tabRemetente!!.selectedRows
            try {
                for (linhasSelecionada in linhasSelecionadas) {
                    remetenteBean = remetenteTableModel.getRemetente(linhasSelecionada)!!
                    RemetenteDao.instance!!.excluirRemetente(remetenteBean.numeroRemetente)
                    listaRemetentes.add(remetenteBean)
                }
                observable?.notifyObservers(listaRemetentes)
                jtxtNomeRemetente!!.text = ""
                JOptionPane.showMessageDialog(
                    frmParent,
                    "Remetente(s) excluído(s) com sucesso!",
                    "Endereçador",
                    JOptionPane.INFORMATION_MESSAGE
                )
            } catch (re: DaoException) {
                logger.error(re.message, re)
                JOptionPane.showMessageDialog(
                    frmParent,
                    "Não foi possivel carregar relação de remetentes",
                    "Endereçador",
                    JOptionPane.WARNING_MESSAGE
                )
            }
        }
    }

    private fun jbtPesquisarActionPerformed(evt: ActionEvent) {
        val nomeRemetente = jtxtNomeRemetente!!.text.trim { it <= ' ' }
        val numeroRows = tabRemetente!!.rowCount
        var index = -1
        if (ultimaConsulta.equals(nomeRemetente, ignoreCase = true)) {
            index = tabRemetente!!.selectedRow
        } else {
            ultimaConsulta = nomeRemetente
        }
        for (i in index + 1 until numeroRows) {
            val nomeRemetenteTabela = tabRemetente!!.getValueAt(i, 0) as String
            if (nomeRemetenteTabela.uppercase(Locale.getDefault())
                    .contains(nomeRemetente.uppercase(Locale.getDefault()))
            ) {
                index = i
                break
            }
        }
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Remetente não encontrado!", "Endereçador", 1)
        } else {
            val lsSelectionModelRemetente = tabRemetente!!.selectionModel
            lsSelectionModelRemetente.setSelectionInterval(index, index)
            val tamanhoJscrool = jScrollPane1!!.verticalScrollBar.maximum
            var value = 0
            value = index * tamanhoJscrool / numeroRows
            jScrollPane1!!.verticalScrollBar.value = value
        }
    }

    override fun update(o: Observable, arg: Any) {
        if (arg is RemetenteBean) {
            val remetente = arg
            val index = remetenteTableModel.indexOf(remetente)
            if (index != -1) {
                remetenteTableModel.setRemetente(index, remetente)
            } else {
                remetenteTableModel.addRemetente(remetente)
            }
        } else if (arg is List<*>) {
            arg
                .filterNotNull()
                .forEach { remetenteTableModel.removeRemetente(it as RemetenteBean) }
        }
    }

    companion object {
        private val logger = Logger.getLogger(TelaRemetente::class.java)
        private var instance: TelaRemetente? = null
        fun getInstance(parent: Frame): TelaRemetente? {
            if (instance == null) {
                instance = TelaRemetente(parent)
                instance!!.recuperarDadosTabelaRemetente()
            }
            return instance
        }
    }
}
