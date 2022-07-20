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
  RemetenteTableModel remetenteTableModel = new RemetenteTableModel();
  
  EnderecadorObservable observable = EnderecadorObservable.getInstance();
  
  private static Logger logger = Logger.getLogger(TelaRemetente.class);
  
  private static TelaRemetente instance;
  
  private String ultimaConsulta = "";
  
  private Frame frmParent;
  
  private JLabel jLabel1;
  
  private JLabel jLabel2;
  
  private JPanel jPanel1;
  
  private JPanel jPanel2;
  
  private JScrollPane jScrollPane1;
  
  private JToolBar jToolBar1;
  
  private JButton jbtEditar;
  
  private JButton jbtExcluir;
  
  private JButton jbtNovo;
  
  private JButton jbtPesquisar;
  
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
      ArrayList arrayRemetente = RemetenteDao.getInstance().recuperaRemetente("");
      this.remetenteTableModel.setRemetente(arrayRemetente);
      this.tabRemetente.setSelectionMode(2);
      TextoCellRenderer renderer = new TextoCellRenderer(2);
      TableColumn coluna = null;
      coluna = this.tabRemetente.getColumnModel().getColumn(0);
      coluna.setCellRenderer((TableCellRenderer)renderer);
      coluna = this.tabRemetente.getColumnModel().getColumn(1);
      coluna.setPreferredWidth(70);
      coluna.setCellRenderer((TableCellRenderer)renderer);
      coluna = this.tabRemetente.getColumnModel().getColumn(2);
      coluna.setCellRenderer((TableCellRenderer)renderer);
      coluna.setPreferredWidth(70);
      coluna = this.tabRemetente.getColumnModel().getColumn(3);
      coluna.setCellRenderer((TableCellRenderer)renderer);
      coluna = this.tabRemetente.getColumnModel().getColumn(4);
      coluna.setCellRenderer((TableCellRenderer)renderer);
      coluna.setPreferredWidth(5);
      coluna = this.tabRemetente.getColumnModel().getColumn(5);
      coluna.setCellRenderer((TableCellRenderer)renderer);
      coluna.setPreferredWidth(1);
      coluna.setWidth(1);
    } catch (DaoException e) {
      logger.error(e.getMessage(), (Throwable)e);
      JOptionPane.showMessageDialog(this, "Não foi possivel carregar relação de remetentes", "Endereçador", 2);
    } 
  }
  
  private void initComponents() {
    this.jToolBar1 = new JToolBar();
    this.jbtNovo = new JButton();
    this.jbtEditar = new JButton();
    this.jbtPesquisar = new JButton();
    this.jbtExcluir = new JButton();
    this.jPanel1 = new JPanel();
    this.jPanel2 = new JPanel();
    this.jLabel1 = new JLabel();
    this.jtxtNomeRemetente = new JTextField();
    this.jLabel2 = new JLabel();
    this.jScrollPane1 = new JScrollPane();
    this.tabRemetente = new JTable();
    setClosable(true);
    setIconifiable(true);
    setMaximizable(true);
    setResizable(true);
    setTitle("Cadastro de remetentes");
    setPreferredSize(new Dimension(744, 434));
    this.jToolBar1.setBorder(BorderFactory.createEtchedBorder());
    this.jbtNovo.setFont(new Font("Dialog", 0, 9));
    this.jbtNovo.setIcon(new ImageIcon(getClass().getResource("/imagens/usuario.gif")));
    this.jbtNovo.setText("Novo remetente");
    this.jbtNovo.setHorizontalTextPosition(0);
    this.jbtNovo.setMaximumSize(new Dimension(90, 60));
    this.jbtNovo.setMinimumSize(new Dimension(81, 47));
    this.jbtNovo.setPreferredSize(new Dimension(60, 50));
    this.jbtNovo.setVerticalTextPosition(3);
    this.jbtNovo.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            TelaRemetente.this.jbtNovoActionPerformed(evt);
          }
        });
    this.jToolBar1.add(this.jbtNovo);
    this.jbtEditar.setFont(new Font("Dialog", 0, 9));
    this.jbtEditar.setIcon(new ImageIcon(getClass().getResource("/imagens/editar.gif")));
    this.jbtEditar.setText("Editar");
    this.jbtEditar.setHorizontalTextPosition(0);
    this.jbtEditar.setMaximumSize(new Dimension(90, 60));
    this.jbtEditar.setVerticalTextPosition(3);
    this.jbtEditar.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            TelaRemetente.this.jbtEditarActionPerformed(evt);
          }
        });
    this.jToolBar1.add(this.jbtEditar);
    this.jbtPesquisar.setFont(new Font("Dialog", 0, 9));
    this.jbtPesquisar.setIcon(new ImageIcon(getClass().getResource("/imagens/binoculo.gif")));
    this.jbtPesquisar.setText("Pesquisar");
    this.jbtPesquisar.setHorizontalTextPosition(0);
    this.jbtPesquisar.setMaximumSize(new Dimension(90, 60));
    this.jbtPesquisar.setVerticalTextPosition(3);
    this.jbtPesquisar.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            TelaRemetente.this.jbtPesquisarActionPerformed(evt);
          }
        });
    this.jToolBar1.add(this.jbtPesquisar);
    this.jbtExcluir.setFont(new Font("Dialog", 0, 9));
    this.jbtExcluir.setIcon(new ImageIcon(getClass().getResource("/imagens/TRASH.gif")));
    this.jbtExcluir.setText("Excluir");
    this.jbtExcluir.setHorizontalTextPosition(0);
    this.jbtExcluir.setMaximumSize(new Dimension(90, 60));
    this.jbtExcluir.setVerticalTextPosition(3);
    this.jbtExcluir.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            TelaRemetente.this.jbtExcluirActionPerformed(evt);
          }
        });
    this.jToolBar1.add(this.jbtExcluir);
    getContentPane().add(this.jToolBar1, "North");
    this.jPanel1.setLayout(new BorderLayout());
    this.jPanel1.setBorder(BorderFactory.createEtchedBorder());
    this.jPanel2.setLayout((LayoutManager)new AbsoluteLayout());
    this.jLabel1.setFont(new Font("MS Sans Serif", 0, 10));
    this.jLabel1.setText("Procurar por:");
    this.jPanel2.add(this.jLabel1, new AbsoluteConstraints(10, 11, 70, 20));
    this.jPanel2.add(this.jtxtNomeRemetente, new AbsoluteConstraints(80, 10, 310, -1));
    this.jLabel2.setFont(new Font("MS Sans Serif", 0, 10));
    this.jLabel2.setText("Destinatários:");
    this.jPanel2.add(this.jLabel2, new AbsoluteConstraints(10, 30, 70, 30));
    this.jPanel1.add(this.jPanel2, "North");
    this.tabRemetente.setModel(new DefaultTableModel(new Object[][] { { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null },  }, (Object[])new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));
    this.tabRemetente.setModel(this.remetenteTableModel);
    this.jScrollPane1.setViewportView(this.tabRemetente);
    this.jPanel1.add(this.jScrollPane1, "Center");
    getContentPane().add(this.jPanel1, "Center");
    pack();
  }
  
  private void jbtEditarActionPerformed(ActionEvent evt) {
    if (this.tabRemetente.getSelectedRow() < 0) {
      JOptionPane.showMessageDialog(this, "Não existe nenhum remetente selecionado!", "Endereçador ECT", 1);
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
    RemetenteBean remetenteBean = null;
    List<RemetenteBean> listaRemetentes = new ArrayList();
    evt.getSource();
    if (this.tabRemetente.getSelectedRow() == -1) {
      JOptionPane.showMessageDialog(this.frmParent, "Não existe nenhum remetente selecionado!", "Enderecador", 2);
      return;
    } 
    remetenteBean = this.remetenteTableModel.getRemetente(this.tabRemetente.getSelectedRow());
    String[] options = { "Sim", "Não" };
    int resp = JOptionPane.showOptionDialog(this.frmParent, "Tem certeza que deseja excluir o(s) remetente(s)?", "Endereçador", 0, 3, null, (Object[])options, null);
    if (resp == 0) {
      int[] linhasSelecionadas = this.tabRemetente.getSelectedRows();
      try {
        for (int i = 0; i < linhasSelecionadas.length; i++) {
          remetenteBean = this.remetenteTableModel.getRemetente(linhasSelecionadas[i]);
          RemetenteDao.getInstance().excluirRemetente(remetenteBean.getNumeroRemetente());
          listaRemetentes.add(remetenteBean);
        } 
        this.observable.notifyObservers(listaRemetentes);
        this.jtxtNomeRemetente.setText("");
        JOptionPane.showMessageDialog(this.frmParent, "Remetente(s) excluído(s) com sucesso!", "Endereçador", 1);
      } catch (DaoException re) {
        logger.error(re.getMessage(), (Throwable)re);
        JOptionPane.showMessageDialog(this.frmParent, "Não foi possivel carregar relação de remetentes", "Endereçador", 2);
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
      String nomeRemetenteTabela = (String)this.tabRemetente.getValueAt(i, 0);
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
      RemetenteBean remetente = (RemetenteBean)arg;
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
