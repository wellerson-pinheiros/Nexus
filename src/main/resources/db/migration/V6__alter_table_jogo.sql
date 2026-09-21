ALTER TABLE tb_jogo
    DROP COLUMN IF EXISTS requisitos_minimos,
    DROP COLUMN IF EXISTS requisitos_recomendados;

-- 2. Adiciona os novos campos da RAWG na tabela tb_jogo
ALTER TABLE tb_jogo
    ADD COLUMN IF NOT EXISTS rating_top DOUBLE PRECISION,
    ADD COLUMN IF NOT EXISTS ratings_count INT,
    ADD COLUMN IF NOT EXISTS metacritic INT,
    ADD COLUMN IF NOT EXISTS playtime INT,
    ADD COLUMN IF NOT EXISTS esrb_rating VARCHAR(100);