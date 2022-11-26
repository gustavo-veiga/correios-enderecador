package br.com.correios.enderecador.view

import javax.swing.JFrame
import javax.swing.ButtonGroup
import javax.swing.JPanel
import javax.swing.JCheckBox
import javax.swing.JComboBox
import br.com.correios.enderecador.bean.RemetenteBean
import javax.swing.JLabel
import javax.swing.JRadioButton
import javax.swing.JTable
import javax.swing.JTextField
import javax.swing.DefaultCellEditor
import br.com.correios.enderecador.dao.RemetenteDao
import br.com.correios.enderecador.exception.DaoException
import javax.swing.JOptionPane
import javax.swing.JToolBar
import javax.swing.JButton
import javax.swing.JScrollPane
import javax.swing.ImageIcon
import javax.swing.BorderFactory
import br.com.correios.enderecador.exception.EnderecadorExcecao
import br.com.correios.enderecador.service.PrintReportService
import br.com.correios.enderecador.tablemodel.RecipientPrintTableModel
import br.com.correios.enderecador.util.*
import br.com.correios.enderecador.util.TypeDocumentFormat.ONLY_DIGITS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.miginfocom.swing.MigLayout
import org.koin.core.annotation.Singleton
import java.awt.*
import java.awt.Font.PLAIN
import java.awt.Font.SANS_SERIF

