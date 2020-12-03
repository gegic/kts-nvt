INSERT INTO authority (authority) VALUES ('ROLE_ADMIN');
INSERT INTO authority (authority) VALUES ('ROLE_USER');

INSERT INTO category (id, name) VALUES (1, 'Institucija');
INSERT INTO category (id, name) VALUES (2, 'Manifestacija');

INSERT INTO subcategory (name, category_id) VALUES ('Muzej', 1);
INSERT INTO subcategory (name, category_id) VALUES ('Galerija', 1);

INSERT INTO subcategory (name, category_id) VALUES ('Vasar', 2);
INSERT INTO subcategory (name, category_id) VALUES ('Sajam', 2);

INSERT INTO cultural_offering (address, brief_info, latitude, longitude, name, subcategory_id) VALUES ('Delimedje bb Tutin', 'Godisnji vasar u Delimedje', 10, 10, 'Vasar u Delimedje', 2);

INSERT INTO cultural_offering (address, brief_info, latitude, longitude, name, subcategory_id) VALUES ('Bulevar Vojvode Misica', 'Godisnji sajam knjiga', 10, 10, 'Sajam knjiga', 2);