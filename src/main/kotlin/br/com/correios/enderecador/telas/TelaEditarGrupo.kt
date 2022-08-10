package br.com.correios.enderecador.telas


import javax.swing.JDialog
import br.com.correios.enderecador.bean.DestinatarioBean
import javax.swing.JTable
import javax.swing.JTextField
import br.com.correios.enderecador.bean.GrupoBean
import br.com.correios.enderecador.dao.DestinatarioDao
import br.com.correios.enderecador.dao.DaoException
import javax.swing.JOptionPane
import javax.swing.JToolBar
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JLabel
import javax.swing.JScrollPane
import javax.swing.BorderFactory
import javax.swing.ImageIcon
import java.awt.event.ActionEvent
import javax.swing.table.DefaultTableModel
import br.com.correios.enderecador.bean.GrupoDestinatarioBean
import br.com.correios.enderecador.dao.GrupoDao
import br.com.correios.enderecador.dao.GrupoDestinatarioDao
import org.apache.log4j.Logger
import org.jdesktop.layout.GroupLayout
import java.awt.Dimension
import java.awt.Font
import java.awt.Frame
import java.awt.Toolkit
import java.lang.Exception
import java.util.*

class TelaEditarGrupo : JDialog {
    private var blnIncluir: Boolean
    private var nuGrupo: String?
    private var vecDestinatario: Vector<DestinatarioBean?>
    private var vecDestinatarioGrupo: Vector<DestinatarioBean?>
    private var jTDestinatario: JTable? = null
    private var jTDestinatarioGrupo: JTable? = null
    private var jtxtGrupo: JTextField? = null

    constructor(parent: Frame?, modal: Boolean) : super(parent, modal) {
        nuGrupo = ""
        vecDestinatario = Vector()
        vecDestinatarioGrupo = Vector()
        initComponents()
        blnIncluir = true
        configuracoesAdicionais()
        carregaListaDestinatario("")
    }

    constructor(parent: Frame?, modal: Boolean, grupo: GrupoBean) : super(parent, modal) {
        nuGrupo = ""
        vecDestinatario = Vector()
        vecDestinatarioGrupo = Vector()
        initComponents()
        blnIncluir = false
        configuracoesAdicionais()
        carregaListaDestinatario(grupo.numeroGrupo)
        jtxtGrupo!!.text = grupo.descricaoGrupo
        nuGrupo = grupo.numeroGrupo
    }

    private fun configuracoesAdicionais() {
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        val dialogSize = size
        if (dialogSize.height > screenSize.height) dialogSize.height = screenSize.height
        if (dialogSize.width > screenSize.width) dialogSize.width = screenSize.width
        setLocation((screenSize.width - dialogSize.width) / 2, (screenSize.height - dialogSize.height) / 2)
        jTDestinatario!!.model = DestinatarioTableModel()
        jTDestinatarioGrupo!!.model = DestinatarioTableModel()
    }

    private fun carregaListaDestinatario(grupo: String?) {
        var model: DestinatarioTableModel?
        try {
            val arrayDestinatarioGrupo: ArrayList<DestinatarioBean>
            val arrayDestinatario: ArrayList<DestinatarioBean>
            if (grupo == "") {
                arrayDestinatario = DestinatarioDao.instance!!.recuperaDestinatario("")
                arrayDestinatarioGrupo = ArrayList()
            } else {
                arrayDestinatario = DestinatarioDao.instance!!.recuperarDestinatarioForaDoGrupo(grupo!!)
                arrayDestinatarioGrupo = DestinatarioDao.instance!!.recuperaDestinatarioPorGrupo(grupo)
            }
            var i = 0
            while (i < arrayDestinatario.size) {
                vecDestinatario.add(arrayDestinatario[i])
                i++
            }
            model = jTDestinatario!!.model as DestinatarioTableModel
            model.setDestinatario(arrayDestinatario)
            i = 0
            while (i < arrayDestinatarioGrupo.size) {
                vecDestinatarioGrupo.add(arrayDestinatarioGrupo[i])
                i++
            }
            model = jTDestinatarioGrupo!!.model as DestinatarioTableModel
            model.setDestinatario(arrayDestinatarioGrupo)
        } catch (e: DaoException) {
            logger.error(e.message, e as Throwable)
            JOptionPane.showMessageDialog(this, "Não foi possivel carregar relação de destinatários", "Endereçador ", 2)
        }
    }

