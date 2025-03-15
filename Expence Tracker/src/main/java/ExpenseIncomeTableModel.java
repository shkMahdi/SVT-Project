import java.util.List;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class ExpenseIncomeTableModel extends AbstractTableModel{
    private final List<ExpenseIncomeEntry> entries;
    private final String[] columNames = {"Date", "Category", "Amount", "Type"};

    public ExpenseIncomeTableModel() { entries = new ArrayList<>(); }

    public void addEntry(ExpenseIncomeEntry entry){
        entries.add(entry);
        fireTableRowsInserted(entries.size() - 1, entries.size() - 1);
    }

    public void editEntry(int rowIndex, ExpenseIncomeEntry updatedEntry) {
        entries.set(rowIndex, updatedEntry);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public void removeEntry(int rowIndex) {
        entries.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    private Object getColumnValue(ExpenseIncomeEntry entry, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return entry.getDate();
            case 1:
                return entry.getCategory();
            case 2:
                return entry.getAmount();
            case 3:
                return entry.getType();
            default:
                return null;
        }
    }

    @Override
    public int getRowCount() {
        return entries.size();
    }

    @Override
    public int getColumnCount() {
        return columNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ExpenseIncomeEntry entry = entries.get(rowIndex);
        return getColumnValue(entry, columnIndex);
    }
}
