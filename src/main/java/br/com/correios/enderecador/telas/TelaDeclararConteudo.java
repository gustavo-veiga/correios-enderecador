package br.com.correios.enderecador.telas;

import br.com.correios.enderecador.bean.DestinatarioBean;
import br.com.correios.enderecador.bean.RemetenteBean;
import br.com.correios.enderecador.excecao.EnderecadorExcecao;
import br.com.correios.enderecador.util.Impressao;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.LayoutStyle;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

public class TelaDeclararConteudo extends JDialog {
    private static final Logger LOGGER = Logger.getLogger(TelaDeclararConteudo.class);

    private final RemetenteBean remetente;

    private final DefaultTableModel model;

    private Vector<DestinatarioBean> vecDestinatario;

    private JTable jTable1;

    private JTextField txtPesoTotal;

    public TelaDeclararConteudo(Frame parent, boolean modal, Vector<DestinatarioBean> vecDestinatario, RemetenteBean remetente) {
        super(parent, modal);
        this.vecDestinatario = new Vector<>();
        String[][] data = new String[][]{{null, null, null}, {null, null, null}, {null, null, null}, {null, null, null}, {null, null, null}, {null, null, null}};
        String[] columnNames = new String[]{"Conteúdo", "Quantidade", "Valor"};
        this.model = new DefaultTableModel(data, columnNames);
        initComponents();
        this.remetente = remetente;
        this.vecDestinatario = vecDestinatario;
    }

    private void initComponents() {
        JToolBar jToolBar1 = new JToolBar();
        JButton jButton1 = new JButton();
        JButton jButton2 = new JButton();
        JScrollPane jScrollPane1 = new JScrollPane();
        this.jTable1 = new JTable();
        this.txtPesoTotal = new JTextField();
        JLabel jLabel1 = new JLabel();
        JLabel jLabel3 = new JLabel();
        setDefaultCloseOperation(0);
        setTitle("Declaração de Conteúdo");
        setResizable(false);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                TelaDeclararConteudo.this.fecharJanela(evt);
            }
        });
        jToolBar1.setRollover(true);
        jButton1.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        jButton1.setIcon(new ImageIcon(getClass().getResource("/imagens/print.gif")));
        jButton1.setText("Imprimir");
        jButton1.setHorizontalTextPosition(0);
        jButton1.setMaximumSize(new Dimension(90, 60));
        jButton1.setMinimumSize(new Dimension(47, 55));
        jButton1.setVerticalTextPosition(3);
        jButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                TelaDeclararConteudo.this.jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);
        jButton2.setFont(new Font(Font.SANS_SERIF, 0, 9));
        jButton2.setIcon(new ImageIcon(getClass().getResource("/imagens/remover.gif")));
        jButton2.setText("Excluir item");
        jButton2.setHorizontalTextPosition(0);
        jButton2.setMaximumSize(new Dimension(90, 60));
        jButton2.setMinimumSize(new Dimension(47, 55));
        jButton2.setVerticalTextPosition(3);
        jButton2.addActionListener(TelaDeclararConteudo.this::jButton2ActionPerformed);
        jToolBar1.add(jButton2);
        jScrollPane1.setName("");
        this.jTable1.setModel(this.model);
        this.jTable1.setSelectionMode(0);
        jScrollPane1.setViewportView(this.jTable1);
        this.txtPesoTotal.setHorizontalAlignment(4);
        this.txtPesoTotal.setText("0");
        jLabel1.setText("Peso total (kg):");
        jLabel3.setForeground(new Color(51, 51, 255));
        jLabel3.setText("* Após o preenchimento de cada campo, pressionar a tecla <tab>");
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar1, GroupLayout.Alignment.TRAILING, -1, -1, 32767)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(this.txtPesoTotal, -2, 78, -2))
                    .addComponent(jScrollPane1, -2, 672, -2)))
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(-1, 32767)
                .addComponent(jLabel3).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, -2, 57, -2)
                .addGap(1, 1, 1)
                .addComponent(jLabel3, -2, 14, -2)
                .addGap(3, 3, 3)
                .addComponent(jScrollPane1, -2, 159, -2)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(this.txtPesoTotal, -2, -1, -2)
                    .addComponent(jLabel1)).addGap(30, 30, 30)));
        jScrollPane1.getAccessibleContext().setAccessibleName("");
        getAccessibleContext().setAccessibleDescription("");
        setSize(new Dimension(689, 328));
        setLocationRelativeTo(null);
    }

    private void jButton1ActionPerformed(ActionEvent evt) {
        Impressao impressao = new Impressao();
        Vector<Vector> itens = this.model.getDataVector();
        try {
            impressao.imprimirDeclaracao("declaracao", itens, this.remetente, this.vecDestinatario, this.txtPesoTotal.getText());
        } catch (EnderecadorExcecao ex) {
            LOGGER.error(ex.getMessage(), (Throwable) ex);
            JOptionPane.showMessageDialog(this, "Não foi possível imprimir a declaração de conteúdo", "Endereçador", JOptionPane.WARNING_MESSAGE);
        }
        setVisible(false);
    }

    private void jButton2ActionPerformed(ActionEvent evt) {
        if (this.jTable1.getSelectedRow() != -1) {
            this.model.removeRow(this.jTable1.getSelectedRow());
            Object[] novaLinha = {null, null, null};
            this.model.addRow(novaLinha);
        }
    }

    private void fecharJanela(WindowEvent evt) {
        int resposta = JOptionPane.showConfirmDialog(null, "A declaração de conteúdo não é armazenada. Deseja sair?", "Fechar Declaração de Conteúdo", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (resposta == 0)
            setDefaultCloseOperation(2);
    }
}
