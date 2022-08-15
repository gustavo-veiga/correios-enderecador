package br.com.correios.enderecador.telas

import javax.swing.JDialog
import br.com.correios.enderecador.bean.ConfiguracaoBean
import javax.swing.JTabbedPane
import javax.swing.JLabel
import javax.swing.JTextField
import javax.swing.JOptionPane
import kotlin.Throws
import javax.swing.JPanel
import javax.swing.JButton
import javax.swing.BorderFactory
import javax.swing.ImageIcon
import java.awt.event.MouseAdapter
import br.com.correios.enderecador.util.Geral
import br.com.correios.enderecador.excecao.EnderecadorExcecao
import br.com.correios.enderecador.dao.ConfiguracaoDao
import br.com.correios.enderecador.dao.DaoException
import org.apache.log4j.Logger
import org.jdesktop.layout.GroupLayout
import org.koin.core.annotation.Singleton
import java.awt.Cursor
import java.awt.Font
import java.awt.Font.BOLD
import java.awt.Font.PLAIN
import java.awt.Toolkit
import java.awt.event.MouseEvent
import java.lang.Exception

@Singleton
class TelaSobre(
    private val configuracaoDao: ConfiguracaoDao,
    private val configuracaoBean: ConfiguracaoBean
) : JDialog() {
    private var jTabbedPane1: JTabbedPane? = null
    private var jlDirInstalacao: JLabel? = null
    private var jlJava: JLabel? = null
    private var jlJavaHome: JLabel? = null
    private var jlSistemaOperacional: JLabel? = null
    private var jlVM: JLabel? = null
    private var jlVersao: JLabel? = null
    private var jtxtChave: JTextField? = null

    init {
        initComponents()
        try {
            configuracoesAdicionais()
        } catch (ex: Exception) {
            logger.error(ex.message, ex)
            JOptionPane.showMessageDialog(
                null,
                "Não foi possível configurar interface GUI com o sistema operacional",
                "Erro",
                JOptionPane.ERROR_MESSAGE
            )
        }
    }

    @Throws(Exception::class)
    private fun configuracoesAdicionais() {
        configuracaoBean.carregaVariaveis()
        if (configuracaoBean.chave.isEmpty() && configuracaoBean.chave.isEmpty()) {
            jtxtChave!!.text = configuracaoBean.chave
        }
        if (configuracaoBean.versao != "") {
            jlVersao!!.text = "Versão " + configuracaoBean.versao + ", " + configuracaoBean.banco
        }
        jlSistemaOperacional!!.text =
            System.getProperty("os.name") + " versão " + System.getProperty("os.version") + " ( " + System.getProperty(
                "os.arch"
            ) + " )"
        jlJava!!.text = System.getProperty("java.version")
        jlVM!!.text =
            System.getProperty("java.vm.name") + " versão " + System.getProperty("java.runtime.version")
        jlJavaHome!!.text = System.getProperty("java.home")
        jlDirInstalacao!!.text = System.getProperty("user.dir")
        if (configuracaoBean.banco != "DNEC") jTabbedPane1!!.remove(1)
    }

    private fun initComponents() {
        val jLabel10 = JLabel()
        jTabbedPane1 = JTabbedPane()
        val jPanel1 = JPanel()
        val jLabel1 = JLabel()
        val jLabel2 = JLabel()
        val jLabel3 = JLabel()
        val jLabel4 = JLabel()
        jlVersao = JLabel()
        val jLabel9 = JLabel()
        val jLabel11 = JLabel()
        val jPanel2 = JPanel()
        val jLabel5 = JLabel()
        val jLabel6 = JLabel()
        val jLabel7 = JLabel()
        val jLabel8 = JLabel()
        jtxtChave = JTextField()
        val jPanel3 = JPanel()
        jlJava = JLabel()
        jlJavaHome = JLabel()
        jlSistemaOperacional = JLabel()
        val jLabel16 = JLabel()
        val jLabel17 = JLabel()
        val jLabel12 = JLabel()
        val jLabel13 = JLabel()
        jlDirInstalacao = JLabel()
        val jLabel14 = JLabel()
        jlVM = JLabel()
        val jbtnOk = JButton()
        jLabel10.text = "jLabel10"
        defaultCloseOperation = 2
        title = "Endereçador Escritório"
        isResizable = false
        jPanel1.border = BorderFactory.createEtchedBorder()
        jLabel1.horizontalAlignment = 0
        jLabel1.icon = ImageIcon(javaClass.getResource("/imagens/logo_enderecador.gif"))
        jLabel1.horizontalTextPosition = 0
        jLabel2.font = Font(Font.SANS_SERIF, PLAIN, 10)
        jLabel2.text = "Todos os direitos reservados, 2007"
        jLabel3.font = Font(Font.SANS_SERIF, PLAIN, 13)
        jLabel3.text = "Endereçador Escritório"
        jLabel4.font = Font(Font.SANS_SERIF, BOLD, 13)
        jLabel4.text = "Empresa Brasileira de Correios e Telégrafos"
        jlVersao!!.font = Font("MS Sans Serif", 1, 14)
        jlVersao!!.text = "Versão"
        jLabel9.font = Font("Tahoma", BOLD, 11)
        jLabel9.text = "Sugestões e aperfeiçoamentos: "
        jLabel11.font = Font("Tahoma", BOLD, 11)
        jLabel11.text =
            "<html><head></head><body><a href=http://www.correios.com.br/servicos/falecomoscorreios/default.cfm>Fale com os Correios</a></body></html>"
        jLabel11.toolTipText = ""
        jLabel11.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(evt: MouseEvent) {
                jLabel11MouseClicked()
            }

            override fun mouseEntered(evt: MouseEvent) {
                jLabel11MouseEntered()
            }

            override fun mouseExited(evt: MouseEvent) {
                jLabel11MouseExited()
            }
        })
        val jPanel1Layout = GroupLayout(jPanel1)
        jPanel1.layout = jPanel1Layout
        jPanel1Layout.horizontalGroup = jPanel1Layout.createParallelGroup(1)
            .add(
                jPanel1Layout.createSequentialGroup()
                    .addContainerGap().add(
                        jPanel1Layout.createParallelGroup(1)
                            .add(2, jLabel2)
                            .add(
                                jPanel1Layout.createSequentialGroup()
                                    .add(jLabel3)
                                    .addPreferredGap(0, 189, 32767)
                                    .add(jlVersao)
                            )
                            .add(jLabel9).add(jLabel11)
                    )
                    .addContainerGap()
            )
            .add(
                jPanel1Layout.createSequentialGroup()
                    .add(51, 51, 51)
                    .add(jLabel4).addContainerGap(59, 32767)
            )
            .add(
                2, jPanel1Layout.createSequentialGroup()
                    .addContainerGap(88, 32767)
                    .add(jLabel1).add(77, 77, 77)
            )
        jPanel1Layout.verticalGroup = jPanel1Layout.createParallelGroup(1)
            .add(
                jPanel1Layout.createSequentialGroup()
                    .add(23, 23, 23)
                    .add(jLabel1)
                    .add(15, 15, 15)
                    .add(jLabel4)
                    .add(38, 38, 38)
                    .add(
                        jPanel1Layout.createParallelGroup(3)
                            .add(jLabel3).add(jlVersao)
                    )
                    .addPreferredGap(0, 38, 32767)
                    .add(jLabel9)
                    .addPreferredGap(0)
                    .add(jLabel11)
                    .add(14, 14, 14)
                    .add(jLabel2)
                    .addContainerGap()
            )
        jTabbedPane1!!.addTab("Sobre", jPanel1)
        jPanel2.border = BorderFactory.createEtchedBorder()
        jLabel5.font = Font("Tahoma", BOLD, 11)
        jLabel5.text = "A Chave de Registro possibilita o uso de todas as"
        jLabel6.font = Font("Tahoma", BOLD, 11)
        jLabel6.text = "funcionalidades do  Endereçador, mas não impede"
        jLabel7.font = Font("Tahoma", BOLD, 11)
        jLabel7.text = "a utilização do sistema."
        jLabel8.font = Font("Tahoma", BOLD, 11)
        jLabel8.text = "Chave de registro:"
        val jPanel2Layout = GroupLayout(jPanel2)
        jPanel2.layout = jPanel2Layout
        jPanel2Layout.horizontalGroup = jPanel2Layout
            .createParallelGroup(1)
            .add(
                2, jPanel2Layout.createSequentialGroup()
                    .add(30, 30, 30)
                    .add(
                        jPanel2Layout.createParallelGroup(1, false)
                            .add(jLabel8, -2, 313, -2)
                            .add(jtxtChave, -2, 329, -2)
                            .add(jLabel6, -1, -1, 32767)
                            .add(jLabel5, -1, 330, 32767)
                            .add(jLabel7, -1, -1, 32767)
                    )
                    .addContainerGap(30, 32767)
            )
        jPanel2Layout.verticalGroup = jPanel2Layout.createParallelGroup(1)
            .add(
                jPanel2Layout.createSequentialGroup()
                    .add(70, 70, 70)
                    .add(jLabel5).add(16, 16, 16)
                    .add(jLabel6).add(16, 16, 16)
                    .add(jLabel7).add(47, 47, 47)
                    .add(jLabel8).addPreferredGap(0)
                    .add(jtxtChave, -2, -1, -2)
                    .addContainerGap(93, 32767)
            )
        jTabbedPane1!!.addTab("Chave de registro", jPanel2)
        jlJava!!.font = Font("Tahoma", PLAIN, 10)
        jlJava!!.text = "jlJVM"
        jlJavaHome!!.font = Font("Tahoma", PLAIN, 10)
        jlJavaHome!!.text = "jlJavaHome"
        jlSistemaOperacional!!.font = Font("Tahoma", PLAIN, 10)
        jlSistemaOperacional!!.text = "jlSistemaOperacional"
        jLabel16.font = Font("Tahoma", BOLD, 11)
        jLabel16.text = "Java:"
        jLabel17.font = Font("Tahoma", BOLD, 11)
        jLabel17.text = "Java Home:"
        jLabel12.font = Font("Tahoma", BOLD, 11)
        jLabel12.text = "Sistema Operacional:"
        jLabel13.font = Font("Tahoma", BOLD, 11)
        jLabel13.text = "Diretório instalação:"
        jlDirInstalacao!!.font = Font("Tahoma", PLAIN, 10)
        jlDirInstalacao!!.text = "jlDirInstalacao"
        jLabel14.font = Font("Tahoma", BOLD, 11)
        jLabel14.text = "VM:"
        jlVM!!.font = Font("Tahoma", PLAIN, 10)
        jlVM!!.text = "jlVM"
        val jPanel3Layout = GroupLayout(jPanel3)
        jPanel3.layout = jPanel3Layout
        jPanel3Layout.horizontalGroup = jPanel3Layout.createParallelGroup(1)
            .add(
                jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .add(
                        jPanel3Layout.createParallelGroup(1)
                            .add(
                                jPanel3Layout.createSequentialGroup()
                                    .add(jLabel17).add(57, 57, 57)
                                    .add(jlJavaHome, -1, 251, 32767)
                            )
                            .add(
                                jPanel3Layout.createSequentialGroup().add(jLabel12)
                                    .add(4, 4, 4)
                                    .add(jlSistemaOperacional, -1, 251, 32767)
                            )
                            .add(
                                jPanel3Layout.createSequentialGroup()
                                    .add(
                                        jPanel3Layout.createParallelGroup(1)
                                            .add(jLabel13)
                                            .add(jLabel14)
                                            .add(jLabel16)
                                    )
                                    .add(9, 9, 9)
                                    .add(
                                        jPanel3Layout.createParallelGroup(1)
                                            .add(jlJava, -1, 251, 32767)
                                            .add(jlVM, -1, 251, 32767)
                                            .add(jlDirInstalacao, -1, 251, 32767)
                                    )
                            )
                    )
                    .addContainerGap()
            )
        jPanel3Layout.verticalGroup = jPanel3Layout.createParallelGroup(1)
            .add(
                jPanel3Layout.createSequentialGroup()
                    .add(44, 44, 44)
                    .add(
                        jPanel3Layout.createParallelGroup(3)
                            .add(jLabel12)
                            .add(jlSistemaOperacional)
                            .add(27, 27, 27)
                            .add(
                                jPanel3Layout.createParallelGroup(3)
                                    .add(jLabel13).add(jlDirInstalacao)
                            )
                            .add(30, 30, 30)
                            .add(
                                jPanel3Layout.createParallelGroup(3)
                                    .add(jLabel14).add(jlVM)
                            )
                            .add(24, 24, 24)
                            .add(
                                jPanel3Layout.createParallelGroup(3)
                                    .add(jLabel16).add(jlJava)
                            )
                            .add(32, 32, 32)
                            .add(
                                jPanel3Layout.createParallelGroup(3)
                                    .add(jLabel17).add(jlJavaHome)
                            )
                    )
                    .addContainerGap(101, 32767)
            )
        jTabbedPane1!!.addTab("Info. Sistema", jPanel3)
        jbtnOk.text = "Ok"
        jbtnOk.addActionListener {jbtnOkActionPerformed() }
        val layout = GroupLayout(contentPane)
        contentPane.layout = layout
        layout.horizontalGroup = layout.createParallelGroup(1)
            .add(jTabbedPane1, -1, 399, 32767)
            .add(
                2, layout.createSequentialGroup()
                    .addContainerGap(169, 32767)
                    .add(jbtnOk, -2, 71, -2)
                    .add(159, 159, 159)
            )
        layout.verticalGroup = layout.createParallelGroup(1)
            .add(
                2, layout.createSequentialGroup()
                    .add(jTabbedPane1, -1, 353, 32767)
                    .addPreferredGap(0)
                    .add(jbtnOk)
                    .addContainerGap()
            )
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        setBounds((screenSize.width - 407) / 2, (screenSize.height - 420) / 2, 407, 420)
    }

    private fun jLabel11MouseClicked() {
        try {
            Geral.displayURL(configuracaoBean.paginaFaleConosco)
        } catch (ex: EnderecadorExcecao) {
            logger.error(ex.message, ex)
            JOptionPane.showMessageDialog(
                this,
                "Não foi possível ativar seu browse, por favor entre no site: " + configuracaoBean.paginaFaleConosco,
                "Endereçador",
                2
            )
        }
    }

    private fun jLabel11MouseExited() {
        cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
    }

    private fun jLabel11MouseEntered() {
        cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
    }

    private fun jbtnOkActionPerformed() {
        configuracaoBean.chave = jtxtChave!!.text
        try {
            configuracaoDao.alterarConfiguracao(configuracaoBean)
        } catch (ex: DaoException) {
            logger.error(ex.message, ex)
            JOptionPane.showMessageDialog(this, "Não foi possível gravar chave.", "Endereçador", 2)
        }
        isVisible = false
    }

    companion object {
        private val logger = Logger.getLogger(TelaSobre::class.java)
    }
}
