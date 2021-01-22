

INSERT INTO authority (authority) VALUES ('ROLE_ADMIN');
INSERT INTO authority (authority) VALUES ('ROLE_MODERATOR');
INSERT INTO authority (authority) VALUES ('ROLE_USER');

INSERT INTO category (id, name) VALUES (1, 'Institucija');
INSERT INTO category (id, name) VALUES (2, 'Manifestacija');
INSERT INTO category (id, name) VALUES (3, 'Kategorija3');
INSERT INTO category (id, name) VALUES (4, 'Kategorija4');
INSERT INTO category (id, name) VALUES (5, 'Kategorija5');
INSERT INTO category (id, name) VALUES (6, 'Kategorija6');
INSERT INTO category (id, name) VALUES (7, 'Kategorija7');
INSERT INTO category (id, name) VALUES (8, 'Kategorija8');
INSERT INTO category (id, name) VALUES (9, 'Kategorija9');
INSERT INTO category (id, name) VALUES (10, 'Kategorija10');
INSERT INTO category (id, name) VALUES (11, 'Kategorija11');
INSERT INTO category (id, name) VALUES (12, 'Kategorija12');

INSERT INTO subcategory (name, category_id) VALUES ('Muzej', 1);
INSERT INTO subcategory (name, category_id) VALUES ('Galerija', 1);
INSERT INTO subcategory (name, category_id) VALUES ('Potkategorija1', 1);
INSERT INTO subcategory (name, category_id) VALUES ('Potkategorija2', 1);
INSERT INTO subcategory (name, category_id) VALUES ('Potkategorija3', 1);
INSERT INTO subcategory (name, category_id) VALUES ('Potkategorija4', 1);
INSERT INTO subcategory (name, category_id) VALUES ('Potkategorija5', 1);
INSERT INTO subcategory (name, category_id) VALUES ('Potkategorija6', 1);
INSERT INTO subcategory (name, category_id) VALUES ('Potkategorija7', 1);
INSERT INTO subcategory (name, category_id) VALUES ('Potkategorija8', 1);
INSERT INTO subcategory (name, category_id) VALUES ('Potkategorija9', 1);
INSERT INTO subcategory (name, category_id) VALUES ('Potkategorija10', 1);
INSERT INTO subcategory (name, category_id) VALUES ('Potkategorija11', 1);
INSERT INTO subcategory (name, category_id) VALUES ('Potkategorija12', 1);

INSERT INTO subcategory (name, category_id) VALUES ('Vasar', 2);
INSERT INTO subcategory (name, category_id) VALUES ('Sajam', 2);

INSERT INTO cultural_offering_main_photo (height, time_added, width, token) VALUES (288, UTC_TIMESTAMP(), 512, '');

INSERT INTO cultural_offering (id, address, brief_info, latitude, longitude, name, subcategory_id, photo_id, num_reviews, overall_rating) VALUES (1, 'Delimedje bb Tutin', 'Godisnji vasar u Delimedje', 42.991436, 20.337982, 'Vasar u Delimedje', 2, 1, 1, 3);

UPDATE cultural_offering_main_photo SET cultural_offering_id = 1 WHERE id = 1;

INSERT INTO cultural_offering_main_photo (height, width, time_added, token) VALUES (429, 763, UTC_TIMESTAMP(), '');

INSERT INTO cultural_offering (id, address, brief_info, latitude, longitude, name, subcategory_id, num_reviews, overall_rating, photo_id) VALUES (2, 'Bulevar Vojvode Misica', 'Godisnji sajam knjiga', 42.988266, 20.333420, 'Sajam knjigaa', 2, 1, 4, 2);

UPDATE cultural_offering_main_photo SET cultural_offering_id = 2 WHERE id = 2;

INSERT INTO cultural_offering_main_photo (height, width, time_added, token) VALUES (429, 763, UTC_TIMESTAMP(), '');

