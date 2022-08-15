package br.com.correios.enderecador.telas

import javax.swing.JFrame
import br.com.correios.enderecador.util.EnderecadorObservable
import br.com.correios.enderecador.bean.DestinatarioBean
import javax.swing.JComboBox
import javax.swing.JTextArea
import javax.swing.JTable
import javax.swing.JToolBar
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JLabel
import javax.swing.JScrollPane
import javax.swing.ImageIcon
import javax.swing.BorderFactory
import javax.swing.table.DefaultTableModel
import javax.swing.JFileChooser
import br.com.correios.enderecador.util.FiltroArquivo
import br.com.correios.enderecador.util.Geral
import javax.swing.JOptionPane
import java.io.BufferedReader
import java.io.FileReader
import java.lang.StringBuilder
import br.com.correios.enderecador.util.Cep
import java.io.IOException
import br.com.correios.enderecador.util.TextoCellRenderer
import javax.swing.table.TableColumn
import br.com.correios.enderecador.bean.GrupoBean
import br.com.correios.enderecador.dao.GrupoDao
import br.com.correios.enderecador.dao.DaoException
import br.com.correios.enderecador.dao.DestinatarioDao
import br.com.correios.enderecador.bean.GrupoDestinatarioBean
import br.com.correios.enderecador.dao.GrupoDestinatarioDao
import org.apache.log4j.Logger
import org.jdesktop.layout.GroupLayout
import org.koin.core.annotation.Singleton
import java.awt.Dimension
import java.awt.Font
import java.util.*

@Singleton
class TelaIncorporarDados : JFrame() {
    private val observable = EnderecadorObservable.instance
    private var vecDestinatario: Vector<DestinatarioBean?>? = null
    private var destinatarioBean: DestinatarioBean? = null
    private var jcmbGrupo: JComboBox<String?>? = null
    private var jtxtMensagem: JTextArea? = null
    private var tabDestinatario: JTable? = null

    init {
        initComponents()
        setLocationRelativeTo(null)
    }

