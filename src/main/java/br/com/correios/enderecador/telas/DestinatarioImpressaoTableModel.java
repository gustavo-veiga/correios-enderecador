package br.com.correios.enderecador.telas;

import br.com.correios.enderecador.bean.DestinatarioBean;

import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class DestinatarioImpressaoTableModel extends DefaultTableModel {
    private final int TAMANHO_CAMPO_ENTREGA_VIZINHO = 40;

    private final String[] colunas;

    private Vector<DestinatarioBean> destinatarioBean = new Vector<>();

    public DestinatarioImpressaoTableModel(String tipo) {
        if (tipo.equalsIgnoreCase("E")) {
            this.colunas = new String[]{"Empresa/Nome", "Endereço", "Cidade/UF", "Quantidade *", "Mão Própria *", "Observação *", "Entrega no Vizinho"};
        } else {
            this.colunas = new String[]{"Empresa/Nome", "Endereço", "Cidade/UF", "Quantidade *", "Mão Própria *", "Observação *"};
        }
    }

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

    public DestinatarioBean getElementAt(int row) {
        return this.destinatarioBean.get(row);
    }

    public void setValueAt(Object value, int row, int col) {
        String valor;
        boolean acho;
        int i;
        String texto;
        StringBuilder sb;
        DestinatarioBean destinatarioBean = this.destinatarioBean.get(row);
        switch (col) {
            case 3:
                valor = (String) value;
                acho = true;
                for (i = 0; i < valor.length(); i++) {
                    if (!Character.isDigit(valor.charAt(i))) {
                        acho = false;
                        break;
                    }
                }
                if (acho) {
                    destinatarioBean.setQuantidade((String) value);
                    break;
                }
                break;
            case 4:
                destinatarioBean.setMaoPropria((String) value);
                if (value == "Sim")
                    destinatarioBean.setDesEntregaVizinho("");
                break;
            case 5:
                destinatarioBean.setDesConteudo((String) value);
                break;
            case 6:
                texto = (String) value;
                if (texto.length() <= TAMANHO_CAMPO_ENTREGA_VIZINHO) {
                    destinatarioBean.setDesEntregaVizinho(texto);
                    break;
                }
                sb = new StringBuilder();
                sb.append("O texto não pode ter mais que ");
                sb.append(40);
                sb.append(" caracteres");
                JOptionPane.showMessageDialog(null, sb.toString(), "Entrega autorizada no vizinho", JOptionPane.WARNING_MESSAGE);
                break;
        }
        fireTableCellUpdated(row, col);
    }

    public Object getValueAt(int linha, int coluna) {
        DestinatarioBean destinatarioBean = this.destinatarioBean.get(linha);
        String valor = null;
        switch (coluna) {
            case 0:
                valor = destinatarioBean.getNome() + " " + destinatarioBean.getApelido();
                break;
            case 1:
                valor = destinatarioBean.getEndereco() + " " + destinatarioBean.getNumeroEndereco() + " " + destinatarioBean.getComplemento() + " " + destinatarioBean.getBairro();
                break;
            case 2:
                valor = destinatarioBean.getCidade() + " - " + destinatarioBean.getUf();
                break;
            case 3:
                valor = destinatarioBean.getQuantidade();
                break;
            case 4:
                valor = destinatarioBean.getMaoPropria();
                break;
            case 5:
                valor = destinatarioBean.getDesConteudo();
                break;
            case 6:
                valor = destinatarioBean.getDesEntregaVizinho();
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
        boolean retorno = coluna == 3 || coluna == 4 || coluna == 5;
        String s = (String) getValueAt(linha, 4);
        if (coluna == 6 && s.equals("Não"))
            retorno = true;
        return retorno;
    }

    public void setDestinatario(Vector<DestinatarioBean> destinatarioBean) {
        this.destinatarioBean = destinatarioBean;
        if (this.destinatarioBean == null)
            this.destinatarioBean = new Vector<>();
        fireTableDataChanged();
    }

    public DestinatarioBean getDestinatario(int linha) {
        DestinatarioBean destinatario = null;
        if (this.destinatarioBean.get(linha) != null)
            destinatario = this.destinatarioBean.get(linha);
        return destinatario;
    }

    public Vector<DestinatarioBean> getDestinatario() {
        return this.destinatarioBean;
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
