package br.com.correios.enderecador.telas

import br.com.correios.enderecador.bean.DestinatarioBean
import javax.swing.JDialog
import javax.swing.JScrollPane
import javax.swing.JList
import javax.swing.JTextField
import br.com.correios.enderecador.dao.DestinatarioDao
import br.com.correios.enderecador.exception.DaoException
import br.com.correios.enderecador.util.getAllItems
import br.com.correios.enderecador.util.setSelectedItem
import net.miginfocom.swing.MigLayout
import javax.swing.JOptionPane
import javax.swing.JToolBar
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JLabel
import javax.swing.BorderFactory
import javax.swing.ImageIcon
import org.apache.log4j.Logger
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import java.awt.Dimension
import java.awt.Font
import java.awt.Font.PLAIN
import java.awt.Font.SANS_SERIF
import java.awt.Toolkit

class TelaPesquisarDestinatario : KoinComponent, JDialog() {
    private val recipientDao: DestinatarioDao = get()

    private val recipientList = JList<DestinatarioBean>()
    private val searchText = JTextField()

    private var lastSearch = ""

    init {
        initComponents()
        carregaListaDestinatario()
    }

    private fun carregaListaDestinatario() {
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
        title = "Selecionar destinatário"
        isResizable = false
        defaultCloseOperation = DISPOSE_ON_CLOSE

        contentPane.add(JToolBar().apply {
            add(JButton().apply {
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@TelaPesquisarDestinatario.javaClass.getResource("/imagens/OK.gif"))
                text = "Confirmar"
                maximumSize = Dimension(90, 60)
                verticalTextPosition = 3
                horizontalTextPosition = 0
                addActionListener { jbtConfirmarActionPerformed() }
            })
            add(JButton().apply {
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@TelaPesquisarDestinatario.javaClass.getResource("/imagens/binoculo.gif"))
                text = "Pesquisar"
                maximumSize = Dimension(90, 60)
                verticalTextPosition = 3
                horizontalTextPosition = 0
                addActionListener { jbtPesquisarActionPerformed() }
            })
            add(JButton().apply {jbtPesquisarActionPerformed()
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@TelaPesquisarDestinatario.javaClass.getResource("/imagens/sair.gif"))
                text = "Sair"
                maximumSize = Dimension(90, 60)
                verticalTextPosition = 3
                horizontalTextPosition = 0
                addActionListener { dispose() }
            })
        }, "North")

        contentPane.add(JPanel().apply {
            layout = MigLayout()
            border = BorderFactory.createEtchedBorder()

            add(JLabel().apply {
                font = Font(SANS_SERIF, PLAIN, 10)
                text = "Destinatário:"
            })

            add(searchText.apply {
                addActionListener { jbtPesquisarActionPerformed() }
            }, "span, grow, pushx, width 550")

            add(JScrollPane().apply {
                setViewportView(recipientList)
            }, "span, grow, push")
        }, "Center")

        val screenSize = Toolkit.getDefaultToolkit().screenSize
        setBounds((screenSize.width - 598) / 2, (screenSize.height - 349) / 2, 598, 349)
    }

    private fun jbtPesquisarActionPerformed() {
        val search = searchText.text.trim().also {
            if (it.isBlank()) return
        }

        recipientList.getAllItems()
            .filterIndexed { index, _ ->
                if (lastSearch.equals(search, ignoreCase = true)) {
                    index > recipientList.selectedIndex
                } else {
                    lastSearch = search
                    true
                }
            }
            .find { it.nome.contains(search, ignoreCase = true).or(it.apelido.contains(search, ignoreCase = true)) }
            ?.let {
                recipientList.setSelectedItem(it)
                return
            }

        JOptionPane.showMessageDialog(
            this,
            "Destinatário não encontrado!",
            "Endereçador",
            JOptionPane.INFORMATION_MESSAGE)
    }


    private fun jbtConfirmarActionPerformed() {
        if (recipientList.isSelectionEmpty) {
            JOptionPane.showMessageDialog(
                this,
                "Não existe nenhum destinatário selecionado",
                "Endereçador ECT",
                JOptionPane.INFORMATION_MESSAGE)
            return
        }
    }

    companion object {
        private val logger = Logger.getLogger(TelaPesquisarDestinatario::class.java)
    }
}
