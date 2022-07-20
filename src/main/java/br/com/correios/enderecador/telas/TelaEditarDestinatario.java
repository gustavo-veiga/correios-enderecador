package br.com.correios.enderecador.telas;

import br.com.correios.enderecador.bean.ConfiguracaoBean;
import br.com.correios.enderecador.bean.DestinatarioBean;
import br.com.correios.enderecador.bean.EnderecoBean;
import br.com.correios.enderecador.bean.GlobalBean;
import br.com.correios.enderecador.bean.UsuarioBean;
import br.com.correios.enderecador.dao.*;
import br.com.correios.enderecador.excecao.ConfiguracaoProxyException;
import br.com.correios.enderecador.excecao.EnderecadorExcecao;
import br.com.correios.enderecador.util.DocumentoPersonalizado;
import br.com.correios.enderecador.util.EnderecadorObservable;
import br.com.correios.enderecador.util.Geral;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.LayoutManager;
import java.awt.Toolkit;
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
import org.jdesktop.layout.GroupLayout;

public class TelaEditarDestinatario extends JDialog {
    private static final Logger logger = Logger.getLogger(TelaEditarDestinatario.class);

    private final EnderecadorObservable observable = EnderecadorObservable.getInstance();

    private final boolean blnIncluir;

    private final String[] arrayUF;

    private String numeroDestinatario = "";

    private JButton jbtnCapturaEndereco;

    private JButton jbtnNaoSeiCep;

    private JComboBox<String> jcmbUfDestinatario;

    private JTextField jtxtApelidoDestinatario;

    private JTextField jtxtBairroDestinatario;

    private JTextField jtxtCaixaPostalDestinatario;

    private JFormattedTextField jtxtCepCaixaPostalDestinatario;

    private JFormattedTextField jtxtCepDestinatario;

    private JTextField jtxtCidadeDestinatario;

    private JTextField jtxtComplementoDestinatario;

    private JTextField jtxtEmailDestinatario;

    private JTextField jtxtEnderecoDestinatario;

    private JTextField jtxtFaxDestinatario;

    private JTextField jtxtNomeDestinatario;

    private JTextField jtxtNumeroDestinatario;

    private JTextField jtxtTelefoneDestinatario;

    private JTextField jtxtTituloDestinatario;

    public TelaEditarDestinatario(Frame parent, boolean modal) {
        super(parent, modal);
        this.arrayUF = new String[]{
            "", "AC", "AL", "AM", "AP", "BA", "CE", "DF", "ES", "GO",
            "MA", "MG", "MS", "MT", "PA", "PB", "PE", "PI", "PR", "RJ",
            "RN", "RO", "RR", "RS", "SC", "SE", "SP", "TO"};
        initComponents();
        configuracoesAdicionais();
        initFormatters(this.jtxtCepDestinatario);
        initFormatters(this.jtxtCepCaixaPostalDestinatario);
        this.blnIncluir = true;
    }

