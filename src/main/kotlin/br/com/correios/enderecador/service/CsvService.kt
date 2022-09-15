package br.com.correios.enderecador.service

import br.com.correios.enderecador.bean.DestinatarioBean
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

interface CsvService {
    fun export(filename: String, header: Boolean, destinatario: List<DestinatarioBean>): Job

    fun import(filename: String, header: Boolean): Flow<DestinatarioBean>
}
