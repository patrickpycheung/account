package com.somecompany.account.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.somecompany.account.model.Transaction;

@Repository
@Transactional
/**
 * Data access object (DAO) for interacting with the "Transaction" table.
 * 
 * @author patrick
 *
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
