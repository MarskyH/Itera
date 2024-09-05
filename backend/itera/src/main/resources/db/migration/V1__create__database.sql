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
    id TEXT PRIMARY KEY NOT NULL, -- Unique project identifier
    name VARCHAR(50) UNIQUE NOT NULL, -- Unique project name
    deadline INTEGER NOT NULL, -- Project deadline in days
    iteration_time INTEGER NOT NULL, -- Project iteration time in days
    work_hours TEXT NOT NULL, -- Project work hours per day
    client_name VARCHAR(50) NOT NULL, -- Client name
    created_by VARCHAR(100) NOT NULL, -- Created by (responsible user)
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Creation date and time
    modification_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Modification date and time
    last_access_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP-- Last access date and time
);


-- Team Table (Tabela de Membros de Equipe)
CREATE TABLE public.team_member (
    id TEXT PRIMARY KEY NOT NULL, -- Identificador único da equipe
    hourly_rate DOUBLE PRECISION NOT NULL, -- Taxa horária do usuário
    dedicated_hours TEXT NOT NULL, -- Horas dedicadas pelo usuário
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
    requirement_original TEXT,
    title VARCHAR(255) NOT NULL UNIQUE, -- Título do requisito
    details VARCHAR(255) NOT NULL, -- Detalhes do requisito
    complexity VARCHAR(30) NOT NULL, -- Complexidade do requisito
    priority VARCHAR(30) NOT NULL, -- Prioridade do requisito
    effort INTEGER NOT NULL, -- Esforço necessário para o requisito
    size_requirement INTEGER NOT NULL, -- Tamanho do requisito
    order_requirement INTEGER,
    cont_interation INTEGER DEFAULT 0,
    progressive_bar INTEGER DEFAULT 0,
    done BOOLEAN DEFAULT false,
    check_cancelled BOOLEAN DEFAULT false,
    iteration_id TEXT, -- Identificador da iteração associada
    project_id TEXT -- Identificador do projeto associado

);

-- Non-Functional Requirement Table (Tabela de Requisitos Não Funcionais)
CREATE TABLE nonfunctionalrequirement (
    id TEXT PRIMARY KEY NOT NULL, -- Identificador único do requisito não funcional
    title VARCHAR(100) NOT NULL, -- Título do requisito não funcional
    description VARCHAR NOT NULL, -- descrição do requisito não funcional
    weights JSONB NOT NULL, -- pesos entre 0 e 6 do requisito funcional
    multiple BOOLEAN NOT NULL -- aceita ou não mais de um value
);

