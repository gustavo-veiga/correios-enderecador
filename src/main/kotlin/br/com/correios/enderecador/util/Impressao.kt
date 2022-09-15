package br.com.correios.enderecador.util

import br.com.correios.enderecador.bean.*
import kotlin.Throws
import br.com.correios.enderecador.exception.EnderecadorExcecao
import net.sf.jasperreports.engine.JRException
import net.sf.jasperreports.engine.JasperFillManager
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
import net.sf.jasperreports.engine.JRDataSource
import net.sf.jasperreports.view.JasperViewer
import net.sf.jasperreports.engine.xml.JRXmlLoader
import net.sf.jasperreports.engine.JasperCompileManager
import org.krysalis.barcode4j.impl.datamatrix.DataMatrixBean
import org.krysalis.barcode4j.BarcodeDimension
import org.krysalis.barcode4j.impl.datamatrix.SymbolShapeHint
import java.io.FileOutputStream
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider
import org.apache.log4j.Logger
import org.koin.core.annotation.Singleton
import java.io.File
import java.io.OutputStream
import java.lang.Exception
import java.text.NumberFormat
import java.util.*

@Singleton
class Impressao {
    private val TAMANHO_CAMPO_ENTREGA_VIZINHO = 40
    private lateinit var destinatarioBean: DestinatarioBean
    private lateinit var impressaoBean: ImpressaoBean
    private val codBarras = CodigoDeBarras128()

    private fun calculaDVCep(cep: String): String {
        var dv = 0
        var somatorio = 0
        var i = 0
        while (i < cep!!.length) {
            somatorio += cep.substring(i, i + 1).toInt()
            i++
        }
        if (somatorio % 10 == 0) return "0"
        i = 0
        while (i < 10) {
            if ((somatorio + i) % 10 == 0) {
                dv = i
                break
            }
            i++
        }
        return dv.toString()
    }

    private fun pontoEntrega(numeroLote: String): String {
        val pontoEntregaDefault = "00000"
        if (numeroLote!!.isBlank() || numeroLote.trim().length > 5 || !numeroLote.matches("^[0-9]{1,5}$".toRegex())) {
            return pontoEntregaDefault
        }
        return if (pontoEntregaDefault.length == numeroLote.length) {
            numeroLote
        } else {
            pontoEntregaDefault.substring(0, pontoEntregaDefault.length - numeroLote.length) + numeroLote
        }
    }

