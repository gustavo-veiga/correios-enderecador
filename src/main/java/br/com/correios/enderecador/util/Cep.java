package br.com.correios.enderecador.util;

public class Cep {
    public static boolean validaCep(String cep) {
        StringBuilder novoCep = new StringBuilder();
        boolean retorno = true;
        if (cep.length() < 8) {
            retorno = false;
        } else {
            for (int i = 0; i < cep.length(); i++) {
                char c = cep.charAt(i);
                if (Character.isDigit(c))
                    novoCep.append(c);
            }
            if (novoCep.length() != 8)
                retorno = false;
        }
        return retorno;
    }

    public static String tiraMascaraCep(String cep) {
        StringBuilder novoCep = new StringBuilder();
        for (int i = 0; i < cep.length(); i++) {
            char c = cep.charAt(i);
            if (Character.isDigit(c))
                novoCep.append(c);
        }
        if (novoCep.length() == 8)
            return novoCep.toString();
        return "";
    }
}
