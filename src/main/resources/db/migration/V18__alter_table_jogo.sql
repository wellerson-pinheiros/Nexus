-- 1. Remove a obrigatoriedade (NOT NULL) da descrição
ALTER TABLE tb_jogo
    ALTER COLUMN descricao DROP NOT NULL;

-- 2. Altera o tipo da data para DATE (compatível com LocalDate)
ALTER TABLE tb_jogo
    ALTER COLUMN data_lancamento TYPE DATE USING data_lancamento::date;