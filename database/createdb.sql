-- Murach’s music db made portable:
-- single-case database identifiers (caseless by SQL standard)
-- no auto_increment, enum columns, datetime type (MySQL-specific)
-- standard INSERTs
-- all implemented columns are "not null", except address1 of site_user
--   which is null until user fully registered as a customer
-- Note: we generate tables for both DBs even if we only use one of them

-- Catalog DB tables ----------------------
-- Note money as decimal here
CREATE TABLE product(
    product_id INT NOT NULL,
    product_code VARCHAR(10) NOT NULL,
    product_description VARCHAR(100) NOT NULL,
    product_price DECIMAL(10,2) NOT NULL,  
    UNIQUE (product_code),   
    PRIMARY KEY (product_id)
);
  
INSERT INTO product VALUES 
  (1, '8601', '86 (the band) - True Life Songs and Pictures', '14.95');
INSERT INTO product VALUES
  (2, 'pf01', 'Paddlefoot - The first CD', '12.95');
INSERT INTO product VALUES 
  (3, 'pf02', 'Paddlefoot - The second CD', '14.95');
INSERT INTO product VALUES 
  (4, 'jr01', 'Joe Rut - Genuine Wood Grained Finish', '14.95');
  
--create track before download: download has FK to track
CREATE TABLE track (
  track_id INT NOT NULL PRIMARY KEY,
  product_id INT NOT NULL,
  track_number INT NOT NULL,
  title varchar(100) NOT NULL,
  sample_filename varchar(100) NOT NULL, 
  FOREIGN KEY (product_id) REFERENCES product (product_id),
  UNIQUE (product_id, track_number)
);

INSERT INTO track VALUES (1, 1, 2, 'You Are a Star', 'star.mp3');
INSERT INTO track VALUES (2, 1, 3, 'Don''t Make No Difference', 'no_difference.mp3');
INSERT INTO track VALUES (3, 2, 2, 'Whiskey Before Breakfast', 'whiskey.mp3');
INSERT INTO track VALUES (4, 2, 6, '64 Corvair, Part 2', 'corvair.mp3');
INSERT INTO track VALUES (5, 3, 1, 'Neon Lights', 'neon.mp3');
INSERT INTO track VALUES (6, 3, 3, 'Tank Hill', 'tank.mp3');
INSERT INTO track VALUES (7, 4, 1, 'Filter', 'filter.mp3');
INSERT INTO track VALUES (8, 4, 5, 'So Long Lazy Ray', 'so_long.mp3');

-- Note track_id for more exact id of download than in Murach
-- Use email_address, not user_id to link to user table to
-- avoid a FK between catalogDB and salesDB
CREATE TABLE download (
    download_id INT NOT NULL,
    email_address VARCHAR(50) NOT NULL,
    download_date TIMESTAMP NOT NULL,
    track_id INT NOT NULL, 
    PRIMARY KEY (download_id), 
    FOREIGN KEY (track_id) REFERENCES track (track_id)
);

-- Sales DB tables----------------------------------
-- site_user: many unimplemented columns for our project
-- avoid identifier 'user', a reserved word in SQL
-- make them "not null" if ever implemented
-- note email_address is a second table key
-- address1 is nullable to allow null address until checkout

CREATE TABLE site_user (
    user_id INT NOT NULL,
    firstname VARCHAR(50) NOT NULL,
    lastname VARCHAR(50) NOT NULL,
    email_address VARCHAR(50) NOT NULL,
    company_name VARCHAR(50),
    address1 VARCHAR(50),
    address2 VARCHAR(50),
    city VARCHAR(50),
    state VARCHAR(50),
    zip VARCHAR(50),
    country VARCHAR(50),
    creditcard_type VARCHAR(50),
    creditcard_number VARCHAR(50),
    creditcard_expirationdate VARCHAR(50),
    UNIQUE(email_address),
    PRIMARY KEY (user_id)
);

-- is_processed has values 'n' or 'y'
CREATE TABLE invoice(
    invoice_id INT NOT NULL,
    user_id INT NOT NULL,
    invoice_date TIMESTAMP NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    is_processed CHAR(1) NOT NULL, 
    PRIMARY KEY (invoice_id),
    FOREIGN KEY (user_id) REFERENCES site_user (user_id)
);

// use product_code, not product_id, to link to product
// avoiding a FK from one salesDB to catalogDB
CREATE TABLE lineitem(
    lineitem_id INT NOT NULL,
    invoice_id INT NOT NULL,
    product_code VARCHAR(10) NOT NULL,
    quantity INT NOT NULL,  
    PRIMARY KEY (lineitem_id),
    FOREIGN KEY (invoice_id) REFERENCES invoice (invoice_id)
);

CREATE TABLE userpass (
  username varchar(15) NOT NULL PRIMARY KEY,
  password varchar(15) NOT NULL
);
  
INSERT INTO userpass VALUES ('andrea', 'sesame');
INSERT INTO userpass VALUES ('joel', 'sesame');
INSERT INTO userpass VALUES ('anne', 'sesame');
                          
CREATE TABLE userrole (   
  username VARCHAR(15) NOT NULL,
  rolename VARCHAR(15) NOT NULL,
  PRIMARY KEY (username, rolename)
);
  
INSERT INTO userrole VALUES ('andrea', 'service');
INSERT INTO userrole VALUES ('andrea', 'programmer');
INSERT INTO userrole VALUES ('joel', 'programmer');

--added table for generating ids (for both DBs)--
create table music_sys_tab (
				invoice_id integer not null,
				user_id integer not null,
				download_id integer not null,
				lineitem_id integer not null);
				
insert into music_sys_tab values (1, 1, 1, 1);


