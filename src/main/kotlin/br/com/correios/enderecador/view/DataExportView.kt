package br.com.correios.enderecador.view

import javax.swing.JFrame
import br.com.correios.enderecador.bean.DestinatarioBean
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JList
import br.com.correios.enderecador.dao.DestinatarioDao
import br.com.correios.enderecador.exception.DaoException
import javax.swing.JOptionPane
import javax.swing.JToolBar
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.ImageIcon
import javax.swing.BorderFactory
import javax.swing.JFileChooser
import br.com.correios.enderecador.service.CsvService
import br.com.correios.enderecador.util.FileExtension.CSV
import br.com.correios.enderecador.util.FileExtension.TXT
import br.com.correios.enderecador.util.FileTypeFilter
import br.com.correios.enderecador.util.Logging
import br.com.correios.enderecador.util.getAllItems
import br.com.correios.enderecador.util.insertNotRepeated
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.annotation.Singleton
import java.awt.*
import java.awt.Font.PLAIN
import java.awt.Font.SANS_SERIF
import javax.swing.JFileChooser.CANCEL_OPTION

@Singleton
@OptIn(DelicateCoroutinesApi::class)
class DataExportView(
    private val csvService: CsvService,
    private val recipientDao: DestinatarioDao
) : JFrame() {
    private val logger by Logging()

    private val recipientSearchView = RecipientSearchView()
    private val groupSearchView = GroupSearchView()

    private val recipientList = JList<DestinatarioBean>()
    private val exportAllCheckBox = JCheckBox()
    private val recipientSelectButton = JButton()
    private val groupSelectButton = JButton()

    init {
        initComponents()
        setLocationRelativeTo(null)

        GlobalScope.launch(Dispatchers.Main) {
            recipientSearchView.recipientListState.onEach { recipients ->
                recipientList.insertNotRepeated(recipients, compareBy { it.numeroDestinatario })
            }.launchIn(scope = this)
            groupSearchView.recipientListState.onEach { recipients ->
                recipientList.insertNotRepeated(recipients, compareBy { it.numeroDestinatario })
            }.launchIn(scope = this)
        }
    }

    fun carregaListaDestinatario() {
        try {
            val recipients = recipientDao.recuperaDestinatario()
            recipientList.setListData(recipients.toTypedArray())
        } catch (e: DaoException) {
            logger.error(e.message, e)
            JOptionPane.showMessageDialog(
                this,
                "Não foi possivel carregar relação de destinatários",
                "Endereçador ",
                JOptionPane.WARNING_MESSAGE)
        }
    }

    private fun initComponents() {
        title = "Exportar Dados"
        isResizable = true
        preferredSize = Dimension(744, 434)

        contentPane.add(JToolBar().apply {
            preferredSize = Dimension(325, 59)
            add(JButton().apply {
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@DataExportView.javaClass.getResource("/imagens/OK.gif"))
                text = "Exportar"
                maximumSize = Dimension(90, 60)
                verticalTextPosition = 3
                horizontalTextPosition = 0
                addActionListener { jbtExportarActionPerformed() }
            })
            add(recipientSelectButton.apply {
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@DataExportView.javaClass.getResource("/imagens/addusuario.gif"))
                text = "Selecionar destinatário"
                maximumSize = Dimension(107, 60)
                verticalTextPosition = 3
                horizontalTextPosition = 0
                addActionListener { jbntDestinatarioActionPerformed() }
            })
            add(groupSelectButton.apply {
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@DataExportView.javaClass.getResource("/imagens/addusuarios.gif"))
                text = "Selecionar grupo"
                maximumSize = Dimension(90, 60)
                preferredSize = Dimension(90, 60)
                verticalTextPosition = 3
                horizontalTextPosition = 0
                addActionListener { jbntGrupoActionPerformed() }
            })
            add(JButton().apply {
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@DataExportView.javaClass.getResource("/imagens/removerTodos.gif"))
                text = "Remover todos"
                maximumSize = Dimension(90, 60)
                verticalTextPosition = 3
                horizontalTextPosition = 0
                addActionListener { jbntExcluirActionPerformed() }
            })
        }, "North")

        contentPane.add(JPanel().apply {
            layout = BorderLayout()
            add(JPanel().apply {
                layout = GridLayout(1, 0)
                border = BorderFactory.createEtchedBorder()
                add(exportAllCheckBox.apply {
                    font = Font(SANS_SERIF, PLAIN, 10)
                    text = "Exportar todos os destinatários"
                    addActionListener { jchkExportarTodosActionPerformed() }
                })
            }, "North")
            add(JScrollPane().apply {
                setViewportView(recipientList)
            }, "Center")
        }, "Center")

        pack()
    }

    private fun jbntExcluirActionPerformed() {
        recipientList.setListData(emptyArray())
        exportAllCheckBox.isSelected = false
        recipientSelectButton.isEnabled = true
        groupSelectButton.isEnabled = true
    }

    private fun jbntGrupoActionPerformed() {
        cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
        groupSearchView.isVisible = true
        cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
    }

    private fun jbntDestinatarioActionPerformed() {
        cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
        recipientSearchView.isVisible = true
        cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
    }

    private fun jchkExportarTodosActionPerformed() {
        cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
        recipientSelectButton.isEnabled = !recipientSelectButton.isEnabled
        groupSelectButton.isEnabled = !groupSelectButton.isEnabled
        if (exportAllCheckBox.isSelected) {
            carregaListaDestinatario()
        } else {
            recipientList.setListData(arrayOf())
        }
        cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
    }

    private fun jbtExportarActionPerformed() {
        if (recipientList.model.size <= 0) {
            JOptionPane.showMessageDialog(
                this,
                "Não existe nenhum destinatário selecionado!",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE)
            return
        }
        val fileChooser = JFileChooser().apply {
            approveButtonText = "Salvar"
            dialogTitle = "Salvar arquivo"
            fileSelectionMode = 0
            isAcceptAllFileFilterUsed = false
            addChoosableFileFilter(FileTypeFilter(CSV))
            addChoosableFileFilter(FileTypeFilter(TXT))
        }
        if (fileChooser.showSaveDialog(this) == CANCEL_OPTION) {
            return
        }

        val file = fileChooser.selectedFile ?: return
        val filter = fileChooser.fileFilter as FileTypeFilter
        val filename = file.absolutePath + filter.fileExtension
        when (filter.fileExtension) {
            CSV -> csvService.export(filename, true, recipientList.getAllItems())
            TXT -> csvService.export(filename, false, recipientList.getAllItems())
        }.invokeOnCompletion { throwable ->
            if (throwable is CancellationException) {
                JOptionPane.showMessageDialog(
                    this@DataExportView,
                    "",
                    "Endereçador",
                    JOptionPane.ERROR_MESSAGE)
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Exportação concluída!",
                    "Endereçador",
                    JOptionPane.WARNING_MESSAGE)
            }
        }
    }
}
