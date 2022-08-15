package br.com.correios.enderecador.telas

import javax.swing.JFrame
import br.com.correios.enderecador.bean.DestinatarioBean
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JList
import br.com.correios.enderecador.util.EnderecadorObservable
import br.com.correios.enderecador.dao.DestinatarioDao
import br.com.correios.enderecador.dao.DaoException
import javax.swing.JOptionPane
import javax.swing.JToolBar
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.ImageIcon
import javax.swing.BorderFactory
import br.com.correios.enderecador.util.Geral
import javax.swing.JFileChooser
import br.com.correios.enderecador.util.FiltroArquivo
import br.com.correios.enderecador.excecao.EnderecadorExcecao
import org.apache.log4j.Logger
import org.koin.core.annotation.Singleton
import java.awt.*
import java.util.*

@Singleton
class TelaExportarDados : JFrame(), Observer {
    private val vecDestinatario: Vector<DestinatarioBean?> = Vector()
    private var jbntDestinatario: JButton? = null
    private var jbntGrupo: JButton? = null
    private var jchkExportarTodos: JCheckBox? = null
    private var jlstDestinatarios: JList<DestinatarioBean>? = null

    init {
        val observable = EnderecadorObservable.instance
        initComponents()
        observable?.addObserver(this)
        setLocationRelativeTo(null)
    }

