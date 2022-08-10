package br.com.correios.enderecador.dao

import kotlin.Throws
import br.com.correios.enderecador.excecao.ConfiguracaoProxyException
import br.com.correios.enderecador.bean.EnderecoBean

interface InterfaceEnderecoDao {
    @Throws(DaoException::class, ConfiguracaoProxyException::class)
    fun consultar(cep: String, proxy: Boolean): EnderecoBean?
}
