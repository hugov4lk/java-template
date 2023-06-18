# Running java-template
 
## Local development (database only):

 - `docker-compose -f docker-compose-db.yml up -d` 
 - Run the app locally from IDE or `./gradlew bootRun`
 - Stop the database after development `docker-compose -f docker-compose-db.yml down`

## Application with docker - http://localhost:8080/

 - `docker-compose up -d`
 - `docker-compose down`