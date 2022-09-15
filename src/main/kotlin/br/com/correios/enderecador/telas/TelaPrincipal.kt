package br.com.correios.enderecador.telas

import javax.help.HelpSet
import br.com.correios.enderecador.bean.ConfiguracaoBean
import br.com.correios.enderecador.exception.EnderecadorExcecao
import javax.help.CSH
import javax.help.CSH.DisplayHelpFromSource
import com.formdev.flatlaf.extras.FlatSVGIcon
import br.com.correios.enderecador.conexao.ConexaoBD
import br.com.correios.enderecador.exception.ConnectException
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

    private val conexaoBD: ConexaoBD by inject()

    private val telaImpressaoDiretaEnvelope: TelaImpressaoDiretaEnvelope by inject()
    private val telaImpressaoEncomenda: TelaImpressaoEncomenda by inject()
    private val telaImpressaoEnvelope: TelaImpressaoEnvelope by inject()
    private val telaIncorporarDados: TelaIncorporarDados by inject()
    private val telaExportarDados: TelaExportarDados by inject()
    private val telaDestinatario: TelaDestinatario by inject()
    private val telaRemetente: TelaRemetente by inject()
    private val telaGrupo: TelaGrupo by inject()
    private val telaSobre: TelaSobre by inject()

    init {
        initComponents()
        configuracoesAdicionais()
    }

    private fun configuracaoAjuda(topics: JMenuItem) {
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
            add(JMenu("Cadastro").apply {
                mnemonic = 'C'.code
                add(JMenuItem("Cadastrar remetentes").apply {
                    mnemonic = 'R'.code
                    addActionListener { cadastrarRemetenteView() }
                })
                add(JMenuItem("Cadastrar destinatários").apply {
                    mnemonic = 'D'.code
                    addActionListener { cadastrarDestinatarioView() }
                })
                add(JMenuItem("Cadastrar grupos").apply {
                    mnemonic = 'G'.code
                    addActionListener {  jbtCadGrupoActionPerformed() }
                })
                add(JSeparator())
                add(JMenuItem("Sair").apply {
                    mnemonic = 'S'.code
                    addActionListener { sairAplicacao() }
                })
            })
            add(JMenu("Impressão").apply {
                mnemonic = 'P'.code
                add(JMenu("Etiquetas").apply {
                    mnemonic = 'Q'.code
                    add(JMenuItem("Encomendas").apply {
                        mnemonic = 'C'.code
                        addActionListener { jbtEtiquetasEncomendasActionPerformed() }
                    })
                    add(JMenuItem("Cartas").apply {
                        mnemonic = 'C'.code
                        addActionListener { jbtEtiquetasCartasActionPerformed() }
                    })
                })
                add(JMenuItem("Envelope").apply {
                    mnemonic = 'V'.code
                    addActionListener { jbtImpressaoEnvelopeActionPerformed() }
                })
            })
            add(JMenu("Ferramentas").apply {
                mnemonic = 'F'.code
                add(JMenuItem("Importar dados").apply {
                    mnemonic = 'I'.code
                    addActionListener { jbtIncorporarDadosActionPerformed() }
                })
                add(JMenuItem("Exportar dados").apply {
                    mnemonic = 'E'.code
                    addActionListener { jbtExportarDadosActionPerformed() }
                })
                add(JSeparator())
                add(JMenuItem("Configurar proxy").apply {
                    mnemonic = 'P'.code
                    addActionListener { mnProxyActionPerformed() }
                })
            })
            add(JMenu("Ajuda").apply {
                mnemonic = 'U'.code
                add(JMenuItem("Sobre").apply {
                    mnemonic = 'S'.code
                    addActionListener { sobreView() }
                })
                add(JMenuItem("Tópicos da ajuda").apply {
                    mnemonic = 'T'.code
                    addActionListener { configuracaoAjuda(this) }
                })
                add(JSeparator())
                add(JMenuItem("Atualizar versão").apply {
                    mnemonic = 'A'.code
                    addActionListener { atualizarVersaoView() }
                })
            })
        }

        contentPane.add(JDesktopPane().apply {
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
        //telaConfiguracao.isVisible = true
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
        try {
            title = "Endereçador Escritório v" + ConfiguracaoBean.versao
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
            conexaoBD.liberarConexao()
        } catch (ex: ConnectException) {
            logger.error(ex.message, ex)
        }
        exitProcess(0)
    }

    companion object {
        private val logger = Logger.getLogger(TelaPrincipal::class.java)
    }
}
