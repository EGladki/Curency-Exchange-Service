INSERT INTO currencies (code, full_name, sign)
VALUES ('USD', 'United States Dollar', '$'),
       ('EUR', 'Euro', '€'),
       ('GBP', 'British Pound Sterling', '£'),
       ('JPY', 'Japanese Yen', '¥'),
       ('TRY', 'Turkish Lira', '₺');

INSERT INTO Exchange_rates (base_currency_id, target_currency_id, rate)
VALUES (1, 2, 0.88),
       (1, 3, 0.76),
       (1, 4, 143.59),
       (1, 5, 37.87),
       (2, 3, 0.87);