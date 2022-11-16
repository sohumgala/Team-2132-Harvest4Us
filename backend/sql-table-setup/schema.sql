CREATE DATABASE harvest4us;
USE harvest4us;

DROP TABLE IF EXISTS consumer_allergies;
DROP TABLE IF EXISTS inventory_allergies;
DROP TABLE IF EXISTS carts;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS inventory;
DROP TABLE IF EXISTS consumer_users;
DROP TABLE IF EXISTS producer_users;
DROP TABLE IF EXISTS users;

CREATE TABLE users(
    username varchar(50) NOT NULL UNIQUE,
    first_name varchar(50) NOT NULL,
    last_name varchar(50) NOT NULL,
    password varchar(128) NOT NULL,
    PRIMARY KEY (username)
) engine = innodb;

CREATE TABLE producer_users(
    username varchar(50) NOT NULL UNIQUE, 
    business_name varchar(80) NOT NULL UNIQUE,
    zip_code varchar(10),
    street varchar(50),
    city varchar(25),
    st varchar(2),
    description TINYTEXT,
    PRIMARY KEY (username),
    FOREIGN KEY (username) REFERENCES users (username) 
  	ON DELETE CASCADE
  	ON UPDATE CASCADE
) engine = innodb;

CREATE TABLE consumer_users (
    username varchar(50) NOT NULL UNIQUE, 
    zip_code varchar(10),
    street varchar(50),
    city varchar(25),
    st varchar(2),
    PRIMARY KEY (username),
    FOREIGN KEY (username) REFERENCES users (username) 
  	ON DELETE CASCADE
  	ON UPDATE CASCADE
) engine = innodb;

CREATE TABLE consumer_allergies(
    username varchar(50) NOT NULL, 
    allergey varchar(25) NOT NULL,
    PRIMARY KEY (username, allergey),
    FOREIGN KEY (username) REFERENCES consumer_users (username)
  	ON DELETE CASCADE
  	ON UPDATE CASCADE
) engine = innodb;


CREATE TABLE inventory(
    producer varchar(50) NOT NULL, 
    product_id int NOT NULL,
    unit varchar(3) NOT NULL,
    produceType varchar(50) NOT NULL,
    organic int NOT NULL,
    usdaGrade varchar(1) NOT NULL,
    price float NOT NULL,
    availableQuantity float NOT NULL,
    produceCategory varchar(50) NOT NULL,
    dateEdited date NOT NULL,
    active int,
    PRIMARY KEY (producer, product_id),
    UNIQUE (producer, product_id),
    
    FOREIGN KEY (producer) REFERENCES producer_users (username) 
  	ON DELETE CASCADE
  	ON UPDATE CASCADE
) engine = innodb;

CREATE TABLE inventory_allergies(
    producer varchar(50) NOT NULL,
    product_id int NOT NULL,
    allergey varchar(25) NOT NULL,
    PRIMARY KEY (product_id, producer, allergey),
    FOREIGN KEY (producer, product_id) REFERENCES inventory (producer, product_id) 
  	ON DELETE CASCADE 
  	ON UPDATE CASCADE
) engine = innodb;

CREATE TABLE carts(
    producer varchar(50) NOT NULL, 
    product_id int NOT NULL,
    consumer varchar(50) NOT NULL, 
    date_added date NOT NULL,
    quantity int NOT NULL,
    PRIMARY KEY (consumer, producer, product_id),
    FOREIGN KEY (producer, product_id) REFERENCES inventory (producer, product_id) 
  	ON DELETE CASCADE 
  	ON UPDATE CASCADE,
  	FOREIGN KEY (consumer) REFERENCES users (username) 
  	ON DELETE CASCADE 
  	ON UPDATE CASCADE
) engine = innodb;

CREATE TABLE orders(
    producer varchar(50) NOT NULL, 
    product_id int NOT NULL,
    consumer varchar(50) NOT NULL, 
    date_placed datetime NOT NULL,
    quantity int NOT NULL,
    shipping float,
    total_cost float,
    sale_id int NOT NULL,
    pendingReview int NOT NULL,
    approved int NOT NULL,
    PRIMARY KEY (sale_id, consumer, producer, product_id),
 	FOREIGN KEY (producer, product_id) REFERENCES inventory (producer, product_id)
  	ON DELETE CASCADE 
  	ON UPDATE CASCADE,
    FOREIGN KEY (consumer) REFERENCES consumer_users (username) 
  	ON UPDATE CASCADE
  	ON DELETE CASCADE
) engine = innodb;

CREATE TABLE images(
	produceType varchar(50) NOT NULL,
    produceCategory varchar(50) NOT NULL,
    image varchar(50) NOT NULL,
    PRIMARY KEY (produceType, produceCategory)
) engine = innodb;