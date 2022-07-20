package br.com.correios.enderecador.telas;

import br.com.correios.enderecador.bean.RemetenteBean;
import br.com.correios.enderecador.dao.DaoException;
import br.com.correios.enderecador.dao.RemetenteDao;
import br.com.correios.enderecador.util.EnderecadorObservable;
import br.com.correios.enderecador.util.TextoCellRenderer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

public class TelaRemetente extends JInternalFrame implements Observer {
    private static final Logger logger = Logger.getLogger(TelaRemetente.class);

    private final RemetenteTableModel remetenteTableModel = new RemetenteTableModel();

    private final EnderecadorObservable observable = EnderecadorObservable.getInstance();

    private static TelaRemetente instance;

    private String ultimaConsulta = "";

    private final Frame frmParent;

    private JScrollPane jScrollPane1;

    private JTextField jtxtNomeRemetente;

    private JTable tabRemetente;

    private TelaRemetente(Frame parent) {
        this.frmParent = parent;
        initComponents();
        this.observable.addObserver(this);
    }

    public static TelaRemetente getInstance(Frame parent) {
        if (instance == null) {
            instance = new TelaRemetente(parent);
            instance.recuperarDadosTabelaRemetente();
        }
        return instance;
    }

    private void recuperarDadosTabelaRemetente() {
        try {
            ArrayList<RemetenteBean> arrayRemetente = RemetenteDao.getInstance().recuperaRemetente("");
            this.remetenteTableModel.setRemetente(arrayRemetente);
            this.tabRemetente.setSelectionMode(2);
            TextoCellRenderer renderer = new TextoCellRenderer(2);
            TableColumn coluna;
            coluna = this.tabRemetente.getColumnModel().getColumn(0);
            coluna.setCellRenderer(renderer);
            coluna = this.tabRemetente.getColumnModel().getColumn(1);
            coluna.setPreferredWidth(70);
            coluna.setCellRenderer(renderer);
            coluna = this.tabRemetente.getColumnModel().getColumn(2);
            coluna.setCellRenderer(renderer);
            coluna.setPreferredWidth(70);
            coluna = this.tabRemetente.getColumnModel().getColumn(3);
            coluna.setCellRenderer(renderer);
            coluna = this.tabRemetente.getColumnModel().getColumn(4);
            coluna.setCellRenderer(renderer);
            coluna.setPreferredWidth(5);
            coluna = this.tabRemetente.getColumnModel().getColumn(5);
            coluna.setCellRenderer(renderer);
            coluna.setPreferredWidth(1);
            coluna.setWidth(1);
        } catch (DaoException e) {
            logger.error(e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Não foi possivel carregar relação de remetentes", "Endereçador", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void initComponents() {
        JToolBar jToolBar1 = new JToolBar();
        JButton jbtNovo = new JButton();
        JButton jbtEditar = new JButton();
        JButton jbtPesquisar = new JButton();
        JButton jbtExcluir = new JButton();
        JPanel jPanel1 = new JPanel();
        JPanel jPanel2 = new JPanel();
        JLabel jLabel1 = new JLabel();
        this.jtxtNomeRemetente = new JTextField();
        JLabel jLabel2 = new JLabel();
        this.jScrollPane1 = new JScrollPane();
        this.tabRemetente = new JTable();
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Cadastro de remetentes");
        setPreferredSize(new Dimension(744, 434));
        jbtNovo.setFont(new Font(Font.DIALOG, Font.PLAIN, 9));
        jbtNovo.setIcon(new ImageIcon(getClass().getResource("/imagens/usuario.gif")));
        jbtNovo.setText("Novo remetente");
        jbtNovo.setHorizontalTextPosition(0);
        jbtNovo.setMaximumSize(new Dimension(90, 60));
        jbtNovo.setMinimumSize(new Dimension(81, 47));
        jbtNovo.setPreferredSize(new Dimension(60, 50));
        jbtNovo.setVerticalTextPosition(3);
        jbtNovo.addActionListener(TelaRemetente.this::jbtNovoActionPerformed);
        jToolBar1.add(jbtNovo);
        jbtEditar.setFont(new Font(Font.DIALOG, Font.PLAIN, 9));
        jbtEditar.setIcon(new ImageIcon(getClass().getResource("/imagens/editar.gif")));
        jbtEditar.setText("Editar");
        jbtEditar.setHorizontalTextPosition(0);
        jbtEditar.setMaximumSize(new Dimension(90, 60));
        jbtEditar.setVerticalTextPosition(3);
        jbtEditar.addActionListener(TelaRemetente.this::jbtEditarActionPerformed);
        jToolBar1.add(jbtEditar);
        jbtPesquisar.setFont(new Font(Font.DIALOG, Font.PLAIN, 9));
        jbtPesquisar.setIcon(new ImageIcon(getClass().getResource("/imagens/binoculo.gif")));
        jbtPesquisar.setText("Pesquisar");
        jbtPesquisar.setHorizontalTextPosition(0);
        jbtPesquisar.setMaximumSize(new Dimension(90, 60));
        jbtPesquisar.setVerticalTextPosition(3);
        jbtPesquisar.addActionListener(TelaRemetente.this::jbtPesquisarActionPerformed);
        jToolBar1.add(jbtPesquisar);
        jbtExcluir.setFont(new Font(Font.DIALOG, Font.PLAIN, 9));
        jbtExcluir.setIcon(new ImageIcon(getClass().getResource("/imagens/TRASH.gif")));
        jbtExcluir.setText("Excluir");
        jbtExcluir.setHorizontalTextPosition(0);
        jbtExcluir.setMaximumSize(new Dimension(90, 60));
        jbtExcluir.setVerticalTextPosition(3);
        jbtExcluir.addActionListener(TelaRemetente.this::jbtExcluirActionPerformed);
        jToolBar1.add(jbtExcluir);
        getContentPane().add(jToolBar1, "North");
        jPanel1.setLayout(new BorderLayout());
        jPanel1.setBorder(BorderFactory.createEtchedBorder());
        jPanel2.setLayout(new AbsoluteLayout());
        jLabel1.setFont(new Font("MS Sans Serif", 0, 10));
        jLabel1.setText("Procurar por:");
        jPanel2.add(jLabel1, new AbsoluteConstraints(10, 11, 70, 20));
        jPanel2.add(this.jtxtNomeRemetente, new AbsoluteConstraints(80, 10, 310, -1));
        jLabel2.setFont(new Font("MS Sans Serif", 0, 10));
        jLabel2.setText("Destinatários:");
        jPanel2.add(jLabel2, new AbsoluteConstraints(10, 30, 70, 30));
        jPanel1.add(jPanel2, "North");
        this.tabRemetente.setModel(new DefaultTableModel(new Object[][]{{null, null, null, null}, {null, null, null, null}, {null, null, null, null}, {null, null, null, null},}, new String[]{"Title 1", "Title 2", "Title 3", "Title 4"}));
        this.tabRemetente.setModel(this.remetenteTableModel);
        this.jScrollPane1.setViewportView(this.tabRemetente);
        jPanel1.add(this.jScrollPane1, "Center");
        getContentPane().add(jPanel1, "Center");
        pack();
    }

    private void jbtEditarActionPerformed(ActionEvent evt) {
        if (this.tabRemetente.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Não existe nenhum remetente selecionado!", "Endereçador ECT", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        TelaEditarRemetente telaEditarRemetente = new TelaEditarRemetente(this.frmParent, true, this.remetenteTableModel.getRemetente(this.tabRemetente.getSelectedRow()));
        telaEditarRemetente.setVisible(true);
    }

    private void jbtNovoActionPerformed(ActionEvent evt) {
        TelaEditarRemetente telaEditarRemetente = new TelaEditarRemetente(this.frmParent, true);
        telaEditarRemetente.setVisible(true);
    }

    private void jbtExcluirActionPerformed(ActionEvent evt) {
        RemetenteBean remetenteBean;
        List<RemetenteBean> listaRemetentes = new ArrayList<>();
        if (this.tabRemetente.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this.frmParent, "Não existe nenhum remetente selecionado!", "Enderecador", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String[] options = {"Sim", "Não"};
        int resp = JOptionPane.showOptionDialog(this.frmParent, "Tem certeza que deseja excluir o(s) remetente(s)?", "Endereçador", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
        if (resp == 0) {
            int[] linhasSelecionadas = this.tabRemetente.getSelectedRows();
            try {
                for (int linhasSelecionada : linhasSelecionadas) {
                    remetenteBean = this.remetenteTableModel.getRemetente(linhasSelecionada);
                    RemetenteDao.getInstance().excluirRemetente(remetenteBean.getNumeroRemetente());
                    listaRemetentes.add(remetenteBean);
                }
                this.observable.notifyObservers(listaRemetentes);
                this.jtxtNomeRemetente.setText("");
                JOptionPane.showMessageDialog(this.frmParent, "Remetente(s) excluído(s) com sucesso!", "Endereçador", JOptionPane.INFORMATION_MESSAGE);
            } catch (DaoException re) {
                logger.error(re.getMessage(), re);
                JOptionPane.showMessageDialog(this.frmParent, "Não foi possivel carregar relação de remetentes", "Endereçador", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void jbtPesquisarActionPerformed(ActionEvent evt) {
        String nomeRemetente = this.jtxtNomeRemetente.getText().trim();
        int numeroRows = this.tabRemetente.getRowCount();
        int index = -1;
        if (this.ultimaConsulta.equalsIgnoreCase(nomeRemetente)) {
            index = this.tabRemetente.getSelectedRow();
        } else {
            this.ultimaConsulta = nomeRemetente;
        }
        for (int i = index + 1; i < numeroRows; i++) {
            String nomeRemetenteTabela = (String) this.tabRemetente.getValueAt(i, 0);
            if (nomeRemetenteTabela.toUpperCase().contains(nomeRemetente.toUpperCase())) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Remetente não encontrado!", "Endereçador", 1);
        } else {
            ListSelectionModel lsSelectionModelRemetente = this.tabRemetente.getSelectionModel();
            lsSelectionModelRemetente.setSelectionInterval(index, index);
            int tamanhoJscrool = this.jScrollPane1.getVerticalScrollBar().getMaximum();
            int value = 0;
            value = index * tamanhoJscrool / numeroRows;
            this.jScrollPane1.getVerticalScrollBar().setValue(value);
        }
    }

    public void update(Observable o, Object arg) {
        if (arg instanceof RemetenteBean) {
            RemetenteBean remetente = (RemetenteBean) arg;
            int index = this.remetenteTableModel.indexOf(remetente);
            if (index != -1) {
                this.remetenteTableModel.setRemetente(index, remetente);
            } else {
                this.remetenteTableModel.addRemetente(remetente);
            }
        } else if (arg instanceof List) {
            List<RemetenteBean> listaRemetentes = (List) arg;
            for (RemetenteBean listaRemetente : listaRemetentes) {
                if (listaRemetente != null)
                    this.remetenteTableModel.removeRemetente(listaRemetente);
            }
        }
    }
}
