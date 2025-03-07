package main.java;

import javax.swing.table.AbstractTableModel;

public class ExpenseIncomeTableModel extends AbstractTableModel{
    private final List<ExpenseIncomeEntry> entries;
    private final String[] columNames = {"Date", "Category", "Amount", "Type"};

    public ExpenseIncomeTableModel() { entries = new Arraylist<>(); }

    public void addEntry(ExpenseIncomeEntry entry){
        entries.add(entry);
        fireTableRowsInserted(entries.size() - 1, entries.size() - 1);
    }
}
