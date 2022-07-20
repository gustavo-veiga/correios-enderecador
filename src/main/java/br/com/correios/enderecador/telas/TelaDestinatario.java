package br.com.correios.enderecador.telas;

import br.com.correios.enderecador.bean.DestinatarioBean;
import br.com.correios.enderecador.dao.DaoException;
import br.com.correios.enderecador.dao.DestinatarioDao;
import br.com.correios.enderecador.dao.GrupoDestinatarioDao;
import br.com.correios.enderecador.util.EnderecadorObservable;
import br.com.correios.enderecador.util.TextoCellRenderer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

public class TelaDestinatario extends JFrame implements Observer {
    private static final Logger logger = Logger.getLogger(TelaDestinatario.class);

    private static TelaDestinatario instance;

    private final DestinatarioTableModel destinatarioTableModel;

    private final Frame frmParent;

    private final EnderecadorObservable observable;

    private String ultimaConsulta;

    private JScrollPane jScrollPane1;

    private JTextField jtxtNomeDestinatario;

    private JTable tabDestinatario;

    private TelaDestinatario(Frame parent) {
        this.destinatarioTableModel = new DestinatarioTableModel();
        this.observable = EnderecadorObservable.getInstance();
        this.ultimaConsulta = "";
        this.frmParent = parent;
        initComponents();
        recuperarDadosTabelaDestinatario();
        this.observable.addObserver(this);
    }

    public static TelaDestinatario getInstance(Frame parent) {
        if (instance == null)
            instance = new TelaDestinatario(parent);
        return instance;
    }