    private fun initComponents() {
        val jToolBar1 = JToolBar()
        val jbtConfirmar = JButton()
        val jbtVoltar = JButton()
        val jPanel1 = JPanel()
        val jLabel1 = JLabel()
        jtxtGrupo = JTextField()
        val jScrollPane1 = JScrollPane()
        jTDestinatario = JTable()
        val jScrollPane2 = JScrollPane()
        jTDestinatarioGrupo = JTable()
        val jbtAdicionar = JButton()
        val jbtRemover = JButton()
        defaultCloseOperation = 2
        title = "Cadastrar Grupo"
        jToolBar1.border = BorderFactory.createEtchedBorder()
        jbtConfirmar.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jbtConfirmar.icon = ImageIcon(javaClass.getResource("/imagens/OK.gif"))
        jbtConfirmar.text = "Confirmar"
        jbtConfirmar.horizontalTextPosition = 0
        jbtConfirmar.maximumSize = Dimension(90, 60)
        jbtConfirmar.verticalTextPosition = 3
        jbtConfirmar.addActionListener { evt: ActionEvent -> jbtConfirmarActionPerformed(evt) }
        jToolBar1.add(jbtConfirmar)
        jbtVoltar.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jbtVoltar.icon = ImageIcon(javaClass.getResource("/imagens/sair.gif"))
        jbtVoltar.text = "Voltar"
        jbtVoltar.horizontalTextPosition = 0
        jbtVoltar.maximumSize = Dimension(90, 60)
        jbtVoltar.verticalTextPosition = 3
        jbtVoltar.addActionListener { evt: ActionEvent -> jbtVoltarActionPerformed(evt) }
        jToolBar1.add(jbtVoltar)
        jPanel1.border = BorderFactory.createEtchedBorder()
        jLabel1.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jLabel1.text = "Grupo:"
        jTDestinatario!!.model = DefaultTableModel(
            arrayOf(
                arrayOf(null, null, null, null),
                arrayOf(null, null, null, null),
                arrayOf(null, null, null, null),
                arrayOf(null, null, null, null)
            ), arrayOf("Title 1", "Title 2", "Title 3", "Title 4") as Array<Any>
        )
        jTDestinatario!!.autoResizeMode = 0
        jScrollPane1.setViewportView(jTDestinatario)
        jTDestinatarioGrupo!!.model = DefaultTableModel(
            arrayOf(
                arrayOf(null, null, null, null),
                arrayOf(null, null, null, null),
                arrayOf(null, null, null, null),
                arrayOf(null, null, null, null)
            ), arrayOf("Title 1", "Title 2", "Title 3", "Title 4") as Array<Any>
        )
        jTDestinatarioGrupo!!.autoResizeMode = 0
        jScrollPane2.setViewportView(jTDestinatarioGrupo)
        jbtAdicionar.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jbtAdicionar.icon = ImageIcon(javaClass.getResource("/imagens/add.gif"))
        jbtAdicionar.text = "Adicionar"
        jbtAdicionar.addActionListener { evt: ActionEvent -> jbtAdicionarActionPerformed(evt) }
        jbtRemover.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jbtRemover.icon = ImageIcon(javaClass.getResource("/imagens/rem.gif"))
        jbtRemover.text = "Remover"
        jbtRemover.addActionListener { evt: ActionEvent -> jbtRemoverActionPerformed(evt) }
        val jPanel1Layout = GroupLayout(jPanel1)
        jPanel1.layout = jPanel1Layout
        jPanel1Layout.horizontalGroup = jPanel1Layout.createParallelGroup(1)
            .add(
                jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .add(
                        jPanel1Layout.createParallelGroup(1)
                            .add(
                                jPanel1Layout.createSequentialGroup()
                                    .add(jScrollPane1, -1, 279, 32767)
                                    .addPreferredGap(0)
                                    .add(
                                        jPanel1Layout.createParallelGroup(1)
                                            .add(jbtRemover).add(jbtAdicionar)
                                    )
                                    .addPreferredGap(0)
                                    .add(jScrollPane2, -1, 275, 32767)
                            )
                            .add(
                                jPanel1Layout.createSequentialGroup()
                                    .add(jLabel1)
                                    .addPreferredGap(0)
                                    .add(jtxtGrupo, -2, 251, -2)
                            )
                    )
                    .addContainerGap()
            )
        jPanel1Layout.verticalGroup = jPanel1Layout.createParallelGroup(1)
            .add(
                jPanel1Layout.createSequentialGroup()
                    .addContainerGap().add(
                        jPanel1Layout.createParallelGroup(3)
                            .add(jLabel1)
                            .add(jtxtGrupo, -2, -1, -2)
                    )
                    .add(
                        jPanel1Layout.createParallelGroup(1)
                            .add(
                                jPanel1Layout.createSequentialGroup()
                                    .add(15, 15, 15)
                                    .add(
                                        jPanel1Layout.createParallelGroup(1)
                                            .add(jScrollPane1, -1, 222, 32767)
                                            .add(jScrollPane2, -1, 222, 32767)
                                    )
                            )
                            .add(
                                jPanel1Layout.createSequentialGroup()
                                    .add(70, 70, 70).add(jbtAdicionar)
                                    .add(48, 48, 48).add(jbtRemover, -2, 25, -2)
                                    .addPreferredGap(0, 65, 32767) as GroupLayout.Group
                            )
                    )
                    .addContainerGap()
            )
        val layout = GroupLayout(contentPane)
        contentPane.layout = layout
        layout.horizontalGroup = layout.createParallelGroup(1)
            .add(jToolBar1, -1, 687, 32767)
            .add(jPanel1, -1, -1, 32767)
        layout.verticalGroup = layout.createParallelGroup(1)
            .add(
                layout.createSequentialGroup()
                    .add(jToolBar1, -2, 55, -2)
                    .addPreferredGap(0)
                    .add(jPanel1, -1, -1, 32767)
            )
        pack()
    }

