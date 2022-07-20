package br.com.correios.enderecador.dao;

public abstract class CEPFactory {
    public static final int DNEC = 1;
    public static final int GPBE = 2;

    public static CEPFactory factory(int type) {
        switch (type) {
            case DNEC:
                return DnecCEPFactory.getIntance();
            case GPBE:
                return GpbeCEPFactory.getIntance();
        }
        throw new IllegalArgumentException();
    }

    public abstract InterfaceEnderecoDao getEndereco();
}
