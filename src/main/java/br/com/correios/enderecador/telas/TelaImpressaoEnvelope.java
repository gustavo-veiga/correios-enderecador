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
import java.util.*;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
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
import javax.swing.table.TableColumn;
import javax.swing.text.Document;

import org.apache.log4j.Logger;
import org.jdesktop.layout.GroupLayout;

public class TelaImpressaoEnvelope extends JInternalFrame implements Observer {
    private static final Logger logger = Logger.getLogger(TelaImpressaoEnvelope.class);

    private final DestinatarioImpressaoTableModel model = new DestinatarioImpressaoTableModel("C");

    private final Frame frmParent;

    private Vector<DestinatarioBean> vecDestinatarioImpressao = new Vector<>();

    private String relatorio = "A4Envelope14.jasper";

    private boolean imprimirTratamento = false;

    private static TelaImpressaoEnvelope instance;

    private JButton jbtRemoverDestinatario;

    private JButton jbtRemoverTodos;

    private JButton jbtSelecionarDestinatario;

    private JButton jbtSelecionarGrupo;

    private JButton jbtnVisializarAR;

    private JCheckBox jchkTelDestinatario;

    private JCheckBox jchkTelRemetente;

    private JComboBox<String> jcmbFolha;

    private JComboBox<String> jcmbRemetente;

    private JComboBox<String> jcmbTamanhoFonte;

    private JLabel jlNumeroVezes;

    private JLabel jlSelecioneRemetente;

    private JLabel jlblImpressao;

    private JRadioButton jrbt10Rotulos;

    private JRadioButton jrbt14Rotulos;

    private JRadioButton jrbtDestinatario;

    private JRadioButton jrbtRemetente;

    private JRadioButton jrbtRemetenteDestinatario;

    private JTable jtblDestinatarioImpressao;

    private JTextField jtxtNumeroEtiqueta;

    private JTextField jtxtNumeroVezes;

    private TelaImpressaoEnvelope(Frame parent) {
        this.frmParent = parent;
        initComponents();
        configuracoesAdicionais();
        carregaRemetente();
        EnderecadorObservable observable = EnderecadorObservable.getInstance();
        observable.addObserver(this);
    }

    public static TelaImpressaoEnvelope getInstance(Frame parent) {
        if (instance == null)
            instance = new TelaImpressaoEnvelope(parent);
        return instance;
    }

    private void configuracoesAdicionais() {
        TextoCellRenderer renderer = new TextoCellRenderer(2);
        TableColumn coluna = this.jtblDestinatarioImpressao.getColumnModel().getColumn(0);
        coluna.setCellRenderer(renderer);
        coluna.setPreferredWidth(300);
        coluna = this.jtblDestinatarioImpressao.getColumnModel().getColumn(1);
        coluna.setPreferredWidth(300);
        coluna.setCellRenderer(renderer);
        coluna = this.jtblDestinatarioImpressao.getColumnModel().getColumn(2);
        coluna.setPreferredWidth(120);
        coluna.setCellRenderer(renderer);
        coluna = this.jtblDestinatarioImpressao.getColumnModel().getColumn(3);
        renderer = new TextoCellRenderer(0);
        coluna.setCellRenderer(renderer);
        coluna.setPreferredWidth(60);
        coluna.setWidth(1);
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addItem("Sim");
        comboBox.addItem("Não");
        coluna = this.jtblDestinatarioImpressao.getColumnModel().getColumn(4);
        coluna.setCellEditor(new DefaultCellEditor(comboBox));
        coluna.setCellRenderer(renderer);
    }

    private void carregaRemetente() {
        try {
            this.jcmbRemetente.removeAllItems();
            ArrayList<RemetenteBean> arrayRemetente = RemetenteDao.getInstance().recuperaRemetente("");
            for (RemetenteBean remetenteBean : arrayRemetente)
                this.jcmbRemetente.addItem(remetenteBean.getNome());
        } catch (DaoException e) {
            logger.error(e.getMessage(), e);
            JOptionPane.showMessageDialog(this.frmParent, "Não foi possivel carregar relação de remetentes", "Endereçador ECT", 2);
        }
    }

