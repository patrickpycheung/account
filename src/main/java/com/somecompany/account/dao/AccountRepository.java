package com.somecompany.account.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.somecompany.account.model.Account;

@Repository
@Transactional
public interface AccountRepository extends JpaRepository<Account, Long> {

}
