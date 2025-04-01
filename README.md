# incode-assignment

## Prerequisites
Before running this application, ensure you have the following installed:
- [Java 21](https://jdk.java.net/21/)
- [Docker](https://www.docker.com/get-started)
- [Maven](https://maven.apache.org/download.cgi)

## Running the Application

### 1. Build the Application
Run the following command to package the application using Maven:
```sh
mvn clean package
```
This will generate a JAR file in the `target/` directory.

### 2. Start the Application with Docker Compose
Use the provided `docker-compose.yml` file to run the application:
```sh
docker-compose up --build
```
This will:
- Start a PostgreSQL database container
- Build and run the Spring Boot application

### 3. Access the Application
Once the containers are up, the application will be available at:
```
http://localhost:8080
```

## Example Usage
### Transform Input
**POST** `localhost:8080/api/transform`
```json
{
    "input": "TeSt1",
    "transformers": [
        {
            "transformerType": "UPPERCASE"
        },
        {
            "transformerType": "REGEX_REMOVE",
            "regex": "[A-Z]"
        },
        {
            "transformerType": "LOWERCASE"
        }
    ]
}
```

### Fetch Transformations
**GET** `localhost:8080/api/transformations?from=2025-03-28T14:00:00&to=2025-12-31T23:59:59`

### Download Report
**GET** `localhost:8080/report/download?format=CSV`

## Stopping the Application
To stop and remove the running containers, use:
```sh
docker-compose down
```

## Notes
- Ensure that port `8080` and `5433` are not in use before running the application.
- Database data will persist across container restarts using a Docker volume.

For any issues, check the logs using:
```sh
docker-compose logs -f
```
