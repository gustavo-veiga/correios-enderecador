package br.com.correios.enderecador.util;

import java.util.Observable;

public class EnderecadorObservable extends Observable {
    private static EnderecadorObservable instance;

    public static EnderecadorObservable getInstance() {
        if (instance == null)
            instance = new EnderecadorObservable();
        return instance;
    }

    public void notifyObservers(Object arg) {
        setChanged();
        super.notifyObservers(arg);
    }
}
