package br.com.correios.enderecador.view

import br.com.correios.enderecador.dao.DestinatarioDao
import br.com.correios.enderecador.exception.DaoException
import br.com.correios.enderecador.dao.GrupoDestinatarioDao
import br.com.correios.enderecador.tablemodel.RecipientTableModel
import br.com.correios.enderecador.util.Logging
import net.miginfocom.swing.MigLayout
import org.koin.core.annotation.Singleton
import java.awt.Dimension
import java.awt.Font
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*
import javax.swing.JLabel.CENTER
import javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
import javax.swing.table.DefaultTableCellRenderer

@Singleton
class RecipientView(
    private val recipientDao: DestinatarioDao,
    private val recipientGroupDao: GrupoDestinatarioDao,
) : JFrame() {
    private val logger by Logging()

    private val recipientTableModel = RecipientTableModel()
    private val recipientSearch = JTextField()
    private val recipientTable = JTable()

    private var lastSearch = ""

    init {
        initComponents()
        recuperarDadosTabelaDestinatario()
        setLocationRelativeTo(null)
    }

    private fun recuperarDadosTabelaDestinatario() {
        try {
            val recipients = recipientDao.recuperaDestinatario()
            recipientTableModel.setAll(recipients)
            recipientTable.setSelectionMode(MULTIPLE_INTERVAL_SELECTION)

            val centerRenderer = DefaultTableCellRenderer().apply {
                horizontalAlignment = CENTER
            }

            recipientTable.columnModel.getColumn(0).apply {
                 preferredWidth = 50
            }
            recipientTable.columnModel.getColumn(1).apply {
                preferredWidth = 70
                cellRenderer = centerRenderer
            }
            recipientTable.columnModel.getColumn(2).apply {
                preferredWidth = 70
            }
            recipientTable.columnModel.getColumn(3).apply {
                preferredWidth = 20
            }
            recipientTable.columnModel.getColumn(4).apply {
                preferredWidth = 5
                cellRenderer = centerRenderer
            }
            recipientTable.columnModel.getColumn(5).apply {
                width = 1
                preferredWidth = 1
                cellRenderer = centerRenderer
            }

        } catch (e: DaoException) {
            logger.error(e.message, e)
            JOptionPane.showMessageDialog(this, e.message,
                "Não foi possivel carregar relação de destinatários",
                JOptionPane.WARNING_MESSAGE)
        }
    }

    private fun initComponents() {
        title = "Cadastro de destinatário"
        isResizable = true
        preferredSize = Dimension(744, 434)

        contentPane.add(JToolBar().apply {
            add(JButton("Novo destinatário").apply {
                font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
                icon = ImageIcon(this@RecipientView.javaClass.getResource("/imagens/usuario.gif"))
                horizontalTextPosition = 0
                maximumSize = Dimension(90, 60)
                minimumSize = Dimension(87, 47)
                preferredSize = Dimension(53, 51)
                verticalTextPosition = 3
                addActionListener { jbtNovoActionPerformed() }
            })
            add(JButton("Editar").apply {
                font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
                icon = ImageIcon(this@RecipientView.javaClass.getResource("/imagens/editar.gif"))
                horizontalTextPosition = 0
                maximumSize = Dimension(90, 60)
                verticalTextPosition = 3
                addActionListener { jbtEditarActionPerformed() }
            })
            add(JButton("Pesquisar").apply {
                font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
                icon = ImageIcon(this@RecipientView.javaClass.getResource("/imagens/binoculo.gif"))
                horizontalTextPosition = 0
                maximumSize = Dimension(90, 60)
                minimumSize = Dimension(47, 55)
                verticalTextPosition = 3
                addActionListener { jbtPesquisarActionPerformed() }
            })
            add(JButton("Excluir").apply {
                font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
                icon = ImageIcon(this@RecipientView.javaClass.getResource("/imagens/TRASH.gif"))
                horizontalTextPosition = 0
                maximumSize = Dimension(90, 60)
                minimumSize = Dimension(47, 55)
                verticalTextPosition = 3
                addActionListener { jbtExcluirActionPerformed() }
            })
        }, "North")

        contentPane.add(JPanel().apply {
            layout = MigLayout()

            add(JLabel("Procurar por:"))

            add(recipientSearch.apply {
                addActionListener { jbtPesquisarActionPerformed() }
            }, "span, grow, pushx, width 9999")

            add(JScrollPane().apply {
                setViewportView(recipientTable.apply {
                    model = recipientTableModel
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

        pack()
    }

    private fun jbtNovoActionPerformed() {
        val editRecipientView = RecipientEditView(parent = this)
        editRecipientView.isVisible = true
    }

    private fun jbtEditarActionPerformed() {
        if (recipientTable.selectedRow < 0) {
            JOptionPane.showMessageDialog(
                this,
                "Não existe nenhum remetente selecionado!",
                "Endereçador ECT",
                JOptionPane.INFORMATION_MESSAGE)
            return
        }
        val telaEditarDestinatrio = RecipientEditView(
            recipientTableModel.getAt(recipientTable.selectedRow),
            parent = this)
        telaEditarDestinatrio.isVisible = true
    }

    private fun jbtPesquisarActionPerformed() {
        val search = recipientSearch.text.trim()

        recipientTableModel
            .filterIndexed { index, _ ->
                if (lastSearch.equals(search, ignoreCase = true)) {
                    index > recipientTable.selectedRow
                } else {
                    lastSearch = search
                    true
                }
            }
            .find { it.nome.contains(search, ignoreCase = true).or(it.apelido.contains(search, ignoreCase = true)) }
            ?.let {
                val index = recipientTableModel.indexOf(it)
                recipientTable.selectionModel.setSelectionInterval(index, index)
                return
            }

        JOptionPane.showMessageDialog(
            this,
            "Destinatário não encontrado!",
            "Endereçador",
            JOptionPane.INFORMATION_MESSAGE)
    }

    private fun jbtExcluirActionPerformed() {
        if (recipientTable.selectedRow == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Não existe nenhum destinatário selecionado!",
                "Enderecador",
                JOptionPane.WARNING_MESSAGE)
        } else {
            val response = JOptionPane.showOptionDialog(
                this,
                "Tem certeza que deseja excluir o(s) destinatário(s)?",
                "Endereçador",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                arrayOf("Sim", "Não"),
                null
            )
            if (response == JOptionPane.YES_OPTION) {
                try {
                    recipientTable.selectedRows.forEach { row ->
                        val recipient = recipientTableModel.getAt(row)
                        recipientGroupDao.excluirDestinatarioDoGrupo(recipient.numeroDestinatario!!)
                        recipientDao.excluirDestinatario(recipient.numeroDestinatario!!)
                        recipientTableModel.removeRowAt(row)
                    }
                    recipientSearch.text = ""
                    JOptionPane.showMessageDialog(
                        this,
                        "Destinatário(s) excluído(s) com sucesso!",
                        "Endereçador",
                        JOptionPane.INFORMATION_MESSAGE)
                } catch (ex: DaoException) {
                    logger.error(ex.message, ex as Throwable)
                    JOptionPane.showMessageDialog(
                        this,
                        "Não foi possivel carregar relação de destinatários!",
                        "Endereçador ECT",
                        JOptionPane.WARNING_MESSAGE)
                }
            }
        }
    }
}
