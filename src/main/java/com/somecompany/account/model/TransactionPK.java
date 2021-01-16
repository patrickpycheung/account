package com.somecompany.account.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
/**
 * The model class for the EmbeddedId (i.e. primary key) of the "Transaction" table.
 * 
 * @author patrick
 *
 */
public class TransactionPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "account_num", referencedColumnName = "account_num", insertable = false, updatable = false, nullable = false)
	@JsonManagedReference
	private Account account;

	@Column(name = "value_date")
	@NotEmpty(message = "Value date cannot be null nor empty")
	private Timestamp valueDate;
}
