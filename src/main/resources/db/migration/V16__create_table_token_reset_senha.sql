CREATE TABLE tb_token_reset_senha (
    id SERIAL PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    data_expiracao TIMESTAMP NOT NULL,
    usuario_id BIGINT NOT NULL UNIQUE,
    CONSTRAINT fk_token_usuario FOREIGN KEY (usuario_id) REFERENCES tb_usuario(usuario_id)
);