CREATE TABLE nonfunctionalrequirementproject (
    id TEXT PRIMARY KEY NOT NULL, -- Identificador único do requisito não funcional do projetp
    project_id TEXT, -- Identificador do projeto associado
    nonfunctionalrequirement_id TEXT NOT NULL, -- Identificador d requisito não funcional associado
    weight_value INTEGER NOT NULL -- Valor do requisisto não funcional
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

CREATE TABLE public.degree (
    id TEXT PRIMARY KEY NOT NULL, -- Identificador único do usuário
    name VARCHAR(100) NOT NULL -- Nome do usuário
);

CREATE TABLE public.priority (
    id TEXT PRIMARY KEY NOT NULL, -- Identificador único da prioridade
    name VARCHAR(100) NOT NULL -- Nome da prioridade
);

CREATE TABLE public.task_type (
    id TEXT PRIMARY KEY NOT NULL, -- Identificador único da prioridade
    name VARCHAR(100) NOT NULL -- Nome da prioridade
);

CREATE TABLE public.size_requirement (
    id TEXT PRIMARY KEY NOT NULL, -- Identificador único da prioridade
    name VARCHAR(100) NOT NULL, -- Nome da prioridade
    function_points INTEGER
);

-- Criar a tabela 'iteration'
CREATE TABLE iteration (
    id TEXT PRIMARY KEY,
    number INTEGER,
    startDate TIMESTAMP,
    endDate TIMESTAMP,
    active BOOLEAN DEFAULT true,
    project_id TEXT NOT NULL
);

CREATE TABLE task (
    id TEXT PRIMARY KEY,
    title VARCHAR(255),
    priority VARCHAR(255),
    details VARCHAR(255),
    complexity VARCHAR(255),
    effort VARCHAR(255),
    size_task VARCHAR(255),
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    progressive_bar INTEGER DEFAULT 0,
    list_name VARCHAR(20),
    order_task INTEGER,
    check_cancelled BOOLEAN,
    details_cancelled TEXT,
    task_type TEXT,
    task_requirement_id TEXT,
    task_improvement_id TEXT,
    task_bug_id TEXT,
    iteration_id TEXT
);

CREATE TABLE task_requirement (
    id TEXT PRIMARY KEY,
    check_project BOOLEAN DEFAULT false,
    check_requirement BOOLEAN DEFAULT false,
    check_front BOOLEAN DEFAULT false,
    check_back BOOLEAN DEFAULT false,
    check_test BOOLEAN DEFAULT false,
    task_id TEXT
);

CREATE TABLE task_improvement (
    id TEXT PRIMARY KEY,
    requirement_id TEXT,
    check_project BOOLEAN DEFAULT false,
    check_requirement BOOLEAN DEFAULT false,
    check_front BOOLEAN DEFAULT false,
    check_back BOOLEAN DEFAULT false,
    check_test BOOLEAN DEFAULT false,
    task_id TEXT
);

CREATE TABLE task_bug (
    id TEXT PRIMARY KEY,
    requirement_id TEXT,
    check_front BOOLEAN DEFAULT false,
    check_back BOOLEAN DEFAULT false,
    check_test BOOLEAN DEFAULT false,
    task_id TEXT
);

CREATE TABLE assignee (
    id TEXT PRIMARY KEY,
    deadline INTEGER,
    task_step VARCHAR(255),
    member_id TEXT,
    task_id TEXT
);

CREATE TABLE pendency (
    id TEXT PRIMARY KEY,
    TITLE VARCHAR(100),
    description TEXT,
    list_name_original TEXT,
    creation_date TIMESTAMP,
    end_date TIMESTAMP,
    status BOOLEAN,
    task_id TEXT
);

-- Foreign Keys (Chaves Estrangeiras)

ALTER TABLE public.role
ADD CONSTRAINT FK_role_project
FOREIGN KEY (project_id) REFERENCES project(id)
ON DELETE CASCADE;

ALTER TABLE public.requirement
ADD CONSTRAINT FK_requirement_project
FOREIGN KEY (project_id) REFERENCES project(id)
ON DELETE CASCADE;

ALTER TABLE public.activity
ADD CONSTRAINT FK_activity_risk
FOREIGN KEY (risk_id) REFERENCES risk(id);

ALTER TABLE public.activity
ADD CONSTRAINT FK_activity_project
FOREIGN KEY (project_id) REFERENCES project(id);

ALTER TABLE public.risk
ADD CONSTRAINT FK_risk_project
FOREIGN KEY (project_id) REFERENCES project(id)
ON DELETE CASCADE;

ALTER TABLE public.team_member
ADD CONSTRAINT FK_user_team_member
FOREIGN KEY (user_id) REFERENCES users(id)
ON DELETE CASCADE;

ALTER TABLE public.team_member
ADD CONSTRAINT FK_role_team_member
FOREIGN KEY (role_id) REFERENCES role(id)
ON DELETE CASCADE;

ALTER TABLE public.team_member
ADD CONSTRAINT FK_project_team_member
FOREIGN KEY (project_id) REFERENCES project(id)
ON DELETE CASCADE;

ALTER TABLE public.nonfunctionalrequirementproject
ADD CONSTRAINT FK_project_nonfunctionalrequirementproject
FOREIGN KEY (project_id) REFERENCES project(id)
ON DELETE CASCADE;

ALTER TABLE public.nonfunctionalrequirementproject
ADD CONSTRAINT FK_nonfunctionalrequirement_nonfunctionalrequirementproject
FOREIGN KEY (nonfunctionalrequirement_id) REFERENCES nonfunctionalrequirement(id);

-- Adicionando chave estrangeira para task_requirement
ALTER TABLE task_requirement
ADD CONSTRAINT fk_task_requirement_task
FOREIGN KEY (task_id) REFERENCES task(id)
ON DELETE CASCADE;

-- Adicionando chave estrangeira para task_improvement
ALTER TABLE task_improvement
ADD CONSTRAINT fk_task_improvement_task
FOREIGN KEY (task_id) REFERENCES task(id)
ON DELETE CASCADE;

-- Adicionando chave estrangeira para task_bug
ALTER TABLE task_bug
ADD CONSTRAINT fk_task_bug_task
FOREIGN KEY (task_id) REFERENCES task(id)
ON DELETE CASCADE;

-- Adicionando chave estrangeira para task
ALTER TABLE task
ADD CONSTRAINT fk_task_iteration
FOREIGN KEY (iteration_id) REFERENCES iteration(id)
ON DELETE CASCADE;

-- Adicionando chave estrangeira para task
ALTER TABLE task
ADD CONSTRAINT fk_task_task_requirement
FOREIGN KEY (task_requirement_id) REFERENCES task_requirement(id)
ON DELETE CASCADE;

-- Adicionando chave estrangeira para task
ALTER TABLE task
ADD CONSTRAINT fk_task_task_improvement
FOREIGN KEY (task_improvement_id) REFERENCES task_improvement(id)
ON DELETE CASCADE;

-- Adicionando chave estrangeira para task
ALTER TABLE task
ADD CONSTRAINT fk_task_task_bug
FOREIGN KEY (task_bug_id) REFERENCES task_bug(id)
ON DELETE CASCADE;

-- Adicionando chave estrangeira para assignee
ALTER TABLE assignee
ADD CONSTRAINT fk_assignee_user
FOREIGN KEY (member_id) REFERENCES team_member(id)
ON DELETE CASCADE,
ADD CONSTRAINT fk_assignee_iteration
FOREIGN KEY (task_id) REFERENCES task(id)
ON DELETE CASCADE;

-- Adicionando chave estrangeira para iteration
ALTER TABLE iteration
ADD CONSTRAINT fk_iteration_project
FOREIGN KEY (project_id) REFERENCES project(id)
ON DELETE CASCADE;

-- Adicionando chave estrangeira para pendency
ALTER TABLE pendency
ADD CONSTRAINT fk_pendency_task
FOREIGN KEY (task_id) REFERENCES task(id)
ON DELETE CASCADE;

-- Data Insertion (Inserção de Dados)
INSERT INTO users (id, name, email, username, password, user_role)
VALUES ('2e8a8fd2-448c-4f5d-a2c2-7d3aabe9f406', 'ADMIN ADMIN', 'admin@itera.com.br', 'admin', '$2a$10$aRMLnzAkwiSDxjoQgZTEcOWKtq2QAmH24i0nVQ5bYlkzfI9k3luYW','1');

INSERT INTO risk_action_type (id, name)
VALUES ('1', 'Mitigação');

INSERT INTO risk_action_type (id, name)
VALUES ('2', 'Contingência');

INSERT INTO degree (id, name)
VALUES ('1', 'Alto');

INSERT INTO degree (id, name)
VALUES ('2', 'Médio');

INSERT INTO degree (id, name)
VALUES ('3', 'Baixo');

INSERT INTO priority (id, name)
VALUES ('1', 'Baixa');

INSERT INTO priority (id, name)
VALUES ('2', 'Média');

INSERT INTO priority (id, name)
VALUES ('3', 'Alta');

INSERT INTO priority (id, name)
VALUES ('4', 'Urgente');

INSERT INTO task_type (id, name)
VALUES ('1', 'Requisito');

INSERT INTO task_type (id, name)
VALUES ('2', 'Melhoria');

INSERT INTO task_type (id, name)
VALUES ('3', 'Bug');

INSERT INTO task_type (id, name)
VALUES ('4', 'Planejamento');

INSERT INTO task_type (id, name)
VALUES ('5', 'Revisão');

INSERT INTO task_type (id, name)
VALUES ('6', 'Retrospectiva');

INSERT INTO size_requirement (id, name, function_points)
VALUES ('1', 'Pequeno', 3);

INSERT INTO size_requirement (id, name, function_points)
VALUES ('2', 'Médio', 8);

INSERT INTO size_requirement (id, name, function_points)
VALUES ('3', 'Grande', 15);

INSERT INTO nonfunctionalrequirement (id, title, description, weights, multiple)
VALUES ('rfn1', 'Comunicação de Dados', 'Descreve o nível em que a aplicação comunica-se diretamente com o processador.', '{"0": {"value": 0, "description": "A aplicação é puramente batch ou uma estação de trabalho isolada."}, "1": {"value": 1, "description": "A aplicação é batch, mas possui entrada de dados ou impressão remota."}, "2": {"value": 2, "description": "A aplicação é batch, mas possui entrada de dados e impressão remota."}, "3": {"value": 3, "description": "A aplicação possui coleta de dados on-line, front-end de teleprocessamento para um processamento batch ou sistema de consulta."}, "4": {"value": 4, "description": "A aplicação é mais que um front-end, mas suporta apenas um tipo de protocolo de comunicação."}, "5": {"value": 5, "description": "A aplicação é mais que um front-end, e suporta mais que um tipo de protocolo de comunicação."}}', FALSE);

INSERT INTO nonfunctionalrequirement (id, title, description, weights, multiple)
VALUES ('rfn2', 'Processamento Distribuído', 'Descreve em que nível a aplicação transfere dados entre seus componentes', '{"0": {"value": 0, "description": "A aplicação não participa na transferência de dados ou processamento de funções entre os componentes do sistema."}, "1": {"value": 1, "description": "A aplicação prepara dados para processamento pelo usuário final em outro componente do sistema, como planilhas eletrônicas ou banco de dados."}, "2": {"value": 2, "description": "Dados são preparados para transferência, então são processados em outro componente do sistema (não para processamento pelo usuário final)."}, "3": {"value": 3, "description": "Processamento distribuído e transferência de dados são feitos em linha e em apenas uma direção."}, "4": {"value": 4, "description": "Processamento distribuído e transferência de dados são feitos em linha e em ambas as direções."}, "5": {"value": 5, "description": "Os processamentos de funções são executados dinamicamente no componente mais apropriado do sistema."}}', FALSE);

INSERT INTO nonfunctionalrequirement (id, title, description, weights, multiple)
VALUES ('rfn3', 'Performance', 'Descreve em que nível os requisitos estabelecidos pelo usuário, sobre tempo de resposta, influenciam no projeto, desenvolvimento, instalação e suporte da aplicação', '{"0": {"value": 0, "description": "O usuário não estabeleceu nenhum requisito especial sobre performance."}, "1": {"value": 1, "description": "Requisitos de performance e projeto foram estabelecidos e revisados, mas nenhuma ação em especial foi tomada."}, "2": {"value": 2, "description": "Tempo de resposta ou taxa de transações são críticos durante as horas de pico. Não é necessário nenhum projeto especial para a utilização de CPU. O limite para o processamento é o dia seguinte."}, "3": {"value": 3, "description": "Tempo de resposta ou taxa de transações são críticos durante todas as horas de trabalho. Não foi necessário nenhum projeto especial para a utilização de CPU. O limite de processamento é crítico."}, "4": {"value": 4, "description": "Adicionalmente, requisitos especificados pelo usuário são exigentes o bastante para que tarefas de análise de performance sejam necessárias na fase de projeto."}, "5": {"value": 5, "description": "Adicionalmente, ferramentas de análise de performance devem ser utilizadas nas fases de projeto, desenvolvimento e/ou implementação para que os requisitos de performance do usuário sejam atendidos."}}', FALSE);

INSERT INTO nonfunctionalrequirement (id, title, description, weights, multiple)
VALUES ('rfn4', 'Carga de Configuração (Máquina)', 'Descreve em que nível restrições computacionais influenciam no desenvolvimento da aplicação.', '{"0": {"value": 0, "description": "Não existem restrições operacionais implícitas ou explícitas nos requisitos."}, "1": {"value": 1, "description": "Existem restrições operacionais, mas são menos restritivas que uma aplicação típica. Não há esforço especial necessário ao atendimento destas restrições."}, "2": {"value": 2, "description": "Existem restrições operacionais, mas são restrições típicas da aplicação. Há esforço especial necessário ao atendimento dessas restrições."}, "3": {"value": 3, "description": "Existem requisitos específicos de processador para uma parte específica da aplicação."}, "4": {"value": 4, "description": "Restrições operacionais explícitas necessitam de um processador dedicado ou utilização pesada do processador central."}, "5": {"value": 5, "description": "Adicionalmente, existem limitações na aplicação nos componentes distribuídos do sistema."}}', FALSE);

INSERT INTO nonfunctionalrequirement (id, title, description, weights, multiple)
VALUES ('rfn5', 'Volume de Transações', 'Descreve em que nível o alto volume de transações influencia o projeto, desenvolvimento, instalação e suporte da aplicação', '{"0": {"value": 0, "description": "Não é antecipado nenhum período de pico de transações."}, "1": {"value": 1, "description": "São previstos períodos de pico de processamento (Ex.: Picos mensal, quinzenal, periódico, anual), mas o impacto no esforço do projeto é mínimo."}, "2": {"value": 2, "description": "Volumes de transação regulares (Ex.: Picos semanais) são previstos. Há algum impacto no esforço do projeto."}, "3": {"value": 3, "description": "Altos volumes de transação (Ex. Picos diários) são previstos, conseqüentemente com impacto significativo no esforço do projeto."}, "4": {"value": 4, "description": "Altas taxas de transação definidos pelo usuário nos requisitos ou os níveis de serviço acordados são altos o bastante para requererem tarefas de análise de performance na fase de projeto."}, "5": {"value": 5, "description": "Adicionalmente, existem requisitos de ferramentas de análise de performance nas fases de projeto, desenvolvimento e/ou instalação."}}', FALSE);

INSERT INTO nonfunctionalrequirement (id, title, description, weights, multiple)
VALUES ('rfn6', 'Entrada de Dados On-Line', 'Descreve em que nível são efetuadas entradas de dados na aplicação por meio de transações interativas', '{"0": {"value": 0, "description": "Todas as transações são processadas em lote."}, "1": {"value": 1, "description": "De 1% a 7% das transações são entradas de dados on-line."}, "2": {"value": 2, "description": "De 8% a 15% das transações são entradas de dados on-line."}, "3": {"value": 3, "description": "De 16% a 23% das transações são entradas de dados on-line."}, "4": {"value": 4, "description": "De 24% a 30% das transações são entradas de dados on-line."}, "5": {"value": 5, "description": "Mais de 30% das transações são entradas de dados on-line."}}', FALSE);

INSERT INTO nonfunctionalrequirement (id, title, description, weights, multiple)
VALUES ('rfn7', 'Eficiência do Usuário Final', 'As funções on-line fornecidas pela aplicação enfatizam um projeto para o aumento da eficiência do usuário final. O projeto inclui auxílio para navegação como, por exemplo: a) teclas de função; b) saltos, menus gerados dinamicamente; c) menus; d) ajuda on-line e documentação; e) movimentação automática de cursor; f) paginação; g) impressão remota através de transações on-line; h) teclas de função pré-definidas; i) tarefas em lote submetidos de transações on-line; j) drop-down list box; k) uso intenso de vídeo reverso, brilho, cores e outros indicadores; l) interface de mouse; m) janelas pop-up; n) utilização de número mínimo de telas para executar uma função do negócio; o) suporte a dois idiomas (conte como 4 itens); p) suporte a mais de dois idiomas (conte como 6 itens); q) impressão de documentação.', '{"0": {"value": 0, "description": "Nenhum dos itens ao acima."}, "1": {"value": 1, "description": "De um a três dos itens ao acima."}, "2": {"value": 2, "description": "De quatro a cinco dos itens ao acima."}, "3": {"value": 3, "description": "Seis ou mais dos itens ao acima, mas não existem requisitos específicos do usuário associados a eficiência."}, "4": {"value": 4, "description": "Seis ou mais dos itens ao acima, e requisitos explícitos sobre a eficiência para o usuário final são fortes o bastante para necessitarem de tarefas de projeto incluírem fatores humanos como minimizar o número de batidas no tecacima, maximizar padrões de campo e uso de templates."}, "5": {"value": 5, "description": "Seis ou mais dos itens ao acima e requisitos explícitos sobre a eficiência para o usuário final são fortes o bastante para necessitarem do uso de ferramentas e processos especiais para demonstrar que os objetivos foram alcançados."}}', FALSE);

INSERT INTO nonfunctionalrequirement (id, title, description, weights, multiple)
VALUES ('rfn8', 'Atualização On-Line', 'Descreve em que nível os arquivos lógicos internos são atualizados de forma on-line.', '{"0": {"value": 0, "description": "Não há nenhuma atualização on-line."}, "1": {"value": 1, "description": "Existe a atualização on-line de um a três arquivos de controle. Volume de atualização é pequeno e a recuperação é fácil."}, "2": {"value": 2, "description": "Existe a atualização on-line de quatro ou mais arquivos de controle. Volume de atualização é pequeno e a recuperação é fácil."}, "3": {"value": 3, "description": "A atualização da maioria dos arquivos internos é on-line. A recuperação é fácil."}, "4": {"value": 4, "description": "A atualização da maioria dos arquivos internos é on-line. Existe a necessidade de proteção contra perda de dados."}, "5": {"value": 5, "description": "A atualização de todos os arquivos internos é on-line. Existe a necessidade de proteção contra perda de dados e o alto volume de processamento torna necessária a análise do custo do processo de recuperação."}}', FALSE);

INSERT INTO nonfunctionalrequirement (id, title, description, weights, multiple)
VALUES ('rfn9', 'Processamento Complexo', 'Descreve em que nível o processamento lógico ou matemático influencia o desenvolvimento da aplicação. Os seguintes componentes estão presentes: a) controle sensível (por exemplo, processamento especial de auditoria) e/ou processamento específico de segurança da aplicação; b) processamento lógico extensivo; c) processamento matemático extensivo; d) muito processamento de exceção resultando em transações incompletas que devem ser processadas novamente, por exemplo, transações incompletas em ATM em função de problemas de teleprocessamento, falta de dados ou problemas de edição; e) processamento complexo para manipular múltiplas possibilidades de entrada e saída, como por exemplo, multimídia, ou independência de dispositivo.', '{"0": {"value": 0, "description": "Nenhum dos itens acima é significativo para a aplicação."}, "1": {"value": 1, "description": "Qualquer um dos itens acima são significativos para a aplicação."}, "2": {"value": 2, "description": "Quaisquer dois itens acima são significativos para a aplicação."}, "3": {"value": 3, "description": "Quaisquer dos três itens acima são significativos para a aplicação."}, "4": {"value": 4, "description": "Quaisquer dos quatro itens acima são significativos para a aplicação."}, "5": {"value": 5, "description": "Todos os itens acima são significativos para a aplicação."}}', FALSE);

INSERT INTO public.nonfunctionalrequirement (id, title, description, weights, multiple)
VALUES ('rfn10', 'Reusabilidade', 'Descreve em que nível a aplicação e seu código foram especificamente projetadas, desenvolvidas, e suportadas para serem utilizadas em outras aplicações', '{"0": {"value": 0, "description": "Não há código reutilizável."}, "1": {"value": 1, "description": "Código reutilizável é utilizado na aplicação."}, "2": {"value": 2, "description": "Menos de dez por cento do código-fonte da aplicação foi construído levando em consideração o uso em mais de uma aplicação."}, "3": {"value": 3, "description": "De dez a vinte e cinco por cento do código-fonte da aplicação foi construído levando em consideração o uso em mais de uma aplicação."}, "4": {"value": 4, "description": "A aplicação foi especificamente empacotada e/ou documentada para fácil reutilização, ela é customizada pelo usuário ao nível de código."}, "5": {"value": 5, "description": "A aplicação foi especificamente empacotada e/ou documentada para fácil reutilização, ela é customizada pelo usuário através de manutenção de parâmetros."}}', FALSE);

INSERT INTO nonfunctionalrequirement (id, title, description, weights, multiple)
VALUES ('rfn11', 'Facilidade de Instalação', 'Um plano e/ou ferramentas de conversão e instalação foram fornecidos e testados durante a fase de instalação.', '{"0": {"value": 0, "description": "O usuário não definiu considerações especiais, assim como não é requerido nenhum setup para a instalação."}, "1": {"value": 1, "description": "O usuário não definiu considerações especiais, mas é necessário setup para a instalação."}, "2": {"value": 2, "description": "Requisitos de instalação e conversão foram definidos pelo usuário, e guias de conversão e instalação foram fornecidos e testados. Não é considerado importante o impacto da conversão."}, "3": {"value": 3, "description": "Requisitos de instalação e conversão foram definidos pelo usuário, e guias de conversão e instalação foram fornecidos e testados. É considerado importante o impacto da conversão."}, "4": {"value": 4, "description": "Além do item 2 acima, ferramentas de instalação e conversão automáticas foram fornecidas e testadas."}, "5": {"value": 5, "description": "Além do item 3 acima, ferramentas de instalação e conversão automáticas foram fornecidas e testadas."}}', FALSE);

INSERT INTO nonfunctionalrequirement (id, title, description, weights, multiple)
VALUES ('rfn12', 'Facilidade de Operação', 'Descreve em que nível a aplicação atende a alguns aspectos operacionais como: inicialização, segurança e remoção da aplicação.', '{"0": {"value": 0, "description": "Não foram estabelecidas pelo usuário outra consideração que não os procedimentos de segurança normais."}, "1": {"value": 1, "description": "Um, alguns ou todos os seguintes itens são válidos para a aplicação. Selecione todos aqueles que sejam válidos. Cada item tem um value de um ponto, a exceção de onde seja citado o contrário: * procedimentos de inicialização, salva e recuperação foram fornecidos, mas é necessária a intervenção do operador; * procedimentos de inicialização, salva e recuperação foram fornecidos, e não é necessária a intervenção do operador (conte como dois itens); * a aplicação minimiza a necessidade de montagem de fitas; * a aplicação minimiza a necessidade de manipulação de papel."}, "5": {"value": 5, "description": "Aplicação projetada para operação não assistida. Isto é, não é necessária nenhuma intervenção do operador para operar o sistema, que não seja a inicialização e término da aplicação. A recuperação automática de erros é uma característica da aplicação."}}', TRUE);

INSERT INTO public.nonfunctionalrequirement (id, title, description, weights, multiple)
VALUES ('rfn13', 'Múltiplos Locais', 'Descreve em que nível a aplicação foi especificamente projetada, desenvolvida e suportada para diferentes ambientes de hardware e software', '{"0": {"value": 0, "description": "Os requisitos do usuário não consideram a necessidade de mais de um usuário/local de instalação."}, "1": {"value": 1, "description": "Necessidade de múltiplos locais foi considerada no projeto, e a aplicação foi projetada para operar apenas nos mesmos ambientes de hardware e software."}, "2": {"value": 2, "description": "Necessidade de múltiplos locais foi considerada no projeto, e a aplicação foi projetada para operar apenas ambientes de hardware e software similares."}, "3": {"value": 3, "description": "Necessidade de múltiplos locais foi considerada no projeto, e a aplicação foi projetada para operar ambientes diferentes de hardware e software."}, "4": {"value": 4, "description": "Adicionalmente aos itens 1 e 2, plano de suporte e documentação são fornecidos e testados para suportar a aplicação em múltiplos locais."}, "5": {"value": 5, "description": "Adicionalmente ao item 3, plano de suporte e documentação são fornecidos e testados para suportar a aplicação em múltiplos locais."}}', FALSE);

INSERT INTO nonfunctionalrequirement (id, title, description, weights, multiple)
VALUES ('rfn14', 'Facilidade de Mudanças', 'Descreve em que nível a aplicação foi especificamente desenvolvida para facilitar a mudança de sua lógica de processamento ou estrutura de dados.  As seguintes características podem ser válidas para a aplicação: a) são fornecidos mecanismos de consulta e reporte flexível, que permitem a manipulação de pedidos simples; por exemplo, lógica de e/ou aplicada a apenas um arquivo lógico (conte como um item); b) são fornecidos mecanismos de consulta e reporte flexível, que permitem a manipulação de pedidos de média complexidade; por exemplo, lógica de e/ou aplicada a mais de um arquivo lógico (conte como dois itens); c) são fornecidos mecanismos de consulta e reporte flexível, que permitem a manipulação de pedidos complexos; por exemplo, lógica de e/ou combinadas em um ou mais arquivos lógicos (conte como três itens); d) dados de controle do negócio são mantidos pelo usuário através de processos interativos, mas as alterações só tem efeito no próximo dia útil; e) dados de controle do negócio são mantidos pelo usuário através de processos interativos, e as alterações tem efeito imediato (conte como dois itens).', '{"0": {"value": 0, "description": "Nenhum dos itens ao acima."}, "1": {"value": 1, "description": "Qualquer um dos itens ao acima."}, "2": {"value": 2, "description": "Quaisquer dois itens ao acima."}, "3": {"value": 3, "description": "Quaisquer três itens ao acima."}, "4": {"value": 4, "description": "Quaisquer quatro itens ao acima."}, "5": {"value": 5, "description": "Todos os cinco itens ao acima."}}', FALSE);

