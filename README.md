# Internal-Tools-API
Cette API REST permet de gérer les outils SaaS internes d’une entreprise (ex : Slack, GitHub). Elle permet de lister, consulter, ajouter et modifier les outils avec des filtres, des validations et une gestion des erreurs. Son but est de centraliser les informations sur les outils, leurs coûts et leur usage pour faciliter la gestion interne.

## Technologies

- Langage : Java 17
- Framework : Spring Boot 4.0.3
- Base de données : MySQL
- Port API : 8085
- Documentation : Swagger UI

## Quick Start

1. (sudo) `docker-compose --profile mysql up -d`

2. `sudo apt install maven`

3. `./mvnw spring-boot:run`

4. API disponible sur http://localhost:8085

5. Documentation: http://localhost:8085/api/swagger-ui/index.html

## Tests  

Afin de lancer les test, il faut utiliser :
```bash
mvn test
```

## Endpoints Principaux

- **GET - /api/tools** Liste les outils (avec filtres & pagination)
- **GET - /api/tools/{id}** Détails complets d'un outil
- **POST - /api/tools** Ajouter un nouveau tool
- **PUT - /api/tools/{id}** Modifier un tool existant

## Architecture

Le projet suit l'architecture suivante pour séparer les responsabilités :

- ***controller :** Point d'entrée API et validation des requêtes.
- **service :** Logique métier, calculs et règles de gestion.
- **repository :** Accès à la base de données via des interfaces JPA.
- **entity :** Modèles de données (MySQL).
- **dto :** Objets de transfert de données pour sécuriser et filtrer les données.
- **exception :** Gestionnaire d'erreurs.

### Choix de l'architecture

Utilisation de Spring Boot pour sa robustess et sa performance. Permet une gestion fiable et simple des données relationnelles et requêtes simplifiées.