INSERT INTO cultural_offering (address, brief_info, latitude, longitude, name, subcategory_id, num_reviews, overall_rating, photo_id) VALUES ('Bulevar Vojvode Misica', 'Godisnji sajam knjiga', 42.988266, 20.333420, 'Sajam knjigab', 1, 0, 0, 2);

UPDATE cultural_offering_main_photo SET cultural_offering_id = 3 WHERE id = 3;

INSERT INTO cultural_offering_main_photo (height, width, time_added, token) VALUES (429, 763, UTC_TIMESTAMP(), '');

INSERT INTO cultural_offering (address, brief_info, latitude, longitude, name, subcategory_id, num_reviews, overall_rating, photo_id) VALUES ('Bulevar Vojvode Misica', 'Godisnji sajam knjiga', 42.988266, 20.333420, 'Sajam knjigac', 1, 0, 0, 2);

UPDATE cultural_offering_main_photo SET cultural_offering_id = 4 WHERE id = 4;

INSERT INTO cultural_offering_main_photo (height, width, time_added, token) VALUES (429, 763, UTC_TIMESTAMP(), '');

INSERT INTO cultural_offering (address, brief_info, latitude, longitude, name, subcategory_id, num_reviews, overall_rating, photo_id) VALUES ('Bulevar Vojvode Misica', 'Godisnji sajam knjiga', 42.988266, 20.333420, 'Sajam knjigad', 1, 0, 0, 2);

UPDATE cultural_offering_main_photo SET cultural_offering_id = 5 WHERE id = 5;

INSERT INTO cultural_offering_main_photo (height, width, time_added, token) VALUES (429, 763, UTC_TIMESTAMP(), '');

INSERT INTO cultural_offering (address, brief_info, latitude, longitude, name, subcategory_id, num_reviews, overall_rating, photo_id) VALUES ('Bulevar Vojvode Misica', 'Godisnji sajam knjiga', 42.988266, 20.333420, 'Sajam knjigae', 1, 0, 0, 2);

UPDATE cultural_offering_main_photo SET cultural_offering_id = 6 WHERE id = 6;

INSERT INTO cultural_offering_main_photo (height, width, time_added, token) VALUES (429, 763, UTC_TIMESTAMP(), '');

INSERT INTO cultural_offering (address, brief_info, latitude, longitude, name, subcategory_id, num_reviews, overall_rating, photo_id) VALUES ('Bulevar Vojvode Misica', 'Godisnji sajam knjiga', 42.988266, 20.333420, 'Sajam knjigaf', 1, 0, 0, 2);

UPDATE cultural_offering_main_photo SET cultural_offering_id = 7 WHERE id = 7;

INSERT INTO cultural_offering_main_photo (height, width, time_added, token) VALUES (429, 763, UTC_TIMESTAMP(), '');

INSERT INTO cultural_offering (address, brief_info, latitude, longitude, name, subcategory_id, num_reviews, overall_rating, photo_id) VALUES ('Bulevar Vojvode Misica', 'Godisnji sajam knjiga', 42.988266, 20.333420, 'Sajam knjigag', 1, 0, 0, 2);

UPDATE cultural_offering_main_photo SET cultural_offering_id = 8 WHERE id = 8;

INSERT INTO cultural_offering_main_photo (height, width, time_added, token) VALUES (429, 763, UTC_TIMESTAMP(), '');

INSERT INTO cultural_offering (address, brief_info, latitude, longitude, name, subcategory_id, num_reviews, overall_rating, photo_id) VALUES ('Bulevar Vojvode Misica', 'Godisnji sajam knjiga', 42.988266, 20.333420, 'Sajam knjigah', 1, 0, 0, 2);

UPDATE cultural_offering_main_photo SET cultural_offering_id = 9 WHERE id = 9;

INSERT INTO cultural_offering_main_photo (height, width, time_added, token) VALUES (429, 763, UTC_TIMESTAMP(), '');

