/*GRUPO 7
Matheus Alves Abreu
Thiago dos Santos Lima
Wanderson Mateus Borges da Silva*/

CREATE DATABASE banco_malvader;
USE banco_malvader;

CREATE TABLE usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(11) UNIQUE NOT NULL,
    data_nascimento DATE NOT NULL,
    telefone VARCHAR(15) NOT NULL,
    tipo_usuario ENUM('FUNCIONARIO', 'CLIENTE') NOT NULL,
    senha_hash VARCHAR(32) NOT NULL -- Aqui vamos simular hash MD5
);

CREATE TABLE cliente (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    score_credito DECIMAL(5,2) DEFAULT 0,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

CREATE TABLE conta (
    id_conta INT AUTO_INCREMENT PRIMARY KEY,
    numero_conta VARCHAR(20) UNIQUE NOT NULL,
    saldo DECIMAL(15,2) DEFAULT 0,
    tipo_conta ENUM('POUPANCA', 'CORRENTE', 'INVESTIMENTO') NOT NULL,
    id_cliente INT NOT NULL,
    data_abertura DATETIME DEFAULT CURRENT_TIMESTAMP,
    status ENUM('ATIVA', 'ENCERRADA', 'BLOQUEADA') DEFAULT 'ATIVA',
    FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente)
);

-- Tabela de Funcionário
CREATE TABLE funcionario (
    id_funcionario INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    codigo_funcionario VARCHAR(20) UNIQUE NOT NULL,
    cargo ENUM('ESTAGIARIO', 'ATENDENTE', 'GERENTE') NOT NULL,
    id_supervisor INT, -- pode ser nulo no caso do gerente
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
    FOREIGN KEY (id_supervisor) REFERENCES funcionario(id_funcionario)
);