    private fun initComponents() {
        val jToolBar1 = JToolBar()
        val jbtnConfirmar = JButton()
        val jbtnAbrir = JButton()
        val jbtnLimpar = JButton()
        val jPanel1 = JPanel()
        val jLabel1 = JLabel()
        jcmbGrupo = JComboBox()
        val jPanel2 = JPanel()
        val jScrollPane1 = JScrollPane()
        tabDestinatario = JTable()
        val jScrollPane2 = JScrollPane()
        jtxtMensagem = JTextArea()
        isResizable = true
        title = "Importar dados"
        jbtnConfirmar.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jbtnConfirmar.icon = ImageIcon(javaClass.getResource("/imagens/OK.gif"))
        jbtnConfirmar.text = "Confirmar"
        jbtnConfirmar.horizontalTextPosition = 0
        jbtnConfirmar.maximumSize = Dimension(90, 60)
        jbtnConfirmar.verticalTextPosition = 3
        jbtnConfirmar.addActionListener { jbtnConfirmarActionPerformed() }
        jToolBar1.add(jbtnConfirmar)
        jbtnAbrir.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jbtnAbrir.icon = ImageIcon(javaClass.getResource("/imagens/arquivo.gif"))
        jbtnAbrir.text = "Abrir arquivo"
        jbtnAbrir.horizontalTextPosition = 0
        jbtnAbrir.maximumSize = Dimension(90, 60)
        jbtnAbrir.verticalTextPosition = 3
        jbtnAbrir.addActionListener { jbtnAbrirActionPerformed() }
        jToolBar1.add(jbtnAbrir)
        jbtnLimpar.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jbtnLimpar.icon = ImageIcon(javaClass.getResource("/imagens/cancelar.gif"))
        jbtnLimpar.text = "Limpar tela"
        jbtnLimpar.horizontalTextPosition = 0
        jbtnLimpar.maximumSize = Dimension(90, 60)
        jbtnLimpar.verticalTextPosition = 3
        jbtnLimpar.addActionListener { jbtnLimparActionPerformed() }
        jToolBar1.add(jbtnLimpar)
        jPanel1.border = BorderFactory.createEtchedBorder()
        jLabel1.text = "Grupo de destinatários:"
        carregaGrupo()
        val jPanel1Layout = GroupLayout(jPanel1)
        jPanel1.layout = jPanel1Layout
        jPanel1Layout.horizontalGroup = jPanel1Layout.createParallelGroup(1)
            .add(
                jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .add(jLabel1)
                    .addPreferredGap(0)
                    .add(jcmbGrupo, 0, 489, 32767)
                    .addContainerGap()
            )
        jPanel1Layout.verticalGroup = jPanel1Layout.createParallelGroup(1)
            .add(
                jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .add(
                        jPanel1Layout.createParallelGroup(3)
                            .add(jLabel1).add(jcmbGrupo, -2, -1, -2)
                    )
                    .addContainerGap(-1, 32767)
            )
        jPanel2.border = BorderFactory.createEtchedBorder()
        tabDestinatario!!.model = IncorporaDestinatarioTableModel()
        tabDestinatario!!.model = DefaultTableModel(
            arrayOf(
                arrayOf(null, null, null, null),
                arrayOf(null, null, null, null),
                arrayOf(null, null, null, null),
                arrayOf(null, null, null, null)
            ), arrayOf("Title 1", "Title 2", "Title 3", "Title 4")
        )
        tabDestinatario!!.model = IncorporaDestinatarioTableModel()
        jScrollPane1.setViewportView(tabDestinatario)
        jtxtMensagem!!.columns = 20
        jtxtMensagem!!.isEditable = false
        jtxtMensagem!!.rows = 5
        jScrollPane2.setViewportView(jtxtMensagem)
        val jPanel2Layout = GroupLayout(jPanel2)
        jPanel2.layout = jPanel2Layout
        jPanel2Layout.horizontalGroup = jPanel2Layout.createParallelGroup(1)
            .add(
                2, jPanel2Layout.createSequentialGroup()
                    .addContainerGap().add(
                        jPanel2Layout.createParallelGroup(2)
                            .add(1, jScrollPane1, -1, 606, 32767)
                            .add(1, jScrollPane2, -1, 606, 32767)
                    )
                    .addContainerGap()
            )
        jPanel2Layout.verticalGroup = jPanel2Layout.createParallelGroup(1)
            .add(
                2, jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .add(jScrollPane1, -1, 183, 32767)
                    .addPreferredGap(0)
                    .add(jScrollPane2, -2, 98, -2)
                    .addContainerGap()
            )
        val layout = GroupLayout(contentPane)
        contentPane.layout = layout
        layout.horizontalGroup = layout.createParallelGroup(1)
            .add(jToolBar1, -1, 630, 32767)
            .add(jPanel1, -1, -1, 32767)
            .add(jPanel2, -1, -1, 32767)
        layout.verticalGroup = layout.createParallelGroup(1)
            .add(
                layout.createSequentialGroup()
                    .add(jToolBar1, -2, 59, -2)
                    .addPreferredGap(0)
                    .add(jPanel1, -2, -1, -2)
                    .addPreferredGap(0)
                    .add(jPanel2, -1, -1, 32767)
            )
        pack()
    }

    private fun jbtnLimparActionPerformed() {
        limpaTela()
    }

    private fun jbtnConfirmarActionPerformed() {
        gravarDados()
    }

