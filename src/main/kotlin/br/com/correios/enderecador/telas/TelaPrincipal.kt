package br.com.correios.enderecador.telas

import javax.swing.JFrame
import javax.swing.JSeparator
import javax.swing.JLabel
import javax.swing.JMenuItem
import javax.help.HelpBroker
import javax.help.HelpSet
import br.com.correios.enderecador.util.Geral
import br.com.correios.enderecador.bean.ConfiguracaoBean
import br.com.correios.enderecador.excecao.EnderecadorExcecao
import javax.help.CSH
import javax.help.CSH.DisplayHelpFromSource
import javax.help.HelpSetException
import javax.swing.JDesktopPane
import javax.swing.JToolBar
import javax.swing.JButton
import javax.swing.JMenuBar
import javax.swing.JMenu
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import com.formdev.flatlaf.extras.FlatSVGIcon
import javax.swing.BorderFactory
import javax.swing.ImageIcon
import java.awt.event.ActionEvent
import javax.swing.JOptionPane
import br.com.correios.enderecador.conexao.ConexaoBD
import br.com.correios.enderecador.conexao.ConnectException
import org.apache.log4j.Logger
import java.awt.Cursor
import java.awt.Dimension
import java.awt.Toolkit
import java.lang.Exception
import kotlin.system.exitProcess

class TelaPrincipal : JFrame() {
    private var jSeparator2: JSeparator? = null
    private var jSeparator3: JSeparator? = null
    private var lblBackground: JLabel? = null
    private var mnAtualizarVersao: JMenuItem? = null
    private var mnConfigUsuario: JMenuItem? = null
    private var mnProxy: JMenuItem? = null
    private var mnTopicosAjuda: JMenuItem? = null
    private var hb: HelpBroker? = null
    private var hs: HelpSet? = null

    init {
        initComponents()
        configuracoesAdicionais()
        configuracaoAjuda(mnTopicosAjuda)
    }

    private fun atualizarVersao() {
        try {
            Geral.displayURL(ConfiguracaoBean.instance!!.paginaEnderecador)
        } catch (ex: EnderecadorExcecao) {
            logger.error(ex.message, ex)
        }
    }

    private fun configuracaoAjuda(topics: JMenuItem?) {
        hs = getHelpSet("ajuda/helpset.hs")
        hs = getHelpSet("ajuda/helpset.hs")
        try {
            hb = hs!!.createHelpBroker()
            hb!!.enableHelpKey(getRootPane(), "apresentacao", hs)
            CSH.setHelpIDString(topics, "apresentacao")
            topics!!.addActionListener(DisplayHelpFromSource(hb))
        } catch (e: Exception) {
            print(e.message)
        }
    }

    fun getHelpSet(helpsetfile: String): HelpSet? {
        var hs: HelpSet? = null
        val cl = javaClass.classLoader
        val hsURL = HelpSet.findHelpSet(cl, helpsetfile)
        try {
            hs = HelpSet(null, hsURL)
        } catch (ex: HelpSetException) {
            logger.error("HelpSet: ${ex.message}")
            logger.error("HelpSet: $helpsetfile not found")
        }
        return hs
    }

