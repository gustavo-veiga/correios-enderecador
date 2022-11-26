package br.com.correios.enderecador.service

interface AddressService {
    fun findByZipCode(zipCode: String)
}