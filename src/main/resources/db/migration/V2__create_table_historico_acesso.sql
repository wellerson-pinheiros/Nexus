CREATE TABLE tb_historico_de_acesso (
    historico_acesso_id BIGSERIAL PRIMARY KEY,
    data_hora_acesso TIMESTAMP,
    ip VARCHAR(255),
    dispositivo VARCHAR(255),
    localizacao_aproximada VARCHAR(255),
    navegador VARCHAR(255),
    sistema_operacional VARCHAR(255),
    cidade VARCHAR(255),
    estado VARCHAR(255),
    pais VARCHAR(255),
    usuario_id BIGINT,

    CONSTRAINT fk_historico_usuario
      FOREIGN KEY (usuario_id)
      REFERENCES tb_usuario(usuario_id)
      ON DELETE CASCADE
);