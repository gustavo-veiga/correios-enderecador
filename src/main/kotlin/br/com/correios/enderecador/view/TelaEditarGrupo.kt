package br.com.correios.enderecador.view

import javax.swing.JDialog
import javax.swing.JTable
import javax.swing.JTextField
import br.com.correios.enderecador.bean.GrupoBean
import br.com.correios.enderecador.dao.DestinatarioDao
import br.com.correios.enderecador.exception.DaoException
import javax.swing.JOptionPane
import javax.swing.JToolBar
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JLabel
import javax.swing.JScrollPane
import javax.swing.ImageIcon
import br.com.correios.enderecador.bean.GrupoDestinatarioBean
import br.com.correios.enderecador.dao.GrupoDao
import br.com.correios.enderecador.dao.GrupoDestinatarioDao
import br.com.correios.enderecador.tablemodel.DestinatarioTableModel
import net.miginfocom.swing.MigLayout
import org.apache.log4j.Logger
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import java.awt.Dimension
import java.awt.Font
import java.awt.Font.PLAIN
import java.awt.Font.SANS_SERIF
import java.awt.Frame
import javax.swing.JTable.AUTO_RESIZE_OFF

class TelaEditarGrupo : KoinComponent, JDialog {
    private val recipientGroupDao: GrupoDestinatarioDao = get()
    private val recipientDao: DestinatarioDao = get()
    private val groupDao: GrupoDao = get()

    private val recipientGroupModel = DestinatarioTableModel()
    private val recipientModel = DestinatarioTableModel()

    private val recipientGroupTable = JTable()
    private val recipientTable = JTable()
    private val groupName = JTextField()

    private var groupNumber = ""
    private var isInclude = true

    constructor(parent: Frame? = null, modal: Boolean = true) : super(parent, modal) {
        initComponents()
        setLocationRelativeTo(null)
        carregaListaDestinatario("")
    }

    constructor(group: GrupoBean, parent: Frame? = null, modal: Boolean = true) : super(parent, modal) {
        initComponents()
        isInclude = false
        setLocationRelativeTo(null)
        carregaListaDestinatario(group.numeroGrupo)
        groupName.text = group.descricaoGrupo
        groupNumber = group.numeroGrupo
    }

    private fun carregaListaDestinatario(grupo: String) {
        try {
            if (grupo.isEmpty()) {
                recipientModel.setAll(recipientDao.recuperaDestinatario())
                recipientGroupModel.setAll(listOf())
            } else {
                recipientModel.setAll(recipientDao.recuperarDestinatarioForaDoGrupo(grupo))
                recipientGroupModel.setAll(recipientDao.recuperaDestinatarioPorGrupo(grupo))
            }
        } catch (e: DaoException) {
            logger.error(e.message, e as Throwable)
            JOptionPane.showMessageDialog(
                this,
                "Não foi possivel carregar relação de destinatários",
                "Endereçador ",
                JOptionPane.WARNING_MESSAGE)
        }
    }

    private fun initComponents() {
        title = "Cadastrar Grupo"
        defaultCloseOperation = DISPOSE_ON_CLOSE

        contentPane.add(JToolBar().apply {
            add(JButton().apply {
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@TelaEditarGrupo.javaClass.getResource("/imagens/OK.gif"))
                text = "Confirmar"
                horizontalTextPosition = 0
                maximumSize = Dimension(90, 60)
                verticalTextPosition = 3
                addActionListener { jbtConfirmarActionPerformed() }
            })
            add(JButton().apply {
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@TelaEditarGrupo.javaClass.getResource("/imagens/sair.gif"))
                text = "Voltar"
                horizontalTextPosition = 0
                maximumSize = Dimension(90, 60)
                verticalTextPosition = 3
                addActionListener { dispose() }
            })
        }, "North")

        contentPane.add(JPanel().apply {
            layout = MigLayout()

            add(JLabel("Grupo:"), "split 2")

            add(groupName, "pushx, width 150, wrap")

            add(JScrollPane().apply {
                setViewportView(recipientTable.apply {
                    model = recipientModel
                    autoResizeMode = AUTO_RESIZE_OFF
                })
            })

            add(JPanel().apply {
                layout = MigLayout("wrap 1")

                add(JButton().apply {
                    text = "Adicionar"
                    font = Font(SANS_SERIF, PLAIN, 9)
                    icon = ImageIcon(this@TelaEditarGrupo.javaClass.getResource("/imagens/add.gif"))
                    addActionListener { insertAndRemoveItems(recipientTable, recipientGroupModel, recipientModel) }
                })

                add(JButton().apply {
                    text = "Remover"
                    font = Font(SANS_SERIF, PLAIN, 9)
                    icon = ImageIcon(this@TelaEditarGrupo.javaClass.getResource("/imagens/rem.gif"))
                    addActionListener { insertAndRemoveItems(recipientGroupTable, recipientModel, recipientGroupModel) }
                })
            })

            add(JScrollPane().apply {
                setViewportView(recipientGroupTable.apply {
                    model = recipientGroupModel
                    autoResizeMode = AUTO_RESIZE_OFF
                })
            })
        })

        pack()
    }

    private fun insertAndRemoveItems(table: JTable, modelToInsert: DestinatarioTableModel, modelToRemove: DestinatarioTableModel) {
        table.selectedRows.reversed().forEach { row ->
            modelToRemove.moveTo(modelToInsert, row)
        }
        modelToInsert.orderBy()
        modelToRemove.orderBy()
    }

    private fun jbtConfirmarActionPerformed() {
        if (groupName.text.isBlank()) {
            JOptionPane.showMessageDialog(
                this,
                "O campo grupo deve ser preenchido!",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE)
            groupName.requestFocus()
        } else {
            try {
                if (isInclude) {
                    groupDao.incluirGrupo(GrupoBean(
                        numeroGrupo = groupNumber,
                        descricaoGrupo = groupName.text
                    ))
                    groupNumber = groupDao.recuperaUltimoGrupo()
                    recipientGroupModel.forEach {
                        recipientGroupDao.incluirGrupoDestinatario(GrupoDestinatarioBean(
                            numeroGrupo = groupNumber,
                            numeroDestinatario = it.numeroDestinatario!!
                        ))
                    }
                    JOptionPane.showMessageDialog(
                        null,
                        "Dados gravados com sucesso!",
                        "Endereçador",
                        JOptionPane.INFORMATION_MESSAGE)
                    isInclude = false
                } else {
                    groupDao.alterarGrupo(GrupoBean(
                        numeroGrupo = groupNumber,
                        descricaoGrupo = groupName.text
                    ))
                    recipientGroupDao.excluirGrupoDestinatario(groupNumber)
                    recipientGroupModel.forEach {
                        recipientGroupDao.incluirGrupoDestinatario(GrupoDestinatarioBean(
                            numeroGrupo = groupNumber,
                            numeroDestinatario = it.numeroDestinatario!!
                        ))
                    }
                    JOptionPane.showMessageDialog(
                        null,
                        "Dados atualizados com sucesso!",
                        "Endereçador",
                        JOptionPane.INFORMATION_MESSAGE)
                }
            } catch (ge: DaoException) {
                logger.error(ge.message, ge)
                JOptionPane.showMessageDialog(
                    null,
                    "Não foi possivel gravar dados",
                    "Endereçador",
                    JOptionPane.WARNING_MESSAGE)
            }
        }
    }

    companion object {
        private val logger = Logger.getLogger(TelaEditarGrupo::class.java)
    }
}
