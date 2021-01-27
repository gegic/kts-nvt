INSERT INTO authority (authority) VALUES ('ROLE_ADMIN');
INSERT INTO authority (authority) VALUES ('ROLE_MODERATOR');
INSERT INTO authority (authority) VALUES ('ROLE_USER');

INSERT INTO category (id, name) VALUES (1, 'Institucija');
INSERT INTO category (id, name) VALUES (2, 'Manifestacija');

INSERT INTO subcategory (name, category_id) VALUES ('Muzej', 1);
INSERT INTO subcategory (name, category_id) VALUES ('Galerija', 1);

INSERT INTO subcategory (name, category_id) VALUES ('Vasar', 2);
INSERT INTO subcategory (name, category_id) VALUES ('Sajam', 2);

INSERT INTO cultural_offering_main_photo (height, time_added, width, token) VALUES (288, UTC_TIMESTAMP(), 512, '');

INSERT INTO cultural_offering (id, address, brief_info, latitude, longitude, name, subcategory_id, photo_id) VALUES (1, 'Delimedje bb Tutin', 'Godisnji vasar u Delimedje', 42.991436, 20.337982, 'Vasar u Delimedje', 2, 1);

UPDATE cultural_offering_main_photo SET cultural_offering_id = 1 WHERE id = 1;

INSERT INTO cultural_offering_main_photo (height, width, time_added, token) VALUES (429, 763, UTC_TIMESTAMP(), '');

INSERT INTO cultural_offering (id, address, brief_info, latitude, longitude, name, subcategory_id, num_reviews, overall_rating, photo_id) VALUES (2, 'Bulevar Vojvode Misica', 'Godisnji sajam knjiga', 42.988266, 20.333420, 'Sajam knjiga', 2, 1, 3, 2);

UPDATE cultural_offering_main_photo SET cultural_offering_id = 2 WHERE id = 2;


INSERT INTO cultural_offering_main_photo (height, width, time_added, token) VALUES (429, 763, UTC_TIMESTAMP(), '');

INSERT INTO cultural_offering (id, address, brief_info, latitude, longitude, name, subcategory_id, photo_id) VALUES (3, 'Bačka tvrđava 41', 'Dani evropske baštine', 45.393086697536496, 19.221788262046566, 'Dani evropske baštine', 2, 3);

UPDATE cultural_offering_main_photo SET cultural_offering_id = 3 WHERE id = 3;


INSERT INTO cultural_offering_photo (height, time_added, width, cultural_offering_id) VALUES (720, UTC_TIMESTAMP(), 480, 1);

INSERT INTO cultural_offering_main_photo (height, time_added, width, token) VALUES (288, UTC_TIMESTAMP(), 512, '');

INSERT INTO cultural_offering_main_photo (height, time_added, width, token) VALUES (288, UTC_TIMESTAMP(), 512, '');

# admin123 je sifra
INSERT INTO user (email, first_name, last_name, last_password_change, password, verified) VALUES ('admin@mail.com', 'Admin', 'Prezime', UTC_TIMESTAMP(),  '$2a$10$yu3dC4LLPclLT9XAmvVtiuygJdD4kXMUnfBtu5k2.SxlrabVbR/vy', true);
INSERT INTO user (email, first_name, last_name, last_password_change, password, verified) VALUES ('moderator@mail.com', 'Moderator', 'Prezime', UTC_TIMESTAMP(), '$2a$10$yu3dC4LLPclLT9XAmvVtiuygJdD4kXMUnfBtu5k2.SxlrabVbR/vy', true);
INSERT INTO user (email, first_name, last_name, last_password_change, password, verified) VALUES ('user@mail.com', 'User', 'Prezime', UTC_TIMESTAMP(), '$2a$10$yu3dC4LLPclLT9XAmvVtiuygJdD4kXMUnfBtu5k2.SxlrabVbR/vy', true);
INSERT INTO user (email, first_name, last_name, last_password_change, password, verified) VALUES ('unverified@mail.com', 'User', 'Prezime', UTC_TIMESTAMP(), '$2a$10$yu3dC4LLPclLT9XAmvVtiuygJdD4kXMUnfBtu5k2.SxlrabVbR/vy', false);
INSERT INTO user (email, first_name, last_name, last_password_change, password, verified) VALUES ('unverified2@mail.com', 'User', 'Prezime', UTC_TIMESTAMP(), '$2a$10$yu3dC4LLPclLT9XAmvVtiuygJdD4kXMUnfBtu5k2.SxlrabVbR/vy', false);

INSERT INTO user_authority (user_id, authority_id) VALUES (1, 1);
INSERT INTO user_authority (user_id, authority_id) VALUES (2, 2);
INSERT INTO user_authority (user_id, authority_id) VALUES (3, 3);

INSERT INTO review (comment, rating, time_added, cultural_offering_id, user_id) VALUES ('Komentar', 3, UTC_TIMESTAMP(), 2, 3);
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
