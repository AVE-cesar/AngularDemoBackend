$output.resource("import.sql")##

-- add admin user
INSERT INTO "app_user" ("id", "first_name", "last_name", "email", "language", "login", "password", "enabled", "locked") VALUES (1, 'admin', 'admin', 'admin@admin.com', 'FR', 'admin', 'admin', 1, 0);

-- add admin role (lower case)
INSERT INTO "app_authority" ("id", "name") VALUES (0, 'admin');
INSERT INTO "app_authority" ("id", "name") VALUES (1, 'user');

-- link admin role to admin user
INSERT INTO "app_user_authority" ("app_user_id", "app_authority_id" ) VALUES (1, 0);

-- add a book screen config
INSERT INTO "app_parameter" ("domain", "key", "value") VALUES ('SCREEN_CONFIG', 'Book', '{ "id": true, "title": true, "description": true, "publicationDate": true, "authorId": true, "price": true, "barcodeid": true}');
INSERT INTO "app_parameter" ("domain", "key", "value") VALUES ('SCREEN_CONFIG', 'AppAuthority', '{ "id": true, "name": true}');
INSERT INTO "app_parameter" ("domain", "key", "value") VALUES ('SCREEN_CONFIG', 'AppParameter', '{ "id": true, "domain": true, "key": true, "value": true}');
INSERT INTO "app_parameter" ("domain", "key", "value") VALUES ('SCREEN_CONFIG', 'AppToken', '{ "id": true, "tokenValue": true, "tokenCreationDate": true, "ipAddress": true, "userAgent": true, "userLogin": true}');

------------------------------------------
--
-- we have to explicitely use the column name before the keyword VALUES because we can not know the column order.
--
-----------------------------------------
-- add authors
INSERT INTO "author" ("id", "name", "birth_date") VALUES (1, 'De Maupassant guy', CURRENT_DATE());
INSERT INTO "author" ("id", "name", "birth_date") VALUES (2, 'Camus albert', CURRENT_DATE());
INSERT INTO "author" ("id", "name", "birth_date") VALUES (3, 'Hugo victor', CURRENT_DATE());
INSERT INTO "author" ("id", "name", "birth_date") VALUES (4, 'Proust marcel', CURRENT_DATE());
INSERT INTO "author" ("id", "name", "birth_date") VALUES (5, 'Zola émile', CURRENT_DATE());
INSERT INTO "author" ("id", "name", "birth_date") VALUES (6, 'Baudelaire charles', CURRENT_DATE());
INSERT INTO "author" ("id", "name", "birth_date") VALUES (7, 'Sartre jean paul', CURRENT_DATE());
INSERT INTO "author" ("id", "name", "birth_date") VALUES (8, 'Flaubert gustave', CURRENT_DATE());
INSERT INTO "author" ("id", "name", "birth_date") VALUES (9, 'Rousseau jean-jacques', CURRENT_DATE());
INSERT INTO "author" ("id", "name", "birth_date") VALUES (10, 'Racine jean', CURRENT_DATE());
INSERT INTO "author" ("id", "name", "birth_date") VALUES (11, 'Daudet alphonse', CURRENT_DATE());
INSERT INTO "author" ("id", "name", "birth_date") VALUES (12, 'Diderot denis', CURRENT_DATE());
INSERT INTO "author" ("id", "name", "birth_date") VALUES (13, 'Aragon louis', CURRENT_DATE());
INSERT INTO "author" ("id", "name", "birth_date") VALUES (14, 'jk Rowling', CURRENT_DATE());
INSERT INTO "author" ("id", "name", "birth_date") VALUES (15, 'Verlaine paul', CURRENT_DATE());
INSERT INTO "author" ("id", "name", "birth_date") VALUES (16, 'Rimbaud arthur', CURRENT_DATE());
INSERT INTO "author" ("id", "name", "birth_date") VALUES (17, 'Appolinaire', CURRENT_DATE());
INSERT INTO "author" ("id", "name", "birth_date") VALUES (18, 'King Stephen', CURRENT_DATE());
INSERT INTO "author" ("id", "name", "birth_date") VALUES (19, 'Ionesco', CURRENT_DATE());
INSERT INTO "author" ("id", "name", "birth_date") VALUES (20, 'de La Fontaine jean', CURRENT_DATE());
INSERT INTO "author" ("id", "name", "birth_date") VALUES (21, 'Eluard paul',  CURRENT_DATE());

-- add a barcode
INSERT INTO "bar_code" ("id", "code", "image") VALUES (1, '123456789', null);
INSERT INTO "bar_code" ("id", "code", "image") VALUES (2, '#####&é12', null);
INSERT INTO "bar_code" ("id", "code", "image") VALUES (3, '__________', null);

-- add a book
INSERT INTO "book" ("id", "title", "description", "publication_date", "author_id", "price", "barcodeid") VALUES ('serial_1', 'Les misérables', 'histoire de cozette', CURRENT_DATE(), 3, 12, /*null,*/ 1);
INSERT INTO "book" ("id", "title", "description", "publication_date", "author_id", "price", "barcodeid") VALUES ('serial_2', 'Les misérables II', 'histoire de cozette', CURRENT_DATE(), 3, 12, /*null,*/ 2);
INSERT INTO "book" ("id", "title", "description", "publication_date", "author_id", "price", "barcodeid") VALUES ('serial_3', 'Les misérables III', 'histoire de cozette', CURRENT_DATE(), 3, 12, /*null,*/ 3);

-- add book stores
INSERT INTO "store" ("id", "name", "address", "zipcode", "city") VALUES (1, 'Fnac', 'rue de Paris', '75000', 'PARIS');
INSERT INTO "store" ("id", "name", "address", "zipcode", "city") VALUES (2, 'Megastore', 'rue de Paris', '75015', 'PARIS');
INSERT INTO "store" ("id", "name", "address", "zipcode", "city") VALUES (3, 'Google', 'rue de Paris', '75016', 'PARIS');

-- add links between book and store
INSERT INTO "book_store" (/*"id", */"book_id", "store_id") VALUES (/*1,*/ 'serial_1', 1);
INSERT INTO "book_store" (/*"id", */"book_id", "store_id") VALUES (/*2, */'serial_2', 1);
INSERT INTO "book_store" (/*"id", */"book_id", "store_id") VALUES (/*3, */'serial_3', 1);
