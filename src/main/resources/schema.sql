DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS menu_dishes;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS menus;
DROP TABLE IF EXISTS dishes;
DROP TABLE IF EXISTS restaurants;

DROP SEQUENCE IF EXISTS global_seq;

CREATE SEQUENCE global_seq START WITH 100000;

CREATE TABLE users
(
    id         INTEGER   DEFAULT nextval('global_seq') PRIMARY KEY,
    name       VARCHAR                 NOT NULL,
    email      VARCHAR UNIQUE          NOT NULL,
    password   VARCHAR                 NOT NULL,
    enabled    BOOL      DEFAULT TRUE  NOT NULL,
    registered TIMESTAMP DEFAULT now() NOT NULL

);

CREATE TABLE restaurants
(
    id     INTEGER DEFAULT nextval('global_seq') PRIMARY KEY,
    name   VARCHAR UNIQUE       NOT NULL,
    active BOOL    DEFAULT TRUE NOT NULL
);

CREATE TABLE user_roles
(
    user_id INTEGER NOT NULL,
    role    VARCHAR,
    CONSTRAINT user_roles_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE dishes
(
    id            INTEGER DEFAULT nextval('global_seq') PRIMARY KEY,
    name          VARCHAR              NOT NULL,
    price         INTEGER              NOT NULL,
    active        BOOL    DEFAULT TRUE NOT NULL,
    restaurant_id INTEGER              NOT NULL,
    FOREIGN KEY (restaurant_id) REFERENCES restaurants (id) ON DELETE CASCADE,
    CONSTRAINT dishes_unique_name_restaurant_idx UNIQUE (name, restaurant_id)
);

CREATE TABLE menus
(
    id            INTEGER   DEFAULT nextval('global_seq') PRIMARY KEY,
    date          TIMESTAMP DEFAULT now() NOT NULL,
    restaurant_id INTEGER                 NOT NULL,
    FOREIGN KEY (restaurant_id) REFERENCES restaurants (id) ON DELETE CASCADE,
    CONSTRAINT menus_unique_date_restaurant_idx UNIQUE (date, restaurant_id)
);

CREATE TABLE menu_dishes
(
    menu_id INTEGER NOT NULL,
    dish_id INTEGER NOT NULL,
    FOREIGN KEY (menu_id) REFERENCES menus (id) ON DELETE CASCADE,
    FOREIGN KEY (dish_id) REFERENCES dishes (id) ON DELETE CASCADE,
    CONSTRAINT menu_dishes_idx UNIQUE (menu_id, dish_id)
)