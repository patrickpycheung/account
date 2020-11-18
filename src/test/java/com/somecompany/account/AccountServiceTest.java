package com.somecompany.account;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Timestamp;
import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.somecompany.account.model.Account;
import com.somecompany.account.model.Customer;
import com.somecompany.account.service.AccountService;

@SpringBootTest
@ActiveProfiles("dev")
public class AccountServiceTest {

	@Autowired
	private AccountService accountService;

	@ParameterizedTest
	@CsvFileSource(resources = "/AccountTestData.csv", numLinesToSkip = 1)
	public void shouldBeAbleToGetAccountsByCustId(String numOfAccountsStr, String inputCustId,
			String expectedAccountLists) {

		Customer customer = new Customer();
		customer.setCustId(1111111111);
		customer.setCustName("ABCDEFGHIJKLMNOPQRSTUVWXYZABCD");

		// The actual account values
		List<Account> accList = accountService.getAllAccounts(customer);

		// The expected account values
		String[] accounts = expectedAccountLists.split("\\|");

		// The number of accounts for the customer
		int numOfAccounts = Integer.valueOf(numOfAccountsStr);

		for (int i = 0; i < numOfAccounts; i++) {
			String[] account = accounts[i].split("~");

			Account expectedAccount = new Account();
			expectedAccount.setCustId(Long.valueOf(account[0]));
			expectedAccount.setAccountNum(Long.valueOf(account[1]));
			expectedAccount.setAccountName(account[2]);
			expectedAccount.setAccountType(account[3]);
			expectedAccount.setBalanceDate(Timestamp.valueOf(account[4]));
			expectedAccount.setCurrency(account[5]);
			expectedAccount.setOpeningAvailableBalance(Float.valueOf(account[6]));

			// Assertion
			assertEquals(expectedAccount, accList.get(i));
		}
	}
}
