# Livraria API

API REST para gerenciamento de livros, desenvolvida com **Java + Spring Boot**.  

---

## 🚀 Stack Tecnológica

- **Java 17+**
- **Spring Boot 3.2+**
- **Spring Data JPA**
- **H2 Database** 
- **JUnit 5 & Mockito**
- **Maven** 
  
---

## ⚠️ Pré-requisitos

Certifique-se de ter instalado:

- **Java 17 ou superior**
- **Maven 3.9+** (ou Gradle)
- Editor recomendado: IntelliJ IDEA / Eclipse


## 🔧 Instalação
```bash
**Clone o repositório**
- git clone https://github.com/gabrielsilva15/crud-livraria.git

**Entre na pasta**
- cd crud-livraria

**Instale as dependências usando o maven**
- mvn clean install
OBS: Se sua IDE é o IntelliJ basta dar Run na aplicação, se não, então é necessário a instalação do Maven na sua máquina
```


## ▶️ Execute a aplicação
```bash
- mvn spring-boot:run ou executar a aplicação diretamente pela IDE
```
Acesse a aplicação na Url: 

🔗 http://localhost:8080/

Banco de Dados:
🔗 http://localhost:8080/h2-console

Dados para acessar o BD:
 - JDBC URL: jdbc:h2:mem:livrariadb
 - User Name: sa
 - password: 1234

Após informar esses dados clique em connect e vai logar no banco de dados

## 🌐 Endpoints da API

URL: http://localhost:8080

**Criar livro**
```bash
POST /livros
```

**Buscar livro por ID**
```bash
GET /livros/{id}
```

**Buscar livro por ISBN**
```bash
GET /livros/isbn/{isbn}
```


**Buscar todos os livros**
```bash
GET /livros
```

**Atualizar um livro**
```bash
PUT /livros/{id}
```

**Deletar um livro**
```bash
DELETE /livros/{id}
```

## 🧪 Testes 

```bash
Para executar os testes basta digitar no seu terminal: mvn test
Caso queira usar a IDE, basta executar o arquivo de teste do servico de Livro
```







