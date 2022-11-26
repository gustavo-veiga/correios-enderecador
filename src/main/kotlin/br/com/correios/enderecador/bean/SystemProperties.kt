package br.com.correios.enderecador.bean

import java.lang.System.*

object SystemProperties {
    val os = Os
    val java = Java
    val user = User

    object Os {
        val name: String = getProperty("os.name")
        val arch: String = getProperty("os.arch")
        val version: String = getProperty("os.version")
    }

    object Java {
        val home: String = getProperty("java.home")
        val name: String = getProperty("java.vm.name")
        val version: String = getProperty("java.version")
        val runtimeVersion: String = getProperty("java.runtime.version")
    }

    object User {
        val dir: String = getProperty("user.dir")
    }
}