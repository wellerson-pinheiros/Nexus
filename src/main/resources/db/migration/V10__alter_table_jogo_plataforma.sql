-- 1. Remove a chave primária composta antiga
ALTER TABLE tb_jogo_plataforma DROP CONSTRAINT tb_jogo_plataforma_pkey;

-- 2. Adiciona a nova coluna 'id' como chave primária auto-incremento
ALTER TABLE tb_jogo_plataforma ADD COLUMN id BIGSERIAL PRIMARY KEY;

-- 3. Garante que a combinação de jogo e plataforma continue única (sem duplicadas)
ALTER TABLE tb_jogo_plataforma ADD CONSTRAINT uk_jogo_plataforma UNIQUE (jogo_id, plataforma_id);