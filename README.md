RESUMO:
1- Configure o SQL, pode utilizar o arquivo dump ou o bancomalsql, mas atente-se ao nome do banco 

2- Aplique a configuração java

Participação:
- Matheus Alves Abreu
- Thiago dos Santos Lima
- Wanderson Mateus Borges da Silva

Passo a passo:
# Banco Malvader 🏦

Sistema bancário desenvolvido em **Java (Swing + JDBC)** com persistência de dados em **MySQL**.

---

## 📂 Estrutura do Projeto

- `/src` → Código-fonte Java (organizado em `model`, `dao`, `view`, `util`)
- `/lib` → Dependências externas (ex.: `mysql-connector-j-9.3.0.jar`)
- `/sql/bancomalvader.sql` → Dump completo do banco de dados MySQL (estrutura, dados, procedures, triggers e views)

---

## 🚀 Como executar o projeto

### 1️⃣ Pré-requisitos

- **Java 17** (ou compatível)
- **Eclipse IDE** (ou outra IDE de sua preferência)
- **MySQL Server 8.0 ou superior**
- **MySQL Workbench** (recomendado para importar o banco)

---

### 2️⃣ Configurar o Banco de Dados

- Abra o **MySQL Workbench**
- Vá em `Server > Data Import`
- Selecione a opção:  
  **Import from Self-Contained File**
- Escolha o arquivo:

/sql/bancomalvaderdump.sql


- Execute a importação.

> ✅ Isso criará automaticamente o banco `banco_malvader` com todas as tabelas, dados de exemplo, procedures, triggers e views.

---

### 3️⃣ Configurar o projeto Java

- Abra o **Eclipse**
- Importe o projeto:
File > Import > Existing Projects into Workspace

- Adicione o conector JDBC:
- Clique com o botão direito no projeto
- `Build Path > Configure Build Path > Libraries > Add External JARs`
- Selecione o arquivo `mysql-connector-j-9.3.0.jar` dentro de `/lib`

---

### 4️⃣ Configurar conexão (caso necessário)

Verifique na classe:

src/util/Conexao.java


- Ajuste o usuário e senha de acesso ao seu MySQL:

```java
private static final String URL = "jdbc:mysql://localhost:3306/banco_malvader";
private static final String USUARIO = "root";
private static final String SENHA = "sua_senha_aqui";

Rode a classe TelaLogin.java para iniciar o sistema.
Funcionario:
CPF: 00000000011
Senha: 123456
Cliente:
CPF: 12345678900
Senha: 123456

