package com.somecompany.account.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Entity
@Data
public class Transaction {

	@EmbeddedId
	private TransactionPK transactionPK;

	@Column(name = "account_name")
	@NotNull(message = "Account name cannot be null")
	@Size(min = 1, max = 30, message = "Account name must have length between 1 and 30")
	private String accountName;

	@Column(name = "currency")
	@NotNull(message = "Currency cannot be null")
	@Size(min = 3, max = 3, message = "Currency must have length exactly equal to 3")
	private String currency;

	@Column(name = "debit_amt", columnDefinition = "Decimal(20,2) default '0.0'")
	@DecimalMin(value = "0.0", message = "Debit amount cannot be negative")
	private float debitAmt;

	@Column(name = "credit_amt", columnDefinition = "Decimal(20,2) default '0.0'")
	@DecimalMin(value = "0.0", message = "Credit amount cannot be negative")
	private float creditAmt;

	@Column(name = "debit_credit")
	@NotNull(message = "Debit/Credit cannot be null")
	@Size(min = 1, max = 6, message = "Debit/Credit must have length between 1 and 6")
	private String debitCredit;

	@Column(name = "transaction_narrative")
	@Size(min = 0, max = 50, message = "Transaction narrative must have length between 0 and 50")
	private String transactionNarrative;
}
