CREATE TABLE IF NOT EXISTS `ez_complex_user` (
  `id` varchar(32) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `username` varchar(100) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `user_type` smallint(6) DEFAULT NULL,
  `score` bigint(20) DEFAULT NULL,
  `account_balance` double DEFAULT NULL,
  `salary` decimal(10,2) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `avatar` blob,
  `description` text,
  `custom_column_name` varchar(100) DEFAULT NULL,
  `secret_content` varchar(500) DEFAULT NULL,
  `ext_info` varchar(1000) DEFAULT NULL,
  `department_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `ez_complex_order` (
  `id` varchar(32) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `user_id` varchar(32) DEFAULT NULL,
  `order_no` varchar(50) DEFAULT NULL,
  `total_amount` decimal(10,2) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `ez_complex_department` (
  `id` varchar(32) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `parent_id` varchar(32) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `ez_complex_order_item` (
  `id` varchar(32) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `order_id` varchar(32) DEFAULT NULL,
  `product_id` varchar(32) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `unit_price` decimal(10,2) DEFAULT NULL,
  `item_total_amount` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `ez_complex_order_archive` (
  `id` varchar(32) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `user_id` varchar(32) DEFAULT NULL,
  `order_no` varchar(50) DEFAULT NULL,
  `total_amount` decimal(10,2) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
