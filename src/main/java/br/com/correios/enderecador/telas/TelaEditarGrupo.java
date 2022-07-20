package br.com.correios.enderecador.telas;

import br.com.correios.enderecador.bean.DestinatarioBean;
import br.com.correios.enderecador.bean.GrupoBean;
import br.com.correios.enderecador.bean.GrupoDestinatarioBean;
import br.com.correios.enderecador.dao.DaoException;
import br.com.correios.enderecador.dao.DestinatarioDao;
import br.com.correios.enderecador.dao.GrupoDao;
import br.com.correios.enderecador.dao.GrupoDestinatarioDao;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;
import org.jdesktop.layout.GroupLayout;

public class TelaEditarGrupo extends JDialog {
    private static final Logger logger = Logger.getLogger(TelaEditarGrupo.class);

    private boolean blnIncluir;

    private String nuGrupo;

    private Vector<DestinatarioBean> vecDestinatario;

    private Vector<DestinatarioBean> vecDestinatarioGrupo;

    private JTable jTDestinatario;

    private JTable jTDestinatarioGrupo;

    private JTextField jtxtGrupo;

    public TelaEditarGrupo(Frame parent, boolean modal) {
        super(parent, modal);
        this.nuGrupo = "";
        this.vecDestinatario = new Vector<>();
        this.vecDestinatarioGrupo = new Vector<>();
        initComponents();
        this.blnIncluir = true;
        configuracoesAdicionais();
        carregaListaDestinatario("");
    }

    public TelaEditarGrupo(Frame parent, boolean modal, GrupoBean grupo) {
        super(parent, modal);
        this.nuGrupo = "";
        this.vecDestinatario = new Vector<>();
        this.vecDestinatarioGrupo = new Vector<>();
        initComponents();
        this.blnIncluir = false;
        configuracoesAdicionais();
        carregaListaDestinatario(grupo.getNumeroGrupo());
        this.jtxtGrupo.setText(grupo.getDescricaoGrupo());
        this.nuGrupo = grupo.getNumeroGrupo();
    }

