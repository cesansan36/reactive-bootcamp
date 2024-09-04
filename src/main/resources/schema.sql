CREATE TABLE IF NOT EXISTS `bootcamp` (
                             `id` BIGINT NOT NULL AUTO_INCREMENT,
                             `name` VARCHAR(50) NOT NULL,
                             `description` VARCHAR(90) NOT NULL,
                             PRIMARY KEY (`id`),
                             UNIQUE INDEX `bootcampcol_UNIQUE` (`name` ASC) VISIBLE);