    private fun jbtnAbrirActionPerformed() {
        val fileChooser = JFileChooser()
        fileChooser.approveButtonText = "Abrir"
        fileChooser.dialogTitle = "Abrir arquivo"
        fileChooser.isAcceptAllFileFilterUsed = true
        fileChooser.addChoosableFileFilter(FiltroArquivo("csv"))
        fileChooser.addChoosableFileFilter(FiltroArquivo("txt"))
        fileChooser.fileSelectionMode = 0
        val result = fileChooser.showOpenDialog(this)
        if (result == 1) return
        val fileName = fileChooser.selectedFile
        if (!Geral.validaArquivo(fileName)) {
            JOptionPane.showMessageDialog(
                this,
                "Não é possível importar esse tipo de arquivo.",
                "Endereçador ",
                JOptionPane.WARNING_MESSAGE
            )
            return
        }
        if (fileName.name == "") {
            JOptionPane.showMessageDialog(this, "Nome de arquivo inválido", "Endereçador ", JOptionPane.ERROR_MESSAGE)
        } else {
            try {
                val `in` = BufferedReader(FileReader(fileName))
                val separador = ";"
                var contLinha = 0
                val bufferLog = StringBuilder()
                vecDestinatario = Vector()
                val options = arrayOf("Sim", "Não")
                val resp = JOptionPane.showOptionDialog(
                    null,
                    "Deseja importar os dados da primeira linha?",
                    "Endereçador",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    null
                )
                if (resp == 1) `in`.readLine()
                var linha: String
                while (`in`.readLine().also { linha = it } != null) {
                    contLinha++
                    val arrLinha = linha.split(separador.toRegex()).toTypedArray()
                    if (arrLinha.size == 15) {
                        destinatarioBean = DestinatarioBean()
                        destinatarioBean!!.titulo = arrLinha[0]
                        destinatarioBean!!.nome = arrLinha[1]
                        destinatarioBean!!.apelido = arrLinha[2]
                        destinatarioBean!!.caixaPostal = arrLinha[3]
                        destinatarioBean!!.endereco = arrLinha[4]
                        destinatarioBean!!.numeroEndereco = arrLinha[5]
                        destinatarioBean!!.complemento = arrLinha[6]
                        destinatarioBean!!.bairro = arrLinha[7]
                        destinatarioBean!!.cidade = arrLinha[8]
                        destinatarioBean!!.uf = arrLinha[9]
                        destinatarioBean!!.email = arrLinha[10]
                        destinatarioBean!!.telefone = arrLinha[11]
                        destinatarioBean!!.fax = arrLinha[12]
                        destinatarioBean!!.cepCaixaPostal = arrLinha[13]
                        destinatarioBean!!.cep = Cep.tiraMascaraCep(arrLinha[14])
                        if (destinatarioBean!!.nome.trim { it <= ' ' } == "") {
                            bufferLog.append("Não é possível gravar a linha ").append(contLinha).append(".")
                            bufferLog.append("Motivo: O nome do destinatário está em branco. ->  ").append(linha)
                                .append("\n")
                            continue
                        }
                        if (destinatarioBean!!.cep.orEmpty().trim { it <= ' ' } == "") {
                            bufferLog.append("Não é possível gravar a linha ").append(contLinha).append(".")
                            bufferLog.append("Motivo: O campo CEP está em branco. ->  ").append(linha).append("\n")
                            continue
                        }
                        if (destinatarioBean!!.endereco.trim { it <= ' ' } == "") {
                            bufferLog.append("Não é possível gravar a linha ").append(contLinha).append(".")
                            bufferLog.append("Motivo: O campo Endereço está em branco. ->  ").append(linha).append("\n")
                            continue
                        }
                        if (destinatarioBean!!.numeroEndereco.trim { it <= ' ' } == "") {
                            bufferLog.append("Não é possível gravar a linha ").append(contLinha).append(".")
                            bufferLog.append("Motivo: O campo número do endereço está em branco. ->  ").append(linha)
                                .append("\n")
                            continue
                        }
                        if (destinatarioBean!!.cidade.trim { it <= ' ' } == "") {
                            bufferLog.append("Não é possível gravar a linha ").append(contLinha).append(".")
                            bufferLog.append("Motivo: O campo cidade está em branco. ->  ").append(linha).append("\n")
                            continue
                        }
                        if (destinatarioBean!!.uf.trim { it <= ' ' } == "") {
                            bufferLog.append("Não é possível gravar a linha ").append(contLinha).append(".")
                            bufferLog.append("Motivo: O campo UF está em branco. ->  ").append(linha).append("\n")
                            continue
                        }
                        if (!destinatarioBean!!.cep?.let { Cep.validaCep(it) }!!) {
                            bufferLog.append("Não é possível gravar a linha ").append(contLinha).append(".")
                            bufferLog.append("Motivo: CEP inválido. ->  ").append(linha).append("\n")
                            continue
                        }
                        if (destinatarioBean!!.email != "" && !destinatarioBean!!.validaEmail()) {
                            bufferLog.append("Não é possível gravar a linha ").append(contLinha).append(".")
                            bufferLog.append("Motivo: E-mail inválido. ->  ").append(linha).append("\n")
                            continue
                        }
                        vecDestinatario!!.add(destinatarioBean)
                        continue
                    }
                    bufferLog.append("Não é possível gravar a linha ").append(contLinha).append(".")
                    bufferLog.append("Motivo: Estrutura da linha inválida. ->  ").append(linha).append("\n")
                }
                `in`.close()
                if (bufferLog.toString() == "") {
                    bufferLog.append("Todos os dados foram validados com sucesso! Para incorporar os dados clique no botão confirmar!")
                } else {
                    bufferLog.append("ATENÇÃO: Alguns dados não poderão ser importados.")
                }
                jtxtMensagem!!.text = bufferLog.toString()
                carregaTabela()
            } catch (exIO: IOException) {
                logger.error(exIO.message, exIO)
                JOptionPane.showMessageDialog(this, "Não foi possível abir o arquivo", "Endereçador", 0)
            }
        }
    }

