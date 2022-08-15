package br.com.correios.enderecador.telas

import javax.swing.JDialog
import br.com.correios.enderecador.util.EnderecadorObservable
import javax.swing.JButton
import javax.swing.JComboBox
import javax.swing.JTextField
import javax.swing.JFormattedTextField
import br.com.correios.enderecador.bean.DestinatarioBean
import br.com.correios.enderecador.bean.ConfiguracaoBean
import br.com.correios.enderecador.excecao.EnderecadorExcecao
import javax.swing.JOptionPane
import javax.swing.text.MaskFormatter
import br.com.correios.enderecador.util.Geral
import br.com.correios.enderecador.dao.DestinatarioDao
import br.com.correios.enderecador.dao.DaoException
import javax.swing.JToolBar
import javax.swing.JPanel
import javax.swing.JLabel
import br.com.correios.enderecador.util.DocumentoPersonalizado
import javax.swing.BorderFactory
import javax.swing.ImageIcon
import br.com.correios.enderecador.bean.UsuarioBean
import br.com.correios.enderecador.excecao.ConfiguracaoProxyException
import br.com.correios.enderecador.dao.ConnectionException
import java.util.Locale
import br.com.correios.enderecador.bean.GlobalBean
import br.com.correios.enderecador.dao.CepInvalidoException
import org.apache.log4j.Logger
import org.jdesktop.layout.GroupLayout
import java.awt.*
import java.awt.Font.PLAIN
import java.awt.Font.SANS_SERIF
import java.text.ParseException

class TelaEditarDestinatario : JDialog {
    private val observable = EnderecadorObservable.instance
    private val blnIncluir: Boolean
    private val arrayUF: Array<String>
    private var numeroDestinatario = ""
    private var jbtnCapturaEndereco: JButton? = null
    private var jbtnNaoSeiCep: JButton? = null
    private var jcmbUfDestinatario: JComboBox<String>? = null
    private var jtxtApelidoDestinatario: JTextField? = null
    private var jtxtBairroDestinatario: JTextField? = null
    private var jtxtCaixaPostalDestinatario: JTextField? = null
    private var jtxtCepCaixaPostalDestinatario: JFormattedTextField? = null
    private var jtxtCepDestinatario: JFormattedTextField? = null
    private var jtxtCidadeDestinatario: JTextField? = null
    private var jtxtComplementoDestinatario: JTextField? = null
    private var jtxtEmailDestinatario: JTextField? = null
    private var jtxtEnderecoDestinatario: JTextField? = null
    private var jtxtFaxDestinatario: JTextField? = null
    private var jtxtNomeDestinatario: JTextField? = null
    private var jtxtNumeroDestinatario: JTextField? = null
    private var jtxtTelefoneDestinatario: JTextField? = null
    private var jtxtTituloDestinatario: JTextField? = null

    constructor(parent: Frame?, modal: Boolean) : super(parent, modal) {
        arrayUF = arrayOf(
            "", "AC", "AL", "AM", "AP", "BA", "CE", "DF", "ES", "GO",
            "MA", "MG", "MS", "MT", "PA", "PB", "PE", "PI", "PR", "RJ",
            "RN", "RO", "RR", "RS", "SC", "SE", "SP", "TO"
        )
        initComponents()
        configuracoesAdicionais()
        initFormatters(jtxtCepDestinatario)
        initFormatters(jtxtCepCaixaPostalDestinatario)
        blnIncluir = true
    }

