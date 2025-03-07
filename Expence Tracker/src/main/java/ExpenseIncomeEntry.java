public class ExpenseIncomeEntry {
    private String date;
    private String category;
    private String type;
    private double amount;
    
    public ExpenseIncomeEntry(String date, String category, double amount, String type){
        this.date = date;
        this.category = category;
        this.amount = amount;
        this.type = type;
    }

    public String getDate(){ return date; }
    public String getCategory(){ return category; }
    public double getAmount(){ return amount; }
    public String getType(){ return type; }
}