CREATE TABLE veiculos (
    id      SERIAL PRIMARY KEY,
    placa   VARCHAR(10)  UNIQUE NOT NULL,
    modelo  VARCHAR(50)  NOT NULL,
    tipo    VARCHAR(20)  CHECK (tipo IN ('LEVE', 'PESADO')),
    ano     INTEGER
);