    constructor(parent: Frame?, modal: Boolean, destinatarioBean: DestinatarioBean) : super(parent, modal) {
        arrayUF = arrayOf(
            "", "AC", "AL", "AM", "AP", "BA", "CE", "DF", "ES", "GO",
            "MA", "MG", "MS", "MT", "PA", "PB", "PE", "PI", "PR", "RJ",
            "RN", "RO", "RR", "RS", "SC", "SE", "SP", "TO"
        )
        initComponents()
        configuracoesAdicionais()
        initFormatters(jtxtCepDestinatario)
        initFormatters(jtxtCepCaixaPostalDestinatario)
        blnIncluir = false
        numeroDestinatario = destinatarioBean.numeroDestinatario
        jtxtApelidoDestinatario!!.text = "" + destinatarioBean.apelido
        jtxtTituloDestinatario!!.text = "" + destinatarioBean.titulo
        jtxtNomeDestinatario!!.text = "" + destinatarioBean.nome
        jtxtCepDestinatario!!.text = "" + destinatarioBean.cep
        jtxtEnderecoDestinatario!!.text = "" + destinatarioBean.endereco
        jtxtNumeroDestinatario!!.text = "" + destinatarioBean.numeroEndereco
        jtxtComplementoDestinatario!!.text = "" + destinatarioBean.complemento
        jtxtBairroDestinatario!!.text = "" + destinatarioBean.bairro
        jtxtCidadeDestinatario!!.text = "" + destinatarioBean.cidade
        val uf = destinatarioBean.uf
        for (i in arrayUF.indices) {
            if (uf == arrayUF[i]) jcmbUfDestinatario!!.selectedIndex = i
        }
        jtxtEmailDestinatario!!.text = "" + destinatarioBean.email
        jtxtTelefoneDestinatario!!.text = "" + destinatarioBean.telefone
        jtxtFaxDestinatario!!.text = "" + destinatarioBean.fax
        jtxtCepCaixaPostalDestinatario!!.text = "" + destinatarioBean.cepCaixaPostal
        jtxtCaixaPostalDestinatario!!.text = "" + destinatarioBean.caixaPostal
    }

    private fun configuracoesAdicionais() {
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        val dialogSize = size
        if (dialogSize.height > screenSize.height) dialogSize.height = screenSize.height
        if (dialogSize.width > screenSize.width) dialogSize.width = screenSize.width
        setLocation((screenSize.width - dialogSize.width) / 2, (screenSize.height - dialogSize.height) / 2)
        val configuracaoBean = ConfiguracaoBean.instance
        try {
            configuracaoBean!!.carregaVariaveis()
        } catch (ex: EnderecadorExcecao) {
            JOptionPane.showMessageDialog(
                this,
                "Não foi possível carregar as configurações da aplicação.",
                "Endereçador",
                2
            )
            logger.error(ex.message, ex as Throwable)
        }
        if (configuracaoBean!!.banco == "DNEC" && configuracaoBean.chave.trim { it <= ' ' } == "") {
            jbtnCapturaEndereco!!.isEnabled = false
            jbtnCapturaEndereco!!.toolTipText =
                "Para habilitar esse botão é necessário informar a \n chave de registro no menu Ajuda - Sobre."
        }
        jbtnNaoSeiCep!!.isVisible = configuracaoBean.banco == "DNEC"
    }

    private fun limparCampos() {
        jtxtApelidoDestinatario!!.text = ""
        jtxtTituloDestinatario!!.text = ""
        jtxtNomeDestinatario!!.text = ""
        jtxtCepDestinatario!!.text = ""
        jtxtEnderecoDestinatario!!.text = ""
        jtxtNumeroDestinatario!!.text = ""
        jtxtComplementoDestinatario!!.text = ""
        jtxtBairroDestinatario!!.text = ""
        jtxtCidadeDestinatario!!.text = ""
        jcmbUfDestinatario!!.selectedIndex = 0
        jtxtEmailDestinatario!!.text = ""
        jtxtTelefoneDestinatario!!.text = ""
        jtxtFaxDestinatario!!.text = ""
        jtxtCepCaixaPostalDestinatario!!.text = ""
        jtxtCaixaPostalDestinatario!!.text = ""
    }

    private fun initFormatters(jft: JFormattedTextField?) {
        try {
            val cepFormatter = MaskFormatter("#####-###")
            cepFormatter.validCharacters = "0123456789"
            cepFormatter.placeholderCharacter = '_'
            cepFormatter.install(jft)
        } catch (ex: ParseException) {
            logger.error(ex.message, ex)
        }
    }

