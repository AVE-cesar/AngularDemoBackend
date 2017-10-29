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

-- one to one relation from BarCode to Book
CREATE TABLE BAR_CODE (
    ID					int not null identity,
    CODE					varchar(100) not null,
    -- should use a BLOB for this field to store a picture of the barcode
    image				varchar(100),

    primary key (ID)
);

CREATE TABLE BOOK (
	ID					char(36) not null,
	TITLE				varchar(40) not null,
	DESCRIPTION                 varchar(20) not null,
	PUBLICATION_DATE            timestamp,
	AUTHOR_ID                   int,
--    PRICE                       double not null,
	PRICE						int not null,
--	previousBookId				char(36),  ce champ génère un pb, il ajoute un tag @FixedLength qu'on ne sait pas où trouver
	BARCODEID					int,
	
	-- Many to one relation
	constraint book_fk_1 foreign key (AUTHOR_ID) references AUTHOR,
	-- One to one relation
	constraint book_fk_3 foreign key (BARCODEID) references BAR_CODE(ID),
	
    primary key (ID)
);
ALTER TABLE BOOK ADD CONSTRAINT BAR_CODE_UNIQUE UNIQUE(BARCODEID);

-- store where to buy books
CREATE TABLE STORE (
    ID				int not null identity,
    NAME				varchar(100) not null,
    ADDRESS			varchar(100) not null,
    ZIPCODE			varchar(100) not null,
    CITY				varchar(100) not null,

    primary key (ID)
);

-- relation (Many to Many) table between book and store
CREATE TABLE BOOK_STORE (
	ID				int not null identity,
	BOOK_ID			char(36) not null,
	STORE_ID 		int not null,
	
	constraint book_store_fk_1 foreign key (BOOK_ID) references BOOK,
	constraint book_store_fk_2 foreign key (STORE_ID) references STORE,
	
	primary key (ID)
);

-- add some test data
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

INSERT INTO BAR_CODE VALUES (1, '123456789', null);

INSERT INTO BOOK  VALUES ('serial_1', 'Les misérables', 'histoire de cozette', null, 3, 12, /*null,*/ 1);
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

INSERT INTO STORE  VALUES (1, 'Fnac', 'rue de Paris', '75000', 'PARIS');
INSERT INTO STORE  VALUES (2, 'Megastore', 'rue de Paris', '75015', 'PARIS');
INSERT INTO STORE  VALUES (3, 'Google', 'rue de Paris', '75016', 'PARIS');

INSERT INTO BOOK_STORE  VALUES (1, 'serial_1', 1);
INSERT INTO BOOK_STORE  VALUES (2, 'serial_2', 1);
INSERT INTO BOOK_STORE  VALUES (3, 'serial_3', 1);
