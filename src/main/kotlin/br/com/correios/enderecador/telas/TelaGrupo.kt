package br.com.correios.enderecador.telas

import javax.swing.JFrame
import br.com.correios.enderecador.bean.GrupoBean
import br.com.correios.enderecador.bean.DestinatarioBean
import javax.swing.JScrollPane
import javax.swing.JTable
import javax.swing.JList
import javax.swing.JTextField
import javax.swing.JToolBar
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JLabel
import javax.swing.ImageIcon
import java.awt.event.ActionEvent
import javax.swing.BorderFactory
import javax.swing.event.ListSelectionListener
import javax.swing.event.ListSelectionEvent
import javax.swing.table.DefaultTableModel
import javax.swing.JOptionPane
import br.com.correios.enderecador.dao.GrupoDestinatarioDao
import br.com.correios.enderecador.dao.GrupoDao
import br.com.correios.enderecador.dao.DaoException
import org.apache.log4j.Logger
import org.jdesktop.layout.GroupLayout
import org.koin.core.annotation.Singleton
import java.awt.Dimension
import java.awt.Font
import java.util.*

@Singleton
class TelaGrupo : JFrame() {
    private val vecTelEndGrupo = Vector<GrupoBean>()
    private val vecTelEndDestinatarioGrupo = Vector<DestinatarioBean>()
    private var ultimaConsulta = ""
    private var jScrollPane1: JScrollPane? = null
    private var jTDestinatario: JTable? = null
    private var jlstTelEndGrupo: JList<Any>? = null
    private var jtxtGrupo: JTextField? = null

    init {
        initComponents()
        setLocationRelativeTo(null)
    }

    private fun initComponents() {
        val jToolBar1 = JToolBar()
        val jbtNovo = JButton()
        val jbtEditar = JButton()
        val jbtPesquisar = JButton()
        val jbtExcluir = JButton()
        val jPanel1 = JPanel()
        val jLabel1 = JLabel()
        jtxtGrupo = JTextField()
        val jLabel2 = JLabel()
        jScrollPane1 = JScrollPane()
        jlstTelEndGrupo = JList<Any>()
        val jScrollPane2 = JScrollPane()
        jTDestinatario = JTable()
        val jLabel3 = JLabel()
        isResizable = true
        title = "Cadastro de Grupos"
        jToolBar1.preferredSize = Dimension(100, 59)
        jbtNovo.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jbtNovo.icon = ImageIcon(javaClass.getResource("/imagens/usuarios.gif"))
        jbtNovo.text = "Novo grupo"
        jbtNovo.horizontalTextPosition = 0
        jbtNovo.maximumSize = Dimension(90, 60)
        jbtNovo.verticalTextPosition = 3
        jbtNovo.addActionListener { jbtNovoActionPerformed() }
        jToolBar1.add(jbtNovo)
        jbtEditar.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jbtEditar.icon = ImageIcon(javaClass.getResource("/imagens/editar.gif"))
        jbtEditar.text = "Editar"
        jbtEditar.horizontalTextPosition = 0
        jbtEditar.maximumSize = Dimension(90, 60)
        jbtEditar.verticalTextPosition = 3
        jbtEditar.addActionListener { jbtEditarActionPerformed() }
        jToolBar1.add(jbtEditar)
        jbtPesquisar.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jbtPesquisar.icon = ImageIcon(javaClass.getResource("/imagens/binoculo.gif"))
        jbtPesquisar.text = "Pesquisar"
        jbtPesquisar.horizontalTextPosition = 0
        jbtPesquisar.maximumSize = Dimension(90, 60)
        jbtPesquisar.verticalTextPosition = 3
        jbtPesquisar.addActionListener { jbtPesquisarActionPerformed() }
        jToolBar1.add(jbtPesquisar)
        jbtExcluir.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jbtExcluir.icon = ImageIcon(javaClass.getResource("/imagens/TRASH.gif"))
        jbtExcluir.text = "Excluir"
        jbtExcluir.horizontalTextPosition = 0
        jbtExcluir.maximumSize = Dimension(90, 60)
        jbtExcluir.verticalTextPosition = 3
        jbtExcluir.addActionListener { jbtExcluirActionPerformed() }
        jToolBar1.add(jbtExcluir)
        jPanel1.border = BorderFactory.createEtchedBorder()
        jLabel1.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jLabel1.text = "Procurar por:"
        jLabel2.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jLabel2.text = "Grupos:"
        jlstTelEndGrupo!!.addListSelectionListener { jlstTelEndGrupoValueChanged() }
        jScrollPane1!!.setViewportView(jlstTelEndGrupo)
        jTDestinatario!!.model = DefaultTableModel(
            arrayOf(
                arrayOf(null, null, null, null),
                arrayOf(null, null, null, null),
                arrayOf(null, null, null, null),
                arrayOf(null, null, null, null)
            ), arrayOf("Title 1", "Title 2", "Title 3", "Title 4") as Array<*>
        )
        jTDestinatario!!.model = DestinatarioTableModel()
        jScrollPane2.setViewportView(jTDestinatario)
        jLabel3.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jLabel3.text = "Destinatários do grupo:"
        val jPanel1Layout = GroupLayout(jPanel1)
        jPanel1.layout = jPanel1Layout
        jPanel1Layout.horizontalGroup = jPanel1Layout.createParallelGroup(1)
            .add(
                2, jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .add(
                        jPanel1Layout.createParallelGroup(2)
                            .add(1, jScrollPane2, -1, 639, 32767)
                            .add(1, jScrollPane1, -1, 639, 32767)
                            .add(
                                1, jPanel1Layout.createSequentialGroup()
                                    .add(jLabel1)
                                    .addPreferredGap(0)
                                    .add(jtxtGrupo, -2, 288, -2)
                                    .addPreferredGap(0, 286, 32767)
                            )
                            .add(1, jLabel2)
                            .add(1, jLabel3)
                    )
                    .addContainerGap()
            )
        jPanel1Layout.verticalGroup = jPanel1Layout.createParallelGroup(1)
            .add(
                jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .add(
                        jPanel1Layout.createParallelGroup(3)
                            .add(jLabel1)
                            .add(jtxtGrupo, -2, -1, -2)
                    )
                    .addPreferredGap(0)
                    .add(jLabel2).addPreferredGap(0)
                    .add(jScrollPane1, -2, 77, -2)
                    .addPreferredGap(0)
                    .add(jLabel3)
                    .addPreferredGap(0)
                    .add(jScrollPane2, -1, 184, 32767)
                    .addContainerGap()
            )
        val layout = GroupLayout(contentPane)
        contentPane.layout = layout
        layout.horizontalGroup = layout.createParallelGroup(1)
            .add(jPanel1, -1, -1, 32767)
            .add(jToolBar1, -1, 663, 32767)
        layout.verticalGroup = layout.createParallelGroup(1)
            .add(
                layout.createSequentialGroup()
                    .add(jToolBar1, -2, 59, -2)
                    .addPreferredGap(0)
                    .add(jPanel1, -1, -1, 32767)
            )
        pack()
    }

