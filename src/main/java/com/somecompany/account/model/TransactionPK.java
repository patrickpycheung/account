package com.somecompany.account.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Embeddable
@Data
public class TransactionPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "account_num")
	@NotEmpty(message = "Account number cannot be null nor empty")
	@Min(value = 1L, message = "Account number  must not be less than 1")
	@Max(value = 9999999999L, message = "Account number must not be larger than 9999999999")
	private long accountNum;

	@Column(name = "value_date")
	@NotEmpty(message = "Value date cannot be null nor empty")
	private Timestamp valueDate;
}
