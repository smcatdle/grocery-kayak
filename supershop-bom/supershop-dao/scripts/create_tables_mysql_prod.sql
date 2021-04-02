DROP TABLE product;
DROP TABLE basketitem;
DROP TABLE basket;
DROP TABLE account;
DROP TABLE promotion_string;
DROP TABLE promotion;

CREATE TABLE product
(
  id BIGINT(3) UNSIGNED ZEROFILL NOT NULL,
  product_code VARCHAR(20),
  price DOUBLE(11,2) NOT NULL,
  name VARCHAR(250),
  barcode VARCHAR(20),
  chain VARCHAR(2),
  category VARCHAR(2),
  image_url VARCHAR(200),
  super_department VARCHAR(50),
  shelf_name VARCHAR(50),
  date_created DATE NOT NULL,
  date_updated DATE NOT NULL,
  CONSTRAINT product_id_uq UNIQUE (id),
  CONSTRAINT product_id_chain_uq UNIQUE (name, chain)
);

CREATE TABLE account
(
  id MEDIUMINT(3) UNSIGNED ZEROFILL NOT NULL,
  email VARCHAR(100),
  password VARCHAR(100),
  role VARCHAR(1),
  ip VARCHAR(40),
  reset_code VARCHAR(30),
  date_created DATE NOT NULL,
  date_updated DATE NOT NULL,
  CONSTRAINT account_id_uq UNIQUE (id),
  CONSTRAINT account_email_uq UNIQUE (email)
);

CREATE TABLE basket
(
  id MEDIUMINT(3) UNSIGNED ZEROFILL NOT NULL,
  name VARCHAR(12),
  chain VARCHAR(1),
  date_created DATE NOT NULL,
  date_updated DATE NOT NULL,
  CONSTRAINT basket_id_uq UNIQUE (id),
  CONSTRAINT basket_name_uq UNIQUE (name)
);

CREATE TABLE basketitem
(
  id MEDIUMINT(3) UNSIGNED ZEROFILL NOT NULL,
  account_id MEDIUMINT(3) UNSIGNED ZEROFILL NOT NULL,
  basket_id MEDIUMINT(3) UNSIGNED ZEROFILL,
  product_id BIGINT(3) UNSIGNED ZEROFILL,
  barcode VARCHAR(20),
  name VARCHAR(200) NOT NULL,
  url VARCHAR(80),
  image_url VARCHAR(120),
  price DOUBLE(11,2) NOT NULL,
  chain VARCHAR(1) NOT NULL,
  amount MEDIUMINT(3) NOT NULL,
  date_created DATE NOT NULL,
  date_updated DATE NOT NULL,
  CONSTRAINT basketitem_id_uq UNIQUE (id),
  CONSTRAINT basketitem_name_chain_uq UNIQUE (name,chain,account_id),
  CONSTRAINT basketitem_account_id_fk FOREIGN KEY (account_id)
      REFERENCES account (id),
  CONSTRAINT basketitem_basket_id_fk FOREIGN KEY (basket_id)
      REFERENCES basket (id)
);

CREATE TABLE promotion
(
  id MEDIUMINT(3) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT,
  name VARCHAR(50),
  priority MEDIUMINT(3) UNSIGNED ZEROFILL NOT NULL,
  date_created DATE NOT NULL,
  date_updated DATE NOT NULL,
  CONSTRAINT promotion_id_uq UNIQUE (id),
  CONSTRAINT promotion_name_uq UNIQUE (name)
);

CREATE TABLE promotion_string
(
  id MEDIUMINT(3) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT,
  promotion_id MEDIUMINT(3) UNSIGNED ZEROFILL NOT NULL,
  string VARCHAR(20),
  date_created DATE NOT NULL,
  date_updated DATE NOT NULL,
  CONSTRAINT promotion_string_id_uq UNIQUE (id),
  CONSTRAINT promotion_string_promotion_id_fk FOREIGN KEY (promotion_id)
      REFERENCES promotion (id)
);

