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
        balance += money;
    }
    public void withdrawMoney(double money) {
        balance -= money;
    }
    public boolean isEnoughMoneyOnBalance (double money) {
        if (balance >= money){
            return true;
        }
        return false;
    }

}
