CREATE SCHEMA accounts;

CREATE TABLE accounts.account (
    id             serial PRIMARY KEY,-- unique key
    customer_id    INT                      NOT NULL,
    country_code   VARCHAR(10)              NOT NULL,
    created_dtime  TIMESTAMP WITH TIME ZONE NOT NULL,
    modified_dtime TIMESTAMP WITH TIME ZONE NOT NULL
);


CREATE TABLE accounts.balance (
    id             serial PRIMARY KEY,-- unique key
    customer_id    INT                      NOT NULL,
    account_id     INT                      NOT NULL,
    balance_amount NUMERIC(16, 2)           NOT NULL,
    currency_code  VARCHAR(5)               NOT NULL,
    created_dtime  TIMESTAMP WITH TIME ZONE NOT NULL,
    modified_dtime TIMESTAMP WITH TIME ZONE NOT NULL,
    FOREIGN KEY (account_id)
        REFERENCES accounts.account(id)
);

CREATE TABLE accounts.account_transaction (
    id                        serial PRIMARY KEY,
    account_id                INT                      NOT NULL,
    direction_code            VARCHAR(3)               NOT NULL,
    currency_code             VARCHAR(3)               NOT NULL,
    amount                    NUMERIC(16, 2)           NOT NULL,
    description               VARCHAR(140)             NOT NULL,
    initial_balance_amount    NUMERIC(16, 2)           NOT NULL,
    balance_after_transaction NUMERIC(16, 2)           NOT NULL,
    created_dtime             TIMESTAMP WITH TIME ZONE NOT NULL,
    modified_dtime            TIMESTAMP WITH TIME ZONE NOT NULL
);



