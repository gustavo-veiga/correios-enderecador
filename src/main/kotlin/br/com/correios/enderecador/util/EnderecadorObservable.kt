package br.com.correios.enderecador.util

import java.util.*

class EnderecadorObservable : Observable() {
    override fun notifyObservers(arg: Any) {
        setChanged()
        super.notifyObservers(arg)
    }

    companion object {
        var instance: EnderecadorObservable? = null
            get() {
                if (field == null) {
                    field = EnderecadorObservable()
                }
                return field
            }
            private set
    }
}
