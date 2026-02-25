CREATE TABLE `students` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `name` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `birth_date` date DEFAULT NULL,
  `hometown` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB 


AUTO_INCREMENT=56 
DEFAULT CHARSET=utf8mb4 
COLLATE=utf8mb4_0900_ai_ci;
