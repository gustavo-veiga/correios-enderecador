package br.com.correios.enderecador.telas

import javax.swing.JDialog
import br.com.correios.enderecador.util.EnderecadorObservable
import javax.swing.JButton
import javax.swing.JComboBox
import javax.swing.JTextField
import javax.swing.JFormattedTextField
import br.com.correios.enderecador.bean.RemetenteBean
import br.com.correios.enderecador.bean.ConfiguracaoBean
import br.com.correios.enderecador.excecao.EnderecadorExcecao
import javax.swing.JOptionPane
import javax.swing.text.MaskFormatter
import br.com.correios.enderecador.util.Geral
import br.com.correios.enderecador.dao.RemetenteDao
import br.com.correios.enderecador.dao.DaoException
import javax.swing.JToolBar
import javax.swing.JPanel
import javax.swing.JLabel
import br.com.correios.enderecador.util.DocumentoPersonalizado
import javax.swing.BorderFactory
import javax.swing.ImageIcon
import java.awt.event.ActionEvent
import org.netbeans.lib.awtextra.AbsoluteLayout
import org.netbeans.lib.awtextra.AbsoluteConstraints
import br.com.correios.enderecador.bean.UsuarioBean
import br.com.correios.enderecador.excecao.ConfiguracaoProxyException
import br.com.correios.enderecador.dao.ConnectionException
import java.util.Locale
import br.com.correios.enderecador.bean.GlobalBean
import br.com.correios.enderecador.dao.CepInvalidoException
import org.apache.log4j.Logger
import java.awt.*
import java.text.ParseException

class TelaEditarRemetente : JDialog {
    private val observable = EnderecadorObservable.instance
    private val arrayUF = arrayOf(
        "", "AC", "AL", "AM", "AP", "BA", "CE", "DF", "ES", "GO",
        "MA", "MG", "MS", "MT", "PA", "PB", "PE", "PI", "PR", "RJ",
        "RN", "RO", "RR", "RS", "SC", "SE", "SP", "TO"
    )
    private var blnIncluir = false
    private var numeroRemetente: String? = ""
    private var jbtnCapturaEndereco: JButton? = null
    private var jbtnNaoSeiCep: JButton? = null
    private var jcmbUfRemetente: JComboBox<String>? = null
    private var jtxtApelidoRemetente: JTextField? = null
    private var jtxtBairroRemetente: JTextField? = null
    private var jtxtCaixaPostalRemetente: JTextField? = null
    private var jtxtCepCaixaPostalRemetente: JFormattedTextField? = null
    private var jtxtCepRemetente: JFormattedTextField? = null
    private var jtxtCidadeRemetente: JTextField? = null
    private var jtxtComplementoRemetente: JTextField? = null
    private var jtxtEmailRemetente: JTextField? = null
    private var jtxtEnderecoRemetente: JTextField? = null
    private var jtxtFaxRemetente: JTextField? = null
    private var jtxtNomeRemetente: JTextField? = null
    private var jtxtNumeroRemetente: JTextField? = null
    private var jtxtTelefoneRemetente: JTextField? = null

    constructor(parent: Frame?, modal: Boolean) : super(parent, modal) {
        initComponents()
        configuracoesAdicionais()
        initFormatters(jtxtCepRemetente)
        initFormatters(jtxtCepCaixaPostalRemetente)
    }

