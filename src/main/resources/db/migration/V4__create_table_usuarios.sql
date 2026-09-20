CREATE TABLE tb_genero (
    genero_id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    rawg_id BIGINT UNIQUE,
    slug VARCHAR(255) UNIQUE
);