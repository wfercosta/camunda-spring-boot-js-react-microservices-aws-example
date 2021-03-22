CREATE TABLE invoices (
  id INT NOT NULL AUTO_INCREMENT,
  order_id INT NOT NULL,
  total DOUBLE NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
  PRIMARY KEY (id),
  FOREIGN KEY (order_id) REFERENCES purchase_orders(id)
);

