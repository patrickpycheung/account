package com.somecompany.account.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.somecompany.account.dao.AccountRepository;
import com.somecompany.account.model.Account;
import com.somecompany.account.model.Customer;

@Service
public class AccountService {

	@Autowired
	private AccountRepository accountRepository;

	public List<Account> getAllAccounts(Customer customer) {

		Account inputAccount = new Account();
		inputAccount.setCustId(customer.getCustId());

		ExampleMatcher matcher = ExampleMatcher.matchingAll().withIgnorePaths("accountNum", "openingAvailableBalance");

		Example<Account> example = Example.of(inputAccount, matcher);

		List<Account> acctList = accountRepository.findAll(example, Sort.by(Sort.Direction.ASC, "accountNum"));

		return acctList;
	}
}
