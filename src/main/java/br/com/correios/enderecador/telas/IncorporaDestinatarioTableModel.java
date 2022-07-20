package br.com.correios.enderecador.telas;

import br.com.correios.enderecador.bean.DestinatarioBean;

import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

public class IncorporaDestinatarioTableModel extends DefaultTableModel {
    private final String[] colunas = new String[]{
        "Tratamento", "Empresa/Nome (linha 1)", "Empresa/Nome (linha 2)", "Caixa Postal", "Endereço", "Numero/Lote",
        "Complemento", "Bairro", "Cidade", "UF", "Email", "Telefone", "Fax", "CEP Caixa Postal", "Cep"};

    private ArrayList<DestinatarioBean> destinatarioBean = new ArrayList<>();

    public int getColumnCount() {
        return this.colunas.length;
    }

    public String getColumnName(int i) {
        return this.colunas[i];
    }

    public int getRowCount() {
        int retorno = 0;
        if (this.destinatarioBean != null)
            retorno = this.destinatarioBean.size();
        return retorno;
    }

    public Object getValueAt(int linha, int coluna) {
        DestinatarioBean destinatarioBean = this.destinatarioBean.get(linha);
        String valor = null;
        switch (coluna) {
            case 0:
                valor = destinatarioBean.getTitulo();
                break;
            case 1:
                valor = destinatarioBean.getNome();
                break;
            case 2:
                valor = destinatarioBean.getApelido();
                break;
            case 3:
                valor = destinatarioBean.getCaixaPostal();
                break;
            case 4:
                valor = destinatarioBean.getEndereco();
                break;
            case 5:
                valor = destinatarioBean.getNumeroEndereco();
                break;
            case 6:
                valor = destinatarioBean.getComplemento();
                break;
            case 7:
                valor = destinatarioBean.getBairro();
                break;
            case 8:
                valor = destinatarioBean.getCidade();
                break;
            case 9:
                valor = destinatarioBean.getUf();
                break;
            case 10:
                valor = destinatarioBean.getEmail();
                break;
            case 11:
                valor = destinatarioBean.getTelefone();
                break;
            case 12:
                valor = destinatarioBean.getFax();
                break;
            case 13:
                valor = destinatarioBean.getCepCaixaPostal();
                break;
            case 14:
                valor = destinatarioBean.getCep();
                break;
            default:
                valor = "";
                break;
        }
        if (valor == null)
            valor = "";
        return valor;
    }

    public boolean isCellEditable(int linha, int coluna) {
        return false;
    }

    public void setDestinatario(ArrayList<DestinatarioBean> destinatarioBean) {
        this.destinatarioBean = destinatarioBean;
        if (this.destinatarioBean == null)
            this.destinatarioBean = new ArrayList<>();
        fireTableDataChanged();
    }

    public DestinatarioBean getDestinatario(int linha) {
        DestinatarioBean destinatario = null;
        if (this.destinatarioBean.get(linha) != null)
            destinatario = this.destinatarioBean.get(linha);
        return destinatario;
    }

    public void setDestinatario(int linha, DestinatarioBean destinatarioBean) {
        this.destinatarioBean.set(linha, destinatarioBean);
        fireTableDataChanged();
    }
}
