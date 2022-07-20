package br.com.correios.enderecador.telas;

import br.com.correios.enderecador.bean.DestinatarioBean;
import br.com.correios.enderecador.bean.GrupoBean;
import br.com.correios.enderecador.bean.GrupoDestinatarioBean;
import br.com.correios.enderecador.dao.DaoException;
import br.com.correios.enderecador.dao.DestinatarioDao;
import br.com.correios.enderecador.dao.GrupoDao;
import br.com.correios.enderecador.dao.GrupoDestinatarioDao;
import br.com.correios.enderecador.util.Cep;
import br.com.correios.enderecador.util.EnderecadorObservable;
import br.com.correios.enderecador.util.FiltroArquivo;
import br.com.correios.enderecador.util.Geral;
import br.com.correios.enderecador.util.TextoCellRenderer;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;
import org.jdesktop.layout.GroupLayout;

public class TelaIncorporarDados extends JInternalFrame {
    private static final Logger logger = Logger.getLogger(TelaIncorporarDados.class);

    private final EnderecadorObservable observable = EnderecadorObservable.getInstance();

    private Vector<DestinatarioBean> vecDestinatario;

    private DestinatarioBean destinatarioBean;

    private static TelaIncorporarDados instance;

    private JComboBox<String> jcmbGrupo;

    private JTextArea jtxtMensagem;

    private JTable tabDestinatario;

    private TelaIncorporarDados() {
        initComponents();
    }

    public static TelaIncorporarDados getInstance() {
        if (instance == null)
            instance = new TelaIncorporarDados();
        return instance;
    }

