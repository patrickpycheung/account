package com.somecompany.account.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Embeddable
@Data
public class TransactionPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "account_num")
	@NotNull(message = "Account number cannot be null")
	@Size(min = 1, max = 10, message = "Account number must have length between 1 and 10")
	private int accountNum;

	@Column(name = "value_date")
	@NotNull(message = "Value date cannot be null")
	private Timestamp valueDate;
}
