CREATE TABLE tb_plataforma (
    plataforma_id BIGSERIAL PRIMARY KEY,
    rawg_id BIGINT UNIQUE,
    nome VARCHAR(255) NOT NULL,
    slug VARCHAR(255) UNIQUE NOT NULL
);