package br.com.correios.enderecador.telas;

import br.com.correios.enderecador.bean.ConfiguracaoBean;
import br.com.correios.enderecador.dao.ConfiguracaoDao;
import br.com.correios.enderecador.dao.DaoException;
import br.com.correios.enderecador.excecao.EnderecadorExcecao;
import br.com.correios.enderecador.util.Geral;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import org.apache.log4j.Logger;
import org.jdesktop.layout.GroupLayout;

public class TelaSobre extends JDialog {
  private static final Logger logger = Logger.getLogger(TelaSobre.class);

  private ConfiguracaoBean configuracaoBean;
  
  private JTabbedPane jTabbedPane1;

  private JLabel jlDirInstalacao;
  
  private JLabel jlJava;
  
  private JLabel jlJavaHome;
  
  private JLabel jlSistemaOperacional;
  
  private JLabel jlVM;
  
  private JLabel jlVersao;
  
  private JTextField jtxtChave;
  
  public TelaSobre(Frame parent, boolean modal) {
    super(parent, modal);
    initComponents();
    try {
      configuracoesAdicionais();
    } catch (Exception ex) {
      logger.error(ex.getMessage(), ex);
      JOptionPane.showMessageDialog(null, "Não foi possível configurar interface GUI com o sistema operacional", "Erro", JOptionPane.ERROR_MESSAGE);
    } 
  }
  
  private void configuracoesAdicionais() throws Exception {
    this.configuracaoBean = ConfiguracaoBean.getInstance();
    this.configuracaoBean.carregaVariaveis();
    if (!this.configuracaoBean.getChave().equals("") && !this.configuracaoBean.getChave().equals(""))
      this.jtxtChave.setText(this.configuracaoBean.getChave()); 
    if (!this.configuracaoBean.getVersao().equals(""))
      this.jlVersao.setText("Versão " + this.configuracaoBean.getVersao() + ", " + this.configuracaoBean.getBanco()); 
    this.jlSistemaOperacional.setText(System.getProperty("os.name") + " versão " + System.getProperty("os.version") + " ( " + System.getProperty("os.arch") + " )");
    this.jlJava.setText(System.getProperty("java.version"));
    this.jlVM.setText(System.getProperty("java.vm.name") + " versão " + System.getProperty("java.runtime.version"));
    this.jlJavaHome.setText(System.getProperty("java.home"));
    this.jlDirInstalacao.setText(System.getProperty("user.dir"));
    if (!this.configuracaoBean.getBanco().equals("DNEC"))
      this.jTabbedPane1.remove(1); 
  }
  
