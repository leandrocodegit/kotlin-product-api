CREATE TABLE product (
ID BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
NOME VARCHAR(40) NOT NULL,
QUANTIDADE INT NULL,
VALOR DOUBLE NOT NULL,
DESCRICAO VARCHAR(80) NOT NULL,
IS_ACTIVE TINYINT(1) NOT NULL
)