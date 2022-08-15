package br.com.correios.enderecador.telas

import br.com.correios.enderecador.util.EnderecadorObservable
import br.com.correios.enderecador.bean.DestinatarioBean
import br.com.correios.enderecador.dao.DestinatarioDao
import br.com.correios.enderecador.dao.DaoException
import java.awt.BorderLayout
import org.netbeans.lib.awtextra.AbsoluteLayout
import org.netbeans.lib.awtextra.AbsoluteConstraints
import javax.swing.table.DefaultTableModel
import br.com.correios.enderecador.dao.GrupoDestinatarioDao
import org.apache.log4j.Logger
import org.koin.core.annotation.Singleton
import java.awt.Dimension
import java.awt.Font
import java.util.*
import javax.swing.*
import javax.swing.JLabel.CENTER
import javax.swing.table.DefaultTableCellRenderer

@Singleton
class TelaDestinatario(
    private val destinatarioDao: DestinatarioDao,
    private val grupoDestinatarioDao: GrupoDestinatarioDao
) : JFrame(), Observer {
    private val destinatarioTableModel: DestinatarioTableModel = DestinatarioTableModel()
    private val observable: EnderecadorObservable? = EnderecadorObservable.instance
    private val jScrollPane =  JScrollPane()
    private val jtxtNomeDestinatario = JTextField()
    private val tabDestinatario = JTable()
    private var ultimaConsulta = ""

    init {
        initComponents()
        recuperarDadosTabelaDestinatario()
        observable?.addObserver(this)
        setLocationRelativeTo(null)
    }

    private fun recuperarDadosTabelaDestinatario() {
        try {
            val arrayDestinatario = destinatarioDao.recuperaDestinatario("")
            destinatarioTableModel.setDestinatario(arrayDestinatario)
            tabDestinatario.setSelectionMode(2)

            val centerRenderer = DefaultTableCellRenderer().apply {
                horizontalAlignment = CENTER
            }
            tabDestinatario.columnModel.getColumn(0).apply {
                 preferredWidth = 50
            }
            tabDestinatario.columnModel.getColumn(1).apply {
                preferredWidth = 70
                cellRenderer = centerRenderer
            }
            tabDestinatario.columnModel.getColumn(2).apply {
                preferredWidth = 70
            }
            tabDestinatario.columnModel.getColumn(3).apply {
                preferredWidth = 20
            }
            tabDestinatario.columnModel.getColumn(4).apply {
                preferredWidth = 5
                cellRenderer = centerRenderer
            }
            tabDestinatario.columnModel.getColumn(5).apply {
                width = 1
                preferredWidth = 1
                cellRenderer = centerRenderer
            }

        } catch (e: DaoException) {
            logger.error(e.message, e)
            JOptionPane.showMessageDialog(this, e.message,
                "Não foi possivel carregar relação de destinatários",
                JOptionPane.WARNING_MESSAGE
            )
        }
    }

    private fun initComponents() {
        title = "Cadastro de destinatário"
        isResizable = true
        preferredSize = Dimension(744, 434)

        contentPane.add(JToolBar().apply {
            add(JButton().apply {
                font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
                icon = ImageIcon(this@TelaDestinatario.javaClass.getResource("/imagens/usuario.gif"))
                text = "Novo destinatário"
                horizontalTextPosition = 0
                maximumSize = Dimension(90, 60)
                minimumSize = Dimension(87, 47)
                preferredSize = Dimension(53, 51)
                verticalTextPosition = 3
                addActionListener { jbtNovoActionPerformed() }
            })
            add(JButton().apply {
                font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
                icon = ImageIcon(this@TelaDestinatario.javaClass.getResource("/imagens/editar.gif"))
                text = "Editar"
                horizontalTextPosition = 0
                maximumSize = Dimension(90, 60)
                verticalTextPosition = 3
                addActionListener { jbtEditarActionPerformed() }
            })
            add(JButton().apply {
                font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
                icon = ImageIcon(this@TelaDestinatario.javaClass.getResource("/imagens/binoculo.gif"))
                text = "Pesquisar"
                horizontalTextPosition = 0
                maximumSize = Dimension(90, 60)
                minimumSize = Dimension(47, 55)
                verticalTextPosition = 3
                addActionListener { jbtPesquisarActionPerformed() }
            })
            add(JButton().apply {
                font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
                icon = ImageIcon(this@TelaDestinatario.javaClass.getResource("/imagens/TRASH.gif"))
                text = "Excluir"
                horizontalTextPosition = 0
                maximumSize = Dimension(90, 60)
                minimumSize = Dimension(47, 55)
                verticalTextPosition = 3
                addActionListener { jbtExcluirActionPerformed() }
            })
        }, "North")

        contentPane.add(JPanel().apply {
            layout = BorderLayout()
            border = BorderFactory.createEtchedBorder()
            add(jScrollPane, "Center")
            add(JPanel().apply {
                layout = AbsoluteLayout()
                add(jtxtNomeDestinatario, AbsoluteConstraints(90, 10, 300, -1))
                add(JLabel("Procurar por:"), AbsoluteConstraints(10, 10, -1, 20))
                add(JLabel("Destinatários:"), AbsoluteConstraints(10, 30, -1, 30))
            }, "North")
        }, "Center")

        tabDestinatario.model = DefaultTableModel(
            arrayOf(
                arrayOf(null, null, null, null),
                arrayOf(null, null, null, null),
                arrayOf(null, null, null, null),
                arrayOf(null, null, null, null)
            ), arrayOf("Title 1", "Title 2", "Title 3", "Title 4")
        )
        tabDestinatario.model = destinatarioTableModel
        jScrollPane.setViewportView(tabDestinatario)

        pack()
    }

    private fun jbtNovoActionPerformed() {
        val telaEditarDestinatrio = TelaEditarDestinatario(this, true)
        telaEditarDestinatrio.isVisible = true
    }

    private fun jbtEditarActionPerformed() {
        if (tabDestinatario.selectedRow < 0) {
            JOptionPane.showMessageDialog(
                this,
                "Não existe nenhum remetente selecionado!",
                "Endereçador ECT",
                JOptionPane.INFORMATION_MESSAGE
            )
            return
        }
        val telaEditarDestinatrio = TelaEditarDestinatario(this, true,
            destinatarioTableModel.getDestinatario(tabDestinatario.selectedRow)!!)
        telaEditarDestinatrio.isVisible = true
    }

    private fun jbtPesquisarActionPerformed() {
        val lsSelectionModelDestinatario = tabDestinatario.selectionModel
        val nomeDestinatario = jtxtNomeDestinatario.text.trim()
        val numeroRows = tabDestinatario.rowCount
        var index = -1
        if (ultimaConsulta.equals(nomeDestinatario, ignoreCase = true)) {
            index = tabDestinatario.selectedRow
        } else {
            ultimaConsulta = nomeDestinatario
        }
        for (i in index + 1 until numeroRows) {
            val nomeDestinatarioTabela = tabDestinatario.getValueAt(i, 0) as String
            if (nomeDestinatarioTabela.uppercase(Locale.getDefault())
                    .contains(nomeDestinatario.uppercase(Locale.getDefault()))
            ) {
                index = i
                break
            }
        }
        if (index == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Destinatário não encontrado!",
                "Endereçador",
                JOptionPane.INFORMATION_MESSAGE
            )
        } else {
            lsSelectionModelDestinatario.setSelectionInterval(index, index)
            val tamanhoJscrool = jScrollPane.verticalScrollBar.maximum
            val value = index * tamanhoJscrool / numeroRows
            jScrollPane.verticalScrollBar.value = value
        }
    }

    private fun jbtExcluirActionPerformed() {
        var destinatarioBean: DestinatarioBean?
        val listaDestinatarios: MutableList<DestinatarioBean?> = ArrayList()
        if (tabDestinatario.selectedRow == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Não existe nenhum destinatário selecionado!",
                "Enderecador",
                JOptionPane.WARNING_MESSAGE
            )
        } else {
            val options = arrayOf("Sim", "Não")
            val resp = JOptionPane.showOptionDialog(
                this,
                "Tem certeza que deseja excluir o(s) destinatário(s)?",
                "Endereçador",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                null
            )
            if (resp == 0) {
                val linhasSelecionadas = tabDestinatario.selectedRows
                try {
                    for (linhasSelecionada in linhasSelecionadas) {
                        destinatarioBean = destinatarioTableModel.getDestinatario(linhasSelecionada)
                        grupoDestinatarioDao.excluirDestinatarioDoGrupo(destinatarioBean!!.numeroDestinatario)
                        destinatarioDao.excluirDestinatario(destinatarioBean.numeroDestinatario)
                        listaDestinatarios.add(destinatarioBean)
                    }
                    observable?.notifyObservers(listaDestinatarios)
                    jtxtNomeDestinatario.text = ""
                    JOptionPane.showMessageDialog(
                        this,
                        "Destinatário(s) excluído(s) com sucesso!",
                        "Endereçador",
                        JOptionPane.INFORMATION_MESSAGE
                    )
                } catch (ex: DaoException) {
                    logger.error(ex.message, ex as Throwable)
                    JOptionPane.showMessageDialog(
                        this,
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
            arg
                .filterNotNull()
                .forEach { destinatarioTableModel.removeDestinatario(it as DestinatarioBean?) }
        }
    }

    companion object {
        private val logger = Logger.getLogger(TelaDestinatario::class.java)
    }
}
