package br.com.correios.enderecador.telas;

import br.com.correios.enderecador.bean.ConfiguracaoBean;
import br.com.correios.enderecador.bean.EnderecoBean;
import br.com.correios.enderecador.bean.GlobalBean;
import br.com.correios.enderecador.bean.RemetenteBean;
import br.com.correios.enderecador.bean.UsuarioBean;
import br.com.correios.enderecador.dao.*;
import br.com.correios.enderecador.excecao.ConfiguracaoProxyException;
import br.com.correios.enderecador.excecao.EnderecadorExcecao;
import br.com.correios.enderecador.util.DocumentoPersonalizado;
import br.com.correios.enderecador.util.EnderecadorObservable;
import br.com.correios.enderecador.util.Geral;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.text.Document;
import javax.swing.text.MaskFormatter;

import org.apache.log4j.Logger;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

public class TelaEditarRemetente extends JDialog {
    private static final Logger logger = Logger.getLogger(TelaEditarRemetente.class);

    private final EnderecadorObservable observable = EnderecadorObservable.getInstance();

    private final String[] arrayUF = new String[]{
        "", "AC", "AL", "AM", "AP", "BA", "CE", "DF", "ES", "GO",
        "MA", "MG", "MS", "MT", "PA", "PB", "PE", "PI", "PR", "RJ",
        "RN", "RO", "RR", "RS", "SC", "SE", "SP", "TO"};

    private boolean blnIncluir;

    private String numeroRemetente = "";

    private JButton jbtnCapturaEndereco;

    private JButton jbtnNaoSeiCep;

    private JComboBox<String> jcmbUfRemetente;

    private JTextField jtxtApelidoRemetente;

    private JTextField jtxtBairroRemetente;

    private JTextField jtxtCaixaPostalRemetente;

    private JFormattedTextField jtxtCepCaixaPostalRemetente;

    private JFormattedTextField jtxtCepRemetente;

    private JTextField jtxtCidadeRemetente;

    private JTextField jtxtComplementoRemetente;

    private JTextField jtxtEmailRemetente;

    private JTextField jtxtEnderecoRemetente;

    private JTextField jtxtFaxRemetente;

    private JTextField jtxtNomeRemetente;

    private JTextField jtxtNumeroRemetente;

    private JTextField jtxtTelefoneRemetente;

    public TelaEditarRemetente(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        configuracoesAdicionais();
        initFormatters(this.jtxtCepRemetente);
        initFormatters(this.jtxtCepCaixaPostalRemetente);
    }

    public TelaEditarRemetente(Frame parent, boolean modal, RemetenteBean remetenteBean) {
        super(parent, modal);
        initComponents();
        configuracoesAdicionais();
        initFormatters(this.jtxtCepRemetente);
        initFormatters(this.jtxtCepCaixaPostalRemetente);
        this.blnIncluir = false;
        this.numeroRemetente = remetenteBean.getNumeroRemetente();
        this.jtxtApelidoRemetente.setText("" + remetenteBean.getApelido());
        this.jtxtNomeRemetente.setText("" + remetenteBean.getNome());
        this.jtxtCepRemetente.setText("" + remetenteBean.getCep());
        this.jtxtEnderecoRemetente.setText("" + remetenteBean.getEndereco());
        this.jtxtNumeroRemetente.setText("" + remetenteBean.getNumeroEndereco());
        this.jtxtComplementoRemetente.setText("" + remetenteBean.getComplemento());
        this.jtxtBairroRemetente.setText("" + remetenteBean.getBairro());
        this.jtxtCidadeRemetente.setText("" + remetenteBean.getCidade());
        String uf = remetenteBean.getUf();
        for (int i = 0; i < this.arrayUF.length; i++) {
            if (uf.equals(this.arrayUF[i]))
                this.jcmbUfRemetente.setSelectedIndex(i);
        }
        this.jtxtEmailRemetente.setText("" + remetenteBean.getEmail());
        this.jtxtTelefoneRemetente.setText("" + remetenteBean.getTelefone());
        this.jtxtFaxRemetente.setText("" + remetenteBean.getFax());
        this.jtxtCepCaixaPostalRemetente.setText("" + remetenteBean.getCepCaixaPostal());
        this.jtxtCaixaPostalRemetente.setText("" + remetenteBean.getCaixaPostal());
    }

