# Hostfully
### **QA Technical Sheet**

## **Steps to Execute the Test**

To execute the test suite, follow these steps:

Using Maven:
Clone the repository:

bash
Copy
Edit
git clone <repository-url>
Navigate to the project directory:

bash
Copy
Edit
cd <project-directory>
Ensure all dependencies are installed by running:

bash
Copy
Edit
mvn clean install
Execute the tests with the following command:

bash
Copy
Edit
mvn test



## **Dependencies or Libraries Required**

JUnit 5: The test suite uses JUnit 5 for unit testing. The dependencies for JUnit 5 are included in the pom.xml (for Maven) or build.gradle (for Gradle).

JUnit Platform: Required to run the tests on the JUnit 5 platform.





 <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.20</version>
        </dependency>

        <dependency>
            <groupId>com.github.javafaker</groupId>
            <artifactId>javafaker</artifactId>
            <version>1.0.2</version>
        </dependency>

        <dependency>
            <groupId>io.rest-assured</groupId>
            <artifactId>rest-assured</artifactId>
            <version>4.4.0</version>
        </dependency>
        <dependency>
            <groupId>com.codeborne</groupId>
            <artifactId>selenide-testng</artifactId>
            <version>7.2.3</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>io.qameta.allure</groupId>
            <artifactId>allure-selenide</artifactId>
            <version>${allure.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.hamcrest</groupId>
            <artifactId>hamcrest</artifactId>
            <version>2.2</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.assertj</groupId>
            <artifactId>assertj-core</artifactId>
            <version>3.25.3</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>5.7.2</version>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-simple</artifactId>
            <version>2.0.13</version>
            <scope>test</scope>
        </dependency>



## **Structure of the Test Cases**

The test cases are organized as follows:

Test Classes:

Each test class represents a specific feature or module in the application.
Each class contains one or more test methods that test individual functions or scenarios.
Test Method Order:

The order of execution for test methods is controlled using the @Order annotation in JUnit 5.
Methods are executed in ascending order of the @Order value.
Test Assertions:

Each test method contains assertions that check the correctness of the application functionality.
Common assertions include assertEquals(), assertTrue(), and assertFalse().