    @Throws(EnderecadorExcecao::class)
    fun impressaoEncomenda(
        relatorio: String,
        remetente: RemetenteBean,
        destinatario: List<DestinatarioBean>,
        posInicial: Int,
    ) {
        val impressao = mutableListOf<ImpressaoBean>()
        val parametros = mutableMapOf<String, Any>()
        for (k in 1 until posInicial) {
            impressaoBean = ImpressaoBean()
            impressao.add(impressaoBean)
        }
        for (bean in destinatario) {
            impressaoBean = ImpressaoBean()
            destinatarioBean = bean
            if (destinatarioBean.quantidade == "") destinatarioBean.quantidade = "0"
            for (j in 0 until destinatarioBean.quantidade.toInt()) {
                impressaoBean.setDes_campo1(destinatarioBean.nome)
                impressaoBean.setDes_campo2(destinatarioBean.apelido)
                if (destinatarioBean.endereco.length < 24) {
                    impressaoBean.setDes_campo3(destinatarioBean.endereco + " " + destinatarioBean.numeroEndereco)
                    impressaoBean.setDes_campo4(destinatarioBean.complemento)
                } else {
                    impressaoBean.setDes_campo3(destinatarioBean.endereco)
                    impressaoBean.setDes_campo4(destinatarioBean.numeroEndereco + ", " + destinatarioBean.complemento)
                }
                impressaoBean.setDes_campo5(destinatarioBean.bairro)
                impressaoBean.setDes_campo6(destinatarioBean.cep.substring(0, 5) + "-" + destinatarioBean.cep.substring(5, 8))
                impressaoBean.setDes_campo7(destinatarioBean.cidade + "-" + destinatarioBean.uf)
                var entrega = destinatarioBean.desEntregaVizinho
                if (entrega.length > TAMANHO_CAMPO_ENTREGA_VIZINHO) entrega = entrega.substring(0, TAMANHO_CAMPO_ENTREGA_VIZINHO)
                if (entrega.isEmpty()) entrega = "entrega no vizinho não autorizada"
                impressaoBean.des_entrega = entrega
                val barCode =
                    codBarras.gerarCodigoDeBarra128(2, codBarras.gerarCodigoDeBarra(2, destinatarioBean.cep)).replace(",".toRegex(), "")
                impressaoBean.des_codBarras = barCode
                impressaoBean.des_cep = destinatarioBean.cep.substring(0, 5) + "-" + destinatarioBean.cep.substring(5, 8)
                var cepRemetente = "0000000000000"
                if (remetente != null) {
                    impressaoBean.setRem_campo1(remetente.titulo + " " + remetente.nome)
                    impressaoBean.setRem_campo2(remetente.apelido)
                    impressaoBean.setRem_campo3(remetente.endereco + " " + remetente.numeroEndereco)
                    impressaoBean.setRem_campo4(remetente.complemento)
                    impressaoBean.setRem_campo5(remetente.bairro)
                    impressaoBean.setRem_campo6(remetente.cep.substring(0, 5) + "-" + remetente.cep.substring(5, 8))
                    impressaoBean.setRem_campo7(remetente.cidade + "-" + remetente.uf)
                    cepRemetente = remetente.cep + pontoEntrega(remetente.numeroEndereco)
                }
                impressaoBean.codigoDoisD = destinatarioBean.cep + pontoEntrega(destinatarioBean.numeroEndereco)
                impressaoBean.codigoDoisD = impressaoBean.codigoDoisD + cepRemetente
                impressaoBean.codigoDoisD = impressaoBean.codigoDoisD + calculaDVCep(destinatarioBean.cep)
                impressaoBean.codigoDoisD = impressaoBean.codigoDoisD + "00"
                impressaoBean.codigoDoisD = impressaoBean.codigoDoisD + "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                createDatamatrix(impressaoBean.codigoDoisD!!, ConfiguracaoBean.caminhoImagem2d.toString() + "\\" + impressaoBean.codigoDoisD + ".png")
                impressao.add(impressaoBean)
            }
        }
        val path = File("").absolutePath
        parametros["caminhoImagem"] = Objects.requireNonNull(javaClass.getResource(ConfiguracaoBean.caminhoImagem)).toString()
        parametros["caminhoImagem2D"] = path
        try {
            visualizarRelatorioJasper(ConfiguracaoBean.caminhoRelatorio + relatorio, parametros, impressao)
            limparDataMatrix(impressao)
        } catch (ex: JRException) {
            logger.error(ex.message)
            throw EnderecadorExcecao(ex.message)
        }
    }

