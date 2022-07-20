package br.com.correios.enderecador.util;

import br.com.correios.enderecador.bean.ConfiguracaoBean;
import br.com.correios.enderecador.bean.DeclaracaoBean;
import br.com.correios.enderecador.bean.DestinatarioBean;
import br.com.correios.enderecador.bean.ImpressaoBean;
import br.com.correios.enderecador.bean.RemetenteBean;
import br.com.correios.enderecador.excecao.EnderecadorExcecao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.*;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.log4j.Logger;
import org.krysalis.barcode4j.BarcodeDimension;
import org.krysalis.barcode4j.impl.datamatrix.DataMatrixBean;
import org.krysalis.barcode4j.impl.datamatrix.SymbolShapeHint;
import org.krysalis.barcode4j.output.CanvasProvider;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;

public class Impressao {
    private final int TAMANHO_CAMPO_ENTREGA_VIZINHO = 40;

    private DestinatarioBean destinatarioBean;

    private ImpressaoBean impressaoBean;

    private final CodigoDeBarras128 codBarras = new CodigoDeBarras128();

    static Logger logger = Logger.getLogger(Impressao.class);

    private String calculaDVCep(String cep) {
        int DV = 0;
        int somatorio = 0;
        int i;
        for (i = 0; i < cep.length(); i++)
            somatorio += Integer.parseInt(cep.substring(i, i + 1));
        if (somatorio % 10 == 0)
            return "0";
        for (i = 0; i < 10; i++) {
            if ((somatorio + i) % 10 == 0) {
                DV = i;
                break;
            }
        }
        return Integer.toString(DV);
    }

    private String pontoEntrega(String numeroLote) {
        String pontoEntregaDefault = "00000";
        if (numeroLote.trim().equals("") || numeroLote.trim().length() > 5 || !numeroLote.matches("^[0-9]{1,5}$"))
            return pontoEntregaDefault;
        if (pontoEntregaDefault.length() == numeroLote.length())
            return numeroLote;
        return pontoEntregaDefault.substring(0, pontoEntregaDefault.length() - numeroLote.length()) + numeroLote;
    }

