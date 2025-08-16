-- Cash-in simulation for Charlie (user_id = 3)
INSERT INTO transaction (amount, name, account_id, transfer_from_id)
VALUES (500.00, 'Cash In', 3, NULL);

UPDATE balance
SET amount = amount + 500.00
WHERE user_id = 3;
