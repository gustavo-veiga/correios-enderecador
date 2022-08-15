package br.com.correios.enderecador.telas

import br.com.correios.enderecador.bean.DestinatarioBean
import javax.swing.JDialog
import br.com.correios.enderecador.bean.GrupoBean
import javax.swing.JScrollPane
import javax.swing.JList
import javax.swing.JTextField
import br.com.correios.enderecador.dao.GrupoDao
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
import br.com.correios.enderecador.util.Geral
import javax.swing.ListModel
import br.com.correios.enderecador.dao.DestinatarioDao
import org.apache.log4j.Logger
import java.awt.Dimension
import java.awt.Font
import java.awt.Frame
import java.util.*

class TelaPesquisarGrupo(parent: Frame?, modal: Boolean, vecDestinatario: Vector<DestinatarioBean?>) :
    JDialog(parent, modal) {
    private var vecDestinatarioRetorno: Vector<DestinatarioBean?> = Vector()
    private val vecDestinatarioGrupo: Vector<GrupoBean?> = Vector()
    private var destinatarioBean: DestinatarioBean? = null
    private var ultimaConsulta: String
    private var jScrollPane1: JScrollPane? = null
    private var jlstGrupo: JList<GrupoBean>? = null
    private var jtxtGrupo: JTextField? = null

    init {
        ultimaConsulta = ""
        initComponents()
        vecDestinatarioRetorno = vecDestinatario
        carregaListaGrupo()
    }

    private fun carregaListaGrupo() {
        try {
            val arrayGrupo = GrupoDao.instance!!.recuperaGrupo("")
            vecDestinatarioGrupo.addAll(arrayGrupo)
            jlstGrupo!!.setListData(vecDestinatarioGrupo)
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
        jtxtGrupo = JTextField()
        jScrollPane1 = JScrollPane()
        jlstGrupo = JList<GrupoBean>()
        defaultCloseOperation = 2
        title = "Selecionar grupo"
        isResizable = false
        jToolBar1.border = BorderFactory.createEtchedBorder()
        jbtConfirmar.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jbtConfirmar.icon = ImageIcon(javaClass.getResource("/imagens/OK.gif"))
        jbtConfirmar.text = "Confirmar"
        jbtConfirmar.horizontalTextPosition = 0
        jbtConfirmar.maximumSize = Dimension(90, 60)
        jbtConfirmar.verticalTextPosition = 3
        jbtConfirmar.addActionListener { jbtConfirmarActionPerformed() }
        jToolBar1.add(jbtConfirmar)
        jbtPesquisar.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jbtPesquisar.icon = ImageIcon(javaClass.getResource("/imagens/binoculo.gif"))
        jbtPesquisar.text = "Pesquisar"
        jbtPesquisar.horizontalTextPosition = 0
        jbtPesquisar.maximumSize = Dimension(90, 60)
        jbtPesquisar.verticalTextPosition = 3
        jbtPesquisar.addActionListener { jbtPesquisarActionPerformed() }
        jToolBar1.add(jbtPesquisar)
        jbtSair.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jbtSair.icon = ImageIcon(javaClass.getResource("/imagens/sair.gif"))
        jbtSair.text = "Sair"
        jbtSair.horizontalTextPosition = 0
        jbtSair.maximumSize = Dimension(90, 60)
        jbtSair.preferredSize = Dimension(51, 27)
        jbtSair.verticalTextPosition = 3
        jbtSair.addActionListener { jbtSairActionPerformed() }
        jToolBar1.add(jbtSair)
        contentPane.add(jToolBar1, "North")
        jPanel1.border = BorderFactory.createEtchedBorder()
        jPanel1.layout = AbsoluteLayout()
        jLabel1.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jLabel1.text = "Grupo:"
        jPanel1.add(jLabel1, AbsoluteConstraints(20, 23, -1, -1))
        jPanel1.add(jtxtGrupo, AbsoluteConstraints(60, 20, 320, -1))
        jScrollPane1!!.setViewportView(jlstGrupo)
        jPanel1.add(jScrollPane1, AbsoluteConstraints(4, 52, 582, 210))
        contentPane.add(jPanel1, "Center")
        size = Dimension(598, 349)
        setLocationRelativeTo(null)
    }

    private fun jbtSairActionPerformed() {
        isVisible = false
    }

    private fun jbtPesquisarActionPerformed() {
        val nomeGrupo = jtxtGrupo!!.text.trim()
        var index = -1
        if (ultimaConsulta.equals(nomeGrupo, ignoreCase = true)) {
            index = jlstGrupo!!.selectedIndex
        } else {
            ultimaConsulta = nomeGrupo
        }
        for (i in index + 1 until vecDestinatarioGrupo.size) {
            val grupoBean = vecDestinatarioGrupo[i]
            val nomeGrupoTabela = grupoBean!!.descricaoGrupo
            if (nomeGrupoTabela!!.uppercase(Locale.getDefault()).contains(nomeGrupo.uppercase(Locale.getDefault()))) {
                index = i
                break
            }
        }
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Grupo não encontrado!", "Endereçador", JOptionPane.INFORMATION_MESSAGE)
        } else {
            jlstGrupo!!.selectedIndex = index
            val tamanhoJscrool = jScrollPane1!!.verticalScrollBar.maximum
            val value = index * tamanhoJscrool / vecDestinatarioGrupo.size
            jScrollPane1!!.verticalScrollBar.value = value
        }
    }

    private fun jbtConfirmarActionPerformed() {
        try {
            if (jlstGrupo!!.selectedIndex != -1) {
                val modelGrupo: ListModel<GrupoBean> = jlstGrupo!!.getModel()
                val arrayIndex = jlstGrupo!!.selectedIndices
                for (index in arrayIndex) {
                    val grupoBean = modelGrupo.getElementAt(index)
                    val arrayDestinatarioGrupo = DestinatarioDao.instance!!.recuperaDestinatarioPorGrupo(
                        grupoBean.numeroGrupo!!
                    )
                    for (bean in arrayDestinatarioGrupo) {
                        destinatarioBean = bean
                        if (!Geral.verificaExistencia(
                                destinatarioBean,
                                vecDestinatarioRetorno
                            )
                        ) vecDestinatarioRetorno.add(
                            destinatarioBean
                        )
                    }
                }
                Geral.ordenarVetor(vecDestinatarioRetorno, destinatarioBean)
                isVisible = false
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Não existe nenhum grupo selecionado ",
                    "Endereçador ECT",
                    JOptionPane.INFORMATION_MESSAGE
                )
            }
        } catch (ex: DaoException) {
            logger.error(ex.message, ex)
        }
    }

    companion object {
        private val logger = Logger.getLogger(TelaPesquisarGrupo::class.java)
    }
}