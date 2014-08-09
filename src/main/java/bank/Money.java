package bank;

public class Money {
    private final double amount;
    private final Currency currency;

    private Money(double amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public static Money money(double amount, Currency currency) {
        return new Money(amount, currency);
    }

    public double getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }
}
