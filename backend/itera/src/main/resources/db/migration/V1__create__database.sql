--Sequencia acao_seq
CREATE SEQUENCE public.acao_seq
    AS INTEGER
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


-- Tabela Acao
CREATE TABLE  public.acao (
    id INTEGER PRIMARY KEY,
    titulo VARCHAR(30),
    descricao VARCHAR(255),
    tipo VARCHAR(20),
    risco_id BIGINT
);

--Linkando acao_seq com acao_id
ALTER SEQUENCE public.acao_seq OWNED BY public.acao.id;

--Sequencia projeto_seq
CREATE SEQUENCE public.projeto_seq
    AS INTEGER
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- Tabela Projeto
CREATE TABLE  public.projeto (
    id INTEGER PRIMARY KEY,
    nome VARCHAR(50) UNIQUE,
    prazo INTEGER,
    tempo_iteracao INTEGER,
    nome_cliente VARCHAR(50),
    created_By VARCHAR(100)

);

--Linkando projeto_seq com projeto_id
ALTER SEQUENCE public.projeto_seq OWNED BY public.projeto.id;

--Sequencia equipe_seq
CREATE SEQUENCE public.equipe_seq
    AS INTEGER
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- Tabela Equipe
CREATE TABLE  public.equipe (
    id INTEGER PRIMARY KEY,
    projeto_id BIGINT
);

--Linkando equipe_seq com equipe_id
ALTER SEQUENCE public.equipe_seq OWNED BY public.equipe.id;

--Sequencia papel_seq
CREATE SEQUENCE public.papel_seq
    AS INTEGER
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- Tabela Papel
CREATE TABLE  public.papel (
    id INTEGER PRIMARY KEY,
    funcao VARCHAR(30),
    habilidade VARCHAR(255),
    competencia VARCHAR(255),
    projeto_id BIGINT
);

--Linkando papel_seq com papel_id
ALTER SEQUENCE public.papel_seq OWNED BY public.papel.id;

--Sequencia requisito_seq
CREATE SEQUENCE public.requisito_seq
    AS INTEGER
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- Tabela Requisito
CREATE TABLE  public.requisito (
    id INTEGER PRIMARY KEY,
    titulo VARCHAR(30),
    detalhamento VARCHAR(30),
    complexidade VARCHAR(30),
    prioridade VARCHAR(30),
    esforco INTEGER,
    tamanho INTEGER,
    projeto_id BIGINT
);

--Linkando requisito__seq com requisito__id
ALTER SEQUENCE public.requisito_seq OWNED BY public.requisito.id;

--Sequencia requisitonaofuncional_seq
CREATE SEQUENCE public.requisitonaofuncional_seq
    AS INTEGER
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


-- Tabela RequisitoNaoFuncional
CREATE TABLE  public.requisitonaofuncional (
    id INTEGER PRIMARY KEY,
    titulo VARCHAR(30),
    valor INTEGER,
    projeto_id BIGINT
);

--Linkando requisitonaofuncional_seq com requisitonaofuncional_id
ALTER SEQUENCE public.requisitonaofuncional_seq OWNED BY public.requisitonaofuncional.id;

--Sequencia risco_seq
CREATE SEQUENCE public.risco_seq
    AS INTEGER
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- Tabela Risco
CREATE TABLE  public.risco (
    id INTEGER PRIMARY KEY,
    titulo VARCHAR(30),
    efeito VARCHAR(255),
    probabilidade VARCHAR(20),
    impacto VARCHAR(20),
    grau_exposicao VARCHAR(20),
    acao_id BIGINT,
    projeto_id BIGINT
);

--Linkando risco_seq com risco_id
ALTER SEQUENCE public.risco_seq OWNED BY public.risco.id;

-- Tabela Users
CREATE TABLE  public.users (
    id TEXT PRIMARY KEY NOT NULL,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    valorHora DOUBLE PRECISION,
    horasDedicada INTEGER,
    role TEXT NOT NULL,
    equipe_id BIGINT,
    papel_id BIGINT
);


--Chaves Estrangeiras

ALTER TABLE  public.equipe
ADD CONSTRAINT FK_equipe_projeto
FOREIGN KEY (projeto_id) REFERENCES projeto(id);

ALTER TABLE  public.papel
ADD CONSTRAINT FK_papel_projeto
FOREIGN KEY (projeto_id) REFERENCES projeto(id);

ALTER TABLE  public.requisito
ADD CONSTRAINT FK_requisito_projeto
FOREIGN KEY (projeto_id) REFERENCES projeto(id);

ALTER TABLE  public.requisitonaofuncional
ADD CONSTRAINT FK_requisitonaofuncional_projeto
FOREIGN KEY (projeto_id) REFERENCES projeto(id);

ALTER TABLE  public.acao
ADD CONSTRAINT FK_acao_risco
FOREIGN KEY (risco_id) REFERENCES risco(id);

ALTER TABLE  public.risco
ADD CONSTRAINT FK_risco_projeto
FOREIGN KEY (projeto_id) REFERENCES projeto(id);

ALTER TABLE  public.users
ADD CONSTRAINT FK_users_equipe
FOREIGN KEY (equipe_id) REFERENCES equipe(id);

ALTER TABLE  public.users
ADD CONSTRAINT FK_users_papel
FOREIGN KEY (papel_id) REFERENCES papel(id);


--Inserção de dados

INSERT INTO users (id, nome, email, username, password, valorHora, horasDedicada, role, equipe_id, papel_id)
VALUES ('2e8a8fd2-448c-4f5d-a2c2-7d3aabe9f406', 'ADMIN ADMIN', '"admin@itera.com.br', 'admin', '$2a$10$aRMLnzAkwiSDxjoQgZTEcOWKtq2QAmH24i0nVQ5bYlkzfI9k3luYW', NULL, NULL, '1', NULL, NULL);