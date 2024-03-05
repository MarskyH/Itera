-- Tabela Acao
CREATE TABLE acao (
    id SERIAL PRIMARY KEY,
    titulo VARCHAR(30),
    descricao VARCHAR(255),
    tipo VARCHAR(20)
);

-- Tabela Projeto
CREATE TABLE projeto (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(50),
    prazo INTEGER,
    tempo_iteracao INTEGER,
    nome_cliente VARCHAR(50),
    equipe_id BIGINT
);

-- Tabela Equipe
CREATE TABLE equipe (
    id SERIAL PRIMARY KEY,
    projeto_id BIGINT
);

-- Tabela Papel
CREATE TABLE papel (
    id SERIAL PRIMARY KEY,
    funcao VARCHAR(30),
    habilidade VARCHAR(255),
    competencia VARCHAR(255),
    projeto_id BIGINT
);



-- Tabela Requisito
CREATE TABLE requisito (
    id SERIAL PRIMARY KEY,
    titulo VARCHAR(30),
    detalhamento VARCHAR(30),
    complexidade VARCHAR(30),
    prioridade VARCHAR(30),
    esforco INTEGER,
    tamanho INTEGER,
    projeto_id BIGINT
);

-- Tabela RequisitoNaoFuncional
CREATE TABLE requisitonaofuncional (
    id SERIAL PRIMARY KEY,
    titulo VARCHAR(30),
    valor INTEGER,
    projeto_id BIGINT
);

-- Tabela Risco
CREATE TABLE risco (
    id SERIAL PRIMARY KEY,
    titulo VARCHAR(30),
    efeito VARCHAR(255),
    probabilidade VARCHAR(20),
    impacto VARCHAR(20),
    grau_exposicao VARCHAR(20),
    acao_id BIGINT,
    projeto_id BIGINT
);

-- Tabela Users
CREATE TABLE users (
    id TEXT PRIMARY KEY NOT NULL,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    valorHora DOUBLE PRECISION,
    horasDedicada INTEGER,
    role VARCHAR(10) NOT NULL,
    equipe_id BIGINT
);

ALTER TABLE equipe
ADD CONSTRAINT FK_equipe_projeto
FOREIGN KEY (projeto_id) REFERENCES projeto(id);


ALTER TABLE papel
ADD CONSTRAINT FK_papel_projeto
FOREIGN KEY (projeto_id) REFERENCES projeto(id);

ALTER TABLE requisito
ADD CONSTRAINT FK_requisito_projeto
FOREIGN KEY (projeto_id) REFERENCES projeto(id);

ALTER TABLE requisitonaofuncional
ADD CONSTRAINT FK_requisitonaofuncional_projeto
FOREIGN KEY (projeto_id) REFERENCES projeto(id);

ALTER TABLE risco
ADD CONSTRAINT FK_risco_acao
FOREIGN KEY (acao_id) REFERENCES acao(id);

ALTER TABLE risco
ADD CONSTRAINT FK_risco_projeto
FOREIGN KEY (projeto_id) REFERENCES projeto(id);

ALTER TABLE users
ADD CONSTRAINT FK_users_equipe
FOREIGN KEY (equipe_id) REFERENCES equipe(id);


INSERT INTO users (id, nome, email, username, password, valorHora, horasDedicada, role)
VALUES ('2e8a8fd2-448c-4f5d-a2c2-7d3aabe9f406', 'ADMIN ADMIN', '"admin@itera.com.br', 'admin', '$2a$10$aRMLnzAkwiSDxjoQgZTEcOWKtq2QAmH24i0nVQ5bYlkzfI9k3luYW', NULL, NULL, '1');