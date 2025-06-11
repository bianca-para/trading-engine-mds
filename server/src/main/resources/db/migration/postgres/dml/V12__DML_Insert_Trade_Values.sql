INSERT INTO trade(trade_id, buyer_id, seller_id, buy_order_id, sell_order_id, asset_id, price, quantity, executed_at) VALUES
                                                                                                                          -- Alice buys 0.005 BTC from Bob
                                                                                                                          ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
                                                                                                                           '11111111-1111-1111-1111-111111111111',  -- buyer: Alice
                                                                                                                           '22222222-2222-2222-2222-222222222222',  -- seller: Bob
                                                                                                                           1, 2, 1,    99500.00,   0.0050, '2025-06-10 09:20:00'),
                                                                                                                          -- Carol sells 500 DOGE to Alice
                                                                                                                          ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb',
                                                                                                                           '11111111-1111-1111-1111-111111111111',  -- buyer: Alice
                                                                                                                           '33333333-3333-3333-3333-333333333333',  -- seller: Carol
                                                                                                                           4, 3, 9,      0.19,      500,    '2025-06-10 09:25:00');