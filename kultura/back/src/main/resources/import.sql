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

INSERT INTO user (id, email, first_name, last_name, last_password_change, password, username, verified) VALUES ('3f332f0b-dbcc-4295-bbd8-a1ff6e908a6a', 'admin@mail.com', 'Admin', 'Prezime', NOW(), 'hesovanaSifra', 'admin', true);
INSERT INTO user (id, email, first_name, last_name, last_password_change, password, username, verified) VALUES ('633cc4f1-896a-4c6c-b290-2ef9b2f908f1', 'moderator@mail.com', 'Moderator', 'Prezime', NOW(), 'hesovanaSifra', 'moderator', true);
INSERT INTO user (id, email, first_name, last_name, last_password_change, password, username, verified) VALUES ('bad4c0a6-78d4-4441-a478-7022e39e4d3c', 'user@mail.com', 'User', 'Prezime', NOW(), 'hesovanaSifra', 'user', true);

INSERT INTO user_authority (user_id, authority_id) VALUES ('3f332f0b-dbcc-4295-bbd8-a1ff6e908a6a', 1);
INSERT INTO user_authority (user_id, authority_id) VALUES ('633cc4f1-896a-4c6c-b290-2ef9b2f908f1', 2);
INSERT INTO user_authority (user_id, authority_id) VALUES ('bad4c0a6-78d4-4441-a478-7022e39e4d3c', 3);

INSERT INTO review (comment, rating, time_added, cultural_offering_id, user_id) VALUES ('Komentar', '3', NOW(), 2, 'bad4c0a6-78d4-4441-a478-7022e39e4d3c');

INSERT INTO post (content, time_added, cultural_offering_id) VALUES ("vrlo zanimljiv post", NOW(), 1);

INSERT INTO post (content, time_added, cultural_offering_id) VALUES ("vrlo zanimljiv post broj dva", NOW(), 1);