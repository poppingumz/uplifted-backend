# UpliftEd - Online Learning Platform

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0-green.svg)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18-blue.svg)](https://reactjs.org/)
[![Docker](https://img.shields.io/badge/Docker-Enabled-blue.svg)](https://www.docker.com/)

**UpliftEd** is a full-stack Learning Management System (LMS) designed to bridge the gap between content delivery and student engagement. It enables instructors to create dynamic courses and assessments while providing students with real-time feedback and progress tracking.

> **Context:** Developed as a Semester 3 Software Engineering project at Fontys University of Applied Sciences.

---

## Table of Contents
- [Key Features](#-key-features)
- [Technology Stack](#-technology-stack)
- [System Architecture](#-system-architecture)
- [Getting Started](#-getting-started)
- [Testing](#-testing)
- [Contact](#-contact)

---

## Key Features

### For Students
* **Course Enrollment:** Browse and enroll in available courses with a single click.
* **Interactive Quizzes:** Take assessments with automated grading and instant result feedback.
* **Real-Time Notifications:** Receive instant alerts for new assignments via **WebSocket** integration.
* **Progress Tracking:** View grades and completion status on a personal dashboard.

### For Teachers
* **Course Management:** Create, update, and delete courses and learning materials (PDFs, Videos).
* **Quiz Creation:** Design multiple-choice quizzes with defined passing criteria.
* **Student Monitoring:** Track enrollment statistics and quiz performance.

### Security & Infrastructure
* **Role-Based Access Control (RBAC):** Strict separation of `STUDENT` and `TEACHER` permissions.
* **Secure Authentication:** Stateless session management using **JWT (JSON Web Tokens)**.
* **Data Integrity:** Passwords hashed using **BCrypt**; inputs validated to prevent injection attacks.

---

## Technology Stack

| Area | Technology |
| :--- | :--- |
| **Backend** | Java 17, Spring Boot (Web, Security, Data JPA, WebSocket) |
| **Frontend** | React.js, Axios, React Router |
| **Database** | MySQL (Relational Data Model) |
| **DevOps** | Docker, Docker Compose, GitLab CI/CD |
| **Build Tools** | Gradle (Backend), NPM (Frontend) |

---

## System Architecture

The application follows a strictly layered **Monolithic Architecture** designed for maintainability and separation of concerns:

1.  **Controller Layer:** Handles incoming REST API requests.
2.  **Service Layer:** Executes business logic (e.g., enrollment validation, score calculation).
3.  **Repository Layer:** Manages data persistence using **Spring Data JPA**.

The frontend and backend are containerized separately but orchestrated together via Docker.

---

## Getting Started

### Prerequisites
* [Docker Desktop](https://www.docker.com/products/docker-desktop) installed.
* Git installed.

### Installation (Using Docker)
The easiest way to run the application is using the included `docker-compose` configuration.

1.  **Clone the repository**
    ```bash
    git clone [https://github.com/YOUR_GITHUB_USERNAME/uplifted-platform.git](https://github.com/YOUR_GITHUB_USERNAME/uplifted-platform.git)
    cd uplifted-platform
    ```

2.  **Build and Run**
    ```bash
    docker-compose up --build
    ```

3.  **Access the Application**
    * **Frontend:** `http://localhost:3000`
    * **Backend API:** `http://localhost:8080`

---

## Testing

The project emphasizes quality assurance through automated testing.

* **Unit Tests:** Backend logic is tested using **JUnit 5** and **Mockito**.
* **Integration Tests:** API endpoints are verified using Spring Boot Test.

To run backend tests manually:
```bash
./gradlew test
