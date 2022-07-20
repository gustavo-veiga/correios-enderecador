package br.com.correios.enderecador.telas;

import br.com.correios.enderecador.bean.DestinatarioBean;
import br.com.correios.enderecador.bean.GrupoBean;
import br.com.correios.enderecador.dao.DaoException;
import br.com.correios.enderecador.dao.GrupoDao;
import br.com.correios.enderecador.dao.GrupoDestinatarioDao;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;
import org.jdesktop.layout.GroupLayout;

public class TelaGrupo extends JInternalFrame {
    private static final Logger logger = Logger.getLogger(TelaGrupo.class);

    private final Vector<GrupoBean> vecTelEndGrupo = new Vector<>();

    private final Vector<DestinatarioBean> vecTelEndDestinatarioGrupo = new Vector<>();

    private final Frame frmParent;

    private static TelaGrupo instance;

    private String ultimaConsulta = "";

    private JScrollPane jScrollPane1;

    private JTable jTDestinatario;

    private JList jlstTelEndGrupo;

    private JTextField jtxtGrupo;

    private TelaGrupo(Frame parent) {
        this.frmParent = parent;
        initComponents();
    }

    public static TelaGrupo getInstance(Frame parent) {
        if (instance == null)
            instance = new TelaGrupo(parent);
        instance.recuperarListaGrupo();
        return instance;
    }