INSERT INTO cultural_offering (address, brief_info, latitude, longitude, name, subcategory_id, num_reviews, overall_rating, photo_id) VALUES ('Bulevar Vojvode Misica', 'Godisnji sajam knjiga', 42.988266, 20.333420, 'Sajam knjigai', 1, 0, 0, 2);

UPDATE cultural_offering_main_photo SET cultural_offering_id = 10 WHERE id = 10;

INSERT INTO cultural_offering_main_photo (height, width, time_added, token) VALUES (429, 763, UTC_TIMESTAMP(), '');

INSERT INTO cultural_offering (address, brief_info, latitude, longitude, name, subcategory_id, num_reviews, overall_rating, photo_id) VALUES ('Bulevar Vojvode Misica', 'Godisnji sajam knjiga', 42.988266, 20.333420, 'Sajam knjigaj', 1, 0, 0, 2);

UPDATE cultural_offering_main_photo SET cultural_offering_id = 11 WHERE id = 11;

INSERT INTO cultural_offering_main_photo (height, width, time_added, token) VALUES (429, 763, UTC_TIMESTAMP(), '');

INSERT INTO cultural_offering (address, brief_info, latitude, longitude, name, subcategory_id, num_reviews, overall_rating, photo_id) VALUES ('Bulevar Vojvode Misica', 'Godisnji sajam knjiga', 42.988266, 20.333420, 'Sajam knjigak', 1, 0, 0, 2);

UPDATE cultural_offering_main_photo SET cultural_offering_id = 12 WHERE id = 12;

INSERT INTO cultural_offering_main_photo (height, width, time_added, token) VALUES (429, 763, UTC_TIMESTAMP(), '');

INSERT INTO cultural_offering (address, brief_info, latitude, longitude, name, subcategory_id, num_reviews, overall_rating, photo_id) VALUES ('Bulevar Vojvode Misica', 'Godisnji sajam knjiga', 42.988266, 20.333420, 'Sajam knjigal', 1, 0, 0, 2);

UPDATE cultural_offering_main_photo SET cultural_offering_id = 13 WHERE id = 13;

INSERT INTO cultural_offering_main_photo (height, width, time_added, token) VALUES (429, 763, UTC_TIMESTAMP(), '');

INSERT INTO cultural_offering (address, brief_info, latitude, longitude, name, subcategory_id, num_reviews, overall_rating, photo_id) VALUES ('Bulevar Vojvode Misica', 'Godisnji sajam knjiga', 42.988266, 20.333420, 'Sajam knjigam', 1, 0, 0, 2);

UPDATE cultural_offering_main_photo SET cultural_offering_id = 14 WHERE id = 14;

INSERT INTO cultural_offering_main_photo (height, width, time_added, token) VALUES (429, 763, UTC_TIMESTAMP(), '');

INSERT INTO cultural_offering (address, brief_info, latitude, longitude, name, subcategory_id, num_reviews, overall_rating, photo_id) VALUES ('Bulevar Vojvode Misica', 'Godisnji sajam knjiga', 42.988266, 20.333420, 'Sajam knjigan', 1, 0, 0, 2);

UPDATE cultural_offering_main_photo SET cultural_offering_id = 15 WHERE id = 15;

INSERT INTO cultural_offering_main_photo (height, width, time_added, token) VALUES (429, 763, UTC_TIMESTAMP(), '');

INSERT INTO cultural_offering (address, brief_info, latitude, longitude, name, subcategory_id, num_reviews, overall_rating, photo_id) VALUES ('Bulevar Vojvode Misica', 'Godisnji sajam knjiga', 42.988266, 20.333420, 'Sajam knjigao', 1, 0, 0, 2);

UPDATE cultural_offering_main_photo SET cultural_offering_id = 16 WHERE id = 16;

INSERT INTO cultural_offering_main_photo (height, width, time_added, token) VALUES (429, 763, UTC_TIMESTAMP(), '');

