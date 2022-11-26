package br.com.correios.enderecador.service

import com.couchbase.lite.CouchbaseLite
import com.couchbase.lite.Database
import com.couchbase.lite.DatabaseConfiguration
import org.koin.core.annotation.Singleton

@Singleton
class DatabaseService {
    val database: Database
    val configuration: DatabaseConfiguration

    init {
        CouchbaseLite.init()

        configuration = DatabaseConfiguration()

        database = Database("enderecador", configuration)
    }
}