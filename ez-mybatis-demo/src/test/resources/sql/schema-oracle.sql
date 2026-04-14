CREATE TABLE ez_complex_user (
  id VARCHAR2(32) NOT NULL PRIMARY KEY,
  create_time DATE,
  update_time DATE,
  username VARCHAR2(100),
  age NUMBER,
  user_type NUMBER(5),
  score NUMBER(19),
  account_balance FLOAT,
  salary NUMBER(10,2),
  is_active NUMBER(1),
  status NUMBER(3),
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
  create_time DATE,
  update_time DATE,
  user_id VARCHAR2(32),
  order_no VARCHAR2(50),
  total_amount NUMBER(10,2),
  status NUMBER(3)
);

CREATE TABLE ez_complex_department (
  id VARCHAR2(32) NOT NULL PRIMARY KEY,
  create_time DATE,
  update_time DATE,
  name VARCHAR2(100),
  parent_id VARCHAR2(32),
  description VARCHAR2(500)
);

CREATE TABLE ez_complex_order_item (
  id VARCHAR2(32) NOT NULL PRIMARY KEY,
  create_time DATE,
  update_time DATE,
  order_id VARCHAR2(32),
  product_id VARCHAR2(32),
  quantity NUMBER,
  unit_price NUMBER(10,2),
  item_total_amount NUMBER(10,2)
);

CREATE TABLE ez_complex_order_archive (
  id VARCHAR2(32) NOT NULL PRIMARY KEY,
  create_time DATE,
  update_time DATE,
  user_id VARCHAR2(32),
  order_no VARCHAR2(50),
  total_amount NUMBER(10,2),
  status NUMBER(3)
);