INSERT INTO cultural_offering (address, brief_info, latitude, longitude, name, subcategory_id, num_reviews, overall_rating, photo_id) VALUES ('Bulevar Vojvode Misica', 'Godisnji sajam knjiga', 42.988266, 20.333420, 'Sajam knjigap', 1, 0, 0, 2);

UPDATE cultural_offering_main_photo SET cultural_offering_id = 17 WHERE id = 17;

INSERT INTO cultural_offering_main_photo (height, width, time_added, token) VALUES (429, 763, UTC_TIMESTAMP(), '');

INSERT INTO cultural_offering (address, brief_info, latitude, longitude, name, subcategory_id, num_reviews, overall_rating, photo_id) VALUES ('Bulevar Vojvode Misica', 'Godisnji sajam knjiga', 42.988266, 20.333420, 'Sajam knjigaq', 1, 0, 0, 2);

UPDATE cultural_offering_main_photo SET cultural_offering_id = 18 WHERE id = 18;

INSERT INTO cultural_offering_main_photo (height, width, time_added, token) VALUES (429, 763, UTC_TIMESTAMP(), '');

INSERT INTO cultural_offering (address, brief_info, latitude, longitude, name, subcategory_id, num_reviews, overall_rating, photo_id) VALUES ('Bulevar Vojvode Misica', 'Godisnji sajam knjiga', 42.988266, 20.333420, 'Sajam knjigar', 1, 0, 0, 2);

UPDATE cultural_offering_main_photo SET cultural_offering_id = 19 WHERE id = 19;

INSERT INTO cultural_offering_main_photo (height, width, time_added, token) VALUES (429, 763, UTC_TIMESTAMP(), '');

INSERT INTO cultural_offering (address, brief_info, latitude, longitude, name, subcategory_id, num_reviews, overall_rating, photo_id) VALUES ('Bulevar Vojvode Misica', 'Godisnji sajam knjiga', 42.988266, 20.333420, 'Sajam knjigas', 1, 0, 0, 2);

UPDATE cultural_offering_main_photo SET cultural_offering_id = 20 WHERE id = 20;

INSERT INTO cultural_offering_main_photo (height, width, time_added, token) VALUES (429, 763, UTC_TIMESTAMP(), '');

INSERT INTO cultural_offering (address, brief_info, latitude, longitude, name, subcategory_id, num_reviews, overall_rating, photo_id) VALUES ('Bulevar Vojvode Misica', 'Godisnji sajam knjiga', 42.988266, 20.333420, 'Sajam knjigat', 1, 0, 0, 2);

UPDATE cultural_offering_main_photo SET cultural_offering_id = 21 WHERE id = 21;

INSERT INTO kts.cultural_offering_photo (height, time_added, width, cultural_offering_id) VALUES (1000, UTC_TIMESTAMP(), 667, 1);
INSERT INTO kts.cultural_offering_photo (height, time_added, width, cultural_offering_id) VALUES (1000, UTC_TIMESTAMP(), 667, 1);