    private fun jbtExcluirActionPerformed() {
        val linhaSelecionada = jlstTelEndGrupo!!.leadSelectionIndex
        if (jlstTelEndGrupo!!.isSelectionEmpty) {
            JOptionPane.showMessageDialog(
                this,
                "Não existe nenhum grupo selecionado!",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE
            )
        } else {
            val grupoBean: GrupoBean?
            jlstTelEndGrupo!!.clearSelection()
            val options = arrayOf("Sim", "Não")
            val resp = JOptionPane.showOptionDialog(
                this,
                "Tem certeza que deseja excluir este grupo juntamente com todos os seus destinatários?",
                "Endereçador",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                null
            )
            if (resp == 0) {
                grupoBean = vecTelEndGrupo[linhaSelecionada]
                try {
                    GrupoDestinatarioDao.instance!!.excluirGrupoDestinatario(grupoBean.numeroGrupo!!)
                    GrupoDao.instance!!.excluirGrupo(grupoBean.numeroGrupo)
                    recuperarListaGrupo()
                    jtxtGrupo!!.text = ""
                    JOptionPane.showMessageDialog(
                        this,
                        "Grupo e destinatários excluídos com sucesso!",
                        "Endereçador",
                        JOptionPane.INFORMATION_MESSAGE
                    )
                } catch (ex: DaoException) {
                    logger.error(ex.message, ex)
                    JOptionPane.showMessageDialog(
                        this,
                        "Não foi possivel realizar exclusão!",
                        "Endereçador",
                        JOptionPane.WARNING_MESSAGE
                    )
                }
            }
        }
    }

