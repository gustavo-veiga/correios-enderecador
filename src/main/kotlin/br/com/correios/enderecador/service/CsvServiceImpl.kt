package br.com.correios.enderecador.service

import br.com.correios.enderecador.bean.DestinatarioBean
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.dataformat.csv.CsvGenerator
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.koin.core.annotation.Singleton
import java.io.FileReader
import java.nio.file.Files
import java.nio.file.Paths

@Singleton
class CsvServiceImpl : CsvService {
    private val scope = CoroutineScope(Dispatchers.IO)

    private val mapper = CsvMapper.builder()
        .enable(CsvGenerator.Feature.ALWAYS_QUOTE_EMPTY_STRINGS)
        .enable(JsonGenerator.Feature.IGNORE_UNKNOWN)
        .build()
        .registerKotlinModule()

    private val schema = CsvSchema.builder()
        .addColumn(DestinatarioBean::titulo.name)
        .addColumn(DestinatarioBean::nome.name)
        .addColumn(DestinatarioBean::apelido.name)
        .addColumn(DestinatarioBean::caixaPostal.name)
        .addColumn(DestinatarioBean::endereco.name)
        .addColumn(DestinatarioBean::numeroEndereco.name)
        .addColumn(DestinatarioBean::complemento.name)
        .addColumn(DestinatarioBean::bairro.name)
        .addColumn(DestinatarioBean::cidade.name)
        .addColumn(DestinatarioBean::uf.name)
        .addColumn(DestinatarioBean::email.name)
        .addColumn(DestinatarioBean::telefone.name)
        .addColumn(DestinatarioBean::fax.name)
        .addColumn(DestinatarioBean::cepCaixaPostal.name)
        .addColumn(DestinatarioBean::cep.name)
        .build()

    override fun export(filename: String, header: Boolean, destinatario: List<DestinatarioBean>) = scope.launch {
        val writer = withContext(Dispatchers.IO) {
            Files.newBufferedWriter(Paths.get(filename))
        }

        if (header) {
            mapper.writer(schema.withHeader()).writeValue(writer, destinatario)
        } else {
            mapper.writer(schema).writeValue(writer, destinatario)
        }

        withContext(Dispatchers.IO) {
            writer.close()
        }
    }

    override fun import(filename: String, header: Boolean) = flow<DestinatarioBean> {
        val reader = FileReader(filename)

        val mapping = mapper.readerFor(DestinatarioBean::class.java)
            .with(if (header) schema.withHeader() else schema)
        val iterator = mapping.readValues<DestinatarioBean>(reader)

        while (iterator.hasNext()) {
            emit(iterator.next())
            delay(2000L)
        }
    }.flowOn(Dispatchers.IO)
}