# admin123 je sifra
INSERT INTO user (email, first_name, last_name, last_password_change, password, verified) VALUES ('admin@mail.com', 'Admin', 'Prezime', UTC_TIMESTAMP(),  '$2a$10$yu3dC4LLPclLT9XAmvVtiuygJdD4kXMUnfBtu5k2.SxlrabVbR/vy', true);
INSERT INTO user (email, first_name, last_name, last_password_change, password, verified) VALUES ('moderator@mail.com', 'Moderator', 'Prezime', UTC_TIMESTAMP(), '$2a$10$yu3dC4LLPclLT9XAmvVtiuygJdD4kXMUnfBtu5k2.SxlrabVbR/vy', true);
INSERT INTO user (email, first_name, last_name, last_password_change, password, verified) VALUES ('user@mail.com', 'User', 'Prezime', UTC_TIMESTAMP(), '$2a$10$yu3dC4LLPclLT9XAmvVtiuygJdD4kXMUnfBtu5k2.SxlrabVbR/vy', true);
INSERT INTO user (email, first_name, last_name, last_password_change, password, verified) VALUES ('moderator1@mail.com', 'Moderator', 'Prezime', UTC_TIMESTAMP(), '$2a$10$yu3dC4LLPclLT9XAmvVtiuygJdD4kXMUnfBtu5k2.SxlrabVbR/vy', true);
INSERT INTO user (email, first_name, last_name, last_password_change, password, verified) VALUES ('moderator2@mail.com', 'Moderator', 'Prezime', UTC_TIMESTAMP(), '$2a$10$yu3dC4LLPclLT9XAmvVtiuygJdD4kXMUnfBtu5k2.SxlrabVbR/vy', true);
INSERT INTO user (email, first_name, last_name, last_password_change, password, verified) VALUES ('moderator3@mail.com', 'Moderator', 'Prezime', UTC_TIMESTAMP(), '$2a$10$yu3dC4LLPclLT9XAmvVtiuygJdD4kXMUnfBtu5k2.SxlrabVbR/vy', true);
INSERT INTO user (email, first_name, last_name, last_password_change, password, verified) VALUES ('moderator4@mail.com', 'Moderator', 'Prezime', UTC_TIMESTAMP(), '$2a$10$yu3dC4LLPclLT9XAmvVtiuygJdD4kXMUnfBtu5k2.SxlrabVbR/vy', true);
INSERT INTO user (email, first_name, last_name, last_password_change, password, verified) VALUES ('moderator5@mail.com', 'Moderator', 'Prezime', UTC_TIMESTAMP(), '$2a$10$yu3dC4LLPclLT9XAmvVtiuygJdD4kXMUnfBtu5k2.SxlrabVbR/vy', true);
INSERT INTO user (email, first_name, last_name, last_password_change, password, verified) VALUES ('moderator6@mail.com', 'Moderator', 'Prezime', UTC_TIMESTAMP(), '$2a$10$yu3dC4LLPclLT9XAmvVtiuygJdD4kXMUnfBtu5k2.SxlrabVbR/vy', true);
INSERT INTO user (email, first_name, last_name, last_password_change, password, verified) VALUES ('moderator7@mail.com', 'Moderator', 'Prezime', UTC_TIMESTAMP(), '$2a$10$yu3dC4LLPclLT9XAmvVtiuygJdD4kXMUnfBtu5k2.SxlrabVbR/vy', true);
INSERT INTO user (email, first_name, last_name, last_password_change, password, verified) VALUES ('moderator8@mail.com', 'Moderator', 'Prezime', UTC_TIMESTAMP(), '$2a$10$yu3dC4LLPclLT9XAmvVtiuygJdD4kXMUnfBtu5k2.SxlrabVbR/vy', true);
INSERT INTO user (email, first_name, last_name, last_password_change, password, verified) VALUES ('moderator9@mail.com', 'Moderator', 'Prezime', UTC_TIMESTAMP(), '$2a$10$yu3dC4LLPclLT9XAmvVtiuygJdD4kXMUnfBtu5k2.SxlrabVbR/vy', true);
INSERT INTO user (email, first_name, last_name, last_password_change, password, verified) VALUES ('moderator10@mail.com', 'Moderator', 'Prezime', UTC_TIMESTAMP(), '$2a$10$yu3dC4LLPclLT9XAmvVtiuygJdD4kXMUnfBtu5k2.SxlrabVbR/vy', true);
INSERT INTO user (email, first_name, last_name, last_password_change, password, verified) VALUES ('moderator11@mail.com', 'Moderator', 'Prezime', UTC_TIMESTAMP(), '$2a$10$yu3dC4LLPclLT9XAmvVtiuygJdD4kXMUnfBtu5k2.SxlrabVbR/vy', true);
INSERT INTO user (email, first_name, last_name, last_password_change, password, verified) VALUES ('moderator12@mail.com', 'Moderator', 'Prezime', UTC_TIMESTAMP(), '$2a$10$yu3dC4LLPclLT9XAmvVtiuygJdD4kXMUnfBtu5k2.SxlrabVbR/vy', true);
INSERT INTO user (email, first_name, last_name, last_password_change, password, verified) VALUES ('moderator13@mail.com', 'Moderator', 'Prezime', UTC_TIMESTAMP(), '$2a$10$yu3dC4LLPclLT9XAmvVtiuygJdD4kXMUnfBtu5k2.SxlrabVbR/vy', true);
INSERT INTO user (email, first_name, last_name, last_password_change, password, verified) VALUES ('moderator14@mail.com', 'Moderator', 'Prezime', UTC_TIMESTAMP(), '$2a$10$yu3dC4LLPclLT9XAmvVtiuygJdD4kXMUnfBtu5k2.SxlrabVbR/vy', true);
INSERT INTO user (email, first_name, last_name, last_password_change, password, verified) VALUES ('user2@mail.com', 'User', 'Prezime', UTC_TIMESTAMP(), '$2a$10$yu3dC4LLPclLT9XAmvVtiuygJdD4kXMUnfBtu5k2.SxlrabVbR/vy', false);
INSERT INTO user (email, first_name, last_name, last_password_change, password, verified) VALUES ('john@mail.com', 'John', 'Smith', UTC_TIMESTAMP(), '$2a$10$yu3dC4LLPclLT9XAmvVtiuygJdD4kXMUnfBtu5k2.SxlrabVbR/vy', true);

