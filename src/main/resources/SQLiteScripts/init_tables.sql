CREATE TABLE IF NOT EXISTS currencies
(
    id        INTEGER PRIMARY KEY AUTOINCREMENT,
    code      TEXT CHECK ( length(code) == 3 ) NOT NULL UNIQUE,
    full_name TEXT                             NOT NULL,
    sign      TEXT                             NOT NULL
);

CREATE TABLE IF NOT EXISTS exchange_rates
(
    id                 INTEGER PRIMARY KEY AUTOINCREMENT,
    base_currency_id   INTEGER NOT NULL,
    target_currency_id INTEGER NOT NULL,
    rate               REAL    NOT NULL,
    FOREIGN KEY (base_currency_id) REFERENCES currencies (id),
    FOREIGN KEY (target_currency_id) REFERENCES currencies (id),
    UNIQUE (base_currency_id, target_currency_id)
);