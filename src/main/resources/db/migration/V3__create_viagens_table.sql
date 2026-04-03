CREATE TABLE viagens (
    id             SERIAL PRIMARY KEY,
    veiculo_id     INTEGER REFERENCES veiculos(id) ON DELETE CASCADE,
    data_saida     TIMESTAMP NOT NULL,
    data_chegada   TIMESTAMP,
    origem         VARCHAR(100),
    destino        VARCHAR(100),
    km_percorrida  DECIMAL(10,2)
);
