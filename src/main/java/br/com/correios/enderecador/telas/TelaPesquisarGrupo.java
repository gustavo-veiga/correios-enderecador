package br.com.correios.enderecador.telas;

import br.com.correios.enderecador.bean.DestinatarioBean;
import br.com.correios.enderecador.bean.GrupoBean;
import br.com.correios.enderecador.dao.DaoException;
import br.com.correios.enderecador.dao.DestinatarioDao;
import br.com.correios.enderecador.dao.GrupoDao;
import br.com.correios.enderecador.util.Geral;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.LayoutManager;
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

public class TelaPesquisarGrupo extends JDialog {
    private static final Logger logger = Logger.getLogger(TelaPesquisarGrupo.class);

    private DestinatarioBean destinatarioBean;

    private Vector<DestinatarioBean> vecDestinatarioRetorno;

    private final Vector<GrupoBean> vecDestinatarioGrupo;

    private String ultimaConsulta;

    private JScrollPane jScrollPane1;

    private JList jlstGrupo;

    private JTextField jtxtGrupo;

    public TelaPesquisarGrupo(Frame parent, boolean modal, Vector<DestinatarioBean> vecDestinatario) {
        super(parent, modal);
        this.destinatarioBean = null;
        this.vecDestinatarioRetorno = new Vector<>();
        this.vecDestinatarioGrupo = new Vector<>();
        this.ultimaConsulta = "";
        initComponents();
        this.vecDestinatarioRetorno = vecDestinatario;
        carregaListaGrupo();
    }

    private void carregaListaGrupo() {
        try {
            ArrayList<GrupoBean> arrayGrupo = GrupoDao.getInstance().recuperaGrupo("");
            this.vecDestinatarioGrupo.addAll(arrayGrupo);
            this.jlstGrupo.setListData(this.vecDestinatarioGrupo);
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
        this.jtxtGrupo = new JTextField();
        this.jScrollPane1 = new JScrollPane();
        this.jlstGrupo = new JList<>();
        setDefaultCloseOperation(2);
        setTitle("Selecionar grupo");
        setResizable(false);
        jToolBar1.setBorder(BorderFactory.createEtchedBorder());
        jbtConfirmar.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        jbtConfirmar.setIcon(new ImageIcon(getClass().getResource("/imagens/OK.gif")));
        jbtConfirmar.setText("Confirmar");
        jbtConfirmar.setHorizontalTextPosition(0);
        jbtConfirmar.setMaximumSize(new Dimension(90, 60));
        jbtConfirmar.setVerticalTextPosition(3);
        jbtConfirmar.addActionListener(TelaPesquisarGrupo.this::jbtConfirmarActionPerformed);
        jToolBar1.add(jbtConfirmar);
        jbtPesquisar.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        jbtPesquisar.setIcon(new ImageIcon(getClass().getResource("/imagens/binoculo.gif")));
        jbtPesquisar.setText("Pesquisar");
        jbtPesquisar.setHorizontalTextPosition(0);
        jbtPesquisar.setMaximumSize(new Dimension(90, 60));
        jbtPesquisar.setVerticalTextPosition(3);
        jbtPesquisar.addActionListener(TelaPesquisarGrupo.this::jbtPesquisarActionPerformed);
        jToolBar1.add(jbtPesquisar);
        jbtSair.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        jbtSair.setIcon(new ImageIcon(getClass().getResource("/imagens/sair.gif")));
        jbtSair.setText("Sair");
        jbtSair.setHorizontalTextPosition(0);
        jbtSair.setMaximumSize(new Dimension(90, 60));
        jbtSair.setPreferredSize(new Dimension(51, 27));
        jbtSair.setVerticalTextPosition(3);
        jbtSair.addActionListener(TelaPesquisarGrupo.this::jbtSairActionPerformed);
        jToolBar1.add(jbtSair);
        getContentPane().add(jToolBar1, "North");
        jPanel1.setBorder(BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new AbsoluteLayout());
        jLabel1.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel1.setText("Grupo:");
        jPanel1.add(jLabel1, new AbsoluteConstraints(20, 23, -1, -1));
        jPanel1.add(this.jtxtGrupo, new AbsoluteConstraints(60, 20, 320, -1));
        this.jScrollPane1.setViewportView(this.jlstGrupo);
        jPanel1.add(this.jScrollPane1, new AbsoluteConstraints(4, 52, 582, 210));
        getContentPane().add(jPanel1, "Center");
        setSize(new Dimension(598, 349));
        setLocationRelativeTo(null);
    }

    private void jbtSairActionPerformed(ActionEvent evt) {
        setVisible(false);
    }

    private void jbtPesquisarActionPerformed(ActionEvent evt) {
        String nomeGrupo = this.jtxtGrupo.getText().trim();
        int index = -1;
        if (this.ultimaConsulta.equalsIgnoreCase(nomeGrupo)) {
            index = this.jlstGrupo.getSelectedIndex();
        } else {
            this.ultimaConsulta = nomeGrupo;
        }
        for (int i = index + 1; i < this.vecDestinatarioGrupo.size(); i++) {
            GrupoBean grupoBean = this.vecDestinatarioGrupo.get(i);
            String nomeGrupoTabela = grupoBean.getDescricaoGrupo();
            if (nomeGrupoTabela.toUpperCase().contains(nomeGrupo.toUpperCase())) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Grupo não encontrado!", "Endereçador", JOptionPane.INFORMATION_MESSAGE);
        } else {
            this.jlstGrupo.setSelectedIndex(index);
            int tamanhoJscrool = this.jScrollPane1.getVerticalScrollBar().getMaximum();
            int value = 0;
            value = index * tamanhoJscrool / this.vecDestinatarioGrupo.size();
            this.jScrollPane1.getVerticalScrollBar().setValue(value);
        }
    }

    private void jbtConfirmarActionPerformed(ActionEvent evt) {
        Geral geral = null;
        try {
            if (this.jlstGrupo.getSelectedIndex() != -1) {
                ListModel<GrupoBean> modelGrupo = this.jlstGrupo.getModel();
                int[] arrayIndex = this.jlstGrupo.getSelectedIndices();
                for (int index : arrayIndex) {
                    GrupoBean grupoBean = modelGrupo.getElementAt(index);
                    ArrayList<DestinatarioBean> arrayDestinatarioGrupo = DestinatarioDao.getInstance().recuperaDestinatarioPorGrupo(grupoBean.getNumeroGrupo());
                    for (DestinatarioBean bean : arrayDestinatarioGrupo) {
                        this.destinatarioBean = bean;
                        if (!Geral.verificaExistencia(this.destinatarioBean, this.vecDestinatarioRetorno))
                            this.vecDestinatarioRetorno.add(this.destinatarioBean);
                    }
                }
                Geral.ordenarVetor(this.vecDestinatarioRetorno, this.destinatarioBean);
                setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "Não existe nenhum grupo selecionado ", "Endereçador ECT", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (DaoException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
