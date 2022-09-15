package br.com.correios.enderecador.view

import javax.swing.JDialog
import javax.swing.JButton
import javax.swing.JComboBox
import javax.swing.JTextField
import javax.swing.JFormattedTextField
import br.com.correios.enderecador.bean.DestinatarioBean
import br.com.correios.enderecador.bean.ConfiguracaoBean
import br.com.correios.enderecador.exception.EnderecadorExcecao
import javax.swing.JOptionPane
import br.com.correios.enderecador.dao.DestinatarioDao
import br.com.correios.enderecador.exception.DaoException
import javax.swing.JToolBar
import javax.swing.JPanel
import javax.swing.JLabel
import javax.swing.BorderFactory
import javax.swing.ImageIcon
import br.com.correios.enderecador.exception.ConfiguracaoProxyException
import br.com.correios.enderecador.exception.ConnectionException
import br.com.correios.enderecador.service.AddressService
import br.com.correios.enderecador.util.*
import net.miginfocom.swing.MigLayout
import org.apache.log4j.Logger
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import java.awt.*
import java.awt.Font.PLAIN
import java.awt.Font.SANS_SERIF

class RecipientEditView : KoinComponent, JDialog {
    private val addressService: AddressService = get()
    private val destinatarioDao: DestinatarioDao = get()

    private val arrayUF = BrazilState.abbreviations()

    private val jtxtCepCaixaPostalDestinatario = JFormattedTextField()
    private val jtxtComplementoDestinatario = JTextField()
    private val jtxtCepDestinatario = JFormattedTextField()
    private val jtxtCaixaPostalDestinatario = JTextField()
    private val jtxtEnderecoDestinatario = JTextField()
    private val jtxtTelefoneDestinatario = JTextField()
    private val jtxtApelidoDestinatario = JTextField()
    private val jtxtCidadeDestinatario = JTextField()
    private val jtxtNumeroDestinatario = JTextField()
    private val jtxtTituloDestinatario = JTextField()
    private val jtxtBairroDestinatario = JTextField()
    private val jtxtEmailDestinatario = JTextField()
    private val jtxtNomeDestinatario = JTextField()
    private val jtxtFaxDestinatario = JTextField()
    private val jbtnCapturaEndereco = JButton()
    private val jcmbUfDestinatario = JComboBox(arrayUF.toComboBox())
    private val jbtnNaoSeiCep = JButton()
    private var numeroDestinatario = ""
    private val blnIncluir: Boolean

    constructor(parent: Frame? = null, modal: Boolean = true) : super(parent, modal) {
        initComponents()
        configuracoesAdicionais()
        jtxtCepDestinatario.maskZipCode()
        jtxtCepCaixaPostalDestinatario.maskZipCode()
        blnIncluir = true
    }

    constructor(destinatarioBean: DestinatarioBean, parent: Frame? = null, modal: Boolean = true) : super(parent, modal) {
        initComponents()
        configuracoesAdicionais()
        jtxtCepDestinatario.maskZipCode()
        jtxtCepCaixaPostalDestinatario.maskZipCode()
        blnIncluir = false

        numeroDestinatario = destinatarioBean.numeroDestinatario!!
        jtxtApelidoDestinatario.text = "" + destinatarioBean.apelido
        jtxtTituloDestinatario.text = "" + destinatarioBean.titulo
        jtxtNomeDestinatario.text = "" + destinatarioBean.nome
        jtxtCepDestinatario.text = "" + destinatarioBean.cep
        jtxtEnderecoDestinatario.text = "" + destinatarioBean.endereco
        jtxtNumeroDestinatario.text = "" + destinatarioBean.numeroEndereco
        jtxtComplementoDestinatario.text = "" + destinatarioBean.complemento
        jtxtBairroDestinatario.text = "" + destinatarioBean.bairro
        jtxtCidadeDestinatario.text = "" + destinatarioBean.cidade
        jcmbUfDestinatario.selectedItem = destinatarioBean.uf
        jtxtEmailDestinatario.text = "" + destinatarioBean.email
        jtxtTelefoneDestinatario.text = "" + destinatarioBean.telefone
        jtxtFaxDestinatario.text = "" + destinatarioBean.fax
        jtxtCepCaixaPostalDestinatario.text = "" + destinatarioBean.cepCaixaPostal
        jtxtCaixaPostalDestinatario.text = "" + destinatarioBean.caixaPostal
    }

