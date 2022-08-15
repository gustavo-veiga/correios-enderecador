package br.com.correios.enderecador.telas

import javax.help.HelpSet
import br.com.correios.enderecador.bean.ConfiguracaoBean
import br.com.correios.enderecador.excecao.EnderecadorExcecao
import javax.help.CSH
import javax.help.CSH.DisplayHelpFromSource
import com.formdev.flatlaf.extras.FlatSVGIcon
import br.com.correios.enderecador.conexao.ConexaoBD
import br.com.correios.enderecador.conexao.ConnectException
import org.apache.log4j.Logger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.awt.Cursor
import java.awt.Dimension
import java.awt.Toolkit
import java.lang.Exception
import javax.swing.*
import kotlin.system.exitProcess

class TelaPrincipal : JFrame(), KoinComponent {
    private val helpSet = helpSet()
    private val helpBroker = helpSet.createHelpBroker()

    private val telaSobre: TelaSobre by inject()
    private val telaGrupo: TelaGrupo by inject()
    private val telaRemetente: TelaRemetente by inject()
    private val telaConfiguracao: TelaConfiguracao by inject()
    private val telaDestinatario: TelaDestinatario by inject()
    private val telaExportarDados: TelaExportarDados by inject()
    private val telaIncorporarDados: TelaIncorporarDados by inject()
    private val telaImpressaoEnvelope: TelaImpressaoEnvelope by inject()
    private val telaImpressaoEncomenda: TelaImpressaoEncomenda by inject()
    private val telaImpressaoDiretaEnvelope: TelaImpressaoDiretaEnvelope by inject()

    init {
        initComponents()
        configuracoesAdicionais()
    }

    private fun configuracaoAjuda() {
        val topics = JMenuItem()
        topics.isVisible = true
        try {
            helpBroker.enableHelpKey(getRootPane(), "apresentacao", helpSet)
            CSH.setHelpIDString(JMenuItem(), "apresentacao")
            topics.addActionListener(DisplayHelpFromSource(helpBroker))
        } catch (e: Exception) {
            print(e.message)
        }
    }

    private fun helpSet(): HelpSet {
        val hsURL = HelpSet.findHelpSet(javaClass.classLoader, "ajuda/helpset.hs")
        return HelpSet(null, hsURL)
    }

