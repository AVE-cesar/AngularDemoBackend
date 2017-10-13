---
-- Example Schema
--

DROP ALL OBJECTS;

CREATE SEQUENCE hibernate_sequence START WITH 1000;

-- many to one relation from Author to Book
CREATE TABLE AUTHOR (
    ID                  int not null IDENTITY,
    NAME                varchar(100),
    BIRTH_DATE          timestamp,
    primary key (ID)
);

CREATE TABLE BOOK (
    ID                          char(36) not null,
    TITLE                       varchar(100) not null,
    DESCRIPTION                 varchar(255) not null,
    PUBLICATION_DATE            timestamp,
    AUTHOR_ID                   int,
--    PRICE                       double not null,
    PRICE						int not null,
--	previousBookId				char(36),  ce champ génère un pb, il ajoute un tag @FixedLength qu'on ne sait pas où trouver
	BARCODEID					int,
	
	constraint book_fk_1 foreign key (AUTHOR_ID) references AUTHOR,
    primary key (ID)
);

INSERT INTO AUTHOR  VALUES (1, 'John Doe',null);
INSERT INTO AUTHOR  VALUES (2, 'Camus albert',null);
INSERT INTO AUTHOR  VALUES (3, 'Hugo victor',null);
INSERT INTO AUTHOR  VALUES (4, 'Proust marcel',null);
INSERT INTO AUTHOR  VALUES (5, 'Zola émile',null);
INSERT INTO AUTHOR  VALUES (6, 'Baudelaire charles',null);
INSERT INTO AUTHOR  VALUES (7, 'Sartre jean paul',null);
INSERT INTO AUTHOR  VALUES (8, 'Flaubert gustave',null);
INSERT INTO AUTHOR  VALUES (9, 'Rousseau jean-jacques',null);
INSERT INTO AUTHOR  VALUES (10, 'Racine jean',null);
INSERT INTO AUTHOR  VALUES (11, 'Daudet alphonse',null);
INSERT INTO AUTHOR  VALUES (12, 'Diderot denis',null);
INSERT INTO AUTHOR  VALUES (13, 'Aragon louis',null);
INSERT INTO AUTHOR  VALUES (14, 'jk Rowling',null);

INSERT INTO BOOK  VALUES ('serial_1', 'Les misérables', 'histoire de cozette', null, 3, 12, /*null,*/ null);
INSERT INTO BOOK  VALUES ('serial_2', 'A la recherche de temps perdu', '...', null, 4, 12, /*null,*/ null);
INSERT INTO BOOK  VALUES ('serial_3', 'Sodome et gomorrhe', '...', null, 4, 12, /*null,*/ null);
INSERT INTO BOOK  VALUES ('serial_4', 'Du côté de chez Swann', '...', null, 4, 12, /*null,*/ null);
INSERT INTO BOOK  VALUES ('serial_5', 'Germinal', '...', null, 5, 12, /*null,*/ null);
INSERT INTO BOOK  VALUES ('serial_6', 'Au bonheur des dames', '...', null, 5, 12, /*null,*/ null);
INSERT INTO BOOK  VALUES ('serial_7', 'Les fleurs du mal', '...', null, 6, 12, /*null,*/ null);
INSERT INTO BOOK  VALUES ('serial_8', 'Le spleen de Paris', '...', null, 6, 12, /*null,*/ null);
INSERT INTO BOOK  VALUES ('serial_9', 'La peste', '...', null, 2, 12, /*null,*/ null);
INSERT INTO BOOK  VALUES ('serial_10', 'L''étranger', '...', null, 2, 12, /*null,*/ null);
INSERT INTO BOOK  VALUES ('serial_11', 'La nausée', '...', null, 7, 12, /*null,*/ null);
INSERT INTO BOOK  VALUES ('serial_12', 'Madame Bovary', '...', null, 8, 12, /*null,*/ null);
INSERT INTO BOOK  VALUES ('serial_13', 'L''éducation sentimentale', '...', null, 8, 12, /*null,*/ null);
INSERT INTO BOOK  VALUES ('serial_14', 'Les confessions', '...', null, 9, 12, /*null,*/ null);
INSERT INTO BOOK  VALUES ('serial_15', 'Phèdre', '...', null, 10, 12, /*null,*/ null);
INSERT INTO BOOK  VALUES ('serial_16', 'Andromaque', '...', null, 10, 12, /*null,*/ null);
INSERT INTO BOOK  VALUES ('serial_17', 'Lettres de mon moulin', '...', null, 11, 12, /*null,*/ null);
INSERT INTO BOOK  VALUES ('serial_18', 'Tartarin de Tarascon', '...', null, 11, 12, /*null,*/ null);
INSERT INTO BOOK  VALUES ('serial_19', 'La religieuse', '...', null, 12, 12, /*null,*/ null);
INSERT INTO BOOK  VALUES ('serial_20', 'Le paysan de Paris', '...', null, 13, 12, /*null,*/ null);
INSERT INTO BOOK  VALUES ('serial_21', 'Harry Potter à l''école des sorciers', '...', null, 14, 25, /*null,*/ null);
INSERT INTO BOOK  VALUES ('serial_22', 'Harry Potter et la Chambre des secrets', '...', null, 14, 21, /*'serial_21',*/ null);