  private void initComponents() {
    JLabel jLabel10 = new JLabel();
    this.jTabbedPane1 = new JTabbedPane();
    JPanel jPanel1 = new JPanel();
    JLabel jLabel1 = new JLabel();
    JLabel jLabel2 = new JLabel();
    JLabel jLabel3 = new JLabel();
    JLabel jLabel4 = new JLabel();
    this.jlVersao = new JLabel();
    JLabel jLabel9 = new JLabel();
    JLabel jLabel11 = new JLabel();
    JPanel jPanel2 = new JPanel();
    JLabel jLabel5 = new JLabel();
    JLabel jLabel6 = new JLabel();
    JLabel jLabel7 = new JLabel();
    JLabel jLabel8 = new JLabel();
    this.jtxtChave = new JTextField();
    JPanel jPanel3 = new JPanel();
    this.jlJava = new JLabel();
    this.jlJavaHome = new JLabel();
    this.jlSistemaOperacional = new JLabel();
    JLabel jLabel16 = new JLabel();
    JLabel jLabel17 = new JLabel();
    JLabel jLabel12 = new JLabel();
    JLabel jLabel13 = new JLabel();
    this.jlDirInstalacao = new JLabel();
    JLabel jLabel14 = new JLabel();
    this.jlVM = new JLabel();
    JButton jbtnOk = new JButton();
    jLabel10.setText("jLabel10");
    setDefaultCloseOperation(2);
    setTitle("Endereçador Escritório");
    setResizable(false);
    jPanel1.setBorder(BorderFactory.createEtchedBorder());
    jLabel1.setHorizontalAlignment(0);
    jLabel1.setIcon(new ImageIcon(getClass().getResource("/imagens/logo_enderecador.gif")));
    jLabel1.setHorizontalTextPosition(0);
    jLabel2.setFont(new Font("MS Sans Serif", 0, 10));
    jLabel2.setText("Todos os direitos reservados, 2007");
    jLabel3.setFont(new Font("MS Sans Serif", 0, 13));
    jLabel3.setText("Endereçador Escritório");
    jLabel4.setFont(new Font("MS Sans Serif", 1, 13));
    jLabel4.setText("Empresa Brasileira de Correios e Telégrafos");
    this.jlVersao.setFont(new Font("MS Sans Serif", 1, 14));
    this.jlVersao.setText("Versão");
    jLabel9.setFont(new Font("Tahoma", 1, 11));
    jLabel9.setText("Sugestões e aperfeiçoamentos: ");
    jLabel11.setFont(new Font("Tahoma", 1, 11));
    jLabel11.setText("<html><head></head><body><a href=http://www.correios.com.br/servicos/falecomoscorreios/default.cfm>Fale com os Correios</a></body></html>");
    jLabel11.setToolTipText("");
    jLabel11.addMouseListener(new MouseAdapter() {
          public void mouseClicked(MouseEvent evt) {
            TelaSobre.this.jLabel11MouseClicked(evt);
          }
          
          public void mouseEntered(MouseEvent evt) {
            TelaSobre.this.jLabel11MouseEntered(evt);
          }
          
          public void mouseExited(MouseEvent evt) {
            TelaSobre.this.jLabel11MouseExited(evt);
          }
        });
    GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(1)
            .add(jPanel1Layout.createSequentialGroup()
                    .addContainerGap().add(jPanel1Layout.createParallelGroup(1)
                            .add(2, jLabel2)
                            .add(jPanel1Layout.createSequentialGroup()
                                    .add(jLabel3)
                                    .addPreferredGap(0, 189, 32767)
                                    .add(this.jlVersao))
                            .add(jLabel9).add(jLabel11))
                    .addContainerGap())
            .add(jPanel1Layout.createSequentialGroup()
                    .add(51, 51, 51)
                    .add(jLabel4).addContainerGap(59, 32767))
            .add(2, jPanel1Layout.createSequentialGroup()
                    .addContainerGap(88, 32767)
                    .add(jLabel1).add(77, 77, 77)));
    jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(1)
            .add(jPanel1Layout.createSequentialGroup()
                    .add(23, 23, 23)
                    .add(jLabel1)
                    .add(15, 15, 15)
                    .add(jLabel4)
                    .add(38, 38, 38)
                    .add(jPanel1Layout.createParallelGroup(3)
                            .add(jLabel3).add(this.jlVersao))
                    .addPreferredGap(0, 38, 32767)
                    .add(jLabel9)
                    .addPreferredGap(0)
                    .add(jLabel11)
                    .add(14, 14, 14)
                    .add(jLabel2)
                    .addContainerGap()));
    this.jTabbedPane1.addTab("Sobre", jPanel1);
    jPanel2.setBorder(BorderFactory.createEtchedBorder());
    jLabel5.setFont(new Font("Tahoma", Font.BOLD, 11));
    jLabel5.setText("A Chave de Registro possibilita o uso de todas as");
    jLabel6.setFont(new Font("Tahoma", Font.BOLD, 11));
    jLabel6.setText("funcionalidades do  Endereçador, mas não impede");
    jLabel7.setFont(new Font("Tahoma", Font.BOLD, 11));
    jLabel7.setText("a utilização do sistema.");
    jLabel8.setFont(new Font("Tahoma", Font.BOLD, 11));
    jLabel8.setText("Chave de registro:");
    GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(jPanel2Layout
            .createParallelGroup(1)
            .add(2, jPanel2Layout.createSequentialGroup()
                    .add(30, 30, 30)
                    .add(jPanel2Layout.createParallelGroup(1, false)
                            .add(jLabel8, -2, 313, -2)
                            .add(this.jtxtChave, -2, 329, -2)
                            .add(jLabel6, -1, -1, 32767)
                            .add(jLabel5, -1, 330, 32767)
                            .add(jLabel7, -1, -1, 32767))
                    .addContainerGap(30, 32767)));
    jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(1)
            .add(jPanel2Layout.createSequentialGroup()
                    .add(70, 70, 70)
                    .add(jLabel5).add(16, 16, 16)
                    .add(jLabel6).add(16, 16, 16)
                    .add(jLabel7).add(47, 47, 47)
                    .add(jLabel8).addPreferredGap(0)
                    .add(this.jtxtChave, -2, -1, -2)
                    .addContainerGap(93, 32767)));
    this.jTabbedPane1.addTab("Chave de registro", jPanel2);
    this.jlJava.setFont(new Font("Tahoma", Font.PLAIN, 10));
    this.jlJava.setText("jlJVM");
    this.jlJavaHome.setFont(new Font("Tahoma", Font.PLAIN, 10));
    this.jlJavaHome.setText("jlJavaHome");
    this.jlSistemaOperacional.setFont(new Font("Tahoma", Font.PLAIN, 10));
    this.jlSistemaOperacional.setText("jlSistemaOperacional");
    jLabel16.setFont(new Font("Tahoma", Font.BOLD, 11));
    jLabel16.setText("Java:");
    jLabel17.setFont(new Font("Tahoma", Font.BOLD, 11));
    jLabel17.setText("Java Home:");
    jLabel12.setFont(new Font("Tahoma", Font.BOLD, 11));
    jLabel12.setText("Sistema Operacional:");
    jLabel13.setFont(new Font("Tahoma", Font.BOLD, 11));
    jLabel13.setText("Diretório instalação:");
    this.jlDirInstalacao.setFont(new Font("Tahoma", Font.PLAIN, 10));
    this.jlDirInstalacao.setText("jlDirInstalacao");
    jLabel14.setFont(new Font("Tahoma", Font.BOLD, 11));
    jLabel14.setText("VM:");
    this.jlVM.setFont(new Font("Tahoma", Font.PLAIN, 10));
    this.jlVM.setText("jlVM");
    GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(1)
            .add(jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .add(jPanel3Layout.createParallelGroup(1)
                            .add(jPanel3Layout.createSequentialGroup()
                                    .add(jLabel17).add(57, 57, 57)
                                    .add(this.jlJavaHome, -1, 251, 32767))
                            .add(jPanel3Layout.createSequentialGroup().add(jLabel12)
                                    .add(4, 4, 4)
                                    .add(this.jlSistemaOperacional, -1, 251, 32767))
                            .add(jPanel3Layout.createSequentialGroup()
                                    .add(jPanel3Layout.createParallelGroup(1)
                                            .add(jLabel13)
                                            .add(jLabel14)
                                            .add(jLabel16))
                                    .add(9, 9, 9)
                                    .add(jPanel3Layout.createParallelGroup(1)
                                            .add(this.jlJava, -1, 251, 32767)
                                            .add(this.jlVM, -1, 251, 32767)
                                            .add(this.jlDirInstalacao, -1, 251, 32767))))
                    .addContainerGap()));
    jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(1)
            .add(jPanel3Layout.createSequentialGroup()
                    .add(44, 44, 44)
                    .add((jPanel3Layout.createParallelGroup(3)
                            .add(jLabel12)
                            .add(this.jlSistemaOperacional))
                            .add(27, 27, 27)
                            .add(jPanel3Layout.createParallelGroup(3)
                                    .add(jLabel13).add(this.jlDirInstalacao))
                            .add(30, 30, 30)
                            .add(jPanel3Layout.createParallelGroup(3)
                                    .add(jLabel14).add(this.jlVM))
                            .add(24, 24, 24)
                            .add(jPanel3Layout.createParallelGroup(3)
                                    .add(jLabel16).add(this.jlJava))
                            .add(32, 32, 32)
                            .add(jPanel3Layout.createParallelGroup(3)
                                    .add(jLabel17).add(this.jlJavaHome)))
                            .addContainerGap(101, 32767)));
    this.jTabbedPane1.addTab("Info. Sistema", jPanel3);
    jbtnOk.setText("Ok");
    jbtnOk.addActionListener(TelaSobre.this::jbtnOkActionPerformed);
    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(layout.createParallelGroup(1)
            .add(this.jTabbedPane1, -1, 399, 32767)
            .add(2, layout.createSequentialGroup()
                    .addContainerGap(169, 32767)
                    .add(jbtnOk, -2, 71, -2)
                    .add(159, 159, 159)));
    layout.setVerticalGroup(layout.createParallelGroup(1)
            .add(2, layout.createSequentialGroup()
                    .add(this.jTabbedPane1, -1, 353, 32767)
                    .addPreferredGap(0)
                    .add(jbtnOk)
                    .addContainerGap()));
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    setBounds((screenSize.width - 407) / 2, (screenSize.height - 420) / 2, 407, 420);
  }
  
  private void jLabel11MouseClicked(MouseEvent evt) {
    try {
      Geral.displayURL(ConfiguracaoBean.getInstance().getPaginaFaleConosco());
    } catch (EnderecadorExcecao ex) {
      logger.error(ex.getMessage(), ex);
      JOptionPane.showMessageDialog(this, "Não foi possível ativar seu browse, por favor entre no site: " + ConfiguracaoBean.getInstance().getPaginaFaleConosco(), "Endereçador", 2);
    } 
  }
  
  private void jLabel11MouseExited(MouseEvent evt) {
    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
  }
  
  private void jLabel11MouseEntered(MouseEvent evt) {
    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
  }
  
  private void jbtnOkActionPerformed(ActionEvent evt) {
    this.configuracaoBean.setChave(this.jtxtChave.getText());
    try {
      ConfiguracaoDao.getInstance().alterarConfiguracao(this.configuracaoBean);
    } catch (DaoException ex) {
      logger.error(ex.getMessage(), ex);
      JOptionPane.showMessageDialog(this, "Não foi possível gravar chave.", "Endereçador", 2);
    } 
    setVisible(false);
  }
}