    @Throws(EnderecadorExcecao::class)
    fun impressaoCarta(
        relatorio: String,
        remetente: RemetenteBean?,
        destinatario: List<DestinatarioBean>?,
        qtdRemetente: Int,
        posInicial: Int,
        telRemetente: Boolean,
        telDestinatario: Boolean,
        imprimirTratamento: Boolean,
        tamanhoFonte: String
    ) {
        val impressao = mutableListOf<ImpressaoBean>()
        val parametros = mutableMapOf<String, Any>()
        for (k in 1 until posInicial) {
            impressaoBean = ImpressaoBean()
            impressao.add(impressaoBean)
        }
        var cepRemetenteComplemento = "0000000000000"
        if (remetente != null) {
            for (i in 0 until qtdRemetente) {
                impressaoBean = ImpressaoBean()
                if (imprimirTratamento) impressaoBean.setDes_campo1(remetente.titulo)
                impressaoBean.setDes_campo2(remetente.nome)
                impressaoBean.setDes_campo3(remetente.apelido)
                impressaoBean.setDes_campo4(remetente.endereco + " " + remetente.numeroEndereco)
                if (telDestinatario) {
                    impressaoBean.setDes_campo5(remetente.complemento + " - Telefone: " + remetente.telefone)
                } else {
                    impressaoBean.setDes_campo5(remetente.complemento)
                }
                impressaoBean.setDes_campo6(remetente.bairro)
                impressaoBean.setDes_campo7(remetente.cep.substring(0, 5) + "-" + remetente.cep.substring(5, 8) + "   " + remetente.cidade + "-" + remetente.uf)
                cepRemetenteComplemento = remetente.cep + pontoEntrega(remetente.numeroEndereco)
                impressaoBean.organizaCampos()
                impressao.add(impressaoBean)
            }
        }
        var cepDestinatarioComplemento = "0000000000000"
        if (destinatario != null) {
            for (bean in destinatario) {
                destinatarioBean = bean
                for (l in 0 until destinatarioBean.quantidade.toInt()) {
                    impressaoBean = ImpressaoBean()
                    if (imprimirTratamento) impressaoBean.setDes_campo1(destinatarioBean.titulo)
                    impressaoBean.setDes_campo2(destinatarioBean.nome)
                    impressaoBean.setDes_campo3(destinatarioBean.apelido)
                    impressaoBean.setDes_campo4(destinatarioBean.endereco + " " + destinatarioBean.numeroEndereco)
                    if (telDestinatario) {
                        impressaoBean.setDes_campo5(destinatarioBean.complemento + " - Telefone: " + destinatarioBean.telefone)
                    } else {
                        impressaoBean.setDes_campo5(destinatarioBean.complemento)
                    }
                    impressaoBean.setDes_campo6(destinatarioBean.bairro)
                    impressaoBean.setDes_campo7(destinatarioBean.cep.substring(0, 5) + "-" + destinatarioBean.cep.substring(5, 8) + "   " + destinatarioBean.cidade + "-" + destinatarioBean.uf)
                    cepDestinatarioComplemento = destinatarioBean.cep + pontoEntrega(destinatarioBean.numeroEndereco)
                    impressaoBean.des_cep = destinatarioBean.cep + calculaDVCep(destinatarioBean.cep)
                    impressaoBean.organizaCampos()
                    impressao.add(impressaoBean)
                }
            }
        }
        impressaoBean.codigoDoisD = cepDestinatarioComplemento
        impressaoBean.codigoDoisD = impressaoBean.codigoDoisD + cepRemetenteComplemento
        impressaoBean.codigoDoisD = impressaoBean.codigoDoisD + calculaDVCep(destinatarioBean.cep)
        impressaoBean.codigoDoisD = impressaoBean.codigoDoisD + "0".repeat(99)
        createDatamatrix(impressaoBean.codigoDoisD!!, ConfiguracaoBean.caminhoImagem2d.toString() + "\\" + impressaoBean.codigoDoisD + ".png")
        parametros["caminhoImagem"] = javaClass.getResource(ConfiguracaoBean.caminhoImagem)!!.toString()
        parametros["tamanhoFonte"] = tamanhoFonte
        parametros["caminhoImagem2D"] = ConfiguracaoBean.caminhoImagem2d
        try {
            visualizarRelatorioJasper(ConfiguracaoBean.caminhoRelatorio + relatorio, parametros, impressao)
            limparDataMatrix(impressao)
        } catch (ex: JRException) {
            logger.error(ex.message, ex as Throwable)
            throw EnderecadorExcecao(ex.message)
        }
    }

    @Throws(JRException::class)
    fun visualizarRelatorioJasper(relatorio: String, parametros: Map<String, Any>, lista: List<*>) {
        val stream = javaClass.classLoader.getResourceAsStream(relatorio)
        val impressao = JasperFillManager.fillReport(stream, parametros, JRBeanCollectionDataSource(lista) as JRDataSource)
        val viewer = JasperViewer(impressao, false)
        viewer.isVisible = true
    }

    @Throws(JRException::class)
    fun visualizarRelatorioJrxml(relatorio: String, parametros: Map<String, Any>, lista: List<*>) {
        val stream = javaClass.classLoader.getResourceAsStream(relatorio.substringBeforeLast(".") + ".jrxml")
        val desenho = JRXmlLoader.load(stream)
        val report = JasperCompileManager.compileReport(desenho)
        val impressao = JasperFillManager.fillReport(report, parametros, JRBeanCollectionDataSource(lista) as JRDataSource)
        val viewer = JasperViewer(impressao, false)
        viewer.isVisible = true
    }

