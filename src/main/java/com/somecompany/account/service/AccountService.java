package com.somecompany.account.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.somecompany.account.dao.AccountRepository;
import com.somecompany.account.dao.TransactionRepository;
import com.somecompany.account.model.Account;
import com.somecompany.account.model.Customer;
import com.somecompany.account.model.Transaction;
import com.somecompany.account.model.TransactionPK;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
/**
 * The logic unit which carries out the request operation.
 * 
 * @author patrick
 *
 */
public class AccountService {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	/**
	 * Get a list of accounts for the customer.
	 * 
	 * @param customer
	 * @return List<Account>
	 */
	public List<Account> getAllAccounts(Customer customer) {

		Account inputAccount = new Account();
		inputAccount.setCustId(customer.getCustId());

		ExampleMatcher matcher = ExampleMatcher.matchingAll().withIgnorePaths("accountNum", "openingAvailableBalance");

		Example<Account> example = Example.of(inputAccount, matcher);

		List<Account> acctList = accountRepository.findAll(example, Sort.by(Sort.Direction.ASC, "accountNum"));

		log.info("\n##################################################\n"
				+ "Retrieved account list for customer with customer ID " + customer.getCustId()
				+ "\n##################################################");

		return acctList;
	}

	/**
	 * Get a list of transactions for the account.
	 * 
	 * @param account
	 * @return List<Transaction>
	 */
	public List<Transaction> getAllTransactions(Account account) {

		TransactionPK inputTransactionPK = new TransactionPK();
		inputTransactionPK.setAccount(account);

		Transaction inputTransaction = new Transaction();
		inputTransaction.setTransactionPK(inputTransactionPK);

		ExampleMatcher matcher = ExampleMatcher.matchingAll().withIgnorePaths("debitAmt", "creditAmt",
				"transactionPK.account.custId", "transactionPK.account.openingAvailableBalance");

		Example<Transaction> example = Example.of(inputTransaction, matcher);

		List<Transaction> transactionList = transactionRepository.findAll(example);

		log.info("\n##################################################\n"
				+ "Retrieved transaction list for account with account number " + account.getAccountNum()
				+ "\n##################################################");

		return transactionList;
	}
}
