package br.com.correios.enderecador.telas

import javax.swing.JDialog
import br.com.correios.enderecador.bean.GrupoBean
import javax.swing.JScrollPane
import javax.swing.JList
import javax.swing.JTextField
import br.com.correios.enderecador.dao.GrupoDao
import br.com.correios.enderecador.exception.DaoException
import javax.swing.JOptionPane
import javax.swing.JToolBar
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JLabel
import javax.swing.BorderFactory
import javax.swing.ImageIcon
import br.com.correios.enderecador.dao.DestinatarioDao
import br.com.correios.enderecador.util.getAllItems
import br.com.correios.enderecador.util.setSelectedItem
import net.miginfocom.swing.MigLayout
import org.apache.log4j.Logger
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import java.awt.Dimension
import java.awt.Font
import java.awt.Font.PLAIN
import java.awt.Font.SANS_SERIF

class TelaPesquisarGrupo : KoinComponent, JDialog() {
    private val recipientDao: DestinatarioDao = get()
    private val groupDao: GrupoDao = get()

    private val groupList = JList<GrupoBean>()
    private val searchText = JTextField()

    init {
        initComponents()
        carregaListaGrupo()
        setLocationRelativeTo(null)
    }

    private fun carregaListaGrupo() {
        try {
            val group = groupDao.recuperaGrupo()
            groupList.setListData(group.toTypedArray())
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
        title = "Selecionar grupo"
        size = Dimension(598, 349)
        isResizable = false
        defaultCloseOperation = DISPOSE_ON_CLOSE

        contentPane.add(JToolBar().apply {
            add(JButton().apply {
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@TelaPesquisarGrupo.javaClass.getResource("/imagens/OK.gif"))
                text = "Confirmar"
                maximumSize = Dimension(90, 60)
                verticalTextPosition = 3
                horizontalTextPosition = 0
                addActionListener { jbtConfirmarActionPerformed() }
            })
            add(JButton().apply {
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@TelaPesquisarGrupo.javaClass.getResource("/imagens/binoculo.gif"))
                text = "Pesquisar"
                maximumSize = Dimension(90, 60)
                verticalTextPosition = 3
                horizontalTextPosition = 0
                addActionListener { jbtPesquisarActionPerformed() }
            })
            add(JButton().apply {
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@TelaPesquisarGrupo.javaClass.getResource("/imagens/sair.gif"))
                text = "Sair"
                maximumSize = Dimension(90, 60)
                preferredSize = Dimension(51, 27)
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
                text = "Grupo:"
            })

            add(searchText, "span, grow, pushx, width 9999")

            add(JScrollPane().apply {
                setViewportView(groupList)
            }, "span, grow, push")
        }, "Center")
    }

    private fun jbtPesquisarActionPerformed() {
        val search = searchText.text.trim().also {
            if (it.isBlank()) return
        }

        groupList.getAllItems()
            .find { it.descricaoGrupo.contains(search, ignoreCase = true) }
            ?.let {
                groupList.setSelectedItem(it)
                return
            }

        JOptionPane.showMessageDialog(
            this,
            "Grupo não encontrado!",
            "Endereçador",
            JOptionPane.INFORMATION_MESSAGE)
    }

    private fun jbtConfirmarActionPerformed() {
        try {
            if (groupList.selectedIndex != -1) {
                groupList.selectedIndices.forEach { row ->
                    val group = groupList.model.getElementAt(row)
                    val recipientGroup = recipientDao.recuperaDestinatarioPorGrupo(group.numeroGrupo)

                    recipientGroup.forEach { recipient ->
                        //searchResultList.add(recipient)
                    }
                }

                /*
                val modelGrupo: ListModel<GrupoBean> = jlstGrupo.model
                val arrayIndex = jlstGrupo.selectedIndices
                for (index in arrayIndex) {
                    val grupoBean = modelGrupo.getElementAt(index)
                    val arrayDestinatarioGrupo = destinatarioDao.recuperaDestinatarioPorGrupo(
                        grupoBean.numeroGrupo
                    )
                    for (bean in arrayDestinatarioGrupo) {
                        val destinatarioBean = bean
                        if (!vecDestinatarioRetorno.contains(destinatarioBean)) {
                            vecDestinatarioRetorno.add(destinatarioBean)
                        }
                    }
                }
                 */
                //Geral.ordenarVetor(vecDestinatarioRetorno, destinatarioBean)
                isVisible = false
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Não existe nenhum grupo selecionado",
                    "Endereçador ECT",
                    JOptionPane.INFORMATION_MESSAGE)
            }
        } catch (ex: DaoException) {
            logger.error(ex.message, ex)
        }
    }

    companion object {
        private val logger = Logger.getLogger(TelaPesquisarGrupo::class.java)
    }
}