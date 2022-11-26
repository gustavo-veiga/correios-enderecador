package br.com.correios.enderecador.service

import br.com.correios.enderecador.bean.*
import br.com.correios.enderecador.exception.EnderecadorExcecao
import br.com.correios.enderecador.util.FontSize
import br.com.correios.enderecador.util.Logging
import br.com.correios.enderecador.util.Report
import br.com.correios.enderecador.util.maskZipCode
import com.google.zxing.BarcodeFormat.CODE_128
import com.google.zxing.BarcodeFormat.DATA_MATRIX
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.datamatrix.DataMatrixWriter
import com.google.zxing.oned.Code128Writer
import net.sf.jasperreports.engine.JRException
import net.sf.jasperreports.engine.JasperFillManager
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
import net.sf.jasperreports.engine.JRDataSource
import net.sf.jasperreports.view.JasperViewer
import net.sf.jasperreports.engine.xml.JRXmlLoader
import net.sf.jasperreports.engine.JasperCompileManager
import org.koin.core.annotation.Singleton
import java.lang.StringBuilder
import java.text.NumberFormat

@Singleton
class PrintReportService {
    companion object {
        private const val TAMANHO_CAMPO_ENTREGA_VIZINHO = 40
    }

    private val logger by Logging()

    private val code128Writer = Code128Writer()
    private val dataMatrixWriter = DataMatrixWriter()

