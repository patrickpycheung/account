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
import com.somecompany.account.model.Customer;
import com.somecompany.account.service.AccountService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class AccountTest {

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

			Map<String, String> expectedAccountMap = new HashMap<>();

			Stream.of(fields).forEach(field -> {
				String[] keyAndVal = field.split("=");
				expectedAccountMap.put(keyAndVal[0], keyAndVal[1]);
			});

			assertEquals(expectedAccountMap.get("custId"), String.valueOf(actualAccountList.get(i).get("custId")));
			assertEquals(expectedAccountMap.get("accountNum"),
					String.valueOf(actualAccountList.get(i).get("accountNum")));
			assertEquals(expectedAccountMap.get("accountName"), actualAccountList.get(i).get("accountName"));
			assertEquals(expectedAccountMap.get("accountType"), actualAccountList.get(i).get("accountType"));
			assertEquals(expectedAccountMap.get("balanceDate"), actualAccountList.get(i).get("balanceDate"));
			assertEquals(expectedAccountMap.get("currency"), actualAccountList.get(i).get("currency"));
			assertEquals(expectedAccountMap.get("openingAvailableBalance"),
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
}
