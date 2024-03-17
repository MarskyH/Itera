-- Activity Table (Tabela de Atividades)
CREATE TABLE public.activity (
    id TEXT PRIMARY KEY, -- Identificador único da atividade
    title VARCHAR(30), -- Título da atividade
    description VARCHAR(255), -- Descrição da atividade
    type VARCHAR(20), -- Tipo da atividade
    risk_id TEXT -- Identificador de risco associado
);

-- Project Table (Tabela de Projetos)
CREATE TABLE public.project (
    id TEXT PRIMARY KEY, -- Identificador único do projeto
    name VARCHAR(50) UNIQUE, -- Nome único do projeto
    deadline INTEGER, -- Prazo do projeto em dias
    iteration_time INTEGER, -- Tempo de iteração do projeto em dias
    work_hours INTEGER, -- Horas de trabalho do projeto por dia
    client_name VARCHAR(50), -- Nome do cliente
    created_by VARCHAR(100) -- Criado por (usuário responsável)
);

-- Team Table (Tabela de Equipes)
CREATE TABLE public.team (
    id TEXT PRIMARY KEY, -- Identificador único da equipe
    project_id TEXT -- Identificador do projeto associado
);

-- Role Table (Tabela de Funções)
CREATE TABLE public.role (
    id TEXT PRIMARY KEY, -- Identificador único da função
    function VARCHAR(30), -- Função
    skill VARCHAR(255), -- Habilidade
    competency VARCHAR(255), -- Competência
    project_id TEXT -- Identificador do projeto associado
);

-- Requirement Table (Tabela de Requisitos)
CREATE TABLE public.requirement (
    id TEXT PRIMARY KEY, -- Identificador único do requisito
    title VARCHAR(30), -- Título do requisito
    details VARCHAR(30), -- Detalhes do requisito
    complexity VARCHAR(30), -- Complexidade do requisito
    priority VARCHAR(30), -- Prioridade do requisito
    effort INTEGER, -- Esforço necessário para o requisito
    size_requirement INTEGER, -- Tamanho do requisito
    project_id TEXT -- Identificador do projeto associado
);

-- Non-Functional Requirement Table (Tabela de Requisitos Não Funcionais)
CREATE TABLE public.nonfunctionalrequirement (
    id TEXT PRIMARY KEY, -- Identificador único do requisito não funcional
    title VARCHAR(30), -- Título do requisito não funcional
    value_requirement INTEGER, -- Valor do requisito não funcional
    project_id TEXT -- Identificador do projeto associado
);

-- Risk Table (Tabela de Riscos)
CREATE TABLE public.risk (
    id TEXT PRIMARY KEY, -- Identificador único do risco
    title VARCHAR(30), -- Título do risco
    effect VARCHAR(255), -- Efeito do risco
    probability VARCHAR(20), -- Probabilidade do risco
    impact VARCHAR(20), -- Impacto do risco
    exposure_degree VARCHAR(20), -- Grau de exposição ao risco
    action_id TEXT, -- Identificador da ação associada
    project_id TEXT -- Identificador do projeto associado
);

-- Users Table (Tabela de Usuários)
CREATE TABLE public.users (
    id TEXT PRIMARY KEY NOT NULL, -- Identificador único do usuário
    name VARCHAR(100) NOT NULL, -- Nome do usuário
    email VARCHAR(100) NOT NULL, -- E-mail do usuário
    username VARCHAR(50) NOT NULL, -- Nome de usuário
    password VARCHAR(255) NOT NULL, -- Senha do usuário
    hourly_rate DOUBLE PRECISION, -- Taxa horária do usuário
    dedicated_hours INTEGER, -- Horas dedicadas pelo usuário
    user_role TEXT NOT NULL, -- Função do usuário
    team_id TEXT, -- Identificador da equipe associada
    role_id TEXT -- Identificador da função associada
);

-- Foreign Keys (Chaves Estrangeiras)

ALTER TABLE public.team
ADD CONSTRAINT FK_team_project
FOREIGN KEY (project_id) REFERENCES project(id);

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

ALTER TABLE public.risk
ADD CONSTRAINT FK_risk_project
FOREIGN KEY (project_id) REFERENCES project(id);

ALTER TABLE public.users
ADD CONSTRAINT FK_users_team
FOREIGN KEY (team_id) REFERENCES team(id);

ALTER TABLE public.users
ADD CONSTRAINT FK_users_role
FOREIGN KEY (role_id) REFERENCES role(id);

-- Data Insertion (Inserção de Dados)

INSERT INTO users (id, name, email, username, password, hourly_rate, dedicated_hours, user_role, team_id, role_id)
VALUES ('2e8a8fd2-448c-4f5d-a2c2-7d3aabe9f406', 'ADMIN ADMIN', 'admin@itera.com.br', 'admin', '$2a$10$aRMLnzAkwiSDxjoQgZTEcOWKtq2QAmH24i0nVQ5bYlkzfI9k3luYW', NULL, NULL, '1', NULL, NULL);