INSERT INTO user_authority (user_id, authority_id) VALUES (1, 1);
INSERT INTO user_authority (user_id, authority_id) VALUES (2, 2);
INSERT INTO user_authority (user_id, authority_id) VALUES (3, 3);
INSERT INTO user_authority (user_id, authority_id) VALUES (4, 2);
INSERT INTO user_authority (user_id, authority_id) VALUES (5, 2);
INSERT INTO user_authority (user_id, authority_id) VALUES (6, 2);
INSERT INTO user_authority (user_id, authority_id) VALUES (7, 2);
INSERT INTO user_authority (user_id, authority_id) VALUES (8, 2);
INSERT INTO user_authority (user_id, authority_id) VALUES (9, 2);
INSERT INTO user_authority (user_id, authority_id) VALUES (10, 2);
INSERT INTO user_authority (user_id, authority_id) VALUES (11, 2);
INSERT INTO user_authority (user_id, authority_id) VALUES (12, 2);
INSERT INTO user_authority (user_id, authority_id) VALUES (13, 2);
INSERT INTO user_authority (user_id, authority_id) VALUES (14, 2);
INSERT INTO user_authority (user_id, authority_id) VALUES (15, 2);
INSERT INTO user_authority (user_id, authority_id) VALUES (16, 2);
INSERT INTO user_authority (user_id, authority_id) VALUES (17, 2);
INSERT INTO user_authority (user_id, authority_id) VALUES (18, 3);
INSERT INTO user_authority (user_id, authority_id) VALUES (19, 3);

INSERT INTO review (comment, rating, time_added, cultural_offering_id, user_id) VALUES ('Komentar', 3, UTC_TIMESTAMP(), 1, 3);

INSERT INTO review (comment, rating, time_added, cultural_offering_id, user_id) VALUES ('Komentar2', 4, UTC_TIMESTAMP(), 2, 3);


INSERT INTO post (content, time_added, cultural_offering_id) VALUES ('vrlo zanimljiv post', UTC_TIMESTAMP(), 1);
INSERT INTO post (content, time_added, cultural_offering_id) VALUES ('Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean sed lobortis neque. Mauris et mauris vehicula, eleifend risus eu, ornare metus. Phasellus porta orci quis lectus sodales, quis vulputate mi eleifend. Donec vitae tortor neque. Donec justo justo, bibendum sed interdum nec, molestie faucibus augue. Donec in libero sed nulla commodo porttitor. Sed ut iaculis lorem, eu vehicula libero. Pellentesque eget placerat mi, non ornare odio.', UTC_TIMESTAMP(), 1);

