package br.com.correios.enderecador.view

import javax.swing.JDialog
import javax.swing.JButton
import javax.swing.JComboBox
import javax.swing.JTextField
import javax.swing.JFormattedTextField
import br.com.correios.enderecador.bean.RemetenteBean
import br.com.correios.enderecador.bean.ConfiguracaoBean
import br.com.correios.enderecador.exception.EnderecadorExcecao
import javax.swing.JOptionPane
import br.com.correios.enderecador.dao.RemetenteDao
import br.com.correios.enderecador.exception.DaoException
import javax.swing.JToolBar
import javax.swing.JPanel
import javax.swing.JLabel
import javax.swing.BorderFactory
import javax.swing.ImageIcon
import br.com.correios.enderecador.exception.ConfiguracaoProxyException
import br.com.correios.enderecador.exception.ConnectionException
import br.com.correios.enderecador.util.*
import br.com.correios.enderecador.util.TypeDocumentFormat.NONE
import br.com.correios.enderecador.util.TypeDocumentFormat.ONLY_DIGITS
import net.miginfocom.swing.MigLayout
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import java.awt.*
import java.awt.Font.PLAIN
import java.awt.Font.SANS_SERIF

class SenderEditView : KoinComponent, JDialog {
    private val logger by Logging()

    private val remetenteDao: RemetenteDao = get()

    private val arrayUF = BrazilState.abbreviations()

    private var blnIncluir = false
    private var numeroRemetente = ""
    private val jbtnCapturaEndereco = JButton()
    private val jbtnNaoSeiCep = JButton()
    private val jcmbUfRemetente = JComboBox(arrayUF.toComboBox())
    private val jtxtApelidoRemetente = JTextField()
    private val jtxtBairroRemetente = JTextField()
    private val jtxtCaixaPostalRemetente = JTextField()
    private val jtxtCepCaixaPostalRemetente = JFormattedTextField()
    private val jtxtCepRemetente = JFormattedTextField()
    private val jtxtCidadeRemetente = JTextField()
    private val jtxtComplementoRemetente = JTextField()
    private val jtxtEmailRemetente = JTextField()
    private val jtxtEnderecoRemetente = JTextField()
    private val jtxtFaxRemetente = JTextField()
    private val jtxtNomeRemetente = JTextField()
    private val jtxtNumeroRemetente = JTextField()
    private val jtxtTelefoneRemetente = JTextField()

    constructor(parent: Frame? = null, modal: Boolean = true) : super(parent, modal) {
        initComponents()
        configuracoesAdicionais()
        jtxtCepRemetente.maskZipCode()
        jtxtCepCaixaPostalRemetente.maskZipCode()
    }

