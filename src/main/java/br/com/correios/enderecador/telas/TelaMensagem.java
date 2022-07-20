package br.com.correios.enderecador.telas;

import br.com.correios.enderecador.bean.GlobalBean;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

public class TelaMensagem extends JDialog {
    private GlobalBean globalBean;

    private JCheckBox jchkMensagem;

    public TelaMensagem() {
        initComponents();
        configuracoesAdicionais();
    }

    private void configuracoesAdicionais() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dialogSize = getSize();
        if (dialogSize.height > screenSize.height)
            dialogSize.height = screenSize.height;
        if (dialogSize.width > screenSize.width)
            dialogSize.width = screenSize.width;
        setLocation((screenSize.width - dialogSize.width) / 2, (screenSize.height - dialogSize.height) / 2);
        this.globalBean = GlobalBean.getInstance();
    }

    private void initComponents() {
        JPanel jPanel1 = new JPanel();
        this.jchkMensagem = new JCheckBox();
        JLabel jLabel1 = new JLabel();
        JLabel jLabel2 = new JLabel();
        JLabel jLabel3 = new JLabel();
        JPanel jPanel2 = new JPanel();
        JButton jButton1 = new JButton();
        setDefaultCloseOperation(2);
        setTitle("Endereçador");
        setModal(true);
        setResizable(false);
        jPanel1.setBorder(BorderFactory.createEtchedBorder());
        jPanel1.setLayout((LayoutManager) new AbsoluteLayout());
        this.jchkMensagem.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        this.jchkMensagem.setText("Não exibir essa mensagem nessa sessão novamente.");
        jPanel1.add(this.jchkMensagem, new AbsoluteConstraints(150, 120, -1, -1));
        jLabel1.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
        jLabel1.setText("A T E N Ç Ã O:  Existe nova versão do Endereçador no site dos Correios.");
        jPanel1.add(jLabel1, new AbsoluteConstraints(30, 40, -1, -1));
        jLabel2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
        jLabel2.setText("Para obter acesse o endereço: http://www2.correios.com.br/enderecador");
        jPanel1.add(jLabel2, new AbsoluteConstraints(30, 60, -1, -1));
        jLabel3.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
        jLabel3.setText("E faça a baixa do arquivo de instalação e execute-o no seu computador.");
        jPanel1.add(jLabel3, new AbsoluteConstraints(30, 80, -1, -1));
        getContentPane().add(jPanel1, "Center");
        jPanel2.setBorder(BorderFactory.createEtchedBorder());
        jPanel2.setLayout(new BorderLayout());
        jButton1.setIcon(new ImageIcon(getClass().getResource("/imagens/OK.gif")));
        jButton1.setToolTipText("Ok");
        jButton1.setPreferredSize(new Dimension(70, 40));
        jButton1.addActionListener(TelaMensagem.this::jButton1ActionPerformed);
        jPanel2.add(jButton1, "East");
        getContentPane().add(jPanel2, "South");
        setSize(new Dimension(605, 246));
        setLocationRelativeTo(null);
    }

    private void jButton1ActionPerformed(ActionEvent evt) {
        if (this.jchkMensagem.isSelected())
            this.globalBean.setMostraMensagem("NAO");
        setVisible(false);
    }
}
