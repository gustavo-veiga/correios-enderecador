package br.com.correios.enderecador.telas

import javax.swing.JDialog
import br.com.correios.enderecador.bean.ConfiguracaoBean
import javax.swing.JTextField
import javax.swing.JLabel
import javax.swing.JButton
import java.awt.event.ActionEvent
import javax.swing.ImageIcon
import br.com.correios.enderecador.dao.ConfiguracaoDao
import br.com.correios.enderecador.dao.DaoException
import javax.swing.JOptionPane
import org.apache.log4j.Logger
import org.jdesktop.layout.GroupLayout
import java.awt.Font
import java.awt.Frame
import java.awt.Toolkit

class TelaChaveRegistro(parent: Frame?, modal: Boolean) : JDialog(parent, modal) {
    private var configuracaoBean: ConfiguracaoBean? = null
    private var jtxtChave: JTextField? = null

    init {
        initComponents()
        configuracoesAdicionais()
    }

    private fun configuracoesAdicionais() {
        configuracaoBean = ConfiguracaoBean.instance
        title = "Endereçador Escritório v" + configuracaoBean!!.versao
    }

    private fun initComponents() {
        val jLabel5 = JLabel()
        val jLabel6 = JLabel()
        val jLabel7 = JLabel()
        jtxtChave = JTextField()
        val jLabel8 = JLabel()
        val jbtnOk = JButton()
        val jLabel1 = JLabel()
        val jLabel4 = JLabel()
        defaultCloseOperation = 2
        title = "Endereçador Escritório"
        isModal = true
        isResizable = false
        jLabel5.font = Font("Arial", Font.PLAIN, 11)
        jLabel5.text = "A Chave de Registro possibilita o uso de todas as"
        jLabel6.font = Font("Arial", Font.PLAIN, 11)
        jLabel6.text = "funcionalidades do  Endereçador, mas não impede"
        jLabel7.font = Font("Arial", Font.PLAIN, 11)
        jLabel7.text = "a utilização do sistema."
        jLabel8.font = Font("Arial", Font.PLAIN, 11)
        jLabel8.text = "Chave de registro:"
        jbtnOk.text = "Clique para Prosseguir"
        jbtnOk.addActionListener { evt: ActionEvent -> jbtnOkActionPerformed(evt) }
        jLabel1.icon = ImageIcon(javaClass.getResource("/imagens/logo_enderecador.gif"))
        jLabel4.font = Font("Arial", Font.BOLD, 13)
        jLabel4.horizontalAlignment = 0
        jLabel4.text = "Empresa Brasileira de Correios e Telégrafos"
        val layout = GroupLayout(contentPane)
        contentPane.layout = layout
        layout.horizontalGroup = layout.createParallelGroup(1)
            .add(jLabel4, -1, 332, 32767)
            .add(
                2, layout.createSequentialGroup()
                    .addContainerGap(37, 32767)
                    .add(
                        layout.createParallelGroup(1)
                            .add(
                                layout.createSequentialGroup()
                                    .add(jLabel7)
                                    .addContainerGap()
                            )
                            .add(
                                2, layout.createSequentialGroup()
                                    .add(
                                        layout.createParallelGroup(1)
                                            .add(jLabel8, -2, 98, -2)
                                            .add(jtxtChave, -1, 268, 32767)
                                            .add(jLabel5, -1, 268, 32767)
                                            .add(jLabel6, -1, 268, 32767)
                                    )
                                    .add(27, 27, 27)
                            )
                    )
            )
            .add(
                layout.createSequentialGroup().add(51, 51, 51)
                    .add(jLabel1).addContainerGap(56, 32767)
            )
            .add(
                layout.createSequentialGroup()
                    .add(95, 95, 95)
                    .add(jbtnOk).addContainerGap(96, 32767)
            )
        layout.verticalGroup = layout.createParallelGroup(1)
            .add(
                2, layout.createSequentialGroup()
                    .addContainerGap().add(jLabel1, -2, 100, -2)
                    .add(21, 21, 21)
                    .add(jLabel4).add(18, 18, 18)
                    .add(jLabel5).addPreferredGap(0)
                    .add(jLabel6).addPreferredGap(0)
                    .add(jLabel7).addPreferredGap(0, 57, 32767)
                    .add(jLabel8).addPreferredGap(0)
                    .add(jtxtChave, -2, -1, -2)
                    .add(34, 34, 34).add(jbtnOk)
                    .add(21, 21, 21)
            )
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        setBounds((screenSize.width - 340) / 2, (screenSize.height - 421) / 2, 340, 421)
    }

    private fun jbtnOkActionPerformed(evt: ActionEvent) {
        try {
            if (jtxtChave!!.text.trim { it <= ' ' } != "") {
                configuracaoBean!!.chave = jtxtChave!!.text
                ConfiguracaoDao.instance!!.alterarConfiguracao(configuracaoBean!!)
            }
        } catch (ex: DaoException) {
            JOptionPane.showMessageDialog(this, "Não foi possível gravar chave.", "Enderecador", 2)
            logger.error(ex.message, ex as Throwable)
        }
        isVisible = false
    }

    companion object {
        private val logger = Logger.getLogger(TelaChaveRegistro::class.java)
    }
}
