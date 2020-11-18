package com.somecompany.account.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Entity
@Data
public class Account {

	@Column(name = "cust_id")
	@NotNull(message = "Customer ID cannot be null")
	@Min(value = 1L, message = "Customer ID must not be less than 1")
	@Max(value = 9999999999L, message = "Customer ID must not be larger than 9999999999")
	private long custId;

	@Column(name = "account_num")
	@Id
	@NotNull(message = "Account number cannot be null")
	@Min(value = 1L, message = "Account number  must not be less than 1")
	@Max(value = 9999999999L, message = "Account number must not be larger than 9999999999")
	private long accountNum;

	@Column(name = "account_name")
	@NotNull(message = "Account name cannot be null")
	@Size(min = 1, max = 30, message = "Account name must have length between 1 and 30")
	private String accountName;

	@Column(name = "account_type")
	@NotNull(message = "Account type cannot be null")
	@Size(min = 1, max = 7, message = "Account type must have length between 1 and 7")
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