-- Tabela de Endereço
CREATE TABLE endereco (
    id_endereco INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    cep VARCHAR(10) NOT NULL,
    local VARCHAR(100) NOT NULL,
    numero_casa INT NOT NULL,
    bairro VARCHAR(50) NOT NULL,
    cidade VARCHAR(50) NOT NULL,
    estado CHAR(2) NOT NULL,
    complemento VARCHAR(50),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

-- Tabela de Agência
CREATE TABLE agencia (
    id_agencia INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    codigo_agencia VARCHAR(10) UNIQUE NOT NULL,
    endereco_id INT NOT NULL,
    FOREIGN KEY (endereco_id) REFERENCES endereco(id_endereco)
);

-- Conta Poupança
CREATE TABLE conta_poupanca (
    id_conta_poupanca INT AUTO_INCREMENT PRIMARY KEY,
    id_conta INT UNIQUE NOT NULL,
    taxa_rendimento DECIMAL(5,2) NOT NULL,
    ultimo_rendimento DATETIME,
    FOREIGN KEY (id_conta) REFERENCES conta(id_conta)
);

-- Conta Corrente
CREATE TABLE conta_corrente (
    id_conta_corrente INT AUTO_INCREMENT PRIMARY KEY,
    id_conta INT UNIQUE NOT NULL,
    limite DECIMAL(15,2) NOT NULL DEFAULT 0,
    data_vencimento DATE NOT NULL,
    taxa_manutencao DECIMAL(5,2) NOT NULL DEFAULT 0,
    FOREIGN KEY (id_conta) REFERENCES conta(id_conta)
);

-- Conta Investimento
CREATE TABLE conta_investimento (
    id_conta_investimento INT AUTO_INCREMENT PRIMARY KEY,
    id_conta INT UNIQUE NOT NULL,
    perfil_risco ENUM('BAIXO', 'MEDIO', 'ALTO') NOT NULL,
    valor_minimo DECIMAL(15,2) NOT NULL,
    taxa_rendimento_base DECIMAL(5,2) NOT NULL,
    FOREIGN KEY (id_conta) REFERENCES conta(id_conta)
);

CREATE TABLE transacao (
    id_transacao INT AUTO_INCREMENT PRIMARY KEY,
    id_conta_origem INT NOT NULL,
    id_conta_destino INT, -- pode ser NULL no caso de depósito ou saque
    tipo_transacao ENUM('DEPOSITO', 'SAQUE', 'TRANSFERENCIA', 'TAXA', 'RENDIMENTO') NOT NULL,
    valor DECIMAL(15,2) NOT NULL,
    data_hora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    descricao VARCHAR(100),
    FOREIGN KEY (id_conta_origem) REFERENCES conta(id_conta),
    FOREIGN KEY (id_conta_destino) REFERENCES conta(id_conta)
);

CREATE TABLE auditoria (
    id_auditoria INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    acao VARCHAR(50) NOT NULL,
    data_hora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    detalhes TEXT,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

CREATE TABLE relatorio (
    id_relatorio INT AUTO_INCREMENT PRIMARY KEY,
    id_funcionario INT NOT NULL,
    tipo_relatorio VARCHAR(50) NOT NULL,
    data_geracao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    conteudo TEXT NOT NULL, -- pode ser JSON ou CSV
    FOREIGN KEY (id_funcionario) REFERENCES funcionario(id_funcionario)
);


CREATE TABLE historico_encerramento (
    id_encerramento INT AUTO_INCREMENT PRIMARY KEY,
    id_conta INT NOT NULL,
    motivo TEXT,
    data_encerramento DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_conta) REFERENCES conta(id_conta)
);

ALTER TABLE usuario
ADD COLUMN otp_ativo VARCHAR(6),
ADD COLUMN otp_expiracao DATETIME;





/* Atualização de saldo */
DELIMITER $$

CREATE TRIGGER atualizar_saldo AFTER INSERT ON transacao
FOR EACH ROW
BEGIN
    IF NEW.tipo_transacao = 'DEPOSITO' THEN
        UPDATE conta SET saldo = saldo + NEW.valor WHERE id_conta = NEW.id_conta_origem;

    ELSEIF NEW.tipo_transacao IN ('SAQUE', 'TAXA') THEN
        UPDATE conta SET saldo = saldo - NEW.valor WHERE id_conta = NEW.id_conta_origem;

    ELSEIF NEW.tipo_transacao = 'TRANSFERENCIA' THEN
        UPDATE conta SET saldo = saldo - NEW.valor WHERE id_conta = NEW.id_conta_origem;
        UPDATE conta SET saldo = saldo + NEW.valor WHERE id_conta = NEW.id_conta_destino;
    END IF;
END $$

DELIMITER ;

/*Senha forte*/
DROP TRIGGER IF EXISTS validar_senha;

DELIMITER $$

CREATE TRIGGER validar_senha BEFORE UPDATE ON usuario
FOR EACH ROW
BEGIN
    IF NEW.senha_hash <> OLD.senha_hash THEN
        IF NEW.senha_hash REGEXP '^[0-9a-f]{32}$' THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Senha deve ser atualizada via procedure com validação';
        END IF;
    END IF;
END$$

DELIMITER ;


/* Limite de depósito diário */
DELIMITER $$

CREATE TRIGGER limite_deposito BEFORE INSERT ON transacao
FOR EACH ROW
BEGIN
    DECLARE total_dia DECIMAL(15,2);

    SELECT SUM(valor) INTO total_dia
    FROM transacao
    WHERE id_conta_origem = NEW.id_conta_origem
      AND tipo_transacao = 'DEPOSITO'
      AND DATE(data_hora) = DATE(NEW.data_hora);

    IF (total_dia + NEW.valor) > 500000 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Limite diário de depósito excedido';
    END IF;
END $$

DELIMITER ;

DROP TRIGGER IF EXISTS limite_deposito;



/*GERAR OTP*/
DELIMITER $$

CREATE PROCEDURE gerar_otp(IN p_id_usuario INT)
BEGIN
    DECLARE novo_otp VARCHAR(6);
    
    SET novo_otp = LPAD(FLOOR(RAND() * 1000000), 6, '0'); -- número aleatório de 6 dígitos
    
    UPDATE usuario
    SET otp_ativo = novo_otp,
        otp_expiracao = NOW() + INTERVAL 5 MINUTE
    WHERE id_usuario = p_id_usuario;

    SELECT novo_otp AS otp_gerado;
END $$

DELIMITER ;

/*Calcular score*/
DELIMITER $$

CREATE PROCEDURE calcular_score_credito(IN p_id_cliente INT)
BEGIN
    DECLARE total_trans DECIMAL(15,2);
    DECLARE media_trans DECIMAL(15,2);

    SELECT 
        IFNULL(SUM(t.valor), 0),
        IFNULL(AVG(t.valor), 0)
    INTO total_trans, media_trans
    FROM transacao t
    JOIN conta c ON t.id_conta_origem = c.id_conta
    WHERE c.id_cliente = p_id_cliente
      AND t.tipo_transacao IN ('DEPOSITO', 'SAQUE');

    UPDATE cliente
    SET score_credito = LEAST(100, (total_trans / 1000) + (media_trans / 100))
    WHERE id_cliente = p_id_cliente;
END $$

DELIMITER ;


DELIMITER $$

CREATE PROCEDURE encerrar_conta(IN p_id_conta INT, IN p_motivo TEXT)
BEGIN
    DECLARE v_saldo DECIMAL(15,2);

    -- Verificar saldo
    SELECT saldo INTO v_saldo
    FROM conta
    WHERE id_conta = p_id_conta;

    IF v_saldo > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Conta com saldo positivo. Não é possível encerrar.';
    ELSEIF v_saldo < 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Conta com saldo negativo. Não é possível encerrar.';
    ELSE
        -- Atualizar status da conta
        UPDATE conta
        SET status = 'ENCERRADA'
        WHERE id_conta = p_id_conta;

        -- Registrar o motivo do encerramento
        INSERT INTO historico_encerramento (id_conta, motivo, data_encerramento)
        VALUES (p_id_conta, p_motivo, NOW());
    END IF;
END$$

DELIMITER ;






/*VIEWS*/
/*Resumo de contas*/
CREATE VIEW vw_resumo_contas AS
SELECT 
    c.id_cliente,
    u.nome,
    COUNT(co.id_conta) AS total_contas,
    SUM(co.saldo) AS saldo_total
FROM cliente c
JOIN usuario u ON c.id_usuario = u.id_usuario
JOIN conta co ON c.id_cliente = co.id_cliente
GROUP BY c.id_cliente, u.nome;

/*Movimentação rescente*/
CREATE VIEW vw_movimentacoes_recentes AS
SELECT 
    t.*,
    co.numero_conta,
    u.nome AS cliente
FROM transacao t
JOIN conta co ON t.id_conta_origem = co.id_conta
JOIN cliente cl ON co.id_cliente = cl.id_cliente
JOIN usuario u ON cl.id_usuario = u.id_usuario
WHERE t.data_hora >= NOW() - INTERVAL 90 DAY;



/* inserir */

INSERT INTO usuario (nome, cpf, data_nascimento, telefone, tipo_usuario, senha_hash)
VALUES (
    'João da Silva',
    '12345678900',
    '1995-04-20',
    '(61) 99999-8888',
    'CLIENTE',
    'e10adc3949ba59abbe56e057f20f883e' -- senha: 123456 (em MD5)
);

-- 1. Inserir a Maria na tabela usuario
INSERT INTO usuario (nome, cpf, data_nascimento, telefone, tipo_usuario, senha_hash)
VALUES (
    'Matheus Alves Abreu',
    '',
    '1997-09-03',
    '(61) 98888-7777',
    'FUNCIONARIO',
    'e10adc3949ba59abbe56e057f20f883e' -- senha: 123456 (MD5)
);

-- 2. Inserir a Maria na tabela funcionario
INSERT INTO funcionario (id_usuario, codigo_funcionario, cargo)
VALUES (
    (SELECT id_usuario FROM usuario WHERE cpf = '98765432100'),
    'FUNC002',
    'GERENTE'
);

INSERT INTO usuario (nome, cpf, data_nascimento, telefone, tipo_usuario, senha_hash)
VALUES (
    'Sistema Agencia',
    '00000000000',
    '1900-01-01',
    '(00) 00000-0000',
    'FUNCIONARIO',
    'd41d8cd98f00b204e9800998ecf8427e' -- hash de senha vazia
);

-- Endereço da agência, vinculado ao usuário fictício
INSERT INTO endereco (id_usuario, cep, local, numero_casa, bairro, cidade, estado)
VALUES (
    (SELECT id_usuario FROM usuario WHERE cpf = '00000000000'),
    '70000-000', 'Av. Central', 123, 'Centro', 'Brasília', 'DF'
);

-- Inserir a agência
INSERT INTO agencia (nome, codigo_agencia, endereco_id)
VALUES ('Agência Central', '001', LAST_INSERT_ID());


ALTER TABLE conta
ADD COLUMN id_agencia INT NOT NULL,
ADD FOREIGN KEY (id_agencia) REFERENCES agencia(id_agencia);



SELECT co.*
FROM conta co
JOIN cliente cl ON co.id_cliente = cl.id_cliente
;

