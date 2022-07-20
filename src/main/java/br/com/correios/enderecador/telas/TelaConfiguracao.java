package br.com.correios.enderecador.telas;

import br.com.correios.enderecador.bean.ConfiguracaoBean;
import br.com.correios.enderecador.dao.ConfiguracaoDao;
import br.com.correios.enderecador.dao.DaoException;
import br.com.correios.enderecador.excecao.EnderecadorExcecao;
import br.com.correios.enderecador.util.DocumentoPersonalizado;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.Document;

import org.apache.log4j.Logger;
import org.jdesktop.layout.GroupLayout;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

public class TelaConfiguracao extends JDialog {
    private static final Logger logger = Logger.getLogger(TelaConfiguracao.class);

    private ConfiguracaoBean configuracaoBean;

    private JButton jbntAjuda;

    private JCheckBox jchkProxy;

    private JTextField jtxtDominio;

    private JTextField jtxtEndereco;

    private JTextField jtxtPorta;

    public TelaConfiguracao(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        configuracaoAjuda();
        configuracoesAdicionais();
    }

    private void configuracoesAdicionais() {
        this.configuracaoBean = ConfiguracaoBean.getInstance();
        try {
            this.configuracaoBean.carregaVariaveis();
            if (this.configuracaoBean.getProxy().equals("") || this.configuracaoBean.getPorta().equals("")) {
                this.jchkProxy.setSelected(false);
                this.jtxtEndereco.setBackground(Color.lightGray);
                this.jtxtEndereco.setEnabled(false);
                this.jtxtPorta.setBackground(Color.lightGray);
                this.jtxtPorta.setEnabled(false);
                this.jtxtDominio.setBackground(Color.lightGray);
                this.jtxtDominio.setEnabled(false);
            } else {
                this.jchkProxy.setSelected(true);
                this.jtxtEndereco.setBackground(Color.white);
                this.jtxtEndereco.setEnabled(true);
                this.jtxtPorta.setBackground(Color.white);
                this.jtxtPorta.setEnabled(true);
                this.jtxtEndereco.setText(this.configuracaoBean.getProxy());
                this.jtxtPorta.setText(this.configuracaoBean.getPorta());
                this.jtxtDominio.setText(this.configuracaoBean.getDominio());
            }
        } catch (EnderecadorExcecao ex) {
            logger.error(ex.getMessage(), ex);
            JOptionPane.showMessageDialog(this, "Não foi possível configurar interface GUI com o sistema operacional", "Endereçador", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void configuracaoAjuda() {
        HelpSet hs = getHelpSet("ajuda/helpset.hs");
        HelpBroker hb = hs.createHelpBroker();
        hb.enableHelpKey(getRootPane(), "configurarProxy", hs);
        this.jbntAjuda.addActionListener(new CSH.DisplayHelpFromSource(hb));
    }

    public HelpSet getHelpSet(String helpsetfile) {
        HelpSet hs = null;
        ClassLoader cl = getClass().getClassLoader();
        try {
            URL hsURL = HelpSet.findHelpSet(cl, helpsetfile);
            hs = new HelpSet(null, hsURL);
        } catch (Exception ee) {
            logger.error("HelpSet: " + ee.getMessage());
            logger.error("HelpSet: " + helpsetfile + " not found");
        }
        return hs;
    }

    private void initComponents() {
        JPanel jPanel3 = new JPanel();
        this.jchkProxy = new JCheckBox();
        JLabel jLabel1 = new JLabel();
        this.jtxtEndereco = new JTextField();
        this.jtxtPorta = new JTextField();
        JLabel jLabel2 = new JLabel();
        this.jtxtDominio = new JTextField();
        JLabel jLabel3 = new JLabel();
        JPanel jPanel1 = new JPanel();
        JButton jbntOk = new JButton();
        this.jbntAjuda = new JButton();
        setDefaultCloseOperation(2);
        setTitle("Configuração Proxy");
        setModal(true);
        jPanel3.setBorder(BorderFactory.createEtchedBorder());
        this.jchkProxy.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        this.jchkProxy.setText("Usar um servidor Proxy");
        this.jchkProxy.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.jchkProxy.setMargin(new Insets(0, 0, 0, 0));
        this.jchkProxy.addActionListener(TelaConfiguracao.this::jchkProxyActionPerformed);
        jLabel1.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel1.setText("Endereço:");
        this.jtxtPorta.setDocument(new DocumentoPersonalizado(10, 1));
        jLabel2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel2.setText("Porta:");
        jLabel3.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel3.setText("Domínio:");
        jPanel1.setLayout(new AbsoluteLayout());
        jPanel1.setBorder(BorderFactory.createEtchedBorder());
        jbntOk.setIcon(new ImageIcon(getClass().getResource("/imagens/OK.gif")));
        jbntOk.setMaximumSize(new Dimension(63, 39));
        jbntOk.setMinimumSize(new Dimension(63, 39));
        jbntOk.addActionListener(TelaConfiguracao.this::jbntOkActionPerformed);
        jPanel1.add(jbntOk, new AbsoluteConstraints(290, 10, 60, 30));
        this.jbntAjuda.setIcon(new ImageIcon(getClass().getResource("/imagens/ajuda.gif")));
        jPanel1.add(this.jbntAjuda, new AbsoluteConstraints(10, 10, 30, -1));
        GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(1)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap().add(this.jchkProxy)
                .addContainerGap(230, 32767))
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(1)
                    .add(jLabel1)
                    .add(jLabel2)
                    .add(jLabel3))
                .add(20, 20, 20)
                .add(jPanel3Layout.createParallelGroup(1)
                    .add(jPanel3Layout.createParallelGroup(2, false)
                        .add(1, this.jtxtDominio)
                        .add(1, this.jtxtPorta, -2, 128, -2))
                    .add(this.jtxtEndereco, -2, 226, -2))
                .addContainerGap(58, 32767))
            .add(jPanel1, -1, 363, 32767));
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(1)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(this.jchkProxy).add(15, 15, 15)
                .add(jPanel3Layout.createParallelGroup(3)
                    .add(jLabel1).add(this.jtxtEndereco, -2, -1, -2))
                .add(15, 15, 15)
                .add(jPanel3Layout.createParallelGroup(3)
                    .add(this.jtxtPorta, -2, -1, -2)
                    .add(jLabel2)).add(16, 16, 16)
                .add(jPanel3Layout.createParallelGroup(3)
                    .add(this.jtxtDominio, -2, -1, -2)
                    .add(jLabel3))
                .addPreferredGap(0, 15, 32767)
                .add(jPanel1, -2, 50, -2)));
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(1)
            .add(jPanel3, -1, -1, 32767));
        layout.setVerticalGroup(layout.createParallelGroup(1)
            .add(jPanel3, -1, -1, 32767));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 375) / 2, (screenSize.height - 228) / 2, 375, 228);
    }

    private void jchkProxyActionPerformed(ActionEvent evt) {
        if (this.jchkProxy.isSelected()) {
            this.jtxtEndereco.setBackground(Color.white);
            this.jtxtEndereco.setEnabled(true);
            this.jtxtPorta.setBackground(Color.white);
            this.jtxtPorta.setEnabled(true);
            this.jtxtDominio.setBackground(Color.white);
            this.jtxtDominio.setEnabled(true);
        } else {
            this.jtxtEndereco.setBackground(Color.lightGray);
            this.jtxtEndereco.setEnabled(false);
            this.jtxtPorta.setBackground(Color.lightGray);
            this.jtxtPorta.setEnabled(false);
            this.jtxtDominio.setBackground(Color.lightGray);
            this.jtxtDominio.setEnabled(false);
            this.jtxtEndereco.setText("");
            this.jtxtPorta.setText("");
            this.jtxtDominio.setText("");
        }
    }

    private void jbntOkActionPerformed(ActionEvent evt) {
        if (this.jchkProxy.isSelected()) {
            if (this.jtxtEndereco.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(this, "O campo Endereço do proxy deve ser preenchido!", "Endereçador", JOptionPane.WARNING_MESSAGE);
                this.jtxtEndereco.requestFocus();
                return;
            }
            if (this.jtxtPorta.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(this, "O campo Porta deve ser preenchido!", "Endereçador", JOptionPane.WARNING_MESSAGE);
                this.jtxtPorta.requestFocus();
                return;
            }
            try {
                this.configuracaoBean.setProxy(this.jtxtEndereco.getText());
                this.configuracaoBean.setPorta(this.jtxtPorta.getText());
                this.configuracaoBean.setDominio(this.jtxtDominio.getText());
                ConfiguracaoDao.getInstance().alterarConfiguracao(this.configuracaoBean);
            } catch (DaoException ex) {
                JOptionPane.showMessageDialog(this, "Não foi possível gravar configurações de proxy.", "Enderecador", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            try {
                this.configuracaoBean.setProxy("");
                this.configuracaoBean.setPorta("");
                this.configuracaoBean.setDominio("");
                ConfiguracaoDao configuracaoDao = ConfiguracaoDao.getInstance();
                configuracaoDao.alterarConfiguracao(this.configuracaoBean);
            } catch (DaoException ex) {
                JOptionPane.showMessageDialog(this, "Não foi possível gravar configurações de proxy.", "Enderecador", JOptionPane.WARNING_MESSAGE);
            }
        }
        setVisible(false);
    }
}
