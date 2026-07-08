# 🛰️ Monitor System API

Sistema de monitoramento de dispositivos de segurança (sensores e câmeras), com autenticação via JWT e um dashboard em React para gerenciamento em tempo real. Desenvolvido como projeto de estudo aprofundado em back-end com Spring Security, JPA e boas práticas de arquitetura em camadas.

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![React](https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB)
![TypeScript](https://img.shields.io/badge/TypeScript-3178C6?style=for-the-badge&logo=typescript&logoColor=white)

---

## 📋 Sobre o projeto

A API permite cadastrar, listar, atualizar e remover dispositivos de segurança (câmeras e sensores), acompanhar o status (online/offline) de cada um e manter um histórico de logs de ativação/alteração. O acesso é protegido por autenticação **JWT**, com diferentes níveis de permissão (`ADMIN` e `USER`).

O front-end (`monitor-system-ui`), em React + TypeScript, consome a API para exibir um dashboard com login, cadastro/edição de dispositivos e visualização de logs.

## ✨ Funcionalidades

- Autenticação e registro de usuários com JWT e senhas criptografadas (BCrypt)
- Controle de acesso por papel (`ADMIN` / `USER`)
- CRUD completo de dispositivos (câmeras e sensores)
- Atualização de status (online/offline) por dispositivo
- Listagem de dispositivos offline
- Histórico de logs por dispositivo
- Documentação interativa da API via Swagger/OpenAPI
- Dashboard web (React) para gerenciamento visual

## 🛠️ Tecnologias

**Back-end**
- Java 21
- Spring Boot
- Spring Data JPA
- Spring Security + JWT (`java-jwt`)
- PostgreSQL (produção) / H2 (console de desenvolvimento)
- Lombok
- Bean Validation
- SpringDoc OpenAPI (Swagger)

**Front-end**
- React 19
- TypeScript
- Vite

## 🚀 Como executar

### Pré-requisitos
- Java 21+
- Maven
- PostgreSQL rodando localmente (ou ajuste para outro banco)
- Node.js 18+ (para o front-end)

### Back-end

```bash
# Clone o repositório
git clone https://github.com/yanborgess/monitor-system-api.git
cd monitor-system-api

# Configure o banco de dados
cp src/main/resources/application.properties.example src/main/resources/application.properties
# edite src/main/resources/application.properties com sua URL, usuário, senha e chave secreta

# Rode a aplicação
./mvnw spring-boot:run
```

A API sobe por padrão em `http://localhost:8080`.
A documentação Swagger fica disponível em `http://localhost:8080/swagger-ui.html`.

### Front-end

```bash
cd monitor-system-ui
npm install
npm run dev
```

O dashboard sobe por padrão em `http://localhost:5173`.

## 📡 Principais endpoints

| Método | Rota | Descrição |
|---|---|---|
| POST | `/auth/register` | Cadastra um novo usuário |
| POST | `/auth/login` | Autentica e retorna um token JWT |
| POST | `/api/devices` | Cadastra um novo dispositivo |
| GET | `/api/devices` | Lista todos os dispositivos |
| GET | `/api/devices/{id}` | Busca um dispositivo por ID |
| PUT | `/api/devices/{id}` | Atualiza um dispositivo |
| DELETE | `/api/devices/{id}` | Remove um dispositivo |
| PATCH | `/api/devices/{id}/status` | Atualiza o status (online/offline) |
| GET | `/api/devices/offline` | Lista dispositivos offline |
| GET | `/api/devices/logs` | Lista o histórico de logs |

## 🗂️ Estrutura do projeto

```
monitor-system-api/
├── src/main/java/com/yan/security/monitorSystem/
│   ├── controllers/       # Endpoints REST (Device, Authentication)
│   ├── infra/security/    # Configuração de segurança e JWT
│   ├── models/            # Entidades JPA (Device, DeviceLog, User)
│   ├── repositories/      # Interfaces Spring Data JPA
│   └── services/          # Regras de negócio
└── monitor-system-ui/     # Front-end React + TypeScript
```

## 👤 Autor

**Yan Borges**
[GitHub](https://github.com/yanborgess) · [LinkedIn](https://www.linkedin.com/in/yan-borges-areda/)
