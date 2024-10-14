# Manual de Implantação da Aplicação

Este manual tem como objetivo guiar o processo de implantação da aplicação, abordando as etapas para configurar e executar o **front-end**, **back-end** e o **banco de dados** PostgreSQL. O manual abrange o processo tanto para sistemas Linux quanto para Windows, destacando as diferenças onde necessário.

---

## 1. Visão Geral da Arquitetura

A aplicação segue uma arquitetura baseada em três componentes principais:

- **Front-end**: Desenvolvido em **Vue.js** com **TypeScript**, responsável pela interação visual com o usuário.
- **Back-end (API REST)**: Desenvolvido em **Spring Boot** (Java), implementa as regras de negócio e processa requisições.
- **Banco de Dados (PostgreSQL)**: Armazena os dados de forma persistente e utiliza **Flyway** para controle de versões e migrações de banco de dados.

---

## 2. Requisitos do Sistema

### Servidores Necessários
- **Servidor Web**: Para hospedar o front-end (Nginx, Apache ou uma CDN como Netlify).
- **Servidor de Aplicação**: Para rodar o back-end (Heroku, AWS EC2, ou um servidor local).
- **Servidor de Banco de Dados**: Para o PostgreSQL (local ou em nuvem, como AWS RDS).

### Ferramentas Necessárias
- **Node.js** (LTS) para o front-end.
- **Java 21** ou superior para o back-end.
- **PostgreSQL 13+** para o banco de dados.
- **Flyway** para controle de migração de banco de dados.

### Pré-requisitos de Software
- **Git** para versionamento de código.
- **Docker** (opcional) para containerização do ambiente.

---

## 3. Instalação e Configuração do Ambiente

### 3.1 Front-end

#### Instalação do Node.js e NPM

##### Linux
No **Linux**, você pode instalar o **Node.js** e o **NPM** executando os seguintes comandos:

```bash
sudo apt update
sudo apt install nodejs npm
````
##### Windows
No **Windows**, baixe e instale o Node.js diretamente do site oficial: https://nodejs.org. O instalador já inclui o NPM.

Verifique se a instalação foi bem-sucedida (tanto no Linux quanto no Windows):
```bash
node -v
npm -v
````

##### Instalando as dependências
Navegue até o diretório do projeto front-end e instale as dependências:
```bash
cd frontend
npm install
````

##### Compilação e Deploy do Front-end
Para compilar o projeto para produção, execute o comando:
```bash
npm run build
````

Os arquivos compilados estarão na pasta dist. Para servir os arquivos estáticos, você pode utilizar servidores como Nginx ou Apache.

##### Configuração de Servidor Web (Nginx no Linux)
Edite o arquivo de configuração do Nginx para servir a aplicação:
```bash
sudo nano /etc/nginx/sites-available/default
````
Adicione a seguinte configuração:
```bash
server {
    listen 80;
    server_name your-domain.com;
    root /path/to/frontend/dist;
    index index.html;

    location / {
        try_files $uri $uri/ /index.html;
    }
}

````
Reinicie o Nginx:
```bash
sudo systemctl restart nginx
````
##### Configuração de Servidor Web (Windows)
No Windows, você pode utilizar servidores como XAMPP ou IIS para servir os arquivos estáticos da pasta dist.


### 3.2 Back-end

#### Instalação do Java e Maven

##### Linux

No \*\*Linux\*\*, você pode instalar o \*\*Java\*\* e o \*\*Maven\*\* com os seguintes comandos:

```bash

sudo apt update

sudo apt install openjdk-11-jdk

sudo apt install maven
````

##### Windows

No Windows, siga os passos abaixo:

- Instalação do Java: Baixe e instale o Java 11 (ou superior) diretamente do site oficial da Oracle: https://www.oracle.com/java/technologies/javase-jdk11-downloads.html.

- Instalação do Maven: Baixe o Apache Maven do site oficial: https://maven.apache.org/download.cgi. Extraia o arquivo ZIP e adicione o caminho do Maven (bin) nas variáveis de ambiente do sistema PATH.

- Para verificar se o Java e o Maven foram instalados corretamente, use os comandos:

````bash
java -version

mvn -version
````

Esses comandos devem exibir a versão do Java e do Maven instalados.

##### Configuração do Banco de Dados

##### Linux

No Linux, o PostgreSQL pode ser instalado com os seguintes comandos:

````bash

sudo apt update

sudo apt install postgresql postgresql-contrib
````

Após a instalação, crie o banco de dados e configure um usuário para a aplicação. Acesse o shell do PostgreSQL:

````bash
sudo -u postgres psql
````

Dentro do psql, execute os comandos abaixo para criar o banco de dados e o usuário:


````sql
CREATE DATABASE nome\_do\_banco;

CREATE USER nome\_do\_usuario WITH PASSWORD 'senha';

GRANT ALL PRIVILEGES ON DATABASE nome\_do\_banco TO nome\_do\_usuario;
````


#### Windows

No Windows, baixe e instale o PostgreSQL através do instalador oficial disponível em: https://www.postgresql.org/download/.

Após a instalação, você pode usar o pgAdmin ou o terminal psql (disponível no instalador) para criar o banco de dados e o usuário:

````sql
CREATE DATABASE nome\_do\_banco;

CREATE USER nome\_do\_usuario WITH PASSWORD 'senha';

GRANT ALL PRIVILEGES ON DATABASE nome\_do\_banco TO nome\_do\_usuario;
````

#### Configuração do Flyway

Flyway é usado para gerenciar as migrações de banco de dados de forma automática. Para configurá-lo, edite o arquivo application.properties ou application.yml no projeto Spring Boot, incluindo as informações de conexão ao banco de dados:

````properties
spring.datasource.url=jdbc:postgresql://localhost:5432/nome\_do\_banco

spring.datasource.username=nome\_do\_usuario

spring.datasource.password=senha

spring.jpa.hibernate.ddl-auto=none
````

#### Compilação e Execução do Back-end

#### Linux e Windows

No Linux e no Windows, a compilação e execução do projeto Spring Boot seguem as mesmas etapas. Navegue até o diretório do projeto back-end e execute os seguintes comandos:

#### Limpar e instalar dependências:

````bash

cd backend

mvn clean install
````

#### Executar a aplicação com o Spring Boot:

````bash

mvn spring-boot:run
````

Isso iniciará a aplicação e a API estará disponível na porta padrão 8080 (a menos que configurada de outra forma).

#### Integração com o Banco de Dados

Para garantir que o PostgreSQL está integrado corretamente com a aplicação, verifique o arquivo de log do Spring Boot. Ele deve indicar uma conexão bem-sucedida com o banco de dados. Qualquer erro relacionado à conexão será exibido no log do terminal.

#### Testando a API

Utilize ferramentas como Postman ou Insomnia para testar as rotas da API, fazendo requisições HTTP (GET, POST, PUT, DELETE) para garantir que o back-end está processando corretamente as informações e interagindo com o banco de dados.


