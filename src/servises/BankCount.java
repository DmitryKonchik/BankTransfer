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
        //прибавляет деньги на счет
        balance += money;
    }

    public void withdrawMoney(double money) {
        // отнимет деньги со счета
        balance -= money;
    }

    public boolean isEnoughMoneyOnBalance(double money) {
        // проверка достаточно ли денег на счету для перевода
        if (balance >= money) {
            return true;
        }
        return false;
    }

}
