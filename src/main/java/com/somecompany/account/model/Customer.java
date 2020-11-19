package com.somecompany.account.model;

import javax.persistence.Column;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class Customer {

	@Column(name = "cust_id")
	@NotEmpty(message = "Customer ID cannot be null nor empty")
	@Pattern(regexp = "^[0-9]+$", message = "Customer ID must be a number")
	@Min(value = 1L, message = "Customer ID must not be less than 1")
	@Max(value = 9999999999L, message = "Customer ID must not be larger than 9999999999")
	private long custId;

	@Size(min = 0, max = 30, message = "Customer name must have length between 1 and 30")
	private String custName;
}
