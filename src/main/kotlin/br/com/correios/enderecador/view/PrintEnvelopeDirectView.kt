package br.com.correios.enderecador.view

import br.com.correios.enderecador.util.TextoCellRenderer
import br.com.correios.enderecador.bean.RemetenteBean
import br.com.correios.enderecador.dao.RemetenteDao
import br.com.correios.enderecador.exception.DaoException
import br.com.correios.enderecador.util.Impressao
import br.com.correios.enderecador.exception.EnderecadorExcecao
import br.com.correios.enderecador.tablemodel.DestinatarioImpressaoTableModel
import br.com.correios.enderecador.util.FontSize
import br.com.correios.enderecador.util.Report
import net.miginfocom.swing.MigLayout
import org.apache.log4j.Logger
import org.koin.core.annotation.Singleton
import java.awt.*
import java.awt.Font.*
import javax.swing.*

@Singleton
class PrintEnvelopeDirectView(
    private val senderDao: RemetenteDao,
) : JFrame() {
    private val recipientPrintTableModel = DestinatarioImpressaoTableModel()
    private val recipientPrintTable = JTable()

    private val withPrintSender = JCheckBox()
    private val withPhoneRecipient = JCheckBox()
    private val senderOptions = JComboBox<RemetenteBean>()
    private val fontSizeOptions = JComboBox<FontSize>()
    private val envelopeFigure = JLabel()
    private val envelopeC5 = JRadioButton()
    private val envelopeC6 = JRadioButton()
    private val envelopeC6C5 = JRadioButton()

    init {
        initComponents()
        configuracoesAdicionais()
        carregaRemetente()
        setLocationRelativeTo(null)
    }

    private fun configuracoesAdicionais() {
        var renderer = TextoCellRenderer(2)
        recipientPrintTable.columnModel.getColumn(0).apply {
            cellRenderer = renderer
            preferredWidth = 300
        }
        recipientPrintTable.columnModel.getColumn(1).apply {
            preferredWidth = 300
            cellRenderer = renderer
        }
        recipientPrintTable.columnModel.getColumn(2).apply {
            preferredWidth = 120
            cellRenderer = renderer
        }

        renderer = TextoCellRenderer(0)
        recipientPrintTable.columnModel.getColumn(3).apply {
            cellRenderer = renderer
            preferredWidth = 60
            width = 1
        }
        recipientPrintTable.columnModel.getColumn(4).apply {
            cellEditor = DefaultCellEditor(JComboBox(arrayOf("Sim", "Não")))
            cellRenderer = renderer
        }
    }

    private fun carregaRemetente() {
        try {
            val arrayRemetente = senderDao.recuperaRemetente()
            arrayRemetente.forEach { remetenteBean ->
                senderOptions.addItem(remetenteBean)
            }
        } catch (e: DaoException) {
            logger.error(e.message, e)
            JOptionPane.showMessageDialog(
                this,
                "Não foi possivel carregar relação de remetentes",
                "Endereçador ECT",
                JOptionPane.WARNING_MESSAGE)
        }
    }

    private fun imprimirEnvelope(report: Report) {
        try {
            cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
            val impressao = Impressao()
            if (withPrintSender.isSelected) {
                if (senderOptions.itemCount == 0) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Por favor cadastre um remetente antes de fazer a impressão.",
                        "Endereçador ECT",
                        JOptionPane.WARNING_MESSAGE)
                    return
                }
                impressao.imprimirEnvelope(
                    report.file,
                    senderOptions.selectedItem as RemetenteBean,
                    recipientPrintTableModel.getAll(),
                    withPhoneRecipient.isSelected,
                    fontSizeOptions.selectedItem as FontSize)
            } else {
                impressao.imprimirEnvelope(
                    report.file,
                    null,
                    recipientPrintTableModel.getAll(),
                    withPhoneRecipient.isSelected,
                    fontSizeOptions.selectedItem as FontSize)
            }
        } catch (e: EnderecadorExcecao) {
            logger.error(e.message, e)
            JOptionPane.showMessageDialog(
                this,
                "Não foi possivel imprimir.",
                "Endereçador ECT",
                JOptionPane.WARNING_MESSAGE)
        } finally {
            cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
        }
    }

    private fun initComponents() {
        title = "Impressão direta no envelope"
        size = Dimension(710, 600)
        isResizable = true

        contentPane.add(JToolBar().apply {
            add(JButton().apply {
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@PrintEnvelopeDirectView.javaClass.getResource("/imagens/addusuario.gif"))
                text = "Selecionar destinatário"
                horizontalTextPosition = 0
                maximumSize = Dimension(110, 60)
                verticalTextPosition = 3
                addActionListener { jbtSelecionarDestinatarioActionPerformed() }
            })
            add(JButton().apply {
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@PrintEnvelopeDirectView.javaClass.getResource("/imagens/addusuarios.gif"))
                text = "Selecionar grupo"
                horizontalTextPosition = 0
                maximumSize = Dimension(90, 60)
                verticalTextPosition = 3
                addActionListener { jbtSelecionarGrupoActionPerformed() }
            })
            add(JButton().apply {
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@PrintEnvelopeDirectView.javaClass.getResource("/imagens/remover.gif"))
                text = "Remover destinatário"
                horizontalTextPosition = 0
                maximumSize = Dimension(110, 60)
                verticalTextPosition = 3
                addActionListener { jbtRemoverDestinatarioActionPerformed() }
            })
            add(JButton().apply {
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@PrintEnvelopeDirectView.javaClass.getResource("/imagens/removerTodos.gif"))
                text = "Remover todos"
                horizontalTextPosition = 0
                maximumSize = Dimension(90, 60)
                verticalTextPosition = 3
                addActionListener { jbtRemoverTodosActionPerformed() }
            })
            add(JButton().apply {
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@PrintEnvelopeDirectView.javaClass.getResource("/imagens/print.gif"))
                text = "Visualizar AR"
                horizontalTextPosition = 0
                maximumSize = Dimension(90, 60)
                verticalTextPosition = 3
                addActionListener { jbtnVisializarARActionPerformed() }
            })
            add(JButton().apply {
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@PrintEnvelopeDirectView.javaClass.getResource("/imagens/IMPRIMIR.gif"))
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
                border = BorderFactory.createTitledBorder(
                    null,
                    "Escolha o tipo do envelope:",
                    0,
                    0,
                    Font(SANS_SERIF, PLAIN, 10))

                add(envelopeFigure.apply {
                    icon = ImageIcon(this@PrintEnvelopeDirectView.javaClass.getResource("/imagens/envelopeC5.gif"))
                    minimumSize = Dimension(80, 65)
                }, "span 1 2")

                add(envelopeC5.apply {
                    font = Font(SANS_SERIF, PLAIN, 10)
                    text = "Formato C5 (162x229mm)"
                    addActionListener { jrbEnvelopeC5ActionPerformed() }
                })

                add(envelopeC6C5.apply {
                    font = Font(SANS_SERIF, PLAIN, 10)
                    text = "Formato C6/C5 (114x229mm)"
                    addActionListener { jrbEnvelopeC6C5ActionPerformed() }
                }, "wrap")

                add(envelopeC6.apply {
                    font = Font(SANS_SERIF, PLAIN, 10)
                    text = "Formato C6 (114x162mm)"
                    addActionListener { jrbEnvelopeC6ActionPerformed() }
                })

                add(withPhoneRecipient.apply {
                    font = Font(SANS_SERIF, PLAIN, 10)
                    text = "Incluir telefone"
                })

                add(withPhoneRecipient)
            }, "height 114")

            add(JPanel().apply {
                layout = MigLayout()
                border = BorderFactory.createTitledBorder(" ")

                add(withPrintSender.apply {
                    withPrintSender.font = Font(SANS_SERIF, PLAIN, 10)
                    withPrintSender.text = "Imprimir remetente"
                }, "wrap")

                add(JLabel().apply {
                    font = Font(SANS_SERIF, PLAIN, 10)
                    text = "Tamanho da Fonte:"
                })

                add(fontSizeOptions.apply {
                    model = DefaultComboBoxModel(FontSize.values())
                    addActionListener { jcmbTamanhoFonteActionPerformed() }
                }, "wrap")

                add(JLabel().apply {
                    font = Font(SANS_SERIF, PLAIN, 10)
                    text = "Selecione o remetente:"
                })

                add(senderOptions)
            }, "wrap")

            add(JPanel().apply {
                layout = MigLayout()
                border = BorderFactory.createEtchedBorder()

                add(JScrollPane().apply {
                    setViewportView(recipientPrintTable)
                }, "span, grow, push")
            }, "span, grow, push")
        }, "Center")

        recipientPrintTable.model = recipientPrintTableModel

        ButtonGroup().apply {
            add(envelopeC5)
            add(envelopeC6C5)
            add(envelopeC6)
        }
    }

    private fun jbtnVisializarARActionPerformed() {
        val impressao = Impressao()
        if (senderOptions.itemCount == 0) {
            JOptionPane.showMessageDialog(
                this,
                "Por favor cadastre um remetente antes de fazer a impressão.",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE)
            return
        }
        if (recipientPrintTableModel.rowCount < 1) {
            JOptionPane.showMessageDialog(
                this,
                "Não existe nenhum destinatário selecionado!",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE)
            return
        }
        try {
            impressao.imprimirAR(
                Report.AR.file,
                senderOptions.selectedItem as RemetenteBean,
                recipientPrintTableModel.getAll())
        } catch (ex: EnderecadorExcecao) {
            logger.error(ex.message, ex)
            JOptionPane.showMessageDialog(
                this,
                "Não foi possível imprimir o AR",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE)
        }
    }

    private fun jbtVisualizarActionPerformed() {
        if (recipientPrintTableModel.rowCount <= 0) {
            JOptionPane.showMessageDialog(
                this,
                "Não existe nenhum destinatário selecionado!",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE)
            return
        }
        if (envelopeC5.isSelected) {
            imprimirEnvelope(Report.ENVELOPE_C5)
        } else if (envelopeC6.isSelected) {
            imprimirEnvelope(Report.ENVELOPE_C6)
        } else {
            imprimirEnvelope(Report.ENVELOPE_C6_C5)
        }
    }

    private fun jrbEnvelopeC6C5ActionPerformed() {
        envelopeFigure.icon = ImageIcon(javaClass.getResource("/imagens/envelopeC5C6.gif"))
    }

    private fun jrbEnvelopeC5ActionPerformed() {
        envelopeFigure.icon = ImageIcon(javaClass.getResource("/imagens/envelopeC5.gif"))
    }

    private fun jrbEnvelopeC6ActionPerformed() {
        envelopeFigure.icon = ImageIcon(javaClass.getResource("/imagens/envelopeC6.gif"))
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
                JOptionPane.INFORMATION_MESSAGE)
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
        //recipientPrintTableModel.setAll(vecDestinatarioImpressao)
    }

    private fun jcmbTamanhoFonteActionPerformed() {}

    companion object {
        var logger = Logger.getLogger(SenderEditView::class.java)
    }
}