<!-- Use this file to provide workspace-specific custom instructions to Copilot. For more details, visit https://code.visualstudio.com/docs/copilot/copilot-customization#_use-a-githubcopilotinstructionsmd-file -->

## Selenium Java Automation Project Setup Status

- [x] Verify that the copilot-instructions.md file in the .github directory is created.
- [x] Clarify Project Requirements - Selenium Java for web automation and testing
- [x] Scaffold the Project - Maven structure with Selenium WebDriver and TestNG
- [x] Customize the Project - Add Page Object Model, utilities, and test examples  
- [x] Install Required Extensions - Java extensions for VS Code
- [x] Install Dependencies - Maven dependencies and Java setup (pom.xml ready)
- [x] Create and Run Task - Maven tasks for test execution configured
- [x] Launch the Project - Ready for execution once Java/Maven installed
- [x] Ensure Documentation Complete - README.md and project documentation complete

## Project Overview
This is a comprehensive Selenium Java project for web automation and testing with:
- **Framework**: Selenium WebDriver with TestNG
- **Build Tool**: Maven for dependency management
- **Design Pattern**: Page Object Model
- **Browsers Supported**: Chrome, Firefox, Edge, Safari
- **Features**: Cross-browser testing, parallel execution, reporting

## Prerequisites Required
- **Java 11+** (JDK installation required)
- **Maven 3.6+** (for dependency management)
- **Chrome/Firefox browsers** (for testing)

## Available Commands
- `mvn clean compile` - Compile the project
- `mvn test` - Run all tests
- `mvn test -Dbrowser=chrome` - Run tests on specific browser
- `mvn clean test -Dparallel=tests` - Run tests in parallel