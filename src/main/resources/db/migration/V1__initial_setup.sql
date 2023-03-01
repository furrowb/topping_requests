CREATE TABLE users(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    email TEXT UNIQUE NOT NULL,
    created_date TEXT NOT NULL -- SQLite doesn't have great date support so we make it a text field instead
);

CREATE TABLE requested_toppings(
    user_id INTEGER NOT NULL,
    topping TEXT NOT NULL,
    created_date TEXT NOT NULL, -- SQLite doesn't have great date support so we make it a text field instead
    FOREIGN KEY(user_id) REFERENCES users(id)
);

CREATE UNIQUE INDEX unique_user_topping ON requested_toppings(user_id, topping);
