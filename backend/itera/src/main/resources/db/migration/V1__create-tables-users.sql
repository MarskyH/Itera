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
VALUES ('your_unique_id', 'Admin Name', 'admin@example.com', 'admin', 'your_hashed_password', NULL, NULL, 'ADMIN');



