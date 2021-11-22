package model;


import java.util.HashMap;
import java.util.Map;

public class MonthlyExpenses {
    public String month;
    private float expenses,income;


    public MonthlyExpenses() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public MonthlyExpenses(String month, float expenses, float income) {
        this.month = month;
        this.expenses = expenses;
        this.income = income;
    }

    public String getMonth() {
        return month;
    }

    public float getExpenses() {
        return expenses;
    }

    public float getIncome() {
        return income;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("month", month);
        result.put("expenses", expenses);
        result.put("income", income);
        return result;
    }

}