    private void initComponents() {
        JToolBar jToolBar1 = new JToolBar();
        JButton jbtnConfirmar = new JButton();
        JButton jbtnAbrir = new JButton();
        JButton jbtnLimpar = new JButton();
        JPanel jPanel1 = new JPanel();
        JLabel jLabel1 = new JLabel();
        this.jcmbGrupo = new JComboBox<>();
        JPanel jPanel2 = new JPanel();
        JScrollPane jScrollPane1 = new JScrollPane();
        this.tabDestinatario = new JTable();
        JScrollPane jScrollPane2 = new JScrollPane();
        this.jtxtMensagem = new JTextArea();
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Importar dados");
        jToolBar1.setBorder(BorderFactory.createEtchedBorder());
        jbtnConfirmar.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        jbtnConfirmar.setIcon(new ImageIcon(getClass().getResource("/imagens/OK.gif")));
        jbtnConfirmar.setText("Confirmar");
        jbtnConfirmar.setHorizontalTextPosition(0);
        jbtnConfirmar.setMaximumSize(new Dimension(90, 60));
        jbtnConfirmar.setVerticalTextPosition(3);
        jbtnConfirmar.addActionListener(TelaIncorporarDados.this::jbtnConfirmarActionPerformed);
        jToolBar1.add(jbtnConfirmar);
        jbtnAbrir.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        jbtnAbrir.setIcon(new ImageIcon(getClass().getResource("/imagens/arquivo.gif")));
        jbtnAbrir.setText("Abrir arquivo");
        jbtnAbrir.setHorizontalTextPosition(0);
        jbtnAbrir.setMaximumSize(new Dimension(90, 60));
        jbtnAbrir.setVerticalTextPosition(3);
        jbtnAbrir.addActionListener(TelaIncorporarDados.this::jbtnAbrirActionPerformed);
        jToolBar1.add(jbtnAbrir);
        jbtnLimpar.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        jbtnLimpar.setIcon(new ImageIcon(getClass().getResource("/imagens/cancelar.gif")));
        jbtnLimpar.setText("Limpar tela");
        jbtnLimpar.setHorizontalTextPosition(0);
        jbtnLimpar.setMaximumSize(new Dimension(90, 60));
        jbtnLimpar.setVerticalTextPosition(3);
        jbtnLimpar.addActionListener(TelaIncorporarDados.this::jbtnLimparActionPerformed);
        jToolBar1.add(jbtnLimpar);
        jPanel1.setBorder(BorderFactory.createEtchedBorder());
        jLabel1.setText("Grupo de destinatários:");
        carregaGrupo();
        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(1)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1)
                .addPreferredGap(0)
                .add(this.jcmbGrupo, 0, 489, 32767)
                .addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(1)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(3)
                    .add(jLabel1).add(this.jcmbGrupo, -2, -1, -2))
                .addContainerGap(-1, 32767)));
        jPanel2.setBorder(BorderFactory.createEtchedBorder());
        this.tabDestinatario.setModel(new IncorporaDestinatarioTableModel());
        this.tabDestinatario.setModel(new DefaultTableModel(new Object[][]{{null, null, null, null}, {null, null, null, null}, {null, null, null, null}, {null, null, null, null},}, new String[]{"Title 1", "Title 2", "Title 3", "Title 4"}));
        this.tabDestinatario.setModel(new IncorporaDestinatarioTableModel());
        jScrollPane1.setViewportView(this.tabDestinatario);
        this.jtxtMensagem.setColumns(20);
        this.jtxtMensagem.setEditable(false);
        this.jtxtMensagem.setRows(5);
        jScrollPane2.setViewportView(this.jtxtMensagem);
        GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(1)
            .add(2, jPanel2Layout.createSequentialGroup()
                .addContainerGap().add(jPanel2Layout.createParallelGroup(2)
                    .add(1, jScrollPane1, -1, 606, 32767)
                    .add(1, jScrollPane2, -1, 606, 32767))
                .addContainerGap()));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(1)
            .add(2, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, -1, 183, 32767)
                .addPreferredGap(0)
                .add(jScrollPane2, -2, 98, -2)
                .addContainerGap()));
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(1)
            .add(jToolBar1, -1, 630, 32767)
            .add(jPanel1, -1, -1, 32767)
            .add(jPanel2, -1, -1, 32767));
        layout.setVerticalGroup(layout.createParallelGroup(1)
            .add(layout.createSequentialGroup()
                .add(jToolBar1, -2, 59, -2)
                .addPreferredGap(0)
                .add(jPanel1, -2, -1, -2)
                .addPreferredGap(0)
                .add(jPanel2, -1, -1, 32767)));
        pack();
    }

    private void jbtnLimparActionPerformed(ActionEvent evt) {
        limpaTela();
    }

    private void jbtnConfirmarActionPerformed(ActionEvent evt) {
        gravarDados();
    }

    private void jbtnAbrirActionPerformed(ActionEvent evt) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setApproveButtonText("Abrir");
        fileChooser.setDialogTitle("Abrir arquivo");
        fileChooser.setAcceptAllFileFilterUsed(true);
        fileChooser.addChoosableFileFilter(new FiltroArquivo("csv"));
        fileChooser.addChoosableFileFilter(new FiltroArquivo("txt"));
        fileChooser.setFileSelectionMode(0);
        int result = fileChooser.showOpenDialog(this);
        if (result == 1)
            return;
        File fileName = fileChooser.getSelectedFile();
        if (!Geral.validaArquivo(fileName)) {
            JOptionPane.showMessageDialog(this, "Não é possível importar esse tipo de arquivo.", "Endereçador ", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (fileName.getName().equals("")) {
            JOptionPane.showMessageDialog(this, "Nome de arquivo inválido", "Endereçador ", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                BufferedReader in = new BufferedReader(new FileReader(fileName));
                String separador = ";";
                int contLinha = 0;
                StringBuilder bufferLog = new StringBuilder();
                this.vecDestinatario = new Vector<>();
                String[] options = {"Sim", "Não"};
                int resp = JOptionPane.showOptionDialog(null, "Deseja importar os dados da primeira linha?", "Endereçador", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
                if (resp == 1)
                    in.readLine();
                String linha;
                while ((linha = in.readLine()) != null) {
                    contLinha++;
                    String[] arrLinha = linha.split(separador);
                    if (arrLinha.length == 15) {
                        this.destinatarioBean = new DestinatarioBean();
                        this.destinatarioBean.setTitulo(arrLinha[0]);
                        this.destinatarioBean.setNome(arrLinha[1]);
                        this.destinatarioBean.setApelido(arrLinha[2]);
                        this.destinatarioBean.setCaixaPostal(arrLinha[3]);
                        this.destinatarioBean.setEndereco(arrLinha[4]);
                        this.destinatarioBean.setNumeroEndereco(arrLinha[5]);
                        this.destinatarioBean.setComplemento(arrLinha[6]);
                        this.destinatarioBean.setBairro(arrLinha[7]);
                        this.destinatarioBean.setCidade(arrLinha[8]);
                        this.destinatarioBean.setUf(arrLinha[9]);
                        this.destinatarioBean.setEmail(arrLinha[10]);
                        this.destinatarioBean.setTelefone(arrLinha[11]);
                        this.destinatarioBean.setFax(arrLinha[12]);
                        this.destinatarioBean.setCepCaixaPostal(arrLinha[13]);
                        this.destinatarioBean.setCep(Cep.tiraMascaraCep(arrLinha[14]));
                        if (this.destinatarioBean.getNome().trim().equals("")) {
                            bufferLog.append("Não é possível gravar a linha ").append(contLinha).append(".");
                            bufferLog.append("Motivo: O nome do destinatário está em branco. ->  ").append(linha).append("\n");
                            continue;
                        }
                        if (this.destinatarioBean.getCep().trim().equals("")) {
                            bufferLog.append("Não é possível gravar a linha ").append(contLinha).append(".");
                            bufferLog.append("Motivo: O campo CEP está em branco. ->  ").append(linha).append("\n");
                            continue;
                        }
                        if (this.destinatarioBean.getEndereco().trim().equals("")) {
                            bufferLog.append("Não é possível gravar a linha ").append(contLinha).append(".");
                            bufferLog.append("Motivo: O campo Endereço está em branco. ->  ").append(linha).append("\n");
                            continue;
                        }
                        if (this.destinatarioBean.getNumeroEndereco().trim().equals("")) {
                            bufferLog.append("Não é possível gravar a linha ").append(contLinha).append(".");
                            bufferLog.append("Motivo: O campo número do endereço está em branco. ->  ").append(linha).append("\n");
                            continue;
                        }
                        if (this.destinatarioBean.getCidade().trim().equals("")) {
                            bufferLog.append("Não é possível gravar a linha ").append(contLinha).append(".");
                            bufferLog.append("Motivo: O campo cidade está em branco. ->  ").append(linha).append("\n");
                            continue;
                        }
                        if (this.destinatarioBean.getUf().trim().equals("")) {
                            bufferLog.append("Não é possível gravar a linha ").append(contLinha).append(".");
                            bufferLog.append("Motivo: O campo UF está em branco. ->  ").append(linha).append("\n");
                            continue;
                        }
                        if (!Cep.validaCep(this.destinatarioBean.getCep())) {
                            bufferLog.append("Não é possível gravar a linha ").append(contLinha).append(".");
                            bufferLog.append("Motivo: CEP inválido. ->  ").append(linha).append("\n");
                            continue;
                        }
                        if (!this.destinatarioBean.getEmail().equals("") && !this.destinatarioBean.validaEmail()) {
                            bufferLog.append("Não é possível gravar a linha ").append(contLinha).append(".");
                            bufferLog.append("Motivo: E-mail inválido. ->  ").append(linha).append("\n");
                            continue;
                        }
                        this.vecDestinatario.add(this.destinatarioBean);
                        continue;
                    }
                    bufferLog.append("Não é possível gravar a linha ").append(contLinha).append(".");
                    bufferLog.append("Motivo: Estrutura da linha inválida. ->  ").append(linha).append("\n");
                }
                in.close();
                if (bufferLog.toString().equals("")) {
                    bufferLog.append("Todos os dados foram validados com sucesso! Para incorporar os dados clique no botão confirmar!");
                } else {
                    bufferLog.append("ATENÇÃO: Alguns dados não poderão ser importados.");
                }
                this.jtxtMensagem.setText(bufferLog.toString());
                carregaTabela();
            } catch (IOException exIO) {
                logger.error(exIO.getMessage(), exIO);
                JOptionPane.showMessageDialog(this, "Não foi possível abir o arquivo", "Endereçador", 0);
            }
        }
    }

    public void carregaTabela() {
        IncorporaDestinatarioTableModel model = null;
        if (this.vecDestinatario.size() != 0) {
            Geral geral = null;
            Geral.ordenarVetor(this.vecDestinatario, this.destinatarioBean);
            ArrayList<DestinatarioBean> arrayDestinatario = new ArrayList<>(this.vecDestinatario);
            model = (IncorporaDestinatarioTableModel) this.tabDestinatario.getModel();
            model.setDestinatario(arrayDestinatario);
            this.tabDestinatario.setSelectionMode(2);
            TextoCellRenderer renderer = new TextoCellRenderer(2);
            TableColumn coluna = null;
            coluna = this.tabDestinatario.getColumnModel().getColumn(0);
            coluna.setCellRenderer(renderer);
            coluna = this.tabDestinatario.getColumnModel().getColumn(1);
            coluna.setCellRenderer(renderer);
            coluna = this.tabDestinatario.getColumnModel().getColumn(2);
            coluna.setCellRenderer(renderer);
            coluna = this.tabDestinatario.getColumnModel().getColumn(3);
            coluna.setCellRenderer(renderer);
            coluna = this.tabDestinatario.getColumnModel().getColumn(4);
            coluna.setCellRenderer(renderer);
            coluna = this.tabDestinatario.getColumnModel().getColumn(5);
            coluna.setCellRenderer(renderer);
            coluna = this.tabDestinatario.getColumnModel().getColumn(6);
            coluna.setCellRenderer(renderer);
            coluna = this.tabDestinatario.getColumnModel().getColumn(7);
            coluna.setCellRenderer(renderer);
            coluna = this.tabDestinatario.getColumnModel().getColumn(8);
            coluna.setCellRenderer(renderer);
            coluna = this.tabDestinatario.getColumnModel().getColumn(9);
            coluna.setCellRenderer(renderer);
            coluna = this.tabDestinatario.getColumnModel().getColumn(10);
            coluna.setCellRenderer(renderer);
            coluna = this.tabDestinatario.getColumnModel().getColumn(11);
            coluna.setCellRenderer(renderer);
            coluna = this.tabDestinatario.getColumnModel().getColumn(12);
            coluna.setCellRenderer(renderer);
        }
    }

    public void carregaGrupo() {
        try {
            ArrayList<GrupoBean> arrayGrupo = GrupoDao.getInstance().recuperaGrupo("");
            Vector<String> vecGrupo = new Vector<>();
            vecGrupo.add("(Selecione um grupo)");
            for (GrupoBean grupoBean : arrayGrupo)
                vecGrupo.add(grupoBean.getDescricaoGrupo());
            this.jcmbGrupo = new JComboBox<>(vecGrupo);
        } catch (DaoException e) {
            logger.error(e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Não foi possivel carregar relação dos grupos", "Endereçador ECT", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void gravarDados() {
        if (this.vecDestinatario == null) {
            JOptionPane.showMessageDialog(this, "Não existem destinatários incorporados para gravação.", "Endereçador", JOptionPane.INFORMATION_MESSAGE);
        } else if (this.vecDestinatario.size() != 0) {
            try {
                for (DestinatarioBean bean : this.vecDestinatario) {
                    DestinatarioDao destinatarioDao = DestinatarioDao.getInstance();
                    destinatarioDao.incluirDestinatario(bean);
                    this.observable.notifyObservers(bean);
                    if (this.jcmbGrupo.getSelectedIndex() != 0) {
                        GrupoBean grupoBean = (GrupoBean) this.jcmbGrupo.getSelectedItem();
                        GrupoDestinatarioBean grupoDestinatarioBean = GrupoDestinatarioBean.getInstance();
                        grupoDestinatarioBean.setNumeroDestinatario(bean.getNumeroDestinatario());
                        grupoDestinatarioBean.setNumeroGrupo(grupoBean.getNumeroGrupo());
                        GrupoDestinatarioDao.getInstance().incluirGrupoDestinatario(grupoDestinatarioBean);
                    }
                }
                JOptionPane.showMessageDialog(this, "Dados Gravados com sucesso", "Enderecador", JOptionPane.INFORMATION_MESSAGE);
                limpaTela();
            } catch (DaoException ex) {
                logger.error(ex.getMessage(), ex);
                JOptionPane.showMessageDialog(this, "Não foi possível gravar esse destinatário.", "Enderecador", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void limpaTela() {
        this.jtxtMensagem.setText("");
        this.tabDestinatario.setModel(new IncorporaDestinatarioTableModel());
    }
}
