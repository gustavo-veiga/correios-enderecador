package br.com.correios.enderecador.telas;

import br.com.correios.enderecador.bean.UsuarioBean;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import org.jdesktop.layout.GroupLayout;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

public class TelaUsuario extends JDialog {
  private JButton jButton1;
  
  private JLabel jLabel1;
  
  private JLabel jLabel2;
  
  private JPanel jPanel1;
  
  private JPanel jPanel2;
  
  private JPasswordField jtxtSenha;
  
  private JTextField jtxtUsuario;
  
  public TelaUsuario(Frame parent, boolean modal) {
    super(parent, modal);
    initComponents();
  }
  
  private void initComponents() {
    this.jPanel1 = new JPanel();
    this.jLabel1 = new JLabel();
    this.jtxtUsuario = new JTextField();
    this.jLabel2 = new JLabel();
    this.jtxtSenha = new JPasswordField();
    this.jPanel2 = new JPanel();
    this.jButton1 = new JButton();
    setDefaultCloseOperation(2);
    setTitle("Usuário da rede");
    this.jPanel1.setBorder(BorderFactory.createEtchedBorder());
    this.jLabel1.setFont(new Font("MS Sans Serif", 0, 10));
    this.jLabel1.setText("Usuário:");
    this.jtxtUsuario.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            TelaUsuario.this.jtxtUsuarioActionPerformed(evt);
          }
        });
    this.jLabel2.setFont(new Font("MS Sans Serif", 0, 10));
    this.jLabel2.setText("Senha:");
    this.jPanel2.setBorder(BorderFactory.createEtchedBorder());
    this.jPanel2.setLayout((LayoutManager)new AbsoluteLayout());
    this.jButton1.setIcon(new ImageIcon(getClass().getResource("/imagens/OK.gif")));
    this.jButton1.setPreferredSize(new Dimension(70, 40));
    this.jButton1.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            TelaUsuario.this.jButton1ActionPerformed(evt);
          }
        });
    this.jPanel2.add(this.jButton1, new AbsoluteConstraints(300, 10, -1, 30));
    GroupLayout jPanel1Layout = new GroupLayout(this.jPanel1);
    this.jPanel1.setLayout((LayoutManager)jPanel1Layout);
    jPanel1Layout.setHorizontalGroup((GroupLayout.Group)jPanel1Layout.createParallelGroup(1).add(this.jPanel2, -1, 380, 32767).add((GroupLayout.Group)jPanel1Layout.createSequentialGroup().add(54, 54, 54).add((GroupLayout.Group)jPanel1Layout.createParallelGroup(1).add((GroupLayout.Group)jPanel1Layout.createSequentialGroup().add(this.jLabel1).add(12, 12, 12).add(this.jtxtUsuario, -2, 220, -2)).add((GroupLayout.Group)jPanel1Layout.createSequentialGroup().add(this.jLabel2).add(16, 16, 16).add(this.jtxtSenha, -2, 160, -2))).addContainerGap(56, 32767)));
    jPanel1Layout.setVerticalGroup((GroupLayout.Group)jPanel1Layout.createParallelGroup(1).add(2, (GroupLayout.Group)jPanel1Layout.createSequentialGroup().add(21, 21, 21).add((GroupLayout.Group)jPanel1Layout.createParallelGroup(1).add(this.jLabel1).add(this.jtxtUsuario, -2, -1, -2)).add(9, 9, 9).add((GroupLayout.Group)jPanel1Layout.createParallelGroup(1).add((GroupLayout.Group)jPanel1Layout.createSequentialGroup().add(3, 3, 3).add(this.jLabel2, -2, 20, -2)).add(this.jtxtSenha, -2, -1, -2)).addPreferredGap(0, 21, 32767).add(this.jPanel2, -2, 49, -2)));
    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout((LayoutManager)layout);
    layout.setHorizontalGroup((GroupLayout.Group)layout.createParallelGroup(1).add(this.jPanel1, -1, -1, 32767));
    layout.setVerticalGroup((GroupLayout.Group)layout.createParallelGroup(1).add(this.jPanel1, -2, -1, -2));
    setSize(new Dimension(392, 175));
    setLocationRelativeTo(null);
  }
  
  private void jButton1ActionPerformed(ActionEvent evt) {
    UsuarioBean usuarioBean = UsuarioBean.getInstance();
    usuarioBean.setUsuario(this.jtxtUsuario.getText());
    usuarioBean.setPwd(String.valueOf(this.jtxtSenha.getPassword()));
    setVisible(false);
  }
  
  private void jtxtUsuarioActionPerformed(ActionEvent evt) {}
}
