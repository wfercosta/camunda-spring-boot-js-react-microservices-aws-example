CREATE TABLE invoices (
  id INT NOT NULL,
  order_id INT NOT NULL,
  total DOUBLE NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
  PRIMARY KEY (id),
  FOREIGN KEY (order_id) REFERENCES purchase_orders(id)
);

CREATE SEQUENCE invoices_seq
  START WITH 1
  INCREMENT BY 1
  MINVALUE 1;

