package br.com.correios.enderecador.bean;

public class UsuarioBean {
    private String pwd = "";

    private String usuarioProxy = "";

    private static UsuarioBean usuarioBean;

    public static UsuarioBean getInstance() {
        if (usuarioBean == null)
            usuarioBean = new UsuarioBean();
        return usuarioBean;
    }

    public String getPwd() {
        return this.pwd;
    }

    public void setUsuario(String usuarioProxy) {
        this.usuarioProxy = usuarioProxy;
    }

    public String getUsuario() {
        return this.usuarioProxy;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
