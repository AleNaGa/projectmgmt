-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema portfolio
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema portfolio
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `portfolio` DEFAULT CHARACTER SET utf8mb4 ;
USE `portfolio` ;

-- -----------------------------------------------------
-- Table `portfolio`.`status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `portfolio`.`status` (
  `status_id` INT NOT NULL AUTO_INCREMENT,
  `status_name` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`status_id`),
  UNIQUE INDEX `status_name_UNIQUE` (`status_name` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `portfolio`.`projects`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `portfolio`.`projects` (
  `project_id` INT NOT NULL AUTO_INCREMENT,
  `project_name` VARCHAR(45) NOT NULL,
  `description` LONGTEXT NULL,
  `start_date` DATE NULL,
  `end_date` DATE NULL,
  `repository_url` VARCHAR(255) NULL,
  `demo_url` VARCHAR(255) NULL,
  `picture` VARCHAR(255) NULL,
  `status_status_id` INT NOT NULL,
  PRIMARY KEY (`project_id`),
  UNIQUE INDEX `project_name_UNIQUE` (`project_name` ASC) VISIBLE,
  INDEX `fk_projects_status_idx` (`status_status_id` ASC) VISIBLE,
  CONSTRAINT `fk_projects_status`
    FOREIGN KEY (`status_status_id`)
    REFERENCES `portfolio`.`status` (`status_id`)
    ON DELETE cascade
    ON UPDATE Cascade)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `portfolio`.`technologies`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `portfolio`.`technologies` (
  `tech_id` INT NOT NULL AUTO_INCREMENT,
  `tech_name` VARCHAR(45) NULL,
  PRIMARY KEY (`tech_id`),
  UNIQUE INDEX `tech_name_UNIQUE` (`tech_name` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `portfolio`.`developers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `portfolio`.`developers` (
  `dev_id` INT NOT NULL AUTO_INCREMENT,
  `dev_name` VARCHAR(45) NULL,
  `dev_surname` VARCHAR(45) NULL,
  `email` VARCHAR(255) NULL,
  `linkedin_url` VARCHAR(255) NULL,
  `github_url` VARCHAR(255) NULL,
  PRIMARY KEY (`dev_id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
  UNIQUE INDEX `linkedin_url_UNIQUE` (`linkedin_url` ASC) VISIBLE,
  UNIQUE INDEX `github_url_UNIQUE` (`github_url` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `portfolio`.`developers_worked_on_projects`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `portfolio`.`developers_worked_on_projects` (
  `developers_dev_id` INT NOT NULL,
  `projects_project_id` INT NOT NULL,
  PRIMARY KEY (`developers_dev_id`, `projects_project_id`),
  INDEX `fk_developers_has_projects_projects1_idx` (`projects_project_id` ASC) VISIBLE,
  INDEX `fk_developers_has_projects_developers1_idx` (`developers_dev_id` ASC) VISIBLE,
  CONSTRAINT `fk_developers_has_projects_developers1`
    FOREIGN KEY (`developers_dev_id`)
    REFERENCES `portfolio`.`developers` (`dev_id`)
    ON DELETE cascade
    ON UPDATE cascade,
  CONSTRAINT `fk_developers_has_projects_projects1`
    FOREIGN KEY (`projects_project_id`)
    REFERENCES `portfolio`.`projects` (`project_id`)
    ON DELETE Cascade
    ON UPDATE Cascade)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `portfolio`.`technologies_used_in_projects`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `portfolio`.`technologies_used_in_projects` (
  `technologies_tech_id` INT NOT NULL,
  `projects_project_id` INT NOT NULL,
  PRIMARY KEY (`technologies_tech_id`, `projects_project_id`),
  INDEX `fk_technologies_has_projects_projects1_idx` (`projects_project_id` ASC) VISIBLE,
  INDEX `fk_technologies_has_projects_technologies1_idx` (`technologies_tech_id` ASC) VISIBLE,
  CONSTRAINT `fk_technologies_has_projects_technologies1`
    FOREIGN KEY (`technologies_tech_id`)
    REFERENCES `portfolio`.`technologies` (`tech_id`)
    ON DELETE cascade
    ON UPDATE cascade,
  CONSTRAINT `fk_technologies_has_projects_projects1`
    FOREIGN KEY (`projects_project_id`)
    REFERENCES `portfolio`.`projects` (`project_id`)
    ON DELETE Cascade
    ON UPDATE Cascade)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

INSERT INTO `portfolio`.`status` VALUES (1, "In Development");
INSERT INTO `portfolio`.`status` VALUES (2, "Testing");
INSERT INTO `portfolio`.`status` VALUES (3, "In Production");
insert into developers values (0,"DEV1","Developer", "developer@gmail.com", "www.github.com", "www.linkedin.es");
insert into technologies values (0,"JavaScript");
Insert into technologies values (0, "Java");
Insert into technologies values (0, "Python");
Insert into technologies values (0, "Mysql");