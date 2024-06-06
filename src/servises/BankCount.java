package servises;

import java.util.Map;

public class BankCount {

    private String name;
    private double balance;

    public BankCount(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void addMoney(double money) {
        balance += money;
    }
    public void withdrawMoney(double money) {
        balance -= money;
    }
    public boolean isEnoughMoneyOnBalance (double money) {
        if (balance - money >= 0){
            return true;
        }
        return false;
    }
    public boolean isCountExists (Map<String, Double> counts) {
        if (counts.containsKey(name)) {
            return true;
        }
        return false;
    }

}