    fun carregaTabela() {
        val model: IncorporaDestinatarioTableModel?
        if (vecDestinatario!!.size != 0) {
            Geral.ordenarVetor(vecDestinatario!!, destinatarioBean)
            val arrayDestinatario = ArrayList(vecDestinatario)
            model = tabDestinatario!!.model as IncorporaDestinatarioTableModel
            model.setDestinatario(arrayDestinatario)
            tabDestinatario!!.setSelectionMode(2)
            val renderer = TextoCellRenderer(2)
            var coluna: TableColumn?
            coluna = tabDestinatario!!.columnModel.getColumn(0)
            coluna.cellRenderer = renderer
            coluna = tabDestinatario!!.columnModel.getColumn(1)
            coluna.cellRenderer = renderer
            coluna = tabDestinatario!!.columnModel.getColumn(2)
            coluna.cellRenderer = renderer
            coluna = tabDestinatario!!.columnModel.getColumn(3)
            coluna.cellRenderer = renderer
            coluna = tabDestinatario!!.columnModel.getColumn(4)
            coluna.cellRenderer = renderer
            coluna = tabDestinatario!!.columnModel.getColumn(5)
            coluna.cellRenderer = renderer
            coluna = tabDestinatario!!.columnModel.getColumn(6)
            coluna.cellRenderer = renderer
            coluna = tabDestinatario!!.columnModel.getColumn(7)
            coluna.cellRenderer = renderer
            coluna = tabDestinatario!!.columnModel.getColumn(8)
            coluna.cellRenderer = renderer
            coluna = tabDestinatario!!.columnModel.getColumn(9)
            coluna.cellRenderer = renderer
            coluna = tabDestinatario!!.columnModel.getColumn(10)
            coluna.cellRenderer = renderer
            coluna = tabDestinatario!!.columnModel.getColumn(11)
            coluna.cellRenderer = renderer
            coluna = tabDestinatario!!.columnModel.getColumn(12)
            coluna.cellRenderer = renderer
        }
    }

    fun carregaGrupo() {
        try {
            val arrayGrupo = GrupoDao.instance!!.recuperaGrupo("")
            val vecGrupo = Vector<String?>()
            vecGrupo.add("(Selecione um grupo)")
            for (grupoBean in arrayGrupo) vecGrupo.add(grupoBean.descricaoGrupo)
            jcmbGrupo = JComboBox(vecGrupo)
        } catch (e: DaoException) {
            logger.error(e.message, e)
            JOptionPane.showMessageDialog(
                this,
                "Não foi possivel carregar relação dos grupos",
                "Endereçador ECT",
                JOptionPane.WARNING_MESSAGE
            )
        }
    }

    private fun gravarDados() {
        if (vecDestinatario == null) {
            JOptionPane.showMessageDialog(
                this,
                "Não existem destinatários incorporados para gravação.",
                "Endereçador",
                JOptionPane.INFORMATION_MESSAGE
            )
        } else if (vecDestinatario!!.size != 0) {
            try {
                for (bean in vecDestinatario!!) {
                    val destinatarioDao = DestinatarioDao.instance
                    destinatarioDao!!.incluirDestinatario(bean!!)
                    observable?.notifyObservers(bean)
                    if (jcmbGrupo!!.selectedIndex != 0) {
                        val grupoBean = jcmbGrupo!!.selectedItem as GrupoBean
                        val grupoDestinatarioBean = GrupoDestinatarioBean.instance
                        grupoDestinatarioBean!!.numeroDestinatario = bean.numeroDestinatario
                        grupoDestinatarioBean.numeroGrupo = grupoBean.numeroGrupo
                        GrupoDestinatarioDao.instance!!.incluirGrupoDestinatario(grupoDestinatarioBean)
                    }
                }
                JOptionPane.showMessageDialog(
                    this,
                    "Dados Gravados com sucesso",
                    "Enderecador",
                    JOptionPane.INFORMATION_MESSAGE
                )
                limpaTela()
            } catch (ex: DaoException) {
                logger.error(ex.message, ex)
                JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível gravar esse destinatário.",
                    "Enderecador",
                    JOptionPane.WARNING_MESSAGE
                )
            }
        }
    }

    private fun limpaTela() {
        jtxtMensagem!!.text = ""
        tabDestinatario!!.model = IncorporaDestinatarioTableModel()
    }

    companion object {
        private val logger = Logger.getLogger(TelaIncorporarDados::class.java)
    }
}