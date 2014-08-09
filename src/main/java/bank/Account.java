package bank;

import static bank.Money.money;

public class Account {

    private final ConversionService conversionService;

    private Money balance;

    private Account(Currency currency, ConversionService conversionService) {
        this.conversionService = conversionService;
        this.balance = money(0.0, currency);
    }

    public static Account account(Currency currency, ConversionService conversionService) {
        return new Account(currency, conversionService);
    }

    public double getBalance() {
        return balance.getAmount();
    }

    public void clearBalance() {
        balance = money(0.0, balance.getCurrency());
    }

    public void deposit(Money amount) {
        final Money deposit = calculateOperationAmount(amount);
        balance = money(balance.getAmount() + deposit.getAmount(), balance.getCurrency());
    }

    public void pay(Money amount) {
        final Money payment = calculateOperationAmount(amount);
        final double currentBalance = balance.getAmount();
        if (currentBalance < payment.getAmount()) {
            throw new InvalidOperationException();
        }
        balance = money(currentBalance - payment.getAmount(), balance.getCurrency());
    }

    private Money calculateOperationAmount(Money amount) {
        return (amount.getCurrency() != balance.getCurrency()) ?
                conversionService.convert(amount, balance.getCurrency()) :
                amount;
    }
}
