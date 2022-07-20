package br.com.correios.enderecador.telas;

import br.com.correios.enderecador.bean.DestinatarioBean;
import br.com.correios.enderecador.dao.DaoException;
import br.com.correios.enderecador.dao.DestinatarioDao;
import br.com.correios.enderecador.excecao.EnderecadorExcecao;
import br.com.correios.enderecador.util.EnderecadorObservable;
import br.com.correios.enderecador.util.FiltroArquivo;
import br.com.correios.enderecador.util.Geral;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import javax.swing.*;

import org.apache.log4j.Logger;

public class TelaExportarDados extends JFrame implements Observer {
    private static final Logger logger = Logger.getLogger(TelaExportarDados.class);

    private final Frame frmParent;

    private final Vector<DestinatarioBean> vecDestinatario;

    private static TelaExportarDados instance;

    private JButton jbntDestinatario;

    private JButton jbntGrupo;

    private JCheckBox jchkExportarTodos;

    private JList<DestinatarioBean> jlstDestinatarios;

    private TelaExportarDados(Frame parent) {
        this.vecDestinatario = new Vector<>();
        EnderecadorObservable observable = EnderecadorObservable.getInstance();
        this.frmParent = parent;
        initComponents();
        observable.addObserver(this);
    }

    public static TelaExportarDados getInstance(Frame parent) {
        if (instance == null)
            instance = new TelaExportarDados(parent);
        return instance;
    }


