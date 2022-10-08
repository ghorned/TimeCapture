CREATE DATABASE `time_capture`;

USE 'time_capture';

CREATE TABLE `employee` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `password` varchar(100) NOT NULL,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `week_hrs_min` double NOT NULL,
  `week_hrs_max` double NOT NULL,
  `access_authority` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `employee_id_UNIQUE` (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`)
);

CREATE TABLE `punch` (
  `id` int NOT NULL AUTO_INCREMENT,
  `employee_id` int NOT NULL,
  `time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `employee_id_idx` (`employee_id`),
  CONSTRAINT `employee_id` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`)
);

INSERT INTO `time_capture`.`employee` (`username`, `password`, `first_name`, `last_name`, `week_hrs_min`, `week_hrs_max`, `access_authority`)
VALUES ('JSmith', '$2a$10$OWChFs7YQ5BhO1Ko.AeQXuSk2o81yS.zONGNP8htLAdDipaR4K7Ua', 'John', 'Smith', '40', '45', 'ADMIN');
