### HTMLDiff: HTML Difference Tool

**HTMLDiff** compares two HTML files, highlighting inserted and deleted content. 
It shows both the original and revised HTML for easy comparison.

### Prerequisites:
- Java 21+
- Docker & Docker Compose

### How to Build and Run:

1. Clone the Repository:
   ```
   git clone https://github.com/krystianslowik/kotlin-htmldiff
   cd kotlin-htmldiff
   ```

2. Build the Application:
   ```
   ./gradlew bootJar
   ```

3. Run Locally:
   ```
   java -jar build/libs/htmldiff-0.0.1-SNAPSHOT.jar
   ```

4. OR Run with Docker:
   ```
   docker-compose build
   docker-compose up
    ```
   Access at http://localhost:8080.