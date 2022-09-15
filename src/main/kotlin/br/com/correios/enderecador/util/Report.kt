package br.com.correios.enderecador.util

enum class Report(val file: String) {
    AR("avisoRecebimento"),
    DECLARATION("declaracao"),
    ENVELOPE_C5("EnvelopeC5"),
    ENVELOPE_C6("EnvelopeC6"),
    ENVELOPE_C6_C5("EnvelopeC6C5"),
    ENVELOPE_A4_10("A4Envelope10"),
    ENVELOPE_A4_14("A4Envelope14"),
    ENVELOPE_LETTER_10("CartaEnvelope10"),
    ENVELOPE_LETTER_14("CartaEnvelope14"),
    ORDER_2_NEIGHBOR("Encomenda2_vizinho"),
    ORDER_4_NEIGHBOR("Encomenda4_vizinho")
}