    private void initComponents() {
        JToolBar jToolBar1 = new JToolBar();
        JButton jbtNovo = new JButton();
        JButton jbtEditar = new JButton();
        JButton jbtPesquisar = new JButton();
        JButton jbtExcluir = new JButton();
        JPanel jPanel1 = new JPanel();
        JLabel jLabel1 = new JLabel();
        this.jtxtGrupo = new JTextField();
        JLabel jLabel2 = new JLabel();
        this.jScrollPane1 = new JScrollPane();
        this.jlstTelEndGrupo = new JList<>();
        JScrollPane jScrollPane2 = new JScrollPane();
        this.jTDestinatario = new JTable();
        JLabel jLabel3 = new JLabel();
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Cadastro de Grupos");
        jToolBar1.setBorder(BorderFactory.createEtchedBorder());
        jToolBar1.setPreferredSize(new Dimension(100, 59));
        jbtNovo.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        jbtNovo.setIcon(new ImageIcon(getClass().getResource("/imagens/usuarios.gif")));
        jbtNovo.setText("Novo grupo");
        jbtNovo.setHorizontalTextPosition(0);
        jbtNovo.setMaximumSize(new Dimension(90, 60));
        jbtNovo.setVerticalTextPosition(3);
        jbtNovo.addActionListener(TelaGrupo.this::jbtNovoActionPerformed);
        jToolBar1.add(jbtNovo);
        jbtEditar.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        jbtEditar.setIcon(new ImageIcon(getClass().getResource("/imagens/editar.gif")));
        jbtEditar.setText("Editar");
        jbtEditar.setHorizontalTextPosition(0);
        jbtEditar.setMaximumSize(new Dimension(90, 60));
        jbtEditar.setVerticalTextPosition(3);
        jbtEditar.addActionListener(TelaGrupo.this::jbtEditarActionPerformed);
        jToolBar1.add(jbtEditar);
        jbtPesquisar.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        jbtPesquisar.setIcon(new ImageIcon(getClass().getResource("/imagens/binoculo.gif")));
        jbtPesquisar.setText("Pesquisar");
        jbtPesquisar.setHorizontalTextPosition(0);
        jbtPesquisar.setMaximumSize(new Dimension(90, 60));
        jbtPesquisar.setVerticalTextPosition(3);
        jbtPesquisar.addActionListener(TelaGrupo.this::jbtPesquisarActionPerformed);
        jToolBar1.add(jbtPesquisar);
        jbtExcluir.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        jbtExcluir.setIcon(new ImageIcon(getClass().getResource("/imagens/TRASH.gif")));
        jbtExcluir.setText("Excluir");
        jbtExcluir.setHorizontalTextPosition(0);
        jbtExcluir.setMaximumSize(new Dimension(90, 60));
        jbtExcluir.setVerticalTextPosition(3);
        jbtExcluir.addActionListener(TelaGrupo.this::jbtExcluirActionPerformed);
        jToolBar1.add(jbtExcluir);
        jPanel1.setBorder(BorderFactory.createEtchedBorder());
        jLabel1.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel1.setText("Procurar por:");
        jLabel2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel2.setText("Grupos:");
        this.jlstTelEndGrupo.addListSelectionListener(TelaGrupo.this::jlstTelEndGrupoValueChanged);
        this.jScrollPane1.setViewportView(this.jlstTelEndGrupo);
        this.jTDestinatario.setModel(new DefaultTableModel(new Object[][]{{null, null, null, null}, {null, null, null, null}, {null, null, null, null}, {null, null, null, null}}, (Object[]) new String[]{"Title 1", "Title 2", "Title 3", "Title 4"}));
        this.jTDestinatario.setModel(new DestinatarioTableModel());
        jScrollPane2.setViewportView(this.jTDestinatario);
        jLabel3.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel3.setText("Destinatários do grupo:");
        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(1)
            .add(2, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(2)
                    .add(1, jScrollPane2, -1, 639, 32767)
                    .add(1, this.jScrollPane1, -1, 639, 32767)
                    .add(1, jPanel1Layout.createSequentialGroup()
                        .add(jLabel1)
                        .addPreferredGap(0)
                        .add(this.jtxtGrupo, -2, 288, -2)
                        .addPreferredGap(0, 286, 32767))
                    .add(1, jLabel2)
                    .add(1, jLabel3))
                .addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(1)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(3)
                    .add(jLabel1)
                    .add(this.jtxtGrupo, -2, -1, -2))
                .addPreferredGap(0)
                .add(jLabel2).addPreferredGap(0)
                .add(this.jScrollPane1, -2, 77, -2)
                .addPreferredGap(0)
                .add(jLabel3)
                .addPreferredGap(0)
                .add(jScrollPane2, -1, 184, 32767)
                .addContainerGap()));
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(1)
            .add(jPanel1, -1, -1, 32767)
            .add(jToolBar1, -1, 663, 32767));
        layout.setVerticalGroup(layout.createParallelGroup(1)
            .add(layout.createSequentialGroup()
                .add(jToolBar1, -2, 59, -2)
                .addPreferredGap(0)
                .add(jPanel1, -1, -1, 32767)));
        pack();
    }

    private void jbtExcluirActionPerformed(ActionEvent evt) {
        int linhaSelecionada = this.jlstTelEndGrupo.getLeadSelectionIndex();
        if (this.jlstTelEndGrupo.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(this.frmParent, "Não existe nenhum grupo selecionado!", "Endereçador", JOptionPane.WARNING_MESSAGE);
        } else {
            GrupoBean grupoBean = null;
            this.jlstTelEndGrupo.clearSelection();
            String[] options = {"Sim", "Não"};
            int resp = JOptionPane.showOptionDialog(this.frmParent, "Tem certeza que deseja excluir este grupo juntamente com todos os seus destinatários?", "Endereçador", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
            if (resp == 0) {
                grupoBean = this.vecTelEndGrupo.get(linhaSelecionada);
                try {
                    GrupoDestinatarioDao.getInstance().excluirGrupoDestinatario(grupoBean.getNumeroGrupo());
                    GrupoDao.getInstance().excluirGrupo(grupoBean.getNumeroGrupo());
                    recuperarListaGrupo();
                    this.jtxtGrupo.setText("");
                    JOptionPane.showMessageDialog(this.frmParent, "Grupo e destinatários excluídos com sucesso!", "Endereçador", JOptionPane.INFORMATION_MESSAGE);
                } catch (DaoException ex) {
                    logger.error(ex.getMessage(), ex);
                    JOptionPane.showMessageDialog(this.frmParent, "Não foi possivel realizar exclusão!", "Endereçador", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }

    private void jbtEditarActionPerformed(ActionEvent evt) {
        if (this.jlstTelEndGrupo.getLeadSelectionIndex() != -1) {
            GrupoBean grupoBean = this.vecTelEndGrupo.get(this.jlstTelEndGrupo.getLeadSelectionIndex());
            TelaEditarGrupo telaEditarGrupo = new TelaEditarGrupo(this.frmParent, true, grupoBean);
            telaEditarGrupo.setVisible(true);
            recuperarListaGrupo();
        } else {
            JOptionPane.showMessageDialog(this.frmParent, "Não existe nenhum grupo selecionado!", "Endereçador", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void jbtNovoActionPerformed(ActionEvent evt) {
        TelaEditarGrupo telaEditarGrupo = new TelaEditarGrupo(this.frmParent, true);
        telaEditarGrupo.setVisible(true);
        recuperarListaGrupo();
    }

    private void jlstTelEndGrupoValueChanged(ListSelectionEvent evt) {
        this.vecTelEndDestinatarioGrupo.removeAllElements();
        if (this.jlstTelEndGrupo.getLeadSelectionIndex() != -1) {
            GrupoBean grupoBean = this.vecTelEndGrupo.get(this.jlstTelEndGrupo.getLeadSelectionIndex());
            recuperarDestinatariosDoGrupo(grupoBean.getNumeroGrupo());
        }
    }

    private void recuperarDestinatariosDoGrupo(String nuGrupo) {
        DestinatarioBean destinatarioSort = new DestinatarioBean();
        DestinatarioTableModel model = null;
        try {
            this.vecTelEndDestinatarioGrupo.removeAllElements();
            ArrayList<DestinatarioBean> arrayGrupoDestinatario = GrupoDestinatarioDao.getInstance().recuperaGrupoDestinatario(nuGrupo);
            this.vecTelEndDestinatarioGrupo.addAll(arrayGrupoDestinatario);
            arrayGrupoDestinatario.sort(destinatarioSort);
            model = (DestinatarioTableModel) this.jTDestinatario.getModel();
            model.setDestinatario(arrayGrupoDestinatario);
        } catch (DaoException e) {
            logger.error(e.getMessage(), e);
            JOptionPane.showMessageDialog(this.frmParent, "Não foi possivel carregar relação de destinatários do grupo", "Endereçador ECT", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void recuperarListaGrupo() {
        DestinatarioTableModel model;
        try {
            this.vecTelEndGrupo.removeAllElements();
            ArrayList<GrupoBean> arrayGrupo = GrupoDao.getInstance().recuperaGrupo("");
            GrupoBean grupoBean;
            this.vecTelEndGrupo.addAll(arrayGrupo);
            if (this.vecTelEndGrupo.size() >= 1) {
                grupoBean = this.vecTelEndGrupo.get(0);
                recuperarDestinatariosDoGrupo(grupoBean.getNumeroGrupo());
                this.jlstTelEndGrupo.setListData(this.vecTelEndGrupo);
            } else {
                this.jlstTelEndGrupo.setListData(this.vecTelEndGrupo);
                this.vecTelEndDestinatarioGrupo.removeAllElements();
                model = (DestinatarioTableModel) this.jTDestinatario.getModel();
                model.setDestinatario(arrayGrupo);
            }
        } catch (DaoException e) {
            logger.error(e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Não foi possivel carregar relação de grupos", "Endereçador ECT", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void jbtPesquisarActionPerformed(ActionEvent evt) {
        GrupoBean grupoBean = null;
        String nomeGrupo = this.jtxtGrupo.getText().trim();
        int index = -1;
        if (this.ultimaConsulta.equalsIgnoreCase(nomeGrupo)) {
            index = this.jlstTelEndGrupo.getSelectedIndex();
        } else {
            this.ultimaConsulta = nomeGrupo;
        }
        for (int i = index + 1; i < this.vecTelEndGrupo.size(); i++) {
            grupoBean = this.vecTelEndGrupo.get(i);
            String nomeGrupoLista = grupoBean.getDescricaoGrupo();
            if (nomeGrupoLista.toUpperCase().contains(nomeGrupo.toUpperCase())) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Grupo não encontrado!", "Endereçador", JOptionPane.INFORMATION_MESSAGE);
        } else {
            this.jlstTelEndGrupo.setSelectedIndex(index);
            int tamanhoJscrool = this.jScrollPane1.getVerticalScrollBar().getMaximum();
            int value = 0;
            value = index * tamanhoJscrool / this.vecTelEndGrupo.size();
            this.jScrollPane1.getVerticalScrollBar().setValue(value);
        }
    }
}
