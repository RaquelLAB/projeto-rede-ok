DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'tipo_documento_enum') THEN
        CREATE TYPE tipo_documento_enum AS ENUM ('CPF', 'CNPJ');
    END IF;
END $$;

CREATE TABLE IF NOT EXISTS cliente (
    id_cliente SERIAL PRIMARY KEY,
    nome VARCHAR(100),
    telefone VARCHAR(11),
    email VARCHAR(200) NOT NULL,
    num_documento VARCHAR(14) NOT NULL,
    tipo_documento tipo_documento_enum NOT NULL,
    data_criacao DATE DEFAULT CURRENT_DATE,
    CONSTRAINT email_valido CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')
 );

CREATE TABLE IF NOT EXISTS endereco (
    id_endereco SERIAL PRIMARY KEY,
    cliente INT NOT NULL,
    CEP VARCHAR(10) NOT NULL,
    UF VARCHAR(2) NOT NULL,
    cidade VARCHAR(50) NOT NULL,
    logradouro VARCHAR(100) NOT NULL,
    numero VARCHAR(10) NOT NULL,

    FOREIGN KEY (cliente) REFERENCES cliente(id_cliente)
);

