package br.com.correios.enderecador.telas;

import br.com.correios.enderecador.bean.DestinatarioBean;
import br.com.correios.enderecador.dao.DaoException;
import br.com.correios.enderecador.dao.DestinatarioDao;
import br.com.correios.enderecador.util.Geral;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListModel;

import org.apache.log4j.Logger;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

public class TelaPesquisarDestinatario extends JDialog {
    private static final Logger logger = Logger.getLogger(TelaPesquisarDestinatario.class);

    private final Vector<DestinatarioBean> vecDestinatario;

    private Vector<DestinatarioBean> vecDestinatarioRetorno;

    private DestinatarioBean destinatarioBean;

    private String ultimaConsulta;

    private JScrollPane jScrollPane1;

    private JList jlstDestinatario;

    private JTextField jtxtDestinatario;

    public TelaPesquisarDestinatario(Frame parent, boolean modal, Vector<DestinatarioBean> vecDestinatario) {
        super(parent, modal);
        this.vecDestinatarioRetorno = new Vector<>();
        this.vecDestinatario = new Vector<>();
        this.ultimaConsulta = "";
        initComponents();
        this.vecDestinatarioRetorno = vecDestinatario;
        carregaListaDestinatario();
    }

    private void carregaListaDestinatario() {
        try {
            ArrayList<DestinatarioBean> arrayDestinatario = DestinatarioDao.getInstance().recuperaDestinatario("");
            this.vecDestinatario.addAll(arrayDestinatario);
            this.jlstDestinatario.setListData(this.vecDestinatario);
        } catch (DaoException e) {
            logger.error(e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Não foi possivel carregar relação de destinatários", "Endereçador ", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void initComponents() {
        JToolBar jToolBar1 = new JToolBar();
        JButton jbtConfirmar = new JButton();
        JButton jbtPesquisar = new JButton();
        JButton jbtSair = new JButton();
        JPanel jPanel1 = new JPanel();
        JLabel jLabel1 = new JLabel();
        this.jtxtDestinatario = new JTextField();
        this.jScrollPane1 = new JScrollPane();
        this.jlstDestinatario = new JList<>();
        setDefaultCloseOperation(2);
        setTitle("Selecionar destinatário");
        setResizable(false);
        jToolBar1.setBorder(BorderFactory.createEtchedBorder());
        jbtConfirmar.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        jbtConfirmar.setIcon(new ImageIcon(getClass().getResource("/imagens/OK.gif")));
        jbtConfirmar.setText("Confirmar");
        jbtConfirmar.setHorizontalTextPosition(0);
        jbtConfirmar.setMaximumSize(new Dimension(90, 60));
        jbtConfirmar.setVerticalTextPosition(3);
        jbtConfirmar.addActionListener(TelaPesquisarDestinatario.this::jbtConfirmarActionPerformed);
        jToolBar1.add(jbtConfirmar);
        jbtPesquisar.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        jbtPesquisar.setIcon(new ImageIcon(getClass().getResource("/imagens/binoculo.gif")));
        jbtPesquisar.setText("Pesquisar");
        jbtPesquisar.setHorizontalTextPosition(0);
        jbtPesquisar.setMaximumSize(new Dimension(90, 60));
        jbtPesquisar.setVerticalTextPosition(3);
        jbtPesquisar.addActionListener(TelaPesquisarDestinatario.this::jbtPesquisarActionPerformed);
        jToolBar1.add(jbtPesquisar);
        jbtSair.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        jbtSair.setIcon(new ImageIcon(getClass().getResource("/imagens/sair.gif")));
        jbtSair.setText("Sair");
        jbtSair.setHorizontalTextPosition(0);
        jbtSair.setMaximumSize(new Dimension(90, 60));
        jbtSair.setVerticalTextPosition(3);
        jbtSair.addActionListener(TelaPesquisarDestinatario.this::jbtSairActionPerformed);
        jToolBar1.add(jbtSair);
        getContentPane().add(jToolBar1, "North");
        jPanel1.setLayout(new AbsoluteLayout());
        jPanel1.setBorder(BorderFactory.createEtchedBorder());
        jLabel1.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel1.setText("Destinatário:");
        jPanel1.add(jLabel1, new AbsoluteConstraints(20, 23, -1, -1));
        jPanel1.add(this.jtxtDestinatario, new AbsoluteConstraints(90, 20, 350, -1));
        this.jScrollPane1.setViewportView(this.jlstDestinatario);
        jPanel1.add(this.jScrollPane1, new AbsoluteConstraints(4, 52, 582, 210));
        getContentPane().add(jPanel1, "Center");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 598) / 2, (screenSize.height - 349) / 2, 598, 349);
    }

    private void jbtSairActionPerformed(ActionEvent evt) {
        setVisible(false);
    }

    private void jbtPesquisarActionPerformed(ActionEvent evt) {
        try {
            String nomeDestinatario = this.jtxtDestinatario.getText().trim();
            int index = -1;
            if (this.ultimaConsulta.equalsIgnoreCase(nomeDestinatario)) {
                index = this.jlstDestinatario.getSelectedIndex();
            } else {
                this.ultimaConsulta = nomeDestinatario;
            }
            for (int i = index + 1; i < this.vecDestinatario.size(); i++) {
                this.destinatarioBean = this.vecDestinatario.get(i);
                String nomeDestinatarioTabela = this.destinatarioBean.getNome();
                if (nomeDestinatarioTabela.toUpperCase().contains(nomeDestinatario.toUpperCase())) {
                    index = i;
                    break;
                }
            }
            if (index == -1) {
                JOptionPane.showMessageDialog(this, "Destinatário não encontrado!", "Endereçador", JOptionPane.INFORMATION_MESSAGE);
            } else {
                this.jlstDestinatario.setSelectedIndex(index);
                int tamanhoJscrool = this.jScrollPane1.getVerticalScrollBar().getMaximum();
                int value = 0;
                value = index * tamanhoJscrool / this.vecDestinatario.size();
                this.jScrollPane1.getVerticalScrollBar().setValue(value);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void jbtConfirmarActionPerformed(ActionEvent evt) {
        if (this.jlstDestinatario.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(this, "Não existe nenhum destinatário selecionado", "Endereçador ECT", 1);
            return;
        }
        ListModel<DestinatarioBean> modelDestinatario = this.jlstDestinatario.getModel();
        Geral geral = null;
        int[] arrayIndex = this.jlstDestinatario.getSelectedIndices();
        this.destinatarioBean = DestinatarioBean.getInstance();
        for (int index : arrayIndex) {
            this.destinatarioBean = modelDestinatario.getElementAt(index);
            if (!Geral.verificaExistencia(this.destinatarioBean, this.vecDestinatarioRetorno))
                this.vecDestinatarioRetorno.add(this.destinatarioBean);
        }
        Geral.ordenarVetor(this.vecDestinatarioRetorno, this.destinatarioBean);
        setVisible(false);
    }
}
