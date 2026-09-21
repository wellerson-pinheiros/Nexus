CREATE TABLE tb_jogo_genero (
    jogo_id BIGINT NOT NULL,
    genero_id BIGINT NOT NULL,

    PRIMARY KEY (jogo_id, genero_id),

    CONSTRAINT fk_jogo_genero_jogo
        FOREIGN KEY (jogo_id) REFERENCES tb_jogo(jogo_id) ON DELETE CASCADE,

    CONSTRAINT fk_jogo_genero_genero
        FOREIGN KEY (genero_id) REFERENCES tb_genero(genero_id) ON DELETE CASCADE
);