    private fun gravaDestinatario() {
        var dadosValidos = false
        if (jtxtNomeDestinatario!!.text.trim { it <= ' ' } == "") {
            JOptionPane.showMessageDialog(
                this,
                "O campo Empresa/Nome (linha 1) deve ser preenchido!",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE
            )
            jtxtNomeDestinatario!!.requestFocus()
        } else if (jtxtCepDestinatario!!.text.trim { it <= ' ' } == "") {
            JOptionPane.showMessageDialog(
                this,
                "O campo CEP deve ser preenchido!",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE
            )
            jtxtCepDestinatario!!.requestFocus()
        } else if (jtxtEnderecoDestinatario!!.text.trim { it <= ' ' } == "") {
            JOptionPane.showMessageDialog(
                this,
                "O campo endereço deve ser preenchido!",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE
            )
            jtxtEnderecoDestinatario!!.requestFocus()
        } else if (jtxtNumeroDestinatario!!.text.trim { it <= ' ' } == "") {
            JOptionPane.showMessageDialog(
                this,
                "O campo Numero/Lote deve ser preenchido!",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE
            )
            jtxtNumeroDestinatario!!.requestFocus()
        } else if (jtxtCidadeDestinatario!!.text.trim { it <= ' ' } == "") {
            JOptionPane.showMessageDialog(
                this,
                "O campo Cidade deve ser preenchido!",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE
            )
            jtxtCidadeDestinatario!!.requestFocus()
        } else if (jcmbUfDestinatario!!.selectedItem == "") {
            JOptionPane.showMessageDialog(
                this,
                "O campo UF deve ser informado!",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE
            )
            jcmbUfDestinatario!!.requestFocus()
        } else if (jtxtEmailDestinatario!!.text.trim { it <= ' ' } != "" && !Geral.validaEmail(jtxtEmailDestinatario!!.text.trim { it <= ' ' })) {
            JOptionPane.showMessageDialog(this, "E-mail inválido!", "Endereçador", JOptionPane.WARNING_MESSAGE)
            jtxtEmailDestinatario!!.requestFocus()
        } else if (jtxtCepDestinatario!!.text.trim { it <= ' ' }.replace("[-_]".toRegex(), "").length == 8) {
            dadosValidos = true
        } else {
            JOptionPane.showMessageDialog(
                this,
                "O campo CEP deve ser preenchido com 8 números!",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE
            )
            jtxtCepDestinatario!!.requestFocus()
        }
        if (dadosValidos) {
            try {
                val destinatarioBean = DestinatarioBean()
                destinatarioBean.apelido = jtxtApelidoDestinatario!!.text
                destinatarioBean.titulo = ""
                destinatarioBean.titulo = jtxtTituloDestinatario!!.text
                destinatarioBean.nome = jtxtNomeDestinatario!!.text
                destinatarioBean.cep = jtxtCepDestinatario!!.text.replace("[-_]".toRegex(), "")
                destinatarioBean.endereco = jtxtEnderecoDestinatario!!.text
                destinatarioBean.numeroEndereco = jtxtNumeroDestinatario!!.text
                destinatarioBean.complemento = jtxtComplementoDestinatario!!.text
                destinatarioBean.bairro = jtxtBairroDestinatario!!.text
                destinatarioBean.cidade = jtxtCidadeDestinatario!!.text
                destinatarioBean.uf = (jcmbUfDestinatario!!.selectedItem as String)
                destinatarioBean.email = jtxtEmailDestinatario!!.text
                destinatarioBean.telefone = jtxtTelefoneDestinatario!!.text
                destinatarioBean.fax = jtxtFaxDestinatario!!.text
                destinatarioBean.cepCaixaPostal = jtxtCepCaixaPostalDestinatario!!.text
                destinatarioBean.caixaPostal = jtxtCaixaPostalDestinatario!!.text
                val destinatarioDao = DestinatarioDao.instance
                if (blnIncluir) {
                    destinatarioDao!!.incluirDestinatario(destinatarioBean)
                } else {
                    destinatarioBean.numeroDestinatario = numeroDestinatario
                    destinatarioDao!!.alterarDestinatario(destinatarioBean)
                }
                JOptionPane.showMessageDialog(
                    this,
                    "Dados Gravados com sucesso",
                    "Enderecador",
                    JOptionPane.INFORMATION_MESSAGE
                )
                limparCampos()
                observable?.notifyObservers(destinatarioBean)
                if (!blnIncluir) {
                    this.isVisible = false
                }
            } catch (ex: DaoException) {
                logger.error(ex.message, ex)
                JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível gravar esse destinatario, verifique se todos os dados foram preenchidos corretamente.",
                    "Enderecador",
                    JOptionPane.WARNING_MESSAGE
                )
            }
        }
    }

    private fun initComponents() {
        val jToolBar1 = JToolBar()
        val jbtnConfirmar = JButton()
        val jbtnLimpar = JButton()
        val jbtnVoltar = JButton()
        val jPanel1 = JPanel()
        val jLabel2 = JLabel()
        val jLabel3 = JLabel()
        val jLabel4 = JLabel()
        val jLabel5 = JLabel()
        val jLabel6 = JLabel()
        val jLabel7 = JLabel()
        val jLabel8 = JLabel()
        val jLabel9 = JLabel()
        val jLabel10 = JLabel()
        val jLabel11 = JLabel()
        val jLabel12 = JLabel()
        jtxtNomeDestinatario = JTextField()
        jtxtNomeDestinatario!!.document = DocumentoPersonalizado(42, 5)
        jtxtApelidoDestinatario = JTextField()
        jtxtApelidoDestinatario!!.document = DocumentoPersonalizado(42, 5)
        jbtnCapturaEndereco = JButton()
        jbtnNaoSeiCep = JButton()
        jtxtEnderecoDestinatario = JTextField()
        jtxtEnderecoDestinatario!!.document = DocumentoPersonalizado(33, 5)
        jtxtNumeroDestinatario = JTextField()
        jtxtNumeroDestinatario!!.document = DocumentoPersonalizado(8, 5)
        jtxtComplementoDestinatario = JTextField()
        jtxtComplementoDestinatario!!.document = DocumentoPersonalizado(35, 5)
        jtxtBairroDestinatario = JTextField()
        jtxtBairroDestinatario!!.document = DocumentoPersonalizado(42, 5)
        jtxtCidadeDestinatario = JTextField()
        jtxtCidadeDestinatario!!.document = DocumentoPersonalizado(30, 5)
        val jLabel13 = JLabel()
        jcmbUfDestinatario = JComboBox(arrayUF)
        jtxtEmailDestinatario = JTextField()
        jtxtTelefoneDestinatario = JTextField()
        jtxtTelefoneDestinatario!!.document = DocumentoPersonalizado(15, 5)
        val jLabel14 = JLabel()
        jtxtFaxDestinatario = JTextField()
        val jLabel15 = JLabel()
        jtxtCaixaPostalDestinatario = JTextField()
        val jLabel16 = JLabel()
        jtxtTituloDestinatario = JTextField()
        jtxtApelidoDestinatario!!.document = DocumentoPersonalizado(42, 5)
        jtxtCepDestinatario = JFormattedTextField()
        jtxtCepCaixaPostalDestinatario = JFormattedTextField()
        val jLabel1 = JLabel()
        defaultCloseOperation = 2
        title = "Selecionar destinatário"
        isResizable = false
        jToolBar1.border = BorderFactory.createEtchedBorder()
        jbtnConfirmar.font = Font(SANS_SERIF, PLAIN, 9)
        jbtnConfirmar.icon = ImageIcon(javaClass.getResource("/imagens/OK.gif"))
        jbtnConfirmar.text = "Confirmar"
        jbtnConfirmar.horizontalTextPosition = 0
        jbtnConfirmar.maximumSize = Dimension(90, 60)
        jbtnConfirmar.verticalTextPosition = 3
        jbtnConfirmar.addActionListener { jbtnConfirmarActionPerformed() }
        jToolBar1.add(jbtnConfirmar)
        jbtnLimpar.font = Font(SANS_SERIF, PLAIN, 9)
        jbtnLimpar.icon = ImageIcon(javaClass.getResource("/imagens/cancelar.gif"))
        jbtnLimpar.text = "Limpar tela"
        jbtnLimpar.horizontalTextPosition = 0
        jbtnLimpar.maximumSize = Dimension(90, 60)
        jbtnLimpar.verticalTextPosition = 3
        jbtnLimpar.addActionListener { jbtnLimparActionPerformed() }
        jToolBar1.add(jbtnLimpar)
        jbtnVoltar.font = Font(SANS_SERIF, PLAIN, 9)
        jbtnVoltar.icon = ImageIcon(javaClass.getResource("/imagens/sair.gif"))
        jbtnVoltar.text = "Voltar"
        jbtnVoltar.horizontalTextPosition = 0
        jbtnVoltar.maximumSize = Dimension(90, 60)
        jbtnVoltar.verticalTextPosition = 3
        jbtnVoltar.addActionListener { jbtnVoltarActionPerformed() }
        jToolBar1.add(jbtnVoltar)
        contentPane.add(jToolBar1, "North")
        jPanel1.border = BorderFactory.createEtchedBorder()
        jLabel2.font = Font(SANS_SERIF, PLAIN, 10)
        jLabel2.text = "* Empresa/Nome (Linha 1):"
        jLabel3.font = Font(SANS_SERIF, PLAIN, 10)
        jLabel3.text = "Empresa/Nome (Linha 2):"
        jLabel4.font = Font(SANS_SERIF, PLAIN, 10)
        jLabel4.text = "* CEP:"
        jLabel5.font = Font(SANS_SERIF, PLAIN, 10)
        jLabel5.text = "* Endereço:"
        jLabel6.font = Font(SANS_SERIF, PLAIN, 10)
        jLabel6.text = "* Número/Lote:"
        jLabel7.font = Font(SANS_SERIF, PLAIN, 10)
        jLabel7.text = "Complemento:"
        jLabel8.font = Font(SANS_SERIF, PLAIN, 10)
        jLabel8.text = "Bairro:"
        jLabel9.font = Font(SANS_SERIF, PLAIN, 10)
        jLabel9.text = "* Cidade:"
        jLabel10.font = Font(SANS_SERIF, PLAIN, 10)
        jLabel10.text = "E-mail:"
        jLabel11.font = Font(SANS_SERIF, PLAIN, 10)
        jLabel11.text = "Telefone:"
        jLabel12.font = Font(SANS_SERIF, PLAIN, 10)
        jLabel12.text = "CEP Caixa Postal:"
        jbtnCapturaEndereco!!.font = Font(SANS_SERIF, PLAIN, 9)
        jbtnCapturaEndereco!!.text = "Captura Endereço"
        jbtnCapturaEndereco!!.addActionListener { jbtnCapturaEnderecoActionPerformed() }
        jbtnNaoSeiCep!!.font = Font(SANS_SERIF, PLAIN, 9)
        jbtnNaoSeiCep!!.foreground = Color(0, 0, 153)
        jbtnNaoSeiCep!!.text = "Não sei o CEP"
        jbtnNaoSeiCep!!.addActionListener { jbtnNaoSeiCepActionPerformed() }
        jtxtEnderecoDestinatario!!.addActionListener { jtxtEnderecoDestinatarioActionPerformed() }
        jLabel13.font = Font(SANS_SERIF, PLAIN, 10)
        jLabel13.text = "* UF:"
        jcmbUfDestinatario!!.addActionListener { jcmbUfDestinatarioActionPerformed() }
        jLabel14.font = Font(SANS_SERIF, PLAIN, 10)
        jLabel14.text = "Fax:"
        jLabel15.font = Font(SANS_SERIF, PLAIN, 10)
        jLabel15.text = "Cx. Postal:"
        jLabel16.font = Font(SANS_SERIF, PLAIN, 10)
        jLabel16.text = "Tratamento (Sr., Sra.):"
        val jPanel1Layout = GroupLayout(jPanel1)
        jPanel1.layout = jPanel1Layout
        jPanel1Layout.horizontalGroup = jPanel1Layout.createParallelGroup(1)
            .add(
                2, jPanel1Layout.createSequentialGroup()
                    .add(24, 24, 24)
                    .add(
                        jPanel1Layout.createParallelGroup(2)
                            .add(
                                jPanel1Layout.createSequentialGroup()
                                    .add(
                                        jPanel1Layout.createParallelGroup(2)
                                            .add(
                                                jPanel1Layout.createSequentialGroup()
                                                    .add(
                                                        jPanel1Layout.createParallelGroup(2)
                                                            .add(1, jLabel3)
                                                            .add(1, jLabel7, -1, -1, 32767)
                                                            .add(1, jLabel4).add(1, jLabel5)
                                                            .add(jLabel6, -1, -1, 32767)
                                                    )
                                                    .addPreferredGap(0)
                                            )
                                            .add(
                                                1, jPanel1Layout.createSequentialGroup()
                                                    .add(
                                                        jPanel1Layout.createParallelGroup(2)
                                                            .add(1, jLabel2)
                                                            .add(1, jLabel16)
                                                    )
                                                    .add(12, 12, 12)
                                            )
                                    )
                                    .add(
                                        jPanel1Layout
                                            .createParallelGroup(1)
                                            .add(jtxtTituloDestinatario, -2, 81, -2)
                                            .add(jtxtNomeDestinatario, -2, 370, -2)
                                            .add(
                                                jPanel1Layout.createSequentialGroup()
                                                    .add(jtxtCepDestinatario, -2, 71, -2)
                                                    .add(17, 17, 17).add(jbtnCapturaEndereco)
                                                    .addPreferredGap(0, -1, 32767)
                                                    .add(jbtnNaoSeiCep, -2, 97, -2)
                                            )
                                            .add(jtxtApelidoDestinatario, -2, 370, -2)
                                            .add(jtxtEnderecoDestinatario, -2, 370, -2)
                                            .add(jtxtComplementoDestinatario, -2, 370, -2)
                                            .add(jtxtBairroDestinatario, -2, 370, -2)
                                            .add(
                                                jPanel1Layout.createSequentialGroup()
                                                    .add(
                                                        jPanel1Layout.createParallelGroup(1)
                                                            .add(jtxtTelefoneDestinatario, -2, 170, -2)
                                                            .add(jtxtCepCaixaPostalDestinatario, -2, 73, -2)
                                                    )
                                                    .add(15, 15, 15)
                                                    .add(
                                                        jPanel1Layout.createParallelGroup(1)
                                                            .add(
                                                                jPanel1Layout.createSequentialGroup()
                                                                    .add(jLabel14).addPreferredGap(0, -1, 32767)
                                                                    .add(jtxtFaxDestinatario, -2, 150, -2)
                                                            )
                                                            .add(
                                                                2, jPanel1Layout.createSequentialGroup()
                                                                    .add(jLabel15).addPreferredGap(0)
                                                                    .add(jtxtCaixaPostalDestinatario, -2, 110, -2)
                                                            )
                                                    )
                                            )
                                            .add(
                                                jPanel1Layout.createSequentialGroup()
                                                    .add(
                                                        jPanel1Layout.createParallelGroup(2, false)
                                                            .add(1, jtxtEmailDestinatario)
                                                            .add(1, jtxtCidadeDestinatario, -1, 277, 32767)
                                                    )
                                                    .addPreferredGap(0)
                                                    .add(jLabel13)
                                                    .addPreferredGap(0, -1, 32767)
                                                    .add(jcmbUfDestinatario, -2, 44, -2)
                                            )
                                            .add(jtxtNumeroDestinatario, -2, 72, -2)
                                    )
                                    .add(184, 184, 184)
                            )
                            .add(
                                jPanel1Layout.createSequentialGroup()
                                    .add(
                                        jPanel1Layout.createParallelGroup(1)
                                            .add(
                                                jPanel1Layout.createParallelGroup(1)
                                                    .add(
                                                        jPanel1Layout.createParallelGroup(1)
                                                            .add(
                                                                jPanel1Layout.createSequentialGroup()
                                                                    .add(
                                                                        jPanel1Layout.createParallelGroup(1)
                                                                            .add(jLabel11).add(jLabel10)
                                                                    )
                                                                    .add(94, 94, 94)
                                                            )
                                                            .add(jLabel9, -1, -1, 32767)
                                                    )
                                                    .add(jLabel8)
                                            )
                                            .add(jLabel12)
                                    ).add(370, 370, 370)
                            )
                    )
                    .add(49, 49, 49)
            )
        jPanel1Layout.verticalGroup = jPanel1Layout.createParallelGroup(1)
            .add(
                jPanel1Layout.createSequentialGroup()
                    .add(5, 5, 5)
                    .add(
                        jPanel1Layout.createParallelGroup(3)
                            .add(jLabel16)
                            .add(jtxtTituloDestinatario, -2, -1, -2)
                    )
                    .addPreferredGap(0)
                    .add(
                        jPanel1Layout.createParallelGroup(3)
                            .add(jLabel2)
                            .add(jtxtNomeDestinatario, -2, -1, -2)
                    )
                    .addPreferredGap(0)
                    .add(
                        jPanel1Layout.createParallelGroup(1)
                            .add(jLabel3)
                            .add(jtxtApelidoDestinatario, -2, -1, -2)
                    )
                    .addPreferredGap(0)
                    .add(
                        jPanel1Layout.createParallelGroup(3)
                            .add(jtxtCepDestinatario, -2, 20, -2)
                            .add(jbtnCapturaEndereco).add(jLabel4)
                            .add(jbtnNaoSeiCep)
                    ).addPreferredGap(0)
                    .add(
                        jPanel1Layout.createParallelGroup(1)
                            .add(jLabel5)
                            .add(jtxtEnderecoDestinatario, -2, -1, -2)
                    )
                    .add(6, 6, 6)
                    .add(
                        jPanel1Layout.createParallelGroup(3)
                            .add(jLabel6)
                            .add(jtxtNumeroDestinatario, -2, -1, -2)
                    )
                    .addPreferredGap(0)
                    .add(
                        jPanel1Layout.createParallelGroup(3)
                            .add(jLabel7)
                            .add(jtxtComplementoDestinatario, -2, -1, -2)
                    )
                    .add(8, 8, 8)
                    .add(
                        jPanel1Layout.createParallelGroup(3)
                            .add(jLabel8)
                            .add(jtxtBairroDestinatario, -2, -1, -2)
                    )
                    .addPreferredGap(0)
                    .add(
                        jPanel1Layout.createParallelGroup(3)
                            .add(jLabel9).add(jLabel13)
                            .add(jtxtCidadeDestinatario, -2, -1, -2)
                            .add(jcmbUfDestinatario, -2, -1, -2)
                    )
                    .add(8, 8, 8)
                    .add(
                        jPanel1Layout.createParallelGroup(3)
                            .add(jLabel10).add(jtxtEmailDestinatario, -2, -1, -2)
                    )
                    .add(
                        jPanel1Layout.createParallelGroup(1)
                            .add(
                                jPanel1Layout.createSequentialGroup()
                                    .add(11, 11, 11)
                                    .add(
                                        jPanel1Layout.createParallelGroup(3)
                                            .add(jLabel11).add(jLabel14)
                                    )
                            )
                            .add(
                                jPanel1Layout.createSequentialGroup()
                                    .addPreferredGap(0)
                                    .add(
                                        jPanel1Layout.createParallelGroup(1)
                                            .add(jtxtTelefoneDestinatario, -2, -1, -2)
                                            .add(jtxtFaxDestinatario, -2, -1, -2)
                                    )
                            )
                    )
                    .addPreferredGap(0)
                    .add(
                        jPanel1Layout.createParallelGroup(1)
                            .add(
                                jPanel1Layout.createParallelGroup(3)
                                    .add(jLabel12)
                                    .add(jtxtCepCaixaPostalDestinatario, -2, -1, -2)
                            )
                            .add(
                                jPanel1Layout.createParallelGroup(3)
                                    .add(jtxtCaixaPostalDestinatario, -2, -1, -2)
                                    .add(jLabel15)
                            )
                    )
                    .addContainerGap()
            )
        contentPane.add(jPanel1, "Center")
        jLabel1.text = "ATENÇÃO: Os campos amarelos marcados com  *  são campos obrigatórios."
        jLabel1.border = BorderFactory.createEtchedBorder()
        contentPane.add(jLabel1, "South")
        size = Dimension(617, 442)
        setLocationRelativeTo(null)
    }

    private fun jbtnConfirmarActionPerformed() {
        gravaDestinatario()
    }

    private fun jbtnCapturaEnderecoActionPerformed() {
        var blnProxy = false
        val configuracaoBean = ConfiguracaoBean.instance
        if (jtxtCepDestinatario!!.text.trim { it <= ' ' } == "") {
            JOptionPane.showMessageDialog(this, "O campo CEP deve ser preenchido!", "Endereçador", 2)
            jtxtCepDestinatario!!.requestFocus()
            return
        }
        try {
            configuracaoBean!!.carregaVariaveis()
            if (configuracaoBean.banco.equals(
                    "DNEC",
                    ignoreCase = true
                )
            ) if (configuracaoBean.proxy != "" || configuracaoBean.porta != "") {
                blnProxy = true
                val usuarioBean = UsuarioBean.instance
                if (usuarioBean!!.usuario == "" || usuarioBean.pwd == "");
            }
            val enderecoDao = configuracaoBean.cepStrategy!!.factory!!.endereco
            val enderecoBean = enderecoDao!!.consultar(jtxtCepDestinatario!!.text.trim { it <= ' ' }
                .replace("[-_]".toRegex(), ""), blnProxy)
            jtxtEnderecoDestinatario!!.text = enderecoBean!!.logradouro
            jtxtBairroDestinatario!!.text = enderecoBean.bairro
            jtxtCidadeDestinatario!!.text = enderecoBean.cidade
            val uf = enderecoBean.uf
            for (i in arrayUF.indices) {
                if (uf == arrayUF[i]) jcmbUfDestinatario!!.selectedIndex = i
            }
        } catch (ex: ConfiguracaoProxyException) {
            logger.error(ex.message, ex as Throwable)
            JOptionPane.showMessageDialog(
                this,
                "Não foi possível buscar o CEP!\nVerifique se suas configuraçãoes de Proxy e Usuário de rede estão corretas no menu ferramentas.",
                "Endereçador",
                2
            )
        } catch (e: ConnectionException) {
            JOptionPane.showMessageDialog(this, e.message, "Endereçador", 2)
        } catch (e: CepInvalidoException) {
            JOptionPane.showMessageDialog(this, "CEP inválido!", "Endereçador", 2)
            jtxtCepDestinatario!!.requestFocus()
        } catch (ex: DaoException) {
            logger.error(ex.message, ex as Throwable)
            JOptionPane.showMessageDialog(
                this,
                "Não foi possível buscar o CEP!\nVerifique se sua conexão necessita de um servidor proxy para acessar a Internet.",
                "Endereçador",
                2
            )
        } catch (ex: EnderecadorExcecao) {
            logger.error(ex.message, ex as Throwable)
            JOptionPane.showMessageDialog(
                this,
                "Não foi possível carregar as configurações da aplicação.",
                "Endereçador",
                2
            )
        } finally {
            val novaVersao = configuracaoBean!!.novaVersao.uppercase(Locale.getDefault()).replace("[.]".toRegex(), "")
                .replace("BETA".toRegex(), "").trim { it <= ' ' }
                .toInt()
            val versaoAplicacao = configuracaoBean.versao.uppercase(Locale.getDefault()).replace("[.]".toRegex(), "")
                .replace("BETA".toRegex(), "").trim { it <= ' ' }
                .toInt()
            if (novaVersao > versaoAplicacao) if (GlobalBean.instance!!.mostraMensagem == "SIM") {
                val telaMensagem = TelaMensagem()
                telaMensagem.isVisible = true
            }
        }
    }

    private fun jbtnNaoSeiCepActionPerformed() {
        try {
            Geral.displayURL(ConfiguracaoBean.instance!!.paginaPesquisaCep)
        } catch (ex: EnderecadorExcecao) {
            JOptionPane.showMessageDialog(
                this,
                "Não foi possível ativar seu browser, por favor consulte o CEP no seguinte site: " + ConfiguracaoBean.instance!!.paginaPesquisaCep,
                "Endereçador",
                2
            )
        }
    }

    private fun jbtnVoltarActionPerformed() {
        isVisible = false
    }

    private fun jbtnLimparActionPerformed() {
        limparCampos()
    }

    private fun jcmbUfDestinatarioActionPerformed() {}
    private fun jtxtEnderecoDestinatarioActionPerformed() {}

    companion object {
        private val logger = Logger.getLogger(TelaEditarDestinatario::class.java)
    }
}
