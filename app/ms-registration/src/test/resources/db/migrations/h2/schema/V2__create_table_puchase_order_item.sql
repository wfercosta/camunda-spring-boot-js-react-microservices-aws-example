CREATE TABLE purchase_order_item (
  id INT NOT NULL,
  order_id INT NOT NULL,
  sku VARCHAR(60) NOT NULL,
  price DOUBLE NOT NULL,
  quantity INT NOT NULL,
  is_virtual BOOLEAN NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
  PRIMARY KEY (id)
--  FOREIGN KEY (purchase_order_id) REFERENCES purchase_order (id)
);

CREATE SEQUENCE purchase_order_item_seq
  START WITH 1
  INCREMENT BY 1
  MINVALUE 1;