    private void configuracoesAdicionais() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dialogSize = getSize();
        if (dialogSize.height > screenSize.height)
            dialogSize.height = screenSize.height;
        if (dialogSize.width > screenSize.width)
            dialogSize.width = screenSize.width;
        setLocation((screenSize.width - dialogSize.width) / 2, (screenSize.height - dialogSize.height) / 2);
        ConfiguracaoBean configuracaoBean = ConfiguracaoBean.getInstance();
        try {
            configuracaoBean.carregaVariaveis();
        } catch (EnderecadorExcecao ex) {
            JOptionPane.showMessageDialog(this, "Não foi possível carregar as configurações da aplicação.", "Endereçador", JOptionPane.WARNING_MESSAGE);
            logger.error(ex.getMessage(), ex);
            return;
        }
        if (configuracaoBean.getBanco().equals("DNEC") && configuracaoBean.getChave().trim().equals("")) {
            this.jbtnCapturaEndereco.setEnabled(false);
            this.jbtnCapturaEndereco.setToolTipText("Para habilitar esse botão é necessário informar a \n chave de registro no menu Ajuda - Sobre.");
        }
        this.jbtnNaoSeiCep.setVisible(configuracaoBean.getBanco().equals("DNEC"));
    }

    private void initFormatters(JFormattedTextField jft) {
        try {
            MaskFormatter cepFormatter = new MaskFormatter("#####-###");
            cepFormatter.setValidCharacters("0123456789");
            cepFormatter.setPlaceholderCharacter('_');
            cepFormatter.install(jft);
        } catch (ParseException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    private void limparCampos() {
        this.jtxtApelidoRemetente.setText("");
        this.jtxtNomeRemetente.setText("");
        this.jtxtCepRemetente.setText("");
        this.jtxtEnderecoRemetente.setText("");
        this.jtxtNumeroRemetente.setText("");
        this.jtxtComplementoRemetente.setText("");
        this.jtxtBairroRemetente.setText("");
        this.jtxtCidadeRemetente.setText("");
        this.jcmbUfRemetente.setSelectedIndex(0);
        this.jtxtEmailRemetente.setText("");
        this.jtxtTelefoneRemetente.setText("");
        this.jtxtFaxRemetente.setText("");
        this.jtxtCepCaixaPostalRemetente.setText("");
        this.jtxtCaixaPostalRemetente.setText("");
    }

    private void gravaRemetente() {
        boolean dadosValidos = false;
        if (this.jtxtNomeRemetente.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "O campo Empresa/Nome (linha 1) deve ser preenchido!", "Endereçador", JOptionPane.WARNING_MESSAGE);
            this.jtxtNomeRemetente.requestFocus();
        } else if (this.jtxtCepRemetente.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "O campo CEP deve ser preenchido!", "Endereçador", JOptionPane.WARNING_MESSAGE);
            this.jtxtCepRemetente.requestFocus();
        } else if (this.jtxtEnderecoRemetente.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "O campo endereço deve ser preenchido!", "Endereçador", JOptionPane.WARNING_MESSAGE);
            this.jtxtEnderecoRemetente.requestFocus();
        } else if (this.jtxtNumeroRemetente.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "O campo Numero/Lote deve ser preenchido!", "Endereçador", JOptionPane.WARNING_MESSAGE);
            this.jtxtNumeroRemetente.requestFocus();
        } else if (this.jtxtCidadeRemetente.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "O campo Cidade deve ser preenchido!", "Endereçador", JOptionPane.WARNING_MESSAGE);
            this.jtxtCidadeRemetente.requestFocus();
        } else if (Objects.equals(this.jcmbUfRemetente.getSelectedItem(), "")) {
            JOptionPane.showMessageDialog(this, "O campo UF deve ser informado!", "Endereçador", JOptionPane.WARNING_MESSAGE);
            this.jcmbUfRemetente.requestFocus();
        } else if (!this.jtxtEmailRemetente.getText().trim().equals("") && !Geral.validaEmail(this.jtxtEmailRemetente.getText().trim())) {
            JOptionPane.showMessageDialog(this, "E-mail inválido!", "Endereçador", JOptionPane.WARNING_MESSAGE);
            this.jtxtEmailRemetente.requestFocus();
        } else if (this.jtxtCepRemetente.getText().trim().replaceAll("[-_]", "").length() == 8) {
            dadosValidos = true;
        } else {
            JOptionPane.showMessageDialog(this, "O campo CEP deve ser preenchido com 8 números!", "Endereçador", JOptionPane.WARNING_MESSAGE);
            this.jtxtCepRemetente.requestFocus();
        }

        if (dadosValidos) {
            try {
                RemetenteBean remetenteBean = new RemetenteBean();
                remetenteBean.setApelido(this.jtxtApelidoRemetente.getText());
                remetenteBean.setTitulo("");
                remetenteBean.setNome(this.jtxtNomeRemetente.getText());
                remetenteBean.setCep(this.jtxtCepRemetente.getText().replaceAll("[-_]", ""));
                remetenteBean.setEndereco(this.jtxtEnderecoRemetente.getText());
                remetenteBean.setNumeroEndereco(this.jtxtNumeroRemetente.getText());
                remetenteBean.setComplemento(this.jtxtComplementoRemetente.getText());
                remetenteBean.setBairro(this.jtxtBairroRemetente.getText());
                remetenteBean.setCidade(this.jtxtCidadeRemetente.getText());
                remetenteBean.setUf((String) this.jcmbUfRemetente.getSelectedItem());
                remetenteBean.setEmail(this.jtxtEmailRemetente.getText());
                remetenteBean.setTelefone(this.jtxtTelefoneRemetente.getText());
                remetenteBean.setFax(this.jtxtFaxRemetente.getText());
                remetenteBean.setCepCaixaPostal(this.jtxtCepCaixaPostalRemetente.getText());
                remetenteBean.setCaixaPostal(this.jtxtCaixaPostalRemetente.getText());
                RemetenteDao remetenteDao = RemetenteDao.getInstance();
                if (this.blnIncluir) {
                    remetenteDao.incluirRemetente(remetenteBean);
                } else {
                    remetenteBean.setNumeroRemetente(this.numeroRemetente);
                    remetenteDao.alterarRemetente(remetenteBean);
                }

                JOptionPane.showMessageDialog((Component) null, "Dados Gravados com sucesso", "Enderecador", JOptionPane.INFORMATION_MESSAGE);
                this.limparCampos();
                this.observable.notifyObservers(remetenteBean);
                if (!this.blnIncluir) {
                    this.setVisible(false);
                }
            } catch (DaoException ex) {
                logger.error(ex.getMessage(), ex);
                JOptionPane.showMessageDialog(this, "Não foi possível gravar esse remetente, verifique se todos os dados foram preenchidos corretamente.", "Enderecador", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void initComponents() {
        JToolBar jToolBar1 = new JToolBar();
        JButton jbtnConfirmar = new JButton();
        JButton jbtnLimpar = new JButton();
        JButton jbtnVoltar = new JButton();
        JPanel jPanel1 = new JPanel();
        JLabel jLabel2 = new JLabel();
        JLabel jLabel3 = new JLabel();
        JLabel jLabel4 = new JLabel();
        JLabel jLabel5 = new JLabel();
        JLabel jLabel6 = new JLabel();
        JLabel jLabel7 = new JLabel();
        JLabel jLabel8 = new JLabel();
        JLabel jLabel9 = new JLabel();
        JLabel jLabel10 = new JLabel();
        JLabel jLabel11 = new JLabel();
        JLabel jLabel12 = new JLabel();
        this.jtxtNomeRemetente = new JTextField();
        this.jtxtNomeRemetente.setDocument(new DocumentoPersonalizado(42, 5));
        this.jtxtApelidoRemetente = new JTextField();
        this.jtxtApelidoRemetente.setDocument(new DocumentoPersonalizado(42, 5));
        this.jbtnCapturaEndereco = new JButton();
        this.jbtnNaoSeiCep = new JButton();
        this.jtxtEnderecoRemetente = new JTextField();
        this.jtxtEnderecoRemetente.setDocument(new DocumentoPersonalizado(33, 5));
        this.jtxtNumeroRemetente = new JTextField();
        this.jtxtNumeroRemetente.setDocument(new DocumentoPersonalizado(8, 5));
        this.jtxtComplementoRemetente = new JTextField();
        this.jtxtComplementoRemetente.setDocument(new DocumentoPersonalizado(35, 5));
        this.jtxtBairroRemetente = new JTextField();
        this.jtxtBairroRemetente.setDocument(new DocumentoPersonalizado(42, 5));
        this.jtxtCidadeRemetente = new JTextField();
        this.jtxtCidadeRemetente.setDocument(new DocumentoPersonalizado(30, 5));
        JLabel jLabel13 = new JLabel();
        this.jcmbUfRemetente = new JComboBox<>(this.arrayUF);
        this.jtxtEmailRemetente = new JTextField();
        this.jtxtTelefoneRemetente = new JTextField();
        this.jtxtTelefoneRemetente.setDocument(new DocumentoPersonalizado(15, 5));
        JLabel jLabel14 = new JLabel();
        this.jtxtFaxRemetente = new JTextField();
        JLabel jLabel15 = new JLabel();
        this.jtxtCaixaPostalRemetente = new JTextField();
        this.jtxtCepRemetente = new JFormattedTextField();
        this.jtxtCepCaixaPostalRemetente = new JFormattedTextField();
        JLabel jLabel1 = new JLabel();
        setDefaultCloseOperation(2);
        setTitle("Selecionar remetente");
        setSize(new Dimension(600, 400));
        setResizable(false);
        jToolBar1.setBorder(BorderFactory.createEtchedBorder());
        jbtnConfirmar.setFont(new Font("MS Sans Serif", 0, 9));
        jbtnConfirmar.setIcon(new ImageIcon(getClass().getResource("/imagens/OK.gif")));
        jbtnConfirmar.setText("Confirmar");
        jbtnConfirmar.setHorizontalTextPosition(0);
        jbtnConfirmar.setMaximumSize(new Dimension(90, 60));
        jbtnConfirmar.setVerticalTextPosition(3);
        jbtnConfirmar.addActionListener(TelaEditarRemetente.this::jbtnConfirmarActionPerformed);
        jToolBar1.add(jbtnConfirmar);
        jbtnLimpar.setFont(new Font("MS Sans Serif", 0, 9));
        jbtnLimpar.setIcon(new ImageIcon(getClass().getResource("/imagens/cancelar.gif")));
        jbtnLimpar.setText("Limpar tela");
        jbtnLimpar.setHorizontalTextPosition(0);
        jbtnLimpar.setMaximumSize(new Dimension(90, 60));
        jbtnLimpar.setVerticalTextPosition(3);
        jbtnLimpar.addActionListener(TelaEditarRemetente.this::jbtnLimparActionPerformed);
        jToolBar1.add(jbtnLimpar);
        jbtnVoltar.setFont(new Font("MS Sans Serif", 0, 9));
        jbtnVoltar.setIcon(new ImageIcon(getClass().getResource("/imagens/sair.gif")));
        jbtnVoltar.setText("Voltar");
        jbtnVoltar.setHorizontalTextPosition(0);
        jbtnVoltar.setMaximumSize(new Dimension(90, 60));
        jbtnVoltar.setVerticalTextPosition(3);
        jbtnVoltar.addActionListener(TelaEditarRemetente.this::jbtnVoltarActionPerformed);
        jToolBar1.add(jbtnVoltar);
        getContentPane().add(jToolBar1, "North");
        jPanel1.setLayout((LayoutManager) new AbsoluteLayout());
        jPanel1.setBorder(BorderFactory.createEtchedBorder());
        jLabel2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel2.setText("* Empresa/Nome (Linha 1):");
        jPanel1.add(jLabel2, new AbsoluteConstraints(30, 33, -1, -1));
        jLabel3.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel3.setText("Empresa/Nome (Linha 2):");
        jPanel1.add(jLabel3, new AbsoluteConstraints(30, 57, -1, -1));
        jLabel4.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel4.setText("* CEP:");
        jPanel1.add(jLabel4, new AbsoluteConstraints(30, 84, 50, -1));
        jLabel5.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel5.setText("* Endereço:");
        jPanel1.add(jLabel5, new AbsoluteConstraints(30, 110, -1, -1));
        jLabel6.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel6.setText("* Número/Lote:");
        jPanel1.add(jLabel6, new AbsoluteConstraints(30, 136, -1, -1));
        jLabel7.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel7.setText("Complemento:");
        jPanel1.add(jLabel7, new AbsoluteConstraints(30, 160, -1, -1));
        jLabel8.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel8.setText("Bairro:");
        jPanel1.add(jLabel8, new AbsoluteConstraints(30, 184, -1, -1));
        jLabel9.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel9.setText("* Cidade:");
        jPanel1.add(jLabel9, new AbsoluteConstraints(30, 210, -1, -1));
        jLabel10.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel10.setText("E-mail:");
        jPanel1.add(jLabel10, new AbsoluteConstraints(30, 235, -1, -1));
        jLabel11.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel11.setText("Telefone:");
        jPanel1.add(jLabel11, new AbsoluteConstraints(30, 260, -1, -1));
        jLabel12.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel12.setText("CEP Caixa Postal:");
        jPanel1.add(jLabel12, new AbsoluteConstraints(30, 285, -1, -1));
        this.jtxtNomeRemetente.setBackground(SystemColor.info);
        jPanel1.add(this.jtxtNomeRemetente, new AbsoluteConstraints(170, 30, 370, -1));
        jPanel1.add(this.jtxtApelidoRemetente, new AbsoluteConstraints(170, 55, 370, -1));
        this.jbtnCapturaEndereco.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        this.jbtnCapturaEndereco.setText("Captura Endereço");
        this.jbtnCapturaEndereco.addActionListener(TelaEditarRemetente.this::jbtnCapturaEnderecoActionPerformed);
        jPanel1.add(this.jbtnCapturaEndereco, new AbsoluteConstraints(280, 80, -1, -1));
        this.jbtnNaoSeiCep.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        this.jbtnNaoSeiCep.setForeground(new Color(0, 0, 153));
        this.jbtnNaoSeiCep.setText("Não sei o CEP");
        this.jbtnNaoSeiCep.addActionListener(TelaEditarRemetente.this::jbtnNaoSeiCepActionPerformed);
        jPanel1.add(this.jbtnNaoSeiCep, new AbsoluteConstraints(440, 80, -1, -1));
        this.jtxtEnderecoRemetente.setBackground(SystemColor.info);
        jPanel1.add(this.jtxtEnderecoRemetente, new AbsoluteConstraints(170, 105, 370, -1));
        this.jtxtNumeroRemetente.setBackground(SystemColor.info);
        jPanel1.add(this.jtxtNumeroRemetente, new AbsoluteConstraints(170, 130, 80, -1));
        jPanel1.add(this.jtxtComplementoRemetente, new AbsoluteConstraints(170, 155, 370, -1));
        jPanel1.add(this.jtxtBairroRemetente, new AbsoluteConstraints(170, 180, 370, -1));
        this.jtxtCidadeRemetente.setBackground(SystemColor.info);
        jPanel1.add(this.jtxtCidadeRemetente, new AbsoluteConstraints(170, 205, 290, -1));
        jLabel13.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel13.setText("* UF:");
        jPanel1.add(jLabel13, new AbsoluteConstraints(470, 210, -1, -1));
        this.jcmbUfRemetente.setBackground(SystemColor.info);
        this.jcmbUfRemetente.addActionListener(TelaEditarRemetente.this::jcmbUfRemetenteActionPerformed);
        jPanel1.add(this.jcmbUfRemetente, new AbsoluteConstraints(495, 205, -1, -1));
        jPanel1.add(this.jtxtEmailRemetente, new AbsoluteConstraints(170, 230, 290, -1));
        jPanel1.add(this.jtxtTelefoneRemetente, new AbsoluteConstraints(170, 255, 180, -1));
        jLabel14.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel14.setText("Fax:");
        jPanel1.add(jLabel14, new AbsoluteConstraints(360, 260, -1, -1));
        jPanel1.add(this.jtxtFaxRemetente, new AbsoluteConstraints(390, 255, 150, -1));
        jLabel15.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel15.setText("Cx. Postal:");
        jPanel1.add(jLabel15, new AbsoluteConstraints(370, 285, -1, -1));
        jPanel1.add(this.jtxtCaixaPostalRemetente, new AbsoluteConstraints(430, 280, 110, -1));
        this.jtxtCepRemetente.setBackground(new Color(255, 255, 225));
        jPanel1.add(this.jtxtCepRemetente, new AbsoluteConstraints(170, 80, 80, -1));
        jPanel1.add(this.jtxtCepCaixaPostalRemetente, new AbsoluteConstraints(170, 280, 80, -1));
        getContentPane().add(jPanel1, "Center");
        jLabel1.setText(" ATENÇÃO: Os campos amarelos marcados com  *  são campos obrigatórios.");
        jLabel1.setBorder(BorderFactory.createEtchedBorder());
        getContentPane().add(jLabel1, "South");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 599) / 2, (screenSize.height - 434) / 2, 599, 434);
    }

    private void jbtnConfirmarActionPerformed(ActionEvent evt) {
        gravaRemetente();
    }

    private void jbtnCapturaEnderecoActionPerformed(ActionEvent evt) {
        boolean blnProxy = false;
        ConfiguracaoBean configuracaoBean = ConfiguracaoBean.getInstance();
        if (this.jtxtCepRemetente.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "O campo CEP deve ser preenchido!", "Endereçador", JOptionPane.WARNING_MESSAGE);
            this.jtxtCepRemetente.requestFocus();
            return;
        }
        try {
            configuracaoBean.carregaVariaveis();
            if (configuracaoBean.getBanco().equalsIgnoreCase("DNEC"))
                if (!configuracaoBean.getProxy().equals("") || !configuracaoBean.getPorta().equals("")) {
                    blnProxy = true;
                    UsuarioBean usuarioBean = UsuarioBean.getInstance();
                    if (usuarioBean.getUsuario().equals("") || usuarioBean.getPwd().equals("")) ;
                }
            InterfaceEnderecoDao enderecoDao = configuracaoBean.getCepStrategy().getFactory().getEndereco();
            EnderecoBean enderecoBean = enderecoDao.consultar(this.jtxtCepRemetente.getText().trim().replaceAll("[-_]", ""), blnProxy);
            this.jtxtEnderecoRemetente.setText(enderecoBean.getLogradouro());
            this.jtxtBairroRemetente.setText(enderecoBean.getBairro());
            this.jtxtCidadeRemetente.setText(enderecoBean.getCidade());
            String uf = enderecoBean.getUf();
            for (int i = 0; i < this.arrayUF.length; i++) {
                if (uf.equals(this.arrayUF[i]))
                    this.jcmbUfRemetente.setSelectedIndex(i);
            }
        } catch (ConfiguracaoProxyException ex) {
            logger.error(ex.getMessage(), ex);
            JOptionPane.showMessageDialog(this, "Não foi possível buscar o CEP!\nVerifique se suas configuraçãoes de Proxy e Usuário de rede estão corretas no menu ferramentas.", "Endereçador", JOptionPane.WARNING_MESSAGE);
        } catch (ConnectionException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Endereçador", JOptionPane.WARNING_MESSAGE);
        } catch (CepInvalidoException e) {
            JOptionPane.showMessageDialog(this, "CEP inválido!", "Endereçador", JOptionPane.WARNING_MESSAGE);
            this.jtxtCepRemetente.requestFocus();
        } catch (DaoException ex) {
            logger.error(ex.getMessage(), (Throwable) ex);
            JOptionPane.showMessageDialog(this, "Não foi possível buscar o CEP!\nVerifique se sua conexão necessita de um servidor proxy para acessar a Internet.", "Endereçador", JOptionPane.WARNING_MESSAGE);
        } catch (EnderecadorExcecao ex) {
            JOptionPane.showMessageDialog(this, "Não foi possível carregar as configurações da aplicação.", "Endereçador", 2);
            logger.error(ex.getMessage(), (Throwable) ex);
        } finally {
            int novaVersao = Integer.parseInt(configuracaoBean.getNovaVersao().toUpperCase().replaceAll("[.]", "").replaceAll("BETA", "").trim());
            int versaoAplicacao = Integer.parseInt(configuracaoBean.getVersao().toUpperCase().replaceAll("[.]", "").replaceAll("BETA", "").trim());
            if (novaVersao > versaoAplicacao)
                if (GlobalBean.getInstance().getMostraMensagem().equals("SIM")) {
                    TelaMensagem telaMensagem = new TelaMensagem();
                    telaMensagem.setVisible(true);
                }
        }
    }

    private void jbtnNaoSeiCepActionPerformed(ActionEvent evt) {
        try {
            Geral.displayURL(ConfiguracaoBean.getInstance().getPaginaPesquisaCep());
        } catch (EnderecadorExcecao ex) {
            JOptionPane.showMessageDialog(this, "Não foi possível ativar seu browse, por favor consulte o CEP no seguinte site: " + ConfiguracaoBean.getInstance().getPaginaPesquisaCep(), "Endereçador", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void jbtnVoltarActionPerformed(ActionEvent evt) {
        setVisible(false);
    }

    private void jbtnLimparActionPerformed(ActionEvent evt) {
        limparCampos();
    }

    private void jcmbUfRemetenteActionPerformed(ActionEvent evt) {
    }
}
