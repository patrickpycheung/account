package com.somecompany.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
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
import com.somecompany.account.model.Transaction;
import com.somecompany.account.model.TransactionPK;
import com.somecompany.account.service.AccountService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class TransactionTest {

	@Autowired
	private AccountService accountService;

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate testRestTemplate;

	@BeforeAll
	public static void setup() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
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
			Account expectedTransactionAccount = new Account();
			expectedTransactionAccount.setAccountNum(Long.valueOf(transaction[0]));
			expectedTransactionPK.setAccount(expectedTransactionAccount);

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
