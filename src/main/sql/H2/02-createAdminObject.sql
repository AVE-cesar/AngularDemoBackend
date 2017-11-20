--
-- Users
-- 
CREATE TABLE APP_USER (
	ID					int not null  IDENTITY,
	FIRST_NAME			varchar(250) not null,
	LAST_NAME			varchar(250) not null,
	EMAIL				varchar(250) not null,
	LANGUAGE				varchar(10) not null,
	LOGIN				varchar(250) not null,
	PASSWORD				varchar(12) not null,
	ENABLED				int not null,
	LAST_LOGIN_DATE		timestamp,
	NB_ATTEMPTS			int,
	LOCKED				int not null,
	--REGISTRATION_DATE		timestamp not null,
	
	primary key (ID)
);
-- add unicity contraint on the login
-- WARNING: Celerio will use this unique constraint to 
--ALTER TABLE APP_USER ADD CONSTRAINT UNIQUE_APP_USER_LOGIN UNIQUE(LOGIN);

-- add admin user
INSERT INTO APP_USER VALUES (-1, 'admin', 'admin', 'admin@admin.com', 'FR', 'admin', 'admin', 1, null, 0, 0/*, sysdate*/);

CREATE TABLE APP_AUTHORITY (
	ID					int not null  IDENTITY,
	NAME				varchar(250) not null,
	
	primary key (ID)
);
-- add admin role (lower case)
INSERT INTO APP_AUTHORITY  VALUES (-1, 'admin');

--
-- relation table between app_user and app_authority
-- 
CREATE TABLE APP_USER_AUTHORITY (
	ID  						int not null identity,
	APP_USER_ID					int not null,
	APP_AUTHORITY_ID			int not null,
	
	constraint APP_USER_AUTHORITY_fk_1 foreign key (APP_USER_ID) references APP_USER(ID),
	constraint APP_USER_AUTHORITY_fk_2 foreign key (APP_AUTHORITY_ID) references APP_AUTHORITY(ID),
	
	primary key (ID)
);
-- link admin role to admin user
INSERT INTO APP_USER_AUTHORITY  VALUES (-1, -1, -1);
--
-- Security tokens
--
CREATE TABLE APP_TOKEN (
	SERIES					varchar(50) not null,
	TOKEN_VALUE				varchar(50),
	TOKEN_CREATION_DATE		timestamp,
	IP_ADDRESS				varchar(250) not null,
	USER_AGENT				varchar(250) not null,
	USER_LOGIN				varchar(250) not null,
	
	primary key (SERIES)
);



---
-- Example Schema
--
CREATE TABLE APP_TRANSLATION (
    ID                  int not null  IDENTITY,
    LANGUAGE			varchar(10) not null,
    KEY                varchar(1000) not null,
    VALUE          	varchar(4000),
    primary key (ID)
);

CREATE TABLE "APP_PARAMETER" (
    "ID"                  int not null  IDENTITY,
    "DOMAIN"				varchar(250) not null,
    "KEY"                varchar(1000) not null,
    "VALUE" 		        varchar(4000),
    primary key ("ID")
);

INSERT INTO APP_TRANSLATION  VALUES (1, 'fr_FR', 'TITLE', 'le titre en français.');
INSERT INTO APP_TRANSLATION  VALUES (2, 'en', 'TITLE', 'le titre en anglais.');

--INSERT INTO APP_PARAMETER VALUES (1, 'SETTINGS', 'defaultDateFormat', 'dd/MM/yyyy');