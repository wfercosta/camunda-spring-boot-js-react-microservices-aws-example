DELETE FROM purchase_order_item;
DELETE FROM purchase_order;

ALTER SEQUENCE purchase_order_item_seq RESTART WITH 1;
ALTER SEQUENCE purchase_order_seq RESTART WITH 1;