    private fun initComponents() {
        val desktop = JDesktopPane()
        lblBackground = JLabel()
        val jToolBar1 = JToolBar()
        val jbtCadRemetente = JButton()
        val jbtCadDestinatario = JButton()
        val jbtCadGrupo = JButton()
        val jbtIncorporarDados = JButton()
        val jbtExportarDados = JButton()
        val jbtEtiquetasEncomendas = JButton()
        val jbtEtiquetasCartas = JButton()
        val jbtImpressaoEnvelope = JButton()
        val jbtSair = JButton()
        val jMenuBar1 = JMenuBar()
        val mnArquivo = JMenu()
        val mnCadRemetente = JMenuItem()
        val mnCadDestinatario = JMenuItem()
        val mnCadGrupo = JMenuItem()
        val jSeparator1 = JSeparator()
        val mnSair = JMenuItem()
        val mnImpressao = JMenu()
        val mnEtiquetas = JMenu()
        val mnEncomendas = JMenuItem()
        val mnCartas = JMenuItem()
        val mnEnvelope = JMenuItem()
        val mnFerramentas = JMenu()
        val mnImportar = JMenuItem()
        val mnExportar = JMenuItem()
        jSeparator2 = JSeparator()
        mnProxy = JMenuItem()
        mnConfigUsuario = JMenuItem()
        val mnAjuda = JMenu()
        val mnSobre = JMenuItem()
        mnTopicosAjuda = JMenuItem()
        jSeparator3 = JSeparator()
        mnAtualizarVersao = JMenuItem()
        defaultCloseOperation = EXIT_ON_CLOSE
        title = "Endereçador Escritório"
        addComponentListener(object : ComponentAdapter() {
            override fun componentResized(evt: ComponentEvent) {
                this_componentResized(evt)
            }
        })
        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(evt: WindowEvent) {
                formWindowClosing(evt)
            }
        })
        this.iconImage = FlatSVGIcon("images/logo.svg", 16, 16).image
        desktop.background = background
        desktop.border = BorderFactory.createEtchedBorder()
        lblBackground!!.icon = ImageIcon(javaClass.getResource("/imagens/logo_enderecador.gif"))
        desktop.add(lblBackground)
        lblBackground!!.setBounds(180, 100, 225, 100)
        contentPane.add(desktop, "Center")
        jbtCadRemetente.icon = ImageIcon(javaClass.getResource("/imagens/remetente.gif"))
        jbtCadRemetente.toolTipText = "Cadastrar remetentes"
        jbtCadRemetente.border = BorderFactory.createEtchedBorder()
        jbtCadRemetente.horizontalTextPosition = 0
        jbtCadRemetente.maximumSize = Dimension(60, 50)
        jbtCadRemetente.preferredSize = Dimension(60, 50)
        jbtCadRemetente.addActionListener { evt: ActionEvent -> jbtCadRemetenteActionPerformed(evt) }
        jToolBar1.add(jbtCadRemetente)
        jbtCadDestinatario.icon = ImageIcon(javaClass.getResource("/imagens/usuario.gif"))
        jbtCadDestinatario.toolTipText = "Cadastrar destinatários"
        jbtCadDestinatario.border = BorderFactory.createEtchedBorder()
        jbtCadDestinatario.maximumSize = Dimension(60, 50)
        jbtCadDestinatario.minimumSize = Dimension(31, 31)
        jbtCadDestinatario.addActionListener { evt: ActionEvent -> jbtCadDestinatarioActionPerformed(evt) }
        jToolBar1.add(jbtCadDestinatario)
        jbtCadGrupo.icon = ImageIcon(javaClass.getResource("/imagens/usuarios.gif"))
        jbtCadGrupo.toolTipText = "Cadastrar grupos"
        jbtCadGrupo.border = BorderFactory.createEtchedBorder()
        jbtCadGrupo.maximumSize = Dimension(60, 50)
        jbtCadGrupo.minimumSize = Dimension(45, 39)
        jbtCadGrupo.addActionListener { evt: ActionEvent -> jbtCadGrupoActionPerformed(evt) }
        jToolBar1.add(jbtCadGrupo)
        jbtIncorporarDados.icon = FlatSVGIcon("images/icon/database.svg", 0.5f)
        jbtIncorporarDados.toolTipText = "Importar dados"
        jbtIncorporarDados.border = BorderFactory.createEtchedBorder()
        jbtIncorporarDados.horizontalTextPosition = 0
        jbtIncorporarDados.maximumSize = Dimension(60, 50)
        jbtIncorporarDados.addActionListener { evt: ActionEvent -> jbtIncorporarDadosActionPerformed(evt) }
        jToolBar1.add(jbtIncorporarDados)
        jbtExportarDados.icon = ImageIcon(javaClass.getResource("/imagens/exportararquivo.gif"))
        jbtExportarDados.toolTipText = "Exportar dados"
        jbtExportarDados.border = BorderFactory.createEtchedBorder()
        jbtExportarDados.maximumSize = Dimension(60, 50)
        jbtExportarDados.addActionListener { evt: ActionEvent -> jbtExportarDadosActionPerformed(evt) }
        jToolBar1.add(jbtExportarDados)
        jbtEtiquetasEncomendas.icon = ImageIcon(javaClass.getResource("/imagens/etiquetaencomenda.gif"))
        jbtEtiquetasEncomendas.toolTipText = "Etiquetas para encomendas"
        jbtEtiquetasEncomendas.border = BorderFactory.createEtchedBorder()
        jbtEtiquetasEncomendas.maximumSize = Dimension(60, 50)
        jbtEtiquetasEncomendas.addActionListener { evt: ActionEvent -> jbtEtiquetasEncomendasActionPerformed(evt) }
        jToolBar1.add(jbtEtiquetasEncomendas)
        jbtEtiquetasCartas.icon = ImageIcon(javaClass.getResource("/imagens/etiquetaenvelope.gif"))
        jbtEtiquetasCartas.toolTipText = "Etiquetas para cartas"
        jbtEtiquetasCartas.border = BorderFactory.createEtchedBorder()
        jbtEtiquetasCartas.maximumSize = Dimension(60, 50)
        jbtEtiquetasCartas.addActionListener { evt: ActionEvent -> jbtEtiquetasCartasActionPerformed(evt) }
        jToolBar1.add(jbtEtiquetasCartas)
        jbtImpressaoEnvelope.icon = FlatSVGIcon("images/icon/envelope.svg", 0.5f)
        jbtImpressaoEnvelope.toolTipText = "Impressão direta em envelope"
        jbtImpressaoEnvelope.border = BorderFactory.createEtchedBorder()
        jbtImpressaoEnvelope.maximumSize = Dimension(60, 50)
        jbtImpressaoEnvelope.addActionListener { evt: ActionEvent -> jbtImpressaoEnvelopeActionPerformed(evt) }
        jToolBar1.add(jbtImpressaoEnvelope)
        jbtSair.icon = FlatSVGIcon("images/icon/exit.svg", 0.5f)
        jbtSair.toolTipText = "Sair do programa"
        jbtSair.border = BorderFactory.createEtchedBorder()
        jbtSair.maximumSize = Dimension(60, 50)
        jbtSair.minimumSize = Dimension(45, 39)
        jbtSair.addActionListener { evt: ActionEvent -> jbtSairActionPerformed(evt) }
        jToolBar1.add(jbtSair)
        contentPane.add(jToolBar1, "North")
        mnArquivo.setMnemonic('C')
        mnArquivo.text = "Cadastro"
        mnArquivo.name = ""
        mnArquivo.addActionListener { evt: ActionEvent -> mnArquivoActionPerformed(evt) }
        mnCadRemetente.setMnemonic('r')
        mnCadRemetente.text = "Cadastrar remetentes"
        mnCadRemetente.addActionListener { evt: ActionEvent -> mnCadRemetenteActionPerformed(evt) }
        mnArquivo.add(mnCadRemetente)
        mnCadDestinatario.setMnemonic('d')
        mnCadDestinatario.text = "Cadastrar destinatários"
        mnCadDestinatario.addActionListener { evt: ActionEvent -> mnCadDestinatarioActionPerformed(evt) }
        mnArquivo.add(mnCadDestinatario)
        mnCadGrupo.setMnemonic('g')
        mnCadGrupo.text = "Cadastrar grupos"
        mnCadGrupo.addActionListener { evt: ActionEvent -> mnCadGrupoActionPerformed(evt) }
        mnArquivo.add(mnCadGrupo)
        mnArquivo.add(jSeparator1)
        mnSair.setMnemonic('S')
        mnSair.text = "Sair"
        mnSair.addActionListener { evt: ActionEvent -> mnSairActionPerformed(evt) }
        mnArquivo.add(mnSair)
        jMenuBar1.add(mnArquivo)
        mnImpressao.setMnemonic('p')
        mnImpressao.text = "Impressão"
        mnImpressao.addActionListener { evt: ActionEvent -> mnImpressaoActionPerformed(evt) }
        mnEtiquetas.setMnemonic('E')
        mnEtiquetas.text = "Etiquetas"
        mnEncomendas.setMnemonic('E')
        mnEncomendas.text = "Encomendas"
        mnEncomendas.addActionListener { evt: ActionEvent -> mnEncomendasActionPerformed(evt) }
        mnEtiquetas.add(mnEncomendas)
        mnCartas.setMnemonic('C')
        mnCartas.text = "Cartas"
        mnCartas.addActionListener { evt: ActionEvent -> mnCartasActionPerformed(evt) }
        mnEtiquetas.add(mnCartas)
        mnImpressao.add(mnEtiquetas)
        mnEnvelope.setMnemonic('v')
        mnEnvelope.text = "Envelope"
        mnEnvelope.addActionListener { evt: ActionEvent -> mnEnvelopeActionPerformed(evt) }
        mnImpressao.add(mnEnvelope)
        jMenuBar1.add(mnImpressao)
        mnFerramentas.setMnemonic('F')
        mnFerramentas.text = "Ferramentas"
        mnImportar.setMnemonic('I')
        mnImportar.text = "Importar dados"
        mnImportar.addActionListener { evt: ActionEvent -> mnImportarActionPerformed(evt) }
        mnFerramentas.add(mnImportar)
        mnExportar.setMnemonic('E')
        mnExportar.text = "Exportar dados"
        mnExportar.addActionListener { evt: ActionEvent -> mnExportarActionPerformed(evt) }
        mnFerramentas.add(mnExportar)
        mnFerramentas.add(jSeparator2)
        mnProxy!!.setMnemonic('p')
        mnProxy!!.text = "Configurar proxy"
        mnProxy!!.addActionListener { evt: ActionEvent -> mnProxyActionPerformed(evt) }
        mnFerramentas.add(mnProxy)
        jMenuBar1.add(mnFerramentas)
        mnAjuda.setMnemonic('u')
        mnAjuda.text = "Ajuda"
        mnSobre.setMnemonic('S')
        mnSobre.text = "Sobre"
        mnSobre.addActionListener { evt: ActionEvent -> mnSobreActionPerformed(evt) }
        mnAjuda.add(mnSobre)
        mnTopicosAjuda!!.setMnemonic('T')
        mnTopicosAjuda!!.text = "Tópicos da ajuda"
        mnAjuda.add(mnTopicosAjuda)
        mnAjuda.add(jSeparator3)
        mnAtualizarVersao!!.setMnemonic('A')
        mnAtualizarVersao!!.text = "Atualizar versão"
        mnAtualizarVersao!!.addActionListener { evt: ActionEvent -> mnAtualizarVersaoActionPerformed(evt) }
        mnAjuda.add(mnAtualizarVersao)
        jMenuBar1.add(mnAjuda)
        jMenuBar = jMenuBar1
        pack()
    }

    private fun formWindowClosing(evt: WindowEvent) {
        sairAplicacao()
    }

    private fun mnSobreActionPerformed(evt: ActionEvent) {
        cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
        val telaSobre = TelaSobre(this, true)
        telaSobre.isVisible = true
        cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
    }

    private fun mnAtualizarVersaoActionPerformed(evt: ActionEvent) {
        cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
        atualizarVersao()
        cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
    }

    private fun mnEnvelopeActionPerformed(evt: ActionEvent) {
        jbtImpressaoEnvelopeActionPerformed(evt)
    }

    private fun mnCartasActionPerformed(evt: ActionEvent) {
        jbtEtiquetasCartasActionPerformed(evt)
    }

    private fun jbtEtiquetasCartasActionPerformed(evt: ActionEvent) {
        cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
        val telaImpressaoEnvelope = TelaImpressaoEnvelope.getInstance(this)
        telaImpressaoEnvelope!!.setLocationRelativeTo(null)
        telaImpressaoEnvelope.isVisible = true
        hb!!.enableHelpKey(telaImpressaoEnvelope, "impressaoCarta", hs)
        cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
    }

    private fun jbtImpressaoEnvelopeActionPerformed(evt: ActionEvent) {
        cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
        val telaImpressaoDiretaEnvelope = TelaImpressaoDiretaEnvelope.getInstance(this)
        hb!!.enableHelpKey(telaImpressaoDiretaEnvelope, "impressaoEnvelopes", hs)
        telaImpressaoDiretaEnvelope!!.setLocationRelativeTo(null)
        telaImpressaoDiretaEnvelope.isVisible = true
        cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
    }

    private fun mnEncomendasActionPerformed(evt: ActionEvent) {
        jbtEtiquetasEncomendasActionPerformed(evt)
    }

    private fun mnExportarActionPerformed(evt: ActionEvent) {
        jbtExportarDadosActionPerformed(evt)
    }

    private fun jbtIncorporarDadosActionPerformed(evt: ActionEvent) {
        cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
        val telaIncorporarDados = TelaIncorporarDados.instance
        telaIncorporarDados!!.isVisible = true
        telaIncorporarDados.setLocationRelativeTo(null)
        hb!!.enableHelpKey(telaIncorporarDados, "importar", hs)
        cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
    }

    private fun jbtEtiquetasEncomendasActionPerformed(evt: ActionEvent) {
        cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
        val telaImpressaoEncomenda = TelaImpressaoEncomenda.getInstance(this)
        telaImpressaoEncomenda!!.setLocationRelativeTo(null)
        telaImpressaoEncomenda.isVisible = true
        hb!!.enableHelpKey(telaImpressaoEncomenda, "impressaoEncomenda", hs)
        cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
    }

    private fun jbtExportarDadosActionPerformed(evt: ActionEvent) {
        cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
        val telaExportarDados = TelaExportarDados.getInstance(this)
        telaExportarDados!!.setLocationRelativeTo(null)
        telaExportarDados.isVisible = true
        hb!!.enableHelpKey(telaExportarDados, "exportar", hs)
        cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
    }

    private fun mnSairActionPerformed(evt: ActionEvent) {
        sairAplicacao()
    }

    private fun mnCadGrupoActionPerformed(evt: ActionEvent) {
        jbtCadGrupoActionPerformed(evt)
    }

    private fun mnCadDestinatarioActionPerformed(evt: ActionEvent) {
        jbtCadDestinatarioActionPerformed(evt)
    }

    private fun jbtCadGrupoActionPerformed(evt: ActionEvent) {
        cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
        val telaGrupo = TelaGrupo.getInstance(this)
        telaGrupo!!.setLocationRelativeTo(null)
        telaGrupo.isVisible = true
        hb!!.enableHelpKey(telaGrupo, "cadGrupos", hs)
        cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
    }

    private fun mnProxyActionPerformed(evt: ActionEvent) {
        cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
        val telaConfiguracao = TelaConfiguracao(this, true)
        telaConfiguracao.isVisible = true
        cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
    }

    private fun mnConfigUsuarioActionPerformed(evt: ActionEvent) {
        cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
        val telaUsuario = TelaUsuario(this, true)
        telaUsuario.isVisible = true
        cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
    }

    private fun jbtCadDestinatarioActionPerformed(evt: ActionEvent) {
        cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
        val telaDestinatario = TelaDestinatario.getInstance(this)
        hb!!.enableHelpKey(telaDestinatario, "cadDestinatarios", hs)
        telaDestinatario!!.setLocationRelativeTo(null)
        telaDestinatario.isVisible = true
        cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
    }

    private fun jbtCadRemetenteActionPerformed(evt: ActionEvent) {
        cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
        val telaRemetente = TelaRemetente.getInstance(this)
        telaRemetente!!.setLocationRelativeTo(null)
        telaRemetente.isVisible = true
        hb!!.enableHelpKey(telaRemetente, "cadRemetentes", hs)
        cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
    }

    private fun jbtSairActionPerformed(evt: ActionEvent) {
        sairAplicacao()
    }

    private fun mnImportarActionPerformed(evt: ActionEvent) {
        jbtIncorporarDadosActionPerformed(evt)
    }

    private fun this_componentResized(evt: ComponentEvent) {
        loadBackgroundImage()
    }

    private fun mnImpressaoActionPerformed(evt: ActionEvent) {}
    private fun mnArquivoActionPerformed(evt: ActionEvent) {}
    private fun mnCadRemetenteActionPerformed(evt: ActionEvent) {
        jbtCadRemetenteActionPerformed(evt)
    }

    private fun configuracoesAdicionais() {
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        setBounds(50, 50, screenSize.width - 100, screenSize.height - 100)
        val configuracaoBean = ConfiguracaoBean.instance
        try {
            configuracaoBean!!.carregaVariaveis()
            title = "Endereçador Escritório v" + configuracaoBean.versao
            if (configuracaoBean.banco == "GPBE") {
                mnAtualizarVersao!!.isVisible = false
                mnProxy!!.isVisible = false
                mnConfigUsuario!!.isVisible = false
                jSeparator2!!.isVisible = false
                jSeparator3!!.isVisible = false
            }
        } catch (ex: EnderecadorExcecao) {
            JOptionPane.showMessageDialog(
                this,
                "Não foi possível buscar configurações da aplicação.",
                "Endereçador ECT",
                2
            )
            logger.error(ex.message, ex)
        }
        if (configuracaoBean!!.banco == "DNEC" && configuracaoBean.chave.trim { it <= ' ' } == "") {
            val telaChaveRegistro = TelaChaveRegistro(this, true)
            telaChaveRegistro.isVisible = true
        }
    }

    private fun loadBackgroundImage() {
        val iconX = width / 2 - lblBackground!!.icon.iconWidth / 2
        val iconY = height / 2 - lblBackground!!.icon.iconHeight * 2
        val iconWidth = lblBackground!!.icon.iconWidth * 2
        val iconHeight = lblBackground!!.icon.iconHeight * 2
        lblBackground!!.setBounds(iconX, iconY, iconWidth, iconHeight)
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
