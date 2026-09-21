CREATE TABLE tb_jogo_plataforma (
    jogo_id BIGINT NOT NULL,
    plataforma_id BIGINT NOT NULL,
    released_at DATE,
    requisitos_minimos TEXT,
    requisitos_recomendados TEXT,

    PRIMARY KEY (jogo_id, plataforma_id),

    CONSTRAINT fk_jogo_plataforma_jogo
        FOREIGN KEY (jogo_id) REFERENCES tb_jogo(jogo_id) ON DELETE CASCADE,

    CONSTRAINT fk_jogo_plataforma_plataforma
        FOREIGN KEY (plataforma_id) REFERENCES tb_plataforma(plataforma_id) ON DELETE CASCADE
);