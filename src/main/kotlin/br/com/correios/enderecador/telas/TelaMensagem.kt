package br.com.correios.enderecador.telas

import javax.swing.JDialog
import br.com.correios.enderecador.bean.GlobalBean
import javax.swing.JCheckBox
import javax.swing.JPanel
import javax.swing.JLabel
import javax.swing.JButton
import javax.swing.BorderFactory
import org.netbeans.lib.awtextra.AbsoluteLayout
import org.netbeans.lib.awtextra.AbsoluteConstraints
import java.awt.*
import javax.swing.ImageIcon

class TelaMensagem : JDialog() {
    private var globalBean: GlobalBean? = null
    private var jchkMensagem: JCheckBox? = null

    init {
        initComponents()
        configuracoesAdicionais()
    }

    private fun configuracoesAdicionais() {
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        val dialogSize = size
        if (dialogSize.height > screenSize.height) dialogSize.height = screenSize.height
        if (dialogSize.width > screenSize.width) dialogSize.width = screenSize.width
        setLocation((screenSize.width - dialogSize.width) / 2, (screenSize.height - dialogSize.height) / 2)
    }

    private fun initComponents() {
        val jPanel1 = JPanel()
        jchkMensagem = JCheckBox()
        val jLabel1 = JLabel()
        val jLabel2 = JLabel()
        val jLabel3 = JLabel()
        val jPanel2 = JPanel()
        val jButton1 = JButton()
        defaultCloseOperation = 2
        title = "Endereçador"
        isModal = true
        isResizable = false
        jPanel1.border = BorderFactory.createEtchedBorder()
        jPanel1.layout = AbsoluteLayout()
        jchkMensagem!!.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jchkMensagem!!.text = "Não exibir essa mensagem nessa sessão novamente."
        jPanel1.add(jchkMensagem, AbsoluteConstraints(150, 120, -1, -1))
        jLabel1.font = Font(Font.SANS_SERIF, Font.BOLD, 11)
        jLabel1.text = "A T E N Ç Ã O:  Existe nova versão do Endereçador no site dos Correios."
        jPanel1.add(jLabel1, AbsoluteConstraints(30, 40, -1, -1))
        jLabel2.font = Font(Font.SANS_SERIF, Font.BOLD, 11)
        jLabel2.text = "Para obter acesse o endereço: http://www2.correios.com.br/enderecador"
        jPanel1.add(jLabel2, AbsoluteConstraints(30, 60, -1, -1))
        jLabel3.font = Font(Font.SANS_SERIF, Font.BOLD, 11)
        jLabel3.text = "E faça a baixa do arquivo de instalação e execute-o no seu computador."
        jPanel1.add(jLabel3, AbsoluteConstraints(30, 80, -1, -1))
        contentPane.add(jPanel1, "Center")
        jPanel2.border = BorderFactory.createEtchedBorder()
        jPanel2.layout = BorderLayout()
        jButton1.icon = ImageIcon(javaClass.getResource("/imagens/OK.gif"))
        jButton1.toolTipText = "Ok"
        jButton1.preferredSize = Dimension(70, 40)
        jButton1.addActionListener { jButton1ActionPerformed() }
        jPanel2.add(jButton1, "East")
        contentPane.add(jPanel2, "South")
        size = Dimension(605, 246)
        setLocationRelativeTo(null)
    }

    private fun jButton1ActionPerformed() {
        if (jchkMensagem!!.isSelected) globalBean!!.mostraMensagem = "NAO"
        isVisible = false
    }
}
