package br.com.correios.enderecador.tablemodel

interface IterableTableModel<E> {
    fun insertRow(element: E)

    fun removeRow(element: E)

    fun removeRowAt(row: Int)

    fun getAt(row: Int): E

    fun setAll(elements: List<E>)

    fun setRowAt(row: Int, element: E)

    fun indexOf(element: E): Int

    fun getAll(): List<E>

    fun moveTo(other: IterableTableModel<E>, row: Int)
}
