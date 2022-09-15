package br.com.correios.enderecador.view

import br.com.correios.enderecador.bean.ConteudoDeclaradoBean
import br.com.correios.enderecador.bean.DestinatarioBean
import br.com.correios.enderecador.bean.RemetenteBean
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.awt.Color
import br.com.correios.enderecador.util.Impressao
import br.com.correios.enderecador.exception.EnderecadorExcecao
import br.com.correios.enderecador.tablemodel.DeclaracaoConteudoTableModel
import br.com.correios.enderecador.util.Report
import net.miginfocom.swing.MigLayout
import org.apache.log4j.Logger
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import java.awt.Dimension
import java.awt.Font
import java.awt.Font.PLAIN
import java.awt.Font.SANS_SERIF
import javax.swing.*

class ContentDeclarationView(
    private val vecDestinatario: List<DestinatarioBean>,
    private val remetente: RemetenteBean
) : KoinComponent, JDialog() {
    private val print: Impressao = get()

    private val contentDeclarationTableModel = DeclaracaoConteudoTableModel()
    private val contentDeclarationTable = JTable()
    private val totalWeight = JTextField()

    init {
        initComponents()
    }

    private fun initComponents() {
        title = "Declaração de Conteúdo"
        size = Dimension(689, 328)
        isResizable = false
        defaultCloseOperation = DO_NOTHING_ON_CLOSE
        setLocationRelativeTo(null)
        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(evt: WindowEvent) {
                fecharJanela()
            }
        })

        contentPane.add(JToolBar().apply {
            isRollover = true

            add(JButton().apply {
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@ContentDeclarationView.javaClass.getResource("/imagens/print.gif"))
                text = "Imprimir"
                horizontalTextPosition = 0
                maximumSize = Dimension(90, 60)
                minimumSize = Dimension(47, 55)
                verticalTextPosition = 3
                addActionListener { jButton1ActionPerformed() }
            })
            add(JButton().apply {
                font = Font(SANS_SERIF, 0, 9)
                icon = ImageIcon(this@ContentDeclarationView.javaClass.getResource("/imagens/remover.gif"))
                text = "Excluir item"
                horizontalTextPosition = 0
                maximumSize = Dimension(90, 60)
                minimumSize = Dimension(47, 55)
                verticalTextPosition = 3
                addActionListener { jButton2ActionPerformed() }
            })
        }, "North")

        contentPane.add(JPanel().apply {
            layout = MigLayout()

            add(JLabel().apply {
                foreground = Color(51, 51, 255)
                text = "* Após o preenchimento de cada campo, pressionar a tecla <tab>"
            }, "wrap")

            add(JScrollPane().apply {
                setViewportView(contentDeclarationTable.apply {
                    model = contentDeclarationTableModel
                    setSelectionMode(0)
                })
            }, "span, grow, push")

            add(JLabel().apply {
                text = "Peso total (kg):"
            }, "split 2")

            add(totalWeight.apply {
                horizontalAlignment = 4
                text = "0"
            })
        }, "Center")
    }

    private fun jButton1ActionPerformed() {
        val items = contentDeclarationTableModel.getAll()
        try {
            print.imprimirDeclaracao(Report.DECLARATION.file, items, remetente, vecDestinatario, totalWeight.text)
        } catch (ex: EnderecadorExcecao) {
            LOGGER.error(ex.message, ex)
            JOptionPane.showMessageDialog(
                this,
                "Não foi possível imprimir a declaração de conteúdo",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE)
        }
        isVisible = false
    }

    private fun jButton2ActionPerformed() {
        if (contentDeclarationTable.selectedRow != -1) {
            contentDeclarationTableModel.removeRowAt(contentDeclarationTable.selectedRow)
            contentDeclarationTableModel.insertRow(ConteudoDeclaradoBean())
        }
    }

    private fun fecharJanela() {
        val result = JOptionPane.showConfirmDialog(
            null,
            "A declaração de conteúdo não é armazenada. Deseja sair?",
            "Fechar Declaração de Conteúdo",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE)
        if (result == JOptionPane.YES_NO_OPTION) {
            defaultCloseOperation = DISPOSE_ON_CLOSE
        }
    }

    companion object {
        private val LOGGER = Logger.getLogger(ContentDeclarationView::class.java)
    }
}