    @Throws(EnderecadorExcecao::class)
    fun imprimirEnvelope(
        relatorio: String,
        remetente: RemetenteBean?,
        destinatario: List<DestinatarioBean>,
        telefone: Boolean,
        tamanhoFonte: FontSize
    ) {
        val print = mutableListOf<ImpressaoBean>()
        val parametros = mutableMapOf<String, Any>()
        for (destinatarioBean in destinatario) {
            var cepDestinatarioComplemento: String
            for (j in 0 until destinatarioBean.quantidade.toInt()) {
                impressaoBean = ImpressaoBean()
                impressaoBean.setDes_campo1(destinatarioBean.nome.uppercase())
                impressaoBean.setDes_campo2(destinatarioBean.apelido.uppercase())
                impressaoBean.setDes_campo3(destinatarioBean.endereco.uppercase() + " " + destinatarioBean.numeroEndereco)
                impressaoBean.setDes_campo4(destinatarioBean.complemento.uppercase())
                impressaoBean.setDes_campo5(destinatarioBean.bairro.uppercase())
                impressaoBean.setDes_campo6(destinatarioBean.cep.substring(0, 5) + "-" + destinatarioBean.cep.substring(5, 8) + "   " + destinatarioBean.cidade + "-" + destinatarioBean.uf)
                if (telefone) {
                    impressaoBean.setDes_campo7(destinatarioBean.telefone.uppercase())
                }
                impressaoBean.des_cep = destinatarioBean.cep + calculaDVCep(destinatarioBean.cep)
                cepDestinatarioComplemento = destinatarioBean.cep + pontoEntrega(destinatarioBean.numeroEndereco)
                var cepRemetenteComplemento = "0000000000000"
                if (remetente != null) {
                    impressaoBean.setRem_campo1(remetente.titulo)
                    impressaoBean.setRem_campo2(remetente.nome)
                    impressaoBean.setRem_campo3(remetente.apelido)
                    impressaoBean.setRem_campo4(remetente.endereco + " " + remetente.numeroEndereco)
                    impressaoBean.setRem_campo5(remetente.complemento)
                    impressaoBean.setRem_campo6(remetente.bairro)
                    impressaoBean.setRem_campo7(remetente.cep.substring(0, 5) + "-" + remetente.cep.substring(5, 8) + "   " + remetente.cidade + "-" + remetente.uf)
                    cepRemetenteComplemento = remetente.cep + pontoEntrega(remetente.numeroEndereco)
                }
                impressaoBean.codigoDoisD = cepDestinatarioComplemento
                impressaoBean.codigoDoisD = impressaoBean.codigoDoisD + cepRemetenteComplemento
                impressaoBean.codigoDoisD = impressaoBean.codigoDoisD + calculaDVCep(destinatarioBean.cep)
                impressaoBean.codigoDoisD = impressaoBean.codigoDoisD + "00"
                impressaoBean.codigoDoisD = impressaoBean.codigoDoisD + "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                createDatamatrix(impressaoBean.codigoDoisD!!, ConfiguracaoBean.caminhoImagem2d.toString() + "\\" + impressaoBean.codigoDoisD + ".png")
                impressaoBean.organizaCampos()
                print.add(impressaoBean)
            }
        }
        parametros["tamanhoFonte"] = tamanhoFonte.description.substring(0, 1)
        parametros["caminhoImagem"] = javaClass.getResource(ConfiguracaoBean.caminhoImagem)!!.toString()
        parametros["caminhoImagem2D"] = ConfiguracaoBean.caminhoImagem2d
        try {
            visualizarRelatorioJrxml(ConfiguracaoBean.caminhoRelatorio + relatorio, parametros, print)
            limparDataMatrix(print)
        } catch (ex: JRException) {
            logger.error(ex.message, ex as Throwable)
            throw EnderecadorExcecao(ex.message)
        }
    }

