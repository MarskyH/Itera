-- Activity Table (Tabela de Atividades)
CREATE TABLE public.activity (
    id TEXT PRIMARY KEY, -- Identificador único da atividade
    title VARCHAR(30), -- Título da atividade
    description VARCHAR(255), -- Descrição da atividade
    type VARCHAR(20), -- Tipo da atividade
    risk_id TEXT, -- Identificador de risco associado
    project_id TEXT -- Identificador de projeto associado
);

-- Project Table (Tabela de Projetos)
CREATE TABLE public.project (
    id TEXT PRIMARY KEY NOT NULL, -- Identificador único do projeto
    name VARCHAR(50) UNIQUE NOT NULL, -- Nome único do projeto
    deadline INTEGER NOT NULL, -- Prazo do projeto em dias
    iteration_time INTEGER NOT NULL, -- Tempo de iteração do projeto em dias
    work_hours INTEGER NOT NULL, -- Horas de trabalho do projeto por dia
    client_name VARCHAR(50) NOT NULL, -- Nome do cliente
    created_by VARCHAR(100) NOT NULL -- Criado por (usuário responsável)
);

-- Team Table (Tabela de Membros de Equipe)
CREATE TABLE public.team_member (
    id TEXT PRIMARY KEY NOT NULL, -- Identificador único da equipe
    hourly_rate DOUBLE PRECISION NOT NULL, -- Taxa horária do usuário
    dedicated_hours INTEGER NOT NULL, -- Horas dedicadas pelo usuário
    user_id TEXT,
    role_id TEXT,
    project_id TEXT -- Identificador do projeto associado
);

-- Role Table (Tabela de Funções)
CREATE TABLE public.role (
    id TEXT PRIMARY KEY NOT NULL, -- Identificador único da função
    function VARCHAR(30) NOT NULL, -- Função
    skill VARCHAR(255) NOT NULL, -- Habilidade
    competency VARCHAR(255) NOT NULL, -- Competência
    project_id TEXT -- Identificador do projeto associado
);

-- Requirement Table (Tabela de Requisitos)
CREATE TABLE public.requirement (
    id TEXT PRIMARY KEY NOT NULL, -- Identificador único do requisito
    title VARCHAR(30) NOT NULL, -- Título do requisito
    details VARCHAR(30) NOT NULL, -- Detalhes do requisito
    complexity VARCHAR(30) NOT NULL, -- Complexidade do requisito
    priority VARCHAR(30) NOT NULL, -- Prioridade do requisito
    effort INTEGER NOT NULL, -- Esforço necessário para o requisito
    size_requirement INTEGER NOT NULL, -- Tamanho do requisito
    project_id TEXT -- Identificador do projeto associado
);

-- Non-Functional Requirement Table (Tabela de Requisitos Não Funcionais)
CREATE TABLE public.nonfunctionalrequirement (
    id TEXT PRIMARY KEY NOT NULL, -- Identificador único do requisito não funcional
    title VARCHAR(30) NOT NULL, -- Título do requisito não funcional
    value_requirement INTEGER NOT NULL, -- Valor do requisito não funcional
    project_id TEXT -- Identificador do projeto associado
);

-- Risk Table (Tabela de Riscos)
CREATE TABLE public.risk (
    id TEXT PRIMARY KEY NOT NULL, -- Identificador único do risco
    title VARCHAR(30) NOT NULL, -- Título do risco
    effect VARCHAR(255) NOT NULL, -- Efeito do risco
    probability VARCHAR(20) NOT NULL, -- Probabilidade do risco
    impact VARCHAR(20) NOT NULL, -- Impacto do risco
    exposure_degree VARCHAR(20) NOT NULL, -- Grau de exposição ao risco
    description VARCHAR(255) NOT NULL, -- Descrição da atividade
    type VARCHAR(20) NOT NULL, -- Tipo da atividade
    project_id TEXT -- Identificador do projeto associado
);

-- Users Table (Tabela de Usuários)
CREATE TABLE public.users (
    id TEXT PRIMARY KEY NOT NULL, -- Identificador único do usuário
    name VARCHAR(100) NOT NULL, -- Nome do usuário
    email VARCHAR(100) NOT NULL, -- E-mail do usuário
    username VARCHAR(50) NOT NULL, -- Nome de usuário
    password VARCHAR(255) NOT NULL, -- Senha do usuário
    user_role TEXT NOT NULL -- Função do usuário
);

CREATE TABLE public.risk_action_type (
    id TEXT PRIMARY KEY NOT NULL, -- Identificador único do usuário
    name VARCHAR(100) NOT NULL -- Nome do usuário
);

CREATE TABLE public.exposure_degree (
    id TEXT PRIMARY KEY NOT NULL, -- Identificador único do usuário
    name VARCHAR(100) NOT NULL -- Nome do usuário
);

-- Foreign Keys (Chaves Estrangeiras)


ALTER TABLE public.role
ADD CONSTRAINT FK_role_project
FOREIGN KEY (project_id) REFERENCES project(id);

ALTER TABLE public.requirement
ADD CONSTRAINT FK_requirement_project
FOREIGN KEY (project_id) REFERENCES project(id);

ALTER TABLE public.nonfunctionalrequirement
ADD CONSTRAINT FK_nonfunctionalrequirement_project
FOREIGN KEY (project_id) REFERENCES project(id);

ALTER TABLE public.activity
ADD CONSTRAINT FK_activity_risk
FOREIGN KEY (risk_id) REFERENCES risk(id);

ALTER TABLE public.activity
ADD CONSTRAINT FK_activity_project
FOREIGN KEY (project_id) REFERENCES project(id);

ALTER TABLE public.risk
ADD CONSTRAINT FK_risk_project
FOREIGN KEY (project_id) REFERENCES project(id);

ALTER TABLE public.team_member
ADD CONSTRAINT FK_user_team_member
FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE public.team_member
ADD CONSTRAINT FK_role_team_member
FOREIGN KEY (role_id) REFERENCES role(id);

ALTER TABLE public.team_member
ADD CONSTRAINT FK_project_team_member
FOREIGN KEY (project_id) REFERENCES project(id);

-- Data Insertion (Inserção de Dados)
INSERT INTO users (id, name, email, username, password, user_role)
VALUES ('2e8a8fd2-448c-4f5d-a2c2-7d3aabe9f406', 'ADMIN ADMIN', 'admin@itera.com.br', 'admin', '$2a$10$aRMLnzAkwiSDxjoQgZTEcOWKtq2QAmH24i0nVQ5bYlkzfI9k3luYW','1');

INSERT INTO risk_action_type (id, name)
VALUES ('1', 'Mitigação');

INSERT INTO risk_action_type (id, name)
VALUES ('2', 'Contingência');

INSERT INTO exposure_degree (id, name)
VALUES ('1', 'Alto');

INSERT INTO exposure_degree (id, name)
VALUES ('2', 'Médio');

INSERT INTO exposure_degree (id, name)
VALUES ('3', 'Baixo');

