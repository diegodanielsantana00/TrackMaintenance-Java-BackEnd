ALTER TABLE viagens     DROP CONSTRAINT IF EXISTS viagens_veiculo_id_fkey;
ALTER TABLE manutencoes DROP CONSTRAINT IF EXISTS manutencoes_veiculo_id_fkey;

ALTER TABLE veiculos    ALTER COLUMN id         TYPE BIGINT USING id::bigint;
ALTER TABLE viagens     ALTER COLUMN id         TYPE BIGINT USING id::bigint;
ALTER TABLE manutencoes ALTER COLUMN id         TYPE BIGINT USING id::bigint;

ALTER TABLE viagens     ALTER COLUMN veiculo_id TYPE BIGINT USING veiculo_id::bigint;
ALTER TABLE manutencoes ALTER COLUMN veiculo_id TYPE BIGINT USING veiculo_id::bigint;

ALTER TABLE viagens
    ADD CONSTRAINT viagens_veiculo_id_fkey
    FOREIGN KEY (veiculo_id) REFERENCES veiculos(id) ON DELETE CASCADE;

ALTER TABLE manutencoes
    ADD CONSTRAINT manutencoes_veiculo_id_fkey
    FOREIGN KEY (veiculo_id) REFERENCES veiculos(id) ON DELETE CASCADE;
