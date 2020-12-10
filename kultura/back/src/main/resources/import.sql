SET @Id1 = uuid();
SET @Id2 = uuid();
SET @Id3 = uuid();


INSERT INTO authority (authority) VALUES ('ROLE_ADMIN');
INSERT INTO authority (authority) VALUES ('ROLE_MODERATOR');
INSERT INTO authority (authority) VALUES ('ROLE');

INSERT INTO category (id, name) VALUES (1, 'Institucija');
INSERT INTO category (id, name) VALUES (2, 'Manifestacija');

INSERT INTO subcategory (name, category_id) VALUES ('Muzej', 1);
INSERT INTO subcategory (name, category_id) VALUES ('Galerija', 1);

INSERT INTO subcategory (name, category_id) VALUES ('Vasar', 2);
INSERT INTO subcategory (name, category_id) VALUES ('Sajam', 2);

INSERT INTO cultural_offering (address, brief_info, latitude, longitude, name, subcategory_id) VALUES ('Delimedje bb Tutin', 'Godisnji vasar u Delimedje', 10, 10, 'Vasar u Delimedje', 2);

INSERT INTO cultural_offering (address, brief_info, latitude, longitude, name, subcategory_id) VALUES ('Bulevar Vojvode Misica', 'Godisnji sajam knjiga', 10, 10, 'Sajam knjiga', 2);

# admin123 je sifra
INSERT INTO user (id, email, first_name, last_name, last_password_change, password, verified) VALUES (@Id1, 'admin@mail.com', 'Admin', 'Prezime', NOW(),  '$2a$10$yu3dC4LLPclLT9XAmvVtiuygJdD4kXMUnfBtu5k2.SxlrabVbR/vy', true);
INSERT INTO user (id, email, first_name, last_name, last_password_change, password, verified) VALUES (@Id2, 'moderator@mail.com', 'Moderator', 'Prezime', NOW(), '$2a$10$yu3dC4LLPclLT9XAmvVtiuygJdD4kXMUnfBtu5k2.SxlrabVbR/vy', true);
INSERT INTO user (id, email, first_name, last_name, last_password_change, password, verified) VALUES (@Id3, 'user@mail.com', 'User', 'Prezime', NOW(), '$2a$10$yu3dC4LLPclLT9XAmvVtiuygJdD4kXMUnfBtu5k2.SxlrabVbR/vy', true);

INSERT INTO user_authority (user_id, authority_id) VALUES (@Id1, 1);
INSERT INTO user_authority (user_id, authority_id) VALUES (@Id2, 2);
INSERT INTO user_authority (user_id, authority_id) VALUES (@Id3, 3);

INSERT INTO review (comment, rating, time_added, cultural_offering_id, user_id) VALUES ('Komentar', '3', NOW(), 2, @Id3);

INSERT INTO post (content, time_added, cultural_offering_id) VALUES ('vrlo zanimljiv post', NOW(), 1);

INSERT INTO post (content, time_added, cultural_offering_id) VALUES ('vrlo zanimljiv post broj dva', NOW(), 1);