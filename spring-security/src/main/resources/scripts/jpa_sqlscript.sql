
CREATE TABLE `Customer` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `email` varchar(255) DEFAULT NULL,
    `password` varchar(255) DEFAULT NULL,
    `role` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
);

INSERT INTO `Customer` VALUES (1,'user@gmail.com','{noop}mypass@01','USER');
INSERT INTO `Customer` VALUES (2,'admin@gmail.com','{bcrypt}$2y$12$YnPVVUzj9ZE97xxHANVHRO3KPudi7RVjXXQgh3ttSGUa7woqquSsO','ADMIN');
INSERT INTO `Customer` VALUES (3,'manager@gmail.com','{SHA-256}0f439c8f8b72f3b19479597bf175b6219a3c2181011cd02e250e0f3fba09d5fc','MANAGER');