    private fun configuracoesAdicionais() {
        setLocationRelativeTo(null)
        try {
            ConfiguracaoBean.carregaVariaveis()
        } catch (ex: EnderecadorExcecao) {
            JOptionPane.showMessageDialog(
                this,
                "Não foi possível carregar as configurações da aplicação.",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE)
            logger.error(ex.message, ex as Throwable)
        }
    }

    private fun limparCampos() {
        jtxtApelidoDestinatario.text = ""
        jtxtTituloDestinatario.text = ""
        jtxtNomeDestinatario.text = ""
        jtxtCepDestinatario.text = ""
        jtxtEnderecoDestinatario.text = ""
        jtxtNumeroDestinatario.text = ""
        jtxtComplementoDestinatario.text = ""
        jtxtBairroDestinatario.text = ""
        jtxtCidadeDestinatario.text = ""
        jcmbUfDestinatario.selectedIndex = 0
        jtxtEmailDestinatario.text = ""
        jtxtTelefoneDestinatario.text = ""
        jtxtFaxDestinatario.text = ""
        jtxtCepCaixaPostalDestinatario.text = ""
        jtxtCaixaPostalDestinatario.text = ""
    }

    private fun gravaDestinatario() {
        var dadosValidos = false
        if (jtxtNomeDestinatario.text.isBlank()) {
            JOptionPane.showMessageDialog(
                this,
                "O campo Empresa/Nome (linha 1) deve ser preenchido!",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE)
            jtxtNomeDestinatario.requestFocus()
        } else if (jtxtCepDestinatario.text.isBlank()) {
            JOptionPane.showMessageDialog(
                this,
                "O campo CEP deve ser preenchido!",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE)
            jtxtCepDestinatario.requestFocus()
        } else if (jtxtEnderecoDestinatario.text.isBlank()) {
            JOptionPane.showMessageDialog(
                this,
                "O campo endereço deve ser preenchido!",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE)
            jtxtEnderecoDestinatario.requestFocus()
        } else if (jtxtNumeroDestinatario.text.isBlank()) {
            JOptionPane.showMessageDialog(
                this,
                "O campo Numero/Lote deve ser preenchido!",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE)
            jtxtNumeroDestinatario.requestFocus()
        } else if (jtxtCidadeDestinatario.text.isBlank()) {
            JOptionPane.showMessageDialog(
                this,
                "O campo Cidade deve ser preenchido!",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE
            )
            jtxtCidadeDestinatario.requestFocus()
        } else if ((jcmbUfDestinatario.selectedItem as String).isBlank()) {
            JOptionPane.showMessageDialog(
                this,
                "O campo UF deve ser informado!",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE)
            jcmbUfDestinatario.requestFocus()
        } else if (jtxtEmailDestinatario.text.isBlank() && !Geral.validaEmail(jtxtEmailDestinatario.text.trim())) {
            JOptionPane.showMessageDialog(
                this,
                "E-mail inválido!",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE)
            jtxtEmailDestinatario.requestFocus()
        } else if (jtxtCepDestinatario.text.trim().replace("[-_]".toRegex(), "").length == 8) {
            dadosValidos = true
        } else {
            JOptionPane.showMessageDialog(
                this,
                "O campo CEP deve ser preenchido com 8 números!",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE)
            jtxtCepDestinatario.requestFocus()
        }
        if (dadosValidos) {
            try {
                val destinatarioBean = DestinatarioBean(
                    numeroDestinatario = numeroDestinatario,
                    apelido = jtxtApelidoDestinatario.text,
                    titulo = jtxtTituloDestinatario.text,
                    nome = jtxtNomeDestinatario.text,
                    cep = jtxtCepDestinatario.text.replace("[-_]".toRegex(), ""),
                    endereco = jtxtEnderecoDestinatario.text,
                    numeroEndereco = jtxtNumeroDestinatario.text,
                    complemento = jtxtComplementoDestinatario.text,
                    bairro = jtxtBairroDestinatario.text,
                    cidade = jtxtCidadeDestinatario.text,
                    uf = (jcmbUfDestinatario.selectedItem as String),
                    email = jtxtEmailDestinatario.text,
                    telefone = jtxtTelefoneDestinatario.text,
                    fax = jtxtFaxDestinatario.text,
                    cepCaixaPostal = jtxtCepCaixaPostalDestinatario.text,
                    caixaPostal = jtxtCaixaPostalDestinatario.text
                )
                if (blnIncluir) {
                    destinatarioDao.incluirDestinatario(destinatarioBean)
                } else {
                    destinatarioBean.numeroDestinatario = numeroDestinatario
                    destinatarioDao.alterarDestinatario(destinatarioBean)
                }
                JOptionPane.showMessageDialog(
                    this,
                    "Dados Gravados com sucesso",
                    "Enderecador",
                    JOptionPane.INFORMATION_MESSAGE
                )
                limparCampos()
                //enderecadorObservable.notifyObservers(destinatarioBean)
                if (!blnIncluir) {
                    this.isVisible = false
                }
            } catch (ex: DaoException) {
                logger.error(ex.message, ex)
                JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível gravar esse destinatario, verifique se todos " +
                            "os dados foram preenchidos corretamente.",
                    "Enderecador",
                    JOptionPane.WARNING_MESSAGE)
            }
        }
    }

    private fun initComponents() {
        title = "Selecionar destinatário"
        size = Dimension(600, 480)
        isResizable = false
        defaultCloseOperation = DISPOSE_ON_CLOSE

        contentPane.add(JToolBar().apply {
            add(JButton().apply {
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@RecipientEditView.javaClass.getResource("/imagens/OK.gif"))
                text = "Confirmar"
                horizontalTextPosition = 0
                maximumSize = Dimension(90, 60)
                verticalTextPosition = 3
                addActionListener { jbtnConfirmarActionPerformed() }
            })
            add(JButton().apply {
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@RecipientEditView.javaClass.getResource("/imagens/cancelar.gif"))
                text = "Limpar tela"
                horizontalTextPosition = 0
                maximumSize = Dimension(90, 60)
                verticalTextPosition = 3
                addActionListener { limparCampos() }
            })
            add(JButton().apply {
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@RecipientEditView.javaClass.getResource("/imagens/sair.gif"))
                text = "Voltar"
                horizontalTextPosition = 0
                maximumSize = Dimension(90, 60)
                verticalTextPosition = 3
                addActionListener {
                    isVisible = false
                    dispose()
                }
            })
        }, "North")

        contentPane.add(JPanel().apply {
            layout = MigLayout()
            border = BorderFactory.createEtchedBorder()

            add(JLabel().apply {
                font = Font(SANS_SERIF, PLAIN, 10)
                text = "Tratamento (Sr., Sra.):"
            })

            add(jtxtTituloDestinatario, "wrap")

            add(JLabel().apply {
                font = Font(SANS_SERIF, PLAIN, 10)
                text = "* Empresa/Nome (Linha 1):"
            })

            add(jtxtNomeDestinatario.apply {
                document = DocumentoPersonalizado(42, 5)
            }, "span, grow")

            add(JLabel().apply {
                font = Font(SANS_SERIF, PLAIN, 10)
                text = "Empresa/Nome (Linha 2):"
            })

            add(jtxtApelidoDestinatario.apply {
                document = DocumentoPersonalizado(42, 5)
            }, "span, grow")

            add(JLabel().apply {
                font = Font(SANS_SERIF, PLAIN, 10)
                text = "* CEP:"
            })

            add(jtxtCepDestinatario)

            add(jbtnCapturaEndereco.apply {
                font = Font(SANS_SERIF, PLAIN, 10)
                text = "Captura Endereço"
                addActionListener { jbtnCapturaEnderecoActionPerformed() }
            })

            add(jbtnNaoSeiCep.apply {
                font = Font(SANS_SERIF, PLAIN, 10)
                text = "Não sei o CEP"
                addActionListener { jbtnNaoSeiCepActionPerformed() }
            }, "wrap")

            add(JLabel().apply {
                font = Font(SANS_SERIF, PLAIN, 10)
                text = "* Endereço:"
            })

            add(jtxtEnderecoDestinatario.apply {
                document = DocumentoPersonalizado(33, 5)
            }, "span, grow")

            add(JLabel().apply {
                font = Font(SANS_SERIF, PLAIN, 10)
                text = "* Número/Lote:"
            })

            add(jtxtNumeroDestinatario.apply {
                document = DocumentoPersonalizado(8, 5)
            }, "wrap")

            add(JLabel().apply {
                font = Font(SANS_SERIF, PLAIN, 10)
                text = "Complemento:"
            })

            add(jtxtComplementoDestinatario.apply {
                document = DocumentoPersonalizado(35, 5)
            }, "span, grow")

            add(JLabel().apply {
                font = Font(SANS_SERIF, PLAIN, 10)
                text = "Bairro:"
            })

            add(jtxtBairroDestinatario.apply {
                document = DocumentoPersonalizado(42, 5)
            }, "span, grow")

            add(JLabel().apply {
                font = Font(SANS_SERIF, PLAIN, 10)
                text = "* Cidade:"
            })

            add(jtxtCidadeDestinatario.apply {
                document = DocumentoPersonalizado(30, 5)
            })

            add(JLabel().apply {
                font = Font(SANS_SERIF, PLAIN, 10)
                text = "* UF:"
            })

            add(jcmbUfDestinatario, "wrap")

            add(JLabel().apply {
                font = Font(SANS_SERIF, PLAIN, 10)
                text = "E-mail:"
            })

            add(jtxtEmailDestinatario, "span, grow")

            add(JLabel().apply {
                font = Font(SANS_SERIF, PLAIN, 10)
                text = "Telefone:"
            })

            add(jtxtTelefoneDestinatario.apply {
                document = DocumentoPersonalizado(15, 5)
            })

            add(JLabel().apply {
                font = Font(SANS_SERIF, PLAIN, 10)
                text = "Fax:"
            })

            add(jtxtFaxDestinatario, "wrap")

            add(JLabel().apply {
                font = Font(SANS_SERIF, PLAIN, 10)
                text = "Cx. Postal:"
            })

            add(jtxtCaixaPostalDestinatario)

            add(JLabel().apply {
                font = Font(SANS_SERIF, PLAIN, 10)
                text = "CEP Caixa Postal:"
            })

            add(jtxtCepCaixaPostalDestinatario)
        }, "Center")

        contentPane.add(JLabel().apply {
            text = " ATENÇÃO: Os campos marcados com  *  são campos obrigatórios."
            border = BorderFactory.createEtchedBorder()
        }, "South")
    }

    private fun jbtnConfirmarActionPerformed() {
        gravaDestinatario()
    }

    private fun jbtnCapturaEnderecoActionPerformed() {
        if (jtxtCepDestinatario.text.isBlank()) {
            JOptionPane.showMessageDialog(
                this,
                "O campo CEP deve ser preenchido!",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE)
            jtxtCepDestinatario.requestFocus()
            return
        }
        try {
            ConfiguracaoBean.carregaVariaveis()
            addressService.findByZipCode(jtxtCepDestinatario.text)
        } catch (ex: ConfiguracaoProxyException) {
            logger.error(ex.message, ex as Throwable)
            JOptionPane.showMessageDialog(
                this,
                "Não foi possível buscar o CEP!\n" +
                        "Verifique se suas configuraçãoes de Proxy e " +
                        "Usuário de rede estão corretas no menu ferramentas.",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE)
        } catch (e: ConnectionException) {
            JOptionPane.showMessageDialog(
                this,
                e.message,
                "Endereçador",
                JOptionPane.WARNING_MESSAGE)
        } catch (ex: DaoException) {
            logger.error(ex.message, ex as Throwable)
            JOptionPane.showMessageDialog(
                this,
                "Não foi possível buscar o CEP!\n" +
                        "Verifique se sua conexão necessita de um servidor proxy para acessar a Internet.",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE)
        } catch (ex: EnderecadorExcecao) {
            logger.error(ex.message, ex as Throwable)
            JOptionPane.showMessageDialog(
                this,
                "Não foi possível carregar as configurações da aplicação.",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE)
        }
    }

    private fun jbtnNaoSeiCepActionPerformed() {
        try {
            Geral.displayURL(ConfiguracaoBean.paginaPesquisaCep)
        } catch (ex: EnderecadorExcecao) {
            JOptionPane.showMessageDialog(
                this,
                "Não foi possível ativar seu browser, por favor consulte o CEP no seguinte site: " +
                        ConfiguracaoBean.paginaPesquisaCep,
                "Endereçador",
                JOptionPane.WARNING_MESSAGE)
        }
    }

    companion object {
        private val logger = Logger.getLogger(RecipientEditView::class.java)
    }
}
