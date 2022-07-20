package br.com.correios.enderecador.telas;

import br.com.correios.enderecador.bean.DestinatarioBean;
import br.com.correios.enderecador.bean.RemetenteBean;
import br.com.correios.enderecador.dao.DaoException;
import br.com.correios.enderecador.dao.RemetenteDao;
import br.com.correios.enderecador.excecao.EnderecadorExcecao;
import br.com.correios.enderecador.util.EnderecadorObservable;
import br.com.correios.enderecador.util.Impressao;
import br.com.correios.enderecador.util.TextoCellRenderer;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;
import org.jdesktop.layout.GroupLayout;

public class TelaImpressaoDiretaEnvelope extends JFrame implements Observer {
    private final DestinatarioImpressaoTableModel model = new DestinatarioImpressaoTableModel("C");

    static Logger logger = Logger.getLogger(TelaEditarRemetente.class);

    private final Frame frmParent;

    private Vector<DestinatarioBean> vecDestinatarioImpressao = new Vector<>();

    private static TelaImpressaoDiretaEnvelope instance;

    private JCheckBox jchkImprimirRemetente;

    private JCheckBox jchkTelDestinatario;

    private JComboBox<String> jcmbRemetente;

    private JComboBox<String> jcmbTamanhoFonte;

    private JLabel jlblEnvelope;

    private JRadioButton jrbEnvelopeC5;

    private JRadioButton jrbEnvelopeC6;

    private JTable jtblDestinatarioImpressao;

    private TelaImpressaoDiretaEnvelope(Frame parent) {
        this.frmParent = parent;
        initComponents();
        configuracoesAdicionais();
        carregaRemetente();
        EnderecadorObservable observable = EnderecadorObservable.getInstance();
        observable.addObserver(this);
    }

