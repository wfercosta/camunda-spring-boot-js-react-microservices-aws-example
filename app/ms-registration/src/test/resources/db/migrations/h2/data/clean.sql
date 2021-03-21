DELETE FROM products;
DELETE FROM purchase_order_items;
DELETE FROM purchase_orders;

ALTER SEQUENCE purchase_order_item_seq RESTART WITH 1;
ALTER SEQUENCE purchase_order_seq RESTART WITH 1;