INSERT INTO post (content, time_added, cultural_offering_id) VALUES ('vrlo zanimljiv post broj dva', UTC_TIMESTAMP(), 1);

INSERT INTO post (content, time_added, cultural_offering_id) VALUES ('Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean sed lobortis neque. Mauris et mauris vehicula, eleifend risus eu, ornare metus. Phasellus porta orci quis lectus sodales, quis vulputate mi eleifend. Donec vitae tortor neque. Donec justo justo, bibendum sed interdum nec, molestie faucibus augue. Donec in libero sed nulla commodo porttitor. Sed ut iaculis lorem, eu vehicula libero. Pellentesque eget placerat mi, non ornare odio.', UTC_TIMESTAMP(), 1);
INSERT INTO post (content, time_added, cultural_offering_id) VALUES ('Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean sed lobortis neque. Mauris et mauris vehicula, eleifend risus eu, ornare metus. Phasellus porta orci quis lectus sodales, quis vulputate mi eleifend. Donec vitae tortor neque. Donec justo justo, bibendum sed interdum nec, molestie faucibus augue. Donec in libero sed nulla commodo porttitor. Sed ut iaculis lorem, eu vehicula libero. Pellentesque eget placerat mi, non ornare odio.', UTC_TIMESTAMP(), 1);
INSERT INTO post (content, time_added, cultural_offering_id) VALUES ('Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean sed lobortis neque. Mauris et mauris vehicula, eleifend risus eu, ornare metus. Phasellus porta orci quis lectus sodales, quis vulputate mi eleifend. Donec vitae tortor neque. Donec justo justo, bibendum sed interdum nec, molestie faucibus augue. Donec in libero sed nulla commodo porttitor. Sed ut iaculis lorem, eu vehicula libero. Pellentesque eget placerat mi, non ornare odio.', UTC_TIMESTAMP(), 1);
INSERT INTO post (content, time_added, cultural_offering_id) VALUES ('Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean sed lobortis neque. Mauris et mauris vehicula, eleifend risus eu, ornare metus. Phasellus porta orci quis lectus sodales, quis vulputate mi eleifend. Donec vitae tortor neque. Donec justo justo, bibendum sed interdum nec, molestie faucibus augue. Donec in libero sed nulla commodo porttitor. Sed ut iaculis lorem, eu vehicula libero. Pellentesque eget placerat mi, non ornare odio.', UTC_TIMESTAMP(), 1);
INSERT INTO post (content, time_added, cultural_offering_id) VALUES ('Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean sed lobortis neque. Mauris et mauris vehicula, eleifend risus eu, ornare metus. Phasellus porta orci quis lectus sodales, quis vulputate mi eleifend. Donec vitae tortor neque. Donec justo justo, bibendum sed interdum nec, molestie faucibus augue. Donec in libero sed nulla commodo porttitor. Sed ut iaculis lorem, eu vehicula libero. Pellentesque eget placerat mi, non ornare odio.', UTC_TIMESTAMP(), 1);
INSERT INTO post (content, time_added, cultural_offering_id) VALUES ('Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean sed lobortis neque. Mauris et mauris vehicula, eleifend risus eu, ornare metus. Phasellus porta orci quis lectus sodales, quis vulputate mi eleifend. Donec vitae tortor neque. Donec justo justo, bibendum sed interdum nec, molestie faucibus augue. Donec in libero sed nulla commodo porttitor. Sed ut iaculis lorem, eu vehicula libero. Pellentesque eget placerat mi, non ornare odio.', UTC_TIMESTAMP(), 1);
INSERT INTO post (content, time_added, cultural_offering_id) VALUES ('Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean sed lobortis neque. Mauris et mauris vehicula, eleifend risus eu, ornare metus. Phasellus porta orci quis lectus sodales, quis vulputate mi eleifend. Donec vitae tortor neque. Donec justo justo, bibendum sed interdum nec, molestie faucibus augue. Donec in libero sed nulla commodo porttitor. Sed ut iaculis lorem, eu vehicula libero. Pellentesque eget placerat mi, non ornare odio.', UTC_TIMESTAMP(), 1);
INSERT INTO post (content, time_added, cultural_offering_id) VALUES ('Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean sed lobortis neque. Mauris et mauris vehicula, eleifend risus eu, ornare metus. Phasellus porta orci quis lectus sodales, quis vulputate mi eleifend. Donec vitae tortor neque. Donec justo justo, bibendum sed interdum nec, molestie faucibus augue. Donec in libero sed nulla commodo porttitor. Sed ut iaculis lorem, eu vehicula libero. Pellentesque eget placerat mi, non ornare odio.', UTC_TIMESTAMP(), 1);
INSERT INTO post (content, time_added, cultural_offering_id) VALUES ('Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean sed lobortis neque. Mauris et mauris vehicula, eleifend risus eu, ornare metus. Phasellus porta orci quis lectus sodales, quis vulputate mi eleifend. Donec vitae tortor neque. Donec justo justo, bibendum sed interdum nec, molestie faucibus augue. Donec in libero sed nulla commodo porttitor. Sed ut iaculis lorem, eu vehicula libero. Pellentesque eget placerat mi, non ornare odio.', UTC_TIMESTAMP(), 1);
INSERT INTO post (content, time_added, cultural_offering_id) VALUES ('Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean sed lobortis neque. Mauris et mauris vehicula, eleifend risus eu, ornare metus. Phasellus porta orci quis lectus sodales, quis vulputate mi eleifend. Donec vitae tortor neque. Donec justo justo, bibendum sed interdum nec, molestie faucibus augue. Donec in libero sed nulla commodo porttitor. Sed ut iaculis lorem, eu vehicula libero. Pellentesque eget placerat mi, non ornare odio.', UTC_TIMESTAMP(), 1);
INSERT INTO post (content, time_added, cultural_offering_id) VALUES ('Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean sed lobortis neque. Mauris et mauris vehicula, eleifend risus eu, ornare metus. Phasellus porta orci quis lectus sodales, quis vulputate mi eleifend. Donec vitae tortor neque. Donec justo justo, bibendum sed interdum nec, molestie faucibus augue. Donec in libero sed nulla commodo porttitor. Sed ut iaculis lorem, eu vehicula libero. Pellentesque eget placerat mi, non ornare odio.', UTC_TIMESTAMP(), 1);
INSERT INTO post (content, time_added, cultural_offering_id) VALUES ('Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean sed lobortis neque. Mauris et mauris vehicula, eleifend risus eu, ornare metus. Phasellus porta orci quis lectus sodales, quis vulputate mi eleifend. Donec vitae tortor neque. Donec justo justo, bibendum sed interdum nec, molestie faucibus augue. Donec in libero sed nulla commodo porttitor. Sed ut iaculis lorem, eu vehicula libero. Pellentesque eget placerat mi, non ornare odio.', UTC_TIMESTAMP(), 1);

INSERT INTO post (content, time_added, cultural_offering_id) VALUES ('Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean sed lobortis neque. Mauris et mauris vehicula, eleifend risus eu, ornare metus. Phasellus porta orci quis lectus sodales, quis vulputate mi eleifend. Donec vitae tortor neque. Donec justo justo, bibendum sed interdum nec, molestie faucibus augue. Donec in libero sed nulla commodo porttitor. Sed ut iaculis lorem, eu vehicula libero. Pellentesque eget placerat mi, non ornare odio.', UTC_TIMESTAMP(), 1);

