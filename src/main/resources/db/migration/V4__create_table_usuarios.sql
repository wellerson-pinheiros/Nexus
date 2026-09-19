CREATE TABLE tb_jogos (
    id_jogo BIGSERIAL PRIMARY KEY,
    rawg_id BIGINT UNIQUE,
    slug_raweg VARCHAR(255) UNIQUE,
    titulo VARCHAR(255) NOT NULL,
    descricao TEXT NOT NULL,
    imagem_capa VARCHAR(512),
    data_lancamento TIMESTAMP WITH TIME ZONE,
    rating DOUBLE PRECISION,
    requisitos_minimos TEXT,
    requisitos_recomendados TEXT
);