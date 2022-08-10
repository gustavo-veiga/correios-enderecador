package br.com.correios.enderecador.cep.gpbe.model

import br.com.correios.enderecador.cep.gpbe.security.GpbeFox
import kotlin.Throws
import br.com.correios.enderecador.cep.gpbe.excecao.GpbeException
import java.sql.DriverManager
import br.com.correios.enderecador.cep.gpbe.excecao.CepNotFoundException
import br.com.correios.enderecador.cep.gpbe.bean.CepBean
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.lang.StringBuilder
import java.lang.Exception
import java.sql.Connection

class GpbeBusinessService private constructor() {
    private var conn: Connection? = null
    private val fox: GpbeFox
    private val urlConexao =
        "jdbc:ucanaccess:DRIVER={Microsoft Access Driver (*.mdb)};PWD=«¹½¿¥Ð²þÞ¢§³¼»;DBQ=" + System.getProperty("user.dir") + "\\..\\access\\dbgpbe.mdb"

    init {
        connection
        fox = GpbeFox()
    }

    @get:Throws(GpbeException::class)
    private val connection: Unit
        get() {
            try {
                if (conn == null) {
                    conn = DriverManager.getConnection(urlConexao)
                }
            } catch (e: Exception) {
                throw GpbeException("Não foi possível estabelecer uma conexão com o banco.")
            }
        }

    @Throws(GpbeException::class, CepNotFoundException::class)
    fun recuperaCep(cep: String?): CepBean {
        val cepBean: CepBean
        val cepFox: String? = try {
            fox.encrypt(cep)
        } catch (e: Exception) {
            throw GpbeException(e.message)
        }
        val localidade = recuperaCepLocalidade(cepFox)
        val logradouro = recuperaCepLogradouro(cepFox)
        val cepEspecial = recuperaCepEspecial(cepFox)
        val unidadeOperacional = recuperaUnidadeOperacional(cepFox)
        val caixaComunitaria = recuperaCaixaComunitaria(cepFox)
        cepBean = localidade
            ?: (logradouro ?: (cepEspecial ?: (unidadeOperacional ?: (caixaComunitaria
                ?: throw CepNotFoundException("CEP Inválido!")))))
        return cepBean
    }

    @Throws(GpbeException::class)
    private fun recuperaCepLocalidade(cep: String?): CepBean? {
        val stmt: PreparedStatement?
        val rs: ResultSet?
        var cepBean: CepBean? = null
        try {
            stmt = conn!!.prepareStatement("""
                SELECT LOC_NO AS LOCALIDADE, UFE_SG AS UF
                FROM LOG_LOCALIDADE
                WHERE LOC_KEY_DNE = '$cep'
            """.trimIndent())
            rs = stmt.executeQuery()
            while (rs.next()) {
                cepBean = CepBean()
                cepBean.localidade = rs.getString("LOCALIDADE")
                cepBean.uf = rs.getString("UF")
            }
        } catch (e: Exception) {
            throw GpbeException(e.message)
        }
        return cepBean
    }

    @Throws(GpbeException::class)
    private fun recuperaCepLogradouro(cep: String?): CepBean? {
        var stmt: PreparedStatement? = null
        var rs: ResultSet? = null
        var cepBean: CepBean? = null
        try {
            stmt = conn!!.prepareStatement("""
                SELECT LOG.LOG_NOME AS LOGRADOURO, LOG.UFE_SG AS UF,
                    BAI.BAI_NO AS BAIRRO, LOC.LOC_NO AS LOCALIDADE
                FROM LOG_LOGRADOURO LOG,   LOG_BAIRRO BAI, LOG_LOCALIDADE LOC
                WHERE (LOG.UFE_SG                = BAI.UFE_SG)
                AND (LOG.LOC_NU_SEQUENCIAL     = BAI.LOC_NU_SEQUENCIAL)
                AND (LOG.BAI_NU_SEQUENCIAL_INI = BAI.BAI_NU_SEQUENCIAL
                AND (LOG.UFE_SG                = LOC.UFE_SG)
                AND (LOG.LOC_NU_SEQUENCIAL     = LOC.LOC_NU_SEQUENCIAL)
                AND (LOG.LOG_KEY_DNE = '$cep')
            """.trimIndent())
            rs = stmt.executeQuery()
            while (rs.next()) {
                cepBean = CepBean()
                cepBean.bairro = rs.getString("BAIRRO")
                cepBean.logradouro = rs.getString("LOGRADOURO")
                cepBean.localidade = rs.getString("LOCALIDADE")
                cepBean.uf = rs.getString("UF")
            }
        } catch (e: Exception) {
            throw GpbeException(e.message)
        }
        return cepBean
    }

