package br.com.correios.enderecador.telas

import javax.swing.JDialog
import br.com.correios.enderecador.bean.ConfiguracaoBean
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JTextField
import br.com.correios.enderecador.excecao.EnderecadorExcecao
import javax.swing.JOptionPane
import javax.help.HelpSet
import javax.help.CSH.DisplayHelpFromSource
import javax.swing.JPanel
import javax.swing.JLabel
import javax.swing.BorderFactory
import br.com.correios.enderecador.util.DocumentoPersonalizado
import org.netbeans.lib.awtextra.AbsoluteLayout
import javax.swing.ImageIcon
import org.netbeans.lib.awtextra.AbsoluteConstraints
import br.com.correios.enderecador.dao.ConfiguracaoDao
import br.com.correios.enderecador.dao.DaoException
import org.apache.log4j.Logger
import org.jdesktop.layout.GroupLayout
import org.koin.core.annotation.Singleton
import java.awt.*
import java.lang.Exception

@Singleton
class TelaConfiguracao : JDialog() {
    private var configuracaoBean: ConfiguracaoBean? = null
    private var jbntAjuda: JButton? = null
    private var jchkProxy: JCheckBox? = null
    private var jtxtDominio: JTextField? = null
    private var jtxtEndereco: JTextField? = null
    private var jtxtPorta: JTextField? = null

    init {
        initComponents()
        configuracaoAjuda()
        configuracoesAdicionais()
    }

    private fun configuracoesAdicionais() {
        configuracaoBean = ConfiguracaoBean.instance
        try {
            configuracaoBean!!.carregaVariaveis()
            if (configuracaoBean!!.proxy == "" || configuracaoBean!!.porta == "") {
                jchkProxy!!.isSelected = false
                jtxtEndereco!!.background = Color.lightGray
                jtxtEndereco!!.isEnabled = false
                jtxtPorta!!.background = Color.lightGray
                jtxtPorta!!.isEnabled = false
                jtxtDominio!!.background = Color.lightGray
                jtxtDominio!!.isEnabled = false
            } else {
                jchkProxy!!.isSelected = true
                jtxtEndereco!!.background = Color.white
                jtxtEndereco!!.isEnabled = true
                jtxtPorta!!.background = Color.white
                jtxtPorta!!.isEnabled = true
                jtxtEndereco!!.text = configuracaoBean!!.proxy
                jtxtPorta!!.text = configuracaoBean!!.porta
                jtxtDominio!!.text = configuracaoBean!!.dominio
            }
        } catch (ex: EnderecadorExcecao) {
            logger.error(ex.message, ex)
            JOptionPane.showMessageDialog(
                this,
                "Não foi possível configurar interface GUI com o sistema operacional",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE
            )
        }
    }

    private fun configuracaoAjuda() {
        val hs = getHelpSet("ajuda/helpset.hs")
        val hb = hs!!.createHelpBroker()
        hb.enableHelpKey(getRootPane(), "configurarProxy", hs)
        jbntAjuda!!.addActionListener(DisplayHelpFromSource(hb))
    }

    fun getHelpSet(helpsetfile: String): HelpSet? {
        var hs: HelpSet? = null
        val cl = javaClass.classLoader
        try {
            val hsURL = HelpSet.findHelpSet(cl, helpsetfile)
            hs = HelpSet(null, hsURL)
        } catch (ee: Exception) {
            logger.error("HelpSet: " + ee.message)
            logger.error("HelpSet: $helpsetfile not found")
        }
        return hs
    }