    fun imprimirAR(relatorio: String, rementente: RemetenteBean, destinatario: List<DestinatarioBean>) {
        val impressao = mutableListOf<ImpressaoBean>()
        val parametros = mutableMapOf<String, Any>()
        for (bean in destinatario) {
            impressaoBean = ImpressaoBean()
            destinatarioBean = bean
            if (destinatarioBean.quantidade.isEmpty()) {
                destinatarioBean.quantidade = "0"
            }
            for (j in 0 until destinatarioBean.quantidade.toInt()) {
                impressaoBean.setDes_campo1(destinatarioBean.nome)
                impressaoBean.setDes_campo2(destinatarioBean.apelido)
                impressaoBean.setDes_campo3(destinatarioBean.endereco + " " + destinatarioBean.numeroEndereco)
                impressaoBean.setDes_campo4(destinatarioBean.complemento + " " + destinatarioBean.bairro)
                impressaoBean.setDes_campo5(destinatarioBean.cep.substring(0, 5) + "-" + destinatarioBean.cep.substring(5, 8) + "   " + destinatarioBean.cidade + "-" + destinatarioBean.uf)
                impressaoBean.desMaoPropria = destinatarioBean.maoPropria
                impressaoBean.desConteudo = destinatarioBean.desConteudo
                impressaoBean.setRem_campo1(rementente.nome)
                impressaoBean.setRem_campo2(rementente.apelido)
                impressaoBean.setRem_campo3(rementente.endereco + " " + rementente.numeroEndereco)
                impressaoBean.setRem_campo4(rementente.complemento + " " + rementente.bairro)
                impressaoBean.setRem_campo5(rementente.cep.substring(0, 5) + "-" + rementente.cep.substring(5, 8) + "   " + rementente.cidade + "-" + rementente.uf)
                impressaoBean.organizaCampos()
                impressao.add(impressaoBean)
            }
        }
        parametros["caminhoImagem"] = javaClass.getResource(ConfiguracaoBean.caminhoImagem)!!.toString()
        parametros["caminhoImagem2D"] = ConfiguracaoBean.caminhoImagem2d
        try {
            visualizarRelatorioJasper(ConfiguracaoBean.caminhoRelatorio + relatorio, parametros, impressao)
        } catch (ex: JRException) {
            logger.error(ex.message, ex)
            throw EnderecadorExcecao(ex.message)
        }
    }

    fun limparDataMatrix(impressao: MutableList<ImpressaoBean>) {
        for (impressaoBean in impressao) {
            val nome = ConfiguracaoBean.caminhoImagem2d + "\\" + impressaoBean.codigoDoisD + ".png"
            val f = File(nome)
            f.delete()
        }
    }

