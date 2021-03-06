INSERT INTO ACCOUNT (cust_id, account_num, account_name, account_type, balance_date, currency, opening_available_balance) VALUES
  (1111111111, 1111111111, 'AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA', 'Savings', TIMESTAMP '2020-11-01 11:01:01', 'SGD', 99999.99),
  (2, 2, 'B', 'Savings', TIMESTAMP '2020-11-02 11:02:02', 'AUD', 0.0),
  (1111111111, 3333333333, 'CCCCCCCCCCCCCCCCCCCCCCCCCCCCCC', 'Current', TIMESTAMP '2020-11-03 11:03:03', 'USD', 99999.99);

INSERT INTO TRANSACTION (account_num, account_name, value_date, currency, debit_amt, credit_amt, debit_credit, transaction_narrative) VALUES
  (1111111111, 'AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA', TIMESTAMP '2012-11-01 11:01:01', 'SGD', 0.0, 99999.99, 'Credit', 'AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA'),
  (2, 'Savings Account', TIMESTAMP '2012-11-02 11:02:02', 'USD', 0.1, 0.0, 'Debit', null),
  (1111111111, 'CCCCCCCCCCCCCCCCCCCCCCCCCCCCCC', TIMESTAMP '2012-11-03 11:03:03', 'USD', 99999.99, 0.0, 'Debit', 'CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC');
