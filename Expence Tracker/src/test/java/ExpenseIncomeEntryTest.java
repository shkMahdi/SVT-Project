import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseIncomeEntryTest {
    @Test
    void testConstructorAndGetters() {
        ExpenseIncomeEntry entry = new ExpenseIncomeEntry("2025-03-13", "Food", 250.50, "Expense");

        // assertEquals - checks if values mat
        assertEquals("2025-03-13", entry.getDate(), "Date should match");
        assertEquals("Food", entry.getCategory(), "Category should match");
        assertEquals(250.50, entry.getAmount(), "Amount should match");
        assertEquals("Expense", entry.getType(), "Type should match");

        // assertnotnull - ensures that an object is not null
        assertNotNull(entry.getDate(), "Date should not be null");
        assertNotNull(entry.getCategory(), "Category should not be null");
        assertNotNull(entry.getType(), "Type should not be null");

        assertTrue(entry.getAmount() > 0, "Amount should be positive for income/expense tracking");

        assertFalse(entry.getCategory().isEmpty(), "Category should not be empty");
    }

    @Test
    void testNegativeAmount() {
        ExpenseIncomeEntry entry = new ExpenseIncomeEntry("2025-03-13", "Transport", -100.00, "Expense");

        assertEquals(-100.00, entry.getAmount(), "Amount should be negative for an expense");
    }

}