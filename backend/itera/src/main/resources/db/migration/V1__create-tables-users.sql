CREATE TABLE users (
    id TEXT PRIMARY KEY UNIQUE NOT NULL,
    nome TEXT NOT NULL,
    email TEXT NOT NULL,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    valorHora NUMERIC,
    horasDedicada NUMERIC,
    role TEXT NOT NULL
);

INSERT INTO users (id, nome, email, username, password, valorHora, horasDedicada, role)
VALUES ('2e8a8fd2-448c-4f5d-a2c2-7d3aabe9f406', 'ADMIN ADMIN', '"admin@itera.com.br', 'admin', '$2a$10$aRMLnzAkwiSDxjoQgZTEcOWKtq2QAmH24i0nVQ5bYlkzfI9k3luYW', NULL, NULL, '1');



