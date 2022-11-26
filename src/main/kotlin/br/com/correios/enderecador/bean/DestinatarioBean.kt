package br.com.correios.enderecador.bean

import br.com.correios.enderecador.util.BrazilState
import io.konform.validation.Validation
import io.konform.validation.ValidationResult
import io.konform.validation.jsonschema.enum
import io.konform.validation.jsonschema.minLength
import io.konform.validation.jsonschema.pattern

data class DestinatarioBean(
    var numeroDestinatario: String?,
    var apelido: String,
    var titulo: String,
    var nome: String,
    var cep: String,
    var endereco: String,
    var numeroEndereco: String,
    var complemento: String,
    var bairro: String,
    var cidade: String,
    var uf: String,
    var email: String,
    var telefone: String,
    var fax: String,
    var cepCaixaPostal: String,
    var caixaPostal: String,
    var copies: Int = 1,
    var maoPropria: String = "Não",
    var desConteudo: String = "",
    var desEntregaVizinho: String = "",
) {
    fun validate(): ValidationResult<DestinatarioBean> {
        val rules = Validation {
            DestinatarioBean::nome required {
                minLength(2) hint "O campo Empresa/Nome (linha 1) deve ser preenchido!"
            }

            DestinatarioBean::cep required {
                pattern("^\\d{8}\$") hint "O campo CEP deve ser preenchido com 8 números!"
            }

            DestinatarioBean::endereco required {
                minLength(2) hint "O campo endereço deve ser preenchido!"
            }

            DestinatarioBean::numeroEndereco required {
                minLength(1) hint "O campo Numero/Lote deve ser preenchido!"
            }

            DestinatarioBean::cidade required {
                minLength(2) hint "O campo Cidade deve ser preenchido!"
            }

            DestinatarioBean::uf required {
                enum<BrazilState>() hint "O campo UF deve ser informado!"
            }

            DestinatarioBean::email required {
                pattern(".+@.+\\..+") hint "E-mail inválido!"
            }
        }

        return rules.validate(this)
    }

    override fun toString() = nome
}