    private void initComponents() {
        ButtonGroup jbtgTipoEtiqueta = new ButtonGroup();
        ButtonGroup jbtgTipoImpressao = new ButtonGroup();
        JToolBar jToolBar1 = new JToolBar();
        this.jbtSelecionarDestinatario = new JButton();
        this.jbtSelecionarGrupo = new JButton();
        this.jbtRemoverDestinatario = new JButton();
        this.jbtRemoverTodos = new JButton();
        this.jbtnVisializarAR = new JButton();
        JButton jbtVisualizar = new JButton();
        JPanel jPanel1 = new JPanel();
        JPanel jPanel2 = new JPanel();
        this.jlblImpressao = new JLabel();
        this.jrbt14Rotulos = new JRadioButton();
        this.jrbt10Rotulos = new JRadioButton();
        JLabel jLabel2 = new JLabel();
        JLabel jLabel1 = new JLabel();
        this.jcmbFolha = new JComboBox<>();
        JLabel jLabelTamanhoFonte = new JLabel();
        this.jcmbTamanhoFonte = new JComboBox<>();
        JPanel jPanel3 = new JPanel();
        this.jlSelecioneRemetente = new JLabel();
        this.jlNumeroVezes = new JLabel();
        JLabel jLabel5 = new JLabel();
        this.jchkTelRemetente = new JCheckBox();
        this.jchkTelDestinatario = new JCheckBox();
        this.jcmbRemetente = new JComboBox<>();
        this.jtxtNumeroVezes = new JTextField();
        JLabel jLabel6 = new JLabel();
        this.jtxtNumeroEtiqueta = new JTextField();
        JScrollPane jScrollPane1 = new JScrollPane();
        this.jtblDestinatarioImpressao = new JTable();
        JPanel jPanel4 = new JPanel();
        JLabel jLabel7 = new JLabel();
        this.jrbtRemetente = new JRadioButton();
        this.jrbtDestinatario = new JRadioButton();
        this.jrbtRemetenteDestinatario = new JRadioButton();
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Etiquetas para cartas");
        jToolBar1.setBorder(BorderFactory.createEtchedBorder());
        this.jbtSelecionarDestinatario.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        this.jbtSelecionarDestinatario.setIcon(new ImageIcon(getClass().getResource("/imagens/addusuario.gif")));
        this.jbtSelecionarDestinatario.setText("Selecionar destinatário");
        this.jbtSelecionarDestinatario.setEnabled(false);
        this.jbtSelecionarDestinatario.setHorizontalTextPosition(0);
        this.jbtSelecionarDestinatario.setMaximumSize(new Dimension(110, 60));
        this.jbtSelecionarDestinatario.setVerticalTextPosition(3);
        this.jbtSelecionarDestinatario.addActionListener(TelaImpressaoEnvelope.this::jbtSelecionarDestinatarioActionPerformed);
        jToolBar1.add(this.jbtSelecionarDestinatario);
        this.jbtSelecionarGrupo.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        this.jbtSelecionarGrupo.setIcon(new ImageIcon(getClass().getResource("/imagens/addusuarios.gif")));
        this.jbtSelecionarGrupo.setText("Selecionar grupo");
        this.jbtSelecionarGrupo.setEnabled(false);
        this.jbtSelecionarGrupo.setHorizontalTextPosition(0);
        this.jbtSelecionarGrupo.setMaximumSize(new Dimension(90, 60));
        this.jbtSelecionarGrupo.setVerticalTextPosition(3);
        this.jbtSelecionarGrupo.addActionListener(TelaImpressaoEnvelope.this::jbtSelecionarGrupoActionPerformed);
        jToolBar1.add(this.jbtSelecionarGrupo);
        this.jbtRemoverDestinatario.setFont(new Font("MS Sans Serif", 0, 9));
        this.jbtRemoverDestinatario.setIcon(new ImageIcon(getClass().getResource("/imagens/remover.gif")));
        this.jbtRemoverDestinatario.setText("Remover destinatário");
        this.jbtRemoverDestinatario.setEnabled(false);
        this.jbtRemoverDestinatario.setHorizontalTextPosition(0);
        this.jbtRemoverDestinatario.setMaximumSize(new Dimension(110, 60));
        this.jbtRemoverDestinatario.setVerticalTextPosition(3);
        this.jbtRemoverDestinatario.addActionListener(TelaImpressaoEnvelope.this::jbtRemoverDestinatarioActionPerformed);
        jToolBar1.add(this.jbtRemoverDestinatario);
        this.jbtRemoverTodos.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        this.jbtRemoverTodos.setIcon(new ImageIcon(getClass().getResource("/imagens/removerTodos.gif")));
        this.jbtRemoverTodos.setText("Remover todos");
        this.jbtRemoverTodos.setEnabled(false);
        this.jbtRemoverTodos.setHorizontalTextPosition(0);
        this.jbtRemoverTodos.setMaximumSize(new Dimension(90, 60));
        this.jbtRemoverTodos.setVerticalTextPosition(3);
        this.jbtRemoverTodos.addActionListener(TelaImpressaoEnvelope.this::jbtRemoverTodosActionPerformed);
        jToolBar1.add(this.jbtRemoverTodos);
        this.jbtnVisializarAR.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        this.jbtnVisializarAR.setIcon(new ImageIcon(getClass().getResource("/imagens/print.gif")));
        this.jbtnVisializarAR.setText("Visualizar AR");
        this.jbtnVisializarAR.setEnabled(false);
        this.jbtnVisializarAR.setHorizontalTextPosition(0);
        this.jbtnVisializarAR.setMaximumSize(new Dimension(90, 60));
        this.jbtnVisializarAR.setVerticalTextPosition(3);
        this.jbtnVisializarAR.addActionListener(TelaImpressaoEnvelope.this::jbtnVisializarARActionPerformed);
        jToolBar1.add(this.jbtnVisializarAR);
        jbtVisualizar.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        jbtVisualizar.setIcon(new ImageIcon(getClass().getResource("/imagens/IMPRIMIR.gif")));
        jbtVisualizar.setText("Visualizar");
        jbtVisualizar.setHorizontalTextPosition(0);
        jbtVisualizar.setMaximumSize(new Dimension(90, 60));
        jbtVisualizar.setVerticalTextPosition(3);
        jbtVisualizar.addActionListener(TelaImpressaoEnvelope.this::jbtVisualizarActionPerformed);
        jToolBar1.add(jbtVisualizar);
        jPanel1.setBorder(BorderFactory.createEtchedBorder());
        jPanel2.setBorder(BorderFactory.createTitledBorder(null, "Escolha o tamanho do rótulo que deseja gerar", 0, 0, new Font("Tahoma", 0, 10)));
        this.jlblImpressao.setIcon(new ImageIcon(getClass().getResource("/imagens/tipo_14etq.gif")));
        jbtgTipoEtiqueta.add(this.jrbt14Rotulos);
        this.jrbt14Rotulos.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        this.jrbt14Rotulos.setSelected(true);
        this.jrbt14Rotulos.setText("14 rótulos por vez, distribuidos numa folha");
        this.jrbt14Rotulos.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.jrbt14Rotulos.setMargin(new Insets(0, 0, 0, 0));
        this.jrbt14Rotulos.addActionListener(TelaImpressaoEnvelope.this::jrbt14RotulosActionPerformed);
        jbtgTipoEtiqueta.add(this.jrbt10Rotulos);
        this.jrbt10Rotulos.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        this.jrbt10Rotulos.setText("10 rótulos por vez, distribuídos numa folha ");
        this.jrbt10Rotulos.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.jrbt10Rotulos.setMargin(new Insets(0, 0, 0, 0));
        this.jrbt10Rotulos.addActionListener(TelaImpressaoEnvelope.this::jrbt10RotulosActionPerformed);
        jLabel2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel2.setText("(com impressão do tratamento)");
        jLabel1.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel1.setText("Folhas do tamanho:");
        this.jcmbFolha.setModel(new DefaultComboBoxModel<>(new String[]{"A4", "Carta"}));
        this.jcmbFolha.addActionListener(TelaImpressaoEnvelope.this::jcmbFolhaActionPerformed);
        jLabelTamanhoFonte.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabelTamanhoFonte.setText("Tamanho da Fonte:");
        this.jcmbTamanhoFonte.setModel(new DefaultComboBoxModel<>(new String[]{"Pequeno", "Médio", "Grande"}));
        this.jcmbTamanhoFonte.addActionListener(TelaImpressaoEnvelope.this::jcmbTamanhoFonteActionPerformed);
        GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(1)
            .add(2, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(this.jlblImpressao, -2, 87, -2)
                .add(jPanel2Layout.createParallelGroup(1)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(21, 21, 21)
                        .add(jPanel2Layout.createParallelGroup(1)
                            .add(this.jrbt10Rotulos)
                            .add(jPanel2Layout.createSequentialGroup()
                                .add(17, 17, 17)
                                .add(jLabel2))))
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(20, 20, 20)
                        .add(this.jrbt14Rotulos))
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(18, 18, 18)
                        .add(jPanel2Layout.createParallelGroup(1)
                            .add(jPanel2Layout.createSequentialGroup()
                                .add(jLabel1)
                                .addPreferredGap(0)
                                .add(this.jcmbFolha, -2, -1, -2))
                            .add(jPanel2Layout.createSequentialGroup()
                                .add(jLabelTamanhoFonte)
                                .addPreferredGap(0)
                                .add(this.jcmbTamanhoFonte, -2, 89, -2)))))
                .add(102, 102, 102)));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(1)
            .add(jPanel2Layout.createSequentialGroup()
                .add(jPanel2Layout.createParallelGroup(1).add(this.jlblImpressao)
                    .add(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(this.jrbt14Rotulos)
                        .add(17, 17, 17)
                        .add(this.jrbt10Rotulos)
                        .addPreferredGap(0)
                        .add(jLabel2)
                        .addPreferredGap(0)
                        .add(jPanel2Layout.createParallelGroup(3)
                            .add(jLabel1)
                            .add(this.jcmbFolha, -2, -1, -2))
                        .addPreferredGap(0)
                        .add(jPanel2Layout.createParallelGroup(3)
                            .add(jLabelTamanhoFonte)
                            .add(this.jcmbTamanhoFonte, -2, -1, -2))))
                .addContainerGap(-1, 32767)));
        jPanel3.setBorder(BorderFactory.createTitledBorder(" "));
        this.jlSelecioneRemetente.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        this.jlSelecioneRemetente.setText("Selecione o remetente:");
        this.jlNumeroVezes.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        this.jlNumeroVezes.setText("Quantas vezes deseja imprimir o remetente:");
        jLabel5.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel5.setText("Incluir telefone na etiqueta:");
        this.jchkTelRemetente.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        this.jchkTelRemetente.setText("do remetente");
        this.jchkTelRemetente.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.jchkTelRemetente.setMargin(new Insets(0, 0, 0, 0));
        this.jchkTelDestinatario.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        this.jchkTelDestinatario.setText("do destinatário");
        this.jchkTelDestinatario.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.jchkTelDestinatario.setEnabled(false);
        this.jchkTelDestinatario.setMargin(new Insets(0, 0, 0, 0));
        carregaRemetente();
        this.jtxtNumeroVezes.setDocument(new DocumentoPersonalizado(4, 1));
        jLabel6.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel6.setText("Quero imprimir a partir da etiqueta nº:");
        this.jtxtNumeroEtiqueta.setDocument(new DocumentoPersonalizado(3, 1));
        GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(1)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(1)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(this.jlSelecioneRemetente)
                        .addPreferredGap(0)
                        .add(this.jcmbRemetente, -2, -1, -2))
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(this.jlNumeroVezes)
                        .addPreferredGap(0)
                        .add(this.jtxtNumeroVezes, -2, 66, -2))
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jLabel6)
                        .addPreferredGap(0)
                        .add(this.jtxtNumeroEtiqueta, -2, 66, -2))
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jLabel5)
                        .addPreferredGap(0)
                        .add(this.jchkTelDestinatario)
                        .addPreferredGap(0)
                        .add(this.jchkTelRemetente)))
                .addContainerGap(105, 32767)));
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(1)
            .add(jPanel3Layout.createSequentialGroup()
                .add(jPanel3Layout.createParallelGroup(3)
                    .add(this.jlSelecioneRemetente)
                    .add(this.jcmbRemetente, -2, -1, -2))
                .addPreferredGap(0, -1, 32767)
                .add(jPanel3Layout.createParallelGroup(3)
                    .add(jLabel6)
                    .add(this.jtxtNumeroEtiqueta, -2, -1, -2))
                .addPreferredGap(0)
                .add(jPanel3Layout.createParallelGroup(3)
                    .add(this.jlNumeroVezes)
                    .add(this.jtxtNumeroVezes, -2, -1, -2))
                .add(17, 17, 17)
                .add(jPanel3Layout.createParallelGroup(3)
                    .add(jLabel5)
                    .add(this.jchkTelDestinatario)
                    .add(this.jchkTelRemetente))
                .add(23, 23, 23)));
        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(1)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel2, -2, 353, -2)
                .addPreferredGap(0).add(jPanel3, -1, -1, 32767)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(1)
            .add(jPanel2, -1, -1, 32767)
            .add(jPanel3, -1, -1, 32767));
        this.jtblDestinatarioImpressao.setModel(new DefaultTableModel(new Object[][]{{null, null, null, null}, {null, null, null, null}, {null, null, null, null}, {null, null, null, null},}, new String[]{"Title 1", "Title 2", "Title 3", "Title 4"}));
        this.jtblDestinatarioImpressao.setModel(this.model);
        jScrollPane1.setViewportView(this.jtblDestinatarioImpressao);
        jPanel4.setBorder(BorderFactory.createEtchedBorder());
        jLabel7.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel7.setText("Quero imprimir etiquetas de:");
        jbtgTipoImpressao.add(this.jrbtRemetente);
        this.jrbtRemetente.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        this.jrbtRemetente.setSelected(true);
        this.jrbtRemetente.setText("Remetente");
        this.jrbtRemetente.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.jrbtRemetente.setMargin(new Insets(0, 0, 0, 0));
        this.jrbtRemetente.addActionListener(TelaImpressaoEnvelope.this::jrbtRemetenteActionPerformed);
        jbtgTipoImpressao.add(this.jrbtDestinatario);
        this.jrbtDestinatario.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        this.jrbtDestinatario.setText("Destinatário");
        this.jrbtDestinatario.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.jrbtDestinatario.setMargin(new Insets(0, 0, 0, 0));
        this.jrbtDestinatario.addActionListener(TelaImpressaoEnvelope.this::jrbtDestinatarioActionPerformed);
        jbtgTipoImpressao.add(this.jrbtRemetenteDestinatario);
        this.jrbtRemetenteDestinatario.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        this.jrbtRemetenteDestinatario.setText("Remetente e destinatário");
        this.jrbtRemetenteDestinatario.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.jrbtRemetenteDestinatario.setMargin(new Insets(0, 0, 0, 0));
        this.jrbtRemetenteDestinatario.addActionListener(TelaImpressaoEnvelope.this::jrbtRemetenteDestinatarioActionPerformed);
        GroupLayout jPanel4Layout = new GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(1)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel7).add(17, 17, 17)
                .add(this.jrbtRemetente)
                .add(95, 95, 95)
                .add(this.jrbtDestinatario)
                .add(88, 88, 88)
                .add(this.jrbtRemetenteDestinatario)
                .addContainerGap(172, 32767)));
        jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(1)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel4Layout.createParallelGroup(3)
                    .add(this.jrbtRemetenteDestinatario)
                    .add(jLabel7)
                    .add(this.jrbtRemetente)
                    .add(this.jrbtDestinatario))
                .addContainerGap(-1, 32767)));
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(1)
            .add(jToolBar1, -1, 800, 32767)
            .add(jPanel4, -1, -1, 32767)
            .add(jPanel1, -1, -1, 32767)
            .add(jScrollPane1, -1, 800, 32767));
        layout.setVerticalGroup(layout.createParallelGroup(1)
            .add(layout.createSequentialGroup()
                .add(jToolBar1, -2, 59, -2)
                .addPreferredGap(0)
                .add(jPanel4, -2, -1, -2)
                .addPreferredGap(0)
                .add(jPanel1, -2, -1, -2)
                .addPreferredGap(0)
                .add(jScrollPane1, -1, 183, 32767)));
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

    private String getSiglaTamanhoFonte(String tamanhoFonte) {
        return tamanhoFonte.substring(0, 1);
    }

    private void jbtVisualizarActionPerformed(ActionEvent evt) {
        Impressao impressao = new Impressao();
        if ((this.jrbtDestinatario.isSelected() || this.jrbtRemetenteDestinatario.isSelected()) && this.vecDestinatarioImpressao.size() < 1) {
            JOptionPane.showMessageDialog(this, "Não existe nenhum destinatário selecionado!", "Endereçador", JOptionPane.WARNING_MESSAGE);
            this.jtxtNumeroVezes.requestFocus();
            return;
        }
        try {
            if (this.jtxtNumeroEtiqueta.getText().trim().equals(""))
                this.jtxtNumeroEtiqueta.setText("1");
            if (this.jtxtNumeroVezes.getText().trim().equals(""))
                this.jtxtNumeroVezes.setText("1");
            if (this.jrbt14Rotulos.isSelected() && Integer.parseInt(this.jtxtNumeroEtiqueta.getText()) > 14) {
                JOptionPane.showMessageDialog(this, "Posição inicial para impressão de etiquetas deve ser um número de 1 a 14.", "Endereçador", 2);
                return;
            }
            if (this.jrbt10Rotulos.isSelected() && Integer.parseInt(this.jtxtNumeroEtiqueta.getText()) > 10) {
                JOptionPane.showMessageDialog(this, "Posição inicial para impressão de etiquetas deve ser um número de 1 a 10.", "Endereçador", 2);
                return;
            }
            if (this.jrbtDestinatario.isSelected()) {
                impressao.impressaoCarta(this.relatorio, null, this.vecDestinatarioImpressao, 0, Integer.parseInt(this.jtxtNumeroEtiqueta.getText()), false, this.jchkTelDestinatario.isSelected(), this.imprimirTratamento, ((String) Objects.requireNonNull(this.jcmbTamanhoFonte.getSelectedItem())).substring(0, 1));
            } else if (this.jrbtRemetente.isSelected()) {
                if (this.jcmbRemetente.getItemCount() == 0) {
                    JOptionPane.showMessageDialog(this, "Por favor cadastre um remetente antes de fazer a impressão.", "Endereçador ECT", 2);
                    return;
                }
                impressao.impressaoCarta(this.relatorio, (RemetenteBean) this.jcmbRemetente.getSelectedItem(), null, Integer.parseInt(this.jtxtNumeroVezes.getText()), Integer.parseInt(this.jtxtNumeroEtiqueta.getText()), this.jchkTelRemetente.isSelected(), false, this.imprimirTratamento, ((String) Objects.requireNonNull(this.jcmbTamanhoFonte.getSelectedItem())).substring(0, 1));
            } else {
                if (this.jcmbRemetente.getItemCount() == 0) {
                    JOptionPane.showMessageDialog(this, "Por favor cadastre um remetente antes de fazer a impressão.", "Endereçador ECT", 2);
                    return;
                }
                impressao.impressaoCarta(this.relatorio, (RemetenteBean) this.jcmbRemetente.getSelectedItem(), this.vecDestinatarioImpressao, Integer.parseInt(this.jtxtNumeroVezes.getText()), Integer.parseInt(this.jtxtNumeroEtiqueta.getText()), this.jchkTelRemetente.isSelected(), this.jchkTelDestinatario.isSelected(), this.imprimirTratamento, ((String) Objects.requireNonNull(this.jcmbTamanhoFonte.getSelectedItem())).substring(0, 1));
            }
        } catch (EnderecadorExcecao ex) {
            logger.error(ex.getMessage(), (Throwable) ex);
        }
    }

    private void jcmbFolhaActionPerformed(ActionEvent evt) {
        if (Objects.equals(this.jcmbFolha.getSelectedItem(), "A4")) {
            if (this.jrbt14Rotulos.isSelected()) {
                this.relatorio = "A4Envelope14.jasper";
            } else if (this.jrbt10Rotulos.isSelected()) {
                this.relatorio = "A4Envelope10.jasper";
            }
        } else if (this.jrbt14Rotulos.isSelected()) {
            this.relatorio = "CartaEnvelope14.jasper";
        } else if (this.jrbt10Rotulos.isSelected()) {
            this.relatorio = "CartaEnvelope10.jasper";
        }
    }

    private void jrbtRemetenteDestinatarioActionPerformed(ActionEvent evt) {
        this.jbtSelecionarDestinatario.setEnabled(true);
        this.jbtRemoverDestinatario.setEnabled(true);
        this.jbtSelecionarGrupo.setEnabled(true);
        this.jbtRemoverTodos.setEnabled(true);
        this.jtxtNumeroVezes.setEnabled(true);
        this.jlNumeroVezes.setEnabled(true);
        this.jcmbRemetente.setEnabled(true);
        this.jcmbRemetente.setEnabled(true);
        this.jlSelecioneRemetente.setEnabled(true);
        this.jtxtNumeroVezes.setBackground(Color.white);
        this.jchkTelDestinatario.setEnabled(true);
        this.jchkTelRemetente.setEnabled(true);
        this.jbtnVisializarAR.setEnabled(true);
    }

    private void jrbtDestinatarioActionPerformed(ActionEvent evt) {
        this.jbtSelecionarDestinatario.setEnabled(true);
        this.jbtRemoverDestinatario.setEnabled(true);
        this.jbtSelecionarGrupo.setEnabled(true);
        this.jbtRemoverTodos.setEnabled(true);
        this.jtxtNumeroVezes.setEnabled(false);
        this.jtxtNumeroVezes.setText("");
        this.jtxtNumeroVezes.setBackground(Color.lightGray);
        this.jlNumeroVezes.setEnabled(false);
        this.jcmbRemetente.setEnabled(false);
        this.jlSelecioneRemetente.setEnabled(false);
        this.jchkTelDestinatario.setEnabled(true);
        this.jchkTelRemetente.setEnabled(false);
        this.jbtnVisializarAR.setEnabled(false);
    }

    private void jrbtRemetenteActionPerformed(ActionEvent evt) {
        this.jbtSelecionarDestinatario.setEnabled(false);
        this.jbtRemoverDestinatario.setEnabled(false);
        this.jbtSelecionarGrupo.setEnabled(false);
        this.jbtRemoverTodos.setEnabled(false);
        this.jtxtNumeroVezes.setEnabled(true);
        this.jlNumeroVezes.setEnabled(true);
        this.jcmbRemetente.setEnabled(true);
        this.jtxtNumeroVezes.setBackground(Color.white);
        this.jlSelecioneRemetente.setEnabled(true);
        this.vecDestinatarioImpressao.removeAllElements();
        this.model.setDestinatario(this.vecDestinatarioImpressao);
        this.jchkTelDestinatario.setEnabled(false);
        this.jchkTelRemetente.setEnabled(true);
        this.jbtnVisializarAR.setEnabled(false);
    }

    private void jrbt10RotulosActionPerformed(ActionEvent evt) {
        this.jlblImpressao.setIcon(new ImageIcon(getClass().getResource("/imagens/tipo_10etq.gif")));
        if (Objects.equals(this.jcmbFolha.getSelectedItem(), "A4")) {
            this.relatorio = "A4Envelope10.jasper";
        } else {
            this.relatorio = "CartaEnvelope10.jasper";
        }
        this.imprimirTratamento = true;
    }

    private void jrbt14RotulosActionPerformed(ActionEvent evt) {
        this.jlblImpressao.setIcon(new ImageIcon(getClass().getResource("/imagens/tipo_14etq.gif")));
        if (Objects.equals(this.jcmbFolha.getSelectedItem(), "A4")) {
            this.relatorio = "A4Envelope14.jasper";
        } else {
            this.relatorio = "CartaEnvelope14.jasper";
        }
        this.imprimirTratamento = false;
    }

    private void jbtRemoverTodosActionPerformed(ActionEvent evt) {
        this.vecDestinatarioImpressao.removeAllElements();
        this.model.setDestinatario(this.vecDestinatarioImpressao);
        this.jtblDestinatarioImpressao.setModel(this.model);
    }

    private void jbtRemoverDestinatarioActionPerformed(ActionEvent evt) {
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

    private void jbtSelecionarGrupoActionPerformed(ActionEvent evt) {
        TelaPesquisarGrupo telaPesquisarGrupo = new TelaPesquisarGrupo(this.frmParent, true, this.vecDestinatarioImpressao);
        telaPesquisarGrupo.setVisible(true);
        this.model.setDestinatario(this.vecDestinatarioImpressao);
        this.jtblDestinatarioImpressao.setModel(this.model);
    }

    private void jbtSelecionarDestinatarioActionPerformed(ActionEvent evt) {
        TelaPesquisarDestinatario telaPesquisaDestinatario = new TelaPesquisarDestinatario(this.frmParent, true, this.vecDestinatarioImpressao);
        telaPesquisaDestinatario.setVisible(true);
        this.model.setDestinatario(this.vecDestinatarioImpressao);
        this.jtblDestinatarioImpressao.setModel(this.model);
    }

    private void jcmbTamanhoFonteActionPerformed(ActionEvent evt) {
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
