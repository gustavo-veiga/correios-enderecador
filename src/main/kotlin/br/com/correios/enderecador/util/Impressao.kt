package br.com.correios.enderecador.util

import br.com.correios.enderecador.bean.ConfiguracaoBean.Companion.instance
import br.com.correios.enderecador.bean.DestinatarioBean
import br.com.correios.enderecador.bean.ImpressaoBean
import kotlin.Throws
import br.com.correios.enderecador.excecao.EnderecadorExcecao
import br.com.correios.enderecador.bean.RemetenteBean
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
import br.com.correios.enderecador.bean.DeclaracaoBean
import org.apache.log4j.Logger
import java.io.File
import java.io.OutputStream
import java.lang.Exception
import java.text.NumberFormat
import java.lang.NullPointerException
import java.lang.IllegalArgumentException
import java.util.*

class Impressao {
    private val TAMANHO_CAMPO_ENTREGA_VIZINHO = 40
    private var destinatarioBean: DestinatarioBean? = null
    private var impressaoBean: ImpressaoBean? = null
    private val codBarras = CodigoDeBarras128()
    private fun calculaDVCep(cep: String?): String {
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

    private fun pontoEntrega(numeroLote: String?): String? {
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
        remetente: RemetenteBean?,
        destinatario: Vector<DestinatarioBean?>?,
        posInicial: Int,
        telRemetente: Boolean,
        telDestinatario: Boolean
    ) {
        val impressao = ArrayList<ImpressaoBean?>()
        val parametros = HashMap<Any, Any>()
        for (k in 1 until posInicial) {
            impressaoBean = ImpressaoBean()
            impressao.add(impressaoBean)
        }
        if (destinatario != null) for (bean in destinatario) {
            impressaoBean = ImpressaoBean()
            destinatarioBean = bean
            if (destinatarioBean!!.quantidade == "") destinatarioBean!!.quantidade = "0"
            for (j in 0 until destinatarioBean!!.quantidade.toInt()) {
                impressaoBean!!.setDes_campo1(destinatarioBean!!.nome)
                impressaoBean!!.setDes_campo2(destinatarioBean!!.apelido)
                if (destinatarioBean!!.endereco.length < 24) {
                    impressaoBean!!.setDes_campo3(destinatarioBean!!.endereco + " " + destinatarioBean!!.numeroEndereco)
                    impressaoBean!!.setDes_campo4(destinatarioBean!!.complemento)
                } else {
                    impressaoBean!!.setDes_campo3(destinatarioBean!!.endereco)
                    impressaoBean!!.setDes_campo4(destinatarioBean!!.numeroEndereco + ", " + destinatarioBean!!.complemento)
                }
                impressaoBean!!.setDes_campo5(destinatarioBean!!.bairro)
                impressaoBean!!.setDes_campo6(
                    destinatarioBean!!.cep!!.substring(
                        0,
                        5
                    ) + "-" + destinatarioBean!!.cep!!.substring(5, 8)
                )
                impressaoBean!!.setDes_campo7(destinatarioBean!!.cidade + "-" + destinatarioBean!!.uf)
                var entrega = destinatarioBean!!.desEntregaVizinho
                if (entrega.length > 40) entrega = entrega.substring(0, 40)
                if (entrega.isEmpty()) entrega = "entrega no vizinho não autorizada"
                impressaoBean!!.des_entrega = entrega
                val barCode =
                    codBarras.gerarCodigoDeBarra128(2, codBarras.gerarCodigoDeBarra(2, destinatarioBean!!.cep))
                        .replace(",".toRegex(), "")
                impressaoBean!!.des_codBarras = barCode
                impressaoBean!!.des_cep =
                    destinatarioBean!!.cep!!.substring(0, 5) + "-" + destinatarioBean!!.cep!!.substring(5, 8)
                var cepRemetente = "0000000000000"
                if (remetente != null) {
                    impressaoBean!!.setRem_campo1(remetente.titulo + " " + remetente.nome)
                    impressaoBean!!.setRem_campo2(remetente.apelido!!)
                    impressaoBean!!.setRem_campo3(remetente.endereco + " " + remetente.numeroEndereco)
                    impressaoBean!!.setRem_campo4(remetente.complemento!!)
                    impressaoBean!!.setRem_campo5(remetente.bairro!!)
                    impressaoBean!!.setRem_campo6(
                        remetente.cep!!.substring(0, 5) + "-" + remetente.cep!!.substring(
                            5,
                            8
                        )
                    )
                    impressaoBean!!.setRem_campo7(remetente.cidade + "-" + remetente.uf)
                    cepRemetente = remetente.cep + pontoEntrega(remetente.numeroEndereco)
                }
                impressaoBean!!.codigoDoisD = destinatarioBean!!.cep + pontoEntrega(destinatarioBean!!.numeroEndereco)
                impressaoBean!!.codigoDoisD = impressaoBean!!.codigoDoisD + cepRemetente
                impressaoBean!!.codigoDoisD = impressaoBean!!.codigoDoisD + calculaDVCep(destinatarioBean!!.cep)
                impressaoBean!!.codigoDoisD = impressaoBean!!.codigoDoisD + "00"
                impressaoBean!!.codigoDoisD =
                    impressaoBean!!.codigoDoisD + "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                createDatamatrix(
                    impressaoBean!!.codigoDoisD,
                    instance!!.caminhoImagem2d.toString() + "\\" + impressaoBean!!.codigoDoisD + ".png"
                )
                impressao.add(impressaoBean)
            }
        }
        val path = File("").absolutePath
        parametros["caminhoImagem"] = Objects.requireNonNull(javaClass.getResource(instance!!.caminhoImagem)).toString()
        parametros["caminhoImagem2D"] = path
        try {
            visualizarRelatorioJasper(instance!!.caminhoRelatorio + relatorio, parametros, impressao)
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
        destinatario: Vector<DestinatarioBean?>?,
        qtdRemetente: Int,
        posInicial: Int,
        telRemetente: Boolean,
        telDestinatario: Boolean,
        imprimirTratamento: Boolean,
        tamanhoFonte: String
    ) {
        val impressao = ArrayList<ImpressaoBean?>()
        val parametros = HashMap<Any, Any>()
        for (k in 1 until posInicial) {
            impressaoBean = ImpressaoBean()
            impressao.add(impressaoBean)
        }
        var cepRemetenteComplemento = "0000000000000"
        if (remetente != null) for (i in 0 until qtdRemetente) {
            impressaoBean = ImpressaoBean()
            if (imprimirTratamento) impressaoBean!!.setDes_campo1(remetente.titulo!!)
            impressaoBean!!.setDes_campo2(remetente.nome!!)
            impressaoBean!!.setDes_campo3(remetente.apelido!!)
            impressaoBean!!.setDes_campo4(remetente.endereco + " " + remetente.numeroEndereco)
            if (telDestinatario) {
                impressaoBean!!.setDes_campo5(remetente.complemento + " - Telefone: " + remetente.telefone)
            } else {
                impressaoBean!!.setDes_campo5(remetente.complemento!!)
            }
            impressaoBean!!.setDes_campo6(remetente.bairro!!)
            impressaoBean!!.setDes_campo7(
                remetente.cep!!.substring(0, 5) + "-" + remetente.cep!!.substring(
                    5,
                    8
                ) + "   " + remetente.cidade + "-" + remetente.uf
            )
            cepRemetenteComplemento = remetente.cep + pontoEntrega(remetente.numeroEndereco)
            impressaoBean!!.organizaCampos()
            impressao.add(impressaoBean)
        }
        var cepDestinatarioComplemento = "0000000000000"
        if (destinatario != null) {
            for (bean in destinatario) {
                destinatarioBean = bean
                for (l in 0 until destinatarioBean!!.quantidade.toInt()) {
                    impressaoBean = ImpressaoBean()
                    if (imprimirTratamento) impressaoBean!!.setDes_campo1(destinatarioBean!!.titulo)
                    impressaoBean!!.setDes_campo2(destinatarioBean!!.nome)
                    impressaoBean!!.setDes_campo3(destinatarioBean!!.apelido)
                    impressaoBean!!.setDes_campo4(destinatarioBean!!.endereco + " " + destinatarioBean!!.numeroEndereco)
                    if (telDestinatario) {
                        impressaoBean!!.setDes_campo5(destinatarioBean!!.complemento + " - Telefone: " + destinatarioBean!!.telefone)
                    } else {
                        impressaoBean!!.setDes_campo5(destinatarioBean!!.complemento)
                    }
                    impressaoBean!!.setDes_campo6(destinatarioBean!!.bairro)
                    impressaoBean!!.setDes_campo7(
                        destinatarioBean!!.cep!!.substring(
                            0,
                            5
                        ) + "-" + destinatarioBean!!.cep!!.substring(
                            5,
                            8
                        ) + "   " + destinatarioBean!!.cidade + "-" + destinatarioBean!!.uf
                    )
                    cepDestinatarioComplemento =
                        destinatarioBean!!.cep + pontoEntrega(destinatarioBean!!.numeroEndereco)
                    impressaoBean!!.des_cep = destinatarioBean!!.cep + calculaDVCep(destinatarioBean!!.cep)
                    impressaoBean!!.organizaCampos()
                    impressao.add(impressaoBean)
                }
            }
            impressaoBean!!.codigoDoisD = cepDestinatarioComplemento
            impressaoBean!!.codigoDoisD = impressaoBean!!.codigoDoisD + cepRemetenteComplemento
            impressaoBean!!.codigoDoisD = impressaoBean!!.codigoDoisD + calculaDVCep(destinatarioBean!!.cep)
            impressaoBean!!.codigoDoisD = impressaoBean!!.codigoDoisD + "00"
            impressaoBean!!.codigoDoisD =
                impressaoBean!!.codigoDoisD + "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
            createDatamatrix(
                impressaoBean!!.codigoDoisD,
                instance!!.caminhoImagem2d.toString() + "\\" + impressaoBean!!.codigoDoisD + ".png"
            )
        }
        parametros["caminhoImagem"] = Objects.requireNonNull(javaClass.getResource(instance!!.caminhoImagem)).toString()
        parametros["tamanhoFonte"] = tamanhoFonte
        parametros["caminhoImagem2D"] = instance!!.caminhoImagem2d
        try {
            visualizarRelatorioJasper(instance!!.caminhoRelatorio + relatorio, parametros, impressao)
            limparDataMatrix(impressao)
        } catch (ex: JRException) {
            logger.error(ex.message, ex as Throwable)
            throw EnderecadorExcecao(ex.message)
        }
    }

    @Throws(JRException::class)
    fun visualizarRelatorioJasper(relatorio: String?, parametros: Map<*, *>?, lista: List<*>?) {
        val `in` = javaClass.classLoader.getResourceAsStream(relatorio)
        val impressao =
            JasperFillManager.fillReport(`in`, parametros, JRBeanCollectionDataSource(lista) as JRDataSource)
        val viewer = JasperViewer(impressao, false)
        viewer.isVisible = true
    }

    @Throws(JRException::class)
    fun visualizarRelatorioJrxml(relatorio: String, parametros: Map<*, *>?, lista: List<*>?) {
        val `in` = javaClass.classLoader.getResourceAsStream("$relatorio.jrxml")
        val desenho = JRXmlLoader.load(`in`)
        val report = JasperCompileManager.compileReport(desenho)
        val impressao =
            JasperFillManager.fillReport(report, parametros, JRBeanCollectionDataSource(lista) as JRDataSource)
        val viewer = JasperViewer(impressao, false)
        viewer.isVisible = true
    }

    @Throws(EnderecadorExcecao::class)
    fun imprimirEnvelope(
        relatorio: String,
        remetente: RemetenteBean?,
        destinatario: Vector<DestinatarioBean?>,
        telefone: Boolean,
        tamanhoFonte: String
    ) {
        val impressao = ArrayList<ImpressaoBean?>()
        val parametros = HashMap<Any, Any>()
        for (bean in destinatario) {
            destinatarioBean = bean
            var cepDestinatarioComplemento: String
            for (j in 0 until destinatarioBean!!.quantidade.toInt()) {
                impressaoBean = ImpressaoBean()
                impressaoBean!!.setDes_campo1(destinatarioBean!!.nome.uppercase(Locale.getDefault()))
                impressaoBean!!.setDes_campo2(destinatarioBean!!.apelido.uppercase(Locale.getDefault()))
                impressaoBean!!.setDes_campo3(destinatarioBean!!.endereco.uppercase(Locale.getDefault()) + " " + destinatarioBean!!.numeroEndereco)
                impressaoBean!!.setDes_campo4(destinatarioBean!!.complemento.uppercase(Locale.getDefault()))
                impressaoBean!!.setDes_campo5(destinatarioBean!!.bairro.uppercase(Locale.getDefault()))
                impressaoBean!!.setDes_campo6(
                    destinatarioBean!!.cep!!.substring(
                        0,
                        5
                    ) + "-" + destinatarioBean!!.cep!!.substring(
                        5,
                        8
                    ) + "   " + destinatarioBean!!.cidade + "-" + destinatarioBean!!.uf
                )
                if (telefone) impressaoBean!!.setDes_campo7(destinatarioBean!!.telefone.uppercase(Locale.getDefault()))
                impressaoBean!!.des_cep = destinatarioBean!!.cep + calculaDVCep(destinatarioBean!!.cep)
                cepDestinatarioComplemento = destinatarioBean!!.cep + pontoEntrega(destinatarioBean!!.numeroEndereco)
                var cepRemetenteComplemento = "0000000000000"
                if (remetente != null) {
                    impressaoBean!!.setRem_campo1(remetente.titulo!!)
                    impressaoBean!!.setRem_campo2(remetente.nome!!)
                    impressaoBean!!.setRem_campo3(remetente.apelido!!)
                    impressaoBean!!.setRem_campo4(remetente.endereco + " " + remetente.numeroEndereco)
                    impressaoBean!!.setRem_campo5(remetente.complemento!!)
                    impressaoBean!!.setRem_campo6(remetente.bairro!!)
                    impressaoBean!!.setRem_campo7(
                        remetente.cep!!.substring(0, 5) + "-" + remetente.cep!!.substring(
                            5,
                            8
                        ) + "   " + remetente.cidade + "-" + remetente.uf
                    )
                    cepRemetenteComplemento = remetente.cep + pontoEntrega(remetente.numeroEndereco)
                }
                impressaoBean!!.codigoDoisD = cepDestinatarioComplemento
                impressaoBean!!.codigoDoisD = impressaoBean!!.codigoDoisD + cepRemetenteComplemento
                impressaoBean!!.codigoDoisD = impressaoBean!!.codigoDoisD + calculaDVCep(destinatarioBean!!.cep)
                impressaoBean!!.codigoDoisD = impressaoBean!!.codigoDoisD + "00"
                impressaoBean!!.codigoDoisD =
                    impressaoBean!!.codigoDoisD + "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                createDatamatrix(
                    impressaoBean!!.codigoDoisD,
                    instance!!.caminhoImagem2d.toString() + "\\" + impressaoBean!!.codigoDoisD + ".png"
                )
                impressaoBean!!.organizaCampos()
                impressao.add(impressaoBean)
            }
        }
        parametros["tamanhoFonte"] = tamanhoFonte
        parametros["caminhoImagem"] = Objects.requireNonNull(javaClass.getResource(instance!!.caminhoImagem)).toString()
        parametros["caminhoImagem2D"] = instance!!.caminhoImagem2d
        try {
            visualizarRelatorioJasper(instance!!.caminhoRelatorio + relatorio, parametros, impressao)
            limparDataMatrix(impressao)
        } catch (ex: JRException) {
            logger.error(ex.message, ex as Throwable)
            throw EnderecadorExcecao(ex.message)
        }
    }

