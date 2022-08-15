package br.com.correios.enderecador.telas

import br.com.correios.enderecador.bean.UsuarioBean.Companion.instance
import javax.swing.JDialog
import javax.swing.JPasswordField
import javax.swing.JTextField
import javax.swing.JPanel
import javax.swing.JLabel
import javax.swing.JButton
import javax.swing.BorderFactory
import java.awt.event.ActionEvent
import org.netbeans.lib.awtextra.AbsoluteLayout
import javax.swing.ImageIcon
import org.netbeans.lib.awtextra.AbsoluteConstraints
import org.jdesktop.layout.GroupLayout
import java.awt.Dimension
import java.awt.Font
import java.awt.Frame

class TelaUsuario(parent: Frame?, modal: Boolean) : JDialog(parent, modal) {
    private var jtxtSenha: JPasswordField? = null
    private var jtxtUsuario: JTextField? = null

    init {
        initComponents()
    }

    private fun initComponents() {
        val jPanel1 = JPanel()
        val jLabel1 = JLabel()
        jtxtUsuario = JTextField()
        val jLabel2 = JLabel()
        jtxtSenha = JPasswordField()
        val jPanel2 = JPanel()
        val jButton1 = JButton()
        defaultCloseOperation = 2
        title = "Usuário da rede"
        jPanel1.border = BorderFactory.createEtchedBorder()
        jLabel1.font = Font("MS Sans Serif", 0, 10)
        jLabel1.text = "Usuário:"
        jtxtUsuario!!.addActionListener { jtxtUsuarioActionPerformed() }
        jLabel2.font = Font("MS Sans Serif", 0, 10)
        jLabel2.text = "Senha:"
        jPanel2.border = BorderFactory.createEtchedBorder()
        jPanel2.layout = AbsoluteLayout()
        jButton1.icon = ImageIcon(javaClass.getResource("/imagens/OK.gif"))
        jButton1.preferredSize = Dimension(70, 40)
        jButton1.addActionListener { jButton1ActionPerformed() }
        jPanel2.add(jButton1, AbsoluteConstraints(300, 10, -1, 30))
        val jPanel1Layout = GroupLayout(jPanel1)
        jPanel1.layout = jPanel1Layout
        jPanel1Layout.horizontalGroup = jPanel1Layout.createParallelGroup(1).add(jPanel2, -1, 380, 32767).add(
            jPanel1Layout.createSequentialGroup().add(54, 54, 54).add(
                jPanel1Layout.createParallelGroup(1).add(
                    jPanel1Layout.createSequentialGroup().add(jLabel1).add(12, 12, 12).add(
                        jtxtUsuario, -2, 220, -2
                    ) as GroupLayout.Group
                ).add(
                    jPanel1Layout.createSequentialGroup().add(jLabel2).add(16, 16, 16).add(
                        jtxtSenha, -2, 160, -2
                    ) as GroupLayout.Group
                ) as GroupLayout.Group
            ).addContainerGap(56, 32767) as GroupLayout.Group
        ) as GroupLayout.Group
        jPanel1Layout.verticalGroup = jPanel1Layout.createParallelGroup(1).add(
            2, jPanel1Layout.createSequentialGroup().add(21, 21, 21).add(
                jPanel1Layout.createParallelGroup(1).add(jLabel1).add(
                    jtxtUsuario, -2, -1, -2
                ) as GroupLayout.Group
            ).add(9, 9, 9).add(
                jPanel1Layout.createParallelGroup(1).add(
                    jPanel1Layout.createSequentialGroup()
                        .add(3, 3, 3)
                        .add(jLabel2, -2, 20, -2) as GroupLayout.Group
                ).add(
                    jtxtSenha, -2, -1, -2
                ) as GroupLayout.Group
            ).addPreferredGap(0, 21, 32767)
                .add(jPanel2, -2, 49, -2) as GroupLayout.Group
        ) as GroupLayout.Group
        val layout = GroupLayout(contentPane)
        contentPane.layout = layout
        layout.horizontalGroup = layout.createParallelGroup(1)
            .add(jPanel1, -1, -1, 32767) as GroupLayout.Group
        layout.verticalGroup = layout.createParallelGroup(1)
            .add(jPanel1, -2, -1, -2) as GroupLayout.Group
        size = Dimension(392, 175)
        setLocationRelativeTo(null)
    }

    private fun jButton1ActionPerformed() {
        val usuarioBean = instance
        usuarioBean!!.usuario = jtxtUsuario!!.text
        usuarioBean.pwd = String(jtxtSenha!!.password)
        isVisible = false
    }

    private fun jtxtUsuarioActionPerformed() {}
}