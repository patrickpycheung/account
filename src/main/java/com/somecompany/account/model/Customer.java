package com.somecompany.account.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class Customer {

	@NotNull(message = "Customer ID cannot be null")
	@Size(min = 1, max = 10, message = "Customer ID must have length between 1 and 10")
	private long custId;

	@Size(min = 0, max = 30, message = "Customer name must have length between 1 and 30")
	private String custName;
}
