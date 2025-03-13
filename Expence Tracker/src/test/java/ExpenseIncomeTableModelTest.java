import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseIncomeTableModelTest {
    private ExpenseIncomeTableModel tableModel;

    @BeforeEach
    void setUp(){
        tableModel = new ExpenseIncomeTableModel();
    }

    @Test
    void testAddEntry() {
        ExpenseIncomeEntry entry = new ExpenseIncomeEntry("2025-03-13", "Food & Drink", 500.0, "Expense");

        tableModel.addEntry(entry);

        assertEquals(1, tableModel.getRowCount());
        assertEquals("2025-03-13", tableModel.getValueAt(0, 0));
        assertEquals("Food & Drink", tableModel.getValueAt(0, 1));
        assertEquals(500.0, (double) tableModel.getValueAt(0, 2));
        assertEquals("Expense", tableModel.getValueAt(0, 3));
    }

    @Test
    void testEditEntry() {
        ExpenseIncomeEntry entry = new ExpenseIncomeEntry("2025-03-13", "Food & Drink", 500.0, "Expense");
        tableModel.addEntry(entry);

        ExpenseIncomeEntry updatedEntry = new ExpenseIncomeEntry("2025-03-14", "Grocery", 1000.0, "Income");
        tableModel.editEntry(0, updatedEntry);

        assertEquals("2025-03-14", tableModel.getValueAt(0, 0));
        assertEquals("Grocery", tableModel.getValueAt(0, 1));
        assertEquals(1000.0, (double) tableModel.getValueAt(0, 2));
        assertEquals("Income", tableModel.getValueAt(0, 3));
    }

    @Test
    void testRemoveEntry(){
        ExpenseIncomeEntry entry = new ExpenseIncomeEntry("2025-03-13", "Food & Drink", 500.0, "Expense");
        tableModel.addEntry(entry);

        tableModel.removeEntry(0);

        assertEquals(0, tableModel.getRowCount());
    }

}