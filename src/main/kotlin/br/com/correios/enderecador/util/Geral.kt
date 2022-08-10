package br.com.correios.enderecador.util

import kotlin.Throws
import br.com.correios.enderecador.excecao.EnderecadorExcecao
import br.com.correios.enderecador.bean.DestinatarioBean
import java.io.FileWriter
import java.io.IOException
import java.lang.StringBuilder
import edu.stanford.ejalbert.BrowserLauncher
import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException
import java.io.File
import java.util.*

class Geral {
    @Throws(EnderecadorExcecao::class)
    fun exportToTxt(filename: String, destinatario: Vector<DestinatarioBean?>) {
        val text = StringBuilder()
        for (bean in destinatario) {
            text.append("""
                >${bean!!.titulo}
                >;${bean.nome}
                >;${bean.apelido}
                >;${bean.caixaPostal}
                >;${bean.endereco}
                >;${bean.numeroEndereco}
                >;${bean.complemento}
                >;${bean.bairro}
                >;${bean.cidade}
                >;${bean.uf}
                >;${bean.email}
                >;${bean.telefone}
                >;${bean.fax}
                >;${bean.cepCaixaPostal}
                >;${bean.cep}
            """.trimMargin(">")
                .replace("\n", ""))
                .append("\n")
        }
        try {
            val writer = FileWriter(File(filename))
            writer.write(text.toString())
            writer.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Throws(EnderecadorExcecao::class)
    fun exportToCsv(arquivo: String, destinatario: Vector<DestinatarioBean?>) {
        val text = StringBuilder()
        text.append("""
            >Tratamento
            >Empresa/Nome (linha 1)
            >Empresa/Nome (linha 2)
            >Caixa Postal
            >Endereço
            >Número/Lote
            >Complemento
            >Bairro
            >Cidade
            >UF
            >E-mail
            >Telefone
            >Fax
            >CEP Caixa Postal
            >CEP
        """.trimMargin(">")
            .replace("\n", ""))
            .append("\n")
        for (bean in destinatario) {
            text.append("""
                >${bean!!.titulo}
                >;${bean.nome}
                >;${bean.apelido}
                >;${bean.caixaPostal}
                >;${bean.endereco}
                >;${bean.numeroEndereco}
                >;${bean.complemento}
                >;${bean.bairro}
                >;${bean.cidade}
                >;${bean.uf}
                >;${bean.email}
                >;${bean.telefone}
                >;${bean.fax}
                >;${bean.cepCaixaPostal}
                >;${bean.cep}
            """.trimMargin(">")
                .replace("\n", ""))
                .append("\n")
        }
        try {
            val fileWriter = FileWriter(arquivo)
            fileWriter.write(text.toString())
            fileWriter.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {
        fun validaEmail(email: String): Boolean {
            var contArroba = 0
            if (email.length < 5) return false
            for (element in email) {
                if (element == '@') contArroba++
            }
            return contArroba == 1
        }

        fun verificaExistencia(
            destinatarioBean: DestinatarioBean?,
            vecDestinatarioRetorno: Vector<DestinatarioBean?>
        ): Boolean {
            for (bean in vecDestinatarioRetorno) {
                if (destinatarioBean?.numeroDestinatario == bean?.numeroDestinatario) {
                    return true
                }
            }
            return false
        }

        fun getExtension(f: File): String? {
            var ext: String? = null
            val s = f.name
            val i = s.lastIndexOf('.')
            if (i > 0 && i < s.length - 1) ext = s.substring(i + 1).lowercase(Locale.getDefault())
            return ext
        }

        fun validaArquivo(f: File): Boolean {
            if (f.isDirectory) return true
            val extension = getExtension(f)
            return if (extension != null) {
                extension == "csv" || extension == "txt"
            } else false
        }

        fun ordenarVetor(vector: Vector<*>, comparator: Comparator<*>?) {
            val arraySort = ArrayList<Any>()
            var i = 0
            while (i < vector.size) {
                arraySort.add(vector[i])
                i++
            }
            arraySort.sortWith((comparator as Comparator<in Any?>?)!!)
            vector.removeAllElements()
            i = 0
            while (i < arraySort.size) {
                vector.add(arraySort[i] as Nothing?)
                i++
            }
        }

        @Throws(EnderecadorExcecao::class)
        fun displayURL(url: String?) {
            try {
                val bl = BrowserLauncher()
                bl.openURLinBrowser(bl.browserList, url)
            } catch (ex: BrowserLaunchingInitializingException) {
                throw EnderecadorExcecao("Não foi possível abrir o browser. " + ex.message)
            } catch (ex: UnsupportedOperatingSystemException) {
                throw EnderecadorExcecao("Não foi possível abrir o browser. " + ex.message)
            }
        }
    }
}
