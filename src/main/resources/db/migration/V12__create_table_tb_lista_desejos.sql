CREATE TABLE tb_lista_desejos (
    lista_desejos_id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,

    CONSTRAINT fk_lista_desejos_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES tb_usuario(usuario_id)
        ON DELETE CASCADE
);