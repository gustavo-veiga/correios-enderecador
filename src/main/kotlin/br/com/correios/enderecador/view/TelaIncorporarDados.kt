package br.com.correios.enderecador.view

import javax.swing.JFrame
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
import javax.swing.JFileChooser
import javax.swing.JOptionPane
import br.com.correios.enderecador.bean.GrupoBean
import br.com.correios.enderecador.dao.GrupoDao
import br.com.correios.enderecador.exception.DaoException
import br.com.correios.enderecador.dao.DestinatarioDao
import br.com.correios.enderecador.bean.GrupoDestinatarioBean
import br.com.correios.enderecador.dao.GrupoDestinatarioDao
import br.com.correios.enderecador.service.CsvService
import br.com.correios.enderecador.tablemodel.IncorporaDestinatarioTableModel
import br.com.correios.enderecador.util.*
import br.com.correios.enderecador.util.FileExtension.CSV
import br.com.correios.enderecador.util.FileExtension.TXT
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.miginfocom.swing.MigLayout
import org.apache.log4j.Logger
import org.koin.core.annotation.Singleton
import java.awt.Dimension
import java.awt.Font
import java.awt.Font.PLAIN
import java.awt.Font.SANS_SERIF
import java.util.*

@Singleton
class TelaIncorporarDados(
    private val grupoDao: GrupoDao,
    private val csvService: CsvService,
    private val destinatarioDao: DestinatarioDao,
    private val grupoDestinatarioDao: GrupoDestinatarioDao,
) : JFrame() {
    private val vecDestinatario = mutableListOf<DestinatarioBean>()
    private val jcmbGrupo = JComboBox<String>()
    private val jtxtMensagem = JTextArea()
    private val tabDestinatario = JTable()

    init {
        initComponents()
        carregaGrupo()
        setLocationRelativeTo(null)
    }

    private fun initComponents() {
        title = "Importar dados"
        size = Dimension(750, 700)
        isResizable = true

        contentPane.add(JToolBar().apply {
            add(JButton("Confirmar").apply {
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@TelaIncorporarDados.javaClass.getResource("/imagens/OK.gif"))
                horizontalTextPosition = 0
                maximumSize = Dimension(90, 60)
                verticalTextPosition = 3
                addActionListener { gravarDados() }
            })
            add(JButton("Abrir arquivo").apply {
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@TelaIncorporarDados.javaClass.getResource("/imagens/arquivo.gif"))
                horizontalTextPosition = 0
                maximumSize = Dimension(90, 60)
                verticalTextPosition = 3
                addActionListener { jbtnAbrirActionPerformed() }
            })
            add(JButton("Limpar tela").apply {
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@TelaIncorporarDados.javaClass.getResource("/imagens/cancelar.gif"))
                horizontalTextPosition = 0
                maximumSize = Dimension(90, 60)
                verticalTextPosition = 3
                addActionListener { limpaTela() }
            })
        }, "North")

        contentPane.add(JPanel().apply {
            layout = MigLayout()

            add(JLabel("Grupo de destinatários:"))

            add(jcmbGrupo, "span, grow, pushx, width 9999")

            add(JScrollPane().apply {
                setViewportView(tabDestinatario)
            }, "span, grow, push")

            add(JScrollPane().apply {
                setViewportView(jtxtMensagem.apply {
                    columns = 20
                    isEditable = false
                    rows = 5
                })
            }, "span, grow, push")
        }, "Center")

        tabDestinatario.model = IncorporaDestinatarioTableModel()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun jbtnAbrirActionPerformed() {
        val fileChooser = JFileChooser().apply {
            fileSelectionMode = 0
            approveButtonText = "Abrir"
            dialogTitle = "Abrir arquivo"
            isAcceptAllFileFilterUsed = false
            addChoosableFileFilter(FileTypeFilter(CSV))
            addChoosableFileFilter(FileTypeFilter(TXT))
        }
        if (fileChooser.showOpenDialog(this) == JFileChooser.CANCEL_OPTION) {
            return
        }

        val file = fileChooser.selectedFile ?: return
        if (!file.exists()) {
            JOptionPane.showMessageDialog(
                this,
                "Não é possível importar esse tipo de arquivo.",
                "Endereçador ",
                JOptionPane.WARNING_MESSAGE)
            return
        }

        val model = IncorporaDestinatarioTableModel()
        tabDestinatario.model = model
        GlobalScope.launch(Dispatchers.Main) {
            when (file.extension) {
                CSV.extension -> csvService.import(file.absolutePath, true)
                TXT.extension -> csvService.import(file.absolutePath, false)
                else -> return@launch
            }.collect {
                model.insertRow(it)
            }
        }
    }

    fun carregaGrupo() {
        try {
            val arrayGrupo = grupoDao.recuperaGrupo()
            val vecGrupo = Vector<String?>()
            vecGrupo.add("(Selecione um grupo)")
            for (grupoBean in arrayGrupo) {
                vecGrupo.add(grupoBean.descricaoGrupo)
                jcmbGrupo.addItem(grupoBean.descricaoGrupo)
            }
        } catch (e: DaoException) {
            logger.error(e.message, e)
            JOptionPane.showMessageDialog(
                this,
                "Não foi possivel carregar relação dos grupos",
                "Endereçador ECT",
                JOptionPane.WARNING_MESSAGE)
        }
    }

    private fun gravarDados() {
        if (vecDestinatario.size != 0) {
            try {
                for (bean in vecDestinatario) {
                    destinatarioDao.incluirDestinatario(bean)
                    if (jcmbGrupo.selectedIndex != 0) {
                        val grupoBean = jcmbGrupo.selectedItem as GrupoBean
                        grupoDestinatarioDao.incluirGrupoDestinatario(
                            GrupoDestinatarioBean(
                                numeroGrupo = grupoBean.numeroGrupo,
                                numeroDestinatario = bean.numeroDestinatario!!
                        ))
                    }
                }
                JOptionPane.showMessageDialog(
                    this,
                    "Dados Gravados com sucesso",
                    "Enderecador",
                    JOptionPane.INFORMATION_MESSAGE)
                limpaTela()
            } catch (ex: DaoException) {
                logger.error(ex.message, ex)
                JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível gravar esse destinatário.",
                    "Enderecador",
                    JOptionPane.WARNING_MESSAGE)
            }
        }
    }

    private fun limpaTela() {
        jtxtMensagem.text = ""
        tabDestinatario.model = IncorporaDestinatarioTableModel()
    }

    companion object {
        private val logger = Logger.getLogger(TelaIncorporarDados::class.java)
    }
}