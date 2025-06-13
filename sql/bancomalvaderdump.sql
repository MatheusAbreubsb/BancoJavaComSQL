-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: banco_malvader
-- ------------------------------------------------------
-- Server version	9.2.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `agencia`
--

DROP TABLE IF EXISTS `agencia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `agencia` (
  `id_agencia` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) NOT NULL,
  `codigo_agencia` varchar(10) NOT NULL,
  `endereco_id` int NOT NULL,
  PRIMARY KEY (`id_agencia`),
  UNIQUE KEY `codigo_agencia` (`codigo_agencia`),
  KEY `endereco_id` (`endereco_id`),
  CONSTRAINT `agencia_ibfk_1` FOREIGN KEY (`endereco_id`) REFERENCES `endereco` (`id_endereco`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `agencia`
--

LOCK TABLES `agencia` WRITE;
/*!40000 ALTER TABLE `agencia` DISABLE KEYS */;
INSERT INTO `agencia` VALUES (1,'Agência Central','001',1);
/*!40000 ALTER TABLE `agencia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auditoria`
--

DROP TABLE IF EXISTS `auditoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `auditoria` (
  `id_auditoria` int NOT NULL AUTO_INCREMENT,
  `id_usuario` int NOT NULL,
  `acao` varchar(50) NOT NULL,
  `data_hora` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `detalhes` text,
  PRIMARY KEY (`id_auditoria`),
  KEY `id_usuario` (`id_usuario`),
  CONSTRAINT `auditoria_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auditoria`
--

LOCK TABLES `auditoria` WRITE;
/*!40000 ALTER TABLE `auditoria` DISABLE KEYS */;
/*!40000 ALTER TABLE `auditoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cliente`
--

DROP TABLE IF EXISTS `cliente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cliente` (
  `id_cliente` int NOT NULL AUTO_INCREMENT,
  `id_usuario` int NOT NULL,
  `score_credito` decimal(5,2) DEFAULT '0.00',
  PRIMARY KEY (`id_cliente`),
  KEY `id_usuario` (`id_usuario`),
  CONSTRAINT `cliente_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cliente`
--

LOCK TABLES `cliente` WRITE;
/*!40000 ALTER TABLE `cliente` DISABLE KEYS */;
INSERT INTO `cliente` VALUES (1,3,0.00),(2,6,42.35),(3,7,0.00),(4,8,0.00),(5,12,0.00);
/*!40000 ALTER TABLE `cliente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `conta`
--

DROP TABLE IF EXISTS `conta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `conta` (
  `id_conta` int NOT NULL AUTO_INCREMENT,
  `numero_conta` varchar(20) NOT NULL,
  `saldo` decimal(15,2) DEFAULT '0.00',
  `tipo_conta` enum('POUPANCA','CORRENTE','INVESTIMENTO') NOT NULL,
  `id_cliente` int NOT NULL,
  `data_abertura` datetime DEFAULT CURRENT_TIMESTAMP,
  `status` enum('ATIVA','ENCERRADA','BLOQUEADA') DEFAULT 'ATIVA',
  `id_agencia` int NOT NULL,
  PRIMARY KEY (`id_conta`),
  UNIQUE KEY `numero_conta` (`numero_conta`),
  KEY `id_cliente` (`id_cliente`),
  KEY `id_agencia` (`id_agencia`),
  CONSTRAINT `conta_ibfk_1` FOREIGN KEY (`id_cliente`) REFERENCES `cliente` (`id_cliente`),
  CONSTRAINT `conta_ibfk_2` FOREIGN KEY (`id_agencia`) REFERENCES `agencia` (`id_agencia`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `conta`
--

LOCK TABLES `conta` WRITE;
/*!40000 ALTER TABLE `conta` DISABLE KEYS */;
INSERT INTO `conta` VALUES (1,'ACC388821',13050.00,'POUPANCA',1,'2025-05-31 19:43:08','ATIVA',1),(2,'ACC567337',19750.00,'POUPANCA',2,'2025-05-31 23:22:47','ATIVA',1),(3,'ACC486666',0.00,'POUPANCA',3,'2025-06-04 22:04:46','ENCERRADA',1),(4,'ACC114945',0.00,'INVESTIMENTO',4,'2025-06-04 22:15:14','ENCERRADA',1),(5,'ACC163941',0.00,'POUPANCA',5,'2025-06-12 19:42:43','ATIVA',1);
/*!40000 ALTER TABLE `conta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `conta_corrente`
--

DROP TABLE IF EXISTS `conta_corrente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `conta_corrente` (
  `id_conta_corrente` int NOT NULL AUTO_INCREMENT,
  `id_conta` int NOT NULL,
  `limite` decimal(15,2) NOT NULL DEFAULT '0.00',
  `data_vencimento` date NOT NULL,
  `taxa_manutencao` decimal(5,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`id_conta_corrente`),
  UNIQUE KEY `id_conta` (`id_conta`),
  CONSTRAINT `conta_corrente_ibfk_1` FOREIGN KEY (`id_conta`) REFERENCES `conta` (`id_conta`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `conta_corrente`
--

LOCK TABLES `conta_corrente` WRITE;
/*!40000 ALTER TABLE `conta_corrente` DISABLE KEYS */;
/*!40000 ALTER TABLE `conta_corrente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `conta_investimento`
--

DROP TABLE IF EXISTS `conta_investimento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `conta_investimento` (
  `id_conta_investimento` int NOT NULL AUTO_INCREMENT,
  `id_conta` int NOT NULL,
  `perfil_risco` enum('BAIXO','MEDIO','ALTO') NOT NULL,
  `valor_minimo` decimal(15,2) NOT NULL,
  `taxa_rendimento_base` decimal(5,2) NOT NULL,
  PRIMARY KEY (`id_conta_investimento`),
  UNIQUE KEY `id_conta` (`id_conta`),
  CONSTRAINT `conta_investimento_ibfk_1` FOREIGN KEY (`id_conta`) REFERENCES `conta` (`id_conta`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `conta_investimento`
--

LOCK TABLES `conta_investimento` WRITE;
/*!40000 ALTER TABLE `conta_investimento` DISABLE KEYS */;
INSERT INTO `conta_investimento` VALUES (1,4,'MEDIO',1000.00,0.09);
/*!40000 ALTER TABLE `conta_investimento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `conta_poupanca`
--

DROP TABLE IF EXISTS `conta_poupanca`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `conta_poupanca` (
  `id_conta_poupanca` int NOT NULL AUTO_INCREMENT,
  `id_conta` int NOT NULL,
  `taxa_rendimento` decimal(5,2) NOT NULL,
  `ultimo_rendimento` datetime DEFAULT NULL,
  PRIMARY KEY (`id_conta_poupanca`),
  UNIQUE KEY `id_conta` (`id_conta`),
  CONSTRAINT `conta_poupanca_ibfk_1` FOREIGN KEY (`id_conta`) REFERENCES `conta` (`id_conta`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `conta_poupanca`
--

LOCK TABLES `conta_poupanca` WRITE;
/*!40000 ALTER TABLE `conta_poupanca` DISABLE KEYS */;
INSERT INTO `conta_poupanca` VALUES (1,1,0.50,NULL),(2,2,0.60,NULL),(3,3,0.40,NULL),(4,5,0.30,NULL);
/*!40000 ALTER TABLE `conta_poupanca` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `endereco`
--

DROP TABLE IF EXISTS `endereco`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `endereco` (
  `id_endereco` int NOT NULL AUTO_INCREMENT,
  `id_usuario` int NOT NULL,
  `cep` varchar(10) NOT NULL,
  `local` varchar(100) NOT NULL,
  `numero_casa` int NOT NULL,
  `bairro` varchar(50) NOT NULL,
  `cidade` varchar(50) NOT NULL,
  `estado` char(2) NOT NULL,
  `complemento` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id_endereco`),
  KEY `id_usuario` (`id_usuario`),
  CONSTRAINT `endereco_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `endereco`
--

LOCK TABLES `endereco` WRITE;
/*!40000 ALTER TABLE `endereco` DISABLE KEYS */;
INSERT INTO `endereco` VALUES (1,4,'70000-000','Av. Central',123,'Centro','Brasília','DF',NULL);
/*!40000 ALTER TABLE `endereco` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `funcionario`
--

DROP TABLE IF EXISTS `funcionario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `funcionario` (
  `id_funcionario` int NOT NULL AUTO_INCREMENT,
  `id_usuario` int NOT NULL,
  `codigo_funcionario` varchar(20) NOT NULL,
  `cargo` enum('ESTAGIARIO','ATENDENTE','GERENTE') NOT NULL,
  `id_supervisor` int DEFAULT NULL,
  PRIMARY KEY (`id_funcionario`),
  UNIQUE KEY `codigo_funcionario` (`codigo_funcionario`),
  KEY `id_usuario` (`id_usuario`),
  KEY `id_supervisor` (`id_supervisor`),
  CONSTRAINT `funcionario_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`),
  CONSTRAINT `funcionario_ibfk_2` FOREIGN KEY (`id_supervisor`) REFERENCES `funcionario` (`id_funcionario`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `funcionario`
--

LOCK TABLES `funcionario` WRITE;
/*!40000 ALTER TABLE `funcionario` DISABLE KEYS */;
INSERT INTO `funcionario` VALUES (1,2,'FUNC002','GERENTE',NULL);
/*!40000 ALTER TABLE `funcionario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `historico_encerramento`
--

DROP TABLE IF EXISTS `historico_encerramento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `historico_encerramento` (
  `id_encerramento` int NOT NULL AUTO_INCREMENT,
  `id_conta` int NOT NULL,
  `motivo` text,
  `data_encerramento` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_encerramento`),
  KEY `id_conta` (`id_conta`),
  CONSTRAINT `historico_encerramento_ibfk_1` FOREIGN KEY (`id_conta`) REFERENCES `conta` (`id_conta`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `historico_encerramento`
--

LOCK TABLES `historico_encerramento` WRITE;
/*!40000 ALTER TABLE `historico_encerramento` DISABLE KEYS */;
INSERT INTO `historico_encerramento` VALUES (1,3,'teste','2025-06-04 22:06:27'),(2,4,'Teste','2025-06-06 20:31:21');
/*!40000 ALTER TABLE `historico_encerramento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `relatorio`
--

DROP TABLE IF EXISTS `relatorio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `relatorio` (
  `id_relatorio` int NOT NULL AUTO_INCREMENT,
  `id_funcionario` int NOT NULL,
  `tipo_relatorio` varchar(50) NOT NULL,
  `data_geracao` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `conteudo` text NOT NULL,
  PRIMARY KEY (`id_relatorio`),
  KEY `id_funcionario` (`id_funcionario`),
  CONSTRAINT `relatorio_ibfk_1` FOREIGN KEY (`id_funcionario`) REFERENCES `funcionario` (`id_funcionario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `relatorio`
--

LOCK TABLES `relatorio` WRITE;
/*!40000 ALTER TABLE `relatorio` DISABLE KEYS */;
/*!40000 ALTER TABLE `relatorio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transacao`
--

DROP TABLE IF EXISTS `transacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transacao` (
  `id_transacao` int NOT NULL AUTO_INCREMENT,
  `id_conta_origem` int NOT NULL,
  `id_conta_destino` int DEFAULT NULL,
  `tipo_transacao` enum('DEPOSITO','SAQUE','TRANSFERENCIA','TAXA','RENDIMENTO') NOT NULL,
  `valor` decimal(15,2) NOT NULL,
  `data_hora` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `descricao` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id_transacao`),
  KEY `id_conta_origem` (`id_conta_origem`),
  KEY `id_conta_destino` (`id_conta_destino`),
  CONSTRAINT `transacao_ibfk_1` FOREIGN KEY (`id_conta_origem`) REFERENCES `conta` (`id_conta`),
  CONSTRAINT `transacao_ibfk_2` FOREIGN KEY (`id_conta_destino`) REFERENCES `conta` (`id_conta`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transacao`
--

LOCK TABLES `transacao` WRITE;
/*!40000 ALTER TABLE `transacao` DISABLE KEYS */;
INSERT INTO `transacao` VALUES (1,2,NULL,'DEPOSITO',10000.00,'2025-06-01 02:24:09',''),(2,2,1,'TRANSFERENCIA',10.00,'2025-06-01 02:36:18',''),(3,2,NULL,'SAQUE',100.00,'2025-06-01 02:41:28',''),(4,2,NULL,'DEPOSITO',1000.00,'2025-06-12 19:44:27',''),(5,2,NULL,'DEPOSITO',1000.00,'2025-06-12 20:01:13','');
/*!40000 ALTER TABLE `transacao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `id_usuario` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `cpf` varchar(11) NOT NULL,
  `data_nascimento` date NOT NULL,
  `telefone` varchar(15) NOT NULL,
  `tipo_usuario` enum('FUNCIONARIO','CLIENTE') NOT NULL,
  `senha_hash` varchar(32) NOT NULL,
  `otp_ativo` varchar(6) DEFAULT NULL,
  `otp_expiracao` datetime DEFAULT NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `cpf` (`cpf`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (1,'João da Silva','12345678900','1995-04-20','(61) 99999-8888','CLIENTE','e10adc3949ba59abbe56e057f20f883e',NULL,NULL),(2,'Maria de Sousa','98765432100','1988-03-10','(61) 98888-7777','FUNCIONARIO','e10adc3949ba59abbe56e057f20f883e',NULL,NULL),(3,'Ana Costa','11122233344','1997-03-03','(61) 98888-1111','CLIENTE','e10adc3949ba59abbe56e057f20f883e',NULL,NULL),(4,'Sistema Agencia','00000000000','1900-01-01','(00) 00000-0000','FUNCIONARIO','d41d8cd98f00b204e9800998ecf8427e',NULL,NULL),(5,'Matheus Alves Abreu','06071867126','1997-09-03','(61) 98888-7777','FUNCIONARIO','e10adc3949ba59abbe56e057f20f883e','961054','2025-06-12 19:48:03'),(6,'Marcia Lucia','11111111122','1970-04-14','(61) 984129412','CLIENTE','e10adc3949ba59abbe56e057f20f883e',NULL,NULL),(7,'Quianna Martinelle','22222222233','1970-03-05','(61)98888-4444','CLIENTE','e10adc3949ba59abbe56e057f20f883e',NULL,NULL),(8,'Wilton Nicolau','33333333344','1994-06-12','(61)98444-7777','CLIENTE','e10adc3949ba59abbe56e057f20f883e',NULL,NULL),(12,'Willian','11111111133','1950-02-03','6184411111','CLIENTE','e10adc3949ba59abbe56e057f20f883e',NULL,NULL);
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `vw_movimentacoes_recentes`
--

DROP TABLE IF EXISTS `vw_movimentacoes_recentes`;
/*!50001 DROP VIEW IF EXISTS `vw_movimentacoes_recentes`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `vw_movimentacoes_recentes` AS SELECT 
 1 AS `id_transacao`,
 1 AS `id_conta_origem`,
 1 AS `id_conta_destino`,
 1 AS `tipo_transacao`,
 1 AS `valor`,
 1 AS `data_hora`,
 1 AS `descricao`,
 1 AS `numero_conta`,
 1 AS `cliente`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `vw_resumo_contas`
--

DROP TABLE IF EXISTS `vw_resumo_contas`;
/*!50001 DROP VIEW IF EXISTS `vw_resumo_contas`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `vw_resumo_contas` AS SELECT 
 1 AS `id_cliente`,
 1 AS `nome`,
 1 AS `total_contas`,
 1 AS `saldo_total`*/;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `vw_movimentacoes_recentes`
--

/*!50001 DROP VIEW IF EXISTS `vw_movimentacoes_recentes`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `vw_movimentacoes_recentes` AS select `t`.`id_transacao` AS `id_transacao`,`t`.`id_conta_origem` AS `id_conta_origem`,`t`.`id_conta_destino` AS `id_conta_destino`,`t`.`tipo_transacao` AS `tipo_transacao`,`t`.`valor` AS `valor`,`t`.`data_hora` AS `data_hora`,`t`.`descricao` AS `descricao`,`co`.`numero_conta` AS `numero_conta`,`u`.`nome` AS `cliente` from (((`transacao` `t` join `conta` `co` on((`t`.`id_conta_origem` = `co`.`id_conta`))) join `cliente` `cl` on((`co`.`id_cliente` = `cl`.`id_cliente`))) join `usuario` `u` on((`cl`.`id_usuario` = `u`.`id_usuario`))) where (`t`.`data_hora` >= (now() - interval 90 day)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `vw_resumo_contas`
--

/*!50001 DROP VIEW IF EXISTS `vw_resumo_contas`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `vw_resumo_contas` AS select `c`.`id_cliente` AS `id_cliente`,`u`.`nome` AS `nome`,count(`co`.`id_conta`) AS `total_contas`,sum(`co`.`saldo`) AS `saldo_total` from ((`cliente` `c` join `usuario` `u` on((`c`.`id_usuario` = `u`.`id_usuario`))) join `conta` `co` on((`c`.`id_cliente` = `co`.`id_cliente`))) group by `c`.`id_cliente`,`u`.`nome` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-13 15:19:04
