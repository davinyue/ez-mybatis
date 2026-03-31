CREATE TABLE ez_complex_user (
  id varchar(32) NOT NULL PRIMARY KEY,
  create_time timestamp,
  update_time timestamp,
  username varchar(100),
  age integer,
  user_type smallint,
  score bigint,
  account_balance double precision,
  salary numeric(10,2),
  is_active boolean,
  status smallint,
  birthday date,
  avatar bytea,
  description text,
  custom_column_name varchar(100),
  secret_content varchar(500),
  ext_info varchar(1000),
  department_id varchar(32)
);

CREATE TABLE ez_complex_order (
  id varchar(32) NOT NULL PRIMARY KEY,
  create_time timestamp,
  update_time timestamp,
  user_id varchar(32),
  order_no varchar(50),
  total_amount numeric(10,2),
  status smallint
);

CREATE TABLE ez_complex_department (
  id varchar(32) NOT NULL PRIMARY KEY,
  create_time timestamp,
  update_time timestamp,
  name varchar(100),
  parent_id varchar(32),
  description varchar(500)
);

CREATE TABLE ez_complex_order_item (
  id varchar(32) NOT NULL PRIMARY KEY,
  create_time timestamp,
  update_time timestamp,
  order_id varchar(32),
  product_id varchar(32),
  quantity integer,
  unit_price numeric(10,2),
  item_total_amount numeric(10,2)
);

CREATE TABLE ez_complex_order_archive (
  id varchar(32) NOT NULL PRIMARY KEY,
  create_time timestamp,
  update_time timestamp,
  user_id varchar(32),
  order_no varchar(50),
  total_amount numeric(10,2),
  status smallint
);