    private fun initComponents() {
        defaultCloseOperation = EXIT_ON_CLOSE
        iconImage = FlatSVGIcon("images/logo.svg", 16, 16).image
        isVisible = true
        title = "Endereçador Escritório"
        state = 0


        jMenuBar = JMenuBar().apply {
            add(JMenu().apply {
                mnemonic = 'C'.code
                text = "Cadastro"
                add(JMenuItem().apply {
                    mnemonic = 'R'.code
                    text = "Cadastrar remetentes"
                    addActionListener { cadastrarRemetenteView() }
                })
                add(JMenuItem().apply {
                    mnemonic = 'D'.code
                    text = "Cadastrar destinatários"
                    addActionListener { cadastrarDestinatarioView() }
                })
                add(JMenuItem().apply {
                    mnemonic = 'G'.code
                    text = "Cadastrar grupos"
                    addActionListener {  jbtCadGrupoActionPerformed() }
                })
                add(JSeparator())
                add(JMenuItem().apply {
                    mnemonic = 'S'.code
                    text = "Sair"
                    addActionListener { sairAplicacao() }
                })
            })
            add(JMenu().apply {
                mnemonic = 'P'.code
                text = "Impressão"
                add(JMenu().apply {
                    mnemonic = 'Q'.code
                    text = "Etiquetas"
                    add(JMenuItem().apply {
                        mnemonic = 'C'.code
                        text = "Encomendas"
                        addActionListener { jbtEtiquetasEncomendasActionPerformed() }
                    })
                    add(JMenuItem().apply {
                        mnemonic = 'C'.code
                        text = "Cartas"
                        addActionListener { jbtEtiquetasCartasActionPerformed() }
                    })
                })
                add(JMenuItem().apply {
                    mnemonic = 'V'.code
                    text = "Envelope"
                    addActionListener { jbtImpressaoEnvelopeActionPerformed() }
                })
            })
            add(JMenu().apply {
                mnemonic = 'F'.code
                text = "Ferramentas"
                add(JMenuItem().apply {
                    mnemonic = 'I'.code
                    text = "Importar dados"
                    addActionListener { jbtIncorporarDadosActionPerformed() }
                })
                add(JMenuItem().apply {
                    mnemonic = 'E'.code
                    text = "Exportar dados"
                    addActionListener { jbtExportarDadosActionPerformed() }
                })
                add(JSeparator())
                add(JMenuItem().apply {
                    mnemonic = 'P'.code
                    text = "Configurar proxy"
                    addActionListener { mnProxyActionPerformed() }
                })
            })
            add(JMenu().apply {
                mnemonic = 'U'.code
                text = "Ajuda"
                add(JMenuItem().apply {
                    mnemonic = 'S'.code
                    text = "Sobre"
                    addActionListener { sobreView() }
                })
                add(JMenuItem().apply {
                    mnemonic = 'T'.code
                    text = "Tópicos da ajuda"
                    addActionListener { configuracaoAjuda() }
                })
                add(JSeparator())
                add(JMenuItem().apply {
                    mnemonic = 'A'.code
                    text = "Atualizar versão"
                    addActionListener { atualizarVersaoView() }
                })
            })
        }

        contentPane.add(JDesktopPane().apply {
            background = background
            border = BorderFactory.createEtchedBorder()
            add(JLabel().apply {
                icon = ImageIcon(this@TelaPrincipal.javaClass.getResource("/imagens/logo_enderecador.gif"))
                setBounds(180, 100, 225, 100)
            })
        }, "North")

        contentPane.add(JToolBar().apply {
            add(JButton().apply {
                icon = ImageIcon(this@TelaPrincipal.javaClass.getResource("/imagens/remetente.gif"))
                toolTipText = "Cadastrar remetentes"
                horizontalTextPosition = 0
                maximumSize = Dimension(60, 50)
                preferredSize = Dimension(60, 50)
                addActionListener { cadastrarRemetenteView() }
            })
            add(JButton().apply {
                icon = ImageIcon(this@TelaPrincipal.javaClass.getResource("/imagens/usuario.gif"))
                toolTipText = "Cadastrar destinatários"
                maximumSize = Dimension(60, 50)
                minimumSize = Dimension(31, 31)
                addActionListener { cadastrarDestinatarioView() }
            })
            add(JButton().apply {
                icon = ImageIcon(this@TelaPrincipal.javaClass.getResource("/imagens/usuarios.gif"))
                toolTipText = "Cadastrar grupos"
                maximumSize = Dimension(60, 50)
                minimumSize = Dimension(45, 39)
                addActionListener { jbtCadGrupoActionPerformed() }
            })
            add(JButton().apply {
                icon = FlatSVGIcon("images/icon/database.svg", 0.5f)
                toolTipText = "Importar dados"
                horizontalTextPosition = 0
                maximumSize = Dimension(60, 50)
                addActionListener { jbtIncorporarDadosActionPerformed() }
            })
            add(JButton().apply {
                icon = ImageIcon(this@TelaPrincipal.javaClass.getResource("/imagens/exportararquivo.gif"))
                toolTipText = "Exportar dados"
                maximumSize = Dimension(60, 50)
                addActionListener { jbtExportarDadosActionPerformed() }
            })
            add(JButton().apply {
                icon = ImageIcon(this@TelaPrincipal.javaClass.getResource("/imagens/etiquetaencomenda.gif"))
                toolTipText = "Etiquetas para encomendas"
                maximumSize = Dimension(60, 50)
                addActionListener { jbtEtiquetasEncomendasActionPerformed() }
            })
            add(JButton().apply {
                icon = ImageIcon(this@TelaPrincipal.javaClass.getResource("/imagens/etiquetaenvelope.gif"))
                toolTipText = "Etiquetas para cartas"
                maximumSize = Dimension(60, 50)
                addActionListener { jbtEtiquetasCartasActionPerformed() }
            })
            add(JButton().apply {
                icon = FlatSVGIcon("images/icon/envelope.svg", 0.5f)
                toolTipText = "Impressão direta em envelope"
                maximumSize = Dimension(60, 50)
                addActionListener { jbtImpressaoEnvelopeActionPerformed() }
            })
            add(JButton().apply {
                icon = FlatSVGIcon("images/icon/exit.svg", 0.5f)
                toolTipText = "Sair do programa"
                maximumSize = Dimension(60, 50)
                minimumSize = Dimension(45, 39)
                addActionListener { sairAplicacao() }
            })
        }, "North")

        pack()
    }

