CREATE TABLE ez_complex_user (
  id VARCHAR2(32) NOT NULL PRIMARY KEY,
  create_time TIMESTAMP,
  update_time TIMESTAMP,
  username VARCHAR2(100),
  age INT,
  user_type SMALLINT,
  score BIGINT,
  account_balance DOUBLE,
  salary DECIMAL(10,2),
  is_active TINYINT,
  status TINYINT,
  birthday DATE,
  avatar BLOB,
  description CLOB,
  custom_column_name VARCHAR2(100),
  secret_content VARCHAR2(500),
  ext_info VARCHAR2(1000),
  department_id VARCHAR2(32)
);

CREATE TABLE ez_complex_order (
  id VARCHAR2(32) NOT NULL PRIMARY KEY,
  create_time TIMESTAMP,
  update_time TIMESTAMP,
  user_id VARCHAR2(32),
  order_no VARCHAR2(50),
  total_amount DECIMAL(10,2),
  status TINYINT
);

CREATE TABLE ez_complex_department (
  id VARCHAR2(32) NOT NULL PRIMARY KEY,
  create_time TIMESTAMP,
  update_time TIMESTAMP,
  name VARCHAR2(100),
  parent_id VARCHAR2(32),
  description VARCHAR2(500)
);

CREATE TABLE ez_complex_order_item (
  id VARCHAR2(32) NOT NULL PRIMARY KEY,
  create_time TIMESTAMP,
  update_time TIMESTAMP,
  order_id VARCHAR2(32),
  product_id VARCHAR2(32),
  quantity INT,
  unit_price DECIMAL(10,2),
  item_total_amount DECIMAL(10,2)
);

CREATE TABLE ez_complex_order_archive (
  id VARCHAR2(32) NOT NULL PRIMARY KEY,
  create_time TIMESTAMP,
  update_time TIMESTAMP,
  user_id VARCHAR2(32),
  order_no VARCHAR2(50),
  total_amount DECIMAL(10,2),
  status TINYINT
);
