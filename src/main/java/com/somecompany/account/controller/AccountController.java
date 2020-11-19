package com.somecompany.account.controller;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.somecompany.account.model.Account;
import com.somecompany.account.model.Customer;
import com.somecompany.account.model.Transaction;
import com.somecompany.account.service.AccountService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/account")
@Validated
@Slf4j
public class AccountController {

	@Autowired
	private AccountService accountService;

	@GetMapping(path = "", produces = "application/json")
	@ApiOperation(value = "Get a list of accounts for the customer")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully retrieved account list", responseContainer = "List", response = Account.class) })
	public ResponseEntity<List<Account>> getAllAccounts(
			@RequestParam @NotNull(message = "Customer ID cannot be null") @NotEmpty(message = "Customer ID cannot be empty") @Pattern(regexp = "^[0-9]+$", message = "Customer ID must be a number") @Min(value = 1L, message = "Customer ID must not be less than 1") @Max(value = 9999999999L, message = "Customer ID must not be larger than 9999999999") String custId) {

		Customer customer = new Customer();
		customer.setCustId(Long.valueOf(custId));

		log.info("Retrieved accounts for customer with custId " + custId);
		return ResponseEntity.ok(accountService.getAllAccounts(customer));
	}

	@GetMapping(path = "/transaction", produces = "application/json")
	@ApiOperation(value = "Get a list of transactions for the account")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully retrieved transaction list", responseContainer = "List", response = Transaction.class) })
	public ResponseEntity<List<Transaction>> getAllTransactions(
			@RequestParam @NotEmpty(message = "Account number cannot be null nor empty") @Pattern(regexp = "^[0-9]+$", message = "Account number must be a number") @Min(value = 1L, message = "Account number must not be less than 1") @Max(value = 9999999999L, message = "Account number must not be larger than 9999999999") String accountNum) {
		Account account = new Account();
		account.setAccountNum(Long.valueOf(accountNum));

		return ResponseEntity.ok(accountService.getAllTransactions(account));
	}
}
