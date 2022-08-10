package br.com.correios.enderecador.telas

import br.com.correios.enderecador.bean.DestinatarioBean
import javax.swing.JDialog
import javax.swing.JScrollPane
import javax.swing.JList
import javax.swing.JTextField
import br.com.correios.enderecador.dao.DestinatarioDao
import br.com.correios.enderecador.dao.DaoException
import javax.swing.JOptionPane
import javax.swing.JToolBar
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JLabel
import javax.swing.BorderFactory
import javax.swing.ImageIcon
import java.awt.event.ActionEvent
import org.netbeans.lib.awtextra.AbsoluteLayout
import org.netbeans.lib.awtextra.AbsoluteConstraints
import javax.swing.ListModel
import br.com.correios.enderecador.util.Geral
import org.apache.log4j.Logger
import java.awt.Dimension
import java.awt.Font
import java.awt.Frame
import java.awt.Toolkit
import java.lang.Exception
import java.util.*

class TelaPesquisarDestinatario(parent: Frame?, modal: Boolean, vecDestinatario: Vector<DestinatarioBean?>) :
    JDialog(parent, modal) {
    private val vecDestinatario: Vector<DestinatarioBean>
    private var vecDestinatarioRetorno: Vector<DestinatarioBean?>
    private var destinatarioBean: DestinatarioBean? = null
    private var ultimaConsulta: String
    private var jScrollPane1: JScrollPane? = null
    private var jlstDestinatario: JList<DestinatarioBean>? = null
    private var jtxtDestinatario: JTextField? = null

    init {
        vecDestinatarioRetorno = Vector()
        this.vecDestinatario = Vector()
        ultimaConsulta = ""
        initComponents()
        vecDestinatarioRetorno = vecDestinatario
        carregaListaDestinatario()
    }

    private fun carregaListaDestinatario() {
        try {
            val arrayDestinatario = DestinatarioDao.instance!!.recuperaDestinatario("")
            vecDestinatario.addAll(arrayDestinatario)
            jlstDestinatario!!.setListData(vecDestinatario)
        } catch (e: DaoException) {
            logger.error(e.message, e)
            JOptionPane.showMessageDialog(
                this,
                "Não foi possivel carregar relação de destinatários",
                "Endereçador ",
                JOptionPane.WARNING_MESSAGE
            )
        }
    }

    private fun initComponents() {
        val jToolBar1 = JToolBar()
        val jbtConfirmar = JButton()
        val jbtPesquisar = JButton()
        val jbtSair = JButton()
        val jPanel1 = JPanel()
        val jLabel1 = JLabel()
        jtxtDestinatario = JTextField()
        jScrollPane1 = JScrollPane()
        jlstDestinatario = JList<DestinatarioBean>()
        defaultCloseOperation = 2
        title = "Selecionar destinatário"
        isResizable = false
        jToolBar1.border = BorderFactory.createEtchedBorder()
        jbtConfirmar.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jbtConfirmar.icon = ImageIcon(javaClass.getResource("/imagens/OK.gif"))
        jbtConfirmar.text = "Confirmar"
        jbtConfirmar.horizontalTextPosition = 0
        jbtConfirmar.maximumSize = Dimension(90, 60)
        jbtConfirmar.verticalTextPosition = 3
        jbtConfirmar.addActionListener { evt: ActionEvent -> jbtConfirmarActionPerformed(evt) }
        jToolBar1.add(jbtConfirmar)
        jbtPesquisar.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jbtPesquisar.icon = ImageIcon(javaClass.getResource("/imagens/binoculo.gif"))
        jbtPesquisar.text = "Pesquisar"
        jbtPesquisar.horizontalTextPosition = 0
        jbtPesquisar.maximumSize = Dimension(90, 60)
        jbtPesquisar.verticalTextPosition = 3
        jbtPesquisar.addActionListener { evt: ActionEvent -> jbtPesquisarActionPerformed(evt) }
        jToolBar1.add(jbtPesquisar)
        jbtSair.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jbtSair.icon = ImageIcon(javaClass.getResource("/imagens/sair.gif"))
        jbtSair.text = "Sair"
        jbtSair.horizontalTextPosition = 0
        jbtSair.maximumSize = Dimension(90, 60)
        jbtSair.verticalTextPosition = 3
        jbtSair.addActionListener { evt: ActionEvent -> jbtSairActionPerformed(evt) }
        jToolBar1.add(jbtSair)
        contentPane.add(jToolBar1, "North")
        jPanel1.layout = AbsoluteLayout()
        jPanel1.border = BorderFactory.createEtchedBorder()
        jLabel1.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jLabel1.text = "Destinatário:"
        jPanel1.add(jLabel1, AbsoluteConstraints(20, 23, -1, -1))
        jPanel1.add(jtxtDestinatario, AbsoluteConstraints(90, 20, 350, -1))
        jScrollPane1!!.setViewportView(jlstDestinatario)
        jPanel1.add(jScrollPane1, AbsoluteConstraints(4, 52, 582, 210))
        contentPane.add(jPanel1, "Center")
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        setBounds((screenSize.width - 598) / 2, (screenSize.height - 349) / 2, 598, 349)
    }

    private fun jbtSairActionPerformed(evt: ActionEvent) {
        isVisible = false
    }

    private fun jbtPesquisarActionPerformed(evt: ActionEvent) {
        try {
            val nomeDestinatario = jtxtDestinatario!!.text.trim { it <= ' ' }
            var index = -1
            if (ultimaConsulta.equals(nomeDestinatario, ignoreCase = true)) {
                index = jlstDestinatario!!.selectedIndex
            } else {
                ultimaConsulta = nomeDestinatario
            }
            for (i in index + 1 until vecDestinatario.size) {
                destinatarioBean = vecDestinatario[i]
                val nomeDestinatarioTabela = destinatarioBean!!.nome
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
                jlstDestinatario!!.selectedIndex = index
                val tamanhoJscrool = jScrollPane1!!.verticalScrollBar.maximum
                var value = 0
                value = index * tamanhoJscrool / vecDestinatario.size
                jScrollPane1!!.verticalScrollBar.value = value
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun jbtConfirmarActionPerformed(evt: ActionEvent) {
        if (jlstDestinatario!!.isSelectionEmpty) {
            JOptionPane.showMessageDialog(this, "Não existe nenhum destinatário selecionado", "Endereçador ECT", 1)
            return
        }
        val modelDestinatario: ListModel<DestinatarioBean> = jlstDestinatario!!.getModel()
        val arrayIndex = jlstDestinatario!!.selectedIndices
        destinatarioBean = DestinatarioBean.instance
        for (index in arrayIndex) {
            destinatarioBean = modelDestinatario.getElementAt(index)
            if (!Geral.verificaExistencia(destinatarioBean, vecDestinatarioRetorno)) {
                vecDestinatarioRetorno.add(destinatarioBean)
            }
        }
        Geral.ordenarVetor(vecDestinatarioRetorno, destinatarioBean)
        isVisible = false
    }

    companion object {
        private val logger = Logger.getLogger(TelaPesquisarDestinatario::class.java)
    }
}