    @Throws(GpbeException::class)
    private fun recuperaCepEspecial(cep: String?): CepBean? {
        var stmt: PreparedStatement? = null
        var rs: ResultSet? = null
        val sql = StringBuilder()
        var cepBean: CepBean? = null
        try {
            sql.append(" SELECT LGU.GRU_ENDERECO AS LOGRADOURO, LL.LOC_NO AS LOCALIDADE, ")
            sql.append("        LB.BAI_NO AS BAIRRO, LGU.UFE_SG AS UF ")
            sql.append("   FROM LOG_GRANDE_USUARIO LGU, LOG_BAIRRO LB, LOG_LOCALIDADE LL ")
            sql.append("  WHERE (LGU.UFE_SG                = LB.UFE_SG) ")
            sql.append("    AND (LGU.LOC_NU_SEQUENCIAL     = LB.LOC_NU_SEQUENCIAL) ")
            sql.append("    AND (LGU.BAI_NU_SEQUENCIAL     = LB.BAI_NU_SEQUENCIAL) ")
            sql.append("    AND (LGU.UFE_SG                = LL.UFE_SG) ")
            sql.append("    AND (LGU.LOC_NU_SEQUENCIAL     = LL.LOC_NU_SEQUENCIAL) ")
            sql.append("    AND (LGU.GRU_KEY_DNE = ")
            sql.append("'").append(cep).append("')")
            stmt = conn!!.prepareStatement(sql.toString())
            rs = stmt.executeQuery()
            while (rs.next()) {
                cepBean = CepBean()
                cepBean.bairro = rs.getString("BAIRRO")
                cepBean.logradouro = rs.getString("LOGRADOURO")
                cepBean.localidade = rs.getString("LOCALIDADE")
                cepBean.uf = rs.getString("UF")
            }
        } catch (e: Exception) {
            throw GpbeException(e.message)
        }
        return cepBean
    }

    @Throws(GpbeException::class)
    private fun recuperaUnidadeOperacional(cep: String?): CepBean? {
        var stmt: PreparedStatement? = null
        var rs: ResultSet? = null
        val sql = StringBuilder()
        var cepBean: CepBean? = null
        try {
            sql.append(" SELECT LUOP.UOP_ENDERECO AS LOGRADOURO, LL.LOC_NO AS LOCALIDADE, ")
            sql.append("        LB.BAI_NO AS BAIRRO, LUOP.UFE_SG AS UF ")
            sql.append("   FROM Log_Unid_Oper LUOP, LOG_BAIRRO LB, LOG_LOCALIDADE LL ")
            sql.append("  WHERE  (LUOP.UFE_SG                = LB.UFE_SG) ")
            sql.append("    AND (LUOP.LOC_NU_SEQUENCIAL     = LB.LOC_NU_SEQUENCIAL) ")
            sql.append("    AND (LUOP.BAI_NU_SEQUENCIAL     = LB.BAI_NU_SEQUENCIAL) ")
            sql.append("    AND (LUOP.UFE_SG                = LL.UFE_SG) ")
            sql.append("    AND (LUOP.LOC_NU_SEQUENCIAL     = LL.LOC_NU_SEQUENCIAL) ")
            sql.append("    AND (LUOP.UOP_KEY_DNE = ")
            sql.append("'").append(cep).append("')")
            stmt = conn!!.prepareStatement(sql.toString())
            rs = stmt.executeQuery()
            while (rs.next()) {
                cepBean = CepBean()
                cepBean.bairro = rs.getString("BAIRRO")
                cepBean.logradouro = rs.getString("LOGRADOURO")
                cepBean.localidade = rs.getString("LOCALIDADE")
                cepBean.uf = rs.getString("UF")
            }
        } catch (e: Exception) {
            throw GpbeException(e.message)
        }
        return cepBean
    }

    @Throws(GpbeException::class)
    private fun recuperaCaixaComunitaria(cep: String?): CepBean? {
        var stmt: PreparedStatement? = null
        var rs: ResultSet? = null
        val sql = StringBuilder()
        var cepBean: CepBean? = null
        try {
            sql.append(" SELECT CPC.CPC_ENDERECO AS LOGRADOURO, LOC.LOC_NO AS LOCALIDADE, ")
            sql.append("        CPC.UFE_SG AS UF ")
            sql.append("   FROM LOG_CPC CPC, LOG_LOCALIDADE LOC ")
            sql.append("  WHERE (CPC.UFE_SG = LOC.UFE_SG) ")
            sql.append("    AND (CPC.LOC_NU_SEQUENCIAL = LOC.LOC_NU_SEQUENCIAL) ")
            sql.append("     AND (CPC.CPC_KEY_DNE = ")
            sql.append("'").append(cep).append("')")
            stmt = conn!!.prepareStatement(sql.toString())
            rs = stmt.executeQuery()
            while (rs.next()) {
                cepBean = CepBean()
                cepBean.logradouro = rs.getString("LOGRADOURO")
                cepBean.localidade = rs.getString("LOCALIDADE")
                cepBean.uf = rs.getString("UF")
            }
        } catch (e: Exception) {
            throw GpbeException(e.message)
        }
        return cepBean
    }

    companion object {
        private var business: GpbeBusinessService? = null

        @JvmStatic
        @get:Throws(GpbeException::class)
        val instance: GpbeBusinessService?
            get() {
                try {
                    if (business == null) business = GpbeBusinessService()
                } catch (e: GpbeException) {
                    throw GpbeException("Não foi possível instanciar objeto")
                }
                return business
            }
    }
}