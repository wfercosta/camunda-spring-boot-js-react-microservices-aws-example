
INSERT INTO purchase_orders (id, status, customer_id) VALUES (1, 'ORDER_NEW', 1);
INSERT INTO purchase_order_items (id, order_id, sku, price, quantity) VALUES (1, 1, 'APPLE-MACBOOKPRO-15-ALUMINIUM', 10.0, 1);
INSERT INTO purchase_order_items (id, order_id, sku, price, quantity) VALUES (2, 1, 'MICRO-XBOX-BLACK-SERIESX', 10.0, 1);


INSERT INTO purchase_orders (id, status, customer_id) VALUES (2, 'ORDER_NEW', 1);
INSERT INTO purchase_order_items (id, order_id, sku, price, quantity) VALUES (3, 2, 'APPLE-MACBOOKPRO-15-ALUMINIUM', 10.0, 1);
INSERT INTO purchase_order_items (id, order_id, sku, price, quantity) VALUES (4, 2, 'MICRO-XBOX-BLACK-SERIESX', 10.0, 1);


INSERT INTO purchase_orders (id, status, customer_id) VALUES (3, 'ORDER_PENDING_PAYMENT', 1);
INSERT INTO purchase_order_items (id, order_id, sku, price, quantity) VALUES (5, 3, 'APPLE-MACBOOKPRO-15-ALUMINIUM', 10.0, 1);
INSERT INTO purchase_order_items (id, order_id, sku, price, quantity) VALUES (6, 3, 'MICRO-XBOX-BLACK-SERIESX', 10.0, 1);


INSERT INTO products (id, sku, amount, is_dispatchable) VALUES (1, 'APPLE-MACBOOKPRO-15-ALUMINIUM', 500, 1);
INSERT INTO products (id, sku, amount, is_dispatchable) VALUES (2, 'MICRO-XBOX-BLACK-SERIESX', 500, 1);