    private fun initComponents() {
        val jPanel3 = JPanel()
        jchkProxy = JCheckBox()
        val jLabel1 = JLabel()
        jtxtEndereco = JTextField()
        jtxtPorta = JTextField()
        val jLabel2 = JLabel()
        jtxtDominio = JTextField()
        val jLabel3 = JLabel()
        val jPanel1 = JPanel()
        val jbntOk = JButton()
        jbntAjuda = JButton()
        defaultCloseOperation = 2
        title = "Configuração Proxy"
        isModal = true
        jPanel3.border = BorderFactory.createEtchedBorder()
        jchkProxy!!.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jchkProxy!!.text = "Usar um servidor Proxy"
        jchkProxy!!.border = BorderFactory.createEmptyBorder(0, 0, 0, 0)
        jchkProxy!!.margin = Insets(0, 0, 0, 0)
        jchkProxy!!.addActionListener { jchkProxyActionPerformed() }
        jLabel1.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jLabel1.text = "Endereço:"
        jtxtPorta!!.document = DocumentoPersonalizado(10, 1)
        jLabel2.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jLabel2.text = "Porta:"
        jLabel3.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jLabel3.text = "Domínio:"
        jPanel1.layout = AbsoluteLayout()
        jPanel1.border = BorderFactory.createEtchedBorder()
        jbntOk.icon = ImageIcon(javaClass.getResource("/imagens/OK.gif"))
        jbntOk.maximumSize = Dimension(63, 39)
        jbntOk.minimumSize = Dimension(63, 39)
        jbntOk.addActionListener { jbntOkActionPerformed() }
        jPanel1.add(jbntOk, AbsoluteConstraints(290, 10, 60, 30))
        jbntAjuda!!.icon = ImageIcon(javaClass.getResource("/imagens/ajuda.gif"))
        jPanel1.add(jbntAjuda, AbsoluteConstraints(10, 10, 30, -1))
        val jPanel3Layout = GroupLayout(jPanel3)
        jPanel3.layout = jPanel3Layout
        jPanel3Layout.horizontalGroup = jPanel3Layout.createParallelGroup(1)
            .add(
                jPanel3Layout.createSequentialGroup()
                    .addContainerGap().add(jchkProxy)
                    .addContainerGap(230, 32767)
            )
            .add(
                jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .add(
                        jPanel3Layout.createParallelGroup(1)
                            .add(jLabel1)
                            .add(jLabel2)
                            .add(jLabel3)
                    )
                    .add(20, 20, 20)
                    .add(
                        jPanel3Layout.createParallelGroup(1)
                            .add(
                                jPanel3Layout.createParallelGroup(2, false)
                                    .add(1, jtxtDominio)
                                    .add(1, jtxtPorta, -2, 128, -2)
                            )
                            .add(jtxtEndereco, -2, 226, -2)
                    )
                    .addContainerGap(58, 32767)
            )
            .add(jPanel1, -1, 363, 32767)
        jPanel3Layout.verticalGroup = jPanel3Layout.createParallelGroup(1)
            .add(
                jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .add(jchkProxy).add(15, 15, 15)
                    .add(
                        jPanel3Layout.createParallelGroup(3)
                            .add(jLabel1).add(jtxtEndereco, -2, -1, -2)
                    )
                    .add(15, 15, 15)
                    .add(
                        jPanel3Layout.createParallelGroup(3)
                            .add(jtxtPorta, -2, -1, -2)
                            .add(jLabel2)
                    ).add(16, 16, 16)
                    .add(
                        jPanel3Layout.createParallelGroup(3)
                            .add(jtxtDominio, -2, -1, -2)
                            .add(jLabel3)
                    )
                    .addPreferredGap(0, 15, 32767)
                    .add(jPanel1, -2, 50, -2)
            )
        val layout = GroupLayout(contentPane)
        contentPane.layout = layout
        layout.horizontalGroup = layout.createParallelGroup(1)
            .add(jPanel3, -1, -1, 32767)
        layout.verticalGroup = layout.createParallelGroup(1)
            .add(jPanel3, -1, -1, 32767)
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        setBounds((screenSize.width - 375) / 2, (screenSize.height - 228) / 2, 375, 228)
    }

    private fun jchkProxyActionPerformed() {
        if (jchkProxy!!.isSelected) {
            jtxtEndereco!!.background = Color.white
            jtxtEndereco!!.isEnabled = true
            jtxtPorta!!.background = Color.white
            jtxtPorta!!.isEnabled = true
            jtxtDominio!!.background = Color.white
            jtxtDominio!!.isEnabled = true
        } else {
            jtxtEndereco!!.background = Color.lightGray
            jtxtEndereco!!.isEnabled = false
            jtxtPorta!!.background = Color.lightGray
            jtxtPorta!!.isEnabled = false
            jtxtDominio!!.background = Color.lightGray
            jtxtDominio!!.isEnabled = false
            jtxtEndereco!!.text = ""
            jtxtPorta!!.text = ""
            jtxtDominio!!.text = ""
        }
    }

    private fun jbntOkActionPerformed() {
        if (jchkProxy!!.isSelected) {
            if (jtxtEndereco!!.text.isBlank()) {
                JOptionPane.showMessageDialog(
                    this,
                    "O campo Endereço do proxy deve ser preenchido!",
                    "Endereçador",
                    JOptionPane.WARNING_MESSAGE
                )
                jtxtEndereco!!.requestFocus()
                return
            }
            if (jtxtPorta!!.text.trim { it <= ' ' } == "") {
                JOptionPane.showMessageDialog(
                    this,
                    "O campo Porta deve ser preenchido!",
                    "Endereçador",
                    JOptionPane.WARNING_MESSAGE
                )
                jtxtPorta!!.requestFocus()
                return
            }
            try {
                configuracaoBean!!.proxy = jtxtEndereco!!.text
                configuracaoBean!!.porta = jtxtPorta!!.text
                configuracaoBean!!.dominio = jtxtDominio!!.text
                ConfiguracaoDao.instance!!.alterarConfiguracao(configuracaoBean!!)
            } catch (ex: DaoException) {
                JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível gravar configurações de proxy.",
                    "Enderecador",
                    JOptionPane.WARNING_MESSAGE
                )
            }
        } else {
            try {
                configuracaoBean!!.proxy = ""
                configuracaoBean!!.porta = ""
                configuracaoBean!!.dominio = ""
                val configuracaoDao = ConfiguracaoDao.instance
                configuracaoDao!!.alterarConfiguracao(configuracaoBean!!)
            } catch (ex: DaoException) {
                JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível gravar configurações de proxy.",
                    "Enderecador",
                    JOptionPane.WARNING_MESSAGE
                )
            }
        }
        isVisible = false
    }

    companion object {
        private val logger = Logger.getLogger(TelaConfiguracao::class.java)
    }
}