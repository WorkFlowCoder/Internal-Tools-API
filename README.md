# Internal-Tools-API
Cette API REST permet de gérer les outils SaaS internes d’une entreprise (ex : Slack, GitHub). Elle permet de lister, consulter, ajouter et modifier les outils avec des filtres, des validations et une gestion des erreurs. Son but est de centraliser les informations sur les outils, leurs coûts et leur usage pour faciliter la gestion interne.

## Technologies
Langage : Java 17
Framework : Spring Boot 4.0.3
Base de données : MySQL
Port API : 8085
Documentation : OpenAPI / Swagger UI (accessible sur /api/docs a venir)

## Quick Start

1. (sudo) `docker-compose --profile mysql up -d`

2. 'sudo apt install maven'
3. './mvnw spring-boot:run'
4. API disponible sur http://localhost:8085
5. Documentation: http://localhost:[port]/[chemin_docs]

## Configuration
- Variables d'environnement: voir .env.example
- Configuration DB: [instructions_connexion]

## Tests  
[commande_lancement_tests] - Tests unitaires + intégration

## Architecture
- [Justification_choix_tech]
- [Structure_projet_expliquee]