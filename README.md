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



# Test Flow:

#### 1. PropertyCreationTests:

   Valid Input: Tests creating a property with valid input and checks that the response has a status of 201 (Created).
   Invalid Format/Missing Fields: Tests creating a property with missing or invalid fields, expecting a 400 Bad Request.
   Unauthorized Access: Tests unauthorized access by omitting required credentials, expecting 401 Unauthorized.
   Internal Error: Simulates an internal server error during property creation, expecting a 500 Internal Server Error.

#### 2. PropertyRetrievalTests:

   Valid ID: Retrieves a property using a valid property ID and verifies the response's accuracy (including property ID, alias, and date).
   Invalid ID: Retrieves a property with an invalid ID, expecting a 204 No Content status.

#### 3. BookingCreationTests:

   Create Booking with Valid Property: Attempts to create a booking with a valid property ID. It checks if booking dates overlap with existing bookings and ensures no conflicts.
   Date Overlap Check: Handles overlap by generating random booking dates until valid dates are found that do not overlap with existing bookings.
#### 4.   ErrorHandlingTests
#####    Test Bad Request:

This test simulates sending a POST request with missing or invalid parameters.
It verifies that the response status code is 400 (Bad Request) and checks the error details, such as "Validation Error" and "Validation failed."
The response error message is also printed for debugging purposes.

##### Test Unauthorized Request:

This test simulates sending a GET request without providing a required authentication token.
It checks that the response status is 401 (Unauthorized) and validates the error message, which should indicate authentication failure.
The error details, including "Unauthorized" and the authentication error message, are printed.
Test Forbidden Request (Invalid Credentials):

This test sends a GET request with invalid credentials (username and password).
It ensures the response status is 401 (Unauthorized) and validates the error details, including "Bad credentials" and "Unauthorized" messages.
The error response is printed for reference.

##### Test Not Found:

This test simulates sending a GET request to an invalid endpoint (non-existing URL).
It expects a 404 (Not Found) status and verifies that the response contains the "Not Found" error message.
The error response is printed for visibility.

##### Test Method Not Allowed (Unsupported Media Type):

This test sends a POST request with an invalid content type (text/plain).
It checks the response status code, which should be 415 (Unsupported Media Type), and validates the error details, such as unsupported content type information.
The response body is printed for further inspection.

##### Test Internal Server Error:

This test sends a POST request with an intentionally erroneous request body to simulate a server-side issue.
It verifies that the response status is 500 (Internal Server Error) and checks the error details.
The response body is printed to confirm the error details and the server failure.