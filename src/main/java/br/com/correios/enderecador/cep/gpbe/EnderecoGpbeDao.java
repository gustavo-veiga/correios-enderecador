package br.com.correios.enderecador.cep.gpbe;

import br.com.correios.enderecador.bean.ConfiguracaoBean;
import br.com.correios.enderecador.bean.EnderecoBean;
import br.com.correios.enderecador.cep.CepInvalidoException;
import br.com.correios.enderecador.cep.InterfaceEnderecoDao;
import br.com.correios.enderecador.cep.gpbe.bean.CepBean;
import br.com.correios.enderecador.cep.gpbe.excecao.CepNotFoundException;
import br.com.correios.enderecador.cep.gpbe.excecao.GpbeException;
import br.com.correios.enderecador.cep.gpbe.model.GpbeBusinessService;
import br.com.correios.enderecador.dao.ConnectionException;
import br.com.correios.enderecador.dao.DaoException;
import br.com.correios.enderecador.excecao.ConfiguracaoProxyException;

public class EnderecoGpbeDao implements InterfaceEnderecoDao {
    private static EnderecoGpbeDao instance;

    public static EnderecoGpbeDao getInstance() {
        if (instance == null)
            instance = new EnderecoGpbeDao();
        return instance;
    }

    public EnderecoBean consultar(String cep, boolean proxy) throws DaoException, ConfiguracaoProxyException {
        EnderecoBean cepBean = new EnderecoBean();
        try {
            GpbeBusinessService business = GpbeBusinessService.getInstance();
            ConfiguracaoBean configuracaoBean = ConfiguracaoBean.getInstance();
            configuracaoBean.setNovaVersao(configuracaoBean.getVersao());
            cepBean.setCep(cep);
            CepBean cepGpbeBean = business.recuperaCep(cep);
            cepBean.setBairro(cepGpbeBean.getBairro());
            cepBean.setCidade(cepGpbeBean.getLocalidade());
            cepBean.setLogradouro(cepGpbeBean.getLogradouro());
            cepBean.setUf(cepGpbeBean.getUf());
        } catch (GpbeException ex) {
            throw new ConnectionException(ex.getMessage(), ex);
        } catch (CepNotFoundException ex) {
            throw new CepInvalidoException(ex.getMessage(), ex);
        }
        return cepBean;
    }
}
