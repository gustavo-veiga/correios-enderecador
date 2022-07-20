package br.com.correios.enderecador.util;

import br.com.correios.enderecador.bean.DestinatarioBean;
import br.com.correios.enderecador.excecao.EnderecadorExcecao;
import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

public class Geral {
    public static boolean validaEmail(String email) {
        int contArroba = 0;
        if (email.length() < 5)
            return false;
        for (int i = 0; i < email.length(); i++) {
            if (email.charAt(i) == '@')
                contArroba++;
        }
        return contArroba == 1;
    }

    public static boolean verificaExistencia(DestinatarioBean destinatarioBean, Vector<DestinatarioBean> vecDestinatarioRetorno) {
        for (DestinatarioBean bean : vecDestinatarioRetorno) {
            if (destinatarioBean.getNumeroDestinatario().equals(bean.getNumeroDestinatario()))
                return true;
        }
        return false;
    }

    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1)
            ext = s.substring(i + 1).toLowerCase();
        return ext;
    }

    public static boolean validaArquivo(File f) {
        if (f.isDirectory())
            return true;
        String extension = getExtension(f);
        if (extension != null) {
            return extension.equals("csv") || extension.equals("txt");
        }
        return false;
    }

    public static void ordenarVetor(Vector vector, Comparator<?> comparator) {
        ArrayList<Object> arraySort = new ArrayList<>();
        int i;
        for (i = 0; i < vector.size(); i++)
            arraySort.add(vector.get(i));
        arraySort.sort((Comparator<? super Object>) comparator);
        vector.removeAllElements();
        for (i = 0; i < arraySort.size(); i++)
            vector.add(arraySort.get(i));
    }

    public void exportarParaTXT(String arquivo, Vector<DestinatarioBean> destinatario) throws EnderecadorExcecao {
        String texto = "";
        FileWriter fileWriter = null;
        for (int i = 0; i < destinatario.size(); i++) {
            DestinatarioBean destinatarioBean = DestinatarioBean.getInstance();
            destinatarioBean = destinatario.get(i);
            texto = texto + destinatarioBean.getTitulo();
            texto = texto + ";" + destinatarioBean.getNome();
            texto = texto + ";" + destinatarioBean.getApelido();
            texto = texto + ";" + destinatarioBean.getCaixaPostal();
            texto = texto + ";" + destinatarioBean.getEndereco();
            texto = texto + ";" + destinatarioBean.getNumeroEndereco();
            texto = texto + ";" + destinatarioBean.getComplemento();
            texto = texto + ";" + destinatarioBean.getBairro();
            texto = texto + ";" + destinatarioBean.getCidade();
            texto = texto + ";" + destinatarioBean.getUf();
            texto = texto + ";" + destinatarioBean.getEmail();
            texto = texto + ";" + destinatarioBean.getTelefone();
            texto = texto + ";" + destinatarioBean.getFax();
            texto = texto + ";" + destinatarioBean.getCepCaixaPostal();
            texto = texto + ";" + destinatarioBean.getCep() + "\n";
        }
        try {
            fileWriter = new FileWriter(new File(arquivo));
            fileWriter.write(texto);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileWriter != null)
                try {
                    fileWriter.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
        }
    }

    public void exportarParaCSV(String arquivo, Vector<DestinatarioBean> destinatario) throws EnderecadorExcecao {
        String texto = "";
        texto = "Tratamento";
        texto = texto + ";Empresa/Nome (linha 1)";
        texto = texto + ";Empresa/Nome (linha 2)";
        texto = texto + ";Caixa Postal";
        texto = texto + ";Endereço";
        texto = texto + ";Número/Lote";
        texto = texto + ";Complemento";
        texto = texto + ";Bairro";
        texto = texto + ";Cidade";
        texto = texto + ";UF";
        texto = texto + ";E-mail";
        texto = texto + ";Telefone";
        texto = texto + ";Fax";
        texto = texto + ";CEP Caixa Postal";
        texto = texto + ";CEP\n";
        for (int i = 0; i < destinatario.size(); i++) {
            DestinatarioBean destinatarioBean = DestinatarioBean.getInstance();
            destinatarioBean = destinatario.get(i);
            texto = texto + destinatarioBean.getTitulo();
            texto = texto + ";" + destinatarioBean.getNome();
            texto = texto + ";" + destinatarioBean.getApelido();
            texto = texto + ";" + destinatarioBean.getCaixaPostal();
            texto = texto + ";" + destinatarioBean.getEndereco();
            texto = texto + ";" + destinatarioBean.getNumeroEndereco();
            texto = texto + ";" + destinatarioBean.getComplemento();
            texto = texto + ";" + destinatarioBean.getBairro();
            texto = texto + ";" + destinatarioBean.getCidade();
            texto = texto + ";" + destinatarioBean.getUf();
            texto = texto + ";" + destinatarioBean.getEmail();
            texto = texto + ";" + destinatarioBean.getTelefone();
            texto = texto + ";" + destinatarioBean.getFax();
            texto = texto + ";" + destinatarioBean.getCepCaixaPostal();
            texto = texto + ";" + destinatarioBean.getCep() + "\n";
        }
        try {
            FileWriter fileWriter = new FileWriter(new File(arquivo));
            fileWriter.write(texto);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void displayURL(String url) throws EnderecadorExcecao {
        try {
            BrowserLauncher bl = new BrowserLauncher();
            bl.openURLinBrowser(bl.getBrowserList(), url);
        } catch (BrowserLaunchingInitializingException | UnsupportedOperatingSystemException ex) {
            throw new EnderecadorExcecao("Não foi possível abrir o browser. " + ex.getMessage());
        }
    }
}