    public static TelaImpressaoDiretaEnvelope getInstance(Frame parent) {
        if (instance == null)
            instance = new TelaImpressaoDiretaEnvelope(parent);
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
            JOptionPane.showMessageDialog(this.frmParent, "Não foi possivel carregar relação de remetentes", "Endereçador ECT", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void imprimirEnvelope(String nomeArquivo) {
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            Impressao impressao = new Impressao();
            if (this.jchkImprimirRemetente.isSelected()) {
                if (this.jcmbRemetente.getItemCount() == 0) {
                    JOptionPane.showMessageDialog(this, "Por favor cadastre um remetente antes de fazer a impressão.", "Endereçador ECT", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                impressao.imprimirEnvelope(nomeArquivo, (RemetenteBean) this.jcmbRemetente.getSelectedItem(), this.vecDestinatarioImpressao, this.jchkTelDestinatario.isSelected(), ((String) Objects.requireNonNull(this.jcmbTamanhoFonte.getSelectedItem())).substring(0, 1));
            } else {
                impressao.imprimirEnvelope(nomeArquivo, null, this.vecDestinatarioImpressao, this.jchkTelDestinatario.isSelected(), ((String) Objects.requireNonNull(this.jcmbTamanhoFonte.getSelectedItem())).substring(0, 1));
            }
        } catch (EnderecadorExcecao e) {
            logger.error(e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Não foi possivel imprimir.", "Endereçador ECT", 2);
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private void initComponents() {
        ButtonGroup buttonGroup1 = new ButtonGroup();
        JToolBar jToolBar1 = new JToolBar();
        JButton jbtSelecionarDestinatario = new JButton();
        JButton jbtSelecionarGrupo = new JButton();
        JButton jbtRemoverDestinatario = new JButton();
        JButton jbtRemoverTodos = new JButton();
        JButton jbtnVisializarAR = new JButton();
        JButton jbtVisualizar = new JButton();
        JPanel jPanel1 = new JPanel();
        JPanel jPanel2 = new JPanel();
        this.jchkTelDestinatario = new JCheckBox();
        JRadioButton jrbEnvelopeC6C5 = new JRadioButton();
        this.jrbEnvelopeC5 = new JRadioButton();
        this.jrbEnvelopeC6 = new JRadioButton();
        this.jlblEnvelope = new JLabel();
        JPanel jPanel3 = new JPanel();
        JLabel jlblImprimirAR = new JLabel();
        this.jcmbRemetente = new JComboBox<>();
        this.jchkImprimirRemetente = new JCheckBox();
        JLabel jLabelTamanhoFonte = new JLabel();
        this.jcmbTamanhoFonte = new JComboBox<>();
        JScrollPane jScrollPane1 = new JScrollPane();
        this.jtblDestinatarioImpressao = new JTable();
        setResizable(true);
        setTitle("Impressão direta no envelope");
        jbtSelecionarDestinatario.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        jbtSelecionarDestinatario.setIcon(new ImageIcon(getClass().getResource("/imagens/addusuario.gif")));
        jbtSelecionarDestinatario.setText("Selecionar destinatário");
        jbtSelecionarDestinatario.setHorizontalTextPosition(0);
        jbtSelecionarDestinatario.setMaximumSize(new Dimension(110, 60));
        jbtSelecionarDestinatario.setVerticalTextPosition(3);
        jbtSelecionarDestinatario.addActionListener(TelaImpressaoDiretaEnvelope.this::jbtSelecionarDestinatarioActionPerformed);
        jToolBar1.add(jbtSelecionarDestinatario);
        jbtSelecionarGrupo.setFont(new Font("MS Sans Serif", 0, 9));
        jbtSelecionarGrupo.setIcon(new ImageIcon(getClass().getResource("/imagens/addusuarios.gif")));
        jbtSelecionarGrupo.setText("Selecionar grupo");
        jbtSelecionarGrupo.setHorizontalTextPosition(0);
        jbtSelecionarGrupo.setMaximumSize(new Dimension(90, 60));
        jbtSelecionarGrupo.setVerticalTextPosition(3);
        jbtSelecionarGrupo.addActionListener(TelaImpressaoDiretaEnvelope.this::jbtSelecionarGrupoActionPerformed);
        jToolBar1.add(jbtSelecionarGrupo);
        jbtRemoverDestinatario.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        jbtRemoverDestinatario.setIcon(new ImageIcon(getClass().getResource("/imagens/remover.gif")));
        jbtRemoverDestinatario.setText("Remover destinatário");
        jbtRemoverDestinatario.setHorizontalTextPosition(0);
        jbtRemoverDestinatario.setMaximumSize(new Dimension(110, 60));
        jbtRemoverDestinatario.setVerticalTextPosition(3);
        jbtRemoverDestinatario.addActionListener(TelaImpressaoDiretaEnvelope.this::jbtRemoverDestinatarioActionPerformed);
        jToolBar1.add(jbtRemoverDestinatario);
        jbtRemoverTodos.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        jbtRemoverTodos.setIcon(new ImageIcon(getClass().getResource("/imagens/removerTodos.gif")));
        jbtRemoverTodos.setText("Remover todos");
        jbtRemoverTodos.setHorizontalTextPosition(0);
        jbtRemoverTodos.setMaximumSize(new Dimension(90, 60));
        jbtRemoverTodos.setVerticalTextPosition(3);
        jbtRemoverTodos.addActionListener(TelaImpressaoDiretaEnvelope.this::jbtRemoverTodosActionPerformed);
        jToolBar1.add(jbtRemoverTodos);
        jbtnVisializarAR.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        jbtnVisializarAR.setIcon(new ImageIcon(getClass().getResource("/imagens/print.gif")));
        jbtnVisializarAR.setText("Visualizar AR");
        jbtnVisializarAR.setHorizontalTextPosition(0);
        jbtnVisializarAR.setMaximumSize(new Dimension(90, 60));
        jbtnVisializarAR.setVerticalTextPosition(3);
        jbtnVisializarAR.addActionListener(TelaImpressaoDiretaEnvelope.this::jbtnVisializarARActionPerformed);
        jToolBar1.add(jbtnVisializarAR);
        jbtVisualizar.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        jbtVisualizar.setIcon(new ImageIcon(getClass().getResource("/imagens/IMPRIMIR.gif")));
        jbtVisualizar.setText("Visualizar");
        jbtVisualizar.setHorizontalTextPosition(0);
        jbtVisualizar.setMaximumSize(new Dimension(90, 60));
        jbtVisualizar.setVerticalTextPosition(3);
        jbtVisualizar.addActionListener(TelaImpressaoDiretaEnvelope.this::jbtVisualizarActionPerformed);
        jToolBar1.add(jbtVisualizar);
        jPanel1.setBorder(BorderFactory.createEtchedBorder());
        jPanel2.setBorder(BorderFactory.createTitledBorder(null, "Escolha o tipo do envelope:", 0, 0, new Font("Tahoma", 0, 10)));
        this.jchkTelDestinatario.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        this.jchkTelDestinatario.setText("Incluir telefone");
        this.jchkTelDestinatario.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.jchkTelDestinatario.setMargin(new Insets(0, 0, 0, 0));
        buttonGroup1.add(jrbEnvelopeC6C5);
        jrbEnvelopeC6C5.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jrbEnvelopeC6C5.setText("Formato C6/C5 (114x229mm)");
        jrbEnvelopeC6C5.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jrbEnvelopeC6C5.setMargin(new Insets(0, 0, 0, 0));
        jrbEnvelopeC6C5.addActionListener(TelaImpressaoDiretaEnvelope.this::jrbEnvelopeC6C5ActionPerformed);
        buttonGroup1.add(this.jrbEnvelopeC5);
        this.jrbEnvelopeC5.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        this.jrbEnvelopeC5.setSelected(true);
        this.jrbEnvelopeC5.setText("Formato C5 (162x229mm)");
        this.jrbEnvelopeC5.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.jrbEnvelopeC5.setMargin(new Insets(0, 0, 0, 0));
        this.jrbEnvelopeC5.addActionListener(TelaImpressaoDiretaEnvelope.this::jrbEnvelopeC5ActionPerformed);
        buttonGroup1.add(this.jrbEnvelopeC6);
        this.jrbEnvelopeC6.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        this.jrbEnvelopeC6.setText("Formato C6 (114x162mm)");
        this.jrbEnvelopeC6.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.jrbEnvelopeC6.setMargin(new Insets(0, 0, 0, 0));
        this.jrbEnvelopeC6.addActionListener(TelaImpressaoDiretaEnvelope.this::jrbEnvelopeC6ActionPerformed);
        this.jlblEnvelope.setHorizontalAlignment(0);
        this.jlblEnvelope.setIcon(new ImageIcon(getClass().getResource("/imagens/envelopeC5.gif")));
        GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(1)
            .add(2, jPanel2Layout.createSequentialGroup()
                .add(this.jlblEnvelope, -2, 75, -2)
                .addPreferredGap(0, -1, 32767)
                .add(jPanel2Layout.createParallelGroup(1)
                    .add(this.jrbEnvelopeC6, -2, 150, -2)
                    .add(this.jrbEnvelopeC5))
                .addPreferredGap(0)
                .add(jPanel2Layout.createParallelGroup(1)
                    .add(this.jchkTelDestinatario)
                    .add(jrbEnvelopeC6C5))
                .add(171, 171, 171)));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(1)
            .add(jPanel2Layout.createSequentialGroup()
                .add(jPanel2Layout.createParallelGroup(1)
                    .add(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jPanel2Layout.createParallelGroup(3)
                            .add(this.jrbEnvelopeC5)
                            .add(jrbEnvelopeC6C5))
                        .addPreferredGap(0, -1, 32767)
                        .add(jPanel2Layout.createParallelGroup(3)
                            .add(this.jchkTelDestinatario)
                            .add(this.jrbEnvelopeC6))
                        .add(8, 8, 8))
                    .add(this.jlblEnvelope, -1, -1, 32767))
                .addContainerGap()));
        jPanel3.setBorder(BorderFactory.createTitledBorder(" "));
        jlblImprimirAR.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jlblImprimirAR.setText("Selecione o remetente:");
        carregaRemetente();
        this.jchkImprimirRemetente.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        this.jchkImprimirRemetente.setText("Imprimir remetente");
        this.jchkImprimirRemetente.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.jchkImprimirRemetente.setMargin(new Insets(0, 0, 0, 0));
        jLabelTamanhoFonte.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabelTamanhoFonte.setText("Tamanho da Fonte:");
        this.jcmbTamanhoFonte.setModel(new DefaultComboBoxModel<>(new String[]{"Pequeno", "Médio", "Grande"}));
        this.jcmbTamanhoFonte.addActionListener(TelaImpressaoDiretaEnvelope.this::jcmbTamanhoFonteActionPerformed);
        GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(1)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(1)
                    .add(jlblImprimirAR)
                    .add(jLabelTamanhoFonte)
                    .add(this.jchkImprimirRemetente))
                .addPreferredGap(0)
                .add(jPanel3Layout.createParallelGroup(1)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(this.jcmbTamanhoFonte, -2, 89, -2)
                        .add(0, 0, 32767))
                    .add(this.jcmbRemetente, 0, 182, 32767))
                .addContainerGap()));
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(1)
            .add(2, jPanel3Layout.createSequentialGroup()
                .add(this.jchkImprimirRemetente)
                .addPreferredGap(0, -1, 32767)
                .add(jPanel3Layout.createParallelGroup(3)
                    .add(jLabelTamanhoFonte)
                    .add(this.jcmbTamanhoFonte, -2, -1, -2))
                .addPreferredGap(0)
                .add(jPanel3Layout.createParallelGroup(3)
                    .add(jlblImprimirAR)
                    .add(this.jcmbRemetente, -2, -1, -2))
                .add(5, 5, 5)));
        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(1)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel2, -2, 409, -2)
                .addPreferredGap(0).add(jPanel3, -1, -1, 32767)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(1)
            .add(2, jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(2)
                    .add(1, jPanel2, -1, -1, 32767)
                    .add(jPanel3, -1, -1, 32767))
                .addContainerGap()));
        this.jtblDestinatarioImpressao.setModel(new DefaultTableModel(new Object[][]{{null, null, null, null}, {null, null, null, null}, {null, null, null, null}, {null, null, null, null},}, new String[]{"Title 1", "Title 2", "Title 3", "Title 4"}));
        this.jtblDestinatarioImpressao.setModel(this.model);
        jScrollPane1.setViewportView(this.jtblDestinatarioImpressao);
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(1)
            .add(jToolBar1, -1, -1, 32767)
            .add(jPanel1, -1, -1, 32767)
            .add(jScrollPane1));
        layout.setVerticalGroup(layout.createParallelGroup(1)
            .add(layout.createSequentialGroup()
                .add(jToolBar1, -2, 59, -2)
                .addPreferredGap(0).add(jPanel1, -2, 103, -2)
                .addPreferredGap(0).add(jScrollPane1, -1, 329, 32767)
                .addContainerGap()));
        pack();
    }

    private void jbtnVisializarARActionPerformed(ActionEvent evt) {
        Impressao impressao = new Impressao();
        if (this.jcmbRemetente.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "Por favor cadastre um remetente antes de fazer a impressão.", "Endereçador", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (this.vecDestinatarioImpressao.size() < 1) {
            JOptionPane.showMessageDialog(this, "Não existe nenhum destinatário selecionado!", "Endereçador", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            impressao.imprimirAR("avisoRecebimento.jasper", (RemetenteBean) this.jcmbRemetente.getSelectedItem(), this.vecDestinatarioImpressao);
        } catch (EnderecadorExcecao ex) {
            logger.error(ex.getMessage(), ex);
            JOptionPane.showMessageDialog(this, "Não foi possível imprimir o AR", "Endereçador", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void jbtVisualizarActionPerformed(ActionEvent evt) {
        if (this.vecDestinatarioImpressao.size() <= 0) {
            JOptionPane.showMessageDialog(this, "Não existe nenhum destinatário selecionado!", "Endereçador", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (this.jrbEnvelopeC5.isSelected()) {
            imprimirEnvelope("EnvelopeC5.jasper");
        } else if (this.jrbEnvelopeC6.isSelected()) {
            imprimirEnvelope("EnvelopeC6.jasper");
        } else {
            imprimirEnvelope("EnvelopeC6C5.jasper");
        }
    }

    private void jrbEnvelopeC6C5ActionPerformed(ActionEvent evt) {
        this.jlblEnvelope.setIcon(new ImageIcon(getClass().getResource("/imagens/envelopeC5C6.gif")));
    }

    private void jrbEnvelopeC5ActionPerformed(ActionEvent evt) {
        this.jlblEnvelope.setIcon(new ImageIcon(getClass().getResource("/imagens/envelopeC5.gif")));
    }

    private void jrbEnvelopeC6ActionPerformed(ActionEvent evt) {
        this.jlblEnvelope.setIcon(new ImageIcon(getClass().getResource("/imagens/envelopeC6.gif")));
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
