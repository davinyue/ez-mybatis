CREATE TABLE ez_complex_user (
  id nvarchar(32) NOT NULL PRIMARY KEY,
  create_time datetime2,
  update_time datetime2,
  username nvarchar(100),
  age int,
  user_type smallint,
  score bigint,
  account_balance float,
  salary decimal(10,2),
  is_active bit,
  status smallint,
  birthday date,
  avatar varbinary(max),
  description nvarchar(max),
  custom_column_name nvarchar(100),
  secret_content nvarchar(500),
  ext_info nvarchar(1000),
  department_id nvarchar(32)
);

CREATE TABLE ez_complex_order (
  id nvarchar(32) NOT NULL PRIMARY KEY,
  create_time datetime2,
  update_time datetime2,
  user_id nvarchar(32),
  order_no nvarchar(50),
  total_amount decimal(10,2),
  status smallint
);

CREATE TABLE ez_complex_department (
  id nvarchar(32) NOT NULL PRIMARY KEY,
  create_time datetime2,
  update_time datetime2,
  name nvarchar(100),
  parent_id nvarchar(32),
  description nvarchar(500)
);

CREATE TABLE ez_complex_order_item (
  id nvarchar(32) NOT NULL PRIMARY KEY,
  create_time datetime2,
  update_time datetime2,
  order_id nvarchar(32),
  product_id nvarchar(32),
  quantity int,
  unit_price decimal(10,2),
  item_total_amount decimal(10,2)
);

CREATE TABLE ez_complex_order_archive (
  id nvarchar(32) NOT NULL PRIMARY KEY,
  create_time datetime2,
  update_time datetime2,
  user_id nvarchar(32),
  order_no nvarchar(50),
  total_amount decimal(10,2),
  status smallint
);
