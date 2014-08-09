import bank.*;
import com.insightfullogic.lambdabehave.JunitSuiteRunner;
import com.insightfullogic.lambdabehave.generators.Generator;
import com.insightfullogic.lambdabehave.generators.SourceGenerator;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static bank.Account.account;
import static bank.Currency.EUR;
import static bank.Currency.GBP;
import static bank.Money.money;
import static com.insightfullogic.lambdabehave.Suite.describe;
import static org.mockito.Mockito.when;

@RunWith(JunitSuiteRunner.class)
public class BankSpec {

    @Mock
            private ConversionService conversionService;
    {
        describe("a bank account", it -> {

            MockitoAnnotations.initMocks(this);
//            final ConversionService conversionService = it.usesMock(ConversionService.class);
            final Account account = account(GBP, conversionService);

            it.isSetupWith(account::clearBalance);

            it.isConcludedWith(account::clearBalance);

            it.should("be empty when created", expect -> {
                expect.that(account.getBalance()).is(0.0);
            });

            it.should("allow deposits", expect -> {
                account.deposit(money(1.0, GBP));
                expect.that(account.getBalance()).is(1.0);
            });

            it.should("allow payments", expect -> {
                account.deposit(money(2.0, GBP));
                account.pay(money(1.0, GBP));
                expect.that(account.getBalance()).is(1.0);
            });

            it.should("prevent a payment if the balance can't cover it", expect -> {
                expect.exception(InvalidOperationException.class, () -> {
                    account.pay(money(1.0, GBP));
                });
            });

            it.should("allow deposits in different currencies", expect -> {
                final Money deposit = money(1.2, EUR);
                when(conversionService.convert(deposit, GBP)).thenReturn(money(1.0, GBP));

                account.deposit(deposit);
                expect.that(account.getBalance()).is(1.0);
            });

            it.requires(10).example(new Generator<Object>() {
                @Override
                public Object generate(SourceGenerator source) {
                    return null;
                }
            })
            it.should("allow payments in different currencies", expect -> {
                final Money payment = money(1.2, EUR);
                when(conversionService.convert(payment, GBP)).thenReturn(money(1.0, GBP));

                account.deposit(money(1.0, GBP));
                account.pay(payment);
                expect.that(account.getBalance()).is(0.0);
            });
        });
    }
}
