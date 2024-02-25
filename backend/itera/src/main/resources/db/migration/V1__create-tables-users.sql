CREATE TABLE users (
    id TEXT PRIMARY KEY UNIQUE NOT NULL,
    nome TEXT NOT NULL,
    login TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    horasDedicada NUMERIC,
    role TEXT NOT NULL
);