@Singleton
class PrintOrderView(
    private val senderDao: RemetenteDao,
) : JFrame() {
    private val logger by Logging()

    private val recipientPrintTableModel = RecipientPrintTableModel(true)
    private val recipientPrintTable = JTable()

    private val recipientSearchView = RecipientSearchView()
    private val groupSearchView = GroupSearchView()

    private val senderOptions = JComboBox<RemetenteBean>()
    private val withPhoneRecipient = JCheckBox()
    private val withPhoneSender = JCheckBox()
    private val sheetWith2Labels = JRadioButton()
    private val sheetWith4Labels = JRadioButton()
    private val fromTheTag = JTextField()
    private val printFigure = JLabel()
    private val print = PrintReportService()

    private var report = Report.ORDER_2_NEIGHBOR

    init {
        initComponents()
        configuracoesAdicionais()
        carregaRemetente()
        setLocationRelativeTo(null)

        GlobalScope.launch(Dispatchers.Main) {
            recipientSearchView.recipientListState.onEach { recipients ->
                recipientPrintTableModel.insertNotRepeated(recipients, compareBy { it.numeroDestinatario })
            }.launchIn(scope = this)
            groupSearchView.recipientListState.onEach { recipients ->
                recipientPrintTableModel.insertNotRepeated(recipients, compareBy { it.numeroDestinatario })
            }.launchIn(scope = this)
        }
    }

    private fun configuracoesAdicionais() {
        var renderer = TextCellRenderer(2)

        recipientPrintTable.columnModel.getColumn(0).apply {
            cellRenderer = renderer
            preferredWidth = 100
        }
        recipientPrintTable.columnModel.getColumn(1).apply {
            preferredWidth = 250
            cellRenderer = renderer
        }
        recipientPrintTable.columnModel.getColumn(2).apply {
            preferredWidth = 40
            cellRenderer = renderer
        }

        renderer = TextCellRenderer(0)

        recipientPrintTable.columnModel.getColumn(3).apply {
            cellRenderer = renderer
            preferredWidth = 20
        }
        recipientPrintTable.columnModel.getColumn(4).apply {
            cellEditor = DefaultCellEditor(JComboBox(arrayOf("Sim", "Não")))
            cellRenderer = renderer
            preferredWidth = 10
        }
        recipientPrintTable.columnModel.getColumn(5).apply {
            cellRenderer = renderer
            preferredWidth = 20
        }
        recipientPrintTable.columnModel.getColumn(6).apply {
            cellRenderer = renderer
            preferredWidth = 10
        }
    }

    private fun carregaRemetente() {
        try {
            val senders = senderDao.recuperaRemetente()
            senders.forEach { sender -> senderOptions.addItem(sender) }
        } catch (e: DaoException) {
            logger.error(e.message, e)
            JOptionPane.showMessageDialog(
                this,
                "Não foi possivel carregar relação de remetentes",
                "Endereçador ECT",
                JOptionPane.WARNING_MESSAGE)
        }
    }

    private fun initComponents() {
        title = "Etiquetas para encomendas"
        size = Dimension(800, 480)
        isResizable = true

        contentPane.add(JToolBar().apply {
            add(JButton().apply {
                text = "Selecionar destinatário"
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@PrintOrderView.javaClass.getResource("/imagens/addusuario.gif"))
                maximumSize = Dimension(110, 60)
                verticalTextPosition = 3
                horizontalTextPosition = 0
                addActionListener { jbtnSelecionarDestinatarioActionPerformed() }
            })
            add(JButton().apply {
                text = "Selecionar grupo"
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@PrintOrderView.javaClass.getResource("/imagens/addusuarios.gif"))
                maximumSize = Dimension(100, 60)
                verticalTextPosition = 3
                horizontalTextPosition = 0
                addActionListener { jbtnSelecionarGrupoActionPerformed() }
            })
            add(JButton().apply {
                text = "Remover destinatário"
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@PrintOrderView.javaClass.getResource("/imagens/remover.gif"))
                maximumSize = Dimension(110, 60)
                verticalTextPosition = 3
                horizontalTextPosition = 0
                addActionListener { jbtnRemoverDestinatarioActionPerformed() }
            })
            add(JButton().apply {
                text = "Remover todos"
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@PrintOrderView.javaClass.getResource("/imagens/removerTodos.gif"))
                maximumSize = Dimension(90, 60)
                verticalTextPosition = 3
                horizontalTextPosition = 0
                addActionListener { jbtnRemoverTodosActionPerformed() }
            })
            add(JButton().apply {
                text = "Visualizar AR"
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@PrintOrderView.javaClass.getResource("/imagens/print.gif"))
                maximumSize = Dimension(90, 60)
                verticalTextPosition = 3
                horizontalTextPosition = 0
                addActionListener { jbtnVisializarARActionPerformed() }
            }
            )
            add(JButton().apply {
                text = "Visualizar Etiqueta"
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@PrintOrderView.javaClass.getResource("/imagens/IMPRIMIR.gif"))
                maximumSize = Dimension(90, 60)
                verticalTextPosition = 3
                horizontalTextPosition = 0
                addActionListener { jbtnVisializarActionPerformed() }
            })
            add(JButton().apply {
                text = "Declaração de Conteúdo"
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@PrintOrderView.javaClass.getResource("/imagens/print.gif"))
                verticalTextPosition = 3
                horizontalTextPosition = 0
                addActionListener { jButton2ActionPerformed() }
            })
        }, "North")

        contentPane.add(JPanel().apply {
            layout = MigLayout("wrap 2")

            add(JPanel().apply {
                layout = MigLayout()
                border = BorderFactory.createTitledBorder(
                    null,
                    "Escolha o tamanho do rótulo que deseja gerar",
                    0,
                    0,
                    Font(SANS_SERIF, PLAIN, 10))

                add(printFigure.apply {
                    icon = ImageIcon(this@PrintOrderView.javaClass.getResource("/imagens/tipo_2etq.gif"))
                    border = BorderFactory.createEmptyBorder(1, 1, 1, 1)
                }, "span 1 2")

                add(sheetWith2Labels.apply {
                    font = Font(SANS_SERIF, PLAIN, 10)
                    text = "2 rótulos por vez, distribuídos numa folha"
                    isSelected = true
                    addActionListener { jrbtDoisRotulosActionPerformed() }
                }, "wrap")

                add(sheetWith4Labels.apply {
                    font = Font(SANS_SERIF, PLAIN, 10)
                    text = "4 rótulos por vez, distribuídos numa folha"
                    addActionListener { jrbtQuatroRotulosActionPerformed() }
                })
            }, "push, height 156")

            add(JPanel().apply {
                layout = MigLayout()
                border = BorderFactory.createTitledBorder(" ")

                add(JLabel().apply {
                    font = Font(SANS_SERIF, PLAIN, 10)
                    text = "Selecione o remetente:"
                })

                add(senderOptions, "wrap")

                add(JLabel().apply {
                    font = Font(SANS_SERIF, PLAIN, 10)
                    text = "Quero imprimir a partir da etiqueta nº:"
                })

                add(fromTheTag.apply {
                    document = PersonalizedDocument(3, ONLY_DIGITS)
                }, "wrap")

                add(JLabel().apply {
                    font = Font(SANS_SERIF, PLAIN, 10)
                    text = "Incluir telefone na etiqueta:"
                })

                add(withPhoneSender.apply {
                    font = Font(SANS_SERIF, PLAIN, 10)
                    text = "do remetente"
                })

                add(withPhoneRecipient.apply {
                    font = Font(SANS_SERIF, PLAIN, 10)
                    text = "do destinatário"
                })
            }, "span, grow, push, height 156")

            add(JPanel().apply {
                layout = MigLayout()
                border = BorderFactory.createEtchedBorder()

                add(JLabel().apply {
                    foreground = Color(51, 51, 255)
                    text = "* Os campos de Quantidades, Mão Própria, Observação são editáveis com um duplo clique."
                }, "wrap")

                add(JScrollPane().apply {
                    setViewportView(recipientPrintTable.apply {
                        model = recipientPrintTableModel
                    })
                }, "span, grow, push")
            }, "span, grow, push")
        }, "Center")

        ButtonGroup().apply {
            add(sheetWith2Labels)
            add(sheetWith4Labels)
        }
    }

    private fun jbtnVisializarARActionPerformed() {
        if (recipientPrintTableModel.rowCount < 1) {
            JOptionPane.showMessageDialog(
                this,
                "Não existe nenhum destinatário selecionado!",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE)
            return
        }
        if (senderOptions.itemCount == 0) {
            JOptionPane.showMessageDialog(
                this,
                "Por favor cadastre um remetente antes de fazer a impressão.",
                "Endereçador ECT",
                JOptionPane.WARNING_MESSAGE)
            return
        }
        try {
            print.imprimirAR(
                Report.AR.file,
                senderOptions.selectedItem as RemetenteBean,
                recipientPrintTableModel.getAll())
        } catch (ex: EnderecadorExcecao) {
            logger.error(ex.message, ex as Throwable)
            JOptionPane.showMessageDialog(
                this,
                "Não foi possível imprimir o AR",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE)
        }
    }

    private fun jrbtQuatroRotulosActionPerformed() {
        printFigure.icon = ImageIcon(javaClass.getResource("/imagens/tipo_4etq.gif"))
        report = Report.ORDER_4_NEIGHBOR
    }

    private fun jrbtDoisRotulosActionPerformed() {
        printFigure.icon = ImageIcon(javaClass.getResource("/imagens/tipo_2etq.gif"))
        report = Report.ORDER_2_NEIGHBOR
    }

    private fun jbtnVisializarActionPerformed() {
        if (recipientPrintTableModel.rowCount < 1) {
            JOptionPane.showMessageDialog(
                this,
                "Não existe nenhum destinatário selecionado!",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE)
            return
        }
        if (senderOptions.itemCount == 0) {
            JOptionPane.showMessageDialog(
                this,
                "Por favor cadastre um remetente antes de fazer a impressão.",
                "Endereçador ECT",
                JOptionPane.WARNING_MESSAGE)
            return
        }
        try {
            if (fromTheTag.text.isBlank()) {
                fromTheTag.text = "1"
            }
            if (report == Report.ORDER_2_NEIGHBOR && fromTheTag.text.toInt() > 2) {
                JOptionPane.showMessageDialog(
                    this,
                    "Posição inicial para impressão de etiquetas deve ser 1 ou 2.",
                    "Endereçador",
                    JOptionPane.WARNING_MESSAGE)
                return
            }
            if (report == Report.ORDER_4_NEIGHBOR && fromTheTag.text.toInt() > 4) {
                JOptionPane.showMessageDialog(
                    this,
                    "Posição inicial para impressão de etiquetas deve ser um número de 1 a 4.",
                    "Endereçador",
                    JOptionPane.WARNING_MESSAGE)
                return
            }
            print.order(
                report,
                senderOptions.selectedItem as RemetenteBean,
                recipientPrintTableModel.getAll(),
                fromTheTag.text.toInt())
        } catch (ex: EnderecadorExcecao) {
            logger.error(ex.message, ex)
            JOptionPane.showMessageDialog(
                this,
                "Não foi possível imprimir o relatório.",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE)
        }
    }

    private fun jbtnRemoverTodosActionPerformed() {
        recipientPrintTableModel.setAll(arrayListOf())
    }

    private fun jbtnRemoverDestinatarioActionPerformed() {
        if (recipientPrintTable.selectedRow == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Não existe nenhum destinatário selecionado.",
                "Endereçador ECT",
                JOptionPane.INFORMATION_MESSAGE)
            return
        }
        recipientPrintTable.selectedRows.forEach { row ->
            recipientPrintTableModel.removeRowAt(row)
        }
    }

    private fun jbtnSelecionarGrupoActionPerformed() {
        groupSearchView.isVisible = true
    }

    private fun jbtnSelecionarDestinatarioActionPerformed() {
        recipientSearchView.isVisible = true
    }

    private fun jButton2ActionPerformed() {
        if (recipientPrintTableModel.rowCount < 1) {
            JOptionPane.showMessageDialog(
                this,
                "Não existe nenhum destinatário selecionado!",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE)
            return
        }
        if (senderOptions.itemCount == 0) {
            JOptionPane.showMessageDialog(
                this,
                "Por favor cadastre um remetente antes de fazer a impressão.",
                "Endereçador ECT",
                JOptionPane.WARNING_MESSAGE)
            return
        }
        val contentDeclarationView = ContentDeclarationView(
            recipientPrintTableModel.getAll(),
            (senderOptions.selectedItem as RemetenteBean)
        )
        contentDeclarationView.isVisible = true
    }
}
