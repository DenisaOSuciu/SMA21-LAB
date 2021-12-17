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


}