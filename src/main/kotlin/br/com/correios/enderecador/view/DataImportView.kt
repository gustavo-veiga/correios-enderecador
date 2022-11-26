package br.com.correios.enderecador.view

import javax.swing.JFrame
import javax.swing.JComboBox
import javax.swing.JTextArea
import javax.swing.JTable
import javax.swing.JToolBar
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JLabel
import javax.swing.JScrollPane
import javax.swing.ImageIcon
import javax.swing.JFileChooser
import javax.swing.JOptionPane
import br.com.correios.enderecador.dao.GrupoDao
import br.com.correios.enderecador.exception.DaoException
import br.com.correios.enderecador.dao.DestinatarioDao
import br.com.correios.enderecador.bean.GrupoDestinatarioBean
import br.com.correios.enderecador.dao.GrupoDestinatarioDao
import br.com.correios.enderecador.service.CsvService
import br.com.correios.enderecador.tablemodel.RecipientImportTableModel
import br.com.correios.enderecador.util.*
import br.com.correios.enderecador.util.FileExtension.CSV
import br.com.correios.enderecador.util.FileExtension.TXT
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.miginfocom.swing.MigLayout
import org.koin.core.annotation.Singleton
import java.awt.Dimension
import java.awt.Font
import java.awt.Font.PLAIN
import java.awt.Font.SANS_SERIF

@Singleton
class DataImportView(
    private val groupDao: GrupoDao,
    private val csvService: CsvService,
    private val recipientDao: DestinatarioDao,
    private val recipientGroupDao: GrupoDestinatarioDao,
) : JFrame() {
    private val logger by Logging()

    private val recipientImportTableModel = RecipientImportTableModel()
    private val recipientTable = JTable()

    private val groupOptions = JComboBox<String>()
    private val messageArea = JTextArea()

    init {
        initComponents()
        carregaGrupo()
        setLocationRelativeTo(null)
    }

    private fun initComponents() {
        title = "Importar dados"
        size = Dimension(750, 700)
        isResizable = true

        contentPane.add(JToolBar().apply {
            add(JButton("Confirmar").apply {
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@DataImportView.javaClass.getResource("/imagens/OK.gif"))
                maximumSize = Dimension(90, 60)
                verticalTextPosition = 3
                horizontalTextPosition = 0
                addActionListener { gravarDados() }
            })
            add(JButton("Abrir arquivo").apply {
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@DataImportView.javaClass.getResource("/imagens/arquivo.gif"))
                maximumSize = Dimension(90, 60)
                verticalTextPosition = 3
                horizontalTextPosition = 0
                addActionListener { jbtnAbrirActionPerformed() }
            })
            add(JButton("Limpar tela").apply {
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@DataImportView.javaClass.getResource("/imagens/cancelar.gif"))
                maximumSize = Dimension(90, 60)
                verticalTextPosition = 3
                horizontalTextPosition = 0
                addActionListener { limpaTela() }
            })
        }, "North")

        contentPane.add(JPanel().apply {
            layout = MigLayout()

            add(JLabel("Grupo de destinatários:"))

            add(groupOptions, "span, grow, pushx, width 9999")

            add(JScrollPane().apply {
                setViewportView(recipientTable.apply {
                    model = recipientImportTableModel
                })
            }, "span, grow, push")

            add(JScrollPane().apply {
                setViewportView(messageArea.apply {
                    rows = 5
                    columns = 20
                    isEditable = false
                })
            }, "span, grow, push")
        }, "Center")
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun jbtnAbrirActionPerformed() {
        val fileChooser = JFileChooser().apply {
            fileSelectionMode = 0
            approveButtonText = "Abrir"
            dialogTitle = "Abrir arquivo"
            isAcceptAllFileFilterUsed = false
            addChoosableFileFilter(FileTypeFilter(CSV))
            addChoosableFileFilter(FileTypeFilter(TXT))
        }
        if (fileChooser.showOpenDialog(this) == JFileChooser.CANCEL_OPTION) {
            return
        }

        val file = fileChooser.selectedFile ?: return
        if (!file.exists()) {
            JOptionPane.showMessageDialog(
                this,
                "Não é possível importar esse tipo de arquivo.",
                "Endereçador ",
                JOptionPane.WARNING_MESSAGE)
            return
        }

        GlobalScope.launch(Dispatchers.Main) {
            when (file.extension) {
                CSV.extension -> csvService.import(file.absolutePath, true)
                TXT.extension -> csvService.import(file.absolutePath, false)
                else -> return@launch
            }.collect {
                recipientImportTableModel.insertRow(it)
            }
        }
    }

    fun carregaGrupo() {
        try {
            groupOptions.addItem("(Selecione um grupo)")

            val groups = groupDao.recuperaGrupo()
            groups.forEach { group ->
                groupOptions.addItem(group.descricaoGrupo)
            }
        } catch (e: DaoException) {
            logger.error(e.message, e)
            JOptionPane.showMessageDialog(
                this,
                "Não foi possivel carregar relação dos grupos",
                "Endereçador ECT",
                JOptionPane.WARNING_MESSAGE)
        }
    }

    private fun gravarDados() {
        if (recipientImportTableModel.rowCount != 0) {
            try {
                recipientImportTableModel.forEach { recipient ->
                    recipientDao.incluirDestinatario(recipient)

                    if (groupOptions.selectedIndex != 0) {
                        val groupNumber = groupOptions.selectedItem as String
                        recipientGroupDao.incluirGrupoDestinatario(
                            GrupoDestinatarioBean(
                                numeroGrupo = groupNumber,
                                numeroDestinatario = recipient.numeroDestinatario!!
                            ))
                    }
                }
                JOptionPane.showMessageDialog(
                    this,
                    "Dados Gravados com sucesso",
                    "Enderecador",
                    JOptionPane.INFORMATION_MESSAGE)
                limpaTela()
            } catch (ex: DaoException) {
                logger.error(ex.message, ex)
                JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível gravar esse destinatário.",
                    "Enderecador",
                    JOptionPane.WARNING_MESSAGE)
            }
        }
    }

    private fun limpaTela() {
        messageArea.text = ""
    }
}
