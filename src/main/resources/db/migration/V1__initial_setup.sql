CREATE TABLE users(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    email TEXT UNIQUE,
    created_time DATETIME
);

CREATE TABLE requested_toppings(
    user_id INTEGER,
    topping TEXT,
    created_time DATETIME
);

CREATE UNIQUE INDEX unique_user_topping ON requested_toppings(user_id, topping);