    fun createDatamatrix(testo: String, fileoutputpath: String): String {
        try {
            val bean = DataMatrixBean()
            val bd = BarcodeDimension(300.0, 300.0)
            val dpi = 72
            bean.height = dpi.toDouble()
            bean.shape = SymbolShapeHint.FORCE_SQUARE
            bean.moduleWidth = bd.width / 72.0
            bean.quietZone = bd.xOffset
            bean.doQuietZone(true)
            val outputFile = File(fileoutputpath.replace("file:/", ""))
            val out: OutputStream = FileOutputStream(outputFile)
            try {
                val canvas = BitmapCanvasProvider(out, "image/x-png", dpi, 12, false, 0)
                bean.generateBarcode(canvas, testo)
                canvas.finish()
            } finally {
                out.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    @Throws(EnderecadorExcecao::class)
    fun imprimirDeclaracao(
        relatorio: String,
        itens: List<ConteudoDeclaradoBean>,
        remetenteBean: RemetenteBean,
        destinatarios: List<DestinatarioBean>,
        pesoTotal: String
    ) {
        val parametros = mutableMapOf<String, Any>()
        val lista = mutableListOf<DeclaracaoBean>()
        val formatoMoeda = NumberFormat.getCurrencyInstance()
        for (destinatario in destinatarios) {
            destinatarioBean = destinatario
            val declaracaoBean = DeclaracaoBean(
                nomeRemetente = remetenteBean.nome,
                enderecoRemetente = remetenteBean.endereco + " " + remetenteBean.numeroEndereco,
                endereco2Remetente = remetenteBean.complemento + " " + remetenteBean.bairro,
                cidadeRemetente = remetenteBean.cidade,
                ufRemetente = remetenteBean.uf,
                cepRemetente = remetenteBean.cep.substring(0, 5) + "-" + remetenteBean.cep.substring(5, 8),
                cpfRemetente = " ",
                nomeDestinatario = destinatarioBean.nome,
                enderecoDestinatario = destinatarioBean.endereco + " " + destinatarioBean.numeroEndereco,
                endereco2Destinatario = destinatarioBean.complemento + " " + destinatarioBean.bairro,
                cidadeDestinatario = destinatarioBean.cidade,
                ufDestinatario = destinatarioBean.uf,
                cepDestinatario = destinatarioBean.cep.substring(0, 5) + "-" + destinatarioBean.cep.substring(5, 8),
                cpfDestinatario = " "
            )
            var quantidadeTotal = 0
            var valorTotal = 0.0
            (0..5).forEach { j ->
                var conteudo = ""
                var quantidade = 0
                var valor = 0.0
                if (j < itens.size) {
                    conteudo = (itens[j]).conteudo
                    quantidade = itens[j].quantidade.toInt()
                    valor = (itens[j]).valor
                        .replace("[\\s,]".toRegex(), ".")
                        .toDouble()
                }
                val numero = (j + 1).toString()
                when (j) {
                    0 -> {
                        declaracaoBean.numeroItem1 = numero
                        if (conteudo.isEmpty()) declaracaoBean.numeroItem1 = ""
                        declaracaoBean.conteudoItem1 = conteudo
                        declaracaoBean.quantidadeItem1 = quantidade.toString()
                        if (quantidade == 0) declaracaoBean.quantidadeItem1 = ""
                        declaracaoBean.valorItem1 = formatoMoeda.format(valor)
                        if (valor == 0.0) declaracaoBean.valorItem1 = ""
                    }
                    1 -> {
                        declaracaoBean.numeroItem2 = numero
                        if (conteudo.isEmpty()) declaracaoBean.numeroItem2 = ""
                        declaracaoBean.conteudoItem2 = conteudo
                        declaracaoBean.quantidadeItem2 = quantidade.toString()
                        if (quantidade == 0) declaracaoBean.quantidadeItem2 = ""
                        declaracaoBean.valorItem2 = formatoMoeda.format(valor)
                        if (valor == 0.0) declaracaoBean.valorItem2 = ""
                    }
                    2 -> {
                        declaracaoBean.numeroItem3 = numero
                        if (conteudo.isEmpty()) declaracaoBean.numeroItem3 = ""
                        declaracaoBean.conteudoItem3 = conteudo
                        declaracaoBean.quantidadeItem3 = quantidade.toString()
                        if (quantidade == 0) declaracaoBean.quantidadeItem3 = ""
                        declaracaoBean.valorItem3 = formatoMoeda.format(valor)
                        if (valor == 0.0) declaracaoBean.valorItem3 = ""
                    }
                    3 -> {
                        declaracaoBean.numeroItem4 = numero
                        if (conteudo.isEmpty()) declaracaoBean.numeroItem4 = ""
                        declaracaoBean.conteudoItem4 = conteudo
                        declaracaoBean.quantidadeItem4 = quantidade.toString()
                        if (quantidade == 0) declaracaoBean.quantidadeItem4 = ""
                        declaracaoBean.valorItem4 = formatoMoeda.format(valor)
                        if (valor == 0.0) declaracaoBean.valorItem4 = ""
                    }
                    4 -> {
                        declaracaoBean.numeroItem5 = numero
                        if (conteudo.isEmpty()) declaracaoBean.numeroItem5 = ""
                        declaracaoBean.conteudoItem5 = conteudo
                        declaracaoBean.quantidadeItem5 = quantidade.toString()
                        if (quantidade == 0) declaracaoBean.quantidadeItem5 = ""
                        declaracaoBean.valorItem5 = formatoMoeda.format(valor)
                        if (valor == 0.0) declaracaoBean.valorItem5 = ""
                    }
                    5 -> {
                        declaracaoBean.numeroItem6 = numero
                        if (conteudo.isEmpty()) declaracaoBean.numeroItem6 = ""
                        declaracaoBean.conteudoItem6 = conteudo
                        declaracaoBean.quantidadeItem6 = quantidade.toString()
                        if (quantidade == 0) declaracaoBean.quantidadeItem6 = ""
                        declaracaoBean.valorItem6 = formatoMoeda.format(valor)
                        if (valor == 0.0) declaracaoBean.valorItem6 = ""
                    }
                }
                quantidadeTotal = quantidadeTotal + quantidade
                valorTotal = valorTotal + valor
            }
            declaracaoBean.quantidadeTotal = quantidadeTotal.toString()
            declaracaoBean.valorTotal = formatoMoeda.format(valorTotal)
            declaracaoBean.pesoTotal = pesoTotal
            lista.add(declaracaoBean)
        }
        try {
            visualizarRelatorioJasper(ConfiguracaoBean.caminhoRelatorio + relatorio + ".jasper", parametros, lista)
        } catch (ex: JRException) {
            logger.error(ex.message, ex)
            throw EnderecadorExcecao(ex.message)
        }
    }

    companion object {
        var logger = Logger.getLogger(Impressao::class.java)
    }
}
