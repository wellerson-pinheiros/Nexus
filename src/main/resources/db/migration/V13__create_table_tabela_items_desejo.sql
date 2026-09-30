CREATE TABLE tb_lista_item_desejo (
    lista_item_desejo_id BIGSERIAL PRIMARY KEY,
    lista_desejos_id BIGINT NOT NULL,
    jogo_id BIGINT NOT NULL,
    data_adicao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    prioridade VARCHAR(50),

    CONSTRAINT fk_item_desejo_lista
        FOREIGN KEY (lista_desejos_id)
        REFERENCES tb_lista_desejos(lista_desejos_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_item_desejo_jogo
        FOREIGN KEY (jogo_id)
        REFERENCES tb_jogo(jogo_id)
        ON DELETE CASCADE,

    -- (Opcional, mas muito recomendado): Evita que o mesmo jogo seja adicionado mais de uma vez na mesma lista
    CONSTRAINT uk_lista_jogo UNIQUE (lista_desejos_id, jogo_id)
);