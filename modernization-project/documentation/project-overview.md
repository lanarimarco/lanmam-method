# RPGLE to Java Modernization Project

## Overview
This project modernizes 50 RPGLE programs to Java using AI-assisted conversion.

## Project Structure
- `/agents/` - Configuration for each specialized AI agent
- `/common-patterns/` - Reusable patterns and knowledge base
- `/source-rpgle/` - Original RPGLE source code
- `/work-in-progress/` - Active conversion work
- `/final-output/` - Completed Java application
- `/documentation/` - Project documentation
- `/scripts/` - Automation scripts

## Conversion Workflow
1. Analysis: Understand RPGLE program
2. Database: Create JPA entities
3. Conversion: Convert to Java services
4. UI: Create React interfaces
5. Testing: Build test suites
6. Review: Quality assurance
7. Integration: Assemble final application

## Technology Stack
- Java 17+
- Spring Boot 3.x
- JPA/Hibernate
- React with TypeScript
- JUnit 5
- Maven

## Getting Started
1. Place RPGLE sources in `/source-rpgle/`
2. Run analysis: `./scripts/run-analysis.sh PROGRAM_NAME`
3. Follow the agent workflow
4. Review outputs in `/work-in-progress/`


