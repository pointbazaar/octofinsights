-- MySQL dump 10.13  Distrib 5.7.27, for Linux (x86_64)
--
-- Host: localhost    Database: octofinsights
-- ------------------------------------------------------
-- Server version	5.7.27-0ubuntu0.16.04.1

--
-- Table structure for table `employees`
--


CREATE TABLE IF NOT EXISTS `employees` (
  `id` int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `employee_name` varchar(32) DEFAULT NULL,
  `employee_role` varchar(32) DEFAULT NULL,
  `employee_email` varchar(64) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;


--
-- Table structure for table `expenses`
--


CREATE TABLE IF NOT EXISTS `expenses` (
  `id` int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `expense_name` varchar(64) DEFAULT NULL,
  `expense_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `expense_value` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=latin1;


--
-- Table structure for table `inventory`
--



CREATE TABLE IF NOT EXISTS `inventory` (
  `id` int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `item_name` varchar(128) NOT NULL,
  `item_price` int(11) NOT NULL,
  `amount` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;


--
-- Table structure for table `leads`
--

CREATE TABLE IF NOT EXISTS `leads` (
  `id` int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `lead_name` varchar(64) DEFAULT NULL,
  `lead_status` varchar(32) DEFAULT NULL,
  `date_of_lead_entry` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `what_the_lead_wants` varchar(64) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=latin1;


--
-- Table structure for table `sales`
--


CREATE TABLE IF NOT EXISTS `sales` (
  `id` int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `customer_id` int(11) DEFAULT NULL,
  `time_of_sale` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `price_of_sale` int(11) DEFAULT NULL,
  `product_or_service` varchar(128) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `category` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=latin1;


--
-- Table structure for table `users`
--
CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `username` varchar(80) DEFAULT NULL,
  `password` varchar(500) DEFAULT NULL,
  `email` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=latin1;


CREATE TABLE IF NOT EXISTS `projects` (
    `id` int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `user_id` int(11) DEFAULT NULL,
    `project_name` varchar(500) DEFAULT NULL,
    `project_start` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `project_end_estimate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `project_end` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `initial_effort_estimate_hours` int(11) DEFAULT NULL,
    `project_earnings_estimate` int(11) DEFAULT NULL,
    `project_description` varchar(10000) DEFAULT NULL,
    `isactive` boolean DEFAULT TRUE,
    `customer_id` int(11) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `tasks` (
    `id` int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `user_id` int(11) DEFAULT NULL,
    `project_id` int(11) DEFAULT NULL,

    `task_start_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `task_complete_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,

    `initial_effort_estimate_hours` int(11) DEFAULT NULL,
    `effort_spent` int(11) DEFAULT NULL,

    `task_name` varchar(2000) DEFAULT NULL,
    `iscompleted` boolean DEFAULT FALSE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;


CREATE TABLE IF NOT EXISTS customers (
    id int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    user_id int(11) DEFAULT NULL,
    acquisition_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,

    customer_name varchar(500) DEFAULT NULL,
    source varchar(500) DEFAULT NULL
);

-- invoices table contains items that customers have not paid for. so each invoice item needs a customer_id to
-- see which customer should pay.

CREATE TABLE IF NOT EXISTS invoices (
    id int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    user_id int(11) NOT NULL,
    customer_id int(11) NOT NULL,
    product_or_service varchar(500) NOT NULL,
    price int(11) NOT NULL
);