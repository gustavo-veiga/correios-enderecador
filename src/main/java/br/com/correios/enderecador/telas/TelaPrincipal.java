package br.com.correios.enderecador.telas;

import br.com.correios.enderecador.bean.ConfiguracaoBean;
import br.com.correios.enderecador.conexao.ConexaoBD;
import br.com.correios.enderecador.conexao.ConnectException;
import br.com.correios.enderecador.excecao.EnderecadorExcecao;
import br.com.correios.enderecador.util.Geral;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.net.URL;
import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.help.HelpSetException;
import javax.swing.*;

import org.apache.log4j.Logger;

public class TelaPrincipal extends JFrame {
    private static final Logger logger = Logger.getLogger(TelaPrincipal.class);

    private JDesktopPane desktop;

    private JSeparator jSeparator2;

    private JSeparator jSeparator3;

    private JLabel lblBackground;

    private JMenuItem mnAtualizarVersao;

    private JMenuItem mnConfigUsuario;

    private JMenuItem mnProxy;

    private JMenuItem mnTopicosAjuda;

    private HelpBroker hb;

    private HelpSet hs;

    public TelaPrincipal() {
        initComponents();
        configuracoesAdicionais();
        configuracaoAjuda(this.mnTopicosAjuda);
    }

    private void atualizarVersao() {
        try {
            Geral.displayURL(ConfiguracaoBean.getInstance().getPaginaEnderecador());
        } catch (EnderecadorExcecao ex) {
            logger.error(ex.getMessage(), (Throwable) ex);
        }
    }

