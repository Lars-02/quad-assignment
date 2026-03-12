# Open Trivia Challenge - Backend

This is the Java Spring Boot backend for the Open Trivia Challenge. It acts as a secure intermediary between the
frontend application and the Open Trivia Database ([OpenTDB](https://opentdb.com/api_config.php)).

This ensures that players cannot cheat by inspecting the network traffic, as the correct answers are stripped out and
kept in the server's memory until the answers are submitted for grading.

Since the requirements stated no database is required all game data is lost when restarting the server.

## Technologies Used

* **Language:** Java 21
* **Framework:** Spring Boot (Web)
* **Build Tool:** Gradle

## Prerequisites

To run this project, you need to have the following installed:

* [Java Development Kit (JDK) 21](https://adoptium.net/)

## Running the web application

```sh
./gradlew bootRun
```