-- Tabela de controle de estado da importação
CREATE TABLE tb_controle_importacao (
    id BIGINT PRIMARY KEY,
    ultima_pagina_processada INT NOT NULL DEFAULT 0,
    status VARCHAR(50) NOT NULL DEFAULT 'EM_ANDAMENTO',
    total_jogos_importados INT DEFAULT 0,
    data_ultima_execucao TIMESTAMP,
    mensagem_erro TEXT
);

-- Insere o registro inicial de controle único (ID 1)
INSERT INTO tb_controle_importacao (id, ultima_pagina_processada, status, total_jogos_importados)
VALUES (1, 0, 'EM_ANDAMENTO', 0)
ON CONFLICT (id) DO NOTHING;

-- Tabela de log de erros de jogos individuais
CREATE TABLE tb_jogo_erro (
    id BIGSERIAL PRIMARY KEY,
    rawg_id BIGINT,
    titulo VARCHAR(255),
    numero_pagina INT,
    mensagem_erro TEXT NOT NULL,
    data_erro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);