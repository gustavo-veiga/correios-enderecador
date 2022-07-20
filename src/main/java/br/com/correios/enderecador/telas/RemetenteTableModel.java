package br.com.correios.enderecador.telas;

import br.com.correios.enderecador.bean.RemetenteBean;

import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

public class RemetenteTableModel extends DefaultTableModel {
    private final String[] colunas = new String[]{"Empresa/Nome", "Endereço", "Numero/Lote", "Cidade", "CEP", "UF"};

    private ArrayList<RemetenteBean> remetenteBean = new ArrayList<>();

    public int getColumnCount() {
        return this.colunas.length;
    }

    public String getColumnName(int i) {
        return this.colunas[i];
    }

    public int getRowCount() {
        int retorno = 0;
        if (this.remetenteBean != null)
            retorno = this.remetenteBean.size();
        return retorno;
    }

    public Object getValueAt(int linha, int coluna) {
        RemetenteBean remetenteBean = this.remetenteBean.get(linha);
        String valor = null;
        switch (coluna) {
            case 0:
                valor = remetenteBean.getNome() + " " + remetenteBean.getApelido();
                break;
            case 1:
                valor = remetenteBean.getEndereco() + " " + remetenteBean.getComplemento() + " " + remetenteBean.getBairro();
                break;
            case 2:
                valor = remetenteBean.getNumeroEndereco();
                break;
            case 3:
                valor = remetenteBean.getCidade();
                break;
            case 4:
                valor = remetenteBean.getCep();
                break;
            case 5:
                valor = remetenteBean.getUf();
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

    public void setRemetente(ArrayList<RemetenteBean> remetenteBean) {
        this.remetenteBean = remetenteBean;
        if (this.remetenteBean == null)
            this.remetenteBean = new ArrayList<>();
        fireTableDataChanged();
    }

    public RemetenteBean getRemetente(int linha) {
        RemetenteBean remetente = null;
        if (this.remetenteBean.get(linha) != null)
            remetente = this.remetenteBean.get(linha);
        return remetente;
    }

    public void addRemetente(RemetenteBean remetente) {
        this.remetenteBean.add(remetente);
        fireTableDataChanged();
    }

    public void setRemetente(int linha, RemetenteBean remetenteBean) {
        this.remetenteBean.set(linha, remetenteBean);
        fireTableDataChanged();
    }

    public void removeRemetente(RemetenteBean remetente) {
        this.remetenteBean.remove(remetente);
        fireTableDataChanged();
    }

    public int indexOf(RemetenteBean remetente) {
        return this.remetenteBean.indexOf(remetente);
    }
}
