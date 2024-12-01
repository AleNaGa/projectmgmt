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

INSERT INTO `status` VALUES (1,'In Development'),(3,'In Production'),(2,'Testing');
INSERT INTO `developers` VALUES (5,'Alejandro','Navarro','alejandronavarromgmt@gmail.com','https://www.linkedin.com','https://github.com/AleNaGa');
INSERT INTO `technologies` VALUES (20,'Astro'),(19,'HTML'),(2,'Java'),(12,'JavaScript'),(13,'MySQL'),(18,'PHP'),(21,'React'),(17,'SpringBoot'),(15,'Tailwind');
INSERT INTO `projects` VALUES (9,'Pokedex','Front con JS para consumir y renderizar un buscador de Pokemons. Consume una API de terceros. ','2024-10-15','2024-10-16','https://github.com/AleNaGa/pokedex','https://github.com/AleNaGa/pokedex','/projects/pokedex.png',3),(13,'ProjectMGMT','API REST para hacer operaciones CRUD de gestión de proyectos. Con Developers, tecnologías y proyectos. ','2024-11-11','2024-11-27','https://github.com/AleNaGa/projectmgmt','https://github.com/AleNaGa/projectmgmt','/projects/projectmgmt.png',2),(14,'Clon de Twitter','Aplicación monolítica simulando Twitter. Con registro y login además de acciones como tuitear, seguir, consultar seguidores y dar like. ','2024-10-07','2024-11-19','https://github.com/AleNaGa/clonDeTwittr','https://github.com/AleNaGa/clonDeTwittr','/projects/twitterclone.png',1),(15,'Prueba Técnica CRUD','Desarrollo de una API de datos poblacionales a nivel mundial consumiendo datos de una API de terceros. ','2024-11-05','2024-11-15','https://github.com/AleNaGa/pruebaTecnica1','https://github.com/AleNaGa/pruebaTecnica1','/projects/TechTest.png',3),(16,'Front de CRUD','App frontal en JS para recoger insertar editar y borrar datos de una API Crud cuyo endpoint insertas en un formulario. ','2024-11-01','2024-11-10','https://github.com/AleNaGa/clienteEntregable12','https://alenaga.github.io/clienteEntregable12/','/projects/crud.png',3),(17,'Portfolio personal','Pagina web en Astro a modo de CV o porfolio que consume los datos de proyectos de una API personal. ','2024-11-11','2024-12-01','https://github.com/AleNaGa/portfolioFront','https://github.com/AleNaGa/portfolioFront','/projects/portfolio.png',1),(18,'Screen Recorder Extension','Crear una extensión de chrome para grabar pantalla con JS.','2024-12-01','2024-12-22','https://github.com/AleNaGa/extensionREC','https://github.com/AleNaGa/extensionREC','/projects/Screenrecorder.png',1);
INSERT INTO `developers_worked_on_projects` VALUES (5,9),(5,13),(5,14),(5,15),(5,16),(5,17),(5,18);
INSERT INTO `technologies_used_in_projects` VALUES (2,9),(12,9),(2,13),(13,13),(17,13),(13,14),(15,14),(18,14),(2,15),(12,16),(19,16),(12,17),(15,17),(19,17),(20,17),(21,17),(12,18),(19,18);