    private fun calculaDVCep(cep: String): String {
        var dv = 0
        var somatorio = 0
        var i = 0
        while (i < cep.length) {
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
        if (numeroLote.isBlank() || numeroLote.trim().length > 5 || !numeroLote.matches("^[0-9]{1,5}$".toRegex())) {
            return pontoEntregaDefault
        }
        return if (pontoEntregaDefault.length == numeroLote.length) {
            numeroLote
        } else {
            pontoEntregaDefault.substring(0, pontoEntregaDefault.length - numeroLote.length) + numeroLote
        }
    }

    private fun datamatrix(recipientZipCode: String, senderZipCode: String?, recipientNumber: String, senderNumber: String?): String {
        return StringBuilder().apply {
            append(recipientZipCode)
            append(pontoEntrega(recipientNumber))
            senderZipCode?.let { append(it) }
            senderNumber?.let { append(it) }
            append(calculaDVCep(recipientZipCode))
            append("0".repeat(99))
        }.toString()
    }

    private fun visualizarRelatorioJasper(report: String, list: List<Any>, parameters: Map<String, Any> = mutableMapOf()) {
        val file = "/relatorios/$report.jasper"
        val stream = javaClass.classLoader.getResourceAsStream(file)
        val dataSource = JRBeanCollectionDataSource(list) as JRDataSource

        JasperFillManager.fillReport(stream, parameters, dataSource).apply {
            val viewer = JasperViewer(this, false)
            viewer.isVisible = true
        }
    }

    private fun visualizarRelatorioJrxml(file: String, list: List<Any>, parameters: Map<String, Any> = mutableMapOf()) {
        val stream = javaClass.classLoader.getResourceAsStream("relatorios/$file.jrxml")
        val report = JRXmlLoader.load(stream).let {
            JasperCompileManager.compileReport(it)
        }
        val dataSource = JRBeanCollectionDataSource(list) as JRDataSource

        JasperFillManager.fillReport(report, parameters, dataSource).apply {
            val viewer = JasperViewer(this, false)
            viewer.isVisible = true
        }
    }

    fun order(
        report: Report,
        sender: RemetenteBean,
        recipientList: List<DestinatarioBean>,
        fromTheTag: Int,
    ) {
        val emptyPrint = (1 until fromTheTag).map { ImpressaoBean() }

        val recipientPrint = recipientList.flatMap { recipient ->
            (1 until recipient.copies).map { _ ->
                ImpressaoBean(
                    des_campo1 = recipient.nome,
                    des_campo2 = recipient.apelido,
                    des_campo3 = when {
                        recipient.endereco.length < 24 -> recipient.endereco + " " + recipient.numeroEndereco
                        else -> recipient.endereco
                    },
                    des_campo4 = when {
                        recipient.endereco.length < 24 -> recipient.complemento
                        else -> recipient.numeroEndereco + ", " + recipient.complemento
                    },
                    des_campo5 = recipient.bairro,
                    des_campo6 = recipient.cep.maskZipCode(),
                    des_campo7 = recipient.cidade + "-" + recipient.uf,

                    rem_campo1 = sender.titulo + " " + sender.nome,
                    rem_campo2 = sender.apelido,
                    rem_campo3 = sender.endereco + " " + sender.numeroEndereco,
                    rem_campo4 = sender.complemento,
                    rem_campo5 = sender.bairro,
                    rem_campo7 = sender.cidade + "-" + sender.uf,

                    des_cep = recipient.cep.maskZipCode(),
                    des_entrega = when {
                        recipient.desEntregaVizinho.length > TAMANHO_CAMPO_ENTREGA_VIZINHO -> {
                            recipient.desEntregaVizinho.substring(0, TAMANHO_CAMPO_ENTREGA_VIZINHO)
                        }
                        recipient.desEntregaVizinho.isEmpty() -> {
                            "entrega no vizinho não autorizada"
                        }
                        else -> recipient.desEntregaVizinho
                    },

                    barcodeZip = code128Writer.encode(recipient.cep, CODE_128, 150, 50).let {
                        MatrixToImageWriter.toBufferedImage(it)
                    },
                    datamatrix = datamatrix(recipient.cep, sender.cep, recipient.numeroEndereco, sender.numeroEndereco).let {
                        val data = dataMatrixWriter.encode(it, DATA_MATRIX, 300, 300)
                        MatrixToImageWriter.toBufferedImage(data)
                    }
                )
            }
        }

        try {
            visualizarRelatorioJasper(report.file, emptyPrint + recipientPrint)
        } catch (ex: JRException) {
            logger.error(ex.message)
        }
    }

    fun impressaoCarta(
        report: Report,
        sender: RemetenteBean?,
        recipientList: List<DestinatarioBean>?,
        senderCopies: Int,
        fromTheTag: Int,
        withPhoneSender: Boolean,
        withPhoneRecipient: Boolean,
        imprimirTratamento: Boolean,
        fontSize: FontSize
    ) {
        val emptyPrint = (1 until fromTheTag).map { ImpressaoBean() }

        val senderPrintList = if (sender != null) {
            (0 until senderCopies).map { _ ->
                ImpressaoBean(
                    des_campo1 = if (imprimirTratamento) sender.titulo else "",
                    des_campo2 = sender.nome,
                    des_campo3 = sender.apelido,
                    des_campo4 = sender.endereco + " " + sender.numeroEndereco,
                    des_campo5 = when {
                        withPhoneSender -> sender.complemento + " - Telefone: " + sender.telefone
                        else -> sender.complemento
                    },
                    des_campo6 = sender.bairro,
                    des_campo7 = sender.cep.maskZipCode() + "   " + sender.cidade + "-" + sender.uf
                ).also { it.organizaCampos() }
            }
        } else emptyList()

        val recipientPrintList = recipientList?.flatMap { recipient ->
            (0 until recipient.copies).map { _ ->
                ImpressaoBean(
                    des_campo1 = if (imprimirTratamento) recipient.titulo else "",
                    des_campo2 = recipient.nome,
                    des_campo3 = recipient.apelido,
                    des_campo4 = recipient.endereco + " " + recipient.numeroEndereco,
                    des_campo5 = when {
                        withPhoneRecipient -> recipient.complemento + " - Telefone: " + recipient.telefone
                        else -> recipient.complemento
                    },
                    des_campo6 = recipient.bairro,
                    des_campo7 = recipient.cep.maskZipCode() + "   " + recipient.cidade + "-" + recipient.uf,
                    datamatrix = recipient.cep.let {
                        val data = dataMatrixWriter.encode(it, DATA_MATRIX, 300, 300)
                        MatrixToImageWriter.toBufferedImage(data)
                    }
                ).also { it.organizaCampos() }
            }
        } ?: emptyList()

        val parameters = mutableMapOf(
            "tamanhoFonte" to fontSize.description.substring(0, 1)
        )

        try {
            visualizarRelatorioJasper(report.file, emptyPrint + senderPrintList + recipientPrintList, parameters)
        } catch (ex: JRException) {
            logger.error(ex.message, ex as Throwable)
            throw EnderecadorExcecao(ex.message)
        }
    }

    fun imprimirEnvelope(
        report: Report,
        sender: RemetenteBean?,
        recipientList: List<DestinatarioBean>,
        withPhone: Boolean,
        fontSize: FontSize
    ) {
        val recipientPrintList = recipientList.flatMap { recipient ->
            (1 until recipient.copies).map { _ ->
                ImpressaoBean(
                    des_campo1 = recipient.nome.uppercase(),
                    des_campo2 = recipient.apelido.uppercase(),
                    des_campo3 = recipient.endereco.uppercase() + " " + recipient.numeroEndereco,
                    des_campo4 = recipient.complemento.uppercase(),
                    des_campo5 = recipient.bairro.uppercase(),
                    des_campo6 = recipient.cep.maskZipCode() + "   " + recipient.cidade + "-" + recipient.uf,
                    des_campo7 = if (withPhone) recipient.telefone.uppercase() else "",
                    des_cep = recipient.cep + calculaDVCep(recipient.cep),

                    rem_campo1 = sender?.titulo ?: "",
                    rem_campo2 = sender?.nome ?: "",
                    rem_campo3 = sender?.apelido ?: "",
                    rem_campo5 = sender?.complemento ?: "",
                    rem_campo6 = sender?.bairro ?: "",
                    rem_campo7 = (sender?.cep?.maskZipCode() ?: "") + "   " + sender?.cidade + "-" + sender?.uf,

                    datamatrix = datamatrix(recipient.cep, sender?.cep,
                        recipient.numeroEndereco, sender?.numeroEndereco
                    ).let {
                        val data = dataMatrixWriter.encode(it, DATA_MATRIX, 300, 300)
                        MatrixToImageWriter.toBufferedImage(data)
                    }
                ).also { it.organizaCampos() }
            }
        }

        val parameters = mutableMapOf(
            "tamanhoFonte" to fontSize.description.substring(0, 1)
        )

        try {
            visualizarRelatorioJrxml(report.file, recipientPrintList, parameters)
        } catch (ex: JRException) {
            logger.error(ex.message, ex as Throwable)
            throw EnderecadorExcecao(ex.message)
        }
    }

    fun imprimirAR(report: String, sender: RemetenteBean, recipientList: List<DestinatarioBean>) {
        val recipientPrint = recipientList.flatMap { recipient ->
            (1 until recipient.copies).map { _ ->
                ImpressaoBean(
                    des_campo1 = recipient.nome,
                    des_campo2 = recipient.apelido,
                    des_campo3 = recipient.endereco + " " + recipient.numeroEndereco,
                    des_campo4 = recipient.complemento + " " + recipient.bairro,
                    des_campo5 = recipient.cep.maskZipCode() + "   " + recipient.cidade + "-" + recipient.uf,
                    desMaoPropria = recipient.maoPropria,
                    desConteudo = recipient.desConteudo,
                    rem_campo1 = sender.nome,
                    rem_campo2 = sender.apelido,
                    rem_campo3 = sender.endereco + " " + sender.numeroEndereco,
                    rem_campo4 = sender.complemento + " " + sender.bairro,
                    rem_campo5 = sender.cep.maskZipCode() + "   " + sender.cidade + "-" + sender.uf
                ).also { it.organizaCampos() }
            }
        }

        try {
            visualizarRelatorioJasper(report, recipientPrint)
        } catch (ex: JRException) {
            logger.error(ex.message, ex)
            throw EnderecadorExcecao(ex.message)
        }
    }

    fun imprimirDeclaracao(
        report: Report,
        items: List<ConteudoDeclaradoBean>,
        sender: RemetenteBean,
        recipientList: List<DestinatarioBean>,
        totalWeight: String
    ) {
        val printList = mutableListOf<DeclaracaoBean>()
        val currencyFormat = NumberFormat.getCurrencyInstance()

        for (recipient in recipientList) {
            val declaracaoBean = DeclaracaoBean(
                nomeRemetente = sender.nome,
                enderecoRemetente = sender.endereco + " " + sender.numeroEndereco,
                endereco2Remetente = sender.complemento + " " + sender.bairro,
                cidadeRemetente = sender.cidade,
                ufRemetente = sender.uf,
                cepRemetente = sender.cep.maskZipCode(),
                cpfRemetente = " ",
                nomeDestinatario = recipient.nome,
                enderecoDestinatario = recipient.endereco + " " + recipient.numeroEndereco,
                endereco2Destinatario = recipient.complemento + " " + recipient.bairro,
                cidadeDestinatario = recipient.cidade,
                ufDestinatario = recipient.uf,
                cepDestinatario = recipient.cep.maskZipCode(),
                cpfDestinatario = " "
            )
            var quantidadeTotal = 0
            var valorTotal = 0.0
            (0..5).forEach { j ->
                var conteudo = ""
                var quantidade = 0
                var valor = 0.0
                if (j < items.size) {
                    conteudo = (items[j]).conteudo
                    quantidade = items[j].quantidade.toInt()
                    valor = (items[j]).valor
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
                        declaracaoBean.valorItem1 = currencyFormat.format(valor)
                        if (valor == 0.0) declaracaoBean.valorItem1 = ""
                    }
                    1 -> {
                        declaracaoBean.numeroItem2 = numero
                        if (conteudo.isEmpty()) declaracaoBean.numeroItem2 = ""
                        declaracaoBean.conteudoItem2 = conteudo
                        declaracaoBean.quantidadeItem2 = quantidade.toString()
                        if (quantidade == 0) declaracaoBean.quantidadeItem2 = ""
                        declaracaoBean.valorItem2 = currencyFormat.format(valor)
                        if (valor == 0.0) declaracaoBean.valorItem2 = ""
                    }
                    2 -> {
                        declaracaoBean.numeroItem3 = numero
                        if (conteudo.isEmpty()) declaracaoBean.numeroItem3 = ""
                        declaracaoBean.conteudoItem3 = conteudo
                        declaracaoBean.quantidadeItem3 = quantidade.toString()
                        if (quantidade == 0) declaracaoBean.quantidadeItem3 = ""
                        declaracaoBean.valorItem3 = currencyFormat.format(valor)
                        if (valor == 0.0) declaracaoBean.valorItem3 = ""
                    }
                    3 -> {
                        declaracaoBean.numeroItem4 = numero
                        if (conteudo.isEmpty()) declaracaoBean.numeroItem4 = ""
                        declaracaoBean.conteudoItem4 = conteudo
                        declaracaoBean.quantidadeItem4 = quantidade.toString()
                        if (quantidade == 0) declaracaoBean.quantidadeItem4 = ""
                        declaracaoBean.valorItem4 = currencyFormat.format(valor)
                        if (valor == 0.0) declaracaoBean.valorItem4 = ""
                    }
                    4 -> {
                        declaracaoBean.numeroItem5 = numero
                        if (conteudo.isEmpty()) declaracaoBean.numeroItem5 = ""
                        declaracaoBean.conteudoItem5 = conteudo
                        declaracaoBean.quantidadeItem5 = quantidade.toString()
                        if (quantidade == 0) declaracaoBean.quantidadeItem5 = ""
                        declaracaoBean.valorItem5 = currencyFormat.format(valor)
                        if (valor == 0.0) declaracaoBean.valorItem5 = ""
                    }
                    5 -> {
                        declaracaoBean.numeroItem6 = numero
                        if (conteudo.isEmpty()) declaracaoBean.numeroItem6 = ""
                        declaracaoBean.conteudoItem6 = conteudo
                        declaracaoBean.quantidadeItem6 = quantidade.toString()
                        if (quantidade == 0) declaracaoBean.quantidadeItem6 = ""
                        declaracaoBean.valorItem6 = currencyFormat.format(valor)
                        if (valor == 0.0) declaracaoBean.valorItem6 = ""
                    }
                }
                quantidadeTotal = quantidadeTotal + quantidade
                valorTotal = valorTotal + valor
            }
            declaracaoBean.quantidadeTotal = quantidadeTotal.toString()
            declaracaoBean.valorTotal = currencyFormat.format(valorTotal)
            declaracaoBean.pesoTotal = totalWeight
            printList.add(declaracaoBean)
        }
        try {
            visualizarRelatorioJasper(report.file, printList)
        } catch (ex: JRException) {
            logger.error(ex.message, ex)
            throw EnderecadorExcecao(ex.message)
        }
    }
}