    private fun jbtEditarActionPerformed() {
        if (jlstTelEndGrupo!!.leadSelectionIndex != -1) {
            val grupoBean = vecTelEndGrupo[jlstTelEndGrupo!!.leadSelectionIndex]
            val telaEditarGrupo = TelaEditarGrupo(this, true, grupoBean)
            telaEditarGrupo.isVisible = true
            recuperarListaGrupo()
        } else {
            JOptionPane.showMessageDialog(
                this,
                "Não existe nenhum grupo selecionado!",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE
            )
        }
    }

    private fun jbtNovoActionPerformed() {
        val telaEditarGrupo = TelaEditarGrupo(this, true)
        telaEditarGrupo.isVisible = true
        recuperarListaGrupo()
    }

    private fun jlstTelEndGrupoValueChanged() {
        vecTelEndDestinatarioGrupo.removeAllElements()
        if (jlstTelEndGrupo!!.leadSelectionIndex != -1) {
            val grupoBean = vecTelEndGrupo[jlstTelEndGrupo!!.leadSelectionIndex]
            recuperarDestinatariosDoGrupo(grupoBean.numeroGrupo)
        }
    }

    private fun recuperarDestinatariosDoGrupo(nuGrupo: String?) {
        val destinatarioSort = DestinatarioBean()
        val model: DestinatarioTableModel?
        try {
            vecTelEndDestinatarioGrupo.removeAllElements()
            val arrayGrupoDestinatario = GrupoDestinatarioDao.instance!!.recuperaGrupoDestinatario(nuGrupo)
            vecTelEndDestinatarioGrupo.addAll(arrayGrupoDestinatario)
            arrayGrupoDestinatario.sortWith(destinatarioSort)
            model = jTDestinatario!!.model as DestinatarioTableModel
            model.setDestinatario(arrayGrupoDestinatario)
        } catch (e: DaoException) {
            logger.error(e.message, e)
            JOptionPane.showMessageDialog(
                this,
                "Não foi possivel carregar relação de destinatários do grupo",
                "Endereçador ECT",
                JOptionPane.WARNING_MESSAGE
            )
        }
    }

    fun recuperarListaGrupo() {
        val model: DestinatarioTableModel
        try {
            vecTelEndGrupo.removeAllElements()
            val arrayGrupo = GrupoDao.instance!!.recuperaGrupo("")
            val grupoBean: GrupoBean
            vecTelEndGrupo.addAll(arrayGrupo)
            if (vecTelEndGrupo.size >= 1) {
                grupoBean = vecTelEndGrupo[0]
                recuperarDestinatariosDoGrupo(grupoBean.numeroGrupo)
                jlstTelEndGrupo!!.setListData(vecTelEndGrupo)
                vecTelEndDestinatarioGrupo.removeAllElements()
                model = jTDestinatario!!.model as DestinatarioTableModel
                model.setDestinatario(arrayGrupo)
            }
        } catch (e: DaoException) {
            logger.error(e.message, e)
            JOptionPane.showMessageDialog(
                this,
                "Não foi possivel carregar relação de grupos",
                "Endereçador ECT",
                JOptionPane.WARNING_MESSAGE
            )
        }
    }

    private fun jbtPesquisarActionPerformed() {
        var grupoBean: GrupoBean?
        val nomeGrupo = jtxtGrupo!!.text.trim { it <= ' ' }
        var index = -1
        if (ultimaConsulta.equals(nomeGrupo, ignoreCase = true)) {
            index = jlstTelEndGrupo!!.selectedIndex
        } else {
            ultimaConsulta = nomeGrupo
        }
        for (i in index + 1 until vecTelEndGrupo.size) {
            grupoBean = vecTelEndGrupo[i]
            val nomeGrupoLista = grupoBean.descricaoGrupo
            if (nomeGrupoLista!!.uppercase(Locale.getDefault()).contains(nomeGrupo.uppercase(Locale.getDefault()))) {
                index = i
                break
            }
        }
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Grupo não encontrado!", "Endereçador", JOptionPane.INFORMATION_MESSAGE)
        } else {
            jlstTelEndGrupo!!.selectedIndex = index
            val tamanhoJscrool = jScrollPane1!!.verticalScrollBar.maximum
            val value = index * tamanhoJscrool / vecTelEndGrupo.size
            jScrollPane1!!.verticalScrollBar.value = value
        }
    }

    companion object {
        private val logger = Logger.getLogger(TelaGrupo::class.java)
    }
}