    private fun sobreView() {
        cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
        telaSobre.isVisible = true
        cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
    }

    private fun atualizarVersaoView() {
        cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
        cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
    }

    private fun jbtEtiquetasCartasActionPerformed() {
        cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
        telaImpressaoEnvelope.isVisible = true
        helpBroker.enableHelpKey(telaImpressaoEnvelope, "impressaoCarta", helpSet)
        cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
    }

    private fun jbtImpressaoEnvelopeActionPerformed() {
        cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
        telaImpressaoDiretaEnvelope.isVisible = true
        helpBroker!!.enableHelpKey(telaImpressaoDiretaEnvelope, "impressaoEnvelopes", helpSet)
        cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
    }

    private fun jbtIncorporarDadosActionPerformed() {
        cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
        telaIncorporarDados.isVisible = true
        helpBroker.enableHelpKey(telaIncorporarDados, "importar", helpSet)
        cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
    }

    private fun jbtEtiquetasEncomendasActionPerformed() {
        cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
        telaImpressaoEncomenda.isVisible = true
        helpBroker.enableHelpKey(telaImpressaoEncomenda, "impressaoEncomenda", helpSet)
        cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
    }

    private fun jbtExportarDadosActionPerformed() {
        cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
        telaExportarDados.isVisible = true
        helpBroker.enableHelpKey(telaExportarDados, "exportar", helpSet)
        cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
    }

    private fun jbtCadGrupoActionPerformed() {
        cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
        telaGrupo.isVisible = true
        helpBroker.enableHelpKey(telaGrupo, "cadGrupos", helpSet)
        cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
    }

    private fun mnProxyActionPerformed() {
        cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
        telaConfiguracao.isVisible = true
        cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
    }

    private fun cadastrarDestinatarioView() {
        cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
        telaDestinatario.isVisible = true
        helpBroker.enableHelpKey(telaDestinatario, "cadDestinatarios", helpSet)
        cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
    }

    private fun cadastrarRemetenteView() {
        cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
        telaRemetente.isVisible = true
        helpBroker.enableHelpKey(telaRemetente, "cadRemetentes", helpSet)
        cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
    }

    private fun configuracoesAdicionais() {
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        setBounds(50, 50, screenSize.width - 100, screenSize.height - 100)
        val configuracaoBean = ConfiguracaoBean.instance
        try {
            configuracaoBean!!.carregaVariaveis()
            title = "Endereçador Escritório v" + configuracaoBean.versao
        } catch (ex: EnderecadorExcecao) {
            JOptionPane.showMessageDialog(
                this,
                "Não foi possível buscar configurações da aplicação.",
                "Endereçador ECT",
                2
            )
            logger.error(ex.message, ex)
        }
    }

    private fun sairAplicacao() {
        try {
            ConexaoBD.instance!!.liberarConexao()
        } catch (ex: ConnectException) {
            logger.error(ex.message, ex)
        }
        exitProcess(0)
    }

    companion object {
        private val logger = Logger.getLogger(TelaPrincipal::class.java)
    }
}