    @Throws(EnderecadorExcecao::class)
    fun imprimirAR(relatorio: String, rementente: RemetenteBean, destinatario: Vector<DestinatarioBean?>) {
        val impressao = ArrayList<ImpressaoBean?>()
        val parametros = HashMap<Any, Any>()
        for (bean in destinatario) {
            impressaoBean = ImpressaoBean()
            destinatarioBean = bean
            if (destinatarioBean!!.quantidade == "") destinatarioBean!!.quantidade = "0"
            for (j in 0 until destinatarioBean!!.quantidade.toInt()) {
                impressaoBean!!.setDes_campo1(destinatarioBean!!.nome)
                impressaoBean!!.setDes_campo2(destinatarioBean!!.apelido)
                impressaoBean!!.setDes_campo3(destinatarioBean!!.endereco + " " + destinatarioBean!!.numeroEndereco)
                impressaoBean!!.setDes_campo4(destinatarioBean!!.complemento + " " + destinatarioBean!!.bairro)
                impressaoBean!!.setDes_campo5(
                    destinatarioBean!!.cep!!.substring(
                        0,
                        5
                    ) + "-" + destinatarioBean!!.cep!!.substring(
                        5,
                        8
                    ) + "   " + destinatarioBean!!.cidade + "-" + destinatarioBean!!.uf
                )
                impressaoBean!!.desMaoPropria = destinatarioBean!!.maoPropria
                impressaoBean!!.desConteudo = destinatarioBean!!.desConteudo
                impressaoBean!!.setRem_campo1(rementente.nome!!)
                impressaoBean!!.setRem_campo2(rementente.apelido!!)
                impressaoBean!!.setRem_campo3(rementente.endereco + " " + rementente.numeroEndereco)
                impressaoBean!!.setRem_campo4(rementente.complemento + " " + rementente.bairro)
                impressaoBean!!.setRem_campo5(
                    rementente.cep!!.substring(0, 5) + "-" + rementente.cep!!.substring(
                        5,
                        8
                    ) + "   " + rementente.cidade + "-" + rementente.uf
                )
                impressaoBean!!.organizaCampos()
                impressao.add(impressaoBean)
            }
        }
        parametros["caminhoImagem"] = Objects.requireNonNull(javaClass.getResource(instance!!.caminhoImagem)).toString()
        parametros["caminhoImagem2D"] = instance!!.caminhoImagem2d
        try {
            visualizarRelatorioJasper(instance!!.caminhoRelatorio + relatorio, parametros, impressao)
        } catch (ex: JRException) {
            logger.error(ex.message, ex)
            throw EnderecadorExcecao(ex.message)
        }
    }

