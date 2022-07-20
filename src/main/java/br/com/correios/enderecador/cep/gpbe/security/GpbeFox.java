package br.com.correios.enderecador.cep.gpbe.security;

import java.util.HashMap;

public class GpbeFox {
    private final HashMap<String, String> map;

    public GpbeFox() {
        this.map = new HashMap<>();
        this.map.put("00", "AL");
        this.map.put("01", "UG");
        this.map.put("10", "CN");
        this.map.put("11", "X8");
        this.map.put("20", "EP");
        this.map.put("21", "Z0");
        this.map.put("30", "GR");
        this.map.put("31", "1B");
        this.map.put("40", "ID");
        this.map.put("41", "3D");
        this.map.put("50", "JS");
        this.map.put("51", "4C");
        this.map.put("60", "HQ");
        this.map.put("61", "2A");
        this.map.put("70", "FO");
        this.map.put("71", "09");
        this.map.put("80", "DM");
        this.map.put("81", "Y7");
        this.map.put("90", "BK");
        this.map.put("91", "V5");
    }

    public String encrypt(String string) {
        String invertido = "";
        StringBuilder result = new StringBuilder();
        String temp = "";
        for (int i = 0; i < string.trim().length(); i++) {
            if (i % 2 == 0) {
                invertido = inverte(this.map.get(string.charAt(i) + "1"));
                if (i > 3) {
                    if (result.indexOf(invertido) > 0) {
                        temp = this.map.get(string.charAt(i) + "1");
                    } else {
                        temp = invertido;
                    }
                } else {
                    temp = this.map.get(string.charAt(i) + "1");
                }
            } else if (i > 3) {
                invertido = inverte(this.map.get(string.charAt(i) + "0"));
                if (result.indexOf(invertido) > 0) {
                    temp = this.map.get(string.charAt(i) + "0");
                } else {
                    temp = invertido;
                }
            } else {
                temp = this.map.get(string.charAt(i) + "0");
            }
            result.append(temp);
        }
        return result.toString();
    }

    private String inverte(String string) {
        StringBuilder newString = new StringBuilder();
        int tamanho = string.length();
        for (int i = tamanho - 1; i >= 0; i--)
            newString.append(string.charAt(i));
        return newString.toString();
    }
}
