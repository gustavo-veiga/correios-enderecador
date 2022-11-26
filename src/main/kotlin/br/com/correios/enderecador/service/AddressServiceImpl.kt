package br.com.correios.enderecador.service

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.core.annotation.Singleton

@Singleton
class AddressServiceImpl : AddressService {
    private val client = OkHttpClient()
    private val mediaType = "text/xml".toMediaType()

    override fun findByZipCode(zipCode: String) {
        val soap = """
            <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                xmlns:cli="http://cliente.bean.master.sigep.bsb.correios.com.br/">
               <soapenv:Header/>
               <soapenv:Body>
                  <cli:consultaCEP>
                     <cep>${zipCode}</cep>
                  </cli:consultaCEP>
               </soapenv:Body>
            </soapenv:Envelope>
        """.trimIndent()
        val request  = Request.Builder()
            .url("https://apps.correios.com.br/SigepMasterJPA/AtendeClienteService/AtendeCliente")
            .post(soap.toRequestBody(mediaType))
            .addHeader("content-type", "text/xml")
            .build()

        val response = client.newCall(request).execute();

        println(soap)
        println(response.body?.string())
    }
}