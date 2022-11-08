LOCK TABLES `person` WRITE;
/*!40000 ALTER TABLE `person` DISABLE KEYS */;
INSERT INTO `person` VALUES (1,'Felipe','Rezende','Goiania','Male'),
                            (3,'Natalia','Rezende','Goiania','Female'),
                            (4,'Rafaela','Marques','Prata','Female');
/*!40000 ALTER TABLE `person` ENABLE KEYS */;
UNLOCK TABLES;