    public void carregaListaDestinatario() {
        try {
            ArrayList<DestinatarioBean> arrayDestinatario = DestinatarioDao.getInstance().recuperaDestinatario("");
            this.vecDestinatario.addAll(arrayDestinatario);
            this.jlstDestinatarios.setListData(this.vecDestinatario);
        } catch (DaoException e) {
            logger.error(e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Não foi possivel carregar relação de destinatários", "Endereçador ", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void initComponents() {
        JToolBar jToolBar1 = new JToolBar();
        JButton jbtExportar = new JButton();
        this.jbntDestinatario = new JButton();
        this.jbntGrupo = new JButton();
        JButton jbntExcluir = new JButton();
        JPanel jPanel1 = new JPanel();
        JPanel jPanel2 = new JPanel();
        this.jchkExportarTodos = new JCheckBox();
        JScrollPane jScrollPane1 = new JScrollPane();
        this.jlstDestinatarios = new JList<>();
        setResizable(true);
        setTitle("Exportar Dados");
        setPreferredSize(new Dimension(744, 434));
        jToolBar1.setPreferredSize(new Dimension(325, 59));
        jbtExportar.setFont(new Font("MS Sans Serif", 0, 9));
        jbtExportar.setIcon(new ImageIcon(getClass().getResource("/imagens/OK.gif")));
        jbtExportar.setText("Exportar");
        jbtExportar.setHorizontalTextPosition(0);
        jbtExportar.setMaximumSize(new Dimension(90, 60));
        jbtExportar.setVerticalTextPosition(3);
        jbtExportar.addActionListener(TelaExportarDados.this::jbtExportarActionPerformed);
        jToolBar1.add(jbtExportar);
        this.jbntDestinatario.setFont(new Font("MS Sans Serif", 0, 9));
        this.jbntDestinatario.setIcon(new ImageIcon(getClass().getResource("/imagens/addusuario.gif")));
        this.jbntDestinatario.setText("Selecionar destinatário");
        this.jbntDestinatario.setHorizontalTextPosition(0);
        this.jbntDestinatario.setMaximumSize(new Dimension(107, 60));
        this.jbntDestinatario.setVerticalTextPosition(3);
        this.jbntDestinatario.addActionListener(TelaExportarDados.this::jbntDestinatarioActionPerformed);
        jToolBar1.add(this.jbntDestinatario);
        this.jbntGrupo.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        this.jbntGrupo.setIcon(new ImageIcon(getClass().getResource("/imagens/addusuarios.gif")));
        this.jbntGrupo.setText("Selecionar grupo");
        this.jbntGrupo.setHorizontalTextPosition(0);
        this.jbntGrupo.setMaximumSize(new Dimension(90, 60));
        this.jbntGrupo.setPreferredSize(new Dimension(90, 60));
        this.jbntGrupo.setVerticalTextPosition(3);
        this.jbntGrupo.addActionListener(TelaExportarDados.this::jbntGrupoActionPerformed);
        jToolBar1.add(this.jbntGrupo);
        jbntExcluir.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        jbntExcluir.setIcon(new ImageIcon(getClass().getResource("/imagens/removerTodos.gif")));
        jbntExcluir.setText("Remover todos");
        jbntExcluir.setHorizontalTextPosition(0);
        jbntExcluir.setMaximumSize(new Dimension(90, 60));
        jbntExcluir.setVerticalTextPosition(3);
        jbntExcluir.addActionListener(TelaExportarDados.this::jbntExcluirActionPerformed);
        jToolBar1.add(jbntExcluir);
        getContentPane().add(jToolBar1, "North");
        jPanel1.setLayout(new BorderLayout());
        jPanel2.setLayout(new GridLayout(1, 0));
        jPanel2.setBorder(BorderFactory.createEtchedBorder());
        this.jchkExportarTodos.setFont(new Font("MS Sans Serif", 0, 10));
        this.jchkExportarTodos.setText("Exportar todos os destinatários");
        this.jchkExportarTodos.addActionListener(TelaExportarDados.this::jchkExportarTodosActionPerformed);
        jPanel2.add(this.jchkExportarTodos);
        jPanel1.add(jPanel2, "North");
        jScrollPane1.setViewportView(this.jlstDestinatarios);
        jPanel1.add(jScrollPane1, "Center");
        getContentPane().add(jPanel1, "Center");
        pack();
    }

    private void jbntExcluirActionPerformed(ActionEvent evt) {
        this.vecDestinatario.removeAllElements();
        this.jlstDestinatarios.setListData(this.vecDestinatario);
        this.jchkExportarTodos.setSelected(false);
        this.jbntDestinatario.setEnabled(true);
        this.jbntGrupo.setEnabled(true);
    }

    private void jbntGrupoActionPerformed(ActionEvent evt) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        TelaPesquisarGrupo telaPesquisarGrupo = new TelaPesquisarGrupo(this.frmParent, true, this.vecDestinatario);
        telaPesquisarGrupo.setVisible(true);
        this.jlstDestinatarios.setListData(this.vecDestinatario);
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    private void jbntDestinatarioActionPerformed(ActionEvent evt) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        TelaPesquisarDestinatario telaPesquisaDestinatario = new TelaPesquisarDestinatario(this.frmParent, true, this.vecDestinatario);
        telaPesquisaDestinatario.setVisible(true);
        this.jlstDestinatarios.setListData(this.vecDestinatario);
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    private void jchkExportarTodosActionPerformed(ActionEvent evt) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        this.jbntDestinatario.setEnabled(!this.jbntDestinatario.isEnabled());
        this.jbntGrupo.setEnabled(!this.jbntGrupo.isEnabled());
        this.vecDestinatario.removeAllElements();
        this.jlstDestinatarios.setListData(this.vecDestinatario);
        if (this.jchkExportarTodos.isSelected())
            carregaListaDestinatario();
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    private void jbtExportarActionPerformed(ActionEvent evt) {
        Geral geral = new Geral();
        String vNomeArquivo;
        if (this.vecDestinatario.size() <= 0) {
            JOptionPane.showMessageDialog(this, "Não existe nenhum destinatário selecionado!", "Endereçador", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setApproveButtonText("Salvar");
        fileChooser.setDialogTitle("Salvar arquivo");
        fileChooser.setFileSelectionMode(0);
        fileChooser.setAcceptAllFileFilterUsed(true);
        fileChooser.addChoosableFileFilter(new FiltroArquivo("csv"));
        fileChooser.addChoosableFileFilter(new FiltroArquivo("txt"));
        int result = fileChooser.showSaveDialog(this);
        if (result == 1)
            return;
        File fileName = fileChooser.getSelectedFile();
        if (fileName == null)
            return;
        String extension = Geral.getExtension(fileName);
        FiltroArquivo imageFilter = (FiltroArquivo) fileChooser.getFileFilter();
        vNomeArquivo = fileName.getAbsolutePath();
        if (extension == null) {
            vNomeArquivo = vNomeArquivo + "." + imageFilter.getExtension();
            extension = imageFilter.getExtension();
        } else if (!Geral.validaArquivo(fileName)) {
            JOptionPane.showMessageDialog(this, "Formato do arquivo inválido!\nO arquivo deve ser do tipo TXT ou CSV.", "Endereçador", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            if (extension.equals("txt")) {
                geral.exportarParaTXT(vNomeArquivo, this.vecDestinatario);
            } else {
                geral.exportarParaCSV(vNomeArquivo, this.vecDestinatario);
            }
            JOptionPane.showMessageDialog(this, "Exportação concluída!", "Endereçador", JOptionPane.WARNING_MESSAGE);
        } catch (EnderecadorExcecao ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Endereçador", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void update(Observable o, Object arg) {
        if (arg instanceof DestinatarioBean) {
            DestinatarioBean destinatario = (DestinatarioBean) arg;
            int index = this.vecDestinatario.indexOf(destinatario);
            if (index != -1) {
                this.vecDestinatario.remove(index);
                this.vecDestinatario.add(index, destinatario);
                this.jlstDestinatarios.setListData(this.vecDestinatario);
            }
        } else if (arg instanceof List) {
            List<DestinatarioBean> listaDestinatarios = (List<DestinatarioBean>) arg;
            for (DestinatarioBean listaDestinatario : listaDestinatarios) {
                if (listaDestinatario != null)
                    this.vecDestinatario.remove(listaDestinatario);
            }
            this.jlstDestinatarios.setListData(this.vecDestinatario);
        }
    }
}
