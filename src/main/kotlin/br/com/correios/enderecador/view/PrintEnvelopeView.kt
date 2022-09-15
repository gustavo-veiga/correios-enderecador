package br.com.correios.enderecador.view

import javax.swing.JFrame
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JComboBox
import javax.swing.JLabel
import javax.swing.JRadioButton
import javax.swing.JTable
import javax.swing.JTextField
import javax.swing.DefaultCellEditor
import br.com.correios.enderecador.bean.RemetenteBean
import br.com.correios.enderecador.dao.RemetenteDao
import br.com.correios.enderecador.exception.DaoException
import javax.swing.JOptionPane
import javax.swing.ButtonGroup
import javax.swing.JToolBar
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.ImageIcon
import javax.swing.BorderFactory
import javax.swing.DefaultComboBoxModel
import br.com.correios.enderecador.exception.EnderecadorExcecao
import br.com.correios.enderecador.tablemodel.RecipientPrintTableModel
import br.com.correios.enderecador.util.*
import net.miginfocom.swing.MigLayout
import org.apache.log4j.Logger
import org.koin.core.annotation.Singleton
import java.awt.*
import java.awt.Font.PLAIN
import java.awt.Font.SANS_SERIF
import java.util.*

@Singleton
class PrintEnvelopeView(
    private val senderDao: RemetenteDao,
) : JFrame() {
    private val recipientPrintTableModel = RecipientPrintTableModel()
    private val recipientPrintTable = JTable()

    private val removeAllRecipient = JButton()
    private val removeRecipient = JButton()
    private val searchRecipientView = JButton()
    private val searchGroupView = JButton()
    private val viewAR = JButton()
    private val withPhoneRecipient = JCheckBox()
    private val withPhoneSender = JCheckBox()
    private val sheetSizeOptions = JComboBox<SheetSize>()
    private val senderOptions = JComboBox<RemetenteBean>()
    private val fontSizeOptions = JComboBox<FontSize>()
    private val repeatNumberSenderLabel = JLabel()
    private val sheetWith10Labels = JRadioButton()
    private val sheetWith14Labels = JRadioButton()
    private val selectRecipientAndSender = JRadioButton()
    private val selectRecipient = JRadioButton()
    private val selectSender = JRadioButton()
    private val fromTheTag = JTextField()
    private val repeatNumberSender = JTextField()
    private val senderLabel = JLabel()
    private val printFigure = JLabel()
    private val print = Impressao()

    private var imprimirTratamento = false
    private var report = Report.ENVELOPE_A4_14

    init {
        initComponents()
        configuracoesAdicionais()
        carregaRemetente()
        setLocationRelativeTo(null)
    }

    private fun configuracoesAdicionais() {
        var renderer = TextoCellRenderer(2)
        var coluna = recipientPrintTable.columnModel.getColumn(0)
        coluna.cellRenderer = renderer
        coluna.preferredWidth = 300
        coluna = recipientPrintTable.columnModel.getColumn(1)
        coluna.preferredWidth = 300
        coluna.cellRenderer = renderer
        coluna = recipientPrintTable.columnModel.getColumn(2)
        coluna.preferredWidth = 120
        coluna.cellRenderer = renderer
        coluna = recipientPrintTable.columnModel.getColumn(3)
        renderer = TextoCellRenderer(0)
        coluna.cellRenderer = renderer
        coluna.preferredWidth = 60
        coluna.width = 1
        coluna = recipientPrintTable.columnModel.getColumn(4)
        coluna.cellEditor = DefaultCellEditor(JComboBox(arrayOf("Sim", "Não")))
        coluna.cellRenderer = renderer
    }

    private fun carregaRemetente() {
        try {
            senderOptions.removeAllItems()
            val arrayRemetente = senderDao.recuperaRemetente()
            for (remetenteBean in arrayRemetente) senderOptions.addItem(remetenteBean)
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
        title = "Etiquetas para cartas"
        size = Dimension(910, 480)
        isResizable = true

        contentPane.add(JToolBar().apply {
            add(searchRecipientView.apply {
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@PrintEnvelopeView.javaClass.getResource("/imagens/addusuario.gif"))
                text = "Selecionar destinatário"
                isEnabled = false
                horizontalTextPosition = 0
                maximumSize = Dimension(110, 60)
                verticalTextPosition = 3
                addActionListener { jbtSelecionarDestinatarioActionPerformed() }
            })
            add(searchGroupView.apply {
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@PrintEnvelopeView.javaClass.getResource("/imagens/addusuarios.gif"))
                text = "Selecionar grupo"
                isEnabled = false
                horizontalTextPosition = 0
                maximumSize = Dimension(90, 60)
                verticalTextPosition = 3
                addActionListener { jbtSelecionarGrupoActionPerformed() }
            })
            add(removeRecipient.apply {
                font = Font("MS Sans Serif", 0, 9)
                icon = ImageIcon(this@PrintEnvelopeView.javaClass.getResource("/imagens/remover.gif"))
                text = "Remover destinatário"
                isEnabled = false
                horizontalTextPosition = 0
                maximumSize = Dimension(110, 60)
                verticalTextPosition = 3
                addActionListener { jbtRemoverDestinatarioActionPerformed() }
            })
            add(removeAllRecipient.apply {
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@PrintEnvelopeView.javaClass.getResource("/imagens/removerTodos.gif"))
                text = "Remover todos"
                isEnabled = false
                horizontalTextPosition = 0
                maximumSize = Dimension(90, 60)
                verticalTextPosition = 3
                addActionListener { jbtRemoverTodosActionPerformed() }
            })
            add(viewAR.apply {
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@PrintEnvelopeView.javaClass.getResource("/imagens/print.gif"))
                text = "Visualizar AR"
                isEnabled = false
                horizontalTextPosition = 0
                maximumSize = Dimension(90, 60)
                verticalTextPosition = 3
                addActionListener { jbtnVisializarARActionPerformed() }
            })
            add(JButton().apply {
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@PrintEnvelopeView.javaClass.getResource("/imagens/IMPRIMIR.gif"))
                text = "Visualizar"
                horizontalTextPosition = 0
                maximumSize = Dimension(90, 60)
                verticalTextPosition = 3
                addActionListener { jbtVisualizarActionPerformed() }
            })
        }, "North")

        contentPane.add(JPanel().apply {
            layout = MigLayout()

            add(JPanel().apply {
                layout = MigLayout()

                add(JLabel().apply {
                    font = Font(SANS_SERIF, PLAIN, 10)
                    text = "Quero imprimir etiquetas de:"
                })

                add(selectSender.apply {
                    font = Font(SANS_SERIF, PLAIN, 10)
                    isSelected = true
                    text = "Remetente"
                    addActionListener { jrbtRemetenteActionPerformed() }
                })

                add(selectRecipient.apply {
                    font = Font(SANS_SERIF, PLAIN, 10)
                    text = "Destinatário"
                    addActionListener { jrbtDestinatarioActionPerformed() }
                })

                add(selectRecipientAndSender.apply {
                    font = Font(SANS_SERIF, PLAIN, 10)
                    text = "Remetente e destinatário"
                    addActionListener { jrbtRemetenteDestinatarioActionPerformed() }
                })
            }, "span, grow, push")

            add(JPanel().apply {
                layout = MigLayout()
                border = BorderFactory.createTitledBorder(
                    null,
                    "Escolha o tamanho do rótulo que deseja gerar",
                    0,
                    0,
                    Font(SANS_SERIF, PLAIN, 10))

                add(printFigure.apply {
                    icon = ImageIcon(this@PrintEnvelopeView.javaClass.getResource("/imagens/tipo_14etq.gif"))
                }, "span 1 5")

                add(sheetWith14Labels.apply {
                    font = Font(SANS_SERIF, PLAIN, 10)
                    isSelected = true
                    text = "14 rótulos por vez, distribuidos numa folha"
                    addActionListener { jrbt14RotulosActionPerformed() }
                }, "wrap")

                add(sheetWith10Labels.apply {
                    font = Font(SANS_SERIF, PLAIN, 10)
                    text = "10 rótulos por vez, distribuídos numa folha "
                    addActionListener { jrbt10RotulosActionPerformed() }
                }, "wrap")

                add(JLabel().apply {
                    font = Font(SANS_SERIF, PLAIN, 10)
                    text = "(com impressão do tratamento)"
                }, "wrap")

                add(JLabel().apply {
                    font = Font(SANS_SERIF, PLAIN, 10)
                    text = "Folhas do tamanho:"
                })

                add(sheetSizeOptions.apply {
                    model = DefaultComboBoxModel(SheetSize.values())
                    addActionListener { jcmbFolhaActionPerformed() }
                }, "wrap")

                add(JLabel().apply {
                    font = Font(SANS_SERIF, PLAIN, 10)
                    text = "Tamanho da Fonte:"
                })

                add(fontSizeOptions.apply {
                    model = DefaultComboBoxModel(FontSize.values())
                    addActionListener { jcmbTamanhoFonteActionPerformed() }
                })
            })

            add(JPanel().apply {
                layout = MigLayout()
                border = BorderFactory.createTitledBorder(" ")

                add(senderLabel.apply {
                    font = Font(SANS_SERIF, PLAIN, 10)
                    text = "Selecione o remetente:"
                })

                add(senderOptions, "wrap")

                add(JLabel().apply {
                    font = Font(SANS_SERIF, PLAIN, 10)
                    text = "Quero imprimir a partir da etiqueta nº:"
                })

                add(fromTheTag.apply {
                    document = DocumentoPersonalizado(3, 1)
                }, "wrap")

                add(repeatNumberSenderLabel.apply {
                    font = Font(SANS_SERIF, PLAIN, 10)
                    text = "Quantas vezes deseja imprimir o remetente:"
                })

                add(repeatNumberSender.apply {
                    document = DocumentoPersonalizado(4, 1)
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
                    isEnabled = false
                })
            }, "wrap, height 159")

            add(JPanel().apply {
                layout = MigLayout()
                border = BorderFactory.createEtchedBorder()

                add(JScrollPane().apply {
                    setViewportView(recipientPrintTable.apply {
                        model = recipientPrintTableModel
                    })
                }, "span, grow, push")
            }, "span, grow, push")
        }, "Center")

        ButtonGroup().apply {
            add(sheetWith14Labels)
            add(sheetWith10Labels)
        }

        ButtonGroup().apply {
            add(selectSender)
            add(selectRecipient)
            add(selectRecipientAndSender)
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

    private fun jbtVisualizarActionPerformed() {
        val impressao = Impressao()
        if ((selectRecipient.isSelected || selectRecipientAndSender.isSelected) && recipientPrintTableModel.rowCount < 1) {
            JOptionPane.showMessageDialog(
                this,
                "Não existe nenhum destinatário selecionado!",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE)
            repeatNumberSender.requestFocus()
            return
        }
        try {
            if (fromTheTag.text.isEmpty()) {
                fromTheTag.text = "1"
            }
            if (repeatNumberSender.text.isEmpty()) {
                repeatNumberSender.text = "1"
            }
            if (sheetWith14Labels.isSelected && fromTheTag.text.toInt() > 14) {
                JOptionPane.showMessageDialog(
                    this,
                    "Posição inicial para impressão de etiquetas deve ser um número de 1 a 14.",
                    "Endereçador",
                    JOptionPane.WARNING_MESSAGE)
                return
            }
            if (sheetWith10Labels.isSelected && fromTheTag.text.toInt() > 10) {
                JOptionPane.showMessageDialog(
                    this,
                    "Posição inicial para impressão de etiquetas deve ser um número de 1 a 10.",
                    "Endereçador",
                    JOptionPane.WARNING_MESSAGE)
                return
            }
            if (selectRecipient.isSelected) {
                impressao.impressaoCarta(
                    report.file,
                    null,
                    recipientPrintTableModel.getAll(),
                    0,
                    fromTheTag.text.toInt(),
                    false,
                    withPhoneRecipient.isSelected,
                    imprimirTratamento,
                    (Objects.requireNonNull(
                        fontSizeOptions.selectedItem
                    ) as String).substring(0, 1)
                )
            } else if (selectSender.isSelected) {
                if (senderOptions.itemCount == 0) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Por favor cadastre um remetente antes de fazer a impressão.",
                        "Endereçador ECT",
                        JOptionPane.WARNING_MESSAGE)
                    return
                }
                impressao.impressaoCarta(
                    report.file,
                    senderOptions.selectedItem as RemetenteBean,
                    null,
                    repeatNumberSender.text.toInt(),
                    fromTheTag.text.toInt(),
                    withPhoneSender.isSelected,
                    false,
                    imprimirTratamento,
                    (Objects.requireNonNull(
                        fontSizeOptions.selectedItem
                    ) as String).substring(0, 1)
                )
            } else {
                if (senderOptions.itemCount == 0) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Por favor cadastre um remetente antes de fazer a impressão.",
                        "Endereçador ECT",
                        JOptionPane.WARNING_MESSAGE)
                    return
                }
                impressao.impressaoCarta(
                    report.file,
                    senderOptions.selectedItem as RemetenteBean,
                    recipientPrintTableModel.getAll(),
                    repeatNumberSender.text.toInt(),
                    fromTheTag.text.toInt(),
                    withPhoneSender.isSelected,
                    withPhoneRecipient.isSelected,
                    imprimirTratamento,
                    (Objects.requireNonNull(
                        fontSizeOptions.selectedItem
                    ) as String).substring(0, 1)
                )
            }
        } catch (ex: EnderecadorExcecao) {
            logger.error(ex.message, ex as Throwable)
        }
    }

    private fun jcmbFolhaActionPerformed() {
        if (sheetSizeOptions.selectedItem == SheetSize.A4) {
            if (sheetWith14Labels.isSelected) {
                report = Report.ENVELOPE_A4_14
            } else if (sheetWith10Labels.isSelected) {
                report = Report.ENVELOPE_A4_10
            }
        } else if (sheetWith14Labels.isSelected) {
            report = Report.ENVELOPE_LETTER_14
        } else if (sheetWith10Labels.isSelected) {
            report = Report.ENVELOPE_LETTER_10
        }
    }

    private fun jrbtRemetenteDestinatarioActionPerformed() {
        searchRecipientView.isEnabled = true
        removeRecipient.isEnabled = true
        searchGroupView.isEnabled = true
        removeAllRecipient.isEnabled = true
        repeatNumberSender.isEnabled = true
        repeatNumberSenderLabel.isEnabled = true
        senderOptions.isEnabled = true
        senderOptions.isEnabled = true
        senderLabel.isEnabled = true
        withPhoneRecipient.isEnabled = true
        withPhoneSender.isEnabled = true
        viewAR.isEnabled = true
    }

    private fun jrbtDestinatarioActionPerformed() {
        searchRecipientView.isEnabled = true
        removeRecipient.isEnabled = true
        searchGroupView.isEnabled = true
        removeAllRecipient.isEnabled = true
        repeatNumberSender.isEnabled = false
        repeatNumberSender.text = ""
        repeatNumberSenderLabel.isEnabled = false
        senderOptions.isEnabled = false
        senderLabel.isEnabled = false
        withPhoneRecipient.isEnabled = true
        withPhoneSender.isEnabled = false
        viewAR.isEnabled = false
    }

    private fun jrbtRemetenteActionPerformed() {
        searchRecipientView.isEnabled = false
        removeRecipient.isEnabled = false
        searchGroupView.isEnabled = false
        removeAllRecipient.isEnabled = false
        repeatNumberSender.isEnabled = true
        repeatNumberSenderLabel.isEnabled = true
        senderOptions.isEnabled = true
        senderLabel.isEnabled = true
        recipientPrintTableModel.setAll(arrayListOf())
        withPhoneRecipient.isEnabled = false
        withPhoneSender.isEnabled = true
        viewAR.isEnabled = false
    }

    private fun jrbt10RotulosActionPerformed() {
        printFigure.icon = ImageIcon(javaClass.getResource("/imagens/tipo_10etq.gif"))
        report = if (sheetSizeOptions.selectedItem == SheetSize.A4) {
            Report.ENVELOPE_A4_10
        } else {
            Report.ENVELOPE_LETTER_10
        }
        imprimirTratamento = true
    }

    private fun jrbt14RotulosActionPerformed() {
        printFigure.icon = ImageIcon(javaClass.getResource("/imagens/tipo_14etq.gif"))
        report = if (sheetSizeOptions.selectedItem == SheetSize.A4) {
            Report.ENVELOPE_A4_14
        } else {
            Report.ENVELOPE_LETTER_14
        }
        imprimirTratamento = false
    }

    private fun jbtRemoverTodosActionPerformed() {
        recipientPrintTableModel.setAll(arrayListOf())
    }

    private fun jbtRemoverDestinatarioActionPerformed() {
        if (recipientPrintTable.selectedRow == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Não existe nenhum destinatário selecionado.",
                "Endereçador ECT",
                JOptionPane.INFORMATION_MESSAGE
            )
            return
        }
        recipientPrintTable.selectedRows.forEach { row ->
            recipientPrintTableModel.removeRowAt(row)
        }
    }

    private fun jbtSelecionarGrupoActionPerformed() {
        val telaPesquisarGrupo = GroupSearchView()
        telaPesquisarGrupo.isVisible = true
        //recipientPrintTableModel.setAll(vecDestinatarioImpressao)
    }

    private fun jbtSelecionarDestinatarioActionPerformed() {
        val telaPesquisaDestinatario = RecipientSearchView()
        telaPesquisaDestinatario.isVisible = true
        //ecipientPrintTableModel.setAll(vecDestinatarioImpressao)
    }

    private fun jcmbTamanhoFonteActionPerformed() {}

    companion object {
        private val logger = Logger.getLogger(PrintEnvelopeView::class.java)
    }
}
