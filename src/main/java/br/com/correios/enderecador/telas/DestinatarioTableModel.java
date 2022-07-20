package br.com.correios.enderecador.telas;

import br.com.correios.enderecador.bean.DestinatarioBean;

import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

public class DestinatarioTableModel extends DefaultTableModel {
    private final String[] colunas = new String[]{"Empresa/Nome", "Endereço", "Numero/Lote", "Cidade", "CEP", "UF"};

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
        String valor = "";
        if (coluna == 0) {
            valor = destinatarioBean.getNome() + " " + destinatarioBean.getApelido();
        } else if (coluna == 1) {
            valor = destinatarioBean.getEndereco() + " " + destinatarioBean.getComplemento() + " " + destinatarioBean.getBairro();
        } else if (coluna == 2) {
            valor = destinatarioBean.getNumeroEndereco();
        } else if (coluna == 3) {
            valor = destinatarioBean.getCidade();
        } else if (coluna == 4) {
            valor = destinatarioBean.getCep();
        } else if (coluna == 5) {
            valor = destinatarioBean.getUf();
        } else {
            valor = "";
        }
        if (valor == null)
            valor = "";
        return valor;
    }

    public boolean isCellEditable(int linha, int coluna) {
        return false;
    }

    public void setDestinatario(ArrayList destinatarioBean) {
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

    public void addDestinatario(DestinatarioBean destinatario) {
        this.destinatarioBean.add(destinatario);
        fireTableDataChanged();
    }

    public void removeDestinatario(DestinatarioBean destinatario) {
        this.destinatarioBean.remove(destinatario);
        fireTableDataChanged();
    }

    public int indexOf(DestinatarioBean destinatario) {
        return this.destinatarioBean.indexOf(destinatario);
    }
}
