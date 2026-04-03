CREATE TABLE manutencoes (
    id               SERIAL PRIMARY KEY,
    veiculo_id       INTEGER REFERENCES veiculos(id) ON DELETE CASCADE,
    data_inicio      DATE    NOT NULL,
    data_finalizacao DATE,
    tipo_servico     VARCHAR(100),
    custo_estimado   DECIMAL(10,2),
    status           VARCHAR(20) DEFAULT 'PENDENTE'
);
