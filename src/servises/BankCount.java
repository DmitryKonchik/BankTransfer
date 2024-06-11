package servises;

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

    public void addMoney(double money) {
        //adds money to the account
        balance += money;
    }

    public void withdrawMoney(double money) {
        // will take money from the account
        balance -= money;
    }

    public boolean isEnoughMoneyOnBalance(double money) {
        // checking whether there is enough money in the account for the transfer
        return balance >= money;
    }

}
