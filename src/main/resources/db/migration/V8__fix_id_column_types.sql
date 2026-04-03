-- Drop FK constraints before altering referenced primary key columns
ALTER TABLE viagens     DROP CONSTRAINT IF EXISTS viagens_veiculo_id_fkey;
ALTER TABLE manutencoes DROP CONSTRAINT IF EXISTS manutencoes_veiculo_id_fkey;

-- Alter primary key columns from INTEGER (SERIAL) to BIGINT
ALTER TABLE veiculos    ALTER COLUMN id         TYPE BIGINT USING id::bigint;
ALTER TABLE viagens     ALTER COLUMN id         TYPE BIGINT USING id::bigint;
ALTER TABLE manutencoes ALTER COLUMN id         TYPE BIGINT USING id::bigint;

-- Alter foreign key columns from INTEGER to BIGINT
ALTER TABLE viagens     ALTER COLUMN veiculo_id TYPE BIGINT USING veiculo_id::bigint;
ALTER TABLE manutencoes ALTER COLUMN veiculo_id TYPE BIGINT USING veiculo_id::bigint;

-- Re-add FK constraints
ALTER TABLE viagens
    ADD CONSTRAINT viagens_veiculo_id_fkey
    FOREIGN KEY (veiculo_id) REFERENCES veiculos(id) ON DELETE CASCADE;

ALTER TABLE manutencoes
    ADD CONSTRAINT manutencoes_veiculo_id_fkey
    FOREIGN KEY (veiculo_id) REFERENCES veiculos(id) ON DELETE CASCADE;