    public TelaEditarDestinatario(Frame parent, boolean modal, DestinatarioBean destinatarioBean) {
        super(parent, modal);
        this.arrayUF = new String[]{
            "", "AC", "AL", "AM", "AP", "BA", "CE", "DF", "ES", "GO",
            "MA", "MG", "MS", "MT", "PA", "PB", "PE", "PI", "PR", "RJ",
            "RN", "RO", "RR", "RS", "SC", "SE", "SP", "TO"};
        initComponents();
        configuracoesAdicionais();
        initFormatters(this.jtxtCepDestinatario);
        initFormatters(this.jtxtCepCaixaPostalDestinatario);
        this.blnIncluir = false;
        this.numeroDestinatario = destinatarioBean.getNumeroDestinatario();
        this.jtxtApelidoDestinatario.setText("" + destinatarioBean.getApelido());
        this.jtxtTituloDestinatario.setText("" + destinatarioBean.getTitulo());
        this.jtxtNomeDestinatario.setText("" + destinatarioBean.getNome());
        this.jtxtCepDestinatario.setText("" + destinatarioBean.getCep());
        this.jtxtEnderecoDestinatario.setText("" + destinatarioBean.getEndereco());
        this.jtxtNumeroDestinatario.setText("" + destinatarioBean.getNumeroEndereco());
        this.jtxtComplementoDestinatario.setText("" + destinatarioBean.getComplemento());
        this.jtxtBairroDestinatario.setText("" + destinatarioBean.getBairro());
        this.jtxtCidadeDestinatario.setText("" + destinatarioBean.getCidade());
        String uf = destinatarioBean.getUf();
        for (int i = 0; i < this.arrayUF.length; i++) {
            if (uf.equals(this.arrayUF[i]))
                this.jcmbUfDestinatario.setSelectedIndex(i);
        }
        this.jtxtEmailDestinatario.setText("" + destinatarioBean.getEmail());
        this.jtxtTelefoneDestinatario.setText("" + destinatarioBean.getTelefone());
        this.jtxtFaxDestinatario.setText("" + destinatarioBean.getFax());
        this.jtxtCepCaixaPostalDestinatario.setText("" + destinatarioBean.getCepCaixaPostal());
        this.jtxtCaixaPostalDestinatario.setText("" + destinatarioBean.getCaixaPostal());
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
            JOptionPane.showMessageDialog(this, "Não foi possível carregar as configurações da aplicação.", "Endereçador", 2);
            logger.error(ex.getMessage(), (Throwable) ex);
        }
        if (configuracaoBean.getBanco().equals("DNEC") && configuracaoBean.getChave().trim().equals("")) {
            this.jbtnCapturaEndereco.setEnabled(false);
            this.jbtnCapturaEndereco.setToolTipText("Para habilitar esse botão é necessário informar a \n chave de registro no menu Ajuda - Sobre.");
        }
        this.jbtnNaoSeiCep.setVisible(configuracaoBean.getBanco().equals("DNEC"));
    }

    private void limparCampos() {
        this.jtxtApelidoDestinatario.setText("");
        this.jtxtTituloDestinatario.setText("");
        this.jtxtNomeDestinatario.setText("");
        this.jtxtCepDestinatario.setText("");
        this.jtxtEnderecoDestinatario.setText("");
        this.jtxtNumeroDestinatario.setText("");
        this.jtxtComplementoDestinatario.setText("");
        this.jtxtBairroDestinatario.setText("");
        this.jtxtCidadeDestinatario.setText("");
        this.jcmbUfDestinatario.setSelectedIndex(0);
        this.jtxtEmailDestinatario.setText("");
        this.jtxtTelefoneDestinatario.setText("");
        this.jtxtFaxDestinatario.setText("");
        this.jtxtCepCaixaPostalDestinatario.setText("");
        this.jtxtCaixaPostalDestinatario.setText("");
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

    private void gravaDestinatario() {
        boolean dadosValidos = false;
        if (this.jtxtNomeDestinatario.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "O campo Empresa/Nome (linha 1) deve ser preenchido!", "Endereçador", JOptionPane.WARNING_MESSAGE);
            this.jtxtNomeDestinatario.requestFocus();
        } else if (this.jtxtCepDestinatario.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "O campo CEP deve ser preenchido!", "Endereçador", JOptionPane.WARNING_MESSAGE);
            this.jtxtCepDestinatario.requestFocus();
        } else if (this.jtxtEnderecoDestinatario.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "O campo endereço deve ser preenchido!", "Endereçador", JOptionPane.WARNING_MESSAGE);
            this.jtxtEnderecoDestinatario.requestFocus();
        } else if (this.jtxtNumeroDestinatario.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "O campo Numero/Lote deve ser preenchido!", "Endereçador", JOptionPane.WARNING_MESSAGE);
            this.jtxtNumeroDestinatario.requestFocus();
        } else if (this.jtxtCidadeDestinatario.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "O campo Cidade deve ser preenchido!", "Endereçador", JOptionPane.WARNING_MESSAGE);
            this.jtxtCidadeDestinatario.requestFocus();
        } else if (Objects.equals(this.jcmbUfDestinatario.getSelectedItem(), "")) {
            JOptionPane.showMessageDialog(this, "O campo UF deve ser informado!", "Endereçador", JOptionPane.WARNING_MESSAGE);
            this.jcmbUfDestinatario.requestFocus();
        } else if (!this.jtxtEmailDestinatario.getText().trim().equals("") && !Geral.validaEmail(this.jtxtEmailDestinatario.getText().trim())) {
            JOptionPane.showMessageDialog(this, "E-mail inválido!", "Endereçador", JOptionPane.WARNING_MESSAGE);
            this.jtxtEmailDestinatario.requestFocus();
        } else if (this.jtxtCepDestinatario.getText().trim().replaceAll("[-_]", "").length() == 8) {
            dadosValidos = true;
        } else {
            JOptionPane.showMessageDialog(this, "O campo CEP deve ser preenchido com 8 números!", "Endereçador", JOptionPane.WARNING_MESSAGE);
            this.jtxtCepDestinatario.requestFocus();
        }

        if (dadosValidos) {
            try {
                DestinatarioBean destinatarioBean = new DestinatarioBean();
                destinatarioBean.setApelido(this.jtxtApelidoDestinatario.getText());
                destinatarioBean.setTitulo("");
                destinatarioBean.setTitulo(this.jtxtTituloDestinatario.getText());
                destinatarioBean.setNome(this.jtxtNomeDestinatario.getText());
                destinatarioBean.setCep(this.jtxtCepDestinatario.getText().replaceAll("[-_]", ""));
                destinatarioBean.setEndereco(this.jtxtEnderecoDestinatario.getText());
                destinatarioBean.setNumeroEndereco(this.jtxtNumeroDestinatario.getText());
                destinatarioBean.setComplemento(this.jtxtComplementoDestinatario.getText());
                destinatarioBean.setBairro(this.jtxtBairroDestinatario.getText());
                destinatarioBean.setCidade(this.jtxtCidadeDestinatario.getText());
                destinatarioBean.setUf((String) this.jcmbUfDestinatario.getSelectedItem());
                destinatarioBean.setEmail(this.jtxtEmailDestinatario.getText());
                destinatarioBean.setTelefone(this.jtxtTelefoneDestinatario.getText());
                destinatarioBean.setFax(this.jtxtFaxDestinatario.getText());
                destinatarioBean.setCepCaixaPostal(this.jtxtCepCaixaPostalDestinatario.getText());
                destinatarioBean.setCaixaPostal(this.jtxtCaixaPostalDestinatario.getText());
                DestinatarioDao destinatarioDao = DestinatarioDao.getInstance();
                if (this.blnIncluir) {
                    destinatarioDao.incluirDestinatario(destinatarioBean);
                } else {
                    destinatarioBean.setNumeroDestinatario(this.numeroDestinatario);
                    destinatarioDao.alterarDestinatario(destinatarioBean);
                }

                JOptionPane.showMessageDialog(this, "Dados Gravados com sucesso", "Enderecador", JOptionPane.INFORMATION_MESSAGE);
                this.limparCampos();
                this.observable.notifyObservers(destinatarioBean);
                if (!this.blnIncluir) {
                    this.setVisible(false);
                }
            } catch (DaoException ex) {
                logger.error(ex.getMessage(), ex);
                JOptionPane.showMessageDialog(this, "Não foi possível gravar esse destinatario, verifique se todos os dados foram preenchidos corretamente.", "Enderecador", JOptionPane.WARNING_MESSAGE);
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
        this.jtxtNomeDestinatario = new JTextField();
        this.jtxtNomeDestinatario.setDocument(new DocumentoPersonalizado(42, 5));
        this.jtxtApelidoDestinatario = new JTextField();
        this.jtxtApelidoDestinatario.setDocument(new DocumentoPersonalizado(42, 5));
        this.jbtnCapturaEndereco = new JButton();
        this.jbtnNaoSeiCep = new JButton();
        this.jtxtEnderecoDestinatario = new JTextField();
        this.jtxtEnderecoDestinatario.setDocument(new DocumentoPersonalizado(33, 5));
        this.jtxtNumeroDestinatario = new JTextField();
        this.jtxtNumeroDestinatario.setDocument(new DocumentoPersonalizado(8, 5));
        this.jtxtComplementoDestinatario = new JTextField();
        this.jtxtComplementoDestinatario.setDocument(new DocumentoPersonalizado(35, 5));
        this.jtxtBairroDestinatario = new JTextField();
        this.jtxtBairroDestinatario.setDocument(new DocumentoPersonalizado(42, 5));
        this.jtxtCidadeDestinatario = new JTextField();
        this.jtxtCidadeDestinatario.setDocument(new DocumentoPersonalizado(30, 5));
        JLabel jLabel13 = new JLabel();
        this.jcmbUfDestinatario = new JComboBox<>(this.arrayUF);
        this.jtxtEmailDestinatario = new JTextField();
        this.jtxtTelefoneDestinatario = new JTextField();
        this.jtxtTelefoneDestinatario.setDocument(new DocumentoPersonalizado(15, 5));
        JLabel jLabel14 = new JLabel();
        this.jtxtFaxDestinatario = new JTextField();
        JLabel jLabel15 = new JLabel();
        this.jtxtCaixaPostalDestinatario = new JTextField();
        JLabel jLabel16 = new JLabel();
        this.jtxtTituloDestinatario = new JTextField();
        this.jtxtApelidoDestinatario.setDocument((Document) new DocumentoPersonalizado(42, 5));
        this.jtxtCepDestinatario = new JFormattedTextField();
        this.jtxtCepCaixaPostalDestinatario = new JFormattedTextField();
        JLabel jLabel1 = new JLabel();
        setDefaultCloseOperation(2);
        setTitle("Selecionar destinatário");
        setResizable(false);
        jToolBar1.setBorder(BorderFactory.createEtchedBorder());
        jbtnConfirmar.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        jbtnConfirmar.setIcon(new ImageIcon(getClass().getResource("/imagens/OK.gif")));
        jbtnConfirmar.setText("Confirmar");
        jbtnConfirmar.setHorizontalTextPosition(0);
        jbtnConfirmar.setMaximumSize(new Dimension(90, 60));
        jbtnConfirmar.setVerticalTextPosition(3);
        jbtnConfirmar.addActionListener(TelaEditarDestinatario.this::jbtnConfirmarActionPerformed);
        jToolBar1.add(jbtnConfirmar);
        jbtnLimpar.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        jbtnLimpar.setIcon(new ImageIcon(getClass().getResource("/imagens/cancelar.gif")));
        jbtnLimpar.setText("Limpar tela");
        jbtnLimpar.setHorizontalTextPosition(0);
        jbtnLimpar.setMaximumSize(new Dimension(90, 60));
        jbtnLimpar.setVerticalTextPosition(3);
        jbtnLimpar.addActionListener(TelaEditarDestinatario.this::jbtnLimparActionPerformed);
        jToolBar1.add(jbtnLimpar);
        jbtnVoltar.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        jbtnVoltar.setIcon(new ImageIcon(getClass().getResource("/imagens/sair.gif")));
        jbtnVoltar.setText("Voltar");
        jbtnVoltar.setHorizontalTextPosition(0);
        jbtnVoltar.setMaximumSize(new Dimension(90, 60));
        jbtnVoltar.setVerticalTextPosition(3);
        jbtnVoltar.addActionListener(TelaEditarDestinatario.this::jbtnVoltarActionPerformed);
        jToolBar1.add(jbtnVoltar);
        getContentPane().add(jToolBar1, "North");
        jPanel1.setBorder(BorderFactory.createEtchedBorder());
        jLabel2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel2.setText("* Empresa/Nome (Linha 1):");
        jLabel3.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel3.setText("Empresa/Nome (Linha 2):");
        jLabel4.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel4.setText("* CEP:");
        jLabel5.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel5.setText("* Endereço:");
        jLabel6.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel6.setText("* Número/Lote:");
        jLabel7.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel7.setText("Complemento:");
        jLabel8.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel8.setText("Bairro:");
        jLabel9.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel9.setText("* Cidade:");
        jLabel10.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel10.setText("E-mail:");
        jLabel11.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel11.setText("Telefone:");
        jLabel12.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel12.setText("CEP Caixa Postal:");
        this.jbtnCapturaEndereco.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        this.jbtnCapturaEndereco.setText("Captura Endereço");
        this.jbtnCapturaEndereco.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                TelaEditarDestinatario.this.jbtnCapturaEnderecoActionPerformed(evt);
            }
        });
        this.jbtnNaoSeiCep.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        this.jbtnNaoSeiCep.setForeground(new Color(0, 0, 153));
        this.jbtnNaoSeiCep.setText("Não sei o CEP");
        this.jbtnNaoSeiCep.addActionListener(TelaEditarDestinatario.this::jbtnNaoSeiCepActionPerformed);
        this.jtxtEnderecoDestinatario.addActionListener(TelaEditarDestinatario.this::jtxtEnderecoDestinatarioActionPerformed);
        jLabel13.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel13.setText("* UF:");
        this.jcmbUfDestinatario.addActionListener(TelaEditarDestinatario.this::jcmbUfDestinatarioActionPerformed);
        jLabel14.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel14.setText("Fax:");
        jLabel15.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel15.setText("Cx. Postal:");
        jLabel16.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        jLabel16.setText("Tratamento (Sr., Sra.):");
        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(1)
            .add(2, jPanel1Layout.createSequentialGroup()
                .add(24, 24, 24)
                .add(jPanel1Layout.createParallelGroup(2)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(2)
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(jPanel1Layout.createParallelGroup(2)
                                    .add(1, jLabel3)
                                    .add(1, jLabel7, -1, -1, 32767)
                                    .add(1, jLabel4).add(1, jLabel5)
                                    .add(jLabel6, -1, -1, 32767))
                                .addPreferredGap(0))
                            .add(1, jPanel1Layout.createSequentialGroup()
                                .add(jPanel1Layout.createParallelGroup(2)
                                    .add(1, jLabel2)
                                    .add(1, jLabel16))
                                .add(12, 12, 12)))
                        .add(jPanel1Layout
                            .createParallelGroup(1)
                            .add(this.jtxtTituloDestinatario, -2, 81, -2)
                            .add(this.jtxtNomeDestinatario, -2, 370, -2)
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(this.jtxtCepDestinatario, -2, 71, -2)
                                .add(17, 17, 17).add(this.jbtnCapturaEndereco)
                                .addPreferredGap(0, -1, 32767)
                                .add(this.jbtnNaoSeiCep, -2, 97, -2))
                            .add(this.jtxtApelidoDestinatario, -2, 370, -2)
                            .add(this.jtxtEnderecoDestinatario, -2, 370, -2)
                            .add(this.jtxtComplementoDestinatario, -2, 370, -2)
                            .add(this.jtxtBairroDestinatario, -2, 370, -2)
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(jPanel1Layout.createParallelGroup(1)
                                    .add(this.jtxtTelefoneDestinatario, -2, 170, -2)
                                    .add(this.jtxtCepCaixaPostalDestinatario, -2, 73, -2))
                                .add(15, 15, 15)
                                .add(jPanel1Layout.createParallelGroup(1)
                                    .add(jPanel1Layout.createSequentialGroup()
                                        .add(jLabel14).addPreferredGap(0, -1, 32767)
                                        .add(this.jtxtFaxDestinatario, -2, 150, -2))
                                    .add(2, jPanel1Layout.createSequentialGroup()
                                        .add(jLabel15).addPreferredGap(0)
                                        .add(this.jtxtCaixaPostalDestinatario, -2, 110, -2))))
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(jPanel1Layout.createParallelGroup(2, false)
                                    .add(1, this.jtxtEmailDestinatario)
                                    .add(1, this.jtxtCidadeDestinatario, -1, 277, 32767))
                                .addPreferredGap(0)
                                .add(jLabel13)
                                .addPreferredGap(0, -1, 32767)
                                .add(this.jcmbUfDestinatario, -2, 44, -2))
                            .add(this.jtxtNumeroDestinatario, -2, 72, -2))
                        .add(184, 184, 184))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(1)
                            .add(jPanel1Layout.createParallelGroup(1)
                                .add(jPanel1Layout.createParallelGroup(1)
                                    .add(jPanel1Layout.createSequentialGroup()
                                        .add(jPanel1Layout.createParallelGroup(1)
                                            .add(jLabel11).add(jLabel10))
                                        .add(94, 94, 94))
                                    .add(jLabel9, -1, -1, 32767))
                                .add(jLabel8))
                            .add(jLabel12)).add(370, 370, 370)))
                .add(49, 49, 49)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(1)
            .add(jPanel1Layout.createSequentialGroup()
                .add(5, 5, 5)
                .add(jPanel1Layout.createParallelGroup(3)
                    .add(jLabel16)
                    .add(this.jtxtTituloDestinatario, -2, -1, -2))
                .addPreferredGap(0)
                .add(jPanel1Layout.createParallelGroup(3)
                    .add(jLabel2)
                    .add(this.jtxtNomeDestinatario, -2, -1, -2))
                .addPreferredGap(0)
                .add(jPanel1Layout.createParallelGroup(1)
                    .add(jLabel3)
                    .add(this.jtxtApelidoDestinatario, -2, -1, -2))
                .addPreferredGap(0)
                .add(jPanel1Layout.createParallelGroup(3)
                    .add(this.jtxtCepDestinatario, -2, 20, -2)
                    .add(this.jbtnCapturaEndereco).add(jLabel4)
                    .add(this.jbtnNaoSeiCep)).addPreferredGap(0)
                .add(jPanel1Layout.createParallelGroup(1)
                    .add(jLabel5)
                    .add(this.jtxtEnderecoDestinatario, -2, -1, -2))
                .add(6, 6, 6)
                .add(jPanel1Layout.createParallelGroup(3)
                    .add(jLabel6)
                    .add(this.jtxtNumeroDestinatario, -2, -1, -2))
                .addPreferredGap(0)
                .add(jPanel1Layout.createParallelGroup(3)
                    .add(jLabel7)
                    .add(this.jtxtComplementoDestinatario, -2, -1, -2))
                .add(8, 8, 8)
                .add(jPanel1Layout.createParallelGroup(3)
                    .add(jLabel8)
                    .add(this.jtxtBairroDestinatario, -2, -1, -2))
                .addPreferredGap(0)
                .add(jPanel1Layout.createParallelGroup(3)
                    .add(jLabel9).add(jLabel13)
                    .add(this.jtxtCidadeDestinatario, -2, -1, -2)
                    .add(this.jcmbUfDestinatario, -2, -1, -2))
                .add(8, 8, 8)
                .add(jPanel1Layout.createParallelGroup(3)
                    .add(jLabel10).add(this.jtxtEmailDestinatario, -2, -1, -2))
                .add(jPanel1Layout.createParallelGroup(1)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(11, 11, 11)
                        .add(jPanel1Layout.createParallelGroup(3)
                            .add(jLabel11).add(jLabel14)))
                    .add(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(0)
                        .add(jPanel1Layout.createParallelGroup(1)
                            .add(this.jtxtTelefoneDestinatario, -2, -1, -2)
                            .add(this.jtxtFaxDestinatario, -2, -1, -2))))
                .addPreferredGap(0)
                .add(jPanel1Layout.createParallelGroup(1)
                    .add(jPanel1Layout.createParallelGroup(3)
                        .add(jLabel12)
                        .add(this.jtxtCepCaixaPostalDestinatario, -2, -1, -2))
                    .add(jPanel1Layout.createParallelGroup(3)
                        .add(this.jtxtCaixaPostalDestinatario, -2, -1, -2)
                        .add(jLabel15)))
                .addContainerGap()));
        getContentPane().add(jPanel1, "Center");
        jLabel1.setText("ATENÇÃO: Os campos amarelos marcados com  *  são campos obrigatórios.");
        jLabel1.setBorder(BorderFactory.createEtchedBorder());
        getContentPane().add(jLabel1, "South");
        setSize(new Dimension(617, 442));
        setLocationRelativeTo(null);
    }

    private void jbtnConfirmarActionPerformed(ActionEvent evt) {
        gravaDestinatario();
    }

    private void jbtnCapturaEnderecoActionPerformed(ActionEvent evt) {
        boolean blnProxy = false;
        ConfiguracaoBean configuracaoBean = ConfiguracaoBean.getInstance();
        if (this.jtxtCepDestinatario.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "O campo CEP deve ser preenchido!", "Endereçador", 2);
            this.jtxtCepDestinatario.requestFocus();
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
            EnderecoBean enderecoBean = enderecoDao.consultar(this.jtxtCepDestinatario.getText().trim().replaceAll("[-_]", ""), blnProxy);
            this.jtxtEnderecoDestinatario.setText(enderecoBean.getLogradouro());
            this.jtxtBairroDestinatario.setText(enderecoBean.getBairro());
            this.jtxtCidadeDestinatario.setText(enderecoBean.getCidade());
            String uf = enderecoBean.getUf();
            for (int i = 0; i < this.arrayUF.length; i++) {
                if (uf.equals(this.arrayUF[i]))
                    this.jcmbUfDestinatario.setSelectedIndex(i);
            }
        } catch (ConfiguracaoProxyException ex) {
            logger.error(ex.getMessage(), (Throwable) ex);
            JOptionPane.showMessageDialog(this, "Não foi possível buscar o CEP!\nVerifique se suas configuraçãoes de Proxy e Usuário de rede estão corretas no menu ferramentas.", "Endereçador", 2);
        } catch (ConnectionException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Endereçador", 2);
        } catch (CepInvalidoException e) {
            JOptionPane.showMessageDialog(this, "CEP inválido!", "Endereçador", 2);
            this.jtxtCepDestinatario.requestFocus();
        } catch (DaoException ex) {
            logger.error(ex.getMessage(), (Throwable) ex);
            JOptionPane.showMessageDialog(this, "Não foi possível buscar o CEP!\nVerifique se sua conexão necessita de um servidor proxy para acessar a Internet.", "Endereçador", 2);
        } catch (EnderecadorExcecao ex) {
            logger.error(ex.getMessage(), (Throwable) ex);
            JOptionPane.showMessageDialog(this, "Não foi possível carregar as configurações da aplicação.", "Endereçador", 2);
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
        Geral geral = null;
        try {
            Geral.displayURL(ConfiguracaoBean.getInstance().getPaginaPesquisaCep());
        } catch (EnderecadorExcecao ex) {
            JOptionPane.showMessageDialog(this, "Não foi possível ativar seu browser, por favor consulte o CEP no seguinte site: " + ConfiguracaoBean.getInstance().getPaginaPesquisaCep(), "Endereçador", 2);
        }
    }

    private void jbtnVoltarActionPerformed(ActionEvent evt) {
        setVisible(false);
    }

    private void jbtnLimparActionPerformed(ActionEvent evt) {
        limparCampos();
    }

    private void jcmbUfDestinatarioActionPerformed(ActionEvent evt) {
    }

    private void jtxtEnderecoDestinatarioActionPerformed(ActionEvent evt) {
    }
}