    public void impressaoEncomenda(String relatorio, RemetenteBean remetente, Vector<DestinatarioBean> destinatario, int posInicial, boolean telRemetente, boolean telDestinatario) throws EnderecadorExcecao {
        ArrayList<ImpressaoBean> impressao = new ArrayList<>();
        HashMap<Object, Object> parametros = new HashMap<>();
        for (int k = 1; k < posInicial; k++) {
            this.impressaoBean = new ImpressaoBean();
            impressao.add(this.impressaoBean);
        }
        if (destinatario != null)
            for (DestinatarioBean bean : destinatario) {
                this.impressaoBean = new ImpressaoBean();
                this.destinatarioBean = bean;
                if (this.destinatarioBean.getQuantidade().equals(""))
                    this.destinatarioBean.setQuantidade("0");
                for (int j = 0; j < Integer.parseInt(this.destinatarioBean.getQuantidade()); j++) {
                    this.impressaoBean.setDes_campo1(this.destinatarioBean.getNome());
                    this.impressaoBean.setDes_campo2(this.destinatarioBean.getApelido());
                    if (this.destinatarioBean.getEndereco().length() < 24) {
                        this.impressaoBean.setDes_campo3(this.destinatarioBean.getEndereco() + " " + this.destinatarioBean.getNumeroEndereco());
                        this.impressaoBean.setDes_campo4(this.destinatarioBean.getComplemento());
                    } else {
                        this.impressaoBean.setDes_campo3(this.destinatarioBean.getEndereco());
                        this.impressaoBean.setDes_campo4(this.destinatarioBean.getNumeroEndereco() + ", " + this.destinatarioBean.getComplemento());
                    }
                    this.impressaoBean.setDes_campo5(this.destinatarioBean.getBairro());
                    this.impressaoBean.setDes_campo6(this.destinatarioBean.getCep().substring(0, 5) + "-" + this.destinatarioBean.getCep().substring(5, 8));
                    this.impressaoBean.setDes_campo7(this.destinatarioBean.getCidade() + "-" + this.destinatarioBean.getUf());
                    String entrega = this.destinatarioBean.getDesEntregaVizinho();
                    if (entrega.length() > 40)
                        entrega = entrega.substring(0, 40);
                    if (entrega.isEmpty())
                        entrega = "entrega no vizinho não autorizada";
                    this.impressaoBean.setDes_entrega(entrega);
                    String barCode = this.codBarras.gerarCodigoDeBarra128(2, this.codBarras.gerarCodigoDeBarra(2, this.destinatarioBean.getCep())).replaceAll(",", "");
                    this.impressaoBean.setDes_codBarras(barCode);
                    this.impressaoBean.setDes_cep(this.destinatarioBean.getCep().substring(0, 5) + "-" + this.destinatarioBean.getCep().substring(5, 8));
                    String cepRemetente = "0000000000000";
                    if (remetente != null) {
                        this.impressaoBean.setRem_campo1(remetente.getTitulo() + " " + remetente.getNome());
                        this.impressaoBean.setRem_campo2(remetente.getApelido());
                        this.impressaoBean.setRem_campo3(remetente.getEndereco() + " " + remetente.getNumeroEndereco());
                        this.impressaoBean.setRem_campo4(remetente.getComplemento());
                        this.impressaoBean.setRem_campo5(remetente.getBairro());
                        this.impressaoBean.setRem_campo6(remetente.getCep().substring(0, 5) + "-" + remetente.getCep().substring(5, 8));
                        this.impressaoBean.setRem_campo7(remetente.getCidade() + "-" + remetente.getUf());
                        cepRemetente = remetente.getCep() + pontoEntrega(remetente.getNumeroEndereco());
                    }
                    this.impressaoBean.setCodigoDoisD(this.destinatarioBean.getCep() + pontoEntrega(this.destinatarioBean.getNumeroEndereco()));
                    this.impressaoBean.setCodigoDoisD(this.impressaoBean.getCodigoDoisD() + cepRemetente);
                    this.impressaoBean.setCodigoDoisD(this.impressaoBean.getCodigoDoisD() + calculaDVCep(this.destinatarioBean.getCep()));
                    this.impressaoBean.setCodigoDoisD(this.impressaoBean.getCodigoDoisD() + "00");
                    this.impressaoBean.setCodigoDoisD(this.impressaoBean.getCodigoDoisD() + "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
                    createDatamatrix(this.impressaoBean.getCodigoDoisD(), ConfiguracaoBean.getInstance().getCaminhoImagem2d().toString() + "\\" + this.impressaoBean.getCodigoDoisD() + ".png");
                    impressao.add(this.impressaoBean);
                }
            }
        String path = (new File("")).getAbsolutePath();
        parametros.put("caminhoImagem", Objects.requireNonNull(getClass().getResource(ConfiguracaoBean.getInstance().getCaminhoImagem())).toString());
        parametros.put("caminhoImagem2D", path);
        try {
            visualizarRelatorioJasper(ConfiguracaoBean.getInstance().getCaminhoRelatorio() + relatorio, parametros, impressao);
            limparDataMatrix(impressao);
        } catch (JRException ex) {
            logger.error(ex.getMessage());
            throw new EnderecadorExcecao(ex.getMessage());
        }
    }

    public void impressaoCarta(String relatorio, RemetenteBean remetente, Vector<DestinatarioBean> destinatario, int qtdRemetente, int posInicial, boolean telRemetente, boolean telDestinatario, boolean imprimirTratamento, String tamanhoFonte) throws EnderecadorExcecao {
        ArrayList<ImpressaoBean> impressao = new ArrayList<>();
        HashMap<Object, Object> parametros = new HashMap<>();
        for (int k = 1; k < posInicial; k++) {
            this.impressaoBean = new ImpressaoBean();
            impressao.add(this.impressaoBean);
        }
        String cepRemetenteComplemento = "0000000000000";
        if (remetente != null)
            for (int i = 0; i < qtdRemetente; i++) {
                this.impressaoBean = new ImpressaoBean();
                if (imprimirTratamento)
                    this.impressaoBean.setDes_campo1(remetente.getTitulo());
                this.impressaoBean.setDes_campo2(remetente.getNome());
                this.impressaoBean.setDes_campo3(remetente.getApelido());
                this.impressaoBean.setDes_campo4(remetente.getEndereco() + " " + remetente.getNumeroEndereco());
                if (telDestinatario) {
                    this.impressaoBean.setDes_campo5(remetente.getComplemento() + " - Telefone: " + remetente.getTelefone());
                } else {
                    this.impressaoBean.setDes_campo5(remetente.getComplemento());
                }
                this.impressaoBean.setDes_campo6(remetente.getBairro());
                this.impressaoBean.setDes_campo7(remetente.getCep().substring(0, 5) + "-" + remetente.getCep().substring(5, 8) + "   " + remetente.getCidade() + "-" + remetente.getUf());
                cepRemetenteComplemento = remetente.getCep() + pontoEntrega(remetente.getNumeroEndereco());
                this.impressaoBean.organizaCampos();
                impressao.add(this.impressaoBean);
            }
        String cepDestinatarioComplemento = "0000000000000";
        if (destinatario != null) {
            for (DestinatarioBean bean : destinatario) {
                this.destinatarioBean = bean;
                for (int l = 0; l < Integer.parseInt(this.destinatarioBean.getQuantidade());
                     l++) {
                    this.impressaoBean = new ImpressaoBean();
                    if (imprimirTratamento)
                        this.impressaoBean.setDes_campo1(this.destinatarioBean.getTitulo());
                    this.impressaoBean.setDes_campo2(this.destinatarioBean.getNome());
                    this.impressaoBean.setDes_campo3(this.destinatarioBean.getApelido());
                    this.impressaoBean.setDes_campo4(this.destinatarioBean.getEndereco() + " " + this.destinatarioBean.getNumeroEndereco());
                    if (telDestinatario) {
                        this.impressaoBean.setDes_campo5(this.destinatarioBean.getComplemento() + " - Telefone: " + this.destinatarioBean.getTelefone());
                    } else {
                        this.impressaoBean.setDes_campo5(this.destinatarioBean.getComplemento());
                    }
                    this.impressaoBean.setDes_campo6(this.destinatarioBean.getBairro());
                    this.impressaoBean.setDes_campo7(this.destinatarioBean.getCep().substring(0, 5) + "-" + this.destinatarioBean.getCep().substring(5, 8) + "   " + this.destinatarioBean.getCidade() + "-" + this.destinatarioBean.getUf());
                    cepDestinatarioComplemento = this.destinatarioBean.getCep() + pontoEntrega(this.destinatarioBean.getNumeroEndereco());
                    this.impressaoBean.setDes_cep(this.destinatarioBean.getCep() + calculaDVCep(this.destinatarioBean.getCep()));
                    this.impressaoBean.organizaCampos();
                    impressao.add(this.impressaoBean);
                }
            }
            this.impressaoBean.setCodigoDoisD(cepDestinatarioComplemento);
            this.impressaoBean.setCodigoDoisD(this.impressaoBean.getCodigoDoisD() + cepRemetenteComplemento);
            this.impressaoBean.setCodigoDoisD(this.impressaoBean.getCodigoDoisD() + calculaDVCep(this.destinatarioBean.getCep()));
            this.impressaoBean.setCodigoDoisD(this.impressaoBean.getCodigoDoisD() + "00");
            this.impressaoBean.setCodigoDoisD(this.impressaoBean.getCodigoDoisD() + "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
            createDatamatrix(this.impressaoBean.getCodigoDoisD(), ConfiguracaoBean.getInstance().getCaminhoImagem2d().toString() + "\\" + this.impressaoBean.getCodigoDoisD() + ".png");
        }
        parametros.put("caminhoImagem", Objects.requireNonNull(getClass().getResource(ConfiguracaoBean.getInstance().getCaminhoImagem())).toString());
        parametros.put("tamanhoFonte", tamanhoFonte);
        parametros.put("caminhoImagem2D", ConfiguracaoBean.getInstance().getCaminhoImagem2d());
        try {
            visualizarRelatorioJasper(ConfiguracaoBean.getInstance().getCaminhoRelatorio() + relatorio, parametros, impressao);
            limparDataMatrix(impressao);
        } catch (JRException ex) {
            logger.error(ex.getMessage(), (Throwable) ex);
            throw new EnderecadorExcecao(ex.getMessage());
        }
    }

    public void visualizarRelatorioJasper(String relatorio, Map parametros, List lista) throws JRException {
        InputStream in = getClass().getClassLoader().getResourceAsStream(relatorio);
        JasperPrint impressao = JasperFillManager.fillReport(in, parametros, (JRDataSource) new JRBeanCollectionDataSource(lista));
        JasperViewer viewer = new JasperViewer(impressao, false);
        viewer.setVisible(true);
    }

    public void visualizarRelatorioJrxml(String relatorio, Map parametros, List lista) throws JRException {
        InputStream in = getClass().getClassLoader().getResourceAsStream(relatorio + ".jrxml");
        JasperDesign desenho = JRXmlLoader.load(in);
        JasperReport report = JasperCompileManager.compileReport(desenho);
        JasperPrint impressao = JasperFillManager.fillReport(report, parametros, (JRDataSource) new JRBeanCollectionDataSource(lista));
        JasperViewer viewer = new JasperViewer(impressao, false);
        viewer.setVisible(true);
    }

    public void imprimirEnvelope(String relatorio, RemetenteBean remetente, Vector<DestinatarioBean> destinatario, boolean telefone, String tamanhoFonte) throws EnderecadorExcecao {
        ArrayList<ImpressaoBean> impressao = new ArrayList<>();
        HashMap<Object, Object> parametros = new HashMap<>();
        for (DestinatarioBean bean : destinatario) {
            this.destinatarioBean = bean;
            String cepDestinatarioComplemento;
            for (int j = 0; j < Integer.parseInt(this.destinatarioBean.getQuantidade()); j++) {
                this.impressaoBean = new ImpressaoBean();
                this.impressaoBean.setDes_campo1(this.destinatarioBean.getNome().toUpperCase());
                this.impressaoBean.setDes_campo2(this.destinatarioBean.getApelido().toUpperCase());
                this.impressaoBean.setDes_campo3(this.destinatarioBean.getEndereco().toUpperCase() + " " + this.destinatarioBean.getNumeroEndereco());
                this.impressaoBean.setDes_campo4(this.destinatarioBean.getComplemento().toUpperCase());
                this.impressaoBean.setDes_campo5(this.destinatarioBean.getBairro().toUpperCase());
                this.impressaoBean.setDes_campo6(this.destinatarioBean.getCep().substring(0, 5) + "-" + this.destinatarioBean.getCep().substring(5, 8) + "   " + this.destinatarioBean.getCidade() + "-" + this.destinatarioBean.getUf());
                if (telefone)
                    this.impressaoBean.setDes_campo7(this.destinatarioBean.getTelefone().toUpperCase());
                this.impressaoBean.setDes_cep(this.destinatarioBean.getCep() + calculaDVCep(this.destinatarioBean.getCep()));
                cepDestinatarioComplemento = this.destinatarioBean.getCep() + pontoEntrega(this.destinatarioBean.getNumeroEndereco());
                String cepRemetenteComplemento = "0000000000000";
                if (remetente != null) {
                    this.impressaoBean.setRem_campo1(remetente.getTitulo());
                    this.impressaoBean.setRem_campo2(remetente.getNome());
                    this.impressaoBean.setRem_campo3(remetente.getApelido());
                    this.impressaoBean.setRem_campo4(remetente.getEndereco() + " " + remetente.getNumeroEndereco());
                    this.impressaoBean.setRem_campo5(remetente.getComplemento());
                    this.impressaoBean.setRem_campo6(remetente.getBairro());
                    this.impressaoBean.setRem_campo7(remetente.getCep().substring(0, 5) + "-" + remetente.getCep().substring(5, 8) + "   " + remetente.getCidade() + "-" + remetente.getUf());
                    cepRemetenteComplemento = remetente.getCep() + pontoEntrega(remetente.getNumeroEndereco());
                }
                this.impressaoBean.setCodigoDoisD(cepDestinatarioComplemento);
                this.impressaoBean.setCodigoDoisD(this.impressaoBean.getCodigoDoisD() + cepRemetenteComplemento);
                this.impressaoBean.setCodigoDoisD(this.impressaoBean.getCodigoDoisD() + calculaDVCep(this.destinatarioBean.getCep()));
                this.impressaoBean.setCodigoDoisD(this.impressaoBean.getCodigoDoisD() + "00");
                this.impressaoBean.setCodigoDoisD(this.impressaoBean.getCodigoDoisD() + "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
                createDatamatrix(this.impressaoBean.getCodigoDoisD(), ConfiguracaoBean.getInstance().getCaminhoImagem2d().toString() + "\\" + this.impressaoBean.getCodigoDoisD() + ".png");
                this.impressaoBean.organizaCampos();
                impressao.add(this.impressaoBean);
            }
        }
        parametros.put("tamanhoFonte", tamanhoFonte);
        parametros.put("caminhoImagem", Objects.requireNonNull(getClass().getResource(ConfiguracaoBean.getInstance().getCaminhoImagem())).toString());
        parametros.put("caminhoImagem2D", ConfiguracaoBean.getInstance().getCaminhoImagem2d());
        try {
            visualizarRelatorioJasper(ConfiguracaoBean.getInstance().getCaminhoRelatorio() + relatorio, parametros, impressao);
            limparDataMatrix(impressao);
        } catch (JRException ex) {
            logger.error(ex.getMessage(), (Throwable) ex);
            throw new EnderecadorExcecao(ex.getMessage());
        }
    }

    public void imprimirAR(String relatorio, RemetenteBean rementente, Vector<DestinatarioBean> destinatario) throws EnderecadorExcecao {
        ArrayList<ImpressaoBean> impressao = new ArrayList<>();
        HashMap<Object, Object> parametros = new HashMap<>();
        for (DestinatarioBean bean : destinatario) {
            this.impressaoBean = new ImpressaoBean();
            this.destinatarioBean = bean;
            if (this.destinatarioBean.getQuantidade().equals(""))
                this.destinatarioBean.setQuantidade("0");
            for (int j = 0; j < Integer.parseInt(this.destinatarioBean.getQuantidade()); j++) {
                this.impressaoBean.setDes_campo1(this.destinatarioBean.getNome());
                this.impressaoBean.setDes_campo2(this.destinatarioBean.getApelido());
                this.impressaoBean.setDes_campo3(this.destinatarioBean.getEndereco() + " " + this.destinatarioBean.getNumeroEndereco());
                this.impressaoBean.setDes_campo4(this.destinatarioBean.getComplemento() + " " + this.destinatarioBean.getBairro());
                this.impressaoBean.setDes_campo5(this.destinatarioBean.getCep().substring(0, 5) + "-" + this.destinatarioBean.getCep().substring(5, 8) + "   " + this.destinatarioBean.getCidade() + "-" + this.destinatarioBean.getUf());
                this.impressaoBean.setDesMaoPropria(this.destinatarioBean.getMaoPropria());
                this.impressaoBean.setDesConteudo(this.destinatarioBean.getDesConteudo());
                this.impressaoBean.setRem_campo1(rementente.getNome());
                this.impressaoBean.setRem_campo2(rementente.getApelido());
                this.impressaoBean.setRem_campo3(rementente.getEndereco() + " " + rementente.getNumeroEndereco());
                this.impressaoBean.setRem_campo4(rementente.getComplemento() + " " + rementente.getBairro());
                this.impressaoBean.setRem_campo5(rementente.getCep().substring(0, 5) + "-" + rementente.getCep().substring(5, 8) + "   " + rementente.getCidade() + "-" + rementente.getUf());
                this.impressaoBean.organizaCampos();
                impressao.add(this.impressaoBean);
            }
        }
        parametros.put("caminhoImagem", Objects.requireNonNull(getClass().getResource(ConfiguracaoBean.getInstance().getCaminhoImagem())).toString());
        parametros.put("caminhoImagem2D", ConfiguracaoBean.getInstance().getCaminhoImagem2d());
        try {
            visualizarRelatorioJasper(ConfiguracaoBean.getInstance().getCaminhoRelatorio() + relatorio, parametros, impressao);
        } catch (JRException ex) {
            logger.error(ex.getMessage(), ex);
            throw new EnderecadorExcecao(ex.getMessage());
        }
    }

    public void limparDataMatrix(ArrayList<ImpressaoBean> impressao) {
        for (ImpressaoBean bean : impressao) {
            this.impressaoBean = bean;
            String nome = ConfiguracaoBean.getInstance().getCaminhoImagem2d() + "\\" + this.impressaoBean.getCodigoDoisD() + ".png";
            File f = new File(nome);
            f.delete();
        }
    }

    public String createDatamatrix(String testo, String fileoutputpath) {
        try {
            DataMatrixBean bean = new DataMatrixBean();
            BarcodeDimension bd = new BarcodeDimension(300.0D, 300.0D);
            int dpi = 72;
            bean.setHeight(dpi);
            bean.setShape(SymbolShapeHint.FORCE_SQUARE);
            bean.setModuleWidth(bd.getWidth() / 72.0D);
            bean.setQuietZone(bd.getXOffset());
            bean.doQuietZone(true);
            File outputFile = new File(fileoutputpath.replace("file:/", ""));
            OutputStream out = new FileOutputStream(outputFile);
            try {
                BitmapCanvasProvider canvas = new BitmapCanvasProvider(out, "image/x-png", dpi, 12, false, 0);
                bean.generateBarcode(canvas, testo);
                canvas.finish();
            } finally {
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void imprimirDeclaracao(String relatorio, Vector<Vector> itens, RemetenteBean remetenteBean, Vector<DestinatarioBean> destinatarios, String pesoTotal) throws EnderecadorExcecao {
        Map<Object, Object> parametros = new HashMap<>();
        ArrayList<DeclaracaoBean> lista = new ArrayList<>();
        NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance();
        for (DestinatarioBean destinatario : destinatarios) {
            this.destinatarioBean = destinatario;
            DeclaracaoBean declaracaoBean = new DeclaracaoBean(remetenteBean.getNome(), remetenteBean.getEndereco() + " " + remetenteBean.getNumeroEndereco(), remetenteBean.getComplemento() + " " + remetenteBean.getBairro(), remetenteBean.getCidade(), remetenteBean.getUf(), remetenteBean.getCep().substring(0, 5) + "-" + remetenteBean.getCep().substring(5, 8), " ", this.destinatarioBean.getNome(), this.destinatarioBean.getEndereco() + " " + this.destinatarioBean.getNumeroEndereco(), this.destinatarioBean.getComplemento() + " " + this.destinatarioBean.getBairro(), this.destinatarioBean.getCidade(), this.destinatarioBean.getUf(), this.destinatarioBean.getCep().substring(0, 5) + "-" + this.destinatarioBean.getCep().substring(5, 8), " ");
            Integer quantidadeTotal = 0;
            Double valorTotal = 0.0D;
            for (int j = 0; j < 6; j++) {
                String conteudo = "";
                Integer quantidade = 0;
                Double valor = 0.0D;
                if (j < itens.size()) {
                    conteudo = ((Vector<String>) itens.get(j)).get(0);
                    if (conteudo == null)
                        conteudo = "";
                    quantidade = convertToInteger(((Vector<String>) itens.get(j)).get(1));
                    if (quantidade == null)
                        quantidade = 0;
                    String temp = ((Vector<String>) itens.get(j)).get(2);
                    if (temp != null) {
                        temp = temp.replaceAll("[\\s,]", ".");
                        valor = convertToDouble(temp);
                    } else {
                        valor = Double.valueOf(0.0D);
                    }
                }
                String numero = String.valueOf(j + 1);
                switch (j) {
                    case 0:
                        declaracaoBean.setNumeroItem1(numero);
                        if (conteudo.isEmpty())
                            declaracaoBean.setNumeroItem1("");
                        declaracaoBean.setConteudoItem1(conteudo);
                        declaracaoBean.setQuantidadeItem1(String.valueOf(quantidade));
                        if (quantidade.equals(0))
                            declaracaoBean.setQuantidadeItem1("");
                        declaracaoBean.setValorItem1(formatoMoeda.format(valor));
                        if (valor.equals(0.0D))
                            declaracaoBean.setValorItem1("");
                        break;
                    case 1:
                        declaracaoBean.setNumeroItem2(numero);
                        if (conteudo.isEmpty())
                            declaracaoBean.setNumeroItem2("");
                        declaracaoBean.setConteudoItem2(conteudo);
                        declaracaoBean.setQuantidadeItem2(String.valueOf(quantidade));
                        if (quantidade.equals(0))
                            declaracaoBean.setQuantidadeItem2("");
                        declaracaoBean.setValorItem2(formatoMoeda.format(valor));
                        if (valor.equals(0.0D))
                            declaracaoBean.setValorItem2("");
                        break;
                    case 2:
                        declaracaoBean.setNumeroItem3(numero);
                        if (conteudo.isEmpty())
                            declaracaoBean.setNumeroItem3("");
                        declaracaoBean.setConteudoItem3(conteudo);
                        declaracaoBean.setQuantidadeItem3(String.valueOf(quantidade));
                        if (quantidade.equals(0))
                            declaracaoBean.setQuantidadeItem3("");
                        declaracaoBean.setValorItem3(formatoMoeda.format(valor));
                        if (valor.equals(0.0D))
                            declaracaoBean.setValorItem3("");
                        break;
                    case 3:
                        declaracaoBean.setNumeroItem4(numero);
                        if (conteudo.isEmpty())
                            declaracaoBean.setNumeroItem4("");
                        declaracaoBean.setConteudoItem4(conteudo);
                        declaracaoBean.setQuantidadeItem4(String.valueOf(quantidade));
                        if (quantidade.equals(0))
                            declaracaoBean.setQuantidadeItem4("");
                        declaracaoBean.setValorItem4(formatoMoeda.format(valor));
                        if (valor.equals(0.0D))
                            declaracaoBean.setValorItem4("");
                        break;
                    case 4:
                        declaracaoBean.setNumeroItem5(numero);
                        if (conteudo.isEmpty())
                            declaracaoBean.setNumeroItem5("");
                        declaracaoBean.setConteudoItem5(conteudo);
                        declaracaoBean.setQuantidadeItem5(String.valueOf(quantidade));
                        if (quantidade.equals(0))
                            declaracaoBean.setQuantidadeItem5("");
                        declaracaoBean.setValorItem5(formatoMoeda.format(valor));
                        if (valor.equals(0.0D))
                            declaracaoBean.setValorItem5("");
                        break;
                    case 5:
                        declaracaoBean.setNumeroItem6(numero);
                        if (conteudo.isEmpty())
                            declaracaoBean.setNumeroItem6("");
                        declaracaoBean.setConteudoItem6(conteudo);
                        declaracaoBean.setQuantidadeItem6(String.valueOf(quantidade));
                        if (quantidade.equals(0))
                            declaracaoBean.setQuantidadeItem6("");
                        declaracaoBean.setValorItem6(formatoMoeda.format(valor));
                        if (valor.equals(0.0D))
                            declaracaoBean.setValorItem6("");
                        break;
                }
                quantidadeTotal = quantidadeTotal + quantidade;
                valorTotal = valorTotal + valor;
            }
            declaracaoBean.setQuantidadeTotal(String.valueOf(quantidadeTotal));
            declaracaoBean.setValorTotal(formatoMoeda.format(valorTotal));
            declaracaoBean.setPesoTotal(pesoTotal);
            lista.add(declaracaoBean);
        }
        try {
            visualizarRelatorioJasper(ConfiguracaoBean.getInstance().getCaminhoRelatorio() + relatorio + ".jasper", parametros, lista);
        } catch (JRException ex) {
            logger.error(ex.getMessage(), ex);
            throw new EnderecadorExcecao(ex.getMessage());
        }
    }

    private Integer convertToInteger(String valor) {
        try {
            return Integer.valueOf(valor);
        } catch (NullPointerException | IllegalArgumentException e) {
            return 0;
        }
    }

    private Double convertToDouble(String valor) {
        try {
            return Double.valueOf(valor);
        } catch (NullPointerException | IllegalArgumentException e) {
            return 0.0D;
        }
    }
}
