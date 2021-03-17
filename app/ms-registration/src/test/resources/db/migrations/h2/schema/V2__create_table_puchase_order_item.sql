CREATE TABLE purchase_order_item (
  id INT NOT NULL,
  purchase_order_id INT NOT NULL,
  is_virtual BOOLEAN NOT NULL,
  PRIMARY KEY (id)
--  FOREIGN KEY (purchase_order_id) REFERENCES purchase_order (id)
);

CREATE SEQUENCE purchase_order_item_seq
  START WITH 1
  INCREMENT BY 1
  MINVALUE 1;