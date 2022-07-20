package br.com.correios.enderecador.cep;

import br.com.correios.enderecador.bean.EnderecoBean;
import br.com.correios.enderecador.dao.DaoException;
import br.com.correios.enderecador.excecao.ConfiguracaoProxyException;

public interface InterfaceEnderecoDao {
    EnderecoBean consultar(String paramString, boolean paramBoolean) throws DaoException, ConfiguracaoProxyException;
}
