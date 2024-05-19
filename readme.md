# Exercise 1: BINFO-CEP numberle

This is a JSF implementation of numberle game.

- ## Game rules

  The goal is to guess the six digit number within six attepts. After each check the number is validated and each digit box is colored according to fllowing rules:

  1. GREEN - the digit is corect and it right position.
  2. RED - the digit is not present in the solution.
  3. BLUE - digit is pressent in the solution multiple times but the cuurent one is in wrong position, the GREEN ones are omitted if any.
  4. ORANGE - digit is present in the solution once but the real position is on the right side.
  5. YELLOW - digit is present in the solution once but the real position is on the left side.
     > Rules applied in the specified order, so valid digits are excluded for the following colored hints.

- ## Structure

The game logic is defined in the named bean `./src/main/java/lu/uni/numberle/GameBean.java` and it is using `./ejb/SolutionGeneratorBean.java` to generate a random number with a specified size, it is reading a random line from file `./webapp/resources/numbers.txt` and append extra digits if the requested size is grater.

The web flow is defined in `./src/main/webapp/WEB-INF/faces-config.xml` and the pages can be found under `./src/main/webapp/` folder.

> \*The solution can be extended to support custom number size and custom number of attempts ( see TODO: `./src/main/java/lu/uni/numberle/GameBean.java`)

## Run the application

The application was testes with JDK 22, maven 3.9.6 and provided wildfly docker application server

### Prerequisites

- maven 3.9.6 [https://maven.apache.org](https://maven.apache.org)
- jdk 21 [https://www.oracle.com/java/technologies/downloads/#java21](https://www.oracle.com/java/technologies/downloads/#java22)
- wildfly application server

### Build and deploy

- Build `$ mvn clean package wildfly:deploy`
- Open [http://0.0.0.0:8080/numberle/](http://0.0.0.0:8080/numberle/)
