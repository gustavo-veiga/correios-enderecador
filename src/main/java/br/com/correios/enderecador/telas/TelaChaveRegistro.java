package br.com.correios.enderecador.telas;

import br.com.correios.enderecador.bean.ConfiguracaoBean;
import br.com.correios.enderecador.dao.ConfiguracaoDao;
import br.com.correios.enderecador.dao.DaoException;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.jdesktop.layout.GroupLayout;

public class TelaChaveRegistro extends JDialog {
    private static final Logger logger = Logger.getLogger(TelaChaveRegistro.class);

    private ConfiguracaoBean configuracaoBean;

    private JTextField jtxtChave;

    public TelaChaveRegistro(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        configuracoesAdicionais();
    }

    private void configuracoesAdicionais() {
        this.configuracaoBean = ConfiguracaoBean.getInstance();
        setTitle("Endereçador Escritório v" + this.configuracaoBean.getVersao());
    }

    private void initComponents() {
        JLabel jLabel5 = new JLabel();
        JLabel jLabel6 = new JLabel();
        JLabel jLabel7 = new JLabel();
        this.jtxtChave = new JTextField();
        JLabel jLabel8 = new JLabel();
        JButton jbtnOk = new JButton();
        JLabel jLabel1 = new JLabel();
        JLabel jLabel4 = new JLabel();
        setDefaultCloseOperation(2);
        setTitle("Endereçador Escritório");
        setModal(true);
        setResizable(false);
        jLabel5.setFont(new Font("Arial", Font.PLAIN, 11));
        jLabel5.setText("A Chave de Registro possibilita o uso de todas as");
        jLabel6.setFont(new Font("Arial", Font.PLAIN, 11));
        jLabel6.setText("funcionalidades do  Endereçador, mas não impede");
        jLabel7.setFont(new Font("Arial", Font.PLAIN, 11));
        jLabel7.setText("a utilização do sistema.");
        jLabel8.setFont(new Font("Arial", Font.PLAIN, 11));
        jLabel8.setText("Chave de registro:");
        jbtnOk.setText("Clique para Prosseguir");
        jbtnOk.addActionListener(TelaChaveRegistro.this::jbtnOkActionPerformed);
        jLabel1.setIcon(new ImageIcon(getClass().getResource("/imagens/logo_enderecador.gif")));
        jLabel4.setFont(new Font("Arial", Font.BOLD, 13));
        jLabel4.setHorizontalAlignment(0);
        jLabel4.setText("Empresa Brasileira de Correios e Telégrafos");
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(1)
            .add(jLabel4, -1, 332, 32767)
            .add(2, layout.createSequentialGroup()
                .addContainerGap(37, 32767)
                .add(layout.createParallelGroup(1)
                    .add(layout.createSequentialGroup()
                        .add(jLabel7)
                        .addContainerGap())
                    .add(2, layout.createSequentialGroup()
                        .add(layout.createParallelGroup(1)
                            .add(jLabel8, -2, 98, -2)
                            .add(this.jtxtChave, -1, 268, 32767)
                            .add(jLabel5, -1, 268, 32767)
                            .add(jLabel6, -1, 268, 32767))
                        .add(27, 27, 27))))
            .add(layout.createSequentialGroup().add(51, 51, 51)
                .add(jLabel1).addContainerGap(56, 32767))
            .add(layout.createSequentialGroup()
                .add(95, 95, 95)
                .add(jbtnOk).addContainerGap(96, 32767)));
        layout.setVerticalGroup(layout.createParallelGroup(1)
            .add(2, layout.createSequentialGroup()
                .addContainerGap().add(jLabel1, -2, 100, -2)
                .add(21, 21, 21)
                .add(jLabel4).add(18, 18, 18)
                .add(jLabel5).addPreferredGap(0)
                .add(jLabel6).addPreferredGap(0)
                .add(jLabel7).addPreferredGap(0, 57, 32767)
                .add(jLabel8).addPreferredGap(0)
                .add(this.jtxtChave, -2, -1, -2)
                .add(34, 34, 34).add(jbtnOk)
                .add(21, 21, 21)));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 340) / 2, (screenSize.height - 421) / 2, 340, 421);
    }

    private void jbtnOkActionPerformed(ActionEvent evt) {
        try {
            if (!this.jtxtChave.getText().trim().equals("")) {
                this.configuracaoBean.setChave(this.jtxtChave.getText());
                ConfiguracaoDao.getInstance().alterarConfiguracao(this.configuracaoBean);
            }
        } catch (DaoException ex) {
            JOptionPane.showMessageDialog(this, "Não foi possível gravar chave.", "Enderecador", 2);
            logger.error(ex.getMessage(), (Throwable) ex);
        }
        setVisible(false);
    }
}
