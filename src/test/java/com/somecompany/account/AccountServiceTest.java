package com.somecompany.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import com.somecompany.account.model.Account;
import com.somecompany.account.model.Customer;
import com.somecompany.account.model.Transaction;
import com.somecompany.account.model.TransactionPK;
import com.somecompany.account.service.AccountService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class AccountServiceTest {

	@Autowired
	private AccountService accountService;

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate testRestTemplate;

	@ParameterizedTest
	@CsvFileSource(resources = "/AccountTestData.csv", numLinesToSkip = 1)
	public void shouldBeAbleToGetAccountsByCustId(String numOfAccountsStr, String inputCustId,
			String expectedAccountLists) {

		Customer customer = new Customer();
		customer.setCustId(Long.valueOf(inputCustId));

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

	@ParameterizedTest
	@CsvFileSource(resources = "/TransactionTestData.csv", numLinesToSkip = 1)
	public void shouldBeAbleToGetTransactionsByAccountNum(String numOfTransactionsStr, String inputAccNum,
			String expectedTransactionLists) {
		Account account = new Account();
		account.setAccountNum(Long.valueOf(inputAccNum));

		// The actual transaction values
		List<Transaction> transactionList = accountService.getAllTransactions(account);

		// The expected transaction values
		String[] transactions = expectedTransactionLists.split("\\|");

		// The number of transactions for the account
		int numOfTransactions = Integer.valueOf(numOfTransactionsStr);

		for (int i = 0; i < numOfTransactions; i++) {
			String[] transaction = transactions[i].split("~");

			TransactionPK expectedTransactionPK = new TransactionPK();
			expectedTransactionPK.setAccountNum(Long.valueOf(transaction[0]));
			expectedTransactionPK.setValueDate(Timestamp.valueOf(transaction[2]));

			Transaction expectedTransaction = new Transaction();
			expectedTransaction.setTransactionPK(expectedTransactionPK);
			expectedTransaction.setAccountName(transaction[1]);
			expectedTransaction.setCurrency(transaction[3]);
			expectedTransaction.setDebitAmt(Float.valueOf(transaction[4]));
			expectedTransaction.setCreditAmt(Float.valueOf(transaction[5]));
			expectedTransaction.setDebitCredit(transaction[6]);
			expectedTransaction.setTransactionNarrative(transaction[7]);

			// Assertion
			assertEquals(expectedTransaction, transactionList.get(i));
		}
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/AccountAPITestData.csv", numLinesToSkip = 1)
	public void shouldBeAbleToGetAccountsByCustIdThroughAPICall(String numOfAccountsStr, String inputCustId,
			String expectedAccountListStr) {

		// The actual list of accounts
		ArrayList<LinkedHashMap<String, String>> actualAccountList = (ArrayList<LinkedHashMap<String, String>>) testRestTemplate
				.getForObject("http://localhost:" + port + "/api/account?custId=" + inputCustId, Object.class);

		// The expected list of accounts
		String[] accounts = expectedAccountListStr.split("\\|");

		// The number of accounts for the customer
		int numOfAccounts = Integer.valueOf(numOfAccountsStr);

		for (int i = 0; i < numOfAccounts; i++) {

			String[] fields = accounts[i].split("~");

			Map<String, String> expectedAccoutMap = new HashMap<>();

			Stream.of(fields).forEach(field -> {
				String[] keyAndVal = field.split("=");
				expectedAccoutMap.put(keyAndVal[0], keyAndVal[1]);
			});

			assertEquals(expectedAccoutMap.get("custId"), String.valueOf(actualAccountList.get(i).get("custId")));
			assertEquals(expectedAccoutMap.get("accountNum"),
					String.valueOf(actualAccountList.get(i).get("accountNum")));
			assertEquals(expectedAccoutMap.get("accountName"), actualAccountList.get(i).get("accountName"));
			assertEquals(expectedAccoutMap.get("accountType"), actualAccountList.get(i).get("accountType"));
			assertEquals(expectedAccoutMap.get("balanceDate"), actualAccountList.get(i).get("balanceDate"));
			assertEquals(expectedAccoutMap.get("currency"), actualAccountList.get(i).get("currency"));
			assertEquals(expectedAccoutMap.get("openingAvailableBalance"),
					String.valueOf(actualAccountList.get(i).get("openingAvailableBalance")));
		}
	}

	@Test
	public void shouldBeAbleToCatchExceptionInResponseIfRequestParameterIsMissingWhenGetAccountsByCustIdThroughAPICall() {
		Object object = testRestTemplate.getForObject("http://localhost:" + port + "/api/account", Object.class);

		// Assertion
		assertEquals("BAD_REQUEST", ((LinkedHashMap<String, String>) object).get("status"));
		assertThat(((LinkedHashMap<String, String>) object).get("message")
				.contains("Required String parameter 'custId' is not present"));
	}

	@Test
	public void shouldBeAbleToCatchExceptionInResponseIfRequestParameterIsIncomplete01WhenGetAccountsByCustIdThroughAPICall() {
		Object object = testRestTemplate.getForObject("http://localhost:" + port + "/api/account?", Object.class);

		// Assertion
		assertEquals("BAD_REQUEST", ((LinkedHashMap<String, String>) object).get("status"));
		assertThat(((LinkedHashMap<String, String>) object).get("message")
				.contains("Required String parameter 'custId' is not present"));
	}

	@Test
	public void shouldBeAbleToCatchExceptionInResponseIfRequestParameterIsIncomplete02WhenGetAccountsByCustIdThroughAPICall() {
		Object object = testRestTemplate.getForObject("http://localhost:" + port + "/api/account?custId", Object.class);

		// Assertion
		assertEquals("BAD_REQUEST", ((LinkedHashMap<String, String>) object).get("status"));
		assertThat(((LinkedHashMap<String, String>) object).get("message")
				.contains("Customer ID cannot be null nor empty"));
	}

	@Test
	public void shouldBeAbleToCatchExceptionInResponseIfRequestParameterIsEmptyWhenGetAccountsByCustIdThroughAPICall() {
		Object object = testRestTemplate.getForObject("http://localhost:" + port + "/api/account?custId=",
				Object.class);

		// Assertion
		assertEquals("BAD_REQUEST", ((LinkedHashMap<String, String>) object).get("status"));
		assertThat(((LinkedHashMap<String, String>) object).get("message")
				.contains("Customer ID cannot be null nor empty"));
	}

	@Test
	public void shouldBeAbleToCatchExceptionInResponseIfRequestParameterIsNotNumericWhenGetAccountsByCustIdThroughAPICall() {

		String nonNumericCharStr = "~`!@#$%^&*()-_+={[}]|\\:;\"'<,>.?/abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

		Stream.of(nonNumericCharStr.split("")).forEach(nonNumericChar -> {
			Object object = testRestTemplate
					.getForObject("http://localhost:" + port + "/api/account?custId=" + nonNumericChar, Object.class);

			// Assertion
			assertEquals("BAD_REQUEST", ((LinkedHashMap<String, String>) object).get("status"));
			assertThat(
					((LinkedHashMap<String, String>) object).get("message").contains("Customer ID must be a number"));
		});
	}

	@Test
	public void shouldBeAbleToCatchExceptionInResponseIfRequestParameterIsLessThan1WhenGetAccountsByCustIdThroughAPICall() {
		Object object = testRestTemplate.getForObject("http://localhost:" + port + "/api/account?custId=0",
				Object.class);

		// Assertion
		assertEquals("BAD_REQUEST", ((LinkedHashMap<String, String>) object).get("status"));
		assertThat(((LinkedHashMap<String, String>) object).get("message")
				.contains("Customer ID must not be less than 1"));
	}

	@Test
	public void shouldBeAbleToCatchExceptionInResponseIfRequestParameterIsMoreThan9999999999WhenGetAccountsByCustIdThroughAPICall() {
		Object object = testRestTemplate.getForObject("http://localhost:" + port + "/api/account?custId=10000000000",
				Object.class);

		// Assertion
		assertEquals("BAD_REQUEST", ((LinkedHashMap<String, String>) object).get("status"));
		assertThat(((LinkedHashMap<String, String>) object).get("message")
				.contains("Customer ID must not be larger than 9999999999"));
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/TransactionAPITestData.csv", numLinesToSkip = 1)
	public void shouldBeAbleToGetTransactionsByAccountNumThroughAPICall(String numOfTransactionsStr,
			String inputAccountNum, String expectedTransactionListStr) {

		// The actual list of transactions
		ArrayList<LinkedHashMap<String, String>> actualTransactionList = (ArrayList<LinkedHashMap<String, String>>) testRestTemplate
				.getForObject("http://localhost:" + port + "/api/account/transaction?accountNum=" + inputAccountNum,
						Object.class);

		// The expected list of transactions
		String[] transactions = expectedTransactionListStr.split("\\|");

		// The number of transactions for the account
		int numOftransactions = Integer.valueOf(numOfTransactionsStr);

		for (int i = 0; i < numOftransactions; i++) {

			String[] fields = transactions[i].split("~");

			Map<String, String> expectedTransactionMap = new HashMap<>();

			Stream.of(fields).forEach(field -> {
				String[] keyAndVal = field.split("=");
				expectedTransactionMap.put(keyAndVal[0], keyAndVal[1]);
			});

			// Assertion
			Object object = actualTransactionList.get(i).get("transactionPK");

			assertEquals(expectedTransactionMap.get("accountNum"),
					String.valueOf(((LinkedHashMap<String, String>) object).get("accountNum")));
			assertEquals(expectedTransactionMap.get("accountName"), actualTransactionList.get(i).get("accountName"));
			assertEquals(expectedTransactionMap.get("valueDate"),
					((LinkedHashMap<String, String>) object).get("valueDate"));
			assertEquals(expectedTransactionMap.get("currency"), actualTransactionList.get(i).get("currency"));
			assertEquals(expectedTransactionMap.get("debitAmt"),
					String.valueOf(actualTransactionList.get(i).get("debitAmt")));
			assertEquals(expectedTransactionMap.get("creditAmt"),
					String.valueOf(actualTransactionList.get(i).get("creditAmt")));
			assertEquals(expectedTransactionMap.get("debitCredit"), actualTransactionList.get(i).get("debitCredit"));
			assertEquals(expectedTransactionMap.get("transactionNarrative"),
					actualTransactionList.get(i).get("transactionNarrative"));
		}
	}

	@Test
	public void shouldBeAbleToCatchExceptionInResponseIfRequestParameterIsMissingWhenGetTransactionsByAccountNumThroughAPICall() {
		Object object = testRestTemplate.getForObject("http://localhost:" + port + "/api/account/transaction",
				Object.class);

		// Assertion
		assertEquals("BAD_REQUEST", ((LinkedHashMap<String, String>) object).get("status"));
		assertThat(((LinkedHashMap<String, String>) object).get("message")
				.contains("Required String parameter 'accountNum' is not present"));
	}

	@Test
	public void shouldBeAbleToCatchExceptionInResponseIfRequestParameterIsIncomplete01WhenGetTransactionsByAccountNumThroughAPICall() {
		Object object = testRestTemplate.getForObject("http://localhost:" + port + "/api/account/transaction?",
				Object.class);

		// Assertion
		assertEquals("BAD_REQUEST", ((LinkedHashMap<String, String>) object).get("status"));
		assertThat(((LinkedHashMap<String, String>) object).get("message")
				.contains("Required String parameter 'accountNum' is not present"));
	}

	@Test
	public void shouldBeAbleToCatchExceptionInResponseIfRequestParameterIsIncomplete02WhenGetTransactionsByAccountNumThroughAPICall() {
		Object object = testRestTemplate
				.getForObject("http://localhost:" + port + "/api/account/transaction?accountNum", Object.class);

		// Assertion
		assertEquals("BAD_REQUEST", ((LinkedHashMap<String, String>) object).get("status"));
		assertThat(((LinkedHashMap<String, String>) object).get("message")
				.contains("Account number cannot be null nor empty"));
	}

	@Test
	public void shouldBeAbleToCatchExceptionInResponseIfRequestParameterIsEmptyWhenGetTransactionsByAccountNumThroughAPICall() {
		Object object = testRestTemplate
				.getForObject("http://localhost:" + port + "/api/account/transaction?accountNum=", Object.class);

		// Assertion
		assertEquals("BAD_REQUEST", ((LinkedHashMap<String, String>) object).get("status"));
		assertThat(((LinkedHashMap<String, String>) object).get("message")
				.contains("Account number cannot be null nor empty"));
	}

	@Test
	public void shouldBeAbleToCatchExceptionInResponseIfRequestParameterIsNotNumericWhenGetTransactionsByAccountNumThroughAPICall() {

		String nonNumericCharStr = "~`!@#$%^&*()-_+={[}]|\\:;\"'<,>.?/abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

		Stream.of(nonNumericCharStr.split("")).forEach(nonNumericChar -> {
			Object object = testRestTemplate.getForObject(
					"http://localhost:" + port + "/api/account/transaction?accountNum=" + nonNumericChar, Object.class);

			// Assertion
			assertEquals("BAD_REQUEST", ((LinkedHashMap<String, String>) object).get("status"));
			assertThat(((LinkedHashMap<String, String>) object).get("message")
					.contains("Account number must be a number"));
		});
	}

	@Test
	public void shouldBeAbleToCatchExceptionInResponseIfRequestParameterIsLessThan1WhenGetTransactionsByAccountNumThroughAPICall() {
		Object object = testRestTemplate
				.getForObject("http://localhost:" + port + "/api/account/transaction?accountNum=0", Object.class);

		// Assertion
		assertEquals("BAD_REQUEST", ((LinkedHashMap<String, String>) object).get("status"));
		assertThat(((LinkedHashMap<String, String>) object).get("message")
				.contains("Account number must not be less than 1"));
	}

	@Test
	public void shouldBeAbleToCatchExceptionInResponseIfRequestParameterIsMoreThan9999999999WhenGetTransactionsByAccountNumThroughAPICall() {
		Object object = testRestTemplate.getForObject(
				"http://localhost:" + port + "/api/account/transaction?accountNum=10000000000", Object.class);

		// Assertion
		assertEquals("BAD_REQUEST", ((LinkedHashMap<String, String>) object).get("status"));
		assertThat(((LinkedHashMap<String, String>) object).get("message")
				.contains("Account number must not be larger than 9999999999"));
	}
}
