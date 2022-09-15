package br.com.correios.enderecador.util

enum class FileExtension(val description: String) {
    CSV("CSV (Comma-Separated Values)"),
    TXT("TXT (Plain Text)");

    val extension: String
        get() = name.lowercase()
}