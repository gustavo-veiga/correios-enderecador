package br.com.correios.enderecador.util;

import br.com.correios.enderecador.excecao.EnderecadorExcecao;

import java.util.Objects;

public class CodigoDeBarras128 {
    public static final int TIPO_128_AB = 1;

    public static final int TIPO_128_C = 2;

    private final String[] tabelaCode128AB = new String[]{
        "®", "!", String.valueOf('"'), "#", "$", "%", "&", "", "(", ")",
        "*", "+", ",", "-", ".", "/", "0", "1", "2", "3",
        "4", "5", "6", "7", "8", "9", ":", ";", "<", "=",
        ">", "?", "@", "A", "B", "C", "D", "E", "F", "G",
        "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
        "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "[",
        "\\", "]", "^", "_", "`", "a", "b", "c", "d", "e",
        "f", "g", "h", "i", "j", "k", "l", "m", "n", "o",
        "p", "q", "r", "s", "t", "u", "v", "x", "w", "y",
        "z", "¡", "¢", "£", "¤", "¥", "¦", "§", "¨", "©",
        "ª", "«", "¬", "{", "|", "}"};

    private final String[] tabelaCode128C = new String[]{
        "!", String.valueOf('"'), "#", "$", "%", "&", "'", "(", ")", "*",
        "+", ",", "-", ".", "/", "0", "1", "2", "3", "4",
        "5", "6", "7", "8", "9", ":", ";", "<", "=", ">",
        "?", "@", "A", "B", "C", "D", "E", "F", "G", "H",
        "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
        "S", "T", "U", "V", "W", "X", "Y", "Z", "[", "\\",
        "]", "^", "_", "`", "a", "b", "c", "d", "e", "f",
        "g", "h", "i", "j", "k", "l", "m", "n", "o", "p",
        "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
        "¡", "¢", "£", "¤", "¥", "¦", "§", "¨", "©", "ª",
        "°", "±", "²", "{", "|", "}"};

    private final String[] tabelaCode128 = new String[]{
        "2,1,2,2,2,2,", "2,2,2,1,2,2,", "2,2,2,2,2,1,", "1,2,1,2,2,3,", "1,2,1,3,2,2,", "1,3,1,2,2,2,", "1,2,2,2,1,3,", "1,2,2,3,1,2,", "1,3,2,2,1,2,", "2,2,1,2,1,3,",
        "2,2,1,3,1,2,", "2,3,1,2,1,2,", "1,1,2,2,3,2,", "1,2,2,1,3,2,", "1,2,2,2,3,1,", "1,1,3,2,2,2,", "1,2,3,1,2,2,", "1,2,3,2,2,1,", "2,2,3,2,1,1,", "2,2,1,1,3,2,",
        "2,2,1,2,3,1,", "2,1,3,2,1,2,", "2,2,3,1,1,2,", "3,1,2,1,3,1,", "3,1,1,2,2,2,", "3,2,1,1,2,2,", "3,2,1,2,2,1,", "3,1,2,2,1,2,", "3,2,2,1,1,2,", "3,2,2,2,1,1,",
        "2,1,2,1,2,3,", "2,1,2,3,2,1,", "2,3,2,1,2,1,", "1,1,1,3,2,3,", "1,3,1,1,2,3,", "1,3,1,3,2,1,", "1,1,2,3,1,3,", "1,3,2,1,1,3,", "1,3,2,3,1,1,", "2,1,1,3,1,3,",
        "2,3,1,1,1,3,", "2,3,1,3,1,1,", "1,1,2,1,3,3,", "1,1,2,3,3,1,", "1,3,2,1,3,1,", "1,1,3,1,2,3,", "1,1,3,3,2,1,", "1,3,3,1,2,1,", "3,1,3,1,2,1,", "2,1,1,3,3,1,",
        "2,3,1,1,3,1,", "2,1,3,1,1,3,", "2,1,3,3,1,1,", "2,1,3,1,3,1,", "3,1,1,1,2,3,", "3,1,1,3,2,1,", "3,3,1,1,2,1,", "3,1,2,1,1,3,", "3,1,2,3,1,1,", "3,3,2,1,1,1,",
        "3,1,4,1,1,1,", "2,2,1,4,1,1,", "4,3,1,1,1,1,", "1,1,1,2,2,4,", "1,1,1,4,2,2,", "1,2,1,1,2,4,", "1,2,1,4,2,1,", "1,4,1,1,2,2,", "1,4,1,2,2,1,", "1,1,2,2,1,4,",
        "1,1,2,4,1,2,", "1,2,2,1,1,4,", "1,2,2,4,1,1,", "1,4,2,1,1,2,", "1,4,2,2,1,1,", "2,4,1,2,1,1,", "2,2,1,1,1,4,", "4,1,3,1,1,1,", "2,4,1,1,1,2,", "1,3,4,1,1,1,",
        "1,1,1,2,4,2,", "1,2,1,1,4,2,", "1,2,1,2,4,1,", "1,1,4,2,1,2,", "1,2,4,1,1,2,", "1,2,4,2,1,1,", "4,1,1,2,1,2,", "4,2,1,1,1,2,", "4,2,1,2,1,1,", "2,1,2,1,4,1,",
        "2,1,4,1,2,1,", "4,1,2,1,2,1,", "1,1,1,1,4,3,", "1,1,1,3,4,1,", "1,3,1,1,4,1,", "1,1,4,1,1,3,", "1,1,4,3,1,1,", "4,1,1,1,1,3,", "4,1,1,3,1,1,", "1,1,3,1,4,1,",
        "1,1,4,1,3,1,", "3,1,1,1,4,1,", "4,1,1,1,3,1,", "2,1,1,4,1,2,", "2,1,1,2,1,4,", "2,1,1,2,3,2,", "2,3,3,1,1,1,2"};

    public String gerarCodigoDeBarra(int tipoCodigoDeBarras, String informacao) throws EnderecadorExcecao {
        StringBuilder codigoDeBarras = new StringBuilder();
        StringBuilder strTmp = new StringBuilder();
        String indiceTmp;
        int largura = 1;
        int indice;
        int numCaracteres;
        if (informacao == null || informacao.equals(""))
            throw new EnderecadorExcecao("A informação deve ser fornecida!");
        if (informacao.length() % 2 != 0)
            informacao = "0" + informacao;
        if (tipoCodigoDeBarras == TIPO_128_AB) {
            indice = 103;
        } else if (tipoCodigoDeBarras == TIPO_128_C) {
            indice = 105;
            if (!valorNumerico(informacao))
                throw new EnderecadorExcecao("O código de barras CODE_128C deve ser numérico!");
        } else {
            throw new EnderecadorExcecao("Tipo de código de barras inválido!");
        }
        codigoDeBarras.append(getCaractereTabela(tipoCodigoDeBarras, indice));
        if (tipoCodigoDeBarras == 2) {
            for (int j = 0; j < informacao.length(); j += 2) {
                numCaracteres = j + 2;
                if (numCaracteres > informacao.length())
                    numCaracteres = informacao.length();
                indiceTmp = informacao.substring(j, numCaracteres);
                strTmp.append(getCaractereTabela(tipoCodigoDeBarras, indiceTmp));
            }
            informacao = strTmp.toString();
        }
        for (int i = 0; i < informacao.length(); i++) {
            indice += largura * getIndiceTabela(tipoCodigoDeBarras, informacao.substring(i, i + 1));
            largura++;
        }
        indice %= 103;
        codigoDeBarras.append(informacao);
        codigoDeBarras.append(getCaractereTabela(tipoCodigoDeBarras, indice));
        codigoDeBarras.append("~");
        return codigoDeBarras.toString();
    }

    public String gerarCodigoDeBarra128(int tipoCodigoDeBarra, String CEP) {
        StringBuilder codCep = new StringBuilder();
        for (int i = 0; i < CEP.length() - 1; i++)
            codCep.append(this.tabelaCode128[getIndiceTabela(tipoCodigoDeBarra, CEP.substring(i, i + 1))]);
        codCep.append(this.tabelaCode128[this.tabelaCode128.length - 1]);
        return codCep.toString();
    }

    private String getCaractereTabela(int tipoCodigoDeBarra, String indice) {
        return getCaractereTabela(tipoCodigoDeBarra, Integer.parseInt(indice));
    }

    private String getCaractereTabela(int tipoCodigoDeBarra, int indice) {
        String result = null;
        if (tipoCodigoDeBarra == 1) {
            result = this.tabelaCode128AB[indice];
        } else if (tipoCodigoDeBarra == 2) {
            result = this.tabelaCode128C[indice];
        }
        return result;
    }

    private int getIndiceTabela(int tipoCodigoDeBarra, String caractere) {
        int indice = -1;
        String[] tabela = null;
        if (tipoCodigoDeBarra == 1) {
            tabela = this.tabelaCode128AB;
        } else if (tipoCodigoDeBarra == 2) {
            tabela = this.tabelaCode128C;
        }
        for (int i = 0; i < Objects.requireNonNull(tabela).length; i++) {
            if (tabela[i].equals(caractere)) {
                indice = i;
                break;
            }
        }
        return indice;
    }

    private boolean valorNumerico(String valor) {
        boolean isNumero = false;
        try {
            Long.parseLong(valor);
            isNumero = true;
        } catch (NumberFormatException ignored) {
        }
        return isNumero;
    }

    public static void main(String[] arg) throws EnderecadorExcecao {
        CodigoDeBarras128 codigoDeBarras128 = new CodigoDeBarras128();
        codigoDeBarras128.gerarCodigoDeBarra(2, "72110025");
    }
}
