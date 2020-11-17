package com.somecompany.account.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Entity
@Data
public class Account {

	@Column(name = "cust_id")
	@Id
	@NotNull(message = "Customer ID cannot be null")
	@Size(min = 1, max = 10, message = "Customer ID must have length between 1 and 10")
	private int custId;

	@Column(name = "account_num")
	@NotNull(message = "Account number cannot be null")
	@Size(min = 1, max = 10, message = "Account number must have length between 1 and 10")
	private int accountNum;

	@Column(name = "account_name")
	@NotNull(message = "Account name cannot be null")
	@Size(min = 1, max = 30, message = "Account name must have length between 1 and 30")
	private String accountName;

	@Column(name = "account_type")
	@NotNull(message = "Account type cannot be null")
	@Size(min = 1, max = 30, message = "Account type must have length between 1 and 10")
	private String accountType;

	@Column(name = "balance_date")
	@NotNull(message = "Balance date cannot be null")
	private Timestamp balanceDate;

	@Column(name = "currency")
	@NotNull(message = "Currency cannot be null")
	@Size(min = 3, max = 3, message = "Currency must have length exactly equal to 3")
	private String currency;

	@Column(name = "opening_available_balance", columnDefinition = "Decimal(20,2) default '0.0'")
	@NotNull(message = "Opening available balance cannot be null")
	@DecimalMin(value = "0.0", message = "Opening available balance cannot be negative")
	private float openingAvailableBalance;
}
