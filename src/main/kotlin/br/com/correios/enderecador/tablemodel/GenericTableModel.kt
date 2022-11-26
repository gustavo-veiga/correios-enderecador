package br.com.correios.enderecador.tablemodel

import javax.swing.table.AbstractTableModel

abstract class GenericTableModel<T>: AbstractTableModel(), IterableTableModel<T>, Iterable<T> {
    protected var columns = arrayOf<String>()
    
    protected var dataTable = mutableListOf<T>()
        get() {
            if (field == null) {
                return mutableListOf()
            }
            return field
        }

    override fun getColumnCount(): Int {
        return columns.size
    }

    override fun getColumnName(column: Int): String {
        return columns[column]
    }

    override fun getRowCount(): Int {
        return dataTable.size
    }

    override fun isCellEditable(row: Int, column: Int): Boolean {
        return false
    }

    abstract override fun getValueAt(row: Int, column: Int): Any

    override fun setValueAt(value: Any, row: Int, column: Int) {}

    override fun insertRow(element: T) {
        dataTable.add(element)
        fireTableDataChanged()
    }

    override fun removeRow(element: T) {
        dataTable.remove(element)
        fireTableDataChanged()
    }

    override fun removeRowAt(row: Int) {
        dataTable.removeAt(row)
        fireTableDataChanged()
    }

    override fun getAt(row: Int): T {
        return dataTable[row]
    }

    override fun setAll(elements: List<T>) {
        dataTable = elements.toMutableList()
        fireTableDataChanged()
    }

    override fun setRowAt(row: Int, element: T) {
        dataTable[row] = element
        fireTableDataChanged()
    }

    override fun indexOf(element: T): Int {
        return dataTable.indexOf(element)
    }

    override operator fun iterator(): Iterator<T> {
        return dataTable.iterator()
    }

    override fun getAll(): List<T> {
        return dataTable.toList()
    }

    override fun moveTo(other: IterableTableModel<T>, row: Int) {
        val data = dataTable[row]
        other.insertRow(data)
        dataTable.remove(data)
        fireTableDataChanged()
    }

    override fun insertNotRepeated(values: List<T>, comparator: Comparator<T>) {
        values.forEach { value ->
            if (dataTable.contains(value).not()) {
                dataTable.add(value)
            }
        }
        dataTable.sortWith(comparator)
        fireTableDataChanged()
    }
}
