package br.com.correios.enderecador.model

import io.konform.validation.Validation
import io.konform.validation.jsonschema.*

data class SenderEdit(
    var title: String,
    var name: String,
    var lastName: String,
    var email: String,
    var phone: String,
    val street: String,
    val number: String,
    val complement: String,
    val district: String,
    val city: String,
    val state: String,
    val zipCode: String,
    val postOfficeBox: String,
    val postOfficeBoxZipCode: String,
) {
    fun validate() = Validation {
        SenderEdit::name {
            minLength(2)
            maxLength(100)
        }

        SenderEdit::email {
            pattern("^([A-Za-z]|[0-9])+\$") hint "Please provide a valid email address"
        }
    }
}
