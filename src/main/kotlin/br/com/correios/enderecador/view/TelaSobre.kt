package br.com.correios.enderecador.view

import javax.swing.JDialog
import br.com.correios.enderecador.bean.ConfiguracaoBean
import br.com.correios.enderecador.bean.SystemProperties as Properties
import javax.swing.JTabbedPane
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.ImageIcon
import java.awt.event.MouseAdapter
import br.com.correios.enderecador.util.Geral
import br.com.correios.enderecador.exception.EnderecadorExcecao
import net.miginfocom.swing.MigLayout
import org.apache.log4j.Logger
import org.koin.core.annotation.Singleton
import java.awt.*
import java.awt.Font.*
import java.awt.event.MouseEvent

@Singleton
class TelaSobre: JDialog() {
    init {
        title = "Endereçador Escritório"
        size = Dimension(390, 400)
        isResizable = true
        defaultCloseOperation = DISPOSE_ON_CLOSE

        contentPane.add(JTabbedPane().apply {
            addTab("Sobre", JPanel().apply {
                layout = MigLayout()

                add(JLabel().apply {
                    icon = ImageIcon(this@TelaSobre.javaClass.getResource("/imagens/logo_enderecador.gif"))
                    horizontalAlignment = 0
                    horizontalTextPosition = 0
                }, "span, grow, push")

                add(JLabel().apply {
                    font = Font(SANS_SERIF, BOLD, 13)
                    horizontalAlignment = 0
                    horizontalTextPosition = 0
                    text = "Empresa Brasileira de Correios e Telégrafos"
                }, "span, grow, push")

                add(JLabel().apply {
                    font = Font(SANS_SERIF, PLAIN, 13)
                    text = "Endereçador Escritório"
                })

                add(JLabel().apply {
                    font = Font(SANS_SERIF, 1, 14)
                    text = "Versão " + ConfiguracaoBean.versao
                }, "wrap")

                add(JLabel().apply {
                    font = Font(SERIF, BOLD, 11)
                    text = "Sugestões e aperfeiçoamentos: "
                }, "wrap")

                add(JLabel().apply {
                    font = Font(SERIF, BOLD, 11)
                    text = "Fale com os Correio"
                    foreground = Color.BLUE
                    addMouseListener(object : MouseAdapter() {
                        override fun mouseClicked(evt: MouseEvent) {
                            openLink()
                        }

                        override fun mouseEntered(evt: MouseEvent) {
                            mouseHandCursor()
                        }

                        override fun mouseExited(evt: MouseEvent) {
                            mouseDefaultCursor()
                        }
                    })
                }, "wrap")

                add(JLabel().apply {
                    font = Font(SANS_SERIF, PLAIN, 10)
                    text = "Todos os direitos reservados, 2007"
                })
            })

            addTab("Info. Sistema", JPanel().apply {
                layout = MigLayout("wrap 2")

                add(JLabel().apply {
                    font = Font(SERIF, BOLD, 11)
                    text = "Diretório instalação:"
                })
                add(JLabel().apply {
                    font = Font(SERIF, PLAIN, 10)
                    text = Properties.user.dir
                })

                add(JLabel().apply {
                    font = Font(SERIF, BOLD, 11)
                    text = "Java:"
                })
                add(JLabel().apply {
                    font = Font(SERIF, PLAIN, 10)
                    text = Properties.java.version
                })

                add(JLabel().apply {
                    font = Font(SERIF, BOLD, 11)
                    text = "Java Home:"
                })
                add(JLabel().apply {
                    font = Font(SERIF, PLAIN, 10)
                    text = Properties.java.home
                })

                add(JLabel().apply {
                    font = Font(SERIF, BOLD, 11)
                    text = "Sistema Operacional:"
                })
                add(JLabel().apply {
                    font = Font(SERIF, PLAIN, 10)
                    text = "${Properties.os.name} versão ${Properties.os.version} (${Properties.os.arch})"
                })

                add(JLabel().apply {
                    font = Font(SERIF, BOLD, 11)
                    text = "VM:"
                })
                add(JLabel().apply {
                    font = Font(SERIF, PLAIN, 10)
                    text = "${Properties.java.name} versão ${Properties.java.runtimeVersion}"
                })
            })
        }, "Center")
    }

    private fun openLink() {
        try {
            Geral.displayURL(ConfiguracaoBean.paginaFaleConosco)
        } catch (ex: EnderecadorExcecao) {
            logger.error(ex.message, ex)
            JOptionPane.showMessageDialog(
                this,
                "Não foi possível ativar seu browse, por favor entre no site: " +
                        ConfiguracaoBean.paginaFaleConosco,
                "Endereçador",
                JOptionPane.ERROR_MESSAGE
            )
        }
    }

    private fun mouseDefaultCursor() {
        cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
    }

    private fun mouseHandCursor() {
        cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
    }

    companion object {
        private val logger = Logger.getLogger(TelaSobre::class.java)
    }
}
