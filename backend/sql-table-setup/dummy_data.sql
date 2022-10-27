USE harvest4us;

-- users
INSERT INTO users VALUES
( "fff", "Fred", "Fred", "ggg"),
("test@test.com", "Alice", "Alison", "password"),
("fake@test.com", "Fake", "Name", "password");

-- producers
INSERT INTO producer_users VALUES
("test@test.com", "Test Business", "11345", "ABC Lane", "A City", "GA", "A VERY Busy Buisness"),
("fake@test.com", "A Business", "443251", "Street Avenue", "New City", "GA", "A Busy Business");

-- consumers
INSERT INTO consumer_users VALUES
("fff", "11345", "DEC Lane", "A City", "GA");

-- consumer_allergies
INSERT INTO consumer_allergies VALUES
("fff", "peanut"),
("fff", "egg"),
("fff", "dairy");

-- inventory
INSERT INTO inventory VALUES 
("test@test.com", 1, "lb", "red",          0, "A", 1.00, 10, "apple", 	"2021-9-21", 1),
("test@test.com", 2, "lb", "granny smith", 1, "B", 1.30, 15, "apple",  	"2021-10-20", 1),
("fake@test.com", 1, "g" , "large",        0, "A", 3.25, 5,  "banana", 	"2021-11-02", 1), 
("fake@test.com", 2, "g" , "fresh green",  1, "C", 2.50, 6,  "kale",   	"2021-11-05", 1);

-- inventory_allergies
INSERT INTO inventory_allergies VALUES 
("fake@test.com", 1, "soy");

-- carts
INSERT INTO carts VALUES 
("test@test.com", 2, "fff", "2021-11-10", 2),
("fake@test.com", 1, "fff", "2021-11-10", 3); 
    
-- orders
INSERT INTO orders VALUES 
("test@test.com", 1, "fff", "2021-11-20", 3, 0.00, 1.00, 0, 1, 0),
("fake@test.com", 2, "fff", "2021-11-20", 1, 0.00, 2.50, 0, 1, 0);