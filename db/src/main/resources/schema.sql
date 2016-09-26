DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS balance_statistic;
DROP TABLE IF EXISTS customers;

CREATE TABLE customers (
  id            MEDIUMINT AUTO_INCREMENT,
  customer_name VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE transactions (
  id          MEDIUMINT AUTO_INCREMENT,
  time        TIMESTAMP NOT NULL,
  amount      DECIMAL   NOT NULL,
  sender_id   MEDIUMINT,
  receiver_id MEDIUMINT,
  CONSTRAINT fk_sender_id FOREIGN KEY (sender_id)
  REFERENCES customers (id),
  CONSTRAINT fk_receiver_id FOREIGN KEY (receiver_id)
  REFERENCES customers (id),
  PRIMARY KEY (id)
);

CREATE TABLE balance_statistic (
  id          MEDIUMINT AUTO_INCREMENT,
  customer_id MEDIUMINT,
  balance     DECIMAL NOT NULL,
  time        TIMESTAMP,
  PRIMARY KEY (id)
);


