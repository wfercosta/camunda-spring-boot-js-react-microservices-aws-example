DELETE FROM invoices;
DELETE FROM purchase_order_items;
DELETE FROM purchase_orders;
DELETE FROM products;

ALTER TABLE invoices ALTER COLUMN id RESTART WITH 1;
ALTER TABLE purchase_order_items ALTER COLUMN id RESTART WITH 1;
ALTER TABLE purchase_orders ALTER COLUMN id RESTART WITH 1;
ALTER TABLE products ALTER COLUMN id RESTART WITH 1;