    fun limparDataMatrix(impressao: ArrayList<ImpressaoBean?>) {
        for (bean in impressao) {
            impressaoBean = bean
            val nome = instance!!.caminhoImagem2d + "\\" + impressaoBean!!.codigoDoisD + ".png"
            val f = File(nome)
            f.delete()
        }
    }

    fun createDatamatrix(testo: String?, fileoutputpath: String): String {
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
        itens: Vector<Vector<*>>,
        remetenteBean: RemetenteBean,
        destinatarios: Vector<DestinatarioBean?>,
        pesoTotal: String?
    ) {
        val parametros: Map<Any, Any> = HashMap()
        val lista = ArrayList<DeclaracaoBean>()
        val formatoMoeda = NumberFormat.getCurrencyInstance()
        for (destinatario in destinatarios) {
            destinatarioBean = destinatario
            val declaracaoBean = DeclaracaoBean(
                remetenteBean.nome!!,
                remetenteBean.endereco + " " + remetenteBean.numeroEndereco,
                remetenteBean.complemento + " " + remetenteBean.bairro,
                remetenteBean.cidade!!,
                remetenteBean.uf!!,
                remetenteBean.cep!!.substring(0, 5) + "-" + remetenteBean.cep!!.substring(5, 8),
                " ",
                destinatarioBean!!.nome,
                destinatarioBean!!.endereco + " " + destinatarioBean!!.numeroEndereco,
                destinatarioBean!!.complemento + " " + destinatarioBean!!.bairro,
                destinatarioBean!!.cidade,
                destinatarioBean!!.uf,
                destinatarioBean!!.cep!!.substring(0, 5) + "-" + destinatarioBean!!.cep!!.substring(5, 8),
                " "
            )
            var quantidadeTotal = 0
            var valorTotal = 0.0
            for (j in 0..5) {
                var conteudo: String? = ""
                var quantidade = 0
                var valor = 0.0
                if (j < itens.size) {
                    conteudo = (itens[j] as Vector<String?>)[0]
                    if (conteudo == null) conteudo = ""
                    quantidade = convertToInteger((itens[j] as Vector<String?>)[1])
                    if (quantidade == null) quantidade = 0
                    var temp = (itens[j] as Vector<String?>)[2]
                    if (temp != null) {
                        temp = temp.replace("[\\s,]".toRegex(), ".")
                        valor = convertToDouble(temp)
                    } else {
                        valor = java.lang.Double.valueOf(0.0)
                    }
                }
                val numero = (j + 1).toString()
                when (j) {
                    0 -> {
                        declaracaoBean.numeroItem1 = numero
                        if (conteudo!!.isEmpty()) declaracaoBean.numeroItem1 = ""
                        declaracaoBean.conteudoItem1 = conteudo
                        declaracaoBean.quantidadeItem1 = quantidade.toString()
                        if (quantidade == 0) declaracaoBean.quantidadeItem1 = ""
                        declaracaoBean.valorItem1 = formatoMoeda.format(valor)
                        if (valor == 0.0) declaracaoBean.valorItem1 = ""
                    }
                    1 -> {
                        declaracaoBean.numeroItem2 = numero
                        if (conteudo!!.isEmpty()) declaracaoBean.numeroItem2 = ""
                        declaracaoBean.conteudoItem2 = conteudo
                        declaracaoBean.quantidadeItem2 = quantidade.toString()
                        if (quantidade == 0) declaracaoBean.quantidadeItem2 = ""
                        declaracaoBean.valorItem2 = formatoMoeda.format(valor)
                        if (valor == 0.0) declaracaoBean.valorItem2 = ""
                    }
                    2 -> {
                        declaracaoBean.numeroItem3 = numero
                        if (conteudo!!.isEmpty()) declaracaoBean.numeroItem3 = ""
                        declaracaoBean.conteudoItem3 = conteudo
                        declaracaoBean.quantidadeItem3 = quantidade.toString()
                        if (quantidade == 0) declaracaoBean.quantidadeItem3 = ""
                        declaracaoBean.valorItem3 = formatoMoeda.format(valor)
                        if (valor == 0.0) declaracaoBean.valorItem3 = ""
                    }
                    3 -> {
                        declaracaoBean.numeroItem4 = numero
                        if (conteudo!!.isEmpty()) declaracaoBean.numeroItem4 = ""
                        declaracaoBean.conteudoItem4 = conteudo
                        declaracaoBean.quantidadeItem4 = quantidade.toString()
                        if (quantidade == 0) declaracaoBean.quantidadeItem4 = ""
                        declaracaoBean.valorItem4 = formatoMoeda.format(valor)
                        if (valor == 0.0) declaracaoBean.valorItem4 = ""
                    }
                    4 -> {
                        declaracaoBean.numeroItem5 = numero
                        if (conteudo!!.isEmpty()) declaracaoBean.numeroItem5 = ""
                        declaracaoBean.conteudoItem5 = conteudo
                        declaracaoBean.quantidadeItem5 = quantidade.toString()
                        if (quantidade == 0) declaracaoBean.quantidadeItem5 = ""
                        declaracaoBean.valorItem5 = formatoMoeda.format(valor)
                        if (valor == 0.0) declaracaoBean.valorItem5 = ""
                    }
                    5 -> {
                        declaracaoBean.numeroItem6 = numero
                        if (conteudo!!.isEmpty()) declaracaoBean.numeroItem6 = ""
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
            visualizarRelatorioJasper(instance!!.caminhoRelatorio + relatorio + ".jasper", parametros, lista)
        } catch (ex: JRException) {
            logger.error(ex.message, ex)
            throw EnderecadorExcecao(ex.message)
        }
    }

    private fun convertToInteger(valor: String?): Int {
        return try {
            Integer.valueOf(valor)
        } catch (e: NullPointerException) {
            0
        } catch (e: IllegalArgumentException) {
            0
        }
    }

    private fun convertToDouble(valor: String): Double {
        return try {
            java.lang.Double.valueOf(valor)
        } catch (e: NullPointerException) {
            0.0
        } catch (e: IllegalArgumentException) {
            0.0
        }
    }

    companion object {
        var logger = Logger.getLogger(Impressao::class.java)
    }
}
