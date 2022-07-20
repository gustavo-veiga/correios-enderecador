package br.com.correios.enderecador.telas;

import br.com.correios.enderecador.bean.DestinatarioBean;
import br.com.correios.enderecador.bean.RemetenteBean;
import br.com.correios.enderecador.dao.DaoException;
import br.com.correios.enderecador.dao.RemetenteDao;
import br.com.correios.enderecador.excecao.EnderecadorExcecao;
import br.com.correios.enderecador.util.DocumentoPersonalizado;
import br.com.correios.enderecador.util.EnderecadorObservable;
import br.com.correios.enderecador.util.Impressao;
import br.com.correios.enderecador.util.TextoCellRenderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.Document;

import org.apache.log4j.Logger;
import org.jdesktop.layout.GroupLayout;

public class TelaImpressaoEncomenda extends JInternalFrame implements Observer {
    private static final Logger logger = Logger.getLogger(TelaImpressaoEncomenda.class);

    private final Frame frmParent;

    private final DestinatarioImpressaoTableModel model = new DestinatarioImpressaoTableModel("E");

    private Vector<DestinatarioBean> vecDestinatarioImpressao = new Vector<>();

    private String relatorio = "Encomenda2_vizinho.jasper";

    private static TelaImpressaoEncomenda instance;

    private ButtonGroup buttonGroup1;

    private JPanel jPanel2;

    private JCheckBox jchkTelDestinatario;

    private JCheckBox jchkTelRemetente;

    private JComboBox<String> jcmbRemetente;

    private JLabel jlblImpressao;

    private JRadioButton jrbtDoisRotulos;

    private JRadioButton jrbtQuatroRotulos;

    private JTable jtblDestinatarioImpressao;

    private JTextField jtxtNumeroEtiqueta;

    private TelaImpressaoEncomenda(Frame parent) {
        this.frmParent = parent;
        initComponents();
        configuracoesAdicionais();
        carregaRemetente();
        EnderecadorObservable observable = EnderecadorObservable.getInstance();
        observable.addObserver(this);
    }

    public static TelaImpressaoEncomenda getInstance(Frame parent) {
        if (instance == null || instance.isClosed())
            instance = new TelaImpressaoEncomenda(parent);
        return instance;
    }

