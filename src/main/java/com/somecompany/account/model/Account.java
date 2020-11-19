package com.somecompany.account.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Entity
@Data
/**
 * The model class for "Account" table.
 * 
 * @author patrick
 *
 */
public class Account {

	@Column(name = "cust_id")
	@NotEmpty(message = "Customer ID cannot be null nor empty")
	@Pattern(regexp = "^[0-9]+$", message = "Customer ID must be a number")
	@Min(value = 1L, message = "Customer ID must not be less than 1")
	@Max(value = 9999999999L, message = "Customer ID must not be larger than 9999999999")
	private long custId;

	@Column(name = "account_num")
	@Id
	@NotEmpty(message = "Account number cannot be null nor empty")
	@Pattern(regexp = "^[0-9]+$", message = "Account number must be a number")
	@Min(value = 1L, message = "Account number  must not be less than 1")
	@Max(value = 9999999999L, message = "Account number must not be larger than 9999999999")
	private long accountNum;

	@Column(name = "account_name")
	@NotEmpty(message = "Account name cannot be null nor empty")
	@Size(min = 1, max = 30, message = "Account name must have length between 1 and 30")
	private String accountName;

	@Column(name = "account_type")
	@NotEmpty(message = "Account type cannot be null nor empty")
	@Size(min = 1, max = 7, message = "Account type must have length between 1 and 7")
	private String accountType;

	@Column(name = "balance_date")
	@NotEmpty(message = "Balance date cannot be null nor empty")
	private Timestamp balanceDate;

	@Column(name = "currency")
	@NotEmpty(message = "Currency cannot be null nor empty")
	@Size(min = 3, max = 3, message = "Currency must have length exactly equal to 3")
	private String currency;

	@Column(name = "opening_available_balance", columnDefinition = "Decimal(20,2) default '0.0'")
	@NotEmpty(message = "Opening available balance cannot be null nor empty")
	@Pattern(regexp = "^[0-9.]+$", message = "Opening available balance must be a decimal number")
	@DecimalMin(value = "0.0", message = "Opening available balance cannot be negative")
	private float openingAvailableBalance;
}