    private void configuracoesAdicionais() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dialogSize = getSize();
        if (dialogSize.height > screenSize.height)
            dialogSize.height = screenSize.height;
        if (dialogSize.width > screenSize.width)
            dialogSize.width = screenSize.width;
        setLocation((screenSize.width - dialogSize.width) / 2, (screenSize.height - dialogSize.height) / 2);
        this.jTDestinatario.setModel(new DestinatarioTableModel());
        this.jTDestinatarioGrupo.setModel(new DestinatarioTableModel());
    }

    private void carregaListaDestinatario(String grupo) {
        DestinatarioTableModel model = null;
        try {
            ArrayList<DestinatarioBean> arrayDestinatarioGrupo, arrayDestinatario;
            if (grupo.equals("")) {
                arrayDestinatario = DestinatarioDao.getInstance().recuperaDestinatario("");
                arrayDestinatarioGrupo = new ArrayList<>();
            } else {
                arrayDestinatario = DestinatarioDao.getInstance().recuperarDestinatarioForaDoGrupo(grupo);
                arrayDestinatarioGrupo = DestinatarioDao.getInstance().recuperaDestinatarioPorGrupo(grupo);
            }
            int i;
            for (i = 0; i < arrayDestinatario.size(); i++)
                this.vecDestinatario.add(arrayDestinatario.get(i));
            model = (DestinatarioTableModel) this.jTDestinatario.getModel();
            model.setDestinatario(arrayDestinatario);
            for (i = 0; i < arrayDestinatarioGrupo.size(); i++)
                this.vecDestinatarioGrupo.add(arrayDestinatarioGrupo.get(i));
            model = (DestinatarioTableModel) this.jTDestinatarioGrupo.getModel();
            model.setDestinatario(arrayDestinatarioGrupo);
        } catch (DaoException e) {
            logger.error(e.getMessage(), (Throwable) e);
            JOptionPane.showMessageDialog(this, "Não foi possivel carregar relação de destinatários", "Endereçador ", 2);
        }
    }

    private void initComponents() {
        JToolBar jToolBar1 = new JToolBar();
        JButton jbtConfirmar = new JButton();
        JButton jbtVoltar = new JButton();
        JPanel jPanel1 = new JPanel();
        JLabel jLabel1 = new JLabel();
        this.jtxtGrupo = new JTextField();
        JScrollPane jScrollPane1 = new JScrollPane();
        this.jTDestinatario = new JTable();
        JScrollPane jScrollPane2 = new JScrollPane();
        this.jTDestinatarioGrupo = new JTable();
        JButton jbtAdicionar = new JButton();
        JButton jbtRemover = new JButton();
        setDefaultCloseOperation(2);
        setTitle("Cadastrar Grupo");
        jToolBar1.setBorder(BorderFactory.createEtchedBorder());
        jbtConfirmar.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        jbtConfirmar.setIcon(new ImageIcon(getClass().getResource("/imagens/OK.gif")));
        jbtConfirmar.setText("Confirmar");
        jbtConfirmar.setHorizontalTextPosition(0);
        jbtConfirmar.setMaximumSize(new Dimension(90, 60));
        jbtConfirmar.setVerticalTextPosition(3);
        jbtConfirmar.addActionListener(TelaEditarGrupo.this::jbtConfirmarActionPerformed);
        jToolBar1.add(jbtConfirmar);
        jbtVoltar.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        jbtVoltar.setIcon(new ImageIcon(getClass().getResource("/imagens/sair.gif")));
        jbtVoltar.setText("Voltar");
        jbtVoltar.setHorizontalTextPosition(0);
        jbtVoltar.setMaximumSize(new Dimension(90, 60));
        jbtVoltar.setVerticalTextPosition(3);
        jbtVoltar.addActionListener(TelaEditarGrupo.this::jbtVoltarActionPerformed);
        jToolBar1.add(jbtVoltar);
        jPanel1.setBorder(BorderFactory.createEtchedBorder());
        jLabel1.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel1.setText("Grupo:");
        this.jTDestinatario.setModel(new DefaultTableModel(new Object[][]{{null, null, null, null}, {null, null, null, null}, {null, null, null, null}, {null, null, null, null},}, (Object[]) new String[]{"Title 1", "Title 2", "Title 3", "Title 4"}));
        this.jTDestinatario.setAutoResizeMode(0);
        jScrollPane1.setViewportView(this.jTDestinatario);
        this.jTDestinatarioGrupo.setModel(new DefaultTableModel(new Object[][]{{null, null, null, null}, {null, null, null, null}, {null, null, null, null}, {null, null, null, null},}, (Object[]) new String[]{"Title 1", "Title 2", "Title 3", "Title 4"}));
        this.jTDestinatarioGrupo.setAutoResizeMode(0);
        jScrollPane2.setViewportView(this.jTDestinatarioGrupo);
        jbtAdicionar.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        jbtAdicionar.setIcon(new ImageIcon(getClass().getResource("/imagens/add.gif")));
        jbtAdicionar.setText("Adicionar");
        jbtAdicionar.addActionListener(TelaEditarGrupo.this::jbtAdicionarActionPerformed);
        jbtRemover.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        jbtRemover.setIcon(new ImageIcon(getClass().getResource("/imagens/rem.gif")));
        jbtRemover.setText("Remover");
        jbtRemover.addActionListener(TelaEditarGrupo.this::jbtRemoverActionPerformed);
        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(1)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(1)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jScrollPane1, -1, 279, 32767)
                        .addPreferredGap(0)
                        .add(jPanel1Layout.createParallelGroup(1)
                            .add(jbtRemover).add(jbtAdicionar))
                        .addPreferredGap(0)
                        .add(jScrollPane2, -1, 275, 32767))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jLabel1)
                        .addPreferredGap(0)
                        .add(this.jtxtGrupo, -2, 251, -2)))
                .addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(1)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap().add(jPanel1Layout.createParallelGroup(3)
                    .add(jLabel1)
                    .add(this.jtxtGrupo, -2, -1, -2))
                .add(jPanel1Layout.createParallelGroup(1)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(15, 15, 15)
                        .add(jPanel1Layout.createParallelGroup(1)
                            .add(jScrollPane1, -1, 222, 32767)
                            .add(jScrollPane2, -1, 222, 32767)))
                    .add((GroupLayout.Group) jPanel1Layout.createSequentialGroup()
                        .add(70, 70, 70).add(jbtAdicionar)
                        .add(48, 48, 48).add(jbtRemover, -2, 25, -2)
                        .addPreferredGap(0, 65, 32767)))
                .addContainerGap()));
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(1)
            .add(jToolBar1, -1, 687, 32767)
            .add(jPanel1, -1, -1, 32767));
        layout.setVerticalGroup(layout.createParallelGroup(1)
            .add(layout.createSequentialGroup()
                .add(jToolBar1, -2, 55, -2)
                .addPreferredGap(0)
                .add(jPanel1, -1, -1, 32767)));
        pack();
    }

    private void jbtRemoverActionPerformed(ActionEvent evt) {
        DestinatarioTableModel model = null;
        Vector<DestinatarioBean> vecTemp = new Vector<>();
        ArrayList<DestinatarioBean> arraySort = new ArrayList<>();
        DestinatarioBean destinatarioBean = new DestinatarioBean();
        try {
            if (this.jTDestinatarioGrupo.getSelectedRowCount() > 0) {
                model = (DestinatarioTableModel) this.jTDestinatarioGrupo.getModel();
                int i;
                for (i = 0; i < this.jTDestinatarioGrupo.getRowCount(); i++) {
                    destinatarioBean = model.getDestinatario(i);
                    if (!this.jTDestinatarioGrupo.isRowSelected(i)) {
                        vecTemp.add(destinatarioBean);
                    } else {
                        this.vecDestinatario.add(destinatarioBean);
                    }
                }
                this.vecDestinatarioGrupo.removeAllElements();
                this.vecDestinatarioGrupo = vecTemp;
                for (i = 0; i < this.vecDestinatarioGrupo.size(); i++)
                    arraySort.add(this.vecDestinatarioGrupo.get(i));
                arraySort.sort(destinatarioBean);
                model = (DestinatarioTableModel) this.jTDestinatarioGrupo.getModel();
                model.setDestinatario(arraySort);
                this.vecDestinatarioGrupo.removeAllElements();
                for (i = 0; i < arraySort.size(); i++)
                    this.vecDestinatarioGrupo.add(arraySort.get(i));
                arraySort = new ArrayList<DestinatarioBean>();
                for (i = 0; i < this.vecDestinatario.size(); i++)
                    arraySort.add(this.vecDestinatario.get(i));
                arraySort.sort(destinatarioBean);
                model = (DestinatarioTableModel) this.jTDestinatario.getModel();
                model.setDestinatario(arraySort);
                this.vecDestinatario.removeAllElements();
                for (i = 0; i < arraySort.size(); i++)
                    this.vecDestinatario.add(arraySort.get(i));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void jbtAdicionarActionPerformed(ActionEvent evt) {
        DestinatarioTableModel model = null;
        Vector<DestinatarioBean> vecTemp = new Vector<>();
        ArrayList<DestinatarioBean> arraySort = new ArrayList<>();
        DestinatarioBean destinatarioBean = new DestinatarioBean();
        if (this.jTDestinatario.getSelectedRowCount() > 0) {
            model = (DestinatarioTableModel) this.jTDestinatario.getModel();
            int i;
            for (i = 0; i < this.jTDestinatario.getRowCount(); i++) {
                destinatarioBean = model.getDestinatario(i);
                if (!this.jTDestinatario.isRowSelected(i)) {
                    vecTemp.add(destinatarioBean);
                } else {
                    this.vecDestinatarioGrupo.add(destinatarioBean);
                }
            }
            this.vecDestinatario.removeAllElements();
            this.vecDestinatario = vecTemp;
            for (i = 0; i < this.vecDestinatario.size(); i++)
                arraySort.add(this.vecDestinatario.get(i));
            Collections.sort(arraySort, destinatarioBean);
            model = (DestinatarioTableModel) this.jTDestinatario.getModel();
            model.setDestinatario(arraySort);
            this.vecDestinatario.removeAllElements();
            for (i = 0; i < arraySort.size(); i++)
                this.vecDestinatario.add(arraySort.get(i));
            arraySort = new ArrayList<>();
            for (i = 0; i < this.vecDestinatarioGrupo.size(); i++)
                arraySort.add(this.vecDestinatarioGrupo.get(i));
            Collections.sort(arraySort, destinatarioBean);
            model = (DestinatarioTableModel) this.jTDestinatarioGrupo.getModel();
            model.setDestinatario(arraySort);
            this.vecDestinatarioGrupo.removeAllElements();
            for (i = 0; i < arraySort.size(); i++)
                this.vecDestinatarioGrupo.add(arraySort.get(i));
        }
    }

    private void jbtVoltarActionPerformed(ActionEvent evt) {
        setVisible(false);
    }

    private void jbtConfirmarActionPerformed(ActionEvent evt) {
        GrupoBean grupoBean;
        GrupoDestinatarioBean grupoDestinatarioBean;
        DestinatarioBean destinatarioBean;
        if (this.jtxtGrupo.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "O campo grupo deve ser preenchido!", "Endereçador", 2);
            this.jtxtGrupo.requestFocus();
        } else {
            try {
                if (this.blnIncluir) {
                    grupoBean = GrupoBean.getInstance();
                    grupoBean.setDescricaoGrupo(this.jtxtGrupo.getText());
                    GrupoDao.getInstance().incluirGrupo(grupoBean);
                    this.nuGrupo = GrupoDao.getInstance().recuperaUltimoGrupo();
                    grupoDestinatarioBean = GrupoDestinatarioBean.getInstance();
                    for (DestinatarioBean bean : this.vecDestinatarioGrupo) {
                        grupoDestinatarioBean.setNumeroGrupo(this.nuGrupo);
                        destinatarioBean = bean;
                        grupoDestinatarioBean.setNumeroDestinatario(destinatarioBean.getNumeroDestinatario());
                        GrupoDestinatarioDao.getInstance().incluirGrupoDestinatario(grupoDestinatarioBean);
                    }
                    JOptionPane.showMessageDialog(null, "Dados gravados com sucesso!", "Endereçador", 1);
                    this.blnIncluir = false;
                } else {
                    grupoBean = GrupoBean.getInstance();
                    grupoBean.setNumeroGrupo(this.nuGrupo);
                    grupoBean.setDescricaoGrupo(this.jtxtGrupo.getText());
                    GrupoDao.getInstance().alterarGrupo(grupoBean);
                    grupoDestinatarioBean = GrupoDestinatarioBean.getInstance();
                    GrupoDestinatarioDao.getInstance().excluirGrupoDestinatario(this.nuGrupo);
                    for (DestinatarioBean bean : this.vecDestinatarioGrupo) {
                        grupoDestinatarioBean.setNumeroGrupo(this.nuGrupo);
                        destinatarioBean = bean;
                        grupoDestinatarioBean.setNumeroDestinatario(destinatarioBean.getNumeroDestinatario());
                        GrupoDestinatarioDao.getInstance().incluirGrupoDestinatario(grupoDestinatarioBean);
                    }
                    JOptionPane.showMessageDialog(null, "Dados atualizados com sucesso!", "Endereçador", 1);
                }
            } catch (DaoException ge) {
                logger.error(ge.getMessage(), ge);
                JOptionPane.showMessageDialog(null, "Não foi possivel gravar dados", "Endereçador", 2);
            }
        }
    }
}
