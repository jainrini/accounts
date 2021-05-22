CREATE SCHEMA accounts;

CREATE TABLE accounts.account (
    id                serial PRIMARY KEY,-- unique key
    customer_id INT NOT NULL,
    country_code      VARCHAR (10) NOT NULL,
    currency_code  VARCHAR(5)               NOT NULL,
    created_dtime     TIMESTAMP WITH TIME ZONE NOT NULL,
    modified_dtime    TIMESTAMP WITH TIME ZONE NOT NULL
);


CREATE TABLE accounts.balance (
    id             serial PRIMARY KEY,-- unique key
    customer_id    INT                      NOT NULL,
    account_id      INT                      NOT NULL,
    balance_amount  NUMERIC(16, 2)           NOT NULL, -- Account balance in given currency
    currency_code  VARCHAR(5)               NOT NULL,
    created_dtime  TIMESTAMP WITH TIME ZONE NOT NULL,
    modified_dtime TIMESTAMP WITH TIME ZONE NOT NULL,
    FOREIGN KEY (account_id)
        REFERENCES accounts.account(id)
);

CREATE TABLE accounts.account_transaction
(
    id                     serial PRIMARY KEY,
    account_id             INT                  NOT NULL, -- Reference to account
    direction_code         VARCHAR(3)               NOT NULL, -- Direction code
    currency_code          VARCHAR(3)               NOT NULL, -- Transaction currency code
    amount                 NUMERIC(16, 2)           NOT NULL, -- Transaction amount
    description                VARCHAR(140)             NULL, -- Transaction details
    initial_balance_amount NUMERIC(16, 2)           NOT NULL, -- balance on account before transaction
    balance_after_transaction NUMERIC(16, 2)           NOT NULL, -- balance on after before transaction
    created_dtime          TIMESTAMP WITH TIME ZONE NOT NULL,
    modified_dtime         TIMESTAMP WITH TIME ZONE NOT NULL
);

drop table accounts.account_transaction
CREATE UNIQUE INDEX uq_accountId_currencyCode
    ON accounts.balance(account_id, currency_code);


CREATE UNIQUE INDEX uq_accountId_currencyCode
    ON accounts.balance(account_id, currency_code);



