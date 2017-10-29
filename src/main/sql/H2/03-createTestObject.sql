-- just to test all database types
CREATE TABLE TYPE_TEST (
    ID					int not null IDENTITY,
    NAME					varchar(100),
    BIRTH_DATE			timestamp,
--    PRICE				double,
    RATE					DECIMAL(20, 2),
    FIRSTNAME			char(5),
    ENABLE				boolean,
    START				time,
    bimage				blob,
    image				clob,
    
    primary key (ID)
);
