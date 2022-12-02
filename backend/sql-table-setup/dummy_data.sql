USE harvest4us;

-- users
INSERT INTO users VALUES
( "fred123", "Fred", "Fred", "fred_password"),
("test@test.com", "Alice", "Alison", "password"),
("fake@test.com", "Fake", "Name", "password");

-- producers
INSERT INTO producer_users VALUES
("test@test.com", "Alice's Apple Farm", "11345", "ABC Lane", "Atlanta", "GA", "Georgia's Best Apple Farm"),
("fake@test.com", "Patrick's Produce", "443251", "Street Avenue", "Decatur", "GA", "The Freshest Produce");

-- consumers
INSERT INTO consumer_users VALUES
("fred123", "11345", "DEC Lane", "A City", "GA");

-- consumer_allergies
INSERT INTO consumer_allergies VALUES
("fred123", "peanut"),
("fred123", "egg"),
("fred123", "dairy");

-- inventory
INSERT INTO inventory VALUES 
("test@test.com", 1, "lb", "red",          0, "A", 1.00, 10, "apple", 	"2021-9-21", 1),
("test@test.com", 2, "lb", "granny smith", 1, "B", 1.30, 15, "apple",  	"2021-10-20", 1),
("fake@test.com", 1, "g" , "large",        0, "A", 3.25, 5,  "banana", 	"2021-11-02", 1), 
("fake@test.com", 2, "g" , "fresh green",  1, "C", 2.50, 6,  "kale",   	"2021-11-05", 1),
("fake@test.com", 3, "ct", "hass",         0, "C", 1.25, 7,  "avocado",   	"2021-11-05", 1),
("fake@test.com", 4, "lb", "yellow",       1, "A", 2.00, 4,  "peach",   	"2021-11-05", 1);

-- inventory_allergies
INSERT INTO inventory_allergies VALUES 
("fake@test.com", 1, "soy");

-- carts
INSERT INTO carts VALUES 
("test@test.com", 2, "fred123", "2021-11-10", 2),
("fake@test.com", 1, "fred123", "2021-11-10", 3); 
    
-- orders
INSERT INTO orders VALUES 
("test@test.com", 1, "fred123", "2021-11-20", 3, 0.00, 1.00, 0, 1, 0),
("fake@test.com", 2, "fred123", "2021-11-20", 1, 0.00, 2.50, 0, 1, 0);

-- images
INSERT INTO images VALUES
("red", "apple", "red_apple"),
("granny smith", "apple", "green_apple"),
("large", "banana", "banana"),
("fresh green", "kale", "kale"), 
("hass", "avocado", "avocado"),
("yellow", "peach", "peach");