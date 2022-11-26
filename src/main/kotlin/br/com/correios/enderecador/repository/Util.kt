package br.com.correios.enderecador.repository

import br.com.correios.enderecador.model.Address
import br.com.correios.enderecador.model.Sender
import com.couchbase.lite.Dictionary
import com.couchbase.lite.Document
import com.couchbase.lite.MutableDictionary
import com.couchbase.lite.MutableDocument

fun Address.toDictionary(): Dictionary {
    return MutableDictionary().also { dictionary ->
        dictionary.setValue(this::street.name, this.street)
        dictionary.setInt(this::number.name, this.number)
        dictionary.setString(this::complement.name, this.complement)
        dictionary.setString(this::district.name, this.district)
        dictionary.setString(this::city.name, this.city)
        dictionary.setString(this::state.name, this.state)
        dictionary.setString(this::zipCode.name, this.zipCode)
    }
}

fun Sender.toDocument(): Document {
    return MutableDocument().also { document ->
        document.setString("type", this::javaClass.name)
        document.setString(this::name.name, this.name)
        document.setString(this::title.name, this.title)
        document.setDictionary(this::address.name, this.address.toDictionary())
    }
}

fun Document.toSender() {

}