CREATE TABLE page_link_history
(
  id MEDIUMINT(3) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT,
  page_link VARCHAR(200),
  hash VARCHAR(50),
  date_created DATE NOT NULL,
  date_updated DATE NOT NULL,
  CONSTRAINT page_link_history_id_uq UNIQUE (id)
);

CREATE TABLE audit
(
  id MEDIUMINT(3) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT,
  string VARCHAR(200),
  date_created DATE NOT NULL,
  date_updated DATE NOT NULL,
  CONSTRAINT audit_id_uq UNIQUE (id)
);

-- Indexes
CREATE INDEX product_name_idx ON product (name);

-- FULLTEXT Search
ALTER TABLE product ENGINE = MYISAM;
ALTER TABLE product ADD FULLTEXT product_name_shelf_name_idx(name,shelf_name);
--ALTER TABLE product ADD FULLTEXT product_name_idx(name);

-- Stores
INSERT INTO basket (id, name, chain, date_created, date_updated) VALUES (0, 'All', 'x', CURDATE(), CURDATE());
INSERT INTO basket (id, name, chain, date_created, date_updated) VALUES (1, 'Tesco', 't', CURDATE(), CURDATE());
INSERT INTO basket (id, name, chain, date_created, date_updated) VALUES (2, 'SuperValu', 'v', CURDATE(), CURDATE());
INSERT INTO basket (id, name, chain, date_created, date_updated) VALUES (3, 'Aldi', 'a', CURDATE(), CURDATE());
INSERT INTO basket (id, name, chain, date_created, date_updated) VALUES (4, 'EuroSpar', 'e', CURDATE(), CURDATE());
INSERT INTO basket (id, name, chain, date_created, date_updated) VALUES (5, 'Mace', 'm', CURDATE(), CURDATE());
INSERT INTO basket (id, name, chain, date_created, date_updated) VALUES (6, 'Dealz', 'd', CURDATE(), CURDATE());
INSERT INTO basket (id, name, chain, date_created, date_updated) VALUES (7, 'Lidl', 'l', CURDATE(), CURDATE());




-- Promotions
INSERT INTO promotion (name, priority, date_created, date_updated) VALUES ('Chicken', 1, CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Chicken', CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Whole ', CURDATE(), CURDATE());

INSERT INTO promotion (name, priority, date_created, date_updated) VALUES ('Lyons', 2, CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Lyons', CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Tea', CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), '80', CURDATE(), CURDATE());