    constructor(remetenteBean: RemetenteBean, parent: Frame? = null, modal: Boolean = true) : super(parent, modal) {
        initComponents()
        configuracoesAdicionais()
        jtxtCepRemetente.maskZipCode()
        jtxtCepCaixaPostalRemetente.maskZipCode()
        blnIncluir = false
        numeroRemetente = remetenteBean.numeroRemetente
        jtxtApelidoRemetente.text = remetenteBean.apelido
        jtxtNomeRemetente.text = remetenteBean.nome
        jtxtCepRemetente.text = remetenteBean.cep
        jtxtEnderecoRemetente.text = remetenteBean.endereco
        jtxtNumeroRemetente.text = remetenteBean.numeroEndereco
        jtxtComplementoRemetente.text = remetenteBean.complemento
        jtxtBairroRemetente.text = remetenteBean.bairro
        jtxtCidadeRemetente.text = remetenteBean.cidade
        jcmbUfRemetente.selectedItem = remetenteBean.uf
        jtxtEmailRemetente.text = remetenteBean.email
        jtxtTelefoneRemetente.text = remetenteBean.telefone
        jtxtFaxRemetente.text = remetenteBean.fax
        jtxtCepCaixaPostalRemetente.text = remetenteBean.cepCaixaPostal
        jtxtCaixaPostalRemetente.text = remetenteBean.caixaPostal
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
            logger.error(ex.message, ex)
            return
        }
    }

    private fun limparCampos() {
        jtxtApelidoRemetente.text = ""
        jtxtNomeRemetente.text = ""
        jtxtCepRemetente.text = ""
        jtxtEnderecoRemetente.text = ""
        jtxtNumeroRemetente.text = ""
        jtxtComplementoRemetente.text = ""
        jtxtBairroRemetente.text = ""
        jtxtCidadeRemetente.text = ""
        jcmbUfRemetente.selectedIndex = 0
        jtxtEmailRemetente.text = ""
        jtxtTelefoneRemetente.text = ""
        jtxtFaxRemetente.text = ""
        jtxtCepCaixaPostalRemetente.text = ""
        jtxtCaixaPostalRemetente.text = ""
    }

    private fun gravaRemetente() {
        val remetenteBean = RemetenteBean(
            numeroRemetente = numeroRemetente,
            apelido = jtxtApelidoRemetente.text,
            titulo = "",
            nome = jtxtNomeRemetente.text,
            cep = jtxtCepRemetente.text.replace("[-_]".toRegex(), ""),
            endereco = jtxtEnderecoRemetente.text,
            numeroEndereco = jtxtNumeroRemetente.text,
            complemento = jtxtComplementoRemetente.text,
            bairro = jtxtBairroRemetente.text,
            cidade = jtxtCidadeRemetente.text,
            uf = jcmbUfRemetente.selectedItem as String,
            email = jtxtEmailRemetente.text,
            telefone = jtxtTelefoneRemetente.text,
            fax = jtxtFaxRemetente.text,
            cepCaixaPostal = jtxtCepCaixaPostalRemetente.text,
            caixaPostal = jtxtCaixaPostalRemetente.text)

        remetenteBean.validate().errors.firstOrNull()?.let { error ->
            JOptionPane.showMessageDialog(
                this,
                error.message,
                "Endereçador",
                JOptionPane.WARNING_MESSAGE)
            return
        }

        try {
            if (blnIncluir) {
                remetenteDao.incluirRemetente(remetenteBean)
            } else {
                remetenteDao.alterarRemetente(remetenteBean)
            }
            JOptionPane.showMessageDialog(
                null as Component?,
                "Dados Gravados com sucesso",
                "Enderecador",
                JOptionPane.INFORMATION_MESSAGE)
            limparCampos()

            if (!blnIncluir) {
                this.isVisible = false
            }
        } catch (ex: DaoException) {
            logger.error(ex.message, ex)
            JOptionPane.showMessageDialog(
                this,
                "Não foi possível gravar esse remetente, verifique se todos os dados foram preenchidos corretamente.",
                "Enderecador",
                JOptionPane.WARNING_MESSAGE)
        }
    }

    private fun initComponents() {
        title = "Selecionar remetente"
        size = Dimension(600, 500)
        isResizable = false
        defaultCloseOperation = DISPOSE_ON_CLOSE

        contentPane.add(JToolBar().apply {
            add(JButton().apply {
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@SenderEditView.javaClass.getResource("/imagens/OK.gif"))
                text = "Confirmar"
                horizontalTextPosition = 0
                maximumSize = Dimension(90, 60)
                verticalTextPosition = 3
                addActionListener { gravaRemetente() }
            })
            add(JButton().apply {
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@SenderEditView.javaClass.getResource("/imagens/cancelar.gif"))
                text = "Limpar tela"
                horizontalTextPosition = 0
                maximumSize = Dimension(90, 60)
                verticalTextPosition = 3
                addActionListener { limparCampos() }
            })
            add(JButton().apply {
                font = Font(SANS_SERIF, PLAIN, 9)
                icon = ImageIcon(this@SenderEditView.javaClass.getResource("/imagens/sair.gif"))
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
                text = "* Empresa/Nome (Linha 1):"
            })

            add(jtxtNomeRemetente.apply {
                document = PersonalizedDocument(42, NONE)
            }, "span, grow")

            add(JLabel().apply {
                font = Font(SANS_SERIF, PLAIN, 10)
                text = "Empresa/Nome (Linha 2):"
            })

            add(jtxtApelidoRemetente.apply {
                document = PersonalizedDocument(42, NONE)
            }, "span, grow")

            add(JLabel().apply {
                font = Font(SANS_SERIF, PLAIN, 10)
                text = "* CEP:"
            })

            add(jtxtCepRemetente.apply {
                minimumSize = Dimension(80, 26)
            })

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

            add(jtxtEnderecoRemetente.apply {
                document = PersonalizedDocument(33, NONE)
            }, "span, grow")


            add(JLabel().apply {
                font = Font(SANS_SERIF, PLAIN, 10)
                text = "* Número/Lote:"
            })

            add(jtxtNumeroRemetente.apply {
                document = PersonalizedDocument(8, ONLY_DIGITS)
            }, "span, grow")


            add(JLabel().apply {
                font = Font(SANS_SERIF, PLAIN, 10)
                text = "Complemento:"
            })

            add(jtxtComplementoRemetente.apply {
                document = PersonalizedDocument(35, NONE)
            }, "span, grow")

            add(JLabel().apply {
                font = Font(SANS_SERIF, PLAIN, 10)
                text = "Bairro:"
            })

            add(jtxtBairroRemetente.apply {
                document = PersonalizedDocument(42, NONE)
            }, "span, grow")

            add(JLabel().apply {
                font = Font(SANS_SERIF, PLAIN, 10)
                text = "* Cidade:"
            })

            add(jtxtCidadeRemetente.apply {
                document = PersonalizedDocument(30, NONE)
            }, "span 2, grow")

            add(JLabel().apply {
                font = Font(SANS_SERIF, PLAIN, 10)
                text = "* UF:"
            })

            add(jcmbUfRemetente, "wrap")

            add(JLabel().apply {
                font = Font(SANS_SERIF, PLAIN, 10)
                text = "E-mail:"
            })

            add(jtxtEmailRemetente, "span, grow")

            add(JLabel().apply {
                font = Font(SANS_SERIF, PLAIN, 10)
                text = "Telefone:"
            })

            add(jtxtTelefoneRemetente.apply {
                minimumSize = Dimension(115, 26)
                document = PersonalizedDocument(15, NONE)
            })

            add(JLabel().apply {
                font = Font(SANS_SERIF, PLAIN, 10)
                text = "Fax:"
            })

            add(jtxtFaxRemetente.apply {
                minimumSize = Dimension(115, 26)
            }, "wrap")

            add(JLabel().apply {
                font = Font(SANS_SERIF, PLAIN, 10)
                text = "Cx. Postal:"
            })

            add(jtxtCaixaPostalRemetente)

            add(JLabel().apply {
                font = Font(SANS_SERIF, PLAIN, 10)
                text = "CEP Caixa Postal:"
            })

            add(jtxtCepCaixaPostalRemetente)
        }, "Center")

        contentPane.add(JLabel().apply {
            text = " ATENÇÃO: Os campos marcados com  *  são campos obrigatórios."
            border = BorderFactory.createEtchedBorder()
        }, "South")

        val screenSize = Toolkit.getDefaultToolkit().screenSize
        setBounds((screenSize.width - 599) / 2, (screenSize.height - 434) / 2, 599, 434)
    }

    private fun jbtnCapturaEnderecoActionPerformed() {
        if (jtxtCepRemetente.text.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "O campo CEP deve ser preenchido!",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE)
            jtxtCepRemetente.requestFocus()
            return
        }
        try {
            ConfiguracaoBean.carregaVariaveis()
        } catch (ex: ConfiguracaoProxyException) {
            logger.error(ex.message, ex)
            JOptionPane.showMessageDialog(
                this,
                "Não foi possível buscar o CEP!\n" +
                        "Verifique se suas configuraçãoes de Proxy e " +
                        "Usuário de rede estão corretas no menu ferramentas.",
                "Endereçador",
                JOptionPane.WARNING_MESSAGE
            )
        } catch (e: ConnectionException) {
            JOptionPane.showMessageDialog(
                this,
                e.message,
                "Endereçador",
                JOptionPane.WARNING_MESSAGE)
        }
    }

    private fun jbtnNaoSeiCepActionPerformed() {
        try {
            openBrowseLink(ConfiguracaoBean.paginaPesquisaCep)
        } catch (ex: EnderecadorExcecao) {
            JOptionPane.showMessageDialog(
                this,
                "Não foi possível ativar seu browse, por favor consulte o CEP no seguinte site: " +
                        ConfiguracaoBean.paginaPesquisaCep,
                "Endereçador",
                JOptionPane.WARNING_MESSAGE)
        }
    }
}
