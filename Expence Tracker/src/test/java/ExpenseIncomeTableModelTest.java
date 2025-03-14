import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
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

    //--------------------------------------------------------------------------------------------------------------------------------------------------------

    // add entry with method source
    @ParameterizedTest
    @CsvSource({
            "2025-03-13, Food & Drink, 500.0, Expense",
            "2025-03-14, Grocery, 1000.0, Income",
            "2025-03-15, Transport, 200.0, Expense"
    })
    void testAddEntry2(String date, String category, double amount, String type) {
        ExpenseIncomeEntry entry = new ExpenseIncomeEntry(date, category, amount, type);
        tableModel.addEntry(entry);

        // Check if the last added entry matches
        int lastIndex = tableModel.getRowCount() - 1;

        assertNotNull(tableModel.getValueAt(lastIndex, 1)); // Check category is not null

        assertEquals(date, tableModel.getValueAt(lastIndex, 0));
        assertEquals(category, tableModel.getValueAt(lastIndex, 1));
        assertEquals(amount, (double) tableModel.getValueAt(lastIndex, 2));
        assertEquals(type, tableModel.getValueAt(lastIndex, 3));
    }
    //--------------------------------------------------------------------------------------------------------------------------------------------------------

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

    //--------------------------------------------------------------------------------------------------------------------------------------------------------

    // testing EditEntry using MethodSource
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

    //--------------------------------------------------------------------------------------------------------------------------------------------------------

    @ParameterizedTest
    @CsvFileSource(resources = "/edit_entries.csv", numLinesToSkip = 1)
    void testEditEntry2(int index, String date, String category, double amount, String type) {
        // Initialize tableModel inside the test
        ExpenseIncomeTableModel tableModel = new ExpenseIncomeTableModel();

        // Add initial entries before editing
        tableModel.addEntry(new ExpenseIncomeEntry("2025-03-13", "Food & Drink", 500.0, "Expense"));
        tableModel.addEntry(new ExpenseIncomeEntry("2025-03-14", "Entertainment", 150.0, "Expense"));

        // Edit the entry at the given index
        ExpenseIncomeEntry updatedEntry = new ExpenseIncomeEntry(date, category, amount, type);
        tableModel.editEntry(index, updatedEntry);

        // Assertions to verify the update
        assertNotNull(tableModel.getValueAt(index, 0)); // Ensure value is not null
        assertEquals(date, tableModel.getValueAt(index, 0));
        assertEquals(category, tableModel.getValueAt(index, 1));
        assertEquals(amount, (double) tableModel.getValueAt(index, 2));
        assertEquals(type, tableModel.getValueAt(index, 3));
    }


    //--------------------------------------------------------------------------------------------------------------------------------------------------------
    @Test
    void testRemoveEntry(){
        ExpenseIncomeEntry entry = new ExpenseIncomeEntry("2025-03-13", "Food & Drink", 500.0, "Expense");
        tableModel.addEntry(entry);

        tableModel.removeEntry(0);

        assertTrue(tableModel.getRowCount() ==0, "Row count should be 0");
    }
    //--------------------------------------------------------------------------------------------------------------------------------------------------------

    //AddEntry using ValueSource
    @ParameterizedTest
    @ValueSource(strings = {"Food & Drink", "Transport", "Grocery", "Others"})
    void testAddEntryWithDifferentCategories(String category) {
        ExpenseIncomeEntry entry = new ExpenseIncomeEntry("2025-03-13", category, 500.0, "Expense");
        tableModel.addEntry(entry);

        assertEquals(category, tableModel.getValueAt(0, 1));
    }
    //--------------------------------------------------------------------------------------------------------------------------------------------------------

    // testing RemoveEntry using method source
    static Stream<Integer> provideRemoveEntryIndexes() {
        return Stream.of(0, 1); // Removing the first and second entries
    }

    @ParameterizedTest
    @MethodSource("provideRemoveEntryIndexes")
    void testRemoveEntry2(int indexToRemove) {
        tableModel = new ExpenseIncomeTableModel(); // Reset before each test

        // Adding multiple entries
        tableModel.addEntry(new ExpenseIncomeEntry("2025-03-13", "Food & Drink", 500.0, "Expense"));
        tableModel.addEntry(new ExpenseIncomeEntry("2025-03-14", "Grocery", 1000.0, "Income"));

        // Ensure valid index before removing
        assertTrue(indexToRemove < tableModel.getRowCount());

        // Remove entry at the given index
        tableModel.removeEntry(indexToRemove);

        // Assert that row count decreases after removal
        assertTrue(tableModel.getRowCount() < 2);
    }

}