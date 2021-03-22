CREATE TABLE purchase_order_items (
  id INT NOT NULL AUTO_INCREMENT,
  order_id INT NOT NULL,
  sku VARCHAR(60) NOT NULL,
  price DOUBLE NOT NULL,
  quantity INT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
  PRIMARY KEY (id),
  FOREIGN KEY (order_id) REFERENCES purchase_orders(id)
);

