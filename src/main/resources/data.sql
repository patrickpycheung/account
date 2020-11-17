INSERT INTO ACCOUNT (cust_id, account_num, account_name, account_type, balance_date, currency, opening_available_balance) VALUES
  (1, 585309209, 'SGSavings726', 'Savings', TIMESTAMP '2018-11-08 00:00:00', 'SGD', 84327.51),
  (2, 791066619, 'AUSavings933', 'Savings', TIMESTAMP '2018-11-08 00:00:00', 'AUD', 88005.93);

INSERT INTO TRANSACTION (account_num, account_name, value_date, currency, debit_amt, credit_amt, debit_credit, transaction_narrative) VALUES
  (585309209, 'Current Account', TIMESTAMP '2012-01-12 00:00:00', 'SGD', null, 9540.98, 'Credit', 'Rent payment'),
  (1232223212, 'Savings Account', TIMESTAMP '2012-01-12 00:00:00', 'USD', 1234.56, null, 'Debit', null);
