CREATE TABLE tb_lista_item_biblioteca (
    id BIGSERIAL PRIMARY KEY, -- Ou lista_item_biblioteca_id, combinando com o nome do atributo na sua entidade
    biblioteca_id BIGINT NOT NULL,
    jogo_id BIGINT NOT NULL,
    data_compra TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_item_biblioteca_biblio
        FOREIGN KEY (biblioteca_id)
        REFERENCES tb_biblioteca(biblioteca_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_item_biblioteca_jogo
        FOREIGN KEY (jogo_id)
        REFERENCES tb_jogo(jogo_id)
        ON DELETE CASCADE,

    -- (Opcional, mas recomendado) Garante que o usuário não tenha o mesmo jogo duplicado na biblioteca
    CONSTRAINT uk_biblioteca_jogo UNIQUE (biblioteca_id, jogo_id));