    private void recuperarDadosTabelaDestinatario() {
        try {
            ArrayList<DestinatarioBean> arrayDestinatario = DestinatarioDao.getInstance().recuperaDestinatario("");
            this.destinatarioTableModel.setDestinatario(arrayDestinatario);
            this.tabDestinatario.setSelectionMode(2);
            TextoCellRenderer renderer = new TextoCellRenderer(2);
            TableColumn coluna = null;
            coluna = this.tabDestinatario.getColumnModel().getColumn(0);
            coluna.setCellRenderer(renderer);
            coluna = this.tabDestinatario.getColumnModel().getColumn(1);
            coluna.setPreferredWidth(70);
            coluna.setCellRenderer(renderer);
            coluna = this.tabDestinatario.getColumnModel().getColumn(2);
            coluna.setCellRenderer(renderer);
            coluna.setPreferredWidth(70);
            coluna = this.tabDestinatario.getColumnModel().getColumn(3);
            coluna.setCellRenderer(renderer);
            coluna = this.tabDestinatario.getColumnModel().getColumn(4);
            coluna.setCellRenderer(renderer);
            coluna.setPreferredWidth(5);
            coluna = this.tabDestinatario.getColumnModel().getColumn(5);
            coluna.setCellRenderer(renderer);
            coluna.setPreferredWidth(1);
            coluna.setWidth(1);
        } catch (DaoException e) {
            logger.error(e.getMessage(), e);
            JOptionPane.showMessageDialog(this, e.getMessage(), "Não foi possivel carregar relação de destinatários", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void initComponents() {
        JPanel jPanel1 = new JPanel();
        JPanel jPanel2 = new JPanel();
        JLabel jLabel1 = new JLabel();
        this.jtxtNomeDestinatario = new JTextField();
        JLabel jLabel2 = new JLabel();
        this.jScrollPane1 = new JScrollPane();
        this.tabDestinatario = new JTable();
        JToolBar jToolBar1 = new JToolBar();
        JButton jbtNovo = new JButton();
        JButton jbtEditar = new JButton();
        JButton jbtPesquisar = new JButton();
        JButton jbtExcluir = new JButton();
        setResizable(true);
        setTitle("Cadastro de destinatário");
        setPreferredSize(new Dimension(744, 434));
        jPanel1.setLayout(new BorderLayout());
        jPanel1.setBorder(BorderFactory.createEtchedBorder());
        jPanel2.setLayout(new AbsoluteLayout());
        jLabel1.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel1.setText("Procurar por:");
        jPanel2.add(jLabel1, new AbsoluteConstraints(10, 10, -1, 20));
        jPanel2.add(this.jtxtNomeDestinatario, new AbsoluteConstraints(90, 10, 300, -1));
        jLabel2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel2.setText("Destinatários:");
        jPanel2.add(jLabel2, new AbsoluteConstraints(10, 30, -1, 30));
        jPanel1.add(jPanel2, "North");
        this.tabDestinatario.setModel(new DefaultTableModel(new Object[][]{{null, null, null, null}, {null, null, null, null}, {null, null, null, null}, {null, null, null, null},}, new String[]{"Title 1", "Title 2", "Title 3", "Title 4"}));
        this.tabDestinatario.setModel(this.destinatarioTableModel);
        this.jScrollPane1.setViewportView(this.tabDestinatario);
        jPanel1.add(this.jScrollPane1, "Center");
        getContentPane().add(jPanel1, "Center");
        jbtNovo.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        jbtNovo.setIcon(new ImageIcon(getClass().getResource("/imagens/usuario.gif")));
        jbtNovo.setText("Novo destinatário");
        jbtNovo.setHorizontalTextPosition(0);
        jbtNovo.setMaximumSize(new Dimension(90, 60));
        jbtNovo.setMinimumSize(new Dimension(87, 47));
        jbtNovo.setPreferredSize(new Dimension(53, 51));
        jbtNovo.setVerticalTextPosition(3);
        jbtNovo.addActionListener(TelaDestinatario.this::jbtNovoActionPerformed);
        jToolBar1.add(jbtNovo);
        jbtEditar.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        jbtEditar.setIcon(new ImageIcon(getClass().getResource("/imagens/editar.gif")));
        jbtEditar.setText("Editar");
        jbtEditar.setHorizontalTextPosition(0);
        jbtEditar.setMaximumSize(new Dimension(90, 60));
        jbtEditar.setVerticalTextPosition(3);
        jbtEditar.addActionListener(TelaDestinatario.this::jbtEditarActionPerformed);
        jToolBar1.add(jbtEditar);
        jbtPesquisar.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        jbtPesquisar.setIcon(new ImageIcon(getClass().getResource("/imagens/binoculo.gif")));
        jbtPesquisar.setText("Pesquisar");
        jbtPesquisar.setHorizontalTextPosition(0);
        jbtPesquisar.setMaximumSize(new Dimension(90, 60));
        jbtPesquisar.setMinimumSize(new Dimension(47, 55));
        jbtPesquisar.setVerticalTextPosition(3);
        jbtPesquisar.addActionListener(TelaDestinatario.this::jbtPesquisarActionPerformed);
        jToolBar1.add(jbtPesquisar);
        jbtExcluir.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        jbtExcluir.setIcon(new ImageIcon(getClass().getResource("/imagens/TRASH.gif")));
        jbtExcluir.setText("Excluir");
        jbtExcluir.setHorizontalTextPosition(0);
        jbtExcluir.setMaximumSize(new Dimension(90, 60));
        jbtExcluir.setMinimumSize(new Dimension(47, 55));
        jbtExcluir.setVerticalTextPosition(3);
        jbtExcluir.addActionListener(TelaDestinatario.this::jbtExcluirActionPerformed);
        jToolBar1.add(jbtExcluir);
        getContentPane().add(jToolBar1, "North");
        pack();
    }

    private void jbtNovoActionPerformed(ActionEvent evt) {
        TelaEditarDestinatario telaEditarDestinatrio = new TelaEditarDestinatario(this.frmParent, true);
        telaEditarDestinatrio.setVisible(true);
    }

    private void jbtEditarActionPerformed(ActionEvent evt) {
        if (this.tabDestinatario.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Não existe nenhum remetente selecionado!", "Endereçador ECT", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        TelaEditarDestinatario telaEditarDestinatrio = new TelaEditarDestinatario(this.frmParent, true, this.destinatarioTableModel.getDestinatario(this.tabDestinatario.getSelectedRow()));
        telaEditarDestinatrio.setVisible(true);
    }

    private void jbtPesquisarActionPerformed(ActionEvent evt) {
        ListSelectionModel lsSelectionModelDestinatario = this.tabDestinatario.getSelectionModel();
        String nomeDestinatario = this.jtxtNomeDestinatario.getText().trim();
        int numeroRows = this.tabDestinatario.getRowCount();
        int index = -1;
        if (this.ultimaConsulta.equalsIgnoreCase(nomeDestinatario)) {
            index = this.tabDestinatario.getSelectedRow();
        } else {
            this.ultimaConsulta = nomeDestinatario;
        }
        for (int i = index + 1; i < numeroRows; i++) {
            String nomeDestinatarioTabela = (String) this.tabDestinatario.getValueAt(i, 0);
            if (nomeDestinatarioTabela.toUpperCase().contains(nomeDestinatario.toUpperCase())) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            JOptionPane.showMessageDialog(this.frmParent, "Destinatário não encontrado!", "Endereçador", JOptionPane.INFORMATION_MESSAGE);
        } else {
            lsSelectionModelDestinatario.setSelectionInterval(index, index);
            int tamanhoJscrool = this.jScrollPane1.getVerticalScrollBar().getMaximum();
            int value = 0;
            value = index * tamanhoJscrool / numeroRows;
            this.jScrollPane1.getVerticalScrollBar().setValue(value);
        }
    }

    private void jbtExcluirActionPerformed(ActionEvent evt) {
        DestinatarioBean destinatarioBean = null;
        List<DestinatarioBean> listaDestinatarios = new ArrayList<>();
        if (this.tabDestinatario.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this.frmParent, "Não existe nenhum destinatário selecionado!", "Enderecador", JOptionPane.WARNING_MESSAGE);
        } else {
            String[] options = {"Sim", "Não"};
            int resp = JOptionPane.showOptionDialog(this.frmParent, "Tem certeza que deseja excluir o(s) destinatário(s)?", "Endereçador", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
            if (resp == 0) {
                int[] linhasSelecionadas = this.tabDestinatario.getSelectedRows();
                try {
                    for (int linhasSelecionada : linhasSelecionadas) {
                        destinatarioBean = this.destinatarioTableModel.getDestinatario(linhasSelecionada);
                        GrupoDestinatarioDao.getInstance().excluirDestinatarioDoGrupo(destinatarioBean.getNumeroDestinatario());
                        DestinatarioDao.getInstance().excluirDestinatario(destinatarioBean.getNumeroDestinatario());
                        listaDestinatarios.add(destinatarioBean);
                    }
                    this.observable.notifyObservers(listaDestinatarios);
                    this.jtxtNomeDestinatario.setText("");
                    JOptionPane.showMessageDialog(this.frmParent, "Destinatário(s) excluído(s) com sucesso!", "Endereçador", JOptionPane.INFORMATION_MESSAGE);
                } catch (DaoException ex) {
                    logger.error(ex.getMessage(), (Throwable) ex);
                    JOptionPane.showMessageDialog(this.frmParent, "Não foi possivel carregar relação de destinatários!", "Endereçador ECT", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }

    public void update(Observable o, Object arg) {
        if (arg instanceof DestinatarioBean) {
            DestinatarioBean destinatario = (DestinatarioBean) arg;
            int index = this.destinatarioTableModel.indexOf(destinatario);
            if (index != -1) {
                this.destinatarioTableModel.setDestinatario(index, destinatario);
            } else {
                this.destinatarioTableModel.addDestinatario(destinatario);
            }
        } else if (arg instanceof List) {
            List<DestinatarioBean> listaDestinatarios = (List<DestinatarioBean>) arg;
            for (DestinatarioBean listaDestinatario : listaDestinatarios) {
                if (listaDestinatario != null)
                    this.destinatarioTableModel.removeDestinatario(listaDestinatario);
            }
        }
    }
}
