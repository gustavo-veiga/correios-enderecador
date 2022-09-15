package br.com.correios.enderecador.view

import br.com.correios.enderecador.bean.GrupoBean
import br.com.correios.enderecador.dao.GrupoDao
import br.com.correios.enderecador.dao.GrupoDestinatarioDao
import br.com.correios.enderecador.exception.DaoException
import br.com.correios.enderecador.tablemodel.DestinatarioTableModel
import br.com.correios.enderecador.util.getAllItems
import br.com.correios.enderecador.util.setSelectedItem
import net.miginfocom.swing.MigLayout
import org.apache.log4j.Logger
import org.koin.core.annotation.Singleton
import java.awt.Dimension
import java.awt.Font
import java.awt.Font.PLAIN
import java.awt.Font.SANS_SERIF
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*

@Singleton
class TelaGrupo(
    private val groupDao: GrupoDao,
    private val recipientGroupDao: GrupoDestinatarioDao
) : JFrame() {
    private val recipientTableModel = DestinatarioTableModel()
    private val recipientTable = JTable()
    private val groupSearch = JTextField()
    private val groupList = JList<GrupoBean>()

    init {
        initComponents()
        recuperarListaGrupo()
        setLocationRelativeTo(null)
    }

    private fun initComponents() {
        isResizable = true
        title = "Cadastro de Grupos"
        preferredSize = Dimension(744, 434)

        contentPane.add(JToolBar().apply {
            preferredSize = Dimension(100, 59)
            add(JButton("Novo grupo").apply {
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@TelaGrupo.javaClass.getResource("/imagens/usuarios.gif"))
                maximumSize = Dimension(90, 60)
                verticalTextPosition = 3
                horizontalTextPosition = 0
                addActionListener { jbtNovoActionPerformed() }
            })
            add(JButton("Editar").apply {
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@TelaGrupo.javaClass.getResource("/imagens/editar.gif"))
                maximumSize = Dimension(90, 60)
                verticalTextPosition = 3
                horizontalTextPosition = 0
                addActionListener { jbtEditarActionPerformed() }
            })
            add(JButton("Pesquisar").apply {
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@TelaGrupo.javaClass.getResource("/imagens/binoculo.gif"))
                maximumSize = Dimension(90, 60)
                verticalTextPosition = 3
                horizontalTextPosition = 0
                addActionListener { jbtPesquisarActionPerformed() }
            })
            add(JButton("Excluir").apply {
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@TelaGrupo.javaClass.getResource("/imagens/TRASH.gif"))
                maximumSize = Dimension(90, 60)
                verticalTextPosition = 3
                horizontalTextPosition = 0
                addActionListener { jbtExcluirActionPerformed() }
            })
        }, "North")

        contentPane.add(JPanel().apply {
            layout = MigLayout()

            add(JLabel().apply {
                text = "Procurar por:"
                font = Font(SANS_SERIF, PLAIN, 10)
            }, "split 2")

            add(groupSearch.apply {
                addActionListener {
                    jbtPesquisarActionPerformed()
                }
            }, "pushx, width 9999, wrap")

            add(JLabel().apply {
                text = "Grupos:"
                font = Font(SANS_SERIF, PLAIN, 10)
            }, "wrap")

            add(JScrollPane().apply {
                minimumSize = Dimension(this.width, 80)
                setViewportView(groupList.apply {
                    addListSelectionListener { jlstTelEndGrupoValueChanged() }
                    addMouseListener(object : MouseAdapter() {
                        override fun mouseClicked(e: MouseEvent) {
                            if (e.clickCount == 2) {
                                jbtEditarActionPerformed()
                            }
                        }
                    })
                })
            }, "span, grow, push")

            add(JLabel().apply {
                text = "Destinatários do grupo:"
                font = Font(SANS_SERIF, PLAIN, 10)
            }, "wrap")

            add(JScrollPane().apply {
                setViewportView(recipientTable.apply {
                    model = recipientTableModel
                })
            }, "span, grow, push")
        }, "Center")

        pack()
    }

    private fun jbtExcluirActionPerformed() {
        if (groupList.isSelectionEmpty) {
            JOptionPane.showMessageDialog(
                this,
                "Não existe nenhum grupo selecionado!",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE)
        } else {
            groupList.clearSelection()
            val resp = JOptionPane.showOptionDialog(
                this,
                "Tem certeza que deseja excluir este grupo juntamente com todos os seus destinatários?",
                "Endereçador",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                arrayOf("Sim", "Não"),
                null
            )
            if (resp == JOptionPane.YES_OPTION) {
                val group = groupList.selectedValue
                try {
                    recipientGroupDao.excluirGrupoDestinatario(group.numeroGrupo)
                    groupDao.excluirGrupo(group.numeroGrupo)
                    recuperarListaGrupo()
                    groupSearch.text = ""
                    JOptionPane.showMessageDialog(
                        this,
                        "Grupo e destinatários excluídos com sucesso!",
                        "Endereçador",
                        JOptionPane.INFORMATION_MESSAGE)
                } catch (ex: DaoException) {
                    logger.error(ex.message, ex)
                    JOptionPane.showMessageDialog(
                        this,
                        "Não foi possivel realizar exclusão!",
                        "Endereçador",
                        JOptionPane.WARNING_MESSAGE)
                }
            }
        }
    }

    private fun jbtEditarActionPerformed() {
        if (groupList.isSelectionEmpty.not()) {
            val group = groupList.selectedValue
            val telaEditarGrupo = TelaEditarGrupo(group, parent = this)
            telaEditarGrupo.isVisible = true
            recuperarListaGrupo()
        } else {
            JOptionPane.showMessageDialog(
                this,
                "Não existe nenhum grupo selecionado!",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE)
        }
    }

    private fun jbtNovoActionPerformed() {
        val editGroupView = TelaEditarGrupo(parent = this)
        editGroupView.isVisible = true
        recuperarListaGrupo()
    }

    private fun jlstTelEndGrupoValueChanged() {
        val group = groupList.selectedValue
        val recipient = recipientGroupDao.recuperaGrupoDestinatario(group.numeroGrupo)
        recipientTableModel.setAll(recipient)
    }

    fun recuperarListaGrupo() {
        groupList.setListData(groupDao.recuperaGrupo().toTypedArray())
    }

    private fun jbtPesquisarActionPerformed() {
        val search = groupSearch.text.trim()

        groupList.getAllItems()
            .find { it.descricaoGrupo.contains(search, ignoreCase = true) }
            ?.let {
                groupList.setSelectedItem(it)
                return
            }

        JOptionPane.showMessageDialog(
            this,
            "Grupo não encontrado!",
            "Endereçador",
            JOptionPane.INFORMATION_MESSAGE)
    }

    companion object {
        private val logger = Logger.getLogger(TelaGrupo::class.java)
    }
}