    fun carregaListaDestinatario() {
        try {
            val arrayDestinatario = DestinatarioDao.instance!!.recuperaDestinatario("")
            vecDestinatario.addAll(arrayDestinatario)
            jlstDestinatarios!!.setListData(vecDestinatario)
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
        val jbtExportar = JButton()
        jbntDestinatario = JButton()
        jbntGrupo = JButton()
        val jbntExcluir = JButton()
        val jPanel1 = JPanel()
        val jPanel2 = JPanel()
        jchkExportarTodos = JCheckBox()
        val jScrollPane1 = JScrollPane()
        jlstDestinatarios = JList()
        isResizable = true
        title = "Exportar Dados"
        preferredSize = Dimension(744, 434)
        jToolBar1.preferredSize = Dimension(325, 59)
        jbtExportar.font = Font("MS Sans Serif", 0, 9)
        jbtExportar.icon = ImageIcon(javaClass.getResource("/imagens/OK.gif"))
        jbtExportar.text = "Exportar"
        jbtExportar.horizontalTextPosition = 0
        jbtExportar.maximumSize = Dimension(90, 60)
        jbtExportar.verticalTextPosition = 3
        jbtExportar.addActionListener { jbtExportarActionPerformed() }
        jToolBar1.add(jbtExportar)
        jbntDestinatario!!.font = Font("MS Sans Serif", 0, 9)
        jbntDestinatario!!.icon = ImageIcon(javaClass.getResource("/imagens/addusuario.gif"))
        jbntDestinatario!!.text = "Selecionar destinatário"
        jbntDestinatario!!.horizontalTextPosition = 0
        jbntDestinatario!!.maximumSize = Dimension(107, 60)
        jbntDestinatario!!.verticalTextPosition = 3
        jbntDestinatario!!.addActionListener { jbntDestinatarioActionPerformed() }
        jToolBar1.add(jbntDestinatario)
        jbntGrupo!!.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jbntGrupo!!.icon = ImageIcon(javaClass.getResource("/imagens/addusuarios.gif"))
        jbntGrupo!!.text = "Selecionar grupo"
        jbntGrupo!!.horizontalTextPosition = 0
        jbntGrupo!!.maximumSize = Dimension(90, 60)
        jbntGrupo!!.preferredSize = Dimension(90, 60)
        jbntGrupo!!.verticalTextPosition = 3
        jbntGrupo!!.addActionListener { jbntGrupoActionPerformed() }
        jToolBar1.add(jbntGrupo)
        jbntExcluir.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jbntExcluir.icon = ImageIcon(javaClass.getResource("/imagens/removerTodos.gif"))
        jbntExcluir.text = "Remover todos"
        jbntExcluir.horizontalTextPosition = 0
        jbntExcluir.maximumSize = Dimension(90, 60)
        jbntExcluir.verticalTextPosition = 3
        jbntExcluir.addActionListener { jbntExcluirActionPerformed() }
        jToolBar1.add(jbntExcluir)
        contentPane.add(jToolBar1, "North")
        jPanel1.layout = BorderLayout()
        jPanel2.layout = GridLayout(1, 0)
        jPanel2.border = BorderFactory.createEtchedBorder()
        jchkExportarTodos!!.font = Font("MS Sans Serif", 0, 10)
        jchkExportarTodos!!.text = "Exportar todos os destinatários"
        jchkExportarTodos!!.addActionListener { jchkExportarTodosActionPerformed() }
        jPanel2.add(jchkExportarTodos)
        jPanel1.add(jPanel2, "North")
        jScrollPane1.setViewportView(jlstDestinatarios)
        jPanel1.add(jScrollPane1, "Center")
        contentPane.add(jPanel1, "Center")
        pack()
    }

    private fun jbntExcluirActionPerformed() {
        vecDestinatario.removeAllElements()
        jlstDestinatarios!!.setListData(vecDestinatario)
        jchkExportarTodos!!.isSelected = false
        jbntDestinatario!!.isEnabled = true
        jbntGrupo!!.isEnabled = true
    }

    private fun jbntGrupoActionPerformed() {
        cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
        val telaPesquisarGrupo = TelaPesquisarGrupo(this, true, vecDestinatario)
        telaPesquisarGrupo.isVisible = true
        jlstDestinatarios!!.setListData(vecDestinatario)
        cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
    }

    private fun jbntDestinatarioActionPerformed() {
        cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
        val telaPesquisaDestinatario = TelaPesquisarDestinatario(this, true, vecDestinatario)
        telaPesquisaDestinatario.isVisible = true
        jlstDestinatarios!!.setListData(vecDestinatario)
        cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
    }

    private fun jchkExportarTodosActionPerformed() {
        cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
        jbntDestinatario!!.isEnabled = !jbntDestinatario!!.isEnabled
        jbntGrupo!!.isEnabled = !jbntGrupo!!.isEnabled
        vecDestinatario.removeAllElements()
        jlstDestinatarios!!.setListData(vecDestinatario)
        if (jchkExportarTodos!!.isSelected) carregaListaDestinatario()
        cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
    }

    private fun jbtExportarActionPerformed() {
        val geral = Geral()
        var vNomeArquivo: String
        if (vecDestinatario.size <= 0) {
            JOptionPane.showMessageDialog(
                this,
                "Não existe nenhum destinatário selecionado!",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE
            )
            return
        }
        val fileChooser = JFileChooser()
        fileChooser.approveButtonText = "Salvar"
        fileChooser.dialogTitle = "Salvar arquivo"
        fileChooser.fileSelectionMode = 0
        fileChooser.isAcceptAllFileFilterUsed = true
        fileChooser.addChoosableFileFilter(FiltroArquivo("csv"))
        fileChooser.addChoosableFileFilter(FiltroArquivo("txt"))
        val result = fileChooser.showSaveDialog(this)
        if (result == 1) return
        val fileName = fileChooser.selectedFile ?: return
        var extension = Geral.getExtension(fileName)
        val imageFilter = fileChooser.fileFilter as FiltroArquivo
        vNomeArquivo = fileName.absolutePath
        if (extension == null) {
            vNomeArquivo = vNomeArquivo + "." + imageFilter.extension
            extension = imageFilter.extension
        } else if (!Geral.validaArquivo(fileName)) {
            JOptionPane.showMessageDialog(
                this,
                "Formato do arquivo inválido!\nO arquivo deve ser do tipo TXT ou CSV.",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE
            )
            return
        }
        try {
            if (extension == "txt") {
                geral.exportToTxt(vNomeArquivo, vecDestinatario)
            } else {
                geral.exportToCsv(vNomeArquivo, vecDestinatario)
            }
            JOptionPane.showMessageDialog(this, "Exportação concluída!", "Endereçador", JOptionPane.WARNING_MESSAGE)
        } catch (ex: EnderecadorExcecao) {
            JOptionPane.showMessageDialog(this, ex.message, "Endereçador", JOptionPane.WARNING_MESSAGE)
        }
    }

    override fun update(o: Observable, arg: Any) {
        if (arg is DestinatarioBean) {
            val destinatario = arg
            val index = vecDestinatario.indexOf(destinatario)
            if (index != -1) {
                vecDestinatario.removeAt(index)
                vecDestinatario.add(index, destinatario)
                jlstDestinatarios!!.setListData(vecDestinatario)
            }
        } else if (arg is List<*>) {
            arg
                .filterNotNull()
                .forEach { vecDestinatario.remove(it as DestinatarioBean) }
            jlstDestinatarios!!.setListData(vecDestinatario)
        }
    }

    companion object {
        private val logger = Logger.getLogger(TelaExportarDados::class.java)
    }
}