    private fun jbtRemoverActionPerformed(evt: ActionEvent) {
        var model: DestinatarioTableModel? = null
        val vecTemp = Vector<DestinatarioBean?>()
        var arraySort = ArrayList<DestinatarioBean?>()
        var destinatarioBean: DestinatarioBean? = DestinatarioBean()
        try {
            if (jTDestinatarioGrupo!!.selectedRowCount > 0) {
                model = jTDestinatarioGrupo!!.model as DestinatarioTableModel
                var i: Int
                i = 0
                while (i < jTDestinatarioGrupo!!.rowCount) {
                    destinatarioBean = model.getDestinatario(i)
                    if (!jTDestinatarioGrupo!!.isRowSelected(i)) {
                        vecTemp.add(destinatarioBean)
                    } else {
                        vecDestinatario.add(destinatarioBean)
                    }
                    i++
                }
                vecDestinatarioGrupo.removeAllElements()
                vecDestinatarioGrupo = vecTemp
                i = 0
                while (i < vecDestinatarioGrupo.size) {
                    arraySort.add(vecDestinatarioGrupo[i])
                    i++
                }
                arraySort.sortWith(destinatarioBean!!)
                model = jTDestinatarioGrupo!!.model as DestinatarioTableModel
                model!!.setDestinatario(arraySort)
                vecDestinatarioGrupo.removeAllElements()
                i = 0
                while (i < arraySort.size) {
                    vecDestinatarioGrupo.add(arraySort[i])
                    i++
                }
                arraySort = ArrayList()
                i = 0
                while (i < vecDestinatario.size) {
                    arraySort.add(vecDestinatario[i])
                    i++
                }
                arraySort.sortWith(destinatarioBean)
                model = jTDestinatario!!.model as DestinatarioTableModel
                model.setDestinatario(arraySort)
                vecDestinatario.removeAllElements()
                i = 0
                while (i < arraySort.size) {
                    vecDestinatario.add(arraySort[i])
                    i++
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun jbtAdicionarActionPerformed(evt: ActionEvent) {
        var model: DestinatarioTableModel? = null
        val vecTemp = Vector<DestinatarioBean?>()
        var arraySort = ArrayList<DestinatarioBean?>()
        var destinatarioBean: DestinatarioBean? = DestinatarioBean()
        if (jTDestinatario!!.selectedRowCount > 0) {
            model = jTDestinatario!!.model as DestinatarioTableModel
            var i: Int
            i = 0
            while (i < jTDestinatario!!.rowCount) {
                destinatarioBean = model.getDestinatario(i)
                if (!jTDestinatario!!.isRowSelected(i)) {
                    vecTemp.add(destinatarioBean)
                } else {
                    vecDestinatarioGrupo.add(destinatarioBean)
                }
                i++
            }
            vecDestinatario.removeAllElements()
            vecDestinatario = vecTemp
            i = 0
            while (i < vecDestinatario.size) {
                arraySort.add(vecDestinatario[i])
                i++
            }
            Collections.sort(arraySort, destinatarioBean)
            model = jTDestinatario!!.model as DestinatarioTableModel
            model!!.setDestinatario(arraySort)
            vecDestinatario.removeAllElements()
            i = 0
            while (i < arraySort.size) {
                vecDestinatario.add(arraySort[i])
                i++
            }
            arraySort = ArrayList()
            i = 0
            while (i < vecDestinatarioGrupo.size) {
                arraySort.add(vecDestinatarioGrupo[i])
                i++
            }
            Collections.sort(arraySort, destinatarioBean)
            model = jTDestinatarioGrupo!!.model as DestinatarioTableModel
            model.setDestinatario(arraySort)
            vecDestinatarioGrupo.removeAllElements()
            i = 0
            while (i < arraySort.size) {
                vecDestinatarioGrupo.add(arraySort[i])
                i++
            }
        }
    }

    private fun jbtVoltarActionPerformed(evt: ActionEvent) {
        isVisible = false
    }

    private fun jbtConfirmarActionPerformed(evt: ActionEvent) {
        val grupoBean: GrupoBean?
        val grupoDestinatarioBean: GrupoDestinatarioBean?
        var destinatarioBean: DestinatarioBean?
        if (jtxtGrupo!!.text.trim { it <= ' ' } == "") {
            JOptionPane.showMessageDialog(this, "O campo grupo deve ser preenchido!", "Endereçador", 2)
            jtxtGrupo!!.requestFocus()
        } else {
            try {
                if (blnIncluir) {
                    grupoBean = GrupoBean.instance
                    grupoBean!!.descricaoGrupo = jtxtGrupo!!.text
                    GrupoDao.instance!!.incluirGrupo(grupoBean)
                    nuGrupo = GrupoDao.instance!!.recuperaUltimoGrupo()
                    grupoDestinatarioBean = GrupoDestinatarioBean.instance
                    for (bean in vecDestinatarioGrupo) {
                        grupoDestinatarioBean!!.numeroGrupo = nuGrupo
                        destinatarioBean = bean
                        grupoDestinatarioBean.numeroDestinatario = destinatarioBean!!.numeroDestinatario
                        GrupoDestinatarioDao.instance!!.incluirGrupoDestinatario(grupoDestinatarioBean)
                    }
                    JOptionPane.showMessageDialog(null, "Dados gravados com sucesso!", "Endereçador", 1)
                    blnIncluir = false
                } else {
                    grupoBean = GrupoBean.instance
                    grupoBean!!.numeroGrupo = nuGrupo
                    grupoBean.descricaoGrupo = jtxtGrupo!!.text
                    GrupoDao.instance!!.alterarGrupo(grupoBean)
                    grupoDestinatarioBean = GrupoDestinatarioBean.instance
                    GrupoDestinatarioDao.instance!!.excluirGrupoDestinatario(nuGrupo!!)
                    for (bean in vecDestinatarioGrupo) {
                        grupoDestinatarioBean!!.numeroGrupo = nuGrupo
                        destinatarioBean = bean
                        grupoDestinatarioBean.numeroDestinatario = destinatarioBean!!.numeroDestinatario
                        GrupoDestinatarioDao.instance!!.incluirGrupoDestinatario(grupoDestinatarioBean)
                    }
                    JOptionPane.showMessageDialog(null, "Dados atualizados com sucesso!", "Endereçador", 1)
                }
            } catch (ge: DaoException) {
                logger.error(ge.message, ge)
                JOptionPane.showMessageDialog(null, "Não foi possivel gravar dados", "Endereçador", 2)
            }
        }
    }

    companion object {
        private val logger = Logger.getLogger(TelaEditarGrupo::class.java)
    }
}
