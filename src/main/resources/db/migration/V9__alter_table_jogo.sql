-- Altera o tipo da coluna data_lancamento para TIMESTAMP (compatível com LocalDateTime no Java)
ALTER TABLE tb_jogo
    ALTER COLUMN data_lancamento TYPE TIMESTAMP USING data_lancamento::timestamp;