INSERT INTO promotion (name, priority, date_created, date_updated) VALUES ('Kit Kat', 1, CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Kit', CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Kat', CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Pack', CURDATE(), CURDATE());

INSERT INTO promotion (name, priority, date_created, date_updated) VALUES ('Carlsberg', 1, CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Carlsberg', CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), '20', CURDATE(), CURDATE());

INSERT INTO promotion (name, priority, date_created, date_updated) VALUES ('Smirnoff', 1, CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Vodka', CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), ' Litre', CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Smirnoff', CURDATE(), CURDATE());

INSERT INTO promotion (name, priority, date_created, date_updated) VALUES (' Huzzar Vodka', 1, CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Vodka', CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), '70', CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Huzzar', CURDATE(), CURDATE());

INSERT INTO promotion (name, priority, date_created, date_updated) VALUES ('Baileys', 1, CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Baileys', CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), '70', CURDATE(), CURDATE());

INSERT INTO promotion (name, priority, date_created, date_updated) VALUES ('Ketchup', 1, CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Ketchup', CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Heinz', CURDATE(), CURDATE());

INSERT INTO promotion (name, priority, date_created, date_updated) VALUES ('Lamb', 1, CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Lamb', CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Leg', CURDATE(), CURDATE());

INSERT INTO promotion (name, priority, date_created, date_updated) VALUES ('Sirloin', 1, CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Sirloin', CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Steak', CURDATE(), CURDATE());

INSERT INTO promotion (name, priority, date_created, date_updated) VALUES ('Mayonnaise', 1, CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Mayonnaise', CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Hellmann', CURDATE(), CURDATE());

INSERT INTO promotion (name, priority, date_created, date_updated) VALUES ('Fillet', 1, CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Fillet', CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Steak', CURDATE(), CURDATE());

INSERT INTO promotion (name, priority, date_created, date_updated) VALUES ('Pinot Grigio', 1, CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Pinot', CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Grigio', CURDATE(), CURDATE());

INSERT INTO promotion (name, priority, date_created, date_updated) VALUES ('Nescafe Coffee', 1, CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Nescafe', CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Coffee', CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Instant', CURDATE(), CURDATE());

INSERT INTO promotion (name, priority, date_created, date_updated) VALUES ('Beef Burgers', 1, CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Beef', CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Burgers', CURDATE(), CURDATE());

INSERT INTO promotion (name, priority, date_created, date_updated) VALUES ('Tayto Crisps', 1, CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Tayto', CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Crisps', CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'X', CURDATE(), CURDATE());

INSERT INTO promotion (name, priority, date_created, date_updated) VALUES ('Rashers', 1, CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Rashers', CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'smoke', CURDATE(), CURDATE());

INSERT INTO promotion (name, priority, date_created, date_updated) VALUES ('Sausages', 1, CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Sausages', CURDATE(), CURDATE());

INSERT INTO promotion (name, priority, date_created, date_updated) VALUES ('Vanish Stain Remover', 1, CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Vanish', CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'oxi', CURDATE(), CURDATE());

INSERT INTO promotion (name, priority, date_created, date_updated) VALUES ('Ben and Jerry Ice Cream', 1, CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Ben', CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Jerry', CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Ice', CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Cream', CURDATE(), CURDATE());

INSERT INTO promotion (name, priority, date_created, date_updated) VALUES ('Bold Washing', 1, CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), '40', CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Washing', CURDATE(), CURDATE());

INSERT INTO promotion (name, priority, date_created, date_updated) VALUES ('Prawns', 1, CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Prawns', CURDATE(), CURDATE());

INSERT INTO promotion (name, priority, date_created, date_updated) VALUES ('Salmon Fillets', 1, CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Salmon', CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Fillets', CURDATE(), CURDATE());

INSERT INTO promotion (name, priority, date_created, date_updated) VALUES ('Charleville', 1, CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Charleville', CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Cheddar', CURDATE(), CURDATE());

INSERT INTO promotion (name, priority, date_created, date_updated) VALUES ('Whole Chicken', 1, CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Whole', CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), 'Chicken', CURDATE(), CURDATE());
INSERT INTO promotion_string (promotion_id, string, date_created, date_updated) VALUES ((select count(1) from promotion), '1', CURDATE(), CURDATE());

ALTER TABLE product ADD FULLTEXT product_name_shelf_name_idx(name,shelf_name); 
ALTER TABLE product ADD COLUMN aisle VARCHAR(50); 

ALTER TABLE product ADD INDEX product_super_department_shelf_name_idx(super_department,shelf_name);
ALTER TABLE product ADD max_price DOUBLE(11,2) default 0;
ALTER TABLE product ADD min_price DOUBLE(11,2) default 0;
ALTER TABLE product ADD last_price DOUBLE(11,2) default 0;
ALTER TABLE product ADD min_price_date DATE default '2015-10-25';
ALTER TABLE product ADD max_price_date DATE default '2015-10-25';
ALTER TABLE product ADD last_price_date DATE default '2015-10-25';

ALTER TABLE basketitem ADD COLUMN available BOOLEAN;
ALTER TABLE basketitem MODIFY COLUMN available BOOLEAN default true;
ALTER TABLE basketitem MODIFY COLUMN name VARCHAR(200) NOT NULL;
ALTER TABLE basketitem ADD COLUMN strikethrough BOOLEAN default false;

-- Alter data after install