    constructor(parent: Frame?, modal: Boolean, remetenteBean: RemetenteBean) : super(parent, modal) {
        initComponents()
        configuracoesAdicionais()
        initFormatters(jtxtCepRemetente)
        initFormatters(jtxtCepCaixaPostalRemetente)
        blnIncluir = false
        numeroRemetente = remetenteBean.numeroRemetente
        jtxtApelidoRemetente!!.text = "" + remetenteBean.apelido
        jtxtNomeRemetente!!.text = "" + remetenteBean.nome
        jtxtCepRemetente!!.text = "" + remetenteBean.cep
        jtxtEnderecoRemetente!!.text = "" + remetenteBean.endereco
        jtxtNumeroRemetente!!.text = "" + remetenteBean.numeroEndereco
        jtxtComplementoRemetente!!.text = "" + remetenteBean.complemento
        jtxtBairroRemetente!!.text = "" + remetenteBean.bairro
        jtxtCidadeRemetente!!.text = "" + remetenteBean.cidade
        val uf = remetenteBean.uf
        for (i in arrayUF.indices) {
            if (uf == arrayUF[i]) jcmbUfRemetente!!.selectedIndex = i
        }
        jtxtEmailRemetente!!.text = "" + remetenteBean.email
        jtxtTelefoneRemetente!!.text = "" + remetenteBean.telefone
        jtxtFaxRemetente!!.text = "" + remetenteBean.fax
        jtxtCepCaixaPostalRemetente!!.text = "" + remetenteBean.cepCaixaPostal
        jtxtCaixaPostalRemetente!!.text = "" + remetenteBean.caixaPostal
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
                JOptionPane.WARNING_MESSAGE
            )
            logger.error(ex.message, ex)
            return
        }
        if (configuracaoBean.banco == "DNEC" && configuracaoBean.chave.trim { it <= ' ' } == "") {
            jbtnCapturaEndereco!!.isEnabled = false
            jbtnCapturaEndereco!!.toolTipText =
                "Para habilitar esse botão é necessário informar a \n chave de registro no menu Ajuda - Sobre."
        }
        jbtnNaoSeiCep!!.isVisible = configuracaoBean.banco == "DNEC"
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

    private fun limparCampos() {
        jtxtApelidoRemetente!!.text = ""
        jtxtNomeRemetente!!.text = ""
        jtxtCepRemetente!!.text = ""
        jtxtEnderecoRemetente!!.text = ""
        jtxtNumeroRemetente!!.text = ""
        jtxtComplementoRemetente!!.text = ""
        jtxtBairroRemetente!!.text = ""
        jtxtCidadeRemetente!!.text = ""
        jcmbUfRemetente!!.selectedIndex = 0
        jtxtEmailRemetente!!.text = ""
        jtxtTelefoneRemetente!!.text = ""
        jtxtFaxRemetente!!.text = ""
        jtxtCepCaixaPostalRemetente!!.text = ""
        jtxtCaixaPostalRemetente!!.text = ""
    }

    private fun gravaRemetente() {
        var dadosValidos = false
        if (jtxtNomeRemetente!!.text.trim { it <= ' ' } == "") {
            JOptionPane.showMessageDialog(
                this,
                "O campo Empresa/Nome (linha 1) deve ser preenchido!",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE
            )
            jtxtNomeRemetente!!.requestFocus()
        } else if (jtxtCepRemetente!!.text.trim { it <= ' ' } == "") {
            JOptionPane.showMessageDialog(
                this,
                "O campo CEP deve ser preenchido!",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE
            )
            jtxtCepRemetente!!.requestFocus()
        } else if (jtxtEnderecoRemetente!!.text.trim { it <= ' ' } == "") {
            JOptionPane.showMessageDialog(
                this,
                "O campo endereço deve ser preenchido!",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE
            )
            jtxtEnderecoRemetente!!.requestFocus()
        } else if (jtxtNumeroRemetente!!.text.trim { it <= ' ' } == "") {
            JOptionPane.showMessageDialog(
                this,
                "O campo Numero/Lote deve ser preenchido!",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE
            )
            jtxtNumeroRemetente!!.requestFocus()
        } else if (jtxtCidadeRemetente!!.text.trim { it <= ' ' } == "") {
            JOptionPane.showMessageDialog(
                this,
                "O campo Cidade deve ser preenchido!",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE
            )
            jtxtCidadeRemetente!!.requestFocus()
        } else if (jcmbUfRemetente!!.selectedItem == "") {
            JOptionPane.showMessageDialog(
                this,
                "O campo UF deve ser informado!",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE
            )
            jcmbUfRemetente!!.requestFocus()
        } else if (jtxtEmailRemetente!!.text.isNullOrBlank() && !Geral.validaEmail(jtxtEmailRemetente!!.text.trim())) {
            JOptionPane.showMessageDialog(this, "E-mail inválido!", "Endereçador", JOptionPane.WARNING_MESSAGE)
            jtxtEmailRemetente!!.requestFocus()
        } else if (jtxtCepRemetente!!.text.trim { it <= ' ' }.replace("[-_]".toRegex(), "").length == 8) {
            dadosValidos = true
        } else {
            JOptionPane.showMessageDialog(
                this,
                "O campo CEP deve ser preenchido com 8 números!",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE
            )
            jtxtCepRemetente!!.requestFocus()
        }
        if (dadosValidos) {
            try {
                val remetenteBean = RemetenteBean()
                remetenteBean.apelido = jtxtApelidoRemetente!!.text
                remetenteBean.titulo = ""
                remetenteBean.nome = jtxtNomeRemetente!!.text
                remetenteBean.cep = jtxtCepRemetente!!.text.replace("[-_]".toRegex(), "")
                remetenteBean.endereco = jtxtEnderecoRemetente!!.text
                remetenteBean.numeroEndereco = jtxtNumeroRemetente!!.text
                remetenteBean.complemento = jtxtComplementoRemetente!!.text
                remetenteBean.bairro = jtxtBairroRemetente!!.text
                remetenteBean.cidade = jtxtCidadeRemetente!!.text
                remetenteBean.uf = jcmbUfRemetente!!.selectedItem as String
                remetenteBean.email = jtxtEmailRemetente!!.text
                remetenteBean.telefone = jtxtTelefoneRemetente!!.text
                remetenteBean.fax = jtxtFaxRemetente!!.text
                remetenteBean.cepCaixaPostal = jtxtCepCaixaPostalRemetente!!.text
                remetenteBean.caixaPostal = jtxtCaixaPostalRemetente!!.text
                val remetenteDao = RemetenteDao.instance
                if (blnIncluir) {
                    remetenteDao!!.incluirRemetente(remetenteBean)
                } else {
                    remetenteBean.numeroRemetente = numeroRemetente
                    remetenteDao!!.alterarRemetente(remetenteBean)
                }
                JOptionPane.showMessageDialog(
                    null as Component?,
                    "Dados Gravados com sucesso",
                    "Enderecador",
                    JOptionPane.INFORMATION_MESSAGE
                )
                limparCampos()
                observable?.notifyObservers(remetenteBean)
                if (!blnIncluir) {
                    this.isVisible = false
                }
            } catch (ex: DaoException) {
                logger.error(ex.message, ex)
                JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível gravar esse remetente, verifique se todos os dados foram preenchidos corretamente.",
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
        jtxtNomeRemetente = JTextField()
        jtxtNomeRemetente!!.document = DocumentoPersonalizado(42, 5)
        jtxtApelidoRemetente = JTextField()
        jtxtApelidoRemetente!!.document = DocumentoPersonalizado(42, 5)
        jbtnCapturaEndereco = JButton()
        jbtnNaoSeiCep = JButton()
        jtxtEnderecoRemetente = JTextField()
        jtxtEnderecoRemetente!!.document = DocumentoPersonalizado(33, 5)
        jtxtNumeroRemetente = JTextField()
        jtxtNumeroRemetente!!.document = DocumentoPersonalizado(8, 5)
        jtxtComplementoRemetente = JTextField()
        jtxtComplementoRemetente!!.document = DocumentoPersonalizado(35, 5)
        jtxtBairroRemetente = JTextField()
        jtxtBairroRemetente!!.document = DocumentoPersonalizado(42, 5)
        jtxtCidadeRemetente = JTextField()
        jtxtCidadeRemetente!!.document = DocumentoPersonalizado(30, 5)
        val jLabel13 = JLabel()
        jcmbUfRemetente = JComboBox(arrayUF)
        jtxtEmailRemetente = JTextField()
        jtxtTelefoneRemetente = JTextField()
        jtxtTelefoneRemetente!!.document = DocumentoPersonalizado(15, 5)
        val jLabel14 = JLabel()
        jtxtFaxRemetente = JTextField()
        val jLabel15 = JLabel()
        jtxtCaixaPostalRemetente = JTextField()
        jtxtCepRemetente = JFormattedTextField()
        jtxtCepCaixaPostalRemetente = JFormattedTextField()
        val jLabel1 = JLabel()
        defaultCloseOperation = 2
        title = "Selecionar remetente"
        size = Dimension(600, 400)
        isResizable = false
        jToolBar1.border = BorderFactory.createEtchedBorder()
        jbtnConfirmar.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jbtnConfirmar.icon = ImageIcon(javaClass.getResource("/imagens/OK.gif"))
        jbtnConfirmar.text = "Confirmar"
        jbtnConfirmar.horizontalTextPosition = 0
        jbtnConfirmar.maximumSize = Dimension(90, 60)
        jbtnConfirmar.verticalTextPosition = 3
        jbtnConfirmar.addActionListener { evt: ActionEvent -> jbtnConfirmarActionPerformed(evt) }
        jToolBar1.add(jbtnConfirmar)
        jbtnLimpar.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jbtnLimpar.icon = ImageIcon(javaClass.getResource("/imagens/cancelar.gif"))
        jbtnLimpar.text = "Limpar tela"
        jbtnLimpar.horizontalTextPosition = 0
        jbtnLimpar.maximumSize = Dimension(90, 60)
        jbtnLimpar.verticalTextPosition = 3
        jbtnLimpar.addActionListener { evt: ActionEvent -> jbtnLimparActionPerformed(evt) }
        jToolBar1.add(jbtnLimpar)
        jbtnVoltar.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jbtnVoltar.icon = ImageIcon(javaClass.getResource("/imagens/sair.gif"))
        jbtnVoltar.text = "Voltar"
        jbtnVoltar.horizontalTextPosition = 0
        jbtnVoltar.maximumSize = Dimension(90, 60)
        jbtnVoltar.verticalTextPosition = 3
        jbtnVoltar.addActionListener { evt: ActionEvent -> jbtnVoltarActionPerformed(evt) }
        jToolBar1.add(jbtnVoltar)
        contentPane.add(jToolBar1, "North")
        jPanel1.layout = AbsoluteLayout()
        jPanel1.border = BorderFactory.createEtchedBorder()
        jLabel2.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jLabel2.text = "* Empresa/Nome (Linha 1):"
        jPanel1.add(jLabel2, AbsoluteConstraints(30, 33, -1, -1))
        jLabel3.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jLabel3.text = "Empresa/Nome (Linha 2):"
        jPanel1.add(jLabel3, AbsoluteConstraints(30, 57, -1, -1))
        jLabel4.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jLabel4.text = "* CEP:"
        jPanel1.add(jLabel4, AbsoluteConstraints(30, 84, 50, -1))
        jLabel5.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jLabel5.text = "* Endereço:"
        jPanel1.add(jLabel5, AbsoluteConstraints(30, 110, -1, -1))
        jLabel6.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jLabel6.text = "* Número/Lote:"
        jPanel1.add(jLabel6, AbsoluteConstraints(30, 136, -1, -1))
        jLabel7.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jLabel7.text = "Complemento:"
        jPanel1.add(jLabel7, AbsoluteConstraints(30, 160, -1, -1))
        jLabel8.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jLabel8.text = "Bairro:"
        jPanel1.add(jLabel8, AbsoluteConstraints(30, 184, -1, -1))
        jLabel9.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jLabel9.text = "* Cidade:"
        jPanel1.add(jLabel9, AbsoluteConstraints(30, 210, -1, -1))
        jLabel10.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jLabel10.text = "E-mail:"
        jPanel1.add(jLabel10, AbsoluteConstraints(30, 235, -1, -1))
        jLabel11.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jLabel11.text = "Telefone:"
        jPanel1.add(jLabel11, AbsoluteConstraints(30, 260, -1, -1))
        jLabel12.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jLabel12.text = "CEP Caixa Postal:"
        jPanel1.add(jLabel12, AbsoluteConstraints(30, 285, -1, -1))
        jtxtNomeRemetente!!.background = SystemColor.info
        jPanel1.add(jtxtNomeRemetente, AbsoluteConstraints(170, 30, 370, -1))
        jPanel1.add(jtxtApelidoRemetente, AbsoluteConstraints(170, 55, 370, -1))
        jbtnCapturaEndereco!!.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jbtnCapturaEndereco!!.text = "Captura Endereço"
        jbtnCapturaEndereco!!.addActionListener { evt: ActionEvent -> jbtnCapturaEnderecoActionPerformed(evt) }
        jPanel1.add(jbtnCapturaEndereco, AbsoluteConstraints(280, 80, -1, -1))
        jbtnNaoSeiCep!!.font = Font(Font.SANS_SERIF, Font.PLAIN, 9)
        jbtnNaoSeiCep!!.foreground = Color(0, 0, 153)
        jbtnNaoSeiCep!!.text = "Não sei o CEP"
        jbtnNaoSeiCep!!.addActionListener { evt: ActionEvent -> jbtnNaoSeiCepActionPerformed(evt) }
        jPanel1.add(jbtnNaoSeiCep, AbsoluteConstraints(440, 80, -1, -1))
        jtxtEnderecoRemetente!!.background = SystemColor.info
        jPanel1.add(jtxtEnderecoRemetente, AbsoluteConstraints(170, 105, 370, -1))
        jtxtNumeroRemetente!!.background = SystemColor.info
        jPanel1.add(jtxtNumeroRemetente, AbsoluteConstraints(170, 130, 80, -1))
        jPanel1.add(jtxtComplementoRemetente, AbsoluteConstraints(170, 155, 370, -1))
        jPanel1.add(jtxtBairroRemetente, AbsoluteConstraints(170, 180, 370, -1))
        jtxtCidadeRemetente!!.background = SystemColor.info
        jPanel1.add(jtxtCidadeRemetente, AbsoluteConstraints(170, 205, 290, -1))
        jLabel13.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jLabel13.text = "* UF:"
        jPanel1.add(jLabel13, AbsoluteConstraints(470, 210, -1, -1))
        jcmbUfRemetente!!.background = SystemColor.info
        jcmbUfRemetente!!.addActionListener { evt: ActionEvent -> jcmbUfRemetenteActionPerformed(evt) }
        jPanel1.add(jcmbUfRemetente, AbsoluteConstraints(495, 205, -1, -1))
        jPanel1.add(jtxtEmailRemetente, AbsoluteConstraints(170, 230, 290, -1))
        jPanel1.add(jtxtTelefoneRemetente, AbsoluteConstraints(170, 255, 180, -1))
        jLabel14.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jLabel14.text = "Fax:"
        jPanel1.add(jLabel14, AbsoluteConstraints(360, 260, -1, -1))
        jPanel1.add(jtxtFaxRemetente, AbsoluteConstraints(390, 255, 150, -1))
        jLabel15.font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        jLabel15.text = "Cx. Postal:"
        jPanel1.add(jLabel15, AbsoluteConstraints(370, 285, -1, -1))
        jPanel1.add(jtxtCaixaPostalRemetente, AbsoluteConstraints(430, 280, 110, -1))
        jtxtCepRemetente!!.background = Color(255, 255, 225)
        jPanel1.add(jtxtCepRemetente, AbsoluteConstraints(170, 80, 80, -1))
        jPanel1.add(jtxtCepCaixaPostalRemetente, AbsoluteConstraints(170, 280, 80, -1))
        contentPane.add(jPanel1, "Center")
        jLabel1.text = " ATENÇÃO: Os campos amarelos marcados com  *  são campos obrigatórios."
        jLabel1.border = BorderFactory.createEtchedBorder()
        contentPane.add(jLabel1, "South")
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        setBounds((screenSize.width - 599) / 2, (screenSize.height - 434) / 2, 599, 434)
    }

    private fun jbtnConfirmarActionPerformed(evt: ActionEvent) {
        gravaRemetente()
    }

    private fun jbtnCapturaEnderecoActionPerformed(evt: ActionEvent) {
        var blnProxy = false
        val configuracaoBean = ConfiguracaoBean.instance
        if (jtxtCepRemetente!!.text.trim { it <= ' ' } == "") {
            JOptionPane.showMessageDialog(
                this,
                "O campo CEP deve ser preenchido!",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE
            )
            jtxtCepRemetente!!.requestFocus()
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
            val enderecoBean = enderecoDao!!.consultar(jtxtCepRemetente!!.text.trim { it <= ' ' }
                .replace("[-_]".toRegex(), ""), blnProxy)
            jtxtEnderecoRemetente!!.text = enderecoBean!!.logradouro
            jtxtBairroRemetente!!.text = enderecoBean.bairro
            jtxtCidadeRemetente!!.text = enderecoBean.cidade
            val uf = enderecoBean.uf
            for (i in arrayUF.indices) {
                if (uf == arrayUF[i]) jcmbUfRemetente!!.selectedIndex = i
            }
        } catch (ex: ConfiguracaoProxyException) {
            logger.error(ex.message, ex)
            JOptionPane.showMessageDialog(
                this,
                "Não foi possível buscar o CEP!\nVerifique se suas configuraçãoes de Proxy e Usuário de rede estão corretas no menu ferramentas.",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE
            )
        } catch (e: ConnectionException) {
            JOptionPane.showMessageDialog(this, e.message, "Endereçador", JOptionPane.WARNING_MESSAGE)
        } catch (e: CepInvalidoException) {
            JOptionPane.showMessageDialog(this, "CEP inválido!", "Endereçador", JOptionPane.WARNING_MESSAGE)
            jtxtCepRemetente!!.requestFocus()
        } catch (ex: DaoException) {
            logger.error(ex.message, ex as Throwable)
            JOptionPane.showMessageDialog(
                this,
                "Não foi possível buscar o CEP!\nVerifique se sua conexão necessita de um servidor proxy para acessar a Internet.",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE
            )
        } catch (ex: EnderecadorExcecao) {
            JOptionPane.showMessageDialog(
                this,
                "Não foi possível carregar as configurações da aplicação.",
                "Endereçador",
                2
            )
            logger.error(ex.message, ex as Throwable)
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

    private fun jbtnNaoSeiCepActionPerformed(evt: ActionEvent) {
        try {
            Geral.displayURL(ConfiguracaoBean.instance!!.paginaPesquisaCep)
        } catch (ex: EnderecadorExcecao) {
            JOptionPane.showMessageDialog(
                this,
                "Não foi possível ativar seu browse, por favor consulte o CEP no seguinte site: " + ConfiguracaoBean.instance!!.paginaPesquisaCep,
                "Endereçador",
                JOptionPane.WARNING_MESSAGE
            )
        }
    }

    private fun jbtnVoltarActionPerformed(evt: ActionEvent) {
        isVisible = false
    }

    private fun jbtnLimparActionPerformed(evt: ActionEvent) {
        limparCampos()
    }

    private fun jcmbUfRemetenteActionPerformed(evt: ActionEvent) {}

    companion object {
        private val logger = Logger.getLogger(TelaEditarRemetente::class.java)
    }
}
