package br.com.correios.enderecador.telas

import javax.swing.JFrame
import br.com.correios.enderecador.util.EnderecadorObservable
import javax.swing.JScrollPane
import javax.swing.JTextField
import javax.swing.JTable
import br.com.correios.enderecador.bean.RemetenteBean
import br.com.correios.enderecador.dao.RemetenteDao
import br.com.correios.enderecador.dao.DaoException
import javax.swing.JOptionPane
import javax.swing.JToolBar
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JLabel
import javax.swing.ImageIcon
import com.formdev.flatlaf.extras.FlatSVGIcon
import java.awt.BorderLayout
import javax.swing.BorderFactory
import org.netbeans.lib.awtextra.AbsoluteLayout
import org.netbeans.lib.awtextra.AbsoluteConstraints
import javax.swing.table.DefaultTableModel
import org.apache.log4j.Logger
import org.koin.core.annotation.Singleton
import java.awt.Dimension
import java.awt.Font
import java.awt.Font.PLAIN
import java.util.*
import javax.swing.JLabel.CENTER
import javax.swing.table.DefaultTableCellRenderer

@Singleton
class TelaRemetente(
    private val remetenteDao: RemetenteDao
) : JFrame(), Observer {
    private val remetenteTableModel = RemetenteTableModel()
    private val observable = EnderecadorObservable.instance
    private var ultimaConsulta = ""
    private val jScrollPane = JScrollPane()
    private val jtxtNomeRemetente = JTextField()
    private val tabRemetente = JTable()

    init {
        initComponents()
        recuperarDadosTabelaRemetente()
        observable?.addObserver(this)
        setLocationRelativeTo(null)
    }

    private fun recuperarDadosTabelaRemetente() {
        try {
            val remetenteList = remetenteDao.recuperaRemetente("")
            remetenteTableModel.setRemetente(remetenteList)
            tabRemetente.setSelectionMode(2)

            val centerRenderer = DefaultTableCellRenderer().apply {
                horizontalAlignment = CENTER
            }

            tabRemetente.columnModel.getColumn(0).apply {
                preferredWidth = 50
            }
            tabRemetente.columnModel.getColumn(1).apply {
                preferredWidth = 70
            }
            tabRemetente.columnModel.getColumn(2).apply {
                preferredWidth = 70
                cellRenderer = centerRenderer
            }
            tabRemetente.columnModel.getColumn(3).apply {
                preferredWidth = 20
            }
            tabRemetente.columnModel.getColumn(4).apply {
                preferredWidth = 5
                cellRenderer = centerRenderer
            }
            tabRemetente.columnModel.getColumn(5).apply {
                width = 1
                preferredWidth = 1
                cellRenderer = centerRenderer
            }
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
        title = "Cadastro de remetentes"
        isResizable = true
        preferredSize = Dimension(744, 434)

        contentPane.add(JToolBar().apply {
            add(JButton().apply {
                font = Font(Font.DIALOG, PLAIN, 9)
                icon = ImageIcon(this@TelaRemetente.javaClass.getResource("/imagens/usuario.gif"))
                text = "Novo remetente"
                horizontalTextPosition = 0
                maximumSize = Dimension(90, 60)
                minimumSize = Dimension(81, 47)
                preferredSize = Dimension(60, 50)
                verticalTextPosition = 3
                addActionListener {  jbtNovoActionPerformed() }
            })
            add(JButton().apply {
                font = Font(Font.DIALOG, PLAIN, 9)
                icon = ImageIcon(this@TelaRemetente.javaClass.getResource("/imagens/editar.gif"))
                text = "Editar"
                horizontalTextPosition = 0
                maximumSize = Dimension(90, 60)
                verticalTextPosition = 3
                addActionListener { jbtEditarActionPerformed() }
            })
            add(JButton().apply {
                font = Font(Font.DIALOG, PLAIN, 9)
                icon = FlatSVGIcon("images/icon/search.svg", 24, 24)
                text = "Pesquisar"
                horizontalTextPosition = 0
                maximumSize = Dimension(90, 60)
                verticalTextPosition = 3
                addActionListener { jbtPesquisarActionPerformed() }
            })
            add(JButton().apply {
                font = Font(Font.DIALOG, PLAIN, 9)
                icon = FlatSVGIcon("images/icon/trash.svg", 24, 24)
                text = "Excluir"
                horizontalTextPosition = 0
                maximumSize = Dimension(90, 60)
                verticalTextPosition = 3
                addActionListener { jbtExcluirActionPerformed() }
            })
        }, "North")

        contentPane.add(JPanel().apply {
            layout = BorderLayout()
            border = BorderFactory.createEtchedBorder()
            add(JPanel().apply {
                layout = AbsoluteLayout()
                add(JLabel("Procurar por:"), AbsoluteConstraints(10, 11, 70, 20))
                add(jtxtNomeRemetente, AbsoluteConstraints(80, 10, 310, -1))
                add(JLabel("Destinatários:"), AbsoluteConstraints(10, 30, 70, 30))
            }, "North")
            add(jScrollPane, "Center")
        }, "Center")

        tabRemetente.model = DefaultTableModel(
            arrayOf(
                arrayOf(null, null, null, null),
                arrayOf(null, null, null, null),
                arrayOf(null, null, null, null),
                arrayOf(null, null, null, null)
            ), arrayOf("Title 1", "Title 2", "Title 3", "Title 4")
        )

        tabRemetente.model = remetenteTableModel
        jScrollPane.setViewportView(tabRemetente)

        pack()
    }

    private fun jbtEditarActionPerformed() {
        if (tabRemetente.selectedRow < 0) {
            JOptionPane.showMessageDialog(
                this,
                "Não existe nenhum remetente selecionado!",
                "Endereçador ECT",
                JOptionPane.INFORMATION_MESSAGE
            )
            return
        }
        val telaEditarRemetente = remetenteTableModel.getRemetente(
            tabRemetente.selectedRow
        )?.let {
            TelaEditarRemetente(
                this, true, it
            )
        }
        if (telaEditarRemetente != null) {
            telaEditarRemetente.isVisible = true
        }
    }

    private fun jbtNovoActionPerformed() {
        val telaEditarRemetente = TelaEditarRemetente(this, true)
        telaEditarRemetente.isVisible = true
    }

    private fun jbtExcluirActionPerformed() {
        var remetenteBean: RemetenteBean
        val listaRemetentes: MutableList<RemetenteBean> = ArrayList()
        if (tabRemetente.selectedRow == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Não existe nenhum remetente selecionado!",
                "Enderecador",
                JOptionPane.WARNING_MESSAGE
            )
            return
        }
        val options = arrayOf("Sim", "Não")
        val resp = JOptionPane.showOptionDialog(
            this,
            "Tem certeza que deseja excluir o(s) remetente(s)?",
            "Endereçador",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            null
        )
        if (resp == 0) {
            val linhasSelecionadas = tabRemetente.selectedRows
            try {
                for (linhasSelecionada in linhasSelecionadas) {
                    remetenteBean = remetenteTableModel.getRemetente(linhasSelecionada)!!
                    remetenteDao.excluirRemetente(remetenteBean.numeroRemetente)
                    listaRemetentes.add(remetenteBean)
                }
                observable?.notifyObservers(listaRemetentes)
                jtxtNomeRemetente.text = ""
                JOptionPane.showMessageDialog(
                    this,
                    "Remetente(s) excluído(s) com sucesso!",
                    "Endereçador",
                    JOptionPane.INFORMATION_MESSAGE
                )
            } catch (re: DaoException) {
                logger.error(re.message, re)
                JOptionPane.showMessageDialog(
                    this,
                    "Não foi possivel carregar relação de remetentes",
                    "Endereçador",
                    JOptionPane.WARNING_MESSAGE
                )
            }
        }
    }

    private fun jbtPesquisarActionPerformed() {
        val nomeRemetente = jtxtNomeRemetente.text.trim()
        val numeroRows = tabRemetente.rowCount
        var index = -1
        if (ultimaConsulta.equals(nomeRemetente, ignoreCase = true)) {
            index = tabRemetente.selectedRow
        } else {
            ultimaConsulta = nomeRemetente
        }
        for (i in index + 1 until numeroRows) {
            val nomeRemetenteTabela = tabRemetente.getValueAt(i, 0) as String
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
            val lsSelectionModelRemetente = tabRemetente.selectionModel
            lsSelectionModelRemetente.setSelectionInterval(index, index)
            val tamanhoJscrool = jScrollPane.verticalScrollBar.maximum
            val value = index * tamanhoJscrool / numeroRows
            jScrollPane.verticalScrollBar.value = value
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
    }
}
