package br.com.correios.enderecador.view

import br.com.correios.enderecador.dao.RemetenteDao
import br.com.correios.enderecador.exception.DaoException
import br.com.correios.enderecador.tablemodel.SenderTableModel
import br.com.correios.enderecador.util.Logging
import com.formdev.flatlaf.extras.FlatSVGIcon
import net.miginfocom.swing.MigLayout
import org.koin.core.annotation.Singleton
import java.awt.Dimension
import java.awt.Font
import java.awt.Font.PLAIN
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*
import javax.swing.JLabel.CENTER
import javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
import javax.swing.table.DefaultTableCellRenderer

@Singleton
class SenderView(
    private val senderDao: RemetenteDao,
) : JFrame() {
    private val logger by Logging()

    private val senderTableModel = SenderTableModel()
    private val senderSearch = JTextField()
    private val senderTable = JTable()

    private var lastSearch = ""

    init {
        initComponents()
        recuperarDadosTabelaRemetente()
        setLocationRelativeTo(null)
    }

    private fun recuperarDadosTabelaRemetente() {
        try {
            val remetenteList = senderDao.recuperaRemetente()
            senderTableModel.setAll(remetenteList)
            senderTable.setSelectionMode(MULTIPLE_INTERVAL_SELECTION)

            val centerRenderer = DefaultTableCellRenderer().apply {
                horizontalAlignment = CENTER
            }

            senderTable.columnModel.getColumn(0).apply {
                preferredWidth = 50
            }
            senderTable.columnModel.getColumn(1).apply {
                preferredWidth = 70
            }
            senderTable.columnModel.getColumn(2).apply {
                preferredWidth = 70
                cellRenderer = centerRenderer
            }
            senderTable.columnModel.getColumn(3).apply {
                preferredWidth = 20
            }
            senderTable.columnModel.getColumn(4).apply {
                preferredWidth = 5
                cellRenderer = centerRenderer
            }
            senderTable.columnModel.getColumn(5).apply {
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
                JOptionPane.WARNING_MESSAGE)
        }
    }

    private fun initComponents() {
        title = "Cadastro de remetentes"
        isResizable = true
        preferredSize = Dimension(744, 434)

        contentPane.add(JToolBar().apply {
            add(JButton().apply {
                font = Font(Font.DIALOG, PLAIN, 9)
                icon = ImageIcon(this@SenderView.javaClass.getResource("/imagens/usuario.gif"))
                text = "Novo remetente"
                maximumSize = Dimension(90, 60)
                minimumSize = Dimension(81, 47)
                preferredSize = Dimension(60, 50)
                verticalTextPosition = 3
                horizontalTextPosition = 0
                addActionListener {  jbtNovoActionPerformed() }
            })
            add(JButton().apply {
                font = Font(Font.DIALOG, PLAIN, 9)
                icon = ImageIcon(this@SenderView.javaClass.getResource("/imagens/editar.gif"))
                text = "Editar"
                maximumSize = Dimension(90, 60)
                minimumSize = Dimension(81, 47)
                preferredSize = Dimension(60, 50)
                verticalTextPosition = 3
                horizontalTextPosition = 0
                addActionListener { jbtEditarActionPerformed() }
            })
            add(JButton().apply {
                font = Font(Font.DIALOG, PLAIN, 9)
                icon = FlatSVGIcon("images/icon/search.svg", 24, 24)
                text = "Pesquisar"
                maximumSize = Dimension(90, 60)
                minimumSize = Dimension(81, 47)
                preferredSize = Dimension(60, 50)
                verticalTextPosition = 3
                horizontalTextPosition = 0
                addActionListener { jbtPesquisarActionPerformed() }
            })
            add(JButton().apply {
                font = Font(Font.DIALOG, PLAIN, 9)
                icon = FlatSVGIcon("images/icon/trash.svg", 24, 24)
                text = "Excluir"
                maximumSize = Dimension(90, 60)
                minimumSize = Dimension(81, 47)
                preferredSize = Dimension(60, 50)
                verticalTextPosition = 3
                horizontalTextPosition = 0
                addActionListener { jbtExcluirActionPerformed() }
            })
        }, "North")

        contentPane.add(JPanel().apply {
            layout = MigLayout()

            add(JLabel("Procurar por:"))

            add(senderSearch.apply {
                addActionListener { jbtPesquisarActionPerformed() }
            }, "span, grow, pushx, width 9999")

            add(JScrollPane().apply {
                setViewportView(senderTable.apply {
                    addMouseListener(object : MouseAdapter() {
                        override fun mouseClicked(e: MouseEvent) {
                            if (e.clickCount == 2) {
                                jbtEditarActionPerformed()
                            }
                        }
                    })
                })
            }, "span, grow, push")
        }, "Center")

        senderTable.model = senderTableModel

        pack()
    }

    private fun jbtEditarActionPerformed() {
        if (senderTable.selectedRow < 0) {
            JOptionPane.showMessageDialog(
                this,
                "Não existe nenhum remetente selecionado!",
                "Endereçador ECT",
                JOptionPane.INFORMATION_MESSAGE)
            return
        }
        val senderEditView = SenderEditView(
            senderTableModel.getAt(senderTable.selectedRow),
            parent = this)
        senderEditView.isVisible = true
    }

    private fun jbtNovoActionPerformed() {
        val senderEditView = SenderEditView(parent = this)
        senderEditView.isVisible = true
    }

    private fun jbtExcluirActionPerformed() {
        if (senderTable.selectedRow == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Não existe nenhum remetente selecionado!",
                "Enderecador",
                JOptionPane.WARNING_MESSAGE)
            return
        }
        val response = JOptionPane.showOptionDialog(
            this,
            "Tem certeza que deseja excluir o(s) remetente(s)?",
            "Endereçador",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            arrayOf("Sim", "Não"),
            null)
        if (response == JOptionPane.YES_OPTION) {
            try {
                senderTable.selectedRows.forEach { row ->
                    val sender = senderTableModel.getAt(row)
                    senderDao.excluirRemetente(sender.numeroRemetente)
                    senderTableModel.removeRowAt(row)
                }
                senderSearch.text = ""
                JOptionPane.showMessageDialog(
                    this,
                    "Remetente(s) excluído(s) com sucesso!",
                    "Endereçador",
                    JOptionPane.INFORMATION_MESSAGE)
            } catch (re: DaoException) {
                logger.error(re.message, re)
                JOptionPane.showMessageDialog(
                    this,
                    "Não foi possivel carregar relação de remetentes",
                    "Endereçador",
                    JOptionPane.WARNING_MESSAGE)
            }
        }
    }

    private fun jbtPesquisarActionPerformed() {
        val search = senderSearch.text.trim()

        senderTableModel
            .filterIndexed { index, _ ->
                if (lastSearch.equals(search, ignoreCase = true)) {
                    index > senderTable.selectedRow
                } else {
                    lastSearch = search
                    true
                }
            }
            .find { it.nome.contains(search, ignoreCase = true).or(it.apelido.contains(search, ignoreCase = true)) }
            ?.let {
                val index = senderTableModel.indexOf(it)
                senderTable.selectionModel.setSelectionInterval(index, index)
                return
            }

        JOptionPane.showMessageDialog(
            this,
            "Remetente não encontrado!",
            "Endereçador",
            JOptionPane.INFORMATION_MESSAGE)
    }
}
