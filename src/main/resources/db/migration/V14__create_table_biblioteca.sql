CREATE TABLE tb_biblioteca (
    biblioteca_id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL UNIQUE, -- O UNIQUE garante que 1 usuário tenha apenas 1 biblioteca

    CONSTRAINT fk_biblioteca_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES tb_usuario(usuario_id)
        ON DELETE CASCADE
);