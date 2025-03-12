import com.formdev.flatlaf.FlatDarkLaf;
import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ExpensesIncomesTracker extends JFrame{

    private final ExpenseIncomeTableModel tableModel;
    private final JTable table;
    private final JDateChooser dateChooser;
    private final JTextField amountField;
    private final JComboBox<String> categoryComboBox;
    private final JComboBox<String> typeComboBox;
    private final JButton addButton;
    private final JButton editButton;
    private final JButton removeButton;
    private final JButton viewSummaryButton;
    private final JLabel balanceLabel;
    private double balance;

    public ExpensesIncomesTracker() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
            System.err.println("Failed to Set FlatDarkLaf LookAndFeel");
        }

        tableModel = new ExpenseIncomeTableModel();
        table = new JTable(tableModel);
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");
        amountField = new JTextField(10);
        categoryComboBox = new JComboBox<>(new String[]{"Food & Drink", "Transport", "Grocery", "Others"});
        typeComboBox = new JComboBox<>(new String[]{"Expense", "Income"});
        addButton = new JButton("Add");
        editButton = new JButton("Edit");
        removeButton = new JButton("Remove");
        viewSummaryButton = new JButton("View Summary");
        balanceLabel = new JLabel("Balance: Tk " + balance);

        addButton.addActionListener(e -> addEntry());
        editButton.addActionListener(e -> editEntry());
        removeButton.addActionListener(e -> removeEntry());
        viewSummaryButton.addActionListener(e -> showExpenseSummary());

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Date"));
        inputPanel.add(dateChooser);
        inputPanel.add(new JLabel("Category"));
        inputPanel.add(categoryComboBox);
        inputPanel.add(new JLabel("Amount"));
        inputPanel.add(amountField);
        inputPanel.add(new JLabel("Type"));
        inputPanel.add(typeComboBox);
        inputPanel.add(addButton);
        inputPanel.add(editButton);
        inputPanel.add(removeButton);
        inputPanel.add(viewSummaryButton);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(balanceLabel);
        setLayout(new BorderLayout());

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setTitle("Expense Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }


    private void addEntry() {
        Date selectedDate = dateChooser.getDate();
        if (selectedDate == null) {
            JOptionPane.showMessageDialog(this, "Select a Date", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String date = new SimpleDateFormat("yyyy-MM-dd").format(selectedDate);
        String category = (String) categoryComboBox.getSelectedItem();
        String amountStr = amountField.getText();
        String type = (String) typeComboBox.getSelectedItem();
        double amount;

        if (amountStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter the Amount", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Amount Format", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (type.equals("Expense")) {
            amount *= -1;
        }

        ExpenseIncomeEntry entry = new ExpenseIncomeEntry(date, category, amount, type);
        tableModel.addEntry(entry);

        balance += amount;
        balanceLabel.setText("Balance: Tk." + balance);

        clearInputFields();
    }

    private void editEntry() {
        int selectedRowIndex = table.getSelectedRow();
        if (selectedRowIndex == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to edit", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        Date selectedDate = dateChooser.getDate();
        if (selectedDate == null) {
            JOptionPane.showMessageDialog(this, "Select a Date", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        String updatedDate = new SimpleDateFormat("yyyy-MM-dd").format(selectedDate);
        String updatedCategory = (String) categoryComboBox.getSelectedItem();
        String updatedAmountStr = amountField.getText();
        String updatedType = (String) typeComboBox.getSelectedItem();
    
        if (updatedAmountStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter the Updated Amount", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        try {
            double updatedAmount = Double.parseDouble(updatedAmountStr);
            if (updatedType.equals("Expense")) {
                updatedAmount *= -1;
            }
    
            // Get the original amount before editing
            double originalAmount = (double) tableModel.getValueAt(selectedRowIndex, 2);
    
            // Update balance correctly
            balance -= originalAmount;  // Remove the original amount
            balance += updatedAmount;   // Add the updated amount
    
            ExpenseIncomeEntry updatedEntry = new ExpenseIncomeEntry(updatedDate, updatedCategory, updatedAmount, updatedType);
            tableModel.editEntry(selectedRowIndex, updatedEntry);
    
            balanceLabel.setText("Balance: Tk." + balance);
            clearInputFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Updated Amount Format", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    private void removeEntry() {
        int selectedRowIndex = table.getSelectedRow();
        if (selectedRowIndex == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to remove", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double removedAmount = (double) table.getValueAt(selectedRowIndex, 2);
        tableModel.removeEntry(selectedRowIndex);

        balance -= removedAmount;
        balanceLabel.setText("Balance: Tk." + balance);
    }

    private void showExpenseSummary() {
        Map<String, Double> categoryTotals = new HashMap<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String category = (String) tableModel.getValueAt(i, 1);
            double amount = (double) tableModel.getValueAt(i, 2);
            if (amount < 0) {
                categoryTotals.put(category, categoryTotals.getOrDefault(category, 0.0) + Math.abs(amount));
            }
        }

        StringBuilder summary = new StringBuilder("Expense Summary:\n");
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            summary.append(entry.getKey()).append(" : ").append(entry.getValue()).append("\n");
        }

        JOptionPane.showMessageDialog(this, summary.toString(), "Expense Summary", JOptionPane.INFORMATION_MESSAGE);
    }

    private void clearInputFields() {
        dateChooser.setDate(null);
        amountField.setText("");
        categoryComboBox.setSelectedIndex(0);
        typeComboBox.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ExpensesIncomesTracker::new);
    }
    
}