    private void configuracaoAjuda(JMenuItem topics) {
        this.hs = getHelpSet("ajuda/helpset.hs");
        this.hs = getHelpSet("ajuda/helpset.hs");
        try {
            this.hb = this.hs.createHelpBroker();
            this.hb.enableHelpKey(getRootPane(), "apresentacao", this.hs);
            CSH.setHelpIDString(topics, "apresentacao");
            topics.addActionListener((ActionListener) new CSH.DisplayHelpFromSource(this.hb));
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }

    public HelpSet getHelpSet(String helpsetfile) {
        HelpSet hs = null;
        ClassLoader cl = getClass().getClassLoader();
        URL hsURL = HelpSet.findHelpSet(cl, helpsetfile);
        try {
            hs = new HelpSet(null, hsURL);
        } catch (HelpSetException ex) {
            logger.error("HelpSet: " + ex.getMessage());
            logger.error("HelpSet: " + helpsetfile + " not found");
        }
        return hs;
    }

    private boolean frameExiste(JInternalFrame f) {
        boolean retorno = false;
        for (int i = 0; i < (this.desktop.getAllFrames()).length; i++) {
            JInternalFrame t = this.desktop.getAllFrames()[i];
            if (t.equals(f))
                retorno = true;
        }
        return retorno;
    }

    private void initComponents() {
        this.desktop = new JDesktopPane();
        this.lblBackground = new JLabel();
        JToolBar jToolBar1 = new JToolBar();
        JButton jbtCadRemetente = new JButton();
        JButton jbtCadDestinatario = new JButton();
        JButton jbtCadGrupo = new JButton();
        JButton jbtIncorporarDados = new JButton();
        JButton jbtExportarDados = new JButton();
        JButton jbtEtiquetasEncomendas = new JButton();
        JButton jbtEtiquetasCartas = new JButton();
        JButton jbtImpressaoEnvelope = new JButton();
        JButton jbtSair = new JButton();
        JMenuBar jMenuBar1 = new JMenuBar();
        JMenu mnArquivo = new JMenu();
        JMenuItem mnCadRemetente = new JMenuItem();
        JMenuItem mnCadDestinatario = new JMenuItem();
        JMenuItem mnCadGrupo = new JMenuItem();
        JSeparator jSeparator1 = new JSeparator();
        JMenuItem mnSair = new JMenuItem();
        JMenu mnImpressao = new JMenu();
        JMenu mnEtiquetas = new JMenu();
        JMenuItem mnEncomendas = new JMenuItem();
        JMenuItem mnCartas = new JMenuItem();
        JMenuItem mnEnvelope = new JMenuItem();
        JMenu mnFerramentas = new JMenu();
        JMenuItem mnImportar = new JMenuItem();
        JMenuItem mnExportar = new JMenuItem();
        this.jSeparator2 = new JSeparator();
        this.mnProxy = new JMenuItem();
        this.mnConfigUsuario = new JMenuItem();
        JMenu mnAjuda = new JMenu();
        JMenuItem mnSobre = new JMenuItem();
        this.mnTopicosAjuda = new JMenuItem();
        this.jSeparator3 = new JSeparator();
        this.mnAtualizarVersao = new JMenuItem();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Endereçador Escritório");
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                TelaPrincipal.this.this_componentResized(evt);
            }
        });
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                TelaPrincipal.this.formWindowClosing(evt);
            }
        });
        this.desktop.setBackground(getBackground());
        this.desktop.setBorder(BorderFactory.createEtchedBorder());
        this.lblBackground.setIcon(new ImageIcon(getClass().getResource("/imagens/logo_enderecador.gif")));
        this.desktop.add(this.lblBackground);
        this.lblBackground.setBounds(180, 100, 225, 100);
        getContentPane().add(this.desktop, "Center");
        jbtCadRemetente.setIcon(new ImageIcon(getClass().getResource("/imagens/remetente.gif")));
        jbtCadRemetente.setToolTipText("Cadastrar remetentes");
        jbtCadRemetente.setBorder(BorderFactory.createEtchedBorder());
        jbtCadRemetente.setHorizontalTextPosition(0);
        jbtCadRemetente.setMaximumSize(new Dimension(60, 50));
        jbtCadRemetente.setPreferredSize(new Dimension(60, 50));
        jbtCadRemetente.addActionListener(TelaPrincipal.this::jbtCadRemetenteActionPerformed);
        jToolBar1.add(jbtCadRemetente);
        jbtCadDestinatario.setIcon(new ImageIcon(getClass().getResource("/imagens/usuario.gif")));
        jbtCadDestinatario.setToolTipText("Cadastrar destinatários");
        jbtCadDestinatario.setBorder(BorderFactory.createEtchedBorder());
        jbtCadDestinatario.setMaximumSize(new Dimension(60, 50));
        jbtCadDestinatario.setMinimumSize(new Dimension(31, 31));
        jbtCadDestinatario.addActionListener(TelaPrincipal.this::jbtCadDestinatarioActionPerformed);
        jToolBar1.add(jbtCadDestinatario);
        jbtCadGrupo.setIcon(new ImageIcon(getClass().getResource("/imagens/usuarios.gif")));
        jbtCadGrupo.setToolTipText("Cadastrar grupos");
        jbtCadGrupo.setBorder(BorderFactory.createEtchedBorder());
        jbtCadGrupo.setMaximumSize(new Dimension(60, 50));
        jbtCadGrupo.setMinimumSize(new Dimension(45, 39));
        jbtCadGrupo.addActionListener(TelaPrincipal.this::jbtCadGrupoActionPerformed);
        jToolBar1.add(jbtCadGrupo);
        jbtIncorporarDados.setIcon(new ImageIcon(getClass().getResource("/imagens/incorporaarquivo.gif")));
        jbtIncorporarDados.setToolTipText("Importar dados");
        jbtIncorporarDados.setBorder(BorderFactory.createEtchedBorder());
        jbtIncorporarDados.setHorizontalTextPosition(0);
        jbtIncorporarDados.setMaximumSize(new Dimension(60, 50));
        jbtIncorporarDados.addActionListener(TelaPrincipal.this::jbtIncorporarDadosActionPerformed);
        jToolBar1.add(jbtIncorporarDados);
        jbtExportarDados.setIcon(new ImageIcon(getClass().getResource("/imagens/exportararquivo.gif")));
        jbtExportarDados.setToolTipText("Exportar dados");
        jbtExportarDados.setBorder(BorderFactory.createEtchedBorder());
        jbtExportarDados.setMaximumSize(new Dimension(60, 50));
        jbtExportarDados.addActionListener(TelaPrincipal.this::jbtExportarDadosActionPerformed);
        jToolBar1.add(jbtExportarDados);
        jbtEtiquetasEncomendas.setIcon(new ImageIcon(getClass().getResource("/imagens/etiquetaencomenda.gif")));
        jbtEtiquetasEncomendas.setToolTipText("Etiquetas para encomendas");
        jbtEtiquetasEncomendas.setBorder(BorderFactory.createEtchedBorder());
        jbtEtiquetasEncomendas.setMaximumSize(new Dimension(60, 50));
        jbtEtiquetasEncomendas.addActionListener(TelaPrincipal.this::jbtEtiquetasEncomendasActionPerformed);
        jToolBar1.add(jbtEtiquetasEncomendas);
        jbtEtiquetasCartas.setIcon(new ImageIcon(getClass().getResource("/imagens/etiquetaenvelope.gif")));
        jbtEtiquetasCartas.setToolTipText("Etiquetas para cartas");
        jbtEtiquetasCartas.setBorder(BorderFactory.createEtchedBorder());
        jbtEtiquetasCartas.setMaximumSize(new Dimension(60, 50));
        jbtEtiquetasCartas.addActionListener(TelaPrincipal.this::jbtEtiquetasCartasActionPerformed);
        jToolBar1.add(jbtEtiquetasCartas);
        jbtImpressaoEnvelope.setIcon(new ImageIcon(getClass().getResource("/imagens/envelope.gif")));
        jbtImpressaoEnvelope.setToolTipText("Impressão direta em envelope");
        jbtImpressaoEnvelope.setBorder(BorderFactory.createEtchedBorder());
        jbtImpressaoEnvelope.setMaximumSize(new Dimension(60, 50));
        jbtImpressaoEnvelope.addActionListener(TelaPrincipal.this::jbtImpressaoEnvelopeActionPerformed);
        jToolBar1.add(jbtImpressaoEnvelope);
        jbtSair.setIcon(new ImageIcon(getClass().getResource("/imagens/sair.gif")));
        jbtSair.setToolTipText("Sair do programa");
        jbtSair.setBorder(BorderFactory.createEtchedBorder());
        jbtSair.setMaximumSize(new Dimension(60, 50));
        jbtSair.setMinimumSize(new Dimension(45, 39));
        jbtSair.addActionListener(TelaPrincipal.this::jbtSairActionPerformed);
        jToolBar1.add(jbtSair);
        getContentPane().add(jToolBar1, "North");
        mnArquivo.setMnemonic('C');
        mnArquivo.setText("Cadastro");
        mnArquivo.setName("");
        mnArquivo.addActionListener(TelaPrincipal.this::mnArquivoActionPerformed);
        mnCadRemetente.setMnemonic('r');
        mnCadRemetente.setText("Cadastrar remetentes");
        mnCadRemetente.addActionListener(TelaPrincipal.this::mnCadRemetenteActionPerformed);
        mnArquivo.add(mnCadRemetente);
        mnCadDestinatario.setMnemonic('d');
        mnCadDestinatario.setText("Cadastrar destinatários");
        mnCadDestinatario.addActionListener(TelaPrincipal.this::mnCadDestinatarioActionPerformed);
        mnArquivo.add(mnCadDestinatario);
        mnCadGrupo.setMnemonic('g');
        mnCadGrupo.setText("Cadastrar grupos");
        mnCadGrupo.addActionListener(TelaPrincipal.this::mnCadGrupoActionPerformed);
        mnArquivo.add(mnCadGrupo);
        mnArquivo.add(jSeparator1);
        mnSair.setMnemonic('S');
        mnSair.setText("Sair");
        mnSair.addActionListener(TelaPrincipal.this::mnSairActionPerformed);
        mnArquivo.add(mnSair);
        jMenuBar1.add(mnArquivo);
        mnImpressao.setMnemonic('p');
        mnImpressao.setText("Impressão");
        mnImpressao.addActionListener(TelaPrincipal.this::mnImpressaoActionPerformed);
        mnEtiquetas.setMnemonic('E');
        mnEtiquetas.setText("Etiquetas");
        mnEncomendas.setMnemonic('E');
        mnEncomendas.setText("Encomendas");
        mnEncomendas.addActionListener(TelaPrincipal.this::mnEncomendasActionPerformed);
        mnEtiquetas.add(mnEncomendas);
        mnCartas.setMnemonic('C');
        mnCartas.setText("Cartas");
        mnCartas.addActionListener(TelaPrincipal.this::mnCartasActionPerformed);
        mnEtiquetas.add(mnCartas);
        mnImpressao.add(mnEtiquetas);
        mnEnvelope.setMnemonic('v');
        mnEnvelope.setText("Envelope");
        mnEnvelope.addActionListener(TelaPrincipal.this::mnEnvelopeActionPerformed);
        mnImpressao.add(mnEnvelope);
        jMenuBar1.add(mnImpressao);
        mnFerramentas.setMnemonic('F');
        mnFerramentas.setText("Ferramentas");
        mnImportar.setMnemonic('I');
        mnImportar.setText("Importar dados");
        mnImportar.addActionListener(TelaPrincipal.this::mnImportarActionPerformed);
        mnFerramentas.add(mnImportar);
        mnExportar.setMnemonic('E');
        mnExportar.setText("Exportar dados");
        mnExportar.addActionListener(TelaPrincipal.this::mnExportarActionPerformed);
        mnFerramentas.add(mnExportar);
        mnFerramentas.add(this.jSeparator2);
        this.mnProxy.setMnemonic('p');
        this.mnProxy.setText("Configurar proxy");
        this.mnProxy.addActionListener(TelaPrincipal.this::mnProxyActionPerformed);
        mnFerramentas.add(this.mnProxy);
        jMenuBar1.add(mnFerramentas);
        mnAjuda.setMnemonic('u');
        mnAjuda.setText("Ajuda");
        mnSobre.setMnemonic('S');
        mnSobre.setText("Sobre");
        mnSobre.addActionListener(TelaPrincipal.this::mnSobreActionPerformed);
        mnAjuda.add(mnSobre);
        this.mnTopicosAjuda.setMnemonic('T');
        this.mnTopicosAjuda.setText("Tópicos da ajuda");
        mnAjuda.add(this.mnTopicosAjuda);
        mnAjuda.add(this.jSeparator3);
        this.mnAtualizarVersao.setMnemonic('A');
        this.mnAtualizarVersao.setText("Atualizar versão");
        this.mnAtualizarVersao.addActionListener(TelaPrincipal.this::mnAtualizarVersaoActionPerformed);
        mnAjuda.add(this.mnAtualizarVersao);
        jMenuBar1.add(mnAjuda);
        setJMenuBar(jMenuBar1);
        pack();
    }

    private void formWindowClosing(WindowEvent evt) {
        sairAplicacao();
    }

    private void mnSobreActionPerformed(ActionEvent evt) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        TelaSobre telaSobre = new TelaSobre(this, true);
        telaSobre.setVisible(true);
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    private void mnAtualizarVersaoActionPerformed(ActionEvent evt) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        atualizarVersao();
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    private void mnEnvelopeActionPerformed(ActionEvent evt) {
        jbtImpressaoEnvelopeActionPerformed(evt);
    }

    private void mnCartasActionPerformed(ActionEvent evt) {
        jbtEtiquetasCartasActionPerformed(evt);
    }

    private void jbtEtiquetasCartasActionPerformed(ActionEvent evt) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        TelaImpressaoEnvelope telaImpressaoEnvelope = TelaImpressaoEnvelope.getInstance(this);
        if (!frameExiste(telaImpressaoEnvelope))
            this.desktop.add(telaImpressaoEnvelope);
        telaImpressaoEnvelope.setVisible(true);
        this.hb.enableHelpKey(telaImpressaoEnvelope, "impressaoCarta", this.hs);
        try {
            telaImpressaoEnvelope.setSelected(true);
        } catch (PropertyVetoException ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private void jbtImpressaoEnvelopeActionPerformed(ActionEvent evt) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        TelaImpressaoDiretaEnvelope telaImpressaoDiretaEnvelope = TelaImpressaoDiretaEnvelope.getInstance(this);
        this.hb.enableHelpKey(telaImpressaoDiretaEnvelope, "impressaoEnvelopes", this.hs);
        if (!frameExiste(telaImpressaoDiretaEnvelope))
            this.desktop.add(telaImpressaoDiretaEnvelope);
        telaImpressaoDiretaEnvelope.setVisible(true);
        try {
            telaImpressaoDiretaEnvelope.setSelected(true);
        } catch (PropertyVetoException ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private void mnEncomendasActionPerformed(ActionEvent evt) {
        jbtEtiquetasEncomendasActionPerformed(evt);
    }

    private void mnExportarActionPerformed(ActionEvent evt) {
        jbtExportarDadosActionPerformed(evt);
    }

    private void jbtIncorporarDadosActionPerformed(ActionEvent evt) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        TelaIncorporarDados telaIncorporarDados = TelaIncorporarDados.getInstance();
        if (!frameExiste(telaIncorporarDados))
            this.desktop.add(telaIncorporarDados);
        telaIncorporarDados.setVisible(true);
        this.hb.enableHelpKey(telaIncorporarDados, "importar", this.hs);
        try {
            telaIncorporarDados.setSelected(true);
        } catch (PropertyVetoException ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private void jbtEtiquetasEncomendasActionPerformed(ActionEvent evt) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        TelaImpressaoEncomenda telaImpressaoEncomenda = TelaImpressaoEncomenda.getInstance(this);
        if (!frameExiste(telaImpressaoEncomenda))
            this.desktop.add(telaImpressaoEncomenda);
        telaImpressaoEncomenda.setVisible(true);
        this.hb.enableHelpKey(telaImpressaoEncomenda, "impressaoEncomenda", this.hs);
        try {
            telaImpressaoEncomenda.setSelected(true);
        } catch (PropertyVetoException ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private void jbtExportarDadosActionPerformed(ActionEvent evt) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        TelaExportarDados telaExportarDados = TelaExportarDados.getInstance(this);
        if (!frameExiste(telaExportarDados))
            this.desktop.add(telaExportarDados);
        telaExportarDados.setVisible(true);
        this.hb.enableHelpKey(telaExportarDados, "exportar", this.hs);
        try {
            telaExportarDados.setSelected(true);
        } catch (PropertyVetoException ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private void mnSairActionPerformed(ActionEvent evt) {
        sairAplicacao();
    }

    private void mnCadGrupoActionPerformed(ActionEvent evt) {
        jbtCadGrupoActionPerformed(evt);
    }

    private void mnCadDestinatarioActionPerformed(ActionEvent evt) {
        jbtCadDestinatarioActionPerformed(evt);
    }

    private void jbtCadGrupoActionPerformed(ActionEvent evt) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        TelaGrupo telaGrupo = TelaGrupo.getInstance(this);
        if (!frameExiste(telaGrupo))
            this.desktop.add(telaGrupo);
        telaGrupo.setVisible(true);
        this.hb.enableHelpKey(telaGrupo, "cadGrupos", this.hs);
        try {
            telaGrupo.setSelected(true);
        } catch (PropertyVetoException ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private void mnProxyActionPerformed(ActionEvent evt) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        TelaConfiguracao telaConfiguracao = new TelaConfiguracao(this, true);
        telaConfiguracao.setVisible(true);
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    private void mnConfigUsuarioActionPerformed(ActionEvent evt) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        TelaUsuario telaUsuario = new TelaUsuario(this, true);
        telaUsuario.setVisible(true);
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    private void jbtCadDestinatarioActionPerformed(ActionEvent evt) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        TelaDestinatario telaDestinatario = TelaDestinatario.getInstance(this);
        this.hb.enableHelpKey(telaDestinatario, "cadDestinatarios", this.hs);
        if (!frameExiste(telaDestinatario))
            this.desktop.add(telaDestinatario);
        telaDestinatario.setVisible(true);
        try {
            telaDestinatario.setSelected(true);
        } catch (PropertyVetoException ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private void jbtCadRemetenteActionPerformed(ActionEvent evt) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        TelaRemetente telaRemetente = TelaRemetente.getInstance(this);
        if (!frameExiste(telaRemetente))
            this.desktop.add(telaRemetente);
        telaRemetente.setVisible(true);
        this.hb.enableHelpKey(telaRemetente, "cadRemetentes", this.hs);
        try {
            telaRemetente.setSelected(true);
        } catch (PropertyVetoException ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private void jbtSairActionPerformed(ActionEvent evt) {
        sairAplicacao();
    }

    private void mnImportarActionPerformed(ActionEvent evt) {
        jbtIncorporarDadosActionPerformed(evt);
    }

    private void this_componentResized(ComponentEvent evt) {
        loadBackgroundImage();
    }

    private void mnImpressaoActionPerformed(ActionEvent evt) {
    }

    private void mnArquivoActionPerformed(ActionEvent evt) {
    }

    private void mnCadRemetenteActionPerformed(ActionEvent evt) {
        jbtCadRemetenteActionPerformed(evt);
    }

    private void configuracoesAdicionais() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(50, 50, screenSize.width - 100, screenSize.height - 100);
        ConfiguracaoBean configuracaoBean = ConfiguracaoBean.getInstance();
        try {
            configuracaoBean.carregaVariaveis();
            setTitle("Endereçador Escritório v" + configuracaoBean.getVersao());
            if (configuracaoBean.getBanco().equals("GPBE")) {
                this.mnAtualizarVersao.setVisible(false);
                this.mnProxy.setVisible(false);
                this.mnConfigUsuario.setVisible(false);
                this.jSeparator2.setVisible(false);
                this.jSeparator3.setVisible(false);
            }
        } catch (EnderecadorExcecao ex) {
            JOptionPane.showMessageDialog(this, "Não foi possível buscar configurações da aplicação.", "Endereçador ECT", 2);
            logger.error(ex.getMessage(), ex);
        }
        if (configuracaoBean.getBanco().equals("DNEC") && configuracaoBean.getChave().trim().equals("")) {
            TelaChaveRegistro telaChaveRegistro = new TelaChaveRegistro(this, true);
            telaChaveRegistro.setVisible(true);
        }
    }

    private void loadBackgroundImage() {
        int iconX = getWidth() / 2 - this.lblBackground.getIcon().getIconWidth() / 2;
        int iconY = getHeight() / 2 - this.lblBackground.getIcon().getIconHeight() * 2;
        int iconWidth = this.lblBackground.getIcon().getIconWidth() * 2;
        int iconHeight = this.lblBackground.getIcon().getIconHeight() * 2;
        this.lblBackground.setBounds(iconX, iconY, iconWidth, iconHeight);
    }

    private void sairAplicacao() {
        try {
            ConexaoBD.getInstance().liberarConexao();
        } catch (ConnectException ex) {
            logger.error(ex.getMessage(), ex);
        }
        System.exit(0);
    }
}
