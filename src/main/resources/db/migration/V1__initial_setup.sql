CREATE TABLE users(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    email TEXT UNIQUE NOT NULL,
    created_date DATE NOT NULL
);

CREATE TABLE requested_toppings(
    user_id INTEGER NOT NULL,
    topping TEXT NOT NULL,
    created_date DATE NOT NULL,
    FOREIGN KEY(user_id) REFERENCES users(id)
);

CREATE UNIQUE INDEX unique_user_topping ON requested_toppings(user_id, topping);
