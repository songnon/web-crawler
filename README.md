# web-crawler
### Instructions
Run the project using "mvn spring-boot:run" or build the JAR file using "mvn clean package"
and run the JAR file "java -jar target/web-crawler-0.0.1-SNAPSHOT.jar"

Parameters:
- url: the web url to be crawl.
- depth: (Optional) specify the depth to crawl. Default depth is 1; Maximum depth is 5;

### Examples
#### Via browser
- `http://localhost:8080/crawler?url=http://www.google.com`

- `http://localhost:8080/crawler?url=http://www.google.com&depth=0`

#### Using curl in command line
- `curl localhost:8080/crawler?url=http://www.qantas.com/`

- `curl localhost:8080/crawler?url=http://www.qantas.com/ | python -m json.tool`

### Assumptions
The url page should be in HTML.