    private void configuracoesAdicionais() {
        TextoCellRenderer renderer = new TextoCellRenderer(2);
        TableColumn coluna = this.jtblDestinatarioImpressao.getColumnModel().getColumn(0);
        coluna.setCellRenderer(renderer);
        coluna.setPreferredWidth(100);
        coluna = this.jtblDestinatarioImpressao.getColumnModel().getColumn(1);
        coluna.setPreferredWidth(250);
        coluna.setCellRenderer(renderer);
        coluna = this.jtblDestinatarioImpressao.getColumnModel().getColumn(2);
        coluna.setPreferredWidth(40);
        coluna.setCellRenderer(renderer);
        coluna = this.jtblDestinatarioImpressao.getColumnModel().getColumn(3);
        renderer = new TextoCellRenderer(0);
        coluna.setCellRenderer(renderer);
        coluna.setPreferredWidth(20);
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addItem("Sim");
        comboBox.addItem("Não");
        coluna = this.jtblDestinatarioImpressao.getColumnModel().getColumn(4);
        coluna.setCellEditor(new DefaultCellEditor(comboBox));
        coluna.setCellRenderer(renderer);
        coluna.setPreferredWidth(10);
        coluna = this.jtblDestinatarioImpressao.getColumnModel().getColumn(5);
        coluna.setCellRenderer(renderer);
        coluna.setPreferredWidth(20);
        coluna = this.jtblDestinatarioImpressao.getColumnModel().getColumn(6);
        coluna.setCellRenderer(renderer);
        coluna.setPreferredWidth(10);
        this.jrbtQuatroRotulos.doClick();
        this.buttonGroup1.remove(this.jrbtDoisRotulos);
        GroupLayout jPanel2Layout = new GroupLayout(this.jPanel2);
        this.jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(1)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(this.jlblImpressao)
                .addPreferredGap(0)
                .add(jPanel2Layout.createParallelGroup(1)
                    .add(this.jrbtQuatroRotulos))
                .addContainerGap(18, 32767)));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(1)
            .add(jPanel2Layout.createSequentialGroup()
                .add(26, 26, 26)
                .add(this.jrbtQuatroRotulos)
                .addPreferredGap(0, -1, 32767)
                .add(30, 30, 30))
            .add(2, this.jlblImpressao, -2, 123, -2));
    }

    private void carregaRemetente() {
        try {
            this.jcmbRemetente.removeAllItems();
            ArrayList<RemetenteBean> arrayRemetente = RemetenteDao.getInstance().recuperaRemetente("");
            for (RemetenteBean remetenteBean : arrayRemetente)
                this.jcmbRemetente.addItem(remetenteBean.getNome());
        } catch (DaoException e) {
            logger.error(e.getMessage(), e);
            JOptionPane.showMessageDialog(this.frmParent, "Não foi possivel carregar relação de remetentes", "Endereçador ECT", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void initComponents() {
        this.buttonGroup1 = new ButtonGroup();
        JToolBar jToolBar1 = new JToolBar();
        JButton jbtnSelecionarDestinatario = new JButton();
        JButton jbtnSelecionarGrupo = new JButton();
        JButton jbtnRemoverDestinatario = new JButton();
        JButton jbtnRemoverTodos = new JButton();
        JButton jbtnVisializarAR = new JButton();
        JButton jbtnVisializar = new JButton();
        JButton jButton2 = new JButton();
        JPanel jPanel1 = new JPanel();
        this.jPanel2 = new JPanel();
        this.jrbtDoisRotulos = new JRadioButton();
        this.jrbtQuatroRotulos = new JRadioButton();
        this.jlblImpressao = new JLabel();
        JPanel jPanel4 = new JPanel();
        JLabel jLabel4 = new JLabel();
        this.jcmbRemetente = new JComboBox<>();
        JLabel jLabel2 = new JLabel();
        this.jtxtNumeroEtiqueta = new JTextField();
        this.jchkTelRemetente = new JCheckBox();
        JLabel jLabel1 = new JLabel();
        this.jchkTelDestinatario = new JCheckBox();
        JScrollPane jScrollPane1 = new JScrollPane();
        this.jtblDestinatarioImpressao = new JTable();
        JLabel jLabel3 = new JLabel();
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Etiquetas para encomendas");
        jToolBar1.setBorder(BorderFactory.createEtchedBorder());
        jbtnSelecionarDestinatario.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        jbtnSelecionarDestinatario.setIcon(new ImageIcon(getClass().getResource("/imagens/addusuario.gif")));
        jbtnSelecionarDestinatario.setText("Selecionar destinatário");
        jbtnSelecionarDestinatario.setHorizontalTextPosition(0);
        jbtnSelecionarDestinatario.setMaximumSize(new Dimension(110, 60));
        jbtnSelecionarDestinatario.setVerticalTextPosition(3);
        jbtnSelecionarDestinatario.addActionListener(TelaImpressaoEncomenda.this::jbtnSelecionarDestinatarioActionPerformed);
        jToolBar1.add(jbtnSelecionarDestinatario);
        jbtnSelecionarGrupo.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        jbtnSelecionarGrupo.setIcon(new ImageIcon(getClass().getResource("/imagens/addusuarios.gif")));
        jbtnSelecionarGrupo.setText("Selecionar grupo");
        jbtnSelecionarGrupo.setHorizontalTextPosition(0);
        jbtnSelecionarGrupo.setMaximumSize(new Dimension(100, 60));
        jbtnSelecionarGrupo.setVerticalTextPosition(3);
        jbtnSelecionarGrupo.addActionListener(TelaImpressaoEncomenda.this::jbtnSelecionarGrupoActionPerformed);
        jToolBar1.add(jbtnSelecionarGrupo);
        jbtnRemoverDestinatario.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        jbtnRemoverDestinatario.setIcon(new ImageIcon(getClass().getResource("/imagens/remover.gif")));
        jbtnRemoverDestinatario.setText("Remover destinatário");
        jbtnRemoverDestinatario.setHorizontalTextPosition(0);
        jbtnRemoverDestinatario.setMaximumSize(new Dimension(110, 60));
        jbtnRemoverDestinatario.setVerticalTextPosition(3);
        jbtnRemoverDestinatario.addActionListener(TelaImpressaoEncomenda.this::jbtnRemoverDestinatarioActionPerformed);
        jToolBar1.add(jbtnRemoverDestinatario);
        jbtnRemoverTodos.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        jbtnRemoverTodos.setIcon(new ImageIcon(getClass().getResource("/imagens/removerTodos.gif")));
        jbtnRemoverTodos.setText("Remover todos");
        jbtnRemoverTodos.setHorizontalTextPosition(0);
        jbtnRemoverTodos.setMaximumSize(new Dimension(90, 60));
        jbtnRemoverTodos.setVerticalTextPosition(3);
        jbtnRemoverTodos.addActionListener(TelaImpressaoEncomenda.this::jbtnRemoverTodosActionPerformed);
        jToolBar1.add(jbtnRemoverTodos);
        jbtnVisializarAR.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        jbtnVisializarAR.setIcon(new ImageIcon(getClass().getResource("/imagens/print.gif")));
        jbtnVisializarAR.setText("Visualizar AR");
        jbtnVisializarAR.setHorizontalTextPosition(0);
        jbtnVisializarAR.setMaximumSize(new Dimension(90, 60));
        jbtnVisializarAR.setVerticalTextPosition(3);
        jbtnVisializarAR.addActionListener(TelaImpressaoEncomenda.this::jbtnVisializarARActionPerformed);
        jToolBar1.add(jbtnVisializarAR);
        jbtnVisializar.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        jbtnVisializar.setIcon(new ImageIcon(getClass().getResource("/imagens/IMPRIMIR.gif")));
        jbtnVisializar.setText("Visualizar Etiqueta");
        jbtnVisializar.setHorizontalTextPosition(0);
        jbtnVisializar.setMaximumSize(new Dimension(90, 60));
        jbtnVisializar.setVerticalTextPosition(3);
        jbtnVisializar.addActionListener(TelaImpressaoEncomenda.this::jbtnVisializarActionPerformed);
        jToolBar1.add(jbtnVisializar);
        jButton2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        jButton2.setIcon(new ImageIcon(getClass().getResource("/imagens/print.gif")));
        jButton2.setText("Declaração de Conteúdo");
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(0);
        jButton2.setVerticalTextPosition(3);
        jButton2.addActionListener(TelaImpressaoEncomenda.this::jButton2ActionPerformed);
        jToolBar1.add(jButton2);
        jPanel1.setBorder(BorderFactory.createEtchedBorder());
        this.jPanel2.setBorder(BorderFactory.createTitledBorder(null, "Escolha o tamanho do rótulo que deseja gerar", 0, 0, new Font("Tahoma", 0, 10)));
        this.jPanel2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        this.buttonGroup1.add(this.jrbtDoisRotulos);
        this.jrbtDoisRotulos.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        this.jrbtDoisRotulos.setText("2 rótulos por vez, distribuídos numa folha");
        this.jrbtDoisRotulos.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.jrbtDoisRotulos.setEnabled(false);
        this.jrbtDoisRotulos.setFocusable(false);
        this.jrbtDoisRotulos.setMargin(new Insets(0, 0, 0, 0));
        this.jrbtDoisRotulos.addActionListener(TelaImpressaoEncomenda.this::jrbtDoisRotulosActionPerformed);
        this.buttonGroup1.add(this.jrbtQuatroRotulos);
        this.jrbtQuatroRotulos.setFont(new Font("MS Sans Serif", 0, 10));
        this.jrbtQuatroRotulos.setSelected(true);
        this.jrbtQuatroRotulos.setText("4 rótulos por vez, distribuídos numa folha");
        this.jrbtQuatroRotulos.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.jrbtQuatroRotulos.setMargin(new Insets(0, 0, 0, 0));
        this.jrbtQuatroRotulos.addActionListener(TelaImpressaoEncomenda.this::jrbtQuatroRotulosActionPerformed);
        this.jlblImpressao.setIcon(new ImageIcon(getClass().getResource("/imagens/tipo_2etq.gif")));
        this.jlblImpressao.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        GroupLayout jPanel2Layout = new GroupLayout(this.jPanel2);
        this.jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(1)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(this.jlblImpressao)
                .addPreferredGap(0)
                .add(jPanel2Layout.createParallelGroup(1)
                    .add(this.jrbtDoisRotulos)
                    .add(this.jrbtQuatroRotulos))
                .addContainerGap(18, 32767)));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(1)
            .add(jPanel2Layout.createSequentialGroup()
                .add(26, 26, 26)
                .add(this.jrbtDoisRotulos)
                .addPreferredGap(0, -1, 32767)
                .add(this.jrbtQuatroRotulos)
                .add(30, 30, 30))
            .add(2, this.jlblImpressao, -2, 123, -2));
        jPanel4.setBorder(BorderFactory.createTitledBorder(null, " ", 0, 0, new Font("Tahoma", Font.PLAIN, 10)));
        jPanel4.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel4.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel4.setText("Selecione o remetente:");
        jLabel2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel2.setText("Quero imprimir a partir da etiqueta nº:");
        this.jtxtNumeroEtiqueta.setDocument(new DocumentoPersonalizado(3, 1));
        this.jchkTelRemetente.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        this.jchkTelRemetente.setText("do remetente");
        this.jchkTelRemetente.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.jchkTelRemetente.setMargin(new Insets(0, 0, 0, 0));
        jLabel1.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel1.setText("Incluir telefone na etiqueta:");
        this.jchkTelDestinatario.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        this.jchkTelDestinatario.setText("do destinatário");
        this.jchkTelDestinatario.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.jchkTelDestinatario.setMargin(new Insets(0, 0, 0, 0));
        GroupLayout jPanel4Layout = new GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(1)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel4Layout.createParallelGroup(1)
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(jLabel1)
                        .add(17, 17, 17)
                        .add(this.jchkTelRemetente)
                        .add(18, 18, 18)
                        .add(this.jchkTelDestinatario))
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(jLabel4)
                        .addPreferredGap(0)
                        .add(this.jcmbRemetente, -2, -1, -2))
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(jLabel2)
                        .addPreferredGap(0)
                        .add(this.jtxtNumeroEtiqueta, -2, 66, -2)))
                .addContainerGap(-1, 32767)));
        jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(1)
            .add(jPanel4Layout.createSequentialGroup()
                .add(jPanel4Layout.createParallelGroup(3)
                    .add(jLabel4).add(this.jcmbRemetente, -2, -1, -2))
                .add(20, 20, 20)
                .add(jPanel4Layout.createParallelGroup(3)
                    .add(jLabel2)
                    .add(this.jtxtNumeroEtiqueta, -2, -1, -2))
                .addPreferredGap(0, 29, 32767)
                .add(jPanel4Layout.createParallelGroup(3)
                    .add(jLabel1)
                    .add(this.jchkTelRemetente)
                    .add(this.jchkTelDestinatario))
                .add(21, 21, 21)));
        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(1)
            .add(jPanel1Layout.createSequentialGroup()
                .add(this.jPanel2, -2, -1, -2)
                .addPreferredGap(0)
                .add(jPanel4, -1, -1, 32767)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(1)
            .add(2, jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(2)
                    .add(1, jPanel4, -1, -1, 32767)
                    .add(this.jPanel2, -1, -1, 32767))
                .add(12, 12, 12)));
        this.jtblDestinatarioImpressao.setModel(new DefaultTableModel(new Object[][]{{null, null, null, null}, {null, null, null, null}, {null, null, null, null}, {null, null, null, null},}, new String[]{"Title 1", "Title 2", "Title 3", "Title 4"}));
        this.jtblDestinatarioImpressao.setModel(this.model);
        jScrollPane1.setViewportView(this.jtblDestinatarioImpressao);
        jLabel3.setForeground(new Color(51, 51, 255));
        jLabel3.setText("* Os campos de Quantidades, Mão Própria, Observação são editáveis com um duplo clique.");
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(1).add(jToolBar1, -1, 846, 32767)
            .add(jPanel1, -1, -1, 32767)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(1)
                    .add(jScrollPane1)
                    .add(2, layout.createSequentialGroup()
                        .add(0, 0, 32767)
                        .add(jLabel3)))
                .addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(1)
            .add(layout.createSequentialGroup()
                .add(jToolBar1, -2, 59, -2)
                .addPreferredGap(0)
                .add(jPanel1, -2, -1, -2)
                .addPreferredGap(1)
                .add(jLabel3, -2, 14, -2)
                .addPreferredGap(0)
                .add(jScrollPane1, -1, 259, 32767)
                .addContainerGap()));
        pack();
    }

    private void jbtnVisializarARActionPerformed(ActionEvent evt) {
        Impressao impressao = new Impressao();
        if (this.vecDestinatarioImpressao.size() < 1) {
            JOptionPane.showMessageDialog(this, "Não existe nenhum destinatário selecionado!", "Endereçador", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (this.jcmbRemetente.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "Por favor cadastre um remetente antes de fazer a impressão.", "Endereçador ECT", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            impressao.imprimirAR("avisoRecebimento.jasper", (RemetenteBean) this.jcmbRemetente.getSelectedItem(), this.vecDestinatarioImpressao);
        } catch (EnderecadorExcecao ex) {
            logger.error(ex.getMessage(), (Throwable) ex);
            JOptionPane.showMessageDialog(this, "Não foi possível imprimir o AR", "Endereçador", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void jrbtQuatroRotulosActionPerformed(ActionEvent evt) {
        this.jlblImpressao.setIcon(new ImageIcon(getClass().getResource("/imagens/tipo_4etq.gif")));
        this.relatorio = "Encomenda4_vizinho.jasper";
    }

    private void jrbtDoisRotulosActionPerformed(ActionEvent evt) {
        this.jlblImpressao.setIcon(new ImageIcon(getClass().getResource("/imagens/tipo_2etq.gif")));
        this.relatorio = "Encomenda2_vizinho.jasper";
    }

    private void jbtnVisializarActionPerformed(ActionEvent evt) {
        if (this.vecDestinatarioImpressao.size() < 1) {
            JOptionPane.showMessageDialog(this, "Não existe nenhum destinatário selecionado!", "Endereçador", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (this.jcmbRemetente.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "Por favor cadastre um remetente antes de fazer a impressão.", "Endereçador ECT", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Impressao impressao = new Impressao();
        try {
            if (this.jtxtNumeroEtiqueta.getText().trim().equals(""))
                this.jtxtNumeroEtiqueta.setText("1");
            if (this.relatorio.equals("Encomenda2_vizinho.jasper") && Integer.parseInt(this.jtxtNumeroEtiqueta.getText()) > 2) {
                JOptionPane.showMessageDialog(this, "Posição inicial para impressão de etiquetas deve ser 1 ou 2.", "Endereçador", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (this.relatorio.equals("Encomenda4_vizinho.jasper") && Integer.parseInt(this.jtxtNumeroEtiqueta.getText()) > 4) {
                JOptionPane.showMessageDialog(this, "Posição inicial para impressão de etiquetas deve ser um número de 1 a 4.", "Endereçador", JOptionPane.WARNING_MESSAGE);
                return;
            }
            impressao.impressaoEncomenda(this.relatorio, (RemetenteBean) this.jcmbRemetente.getSelectedItem(), this.vecDestinatarioImpressao, Integer.parseInt(this.jtxtNumeroEtiqueta.getText()), this.jchkTelRemetente.isSelected(), this.jchkTelDestinatario.isSelected());
        } catch (EnderecadorExcecao ex) {
            logger.error(ex.getMessage(), ex);
            JOptionPane.showMessageDialog(this, "Não foi possível imprimir o relatório.", "Endereçador", 2);
        }
    }

    private void jbtnRemoverTodosActionPerformed(ActionEvent evt) {
        this.vecDestinatarioImpressao.removeAllElements();
        this.model.setDestinatario(this.vecDestinatarioImpressao);
        this.jtblDestinatarioImpressao.setModel(this.model);
    }

    private void jbtnRemoverDestinatarioActionPerformed(ActionEvent evt) {
        if (this.jtblDestinatarioImpressao.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Não existe nenhum destinatário selecionado.", "Endereçador ECT", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int[] selectedRows = this.jtblDestinatarioImpressao.getSelectedRows();
        this.vecDestinatarioImpressao = new Vector<>(this.model.getDestinatario());
        for (int selectedRow : selectedRows)
            this.vecDestinatarioImpressao.remove(this.model.getElementAt(selectedRow));
        this.model.setDestinatario(this.vecDestinatarioImpressao);
    }

    private void jbtnSelecionarGrupoActionPerformed(ActionEvent evt) {
        TelaPesquisarGrupo telaPesquisarGrupo = new TelaPesquisarGrupo(this.frmParent, true, this.vecDestinatarioImpressao);
        telaPesquisarGrupo.setVisible(true);
        this.model.setDestinatario(this.vecDestinatarioImpressao);
        this.jtblDestinatarioImpressao.setModel(this.model);
    }

    private void jbtnSelecionarDestinatarioActionPerformed(ActionEvent evt) {
        TelaPesquisarDestinatario telaPesquisaDestinatario = new TelaPesquisarDestinatario(this.frmParent, true, this.vecDestinatarioImpressao);
        telaPesquisaDestinatario.setVisible(true);
        this.model.setDestinatario(this.vecDestinatarioImpressao);
        this.jtblDestinatarioImpressao.setModel(this.model);
    }

    private void jButton2ActionPerformed(ActionEvent evt) {
        if (this.vecDestinatarioImpressao.size() < 1) {
            JOptionPane.showMessageDialog(this, "Não existe nenhum destinatário selecionado!", "Endereçador", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (this.jcmbRemetente.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "Por favor cadastre um remetente antes de fazer a impressão.", "Endereçador ECT", JOptionPane.WARNING_MESSAGE);
            return;
        }
        TelaDeclararConteudo telaDeclararConteudo = new TelaDeclararConteudo(this.frmParent, true, this.vecDestinatarioImpressao, (RemetenteBean) this.jcmbRemetente.getSelectedItem());
        telaDeclararConteudo.setVisible(true);
    }

    public void update(Observable o, Object arg) {
        if (arg instanceof RemetenteBean) {
            RemetenteBean remetente = (RemetenteBean) arg;
            this.jcmbRemetente.removeItem(remetente);
            this.jcmbRemetente.addItem(remetente.getNome());
        } else if (arg instanceof DestinatarioBean) {
            DestinatarioBean destinatario = (DestinatarioBean) arg;
            int index = this.model.indexOf(destinatario);
            if (index != -1)
                this.model.setDestinatario(index, destinatario);
        } else if (arg instanceof List) {
            List<?> listaUsuarios = (List<?>) arg;
            for (Object listaUsuario : listaUsuarios) {
                if (listaUsuario instanceof DestinatarioBean) {
                    this.model.removeDestinatario((DestinatarioBean) listaUsuario);
                } else if (listaUsuario instanceof RemetenteBean) {
                    this.jcmbRemetente.removeItem(listaUsuario);
                }
            }
        }
    }
}
