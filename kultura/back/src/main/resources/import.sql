INSERT INTO authority (authority) VALUES ('ROLE_ADMIN');
INSERT INTO authority (authority) VALUES ('ROLE_MODERATOR');
INSERT INTO authority (authority) VALUES ('ROLE_USER');

INSERT INTO category (id, name) VALUES (1, 'Institucija');
INSERT INTO category (id, name) VALUES (2, 'Manifestacija');

INSERT INTO subcategory (name, category_id) VALUES ('Muzej', 1);
INSERT INTO subcategory (name, category_id) VALUES ('Galerija', 1);

INSERT INTO subcategory (name, category_id) VALUES ('Vasar', 2);
INSERT INTO subcategory (name, category_id) VALUES ('Sajam', 2);

INSERT INTO cultural_offering (address, brief_info, latitude, longitude, name, subcategory_id) VALUES ('Delimedje bb Tutin', 'Godisnji vasar u Delimedje', 10, 10, 'Vasar u Delimedje', 2);

INSERT INTO cultural_offering (address, brief_info, latitude, longitude, name, subcategory_id) VALUES ('Bulevar Vojvode Misica', 'Godisnji sajam knjiga', 10, 10, 'Sajam knjiga', 2);

INSERT INTO user (email, first_name, last_name, last_password_change, password, username, verified) VALUES ('admin@mail.com', 'Admin', 'Prezime', NOW(), 'hesovanaSifra', 'admin', true);
INSERT INTO user (email, first_name, last_name, last_password_change, password, username, verified) VALUES ('moderator@mail.com', 'Moderator', 'Prezime', NOW(), 'hesovanaSifra', 'moderator', true);
INSERT INTO user (email, first_name, last_name, last_password_change, password, username, verified) VALUES ('user@mail.com', 'User', 'Prezime', NOW(), 'hesovanaSifra', 'user', true);

INSERT INTO user_authority (user_id, authority_id) VALUES (1, 1);
INSERT INTO user_authority (user_id, authority_id) VALUES (2, 2);
INSERT INTO user_authority (user_id, authority_id) VALUES (3, 3);

INSERT INTO review (comment, rating, time_added, cultural_offering_id, user_id) VALUES ('Komentar', '3', NOW(), 2, 3);

INSERT INTO post (content, time_added, cultural_offering_id) VALUES ("vrlo zanimljiv post", NOW(), 1);

INSERT INTO post (content, time_added, cultural_offering_id) VALUES ("vrlo zanimljiv post broj dva", NOW(), 1);