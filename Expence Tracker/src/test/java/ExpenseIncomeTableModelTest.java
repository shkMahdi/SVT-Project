import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

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

        assertTrue(tableModel.getRowCount() > 0, "Row count should increase after adding an entry");
        assertNotNull(tableModel.getValueAt(0, 0), "Date should not be null");

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

        assertSame("Grocery", tableModel.getValueAt(0, 1), "Category should be 'Grocery'");
        assertFalse((double) tableModel.getValueAt(0, 2) == 500.0, "Amount should not be 500.0");
        assertNotEquals("Expence", tableModel.getValueAt(0, 3));
    }

    @Test
    void testRemoveEntry(){
        ExpenseIncomeEntry entry = new ExpenseIncomeEntry("2025-03-13", "Food & Drink", 500.0, "Expense");
        tableModel.addEntry(entry);

        tableModel.removeEntry(0);

        assertTrue(tableModel.getRowCount() ==0, "Row count should be 0");
    }

    //AddEntry using ValueSource
    @ParameterizedTest
    @ValueSource(strings = {"Food & Drink", "Transport", "Grocery", "Others"})
    void testAddEntryWithDifferentCategories(String category) {
        ExpenseIncomeEntry entry = new ExpenseIncomeEntry("2025-03-13", category, 500.0, "Expense");
        tableModel.addEntry(entry);

        assertEquals(category, tableModel.getValueAt(0, 1));
    }

    //EditEntry using MethodSource
    static Stream<Object[]> provideEditEntryData() {
        return Stream.of(
                new Object[]{"2025-03-14", "Grocery", 1000.0, "Income"},
                new Object[]{"2025-03-15", "Transport", 200.0, "Expense"},
                new Object[]{"2025-03-16", "Entertainment", 500.0, "Expense"}
        );
    }

    @ParameterizedTest
    @MethodSource("provideEditEntryData")
    void testEditEntryWithChangingEntries(String newDate, String newCategory, double newAmount, String newType) {
        // Adding an initial entry
        ExpenseIncomeEntry entry = new ExpenseIncomeEntry("2025-03-13", "Food & Drink", 500.0, "Expense");
        tableModel.addEntry(entry);

        // Updating entry
        ExpenseIncomeEntry updatedEntry = new ExpenseIncomeEntry(newDate, newCategory, newAmount, newType);
        tableModel.editEntry(0, updatedEntry);

        // Assertions
        assertEquals(newDate, tableModel.getValueAt(0, 0));
        assertEquals(newCategory, tableModel.getValueAt(0, 1));
        assertEquals(newAmount, (double) tableModel.getValueAt(0, 2));
        assertEquals(newType, tableModel.getValueAt(0, 3));
    }

}