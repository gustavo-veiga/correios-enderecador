package br.com.correios.enderecador.cep.gpbe

import kotlin.Throws
import br.com.correios.enderecador.dao.DaoException
import br.com.correios.enderecador.excecao.ConfiguracaoProxyException
import br.com.correios.enderecador.bean.EnderecoBean
import br.com.correios.enderecador.cep.gpbe.model.GpbeBusinessService
import br.com.correios.enderecador.bean.ConfiguracaoBean
import br.com.correios.enderecador.cep.CepInvalidoException
import br.com.correios.enderecador.cep.InterfaceEnderecoDao
import br.com.correios.enderecador.cep.gpbe.excecao.GpbeException
import br.com.correios.enderecador.dao.ConnectionException
import br.com.correios.enderecador.cep.gpbe.excecao.CepNotFoundException

class EnderecoGpbeDao : InterfaceEnderecoDao {
    @Throws(DaoException::class, ConfiguracaoProxyException::class)
    override fun consultar(cep: String, proxy: Boolean): EnderecoBean {
        val cepBean = EnderecoBean()
        try {
            val business = GpbeBusinessService.instance
            val configuracaoBean = ConfiguracaoBean.instance
            configuracaoBean!!.novaVersao = configuracaoBean.versao
            cepBean.cep = cep
            val cepGpbeBean = business!!.recuperaCep(cep)
            cepBean.bairro = cepGpbeBean.bairro
            cepBean.cidade = cepGpbeBean.localidade
            cepBean.logradouro = cepGpbeBean.logradouro
            cepBean.uf = cepGpbeBean.uf
        } catch (ex: GpbeException) {
            throw ConnectionException(ex.message, ex)
        } catch (ex: CepNotFoundException) {
            throw CepInvalidoException(ex.message, ex)
        }
        return cepBean
    }

    companion object {
        @JvmStatic
        var instance: EnderecoGpbeDao? = null
            get() {
                if (field == null) {
                    field = EnderecoGpbeDao()
                }
                return field
            }
            private set
    }
}
