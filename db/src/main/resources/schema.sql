DROP TABLE IF EXISTS customers;
CREATE TABLE customers (
  id           MEDIUMINT AUTO_INCREMENT,
  customer_name VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS transaction_table;
CREATE TABLE transaction_table (
  id         MEDIUMINT AUTO_INCREMENT,
  time       TIMESTAMP NOT NULL,
  amount     DECIMAL   NOT NULL,
  sender_id   INT       NOT NULL,
  receiver_id INT       NOT NULL,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS balance_statistic;
CREATE TABLE balance_statistic (
  id      MEDIUMINT AUTO_INCREMENT,
  customer_id      MEDIUMINT ,
  balance DECIMAL NOT NULL,
  time    TIMESTAMP,
  PRIMARY KEY (id)
);


