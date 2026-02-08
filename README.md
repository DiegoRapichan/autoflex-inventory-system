# 🏭 Autoflex Inventory Management System

Sistema completo de gerenciamento de estoque de matérias-primas e sugestão inteligente de produção baseada em disponibilidade e valor dos produtos.

[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=flat&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-6DB33F?style=flat&logo=spring&logoColor=white)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18-61DAFB?style=flat&logo=react&logoColor=white)](https://react.dev/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-18-336791?style=flat&logo=postgresql&logoColor=white)](https://www.postgresql.org/)

---

## 📋 Sobre o Projeto

Sistema web desenvolvido para controlar o estoque de matérias-primas e calcular automaticamente quais produtos podem ser produzidos com base no estoque disponível, priorizando produtos de maior valor.

**Desenvolvido para:** Teste Prático Autoflex

---

## ✨ Funcionalidades

### Backend (API REST)

- ✅ CRUD completo de **Produtos**
- ✅ CRUD completo de **Matérias-Primas**
- ✅ **Associação Produto-Matéria Prima** com quantidades necessárias
- ✅ **Algoritmo inteligente de sugestão de produção**
  - Calcula quantidade máxima produzível baseada no estoque
  - Prioriza produtos de maior valor
  - Identifica materiais limitantes
  - Calcula valor total de produção

### Frontend (React SPA)

- ✅ Interface moderna e responsiva (Tailwind CSS)
- ✅ Gerenciamento de estado com Redux Toolkit
- ✅ Dashboard com estatísticas em tempo real
- ✅ Visualização de sugestões de produção com detalhes
- ✅ Indicadores visuais de estoque (verde/amarelo/vermelho)
- ✅ Notificações toast para feedback do usuário

### Qualidade & Segurança

- ✅ Documentação Swagger/OpenAPI interativa
- ✅ Validação de dados (backend e frontend)
- ✅ Tratamento robusto de exceções
- ✅ CORS configurado
- ✅ Código limpo seguindo princípios SOLID

---

## 🛠️ Tecnologias Utilizadas

### Backend

- **Java 17** - Linguagem de programação
- **Spring Boot 3.2** - Framework backend
- **Spring Data JPA** - ORM/Persistência
- **PostgreSQL 18** - Banco de dados relacional
- **Maven** - Gerenciamento de dependências
- **Lombok** - Redução de boilerplate
- **Swagger/OpenAPI 3** - Documentação da API

### Frontend

- **React 18** - Biblioteca UI
- **Redux Toolkit** - Gerenciamento de estado
- **React Router 6** - Navegação SPA
- **Axios** - Cliente HTTP
- **Tailwind CSS** - Framework CSS
- **Vite** - Build tool
- **React Toastify** - Notificações

---

## 📦 Pré-requisitos

- Java 25 ou superior
- Node.js 18+ e npm
- PostgreSQL 18+
- Maven 3.8+ (ou usar o wrapper incluído)
- Git

---

## 🚀 Como Executar

### 1. Clone o repositório

```bash
git clone https://github.com/DiegoRapichan/autoflex-inventory-system.git
cd autoflex-inventory-system
```

### 2. Configure o PostgreSQL

```sql
CREATE DATABASE autoflex;
CREATE USER autoflex WITH PASSWORD 'autoflex123';
GRANT ALL PRIVILEGES ON DATABASE autoflex TO autoflex;
ALTER DATABASE autoflex OWNER TO autoflex;
```

### 3. Execute o Backend

```bash
cd autoflex-backend

# Compilar
mvnw clean install

# Executar
mvnw spring-boot:run

# Backend rodará em http://localhost:8080
# Swagger UI: http://localhost:8080/swagger-ui.html
```

### 4. Execute o Frontend

```bash
cd autoflex-frontend

# Instalar dependências
npm install

# Executar
npm run dev

# Frontend rodará em http://localhost:5173
```

---

## 📚 Documentação da API

### Endpoints Principais

**Base URL:** `http://localhost:8080/api`

#### Products

```
GET    /products              - Listar todos os produtos
GET    /products/{id}         - Buscar produto por ID
POST   /products              - Criar novo produto
PUT    /products/{id}         - Atualizar produto
DELETE /products/{id}         - Deletar produto
GET    /products/{id}/materials - Listar matérias-primas do produto
POST   /products/{id}/materials - Adicionar matéria-prima ao produto
```

#### Raw Materials

```
GET    /raw-materials         - Listar todas as matérias-primas
GET    /raw-materials/{id}    - Buscar matéria-prima por ID
POST   /raw-materials         - Criar nova matéria-prima
PUT    /raw-materials/{id}    - Atualizar matéria-prima
DELETE /raw-materials/{id}    - Deletar matéria-prima
```

#### Production Suggestions

```
GET    /production/suggestions - Calcular sugestões de produção
```

**Documentação Interativa:** http://localhost:8080/swagger-ui.html

---

## 🗂️ Estrutura do Projeto

```
autoflex-inventory-system/
├── autoflex-backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/autoflex/inventory/
│   │   │   │   ├── config/          # Configurações (CORS, Swagger)
│   │   │   │   ├── controller/      # Controllers REST
│   │   │   │   ├── dto/             # Data Transfer Objects
│   │   │   │   ├── entity/          # Entidades JPA
│   │   │   │   ├── exception/       # Exception Handlers
│   │   │   │   ├── repository/      # Repositories
│   │   │   │   └── service/         # Lógica de negócio
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   └── pom.xml
│
└── autoflex-frontend/
    ├── src/
    │   ├── api/                     # Chamadas API
    │   ├── components/              # Componentes React
    │   ├── pages/                   # Páginas
    │   ├── store/                   # Redux Store
    │   ├── utils/                   # Utilitários
    │   └── App.jsx
    ├── package.json
    └── vite.config.js
```

---

## 🧪 Testes

### Backend

```bash
cd autoflex-backend
mvn test
```

### Frontend

```bash
cd autoflex-frontend
npm run test
```

---

## 🎯 Algoritmo de Sugestão de Produção

O sistema implementa um algoritmo inteligente que:

1. **Busca todos os produtos** ordenados por valor (maior primeiro)
2. **Para cada produto**, calcula a quantidade máxima produzível baseada em:
   - Quantidade disponível de cada matéria-prima
   - Quantidade necessária por unidade de produto
3. **Determina o limitante**: A matéria-prima que permite produzir menos unidades
4. **Calcula o valor total** da produção sugerida
5. **Retorna a lista** ordenada por valor de produção

**Complexidade:** O(n × m) onde n = número de produtos, m = matérias-primas por produto

**Exemplo:**

```
Produto: Cadeira (Valor: R$ 150,00)
Matérias-primas:
- Madeira: 2.5 KG necessário | 100 KG em estoque → 40 unidades possíveis
- Parafuso: 8 UN necessário | 200 UN em estoque → 25 unidades possíveis
- Verniz: 0.3 L necessário | 5 L em estoque → 16 unidades possíveis

Quantidade máxima = MIN(40, 25, 16) = 16 unidades
Valor total = 16 × R$ 150,00 = R$ 2.400,00
Material limitante: Verniz
```

---

## 📸 Screenshots

### Dashboard

![Dashboard](./docs/screenshots/dashboard.png)

### Gestão de Produtos

![Products](./docs/screenshots/products.png)

### Sugestão de Produção

![Production](./docs/screenshots/production.png)

---

## 📋 Checklist de Requisitos

### Requisitos Não Funcionais

- [x] RNF001 - Plataforma WEB (Chrome, Firefox, Edge)
- [x] RNF002 - API separada do front-end
- [x] RNF003 - Responsividade
- [x] RNF004 - PostgreSQL
- [x] RNF005 - Framework backend (Spring Boot)
- [x] RNF006 - React + Redux
- [x] RNF007 - Código em inglês

### Requisitos Funcionais

- [x] RF001 - CRUD Produtos (Backend)
- [x] RF002 - CRUD Matérias-Primas (Backend)
- [x] RF003 - CRUD Associações (Backend)
- [x] RF004 - Consulta de Produção (Backend)
- [x] RF005 - Interface CRUD Produtos
- [x] RF006 - Interface CRUD Matérias-Primas
- [x] RF007 - Interface Associações
- [x] RF008 - Interface Sugestão de Produção

### Desejáveis

- [x] Testes unitários Backend
- [x] Testes unitários Frontend
- [x] Documentação completa

---

## 👨‍💻 Autor

**Diego Rapichan**

- GitHub: [@DiegoRapichan](https://github.com/DiegoRapichan)
- LinkedIn: [Diego Rapichan](https://linkedin.com/in/diego-rapichan)
- Email: direrapichan@gmail.com

---

## 📝 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

## 🙏 Agradecimentos

Projeto desenvolvido para o teste prático da **Autoflex**. Agradeço pela oportunidade de demonstrar minhas habilidades técnicas.

---

<div align="center">
  
**⭐ Se este projeto foi útil, considere dar uma estrela!**

Desenvolvido por Diego Rapichan

</div>
