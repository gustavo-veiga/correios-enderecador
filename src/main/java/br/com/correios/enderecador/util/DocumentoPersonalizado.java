package br.com.correios.enderecador.util;

import java.awt.Toolkit;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class DocumentoPersonalizado extends PlainDocument {
    private final int tamanhoMaximo;

    private final int tipoFormatacao;

    public DocumentoPersonalizado(int tamanhoMaximo, int tipoFormatacao) {
        this.tamanhoMaximo = tamanhoMaximo;
        this.tipoFormatacao = tipoFormatacao;
    }

    public void insertString(int offs, String novoTexto, AttributeSet a) throws BadLocationException {
        if (getLength() == this.tamanhoMaximo) {
            Toolkit.getDefaultToolkit().beep();
        } else if (novoTexto != null) {
            if (getLength() + novoTexto.length() > this.tamanhoMaximo)
                novoTexto = novoTexto.substring(0, this.tamanhoMaximo - getLength());
            if (this.tipoFormatacao == 0) {
                novoTexto = novoTexto.toUpperCase();
                super.insertString(offs, novoTexto, a);
            } else if (this.tipoFormatacao == 1) {
                String resultado = getDigitos(novoTexto);
                super.insertString(offs, resultado, a);
            } else if (this.tipoFormatacao == 5) {
                super.insertString(offs, novoTexto, a);
            } else if (this.tipoFormatacao == 2) {
                String resultado;
                if (novoTexto.length() == 10) {
                    resultado = novoTexto;
                } else {
                    resultado = getDigitos(novoTexto);
                    if (getLength() + resultado.length() == 2) {
                        resultado = resultado + "/";
                    } else if (getLength() + resultado.length() == 5) {
                        resultado = resultado + "/";
                    }
                }
                super.insertString(offs, resultado, a);
            } else if (this.tipoFormatacao == 3) {
                String resultado;
                if (novoTexto.length() == 14 || novoTexto.length() == 13) {
                    resultado = novoTexto;
                } else {
                    resultado = getDigitos(novoTexto);
                    if (getLength() + resultado.length() == 1) {
                        resultado = "(" + resultado;
                    } else if (getLength() + resultado.length() == 3) {
                        resultado = resultado + ") ";
                    } else if (getLength() + resultado.length() == 5) {
                        resultado = " " + resultado;
                    } else if (getLength() + resultado.length() == 9) {
                        resultado = "-" + resultado;
                    } else if (getLength() + resultado.length() == 14) {
                        String tel = getText(0, getLength());
                        StringBuilder novoTel = new StringBuilder();
                        char c;
                        for (int i = 0; i < tel.length(); i++) {
                            c = tel.charAt(i);
                            if (c != ' ' && c != '-') {
                                novoTel.append(c);
                                if (i == 9)
                                    novoTel.append("-");
                            }
                        }
                        resultado = novoTel + resultado;
                        offs = 0;
                        remove(0, getLength());
                    } else if (getLength() + resultado.length() > 14) {
                        remove(14, getLength());
                        resultado = "";
                    }
                }
                super.insertString(offs, resultado, a);
            } else if (this.tipoFormatacao == 4) {
                String resultado;
                if (novoTexto.length() == 10) {
                    resultado = novoTexto;
                } else {
                    resultado = getDigitos(novoTexto);
                    if (getLength() + resultado.length() == 2) {
                        resultado = resultado + ".";
                    } else if (getLength() + resultado.length() == 7) {
                        resultado = "-" + resultado;
                    }
                }
                super.insertString(offs, resultado, a);
            }
        }
    }

    private String getDigitos(String texto) {
        char c;
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < texto.length(); i++) {
            c = texto.charAt(i);
            if (Character.isDigit(c))
                resultado.append(c);
        }
        return resultado.toString();
    }
}
