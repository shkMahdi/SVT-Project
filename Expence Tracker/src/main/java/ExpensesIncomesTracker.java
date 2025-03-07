package main.java;

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
    
}
